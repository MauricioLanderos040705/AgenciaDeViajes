package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `categorias_autos`.
 * Gestiona las categorías de automóviles (Económico, SUV, Lujo, etc.)
 */
public class CategoriaAutoDAO implements GenericDAO<String> {

    private final Connection conn;

    public CategoriaAutoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    /**
     * Inserta una nueva categoría
     */
    @Override
    public boolean insertar(String categoria) {
        String sql = "INSERT INTO categorias_autos (nombre) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene una categoría por ID (aunque aquí usamos nombre)
     */
    @Override
    public String obtenerPorId(int id) {
        String sql = "SELECT nombre FROM categorias_autos WHERE id_categoria = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("nombre");
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene todas las categorías
     */
    @Override
    public List<String> obtenerTodos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nombre FROM categorias_autos ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(rs.getString("nombre"));
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza una categoría
     */
    @Override
    public boolean actualizar(String categoria) {
        // Para categorías, usar el método específico actualizar(int, String)
        return false;
    }

    /**
     * Actualiza una categoría por ID
     */
    public boolean actualizar(int id, String nuevoNombre) {
        String sql = "UPDATE categorias_autos SET nombre=? WHERE id_categoria=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina una categoría por ID
     */
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias_autos WHERE id_categoria = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el ID de una categoría por nombre
     */
    public int obtenerIdPorNombre(String nombre) {
        String sql = "SELECT id_categoria FROM categorias_autos WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id_categoria");
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.obtenerIdPorNombre] " + e.getMessage());
        }
        return -1;
    }

    /**
     * Verifica si una categoría existe
     */
    public boolean existe(String nombre) {
        String sql = "SELECT COUNT(*) FROM categorias_autos WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[CategoriaAutoDAO.existe] " + e.getMessage());
        }
        return false;
    }
}
