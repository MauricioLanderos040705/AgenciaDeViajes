package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.*;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * DAO para las tablas `reservas`, `detalle_reserva_vuelos`,
 * `detalle_reserva_hoteles` y `detalle_reserva_autos`.
 *
 * No implementa GenericDAO directamente porque su operación principal
 * es una transacción multi-tabla (crearReserva), no un CRUD simple.
 */
public class ReservaDAO {

    private final Connection conn;

    public ReservaDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }


    /**
     * Persiste la reserva completa en un solo commit:
     *   INSERT reservas → INSERT detalles vuelo/hotel/auto
     *
     * @return id_reserva generado, o -1 si hubo error
     */
    public int crearReserva(Reserva r) {
        String sqlReserva = """
            INSERT INTO reservas
              (id_usuario, tipo_reserva, fecha_reserva, total_pagado, codigo_divisa)
            VALUES (?, ?, NOW(), ?, ?)
            """;
        try {
            conn.setAutoCommit(false);

            // 1. Insertar cabecera
            try (PreparedStatement ps = conn.prepareStatement(sqlReserva, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, r.getUsuario().getIdUsuario());
                ps.setString(2, r.getTipoReserva());
                ps.setBigDecimal(3, r.getTotalPagado());
                ps.setString(4, r.getCodigoDivisa());
                ps.executeUpdate();

                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) { conn.rollback(); return -1; }
                int idReserva = keys.getInt(1);
                r.setIdReserva(idReserva);

                // 2. Insertar detalles de vuelos
                for (DetalleReservaVuelo dv : r.getDetallesVuelo()) {
                    insertarDetalleVuelo(idReserva, dv);
                }

                // 3. Insertar detalles de hoteles
                for (DetalleReservaHotel dh : r.getDetallesHotel()) {
                    insertarDetalleHotel(idReserva, dh);
                }

                // 4. Insertar detalles de autos
                for (DetalleReservaAuto da : r.getDetallesAuto()) {
                    insertarDetalleAuto(idReserva, da);
                }

                conn.commit();
                return idReserva;
            }
        } catch (SQLException e) {
            System.err.println("[ReservaDAO.crearReserva] " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return -1;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }


    /**
     * Obtiene todas las reservas de un usuario con sus detalles.
     */
    public List<Reserva> obtenerPorUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        String sql = """
            SELECT r.*, u.nombre, u.apellido, u.correo
            FROM reservas r
            JOIN usuarios u ON r.id_usuario = u.id_usuario
            WHERE r.id_usuario = ?
            ORDER BY r.fecha_reserva DESC
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reserva reserva = mapearCabecera(rs);
                cargarDetalles(reserva);
                lista.add(reserva);
            }
        } catch (SQLException e) {
            System.err.println("[ReservaDAO.obtenerPorUsuario] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Retorna estadísticas para el Dashboard.
     * Clave → valor: "totalReservas", "totalIngresos",
     *                "totalPaquetes", "totalIndividuales",
     *                "reservasPorMes" (List<Object[]>),
     *                "topDestinos"   (List<Object[]>)
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        try (Statement st = conn.createStatement()) {

            // totales generales
            ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) AS total, SUM(total_pagado) AS ingresos FROM reservas");
            if (rs.next()) {
                stats.put("totalReservas", rs.getInt("total"));
                stats.put("totalIngresos", rs.getBigDecimal("ingresos"));
            }

            // por tipo
            rs = st.executeQuery(
                    "SELECT tipo_reserva, COUNT(*) AS cnt FROM reservas GROUP BY tipo_reserva");
            while (rs.next()) {
                if ("Paquete".equals(rs.getString("tipo_reserva")))
                    stats.put("totalPaquetes", rs.getInt("cnt"));
                else
                    stats.put("totalIndividuales", rs.getInt("cnt"));
            }

            // reservas por mes (últimos 6 meses)
            List<Object[]> porMes = new ArrayList<>();
            rs = st.executeQuery("""
                SELECT DATE_FORMAT(fecha_reserva, '%b %Y') AS mes,
                       COUNT(*) AS total
                FROM reservas
                WHERE fecha_reserva >= DATE_SUB(NOW(), INTERVAL 6 MONTH)
                GROUP BY mes
                ORDER BY MIN(fecha_reserva)
                """);
            while (rs.next())
                porMes.add(new Object[]{rs.getString("mes"), rs.getInt("total")});
            stats.put("reservasPorMes", porMes);

            // top 5 destinos
            List<Object[]> topDestinos = new ArrayList<>();
            rs = st.executeQuery("""
                SELECT cd.nombre AS destino, COUNT(*) AS cnt
                FROM detalle_reserva_vuelos dv
                JOIN catalogo_vuelos v ON dv.id_vuelo   = v.id_vuelo
                JOIN ciudades       cd ON v.id_ciudad_destino = cd.id_ciudad
                GROUP BY cd.nombre
                ORDER BY cnt DESC
                LIMIT 5
                """);
            while (rs.next())
                topDestinos.add(new Object[]{rs.getString("destino"), rs.getInt("cnt")});
            stats.put("topDestinos", topDestinos);

        } catch (SQLException e) {
            System.err.println("[ReservaDAO.obtenerEstadisticas] " + e.getMessage());
        }
        return stats;
    }


    private void insertarDetalleVuelo(int idReserva, DetalleReservaVuelo dv) throws SQLException {
        String sql = """
            INSERT INTO detalle_reserva_vuelos
              (id_reserva, id_vuelo, cantidad_pasajeros, subtotal)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, dv.getVuelo().getIdVuelo());
            ps.setInt(3, dv.getCantidadPasajeros());
            ps.setBigDecimal(4, dv.getSubtotal());
            ps.executeUpdate();
        }
    }

    private void insertarDetalleHotel(int idReserva, DetalleReservaHotel dh) throws SQLException {
        String sql = """
            INSERT INTO detalle_reserva_hoteles
              (id_reserva, id_hotel, check_in, check_out, cantidad_habitaciones, subtotal)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, dh.getHotel().getIdHotel());
            ps.setDate(3, Date.valueOf(dh.getCheckIn()));
            ps.setDate(4, Date.valueOf(dh.getCheckOut()));
            ps.setInt(5, dh.getCantidadHabitaciones());
            ps.setBigDecimal(6, dh.getSubtotal());
            ps.executeUpdate();
        }
    }

    private void insertarDetalleAuto(int idReserva, DetalleReservaAuto da) throws SQLException {
        String sql = """
            INSERT INTO detalle_reserva_autos
              (id_reserva, id_auto, fecha_inicio, fecha_fin, subtotal)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, da.getAuto().getIdAuto());
            ps.setDate(3, Date.valueOf(da.getFechaInicio()));
            ps.setDate(4, Date.valueOf(da.getFechaFin()));
            ps.setBigDecimal(5, da.getSubtotal());
            ps.executeUpdate();
        }
    }

    private Reserva mapearCabecera(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(rs.getInt("id_reserva"));
        r.setFolio(rs.getString("folio") != null ? rs.getString("folio") :
                String.format("#RVJ-%tY-%04d", rs.getTimestamp("fecha_reserva"), rs.getInt("id_reserva")));
        r.setTipoReserva(rs.getString("tipo_reserva"));
        r.setFechaReserva(rs.getTimestamp("fecha_reserva"));
        r.setTotalPagado(rs.getBigDecimal("total_pagado"));
        r.setCodigoDivisa(rs.getString("codigo_divisa"));

        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setCorreo(rs.getString("correo"));
        r.setUsuario(u);
        return r;
    }

    private void cargarDetalles(Reserva r) {
        // Detalles de vuelo
        String sqlV = """
            SELECT dv.*, v.id_vuelo FROM detalle_reserva_vuelos dv
            JOIN catalogo_vuelos v ON dv.id_vuelo = v.id_vuelo
            WHERE dv.id_reserva = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sqlV)) {
            ps.setInt(1, r.getIdReserva());
            ResultSet rs = ps.executeQuery();
            VueloDAO vueloDAO = new VueloDAO();
            while (rs.next()) {
                DetalleReservaVuelo dv = new DetalleReservaVuelo();
                dv.setIdDetalle(rs.getInt("id_detalle"));
                dv.setVuelo(vueloDAO.obtenerPorId(rs.getInt("id_vuelo")));
                dv.setCantidadPasajeros(rs.getInt("cantidad_pasajeros"));
                dv.setSubtotal(rs.getBigDecimal("subtotal"));
                r.getDetallesVuelo().add(dv);
            }
        } catch (SQLException e) {
            System.err.println("[ReservaDAO.cargarDetalles-vuelo] " + e.getMessage());
        }

        // Detalles de hotel
        String sqlH = "SELECT * FROM detalle_reserva_hoteles WHERE id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlH)) {
            ps.setInt(1, r.getIdReserva());
            ResultSet rs = ps.executeQuery();
            HotelDAO hotelDAO = new HotelDAO();
            while (rs.next()) {
                DetalleReservaHotel dh = new DetalleReservaHotel();
                dh.setIdDetalle(rs.getInt("id_detalle"));
                dh.setHotel(hotelDAO.obtenerPorId(rs.getInt("id_hotel")));
                dh.setCheckIn(rs.getDate("check_in").toLocalDate());
                dh.setCheckOut(rs.getDate("check_out").toLocalDate());
                dh.setCantidadHabitaciones(rs.getInt("cantidad_habitaciones"));
                dh.setSubtotal(rs.getBigDecimal("subtotal"));
                r.getDetallesHotel().add(dh);
            }
        } catch (SQLException e) {
            System.err.println("[ReservaDAO.cargarDetalles-hotel] " + e.getMessage());
        }

        // Detalles de auto
        String sqlA = "SELECT * FROM detalle_reserva_autos WHERE id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlA)) {
            ps.setInt(1, r.getIdReserva());
            ResultSet rs = ps.executeQuery();
            AutoDAO autoDAO = new AutoDAO();
            while (rs.next()) {
                DetalleReservaAuto da = new DetalleReservaAuto();
                da.setIdDetalle(rs.getInt("id_detalle"));
                da.setAuto(autoDAO.obtenerPorId(rs.getInt("id_auto")));
                da.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
                da.setFechaFin(rs.getDate("fecha_fin").toLocalDate());
                da.setSubtotal(rs.getBigDecimal("subtotal"));
                r.getDetallesAuto().add(da);
            }
        } catch (SQLException e) {
            System.err.println("[ReservaDAO.cargarDetalles-auto] " + e.getMessage());
        }
    }
}

