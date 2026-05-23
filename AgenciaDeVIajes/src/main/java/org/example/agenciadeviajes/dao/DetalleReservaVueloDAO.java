package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.DetalleReservaVuelo;
import org.example.agenciadeviajes.model.Vuelo;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `detalle_reserva_vuelos`.
 * Gestiona los detalles de vuelos en una reserva.
 */
public class DetalleReservaVueloDAO implements GenericDAO<DetalleReservaVuelo> {

    private final Connection conn;
    private final VueloDAO vueloDAO;

    public DetalleReservaVueloDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
        this.vueloDAO = new VueloDAO();
    }

    @Override
    public boolean insertar(DetalleReservaVuelo d) {
        String sql = """
            INSERT INTO detalle_reserva_vuelos
              (id_reserva, id_vuelo, cantidad_pasajeros, subtotal)
            VALUES (?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, 0); // id_reserva se asigna desde ReservaDAO
            ps.setInt(2, d.getVuelo().getIdVuelo());
            ps.setInt(3, d.getCantidadPasajeros());
            ps.setBigDecimal(4, d.getSubtotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public DetalleReservaVuelo obtenerPorId(int id) {
        // Esta tabla usa clave compuesta, usar obtenerPorReserva en su lugar
        return null;
    }

    @Override
    public List<DetalleReservaVuelo> obtenerTodos() {
        List<DetalleReservaVuelo> lista = new ArrayList<>();
        String sql = buildSelectBase();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(DetalleReservaVuelo d) {
        String sql = """
            UPDATE detalle_reserva_vuelos
            SET cantidad_pasajeros=?, subtotal=?
            WHERE id_reserva=? AND id_vuelo=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidadPasajeros());
            ps.setBigDecimal(2, d.getSubtotal());
            ps.setInt(3, 0); // id_reserva
            ps.setInt(4, d.getVuelo().getIdVuelo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Esta tabla usa clave compuesta
        return false;
    }

    /**
     * Obtiene todos los vuelos de una reserva
     */
    public List<DetalleReservaVuelo> obtenerPorReserva(int idReserva) {
        List<DetalleReservaVuelo> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE drv.id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.obtenerPorReserva] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Elimina un detalle de reserva de vuelo
     */
    public boolean eliminarDetalle(int idReserva, int idVuelo) {
        String sql = "DELETE FROM detalle_reserva_vuelos WHERE id_reserva = ? AND id_vuelo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            ps.setInt(2, idVuelo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.eliminarDetalle] " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina todos los detalles de vuelo de una reserva
     */
    public boolean eliminarPorReserva(int idReserva) {
        String sql = "DELETE FROM detalle_reserva_vuelos WHERE id_reserva = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idReserva);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DetalleReservaVueloDAO.eliminarPorReserva] " + e.getMessage());
            return false;
        }
    }

    private String buildSelectBase() {
        return """
            SELECT drv.*, v.*
            FROM detalle_reserva_vuelos drv
            JOIN catalogo_vuelos v ON drv.id_vuelo = v.id_vuelo
            """;
    }

    private DetalleReservaVuelo mapear(ResultSet rs) throws SQLException {
        Vuelo vuelo = vueloDAO.obtenerPorId(rs.getInt("id_vuelo"));
        DetalleReservaVuelo detalle = new DetalleReservaVuelo(vuelo, rs.getInt("cantidad_pasajeros"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        return detalle;
    }
}
