package org.example.agenciadeviajes.dao;

import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Hotel;
import org.example.agenciadeviajes.model.Pais;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `catalogo_hoteles`.
 * Implementa GenericDAO<Hotel> + búsqueda por ciudad y fechas.
 */
public class HotelDAO implements GenericDAO<Hotel> {

    private final Connection conn;

    public HotelDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }


    @Override
    public boolean insertar(Hotel h) {
        String sql = """
            INSERT INTO catalogo_hoteles
              (nombre, id_ciudad, estrellas, precio_noche,
               habitaciones_disponibles, codigo_divisa)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getNombre());
            ps.setInt(2, h.getCiudad().getIdCiudad());
            ps.setInt(3, h.getEstrellas());
            ps.setBigDecimal(4, h.getPrecioNoche());
            ps.setInt(5, h.getHabitacionesDisponibles());
            ps.setString(6, h.getCodigoDivisa());   // era índice 7 (incorrecto)
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[HotelDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Hotel obtenerPorId(int id) {
        String sql = buildSelectBase() + " WHERE h.id_hotel = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[HotelDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Hotel> obtenerTodos() {
        List<Hotel> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(buildSelectBase() + " ORDER BY h.estrellas DESC, h.nombre")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[HotelDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Hotel h) {
        String sql = """
            UPDATE catalogo_hoteles
            SET precio_noche=?, habitaciones_disponibles=?, estrellas=?
            WHERE id_hotel=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, h.getPrecioNoche());
            ps.setInt(2, h.getHabitacionesDisponibles());
            ps.setInt(3, h.getEstrellas());
            ps.setInt(4, h.getIdHotel());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[HotelDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM catalogo_hoteles WHERE id_hotel = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[HotelDAO.eliminar] " + e.getMessage());
            return false;
        }
    }


    /**
     * Busca hoteles con habitaciones disponibles en una ciudad y rango de fechas.
     * Usado por BusquedaHotelStrategy.
     */
    public List<Hotel> buscarHoteles(int idCiudad, LocalDate checkIn, LocalDate checkOut) {
        List<Hotel> lista = new ArrayList<>();
        // Hoteles que NO tienen todas sus habitaciones ocupadas en ese rango
        String sql = buildSelectBase() + """
             WHERE h.id_ciudad = ?
               AND h.habitaciones_disponibles > 0
             ORDER BY h.estrellas DESC, h.precio_noche
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCiudad);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[HotelDAO.buscarHoteles] " + e.getMessage());
        }
        return lista;
    }


    private String buildSelectBase() {
        return """
            SELECT h.*,
                   c.id_ciudad, c.nombre AS nombre_ciudad, c.codigo_iata AS iata_ciudad,
                   p.id_pais, p.nombre AS nombre_pais, p.codigo_iso
            FROM catalogo_hoteles h
            JOIN ciudades c ON h.id_ciudad = c.id_ciudad
            JOIN paises   p ON c.id_pais   = p.id_pais
            """;
    }

    private Hotel mapear(ResultSet rs) throws SQLException {
        Pais pais   = new Pais(rs.getInt("id_pais"), rs.getString("nombre_pais"), rs.getString("codigo_iso"));
        Ciudad ciudad = new Ciudad(rs.getInt("id_ciudad"), rs.getString("nombre_ciudad"), rs.getString("iata_ciudad"), pais);

        return new Hotel(
                rs.getInt("id_hotel"),
                rs.getString("nombre"),
                ciudad,
                rs.getInt("estrellas"),
                rs.getBigDecimal("precio_noche"),
                rs.getInt("habitaciones_disponibles"),
                rs.getString("codigo_divisa")
                // 'descripcion' eliminado: la columna no existe en catalogo_hoteles
        );
    }
}