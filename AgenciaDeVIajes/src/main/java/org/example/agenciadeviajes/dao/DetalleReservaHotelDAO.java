package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.DetalleReservaHotel;
import org.example.agenciadeviajes.model.Hotel;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `detalle_reserva_hoteles`.
 * Gestiona los detalles de hoteles en una reserva.
 */
public class DetalleReservaHotelDAO implements GenericDAO<DetalleReservaHotel> {

    private final Connection conn;
    private final HotelDAO hotelDAO;

    public DetalleReservaHotelDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
        this.hotelDAO = new HotelDAO();
    }

    @Override
    public boolean insertar(DetalleReservaHotel d) {
        String sql = """
            INSERT INTO detalle_reserva_hoteles
              (id_reserva, id_hotel, fecha_check_in, fecha_check_out, cantidad_habitaciones, subtotal)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 0); // id_reserva se asigna desde ReservaDAO
            ps.setInt(2, d.getHotel().getIdHotel());
            ps.setDate(3, java.sql.Date.valueOf(d.getCheckIn()));
            ps.setDate(4, java.sql.Date.valueOf(d.getCheckOut()));
            ps.setInt(5, d.getCantidadHabitaciones());
            ps.setBigDecimal(6, d.getSubtotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public DetalleReservaHotel obtenerPorId(int id) {
        // Esta tabla usa clave compuesta, usar obtenerPorReserva en su lugar
        return null;
    }

    @Override
    public List<DetalleReservaHotel> obtenerTodos() {
        List<DetalleReservaHotel> lista = new ArrayList<>();
        String sql = buildSelectBase();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(DetalleReservaHotel d) {
        String sql = """
            UPDATE detalle_reserva_hoteles
            SET fecha_check_in=?, fecha_check_out=?, cantidad_habitaciones=?, subtotal=?
            WHERE id_reserva=? AND id_hotel=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(d.getCheckIn()));
            ps.setDate(2, java.sql.Date.valueOf(d.getCheckOut()));
            ps.setInt(3, d.getCantidadHabitaciones());
            ps.setBigDecimal(4, d.getSubtotal());
            ps.setInt(5, 0); // id_reserva
            ps.setInt(6, d.getHotel().getIdHotel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Esta tabla usa clave compuesta
        return false;
    }

    /**
     * Obtiene todos los hoteles de una reserva
     */
    public List<DetalleReservaHotel> obtenerPorReserva(int idReserva) {
        List<DetalleReservaHotel> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE drh.id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.obtenerPorReserva] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina un detalle de reserva de hotel
     */
    public boolean eliminarDetalle(int idReserva, int idHotel) {
        String sql = "DELETE FROM detalle_reserva_hoteles WHERE id_reserva = ? AND id_hotel = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, idHotel);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.eliminarDetalle] " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina todos los detalles de hotel de una reserva
     */
    public boolean eliminarPorReserva(int idReserva) {
        String sql = "DELETE FROM detalle_reserva_hoteles WHERE id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.eliminarPorReserva] " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca hoteles reservados en un rango de fechas
     */
    public List<DetalleReservaHotel> obtenerPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<DetalleReservaHotel> lista = new ArrayList<>();
        String sql = buildSelectBase() + """
            WHERE (drh.fecha_check_in <= ? AND drh.fecha_check_out >= ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaFin));
            ps.setDate(2, java.sql.Date.valueOf(fechaInicio));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaHotelDAO.obtenerPorFechas] " + e.getMessage());
        }
        return lista;
    }

    private String buildSelectBase() {
        return """
            SELECT drh.*, h.*
            FROM detalle_reserva_hoteles drh
            JOIN catalogo_hoteles h ON drh.id_hotel = h.id_hotel
            """;
    }

    private DetalleReservaHotel mapear(ResultSet rs) throws SQLException {
        Hotel hotel = hotelDAO.obtenerPorId(rs.getInt("id_hotel"));
        LocalDate checkIn = rs.getDate("fecha_check_in").toLocalDate();
        LocalDate checkOut = rs.getDate("fecha_check_out").toLocalDate();
        DetalleReservaHotel detalle = new DetalleReservaHotel(
                hotel, checkIn, checkOut, rs.getInt("cantidad_habitaciones")
        );
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        return detalle;
    }
}
