package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Auto;
import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.ModeloAuto;
import org.example.agenciadeviajes.model.Pais;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `catalogo_autos`.
 * Implementa GenericDAO<Auto> + búsqueda por ciudad y fechas de renta.
 */
public class AutoDAO implements GenericDAO<Auto> {

    private final Connection conn;

    public AutoDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }

    @Override
    public boolean insertar(Auto a) {
        String sql = """
            INSERT INTO catalogo_autos
              (id_modelo, id_proveedor, id_ciudad_disponibilidad,
               precio_dia, codigo_divisa)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getModeloAuto().getIdModelo());
            ps.setInt(2, a.getIdProveedor());               // FK int, no String
            ps.setInt(3, a.getCiudadDisponibilidad().getIdCiudad());
            ps.setBigDecimal(4, a.getPrecioDia());
            ps.setString(5, a.getCodigoDivisa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AutoDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Auto obtenerPorId(int id) {
        String sql = buildSelectBase() + " WHERE a.id_auto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[AutoDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Auto> obtenerTodos() {
        List<Auto> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(buildSelectBase() + " ORDER BY cat.nombre, a.precio_dia")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[AutoDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Auto a) {
        String sql = "UPDATE catalogo_autos SET precio_dia=? WHERE id_auto=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, a.getPrecioDia());
            ps.setInt(2, a.getIdAuto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AutoDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM catalogo_autos WHERE id_auto = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[AutoDAO.eliminar] " + e.getMessage());
            return false;
        }
    }


    /**
     * Busca autos disponibles en una ciudad para un rango de fechas.
     * Usado por BusquedaAutoStrategy.
     */
    public List<Auto> buscarAutos(int idCiudad, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Auto> lista = new ArrayList<>();
        String sql = buildSelectBase() + """
             WHERE a.id_ciudad_disponibilidad = ?
             ORDER BY cat.nombre, a.precio_dia
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCiudad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[AutoDAO.buscarAutos] " + e.getMessage());
        }
        return lista;
    }


    private String buildSelectBase() {
        return """
            SELECT a.*,
                   m.id_modelo, m.marca, m.nombre AS nombre_modelo,
                   cat.nombre  AS categoria,
                   prov.nombre AS nombre_proveedor,
                   c.id_ciudad, c.nombre AS nombre_ciudad, c.codigo_iata,
                   p.id_pais, p.nombre AS nombre_pais, p.codigo_iso
            FROM catalogo_autos a
            JOIN modelos_autos     m    ON a.id_modelo                = m.id_modelo
            JOIN categorias_autos  cat  ON m.id_categoria             = cat.id_categoria
            JOIN proveedores_autos prov ON a.id_proveedor             = prov.id_proveedor
            JOIN ciudades          c    ON a.id_ciudad_disponibilidad = c.id_ciudad
            JOIN paises            p    ON c.id_pais                  = p.id_pais
            """;
    }

    private Auto mapear(ResultSet rs) throws SQLException {
        ModeloAuto modelo = new ModeloAuto(
                rs.getInt("id_modelo"),
                rs.getString("marca"),
                rs.getString("nombre_modelo"),  // m.nombre
                rs.getString("categoria")        // cat.nombre
        );

        Pais pais   = new Pais(rs.getInt("id_pais"), rs.getString("nombre_pais"), rs.getString("codigo_iso"));
        Ciudad ciudad = new Ciudad(rs.getInt("id_ciudad"), rs.getString("nombre_ciudad"), rs.getString("codigo_iata"), pais);

        return new Auto(
                rs.getInt("id_auto"),
                modelo,
                rs.getString("nombre_proveedor"), // nombre del JOIN a proveedores_autos
                rs.getInt("id_proveedor"),         // FK para insertar
                ciudad,
                rs.getBigDecimal("precio_dia"),
                rs.getString("codigo_divisa")
        );
    }
}
