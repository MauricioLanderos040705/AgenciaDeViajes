package org.example.agenciadeviajes.strategy;

import org.example.agenciadeviajes.dao.HotelDAO;
import org.example.agenciadeviajes.model.Hotel;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Concreto para búsqueda de Hoteles
 *
 * Implementa la estrategia específica para buscar hoteles según:
 * - Ciudad
 * - Fechas de Check-In y Check-Out
 * - Número de habitaciones disponibles
 * - Número de estrellas (opcional)
 *
 * Ejemplo:
 * BusquedaStrategy<Hotel> estrategia = new BusquedaHotelStrategy(
 *     hotelDAO, idCiudad, checkIn, checkOut, 2, 3
 * );
 * List<Hotel> resultados = estrategia.buscar();
 */
public class BusquedaHotelStrategy implements BusquedaStrategy<Hotel> {

    private final HotelDAO hotelDAO;
    private final int idCiudad;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final int habitacionesRequeridas;
    private final int estrellasMínimas; // 0 = cualquier número

    /**
     * Constructor básico sin filtro de estrellas
     */
    public BusquedaHotelStrategy(HotelDAO hotelDAO, int idCiudad,
                               LocalDate checkIn, LocalDate checkOut,
                               int habitacionesRequeridas) {
        this(hotelDAO, idCiudad, checkIn, checkOut, habitacionesRequeridas, 0);
    }

    /**
     * Constructor completo con filtro de estrellas
     */
    public BusquedaHotelStrategy(HotelDAO hotelDAO, int idCiudad,
                               LocalDate checkIn, LocalDate checkOut,
                               int habitacionesRequeridas, int estrellasMínimas) {
        this.hotelDAO = hotelDAO;
        this.idCiudad = idCiudad;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.habitacionesRequeridas = habitacionesRequeridas;
        this.estrellasMínimas = estrellasMínimas;
    }

    @Override
    public List<Hotel> buscar() {
        validar();

        // Obtener hoteles de la ciudad
        List<Hotel> hotelesEnCiudad = hotelDAO.obtenerTodos().stream()
                .filter(h -> h.getCiudad().getIdCiudad() == idCiudad)
                .collect(Collectors.toList());

        // Filtrar según criterios
        return hotelesEnCiudad.stream()
                .filter(h -> h.getHabitacionesDisponibles() >= habitacionesRequeridas)
                .filter(h -> estrellasMínimas == 0 || h.getEstrellas() >= estrellasMínimas)
                .filter(h -> !estaOcupado(h))
                .collect(Collectors.toList());
    }

    @Override
    public String obtenerDescripcion() {
        String desc = String.format(
                "Búsqueda de Hoteles: Ciudad=%d, CheckIn=%s, CheckOut=%s, " +
                "Habitaciones=%d, Noches=%d",
                idCiudad, checkIn, checkOut, habitacionesRequeridas,
                ChronoUnit.DAYS.between(checkIn, checkOut)
        );

        if (estrellasMínimas > 0) {
            desc += ", Estrellas mínimas=" + estrellasMínimas;
        }

        return desc;
    }

    @Override
    public String getNombreEstrategia() {
        return "BusquedaHotel";
    }

    @Override
    public void validar() throws IllegalArgumentException {
        if (idCiudad <= 0) {
            throw new IllegalArgumentException("ID de ciudad debe ser válido");
        }

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Fechas de Check-In y Check-Out son requeridas");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-Out debe ser posterior a Check-In");
        }

        if (habitacionesRequeridas < 1) {
            throw new IllegalArgumentException("Habitaciones requeridas debe ser al menos 1");
        }

        if (estrellasMínimas < 0 || estrellasMínimas > 5) {
            throw new IllegalArgumentException("Estrellas mínimas debe estar entre 0 y 5");
        }
    }

    /**
     * Verifica si el hotel está ocupado en las fechas especificadas
     * (Simulado: en una implementación real consultaría las reservas)
     */
    private boolean estaOcupado(Hotel hotel) {
        // Por ahora retorna false
        // En una implementación real:
        // return detalleHotelDAO.obtenerPorFechas(checkIn, checkOut)
        //     .stream()
        //     .anyMatch(d -> d.getHotel().getIdHotel() == hotel.getIdHotel());
        return false;
    }

    /**
     * Calcula el número de noches de la reserva
     */
    public long getNumeroNoches() {
        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    // GETTERS

    public int getIdCiudad() {
        return idCiudad;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public int getHabitacionesRequeridas() {
        return habitacionesRequeridas;
    }

    public int getEstrellasMínimas() {
        return estrellasMínimas;
    }
}
