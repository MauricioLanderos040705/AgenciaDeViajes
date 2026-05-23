package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `proveedores_autos`.
 * Gestiona los proveedores de automóviles de renta.
 */
public class ProveedorAutoDAO implements GenericDAO<String> {

    private final Connection conn;

    public ProveedorAutoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    /**
     * Inserta un nuevo proveedor
     */
    @Override
    public boolean insertar(String nombreProveedor) {
        String sql = "INSERT INTO proveedores_autos (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombreProveedor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un proveedor por ID
     */
    @Override
    public String obtenerPorId(int id) {
        String sql = "SELECT nombre FROM proveedores_autos WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre");
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todos los proveedores
     */
    @Override
    public List<String> obtenerTodos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM proveedores_autos ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getString("nombre"));
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza un proveedor
     */
    @Override
    public boolean actualizar(String nuevoNombre) {
        // Usar el método específico actualizar(int, String)
        return false;
    }

    /**
     * Actualiza un proveedor por ID
     */
    public boolean actualizar(int id, String nuevoNombre) {
        String sql = "UPDATE proveedores_autos SET nombre=? WHERE id_proveedor=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un proveedor por ID
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores_autos WHERE id_proveedor = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el ID de un proveedor por nombre
     */
    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_proveedor FROM proveedores_autos WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_proveedor");
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.obtenerIdPorNombre] " + e.getMessage());
        }
        return -1;
    }

    /**
     * Verifica si un proveedor existe
     */
    public boolean existe(String nombre) {
        String sql = "SELECT COUNT(*) FROM proveedores_autos WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[ProveedorAutoDAO.existe] " + e.getMessage());
        }
        return false;
    }
}
