package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Divisa;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `divisas`.
 * Gestiona las diferentes monedas/divisas del sistema.
 */
public class DivisaDAO implements GenericDAO<Divisa> {

    private final Connection conn;

    public DivisaDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Divisa d) {
        String sql = "INSERT INTO divisas (codigo_iso, nombre, simbolo) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getCodigo());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getSimbolo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Divisa obtenerPorId(int id) {
        String sql = "SELECT * FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Divisa> obtenerTodos() {
        List<Divisa> lista = new ArrayList<>();
        String sql = "SELECT * FROM divisas ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Divisa d) {
        String sql = "UPDATE divisas SET nombre=?, simbolo=? WHERE codigo_iso=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getSimbolo());
            ps.setString(3, d.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, String.valueOf(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una divisa por su código ISO
     */
    public Divisa obtenerPorCodigo(String codigo) {
        String sql = "SELECT * FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.obtenerPorCodigo] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si un código ISO existe
     */
    public boolean codigoExiste(String codigo) {
        String sql = "SELECT COUNT(*) FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.codigoExiste] " + e.getMessage());
        }
        return false;
    }

    private Divisa mapear(ResultSet rs) throws SQLException {
        return new Divisa(
                0, // idDivisa no se usa en BD
                rs.getString("nombre"),
                rs.getString("simbolo"),
                rs.getString("codigo_iso"),
                null // tasaCambio no existe en BD pero está en modelo
        );
    }
}
