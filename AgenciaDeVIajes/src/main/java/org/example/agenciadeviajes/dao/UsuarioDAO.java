package org.example.agenciadeviajes.dao;


import org.example.agenciadeviajes.model.Usuario;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `usuarios`.
 * Implementa GenericDAO<Usuario> + métodos específicos de autenticación.
 */
public class UsuarioDAO implements GenericDAO<Usuario> {

    private final Connection conn;

    public UsuarioDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }


    @Override
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, correo, contrasenia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasenia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nombre";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre=?, apellido=?, correo=?, contrasenia=? WHERE id_usuario=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasenia());
            ps.setInt(5, u.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.eliminar] " + e.getMessage());
            return false;
        }
    }


    /**
     * Valida las credenciales de login.
     * @param correo  correo del usuario
     * @param hash    contraseña ya hasheada con SHA-256
     * @return Usuario si las credenciales son correctas, null si no
     */
    public Usuario validarLogin(String correo, String hash) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasenia = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, hash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.validarLogin] " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si un correo ya está registrado (para evitar duplicados).
     */
    public boolean correoExiste(String correo) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO.correoExiste] " + e.getMessage());
        }
        return false;
    }


    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo"),
                rs.getString("contrasenia"),
                rs.getTimestamp("fecha_registro")
        );
    }
}
