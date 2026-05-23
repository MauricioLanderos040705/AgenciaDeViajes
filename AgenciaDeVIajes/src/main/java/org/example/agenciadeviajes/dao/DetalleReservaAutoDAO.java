package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.DetalleReservaAuto;
import org.example.agenciadeviajes.model.Auto;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `detalle_reserva_autos`.
 * Gestiona los detalles de autos en una reserva.
 */
public class DetalleReservaAutoDAO implements GenericDAO<DetalleReservaAuto> {

    private final Connection conn;
    private final AutoDAO autoDAO;

    public DetalleReservaAutoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
        this.autoDAO = new AutoDAO();
    }

    @Override
    public boolean insertar(DetalleReservaAuto d) {
        String sql = """
            INSERT INTO detalle_reserva_autos
              (id_reserva, id_auto, fecha_inicio, fecha_fin, subtotal)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 0); // id_reserva se asigna desde ReservaDAO
            ps.setInt(2, d.getAuto().getIdAuto());
            ps.setDate(3, java.sql.Date.valueOf(d.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(d.getFechaFin()));
            ps.setBigDecimal(5, d.getSubtotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public DetalleReservaAuto obtenerPorId(int id) {
        // Esta tabla usa clave compuesta, usar obtenerPorReserva en su lugar
        return null;
    }

    @Override
    public List<DetalleReservaAuto> obtenerTodos() {
        List<DetalleReservaAuto> lista = new ArrayList<>();
        String sql = buildSelectBase();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(DetalleReservaAuto d) {
        String sql = """
            UPDATE detalle_reserva_autos
            SET fecha_inicio=?, fecha_fin=?, subtotal=?
            WHERE id_reserva=? AND id_auto=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(d.getFechaInicio()));
            ps.setDate(2, java.sql.Date.valueOf(d.getFechaFin()));
            ps.setBigDecimal(3, d.getSubtotal());
            ps.setInt(4, 0); // id_reserva
            ps.setInt(5, d.getAuto().getIdAuto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Esta tabla usa clave compuesta
        return false;
    }

    /**
     * Obtiene todos los autos de una reserva
     */
    public List<DetalleReservaAuto> obtenerPorReserva(int idReserva) {
        List<DetalleReservaAuto> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE dra.id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.obtenerPorReserva] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina un detalle de reserva de auto
     */
    public boolean eliminarDetalle(int idReserva, int idAuto) {
        String sql = "DELETE FROM detalle_reserva_autos WHERE id_reserva = ? AND id_auto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, idAuto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.eliminarDetalle] " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina todos los detalles de auto de una reserva
     */
    public boolean eliminarPorReserva(int idReserva) {
        String sql = "DELETE FROM detalle_reserva_autos WHERE id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.eliminarPorReserva] " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca autos reservados en un rango de fechas
     */
    public List<DetalleReservaAuto> obtenerPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<DetalleReservaAuto> lista = new ArrayList<>();
        String sql = buildSelectBase() + """
            WHERE (dra.fecha_inicio <= ? AND dra.fecha_fin >= ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(fechaFin));
            ps.setDate(2, java.sql.Date.valueOf(fechaInicio));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaAutoDAO.obtenerPorFechas] " + e.getMessage());
        }
        return lista;
    }

    private String buildSelectBase() {
        return """
            SELECT dra.*, ca.*
            FROM detalle_reserva_autos dra
            JOIN catalogo_autos ca ON dra.id_auto = ca.id_auto
            """;
    }

    private DetalleReservaAuto mapear(ResultSet rs) throws SQLException {
        Auto auto = autoDAO.obtenerPorId(rs.getInt("id_auto"));
        LocalDate fechaInicio = rs.getDate("fecha_inicio").toLocalDate();
        LocalDate fechaFin = rs.getDate("fecha_fin").toLocalDate();
        DetalleReservaAuto detalle = new DetalleReservaAuto(auto, fechaInicio, fechaFin);
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        return detalle;
    }
}
