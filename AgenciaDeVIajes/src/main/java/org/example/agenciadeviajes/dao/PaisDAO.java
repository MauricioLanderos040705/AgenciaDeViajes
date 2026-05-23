package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Pais;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `paises`.
 * Gestiona los países del sistema.
 */
public class PaisDAO implements GenericDAO<Pais> {

    private final Connection conn;

    public PaisDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Pais p) {
        String sql = "INSERT INTO paises (nombre, codigo_iso) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCodigoIso());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaisDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Pais obtenerPorId(int id) {
        String sql = "SELECT * FROM paises WHERE id_pais = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[PaisDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pais> obtenerTodos() {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT * FROM paises ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[PaisDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Pais p) {
        String sql = "UPDATE paises SET nombre=?, codigo_iso=? WHERE id_pais=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getCodigoIso());
            ps.setInt(3, p.getIdPais());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaisDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM paises WHERE id_pais = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[PaisDAO.eliminar] " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un país por su código ISO
     */
    public Pais obtenerPorCodigoIso(String codigoIso) {
        String sql = "SELECT * FROM paises WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoIso);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[PaisDAO.obtenerPorCodigoIso] " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un país por su nombre
     */
    public Pais obtenerPorNombre(String nombre) {
        String sql = "SELECT * FROM paises WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[PaisDAO.obtenerPorNombre] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si un país existe por código ISO
     */
    public boolean codigoExiste(String codigo) {
        String sql = "SELECT COUNT(*) FROM paises WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[PaisDAO.codigoExiste] " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si un país existe por nombre
     */
    public boolean nombreExiste(String nombre) {
        String sql = "SELECT COUNT(*) FROM paises WHERE nombre = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[PaisDAO.nombreExiste] " + e.getMessage());
        }
        return false;
    }

    private Pais mapear(ResultSet rs) throws SQLException {
        return new Pais(
                rs.getInt("id_pais"),
                rs.getString("nombre"),
                rs.getString("codigo_iso")
        );
    }
}
