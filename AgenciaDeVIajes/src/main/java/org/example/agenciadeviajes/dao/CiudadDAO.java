package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Pais;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `ciudades`.
 * Gestiona las ciudades del sistema con relación a países.
 */
public class CiudadDAO implements GenericDAO<Ciudad> {

    private final Connection conn;

    public CiudadDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Ciudad c) {
        String sql = "INSERT INTO ciudades (id_pais, nombre, codigo_iata) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getPais().getIdPais());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getCodigoIata());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Ciudad obtenerPorId(int id) {
        String sql = buildSelectBase() + " WHERE c.id_ciudad = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Ciudad> obtenerTodos() {
        List<Ciudad> lista = new ArrayList<>();
        String sql = buildSelectBase() + " ORDER BY c.nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Ciudad c) {
        String sql = "UPDATE ciudades SET nombre=?, codigo_iata=?, id_pais=? WHERE id_ciudad=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getCodigoIata());
            ps.setInt(3, c.getPais().getIdPais());
            ps.setInt(4, c.getIdCiudad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM ciudades WHERE id_ciudad = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las ciudades de un país
     */
    public List<Ciudad> obtenerPorPais(int idPais) {
        List<Ciudad> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE c.id_pais = ? ORDER BY c.nombre";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPais);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.obtenerPorPais] " + e.getMessage());
        }
        return lista;
    }

    /**
     * Busca una ciudad por su código IATA
     */
    public Ciudad obtenerPorCodigoIata(String codigoIata) {
        String sql = buildSelectBase() + " WHERE c.codigo_iata = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoIata);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.obtenerPorCodigoIata] " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca una ciudad por nombre
     */
    public Ciudad obtenerPorNombre(String nombre) {
        String sql = buildSelectBase() + " WHERE c.nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.obtenerPorNombre] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si una ciudad existe por código IATA
     */
    public boolean codigoIataExiste(String codigo) {
        String sql = "SELECT COUNT(*) FROM ciudades WHERE codigo_iata = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[CiudadDAO.codigoIataExiste] " + e.getMessage());
        }
        return false;
    }

    private String buildSelectBase() {
        return """
            SELECT c.*,
                   p.id_pais, p.nombre AS nombre_pais, p.codigo_iso
            FROM ciudades c
            JOIN paises p ON c.id_pais = p.id_pais
            """;
    }

    private Ciudad mapear(ResultSet rs) throws SQLException {
        Pais pais = new Pais(
                rs.getInt("id_pais"),
                rs.getString("nombre_pais"),
                rs.getString("codigo_iso")
        );
        return new Ciudad(
                rs.getInt("id_ciudad"),
                rs.getString("nombre"),
                rs.getString("codigo_iata"),
                pais
        );
    }
}
