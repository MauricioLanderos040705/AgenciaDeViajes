package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Aerolinea;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `aerolineas`.
 * Implementa GenericDAO<Aerolinea> + búsquedas específicas.
 */
public class AerolineaDAO implements GenericDAO<Aerolinea> {

    private final Connection conn;

    public AerolineaDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Aerolinea a) {
        String sql = "INSERT INTO aerolineas (nombre, codigo_iata) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getCodigoIata());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Aerolinea obtenerPorId(int id) {
        String sql = "SELECT * FROM aerolineas WHERE id_aerolinea = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Aerolinea> obtenerTodos() {
        List<Aerolinea> lista = new ArrayList<>();
        String sql = "SELECT * FROM aerolineas ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Aerolinea a) {
        String sql = "UPDATE aerolineas SET nombre=?, codigo_iata=? WHERE id_aerolinea=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setString(2, a.getCodigoIata());
            ps.setInt(3, a.getIdAerolinea());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM aerolineas WHERE id_aerolinea = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca una aerolínea por su código IATA
     */
    public Aerolinea obtenerPorCodigoIata(String codigoIata) {
        String sql = "SELECT * FROM aerolineas WHERE codigo_iata = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoIata);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.obtenerPorCodigoIata] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si una aerolínea existe por código IATA (para evitar duplicados)
     */
    public boolean codigoIataExiste(String codigoIata) {
        String sql = "SELECT COUNT(*) FROM aerolineas WHERE codigo_iata = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoIata);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.codigoIataExiste] " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si una aerolínea existe por nombre (para evitar duplicados)
     */
    public boolean nombreExiste(String nombre) {
        String sql = "SELECT COUNT(*) FROM aerolineas WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[AerolineaDAO.nombreExiste] " + e.getMessage());
        }
        return false;
    }

    private Aerolinea mapear(ResultSet rs) throws SQLException {
        return new Aerolinea(
                rs.getInt("id_aerolinea"),
                rs.getString("nombre"),
                rs.getString("codigo_iata")
        );
    }
}
