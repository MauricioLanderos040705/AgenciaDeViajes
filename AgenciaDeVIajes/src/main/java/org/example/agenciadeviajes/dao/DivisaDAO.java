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
        // Nota: Divisa usa codigo_iso (String) como clave primaria, no int id
        // Este método no se usa; usar obtenerPorCodigo(String) en su lugar
        return null;
    }

    /**
     * Obtiene una divisa por su código ISO (método correcto para Divisa)
     */
    public Divisa obtenerPorIdCodigo(String codigo) {
        String sql = "SELECT * FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.obtenerPorIdCodigo] " + e.getMessage());
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
            String codigo = d.getCodigo() != null ? d.getCodigo().trim() : "";
            String nombre = d.getNombre() != null ? d.getNombre().trim() : "";
            String simbolo = d.getSimbolo() != null ? d.getSimbolo().trim() : "";

            System.out.println("[DivisaDAO.actualizar] SQL: " + sql);
            System.out.println("[DivisaDAO.actualizar] Parámetros (TRIMMED): nombre=" + nombre + ", simbolo=" + simbolo + ", codigo=" + codigo);

            ps.setString(1, nombre);
            ps.setString(2, simbolo);
            ps.setString(3, codigo);

            int filasActualizadas = ps.executeUpdate();
            System.out.println("[DivisaDAO.actualizar] Filas actualizadas: " + filasActualizadas);

            return filasActualizadas > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.actualizar] ERROR SQL: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        // Nota: Divisa usa codigo_iso (String) como clave primaria, no int id
        // Usar eliminarPorCodigo(String) en su lugar
        return false;
    }

    /**
     * Elimina una divisa por su código ISO (método correcto para Divisa)
     */
    public boolean eliminarPorCodigo(String codigo) {
        String sql = "DELETE FROM divisas WHERE codigo_iso = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Limpiar espacios en blanco
            String codigoLimpio = codigo != null ? codigo.trim() : "";

            System.out.println("[DivisaDAO.eliminarPorCodigo] SQL: " + sql);
            System.out.println("[DivisaDAO.eliminarPorCodigo] Parámetro: codigo='" + codigo + "'");
            System.out.println("[DivisaDAO.eliminarPorCodigo] Parámetro TRIMMED: codigo='" + codigoLimpio + "'");

            ps.setString(1, codigoLimpio);

            int filasEliminadas = ps.executeUpdate();
            System.out.println("[DivisaDAO.eliminarPorCodigo] Filas eliminadas: " + filasEliminadas);

            return filasEliminadas > 0;
        } catch (SQLException e) {
            System.err.println("[DivisaDAO.eliminarPorCodigo] ERROR SQL: " + e.getMessage());
            e.printStackTrace();
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
        String codigo = rs.getString("codigo_iso");
        String nombre = rs.getString("nombre");
        String simbolo = rs.getString("simbolo");

        // DEBUG: Ver qué se obtiene de BD
        System.out.println("[DivisaDAO.mapear] Mapeando divisa:");
        System.out.println("  - Código DB: '" + codigo + "' (length=" + (codigo != null ? codigo.length() : "null") + ")");
        System.out.println("  - Nombre DB: '" + nombre + "'");
        System.out.println("  - Símbolo DB: '" + simbolo + "'");

        return new Divisa(
                0, // idDivisa no se usa en BD
                nombre,
                simbolo,
                codigo,
                null // tasaCambio no existe en BD pero está en modelo
        );
    }
}
