package org.example.agenciadeviajes.strategy;

import org.example.agenciadeviajes.dao.VueloDAO;
import org.example.agenciadeviajes.model.Vuelo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Concreto para búsqueda de Vuelos
 *
 * Implementa la estrategia específica para buscar vuelos según:
 * - Ciudad origen
 * - Ciudad destino
 * - Fecha de salida
 * - Disponibilidad de asientos
 *
 * Ejemplo:
 * BusquedaStrategy<Vuelo> estrategia = new BusquedaVueloStrategy(
 *     vueloDAO, idOrigen, idDestino, fechaBuscada
 * );
 * List<Vuelo> resultados = estrategia.buscar();
 */
public class BusquedaVueloStrategy implements BusquedaStrategy<Vuelo> {

    private final VueloDAO vueloDAO;
    private final int idCiudadOrigen;
    private final int idCiudadDestino;
    private final LocalDateTime fechaBuscada;
    private final int asientosMinimos;

    /**
     * Constructor con parámetros de búsqueda
     */
    public BusquedaVueloStrategy(VueloDAO vueloDAO, int idOrigen, int idDestino,
                               LocalDateTime fecha) {
        this(vueloDAO, idOrigen, idDestino, fecha, 1);
    }

    /**
     * Constructor con parámetro adicional de asientos mínimos
     */
    public BusquedaVueloStrategy(VueloDAO vueloDAO, int idOrigen, int idDestino,
                               LocalDateTime fecha, int asientosMinimos) {
        this.vueloDAO = vueloDAO;
        this.idCiudadOrigen = idOrigen;
        this.idCiudadDestino = idDestino;
        this.fechaBuscada = fecha;
        this.asientosMinimos = asientosMinimos;
    }

    @Override
    public List<Vuelo> buscar() {
        validar();

        // Obtener todos los vuelos
        List<Vuelo> todosVuelos = vueloDAO.obtenerTodos();

        // Filtrar según criterios
        return todosVuelos.stream()
                .filter(v -> v.getCiudadOrigen().getIdCiudad() == idCiudadOrigen)
                .filter(v -> v.getCiudadDestino().getIdCiudad() == idCiudadDestino)
                .filter(v -> esFechaCoincidente(v.getFechaSalida(), fechaBuscada))
                .filter(v -> v.getAsientosDisponibles() >= asientosMinimos)
                .collect(Collectors.toList());
    }

    @Override
    public String obtenerDescripcion() {
        return String.format(
                "Búsqueda de Vuelos: Origen=%d, Destino=%d, Fecha=%s, Asientos mínimos=%d",
                idCiudadOrigen, idCiudadDestino, fechaBuscada, asientosMinimos
        );
    }

    @Override
    public String getNombreEstrategia() {
        return "BusquedaVuelo";
    }

    @Override
    public void validar() throws IllegalArgumentException {
        if (idCiudadOrigen <= 0 || idCiudadDestino <= 0) {
            throw new IllegalArgumentException("IDs de ciudades deben ser válidos");
        }

        if (idCiudadOrigen == idCiudadDestino) {
            throw new IllegalArgumentException("Origen y destino no pueden ser la misma ciudad");
        }

        if (fechaBuscada == null) {
            throw new IllegalArgumentException("Fecha de búsqueda es requerida");
        }

        if (asientosMinimos < 1) {
            throw new IllegalArgumentException("Asientos mínimos debe ser al menos 1");
        }
    }

    /**
     * Verifica si las fechas coinciden (mismo día)
     */
    private boolean esFechaCoincidente(LocalDateTime fechaVuelo, LocalDateTime fechaBuscada) {
        if (fechaVuelo == null || fechaBuscada == null) {
            return false;
        }

        return fechaVuelo.toLocalDate().equals(fechaBuscada.toLocalDate());
    }

    // GETTERS

    public int getIdCiudadOrigen() {
        return idCiudadOrigen;
    }

    public int getIdCiudadDestino() {
        return idCiudadDestino;
    }

    public LocalDateTime getFechaBuscada() {
        return fechaBuscada;
    }

    public int getAsientosMinimos() {
        return asientosMinimos;
    }
}
