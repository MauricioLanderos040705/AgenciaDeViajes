package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.ModeloAuto;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `modelos_autos`.
 * Gestiona los modelos de automóviles (marca, modelo, categoría).
 */
public class ModeloAutoDAO implements GenericDAO<ModeloAuto> {

    private final Connection conn;

    public ModeloAutoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(ModeloAuto m) {
        String sql = """
            INSERT INTO modelos_autos (id_categoria, nombre, marca)
            VALUES ((SELECT id_categoria FROM categorias_autos WHERE nombre = ?), ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getCategoria());
            ps.setString(2, m.getModelo());
            ps.setString(3, m.getMarca());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public ModeloAuto obtenerPorId(int id) {
        String sql = buildSelectBase() + " WHERE ma.id_modelo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ModeloAuto> obtenerTodos() {
        List<ModeloAuto> lista = new ArrayList<>();
        String sql = buildSelectBase() + " ORDER BY ma.marca, ma.nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(ModeloAuto m) {
        String sql = """
            UPDATE modelos_autos
            SET nombre=?, marca=?, id_categoria=(SELECT id_categoria FROM categorias_autos WHERE nombre = ?)
            WHERE id_modelo=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getModelo());
            ps.setString(2, m.getMarca());
            ps.setString(3, m.getCategoria());
            ps.setInt(4, m.getIdModelo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM modelos_autos WHERE id_modelo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los modelos de una categoría
     */
    public List<ModeloAuto> obtenerPorCategoria(String categoria) {
        List<ModeloAuto> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE ca.nombre = ? ORDER BY ma.marca, ma.nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.obtenerPorCategoria] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca un modelo por marca y nombre
     */
    public ModeloAuto obtenerPorMarcaYModelo(String marca, String modelo) {
        String sql = buildSelectBase() + " WHERE ma.marca = ? AND ma.nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marca);
            ps.setString(2, modelo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.obtenerPorMarcaYModelo] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si un modelo existe
     */
    public boolean existe(String marca, String modelo) {
        String sql = "SELECT COUNT(*) FROM modelos_autos WHERE marca = ? AND nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marca);
            ps.setString(2, modelo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[ModeloAutoDAO.existe] " + e.getMessage());
        }
        return false;
    }

    private String buildSelectBase() {
        return """
            SELECT ma.id_modelo, ma.nombre, ma.marca,
                   ca.id_categoria, ca.nombre AS nombre_categoria
            FROM modelos_autos ma
            JOIN categorias_autos ca ON ma.id_categoria = ca.id_categoria
            """;
    }

    private ModeloAuto mapear(ResultSet rs) throws SQLException {
        return new ModeloAuto(
                rs.getInt("id_modelo"),
                rs.getString("marca"),
                rs.getString("nombre"),
                rs.getString("nombre_categoria")
        );
    }
}
