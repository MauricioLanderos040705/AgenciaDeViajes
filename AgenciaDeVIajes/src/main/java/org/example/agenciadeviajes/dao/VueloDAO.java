package org.example.agenciadeviajes.dao;


import org.example.agenciadeviajes.model.Aerolinea;
import org.example.agenciadeviajes.model.Ciudad;
import org.example.agenciadeviajes.model.Pais;
import org.example.agenciadeviajes.model.Vuelo;
import org.example.agenciadeviajes.util.ConexionDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla `catalogo_vuelos`.
 * Implementa GenericDAO<Vuelo> + búsqueda por ruta y fecha.
 */
public class VueloDAO implements GenericDAO<Vuelo> {

    private final Connection conn;

    public VueloDAO() {
        this.conn = ConexionDB.getInstance().getConexion();
    }


    @Override
    public boolean insertar(Vuelo v) {
        String sql = """
            INSERT INTO catalogo_vuelos
              (id_aerolinea, id_ciudad_origen, id_ciudad_destino,
               fecha_salida, fecha_llegada, es_redondo,
               precio_asiento, asientos_disponibles, codigo_divisa)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getAerolinea().getIdAerolinea());
            ps.setInt(2, v.getCiudadOrigen().getIdCiudad());
            ps.setInt(3, v.getCiudadDestino().getIdCiudad());
            ps.setTimestamp(4, Timestamp.valueOf(v.getFechaSalida()));
            ps.setTimestamp(5, Timestamp.valueOf(v.getFechaLlegada()));
            ps.setBoolean(6, v.isEsRedondo());
            ps.setBigDecimal(7, v.getPrecioAsiento());
            ps.setInt(8, v.getAsientosDisponibles());
            ps.setString(9, v.getCodigoDivisa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[VueloDAO.insertar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public Vuelo obtenerPorId(int id) {
        String sql = buildSelectBase() + " WHERE v.id_vuelo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("[VueloDAO.obtenerPorId] " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Vuelo> obtenerTodos() {
        List<Vuelo> lista = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(buildSelectBase() + " ORDER BY v.fecha_salida")) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VueloDAO.obtenerTodos] " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Vuelo v) {
        String sql = """
            UPDATE catalogo_vuelos
            SET precio_asiento=?, asientos_disponibles=?, es_redondo=?
            WHERE id_vuelo=?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, v.getPrecioAsiento());
            ps.setInt(2, v.getAsientosDisponibles());
            ps.setBoolean(3, v.isEsRedondo());
            ps.setInt(4, v.getIdVuelo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[VueloDAO.actualizar] " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM catalogo_vuelos WHERE id_vuelo = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[VueloDAO.eliminar] " + e.getMessage());
            return false;
        }
    }


    /**
     * Busca vuelos disponibles por ruta y fecha de salida.
     * Usado por BusquedaVueloStrategy.
     *
     * @param idOrigen  id de ciudad origen
     * @param idDestino id de ciudad destino
     * @param fecha     fecha de salida deseada
     */
    public List<Vuelo> buscarVuelos(int idOrigen, int idDestino, LocalDate fecha) {
        List<Vuelo> lista = new ArrayList<>();
        String sql = buildSelectBase() + """
             WHERE v.id_ciudad_origen = ?
               AND v.id_ciudad_destino = ?
               AND DATE(v.fecha_salida) = ?
               AND v.asientos_disponibles > 0
             ORDER BY v.precio_asiento
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrigen);
            ps.setInt(2, idDestino);
            ps.setDate(3, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[VueloDAO.buscarVuelos] " + e.getMessage());
        }
        return lista;
    }


    private String buildSelectBase() {
        return """
            SELECT v.*,
                   a.id_aerolinea, a.nombre AS nombre_aerolinea, a.codigo_iata AS iata_aerolinea,
                   co.id_ciudad AS id_origen, co.nombre AS nombre_origen, co.codigo_iata AS iata_origen,
                   cd.id_ciudad AS id_destino, cd.nombre AS nombre_destino, cd.codigo_iata AS iata_destino,
                   po.id_pais AS id_pais_origen, po.nombre AS nombre_pais_origen, po.codigo_iso AS iso_origen,
                   pd.id_pais AS id_pais_destino, pd.nombre AS nombre_pais_destino, pd.codigo_iso AS iso_destino
            FROM catalogo_vuelos v
            JOIN aerolineas a    ON v.id_aerolinea      = a.id_aerolinea
            JOIN ciudades co     ON v.id_ciudad_origen  = co.id_ciudad
            JOIN ciudades cd     ON v.id_ciudad_destino = cd.id_ciudad
            JOIN paises po       ON co.id_pais          = po.id_pais
            JOIN paises pd       ON cd.id_pais          = pd.id_pais
            """;
    }

    private Vuelo mapear(ResultSet rs) throws SQLException {
        Pais paisOrigen  = new Pais(rs.getInt("id_pais_origen"),  rs.getString("nombre_pais_origen"),  rs.getString("iso_origen"));
        Pais paisDestino = new Pais(rs.getInt("id_pais_destino"), rs.getString("nombre_pais_destino"), rs.getString("iso_destino"));

        Ciudad origen  = new Ciudad(rs.getInt("id_origen"),  rs.getString("nombre_origen"),  rs.getString("iata_origen"),  paisOrigen);
        Ciudad destino = new Ciudad(rs.getInt("id_destino"), rs.getString("nombre_destino"), rs.getString("iata_destino"), paisDestino);

        Aerolinea aerolinea = new Aerolinea(
                rs.getInt("id_aerolinea"),
                rs.getString("nombre_aerolinea"),
                rs.getString("iata_aerolinea")
        );

        Vuelo v = new Vuelo();
        v.setIdVuelo(rs.getInt("id_vuelo"));
        v.setAerolinea(aerolinea);
        v.setCiudadOrigen(origen);
        v.setCiudadDestino(destino);
        v.setFechaSalida(rs.getTimestamp("fecha_salida").toLocalDateTime());
        v.setFechaLlegada(rs.getTimestamp("fecha_llegada").toLocalDateTime());
        v.setEsRedondo(rs.getBoolean("es_redondo"));
        v.setPrecioAsiento(rs.getBigDecimal("precio_asiento"));
        v.setAsientosDisponibles(rs.getInt("asientos_disponibles"));
        v.setCodigoDivisa(rs.getString("codigo_divisa"));
        return v;
    }
}

