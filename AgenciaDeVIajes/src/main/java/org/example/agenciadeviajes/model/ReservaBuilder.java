package org.example.agenciadeviajes.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder Pattern para Reserva
 * Permite construir reservas de forma fluida y segura
 *
 * Ejemplo:
 * Reserva reserva = new ReservaBuilder()
 *     .usuario(usuarioActual)
 *     .tipo("Individual")
 *     .divisa("MXN")
 *     .agregarVuelo(vuelo, 1)
 *     .agregarHotel(hotel, checkIn, checkOut, 2)
 *     .calcularTotal()
 *     .build();
 */
public class ReservaBuilder {

    private int idReserva;
    private String folio;
    private Usuario usuario;
    private Timestamp fechaReserva;
    private String tipoReserva;
    private BigDecimal totalPagado = BigDecimal.ZERO;
    private String codigoDivisa;

    private List<DetalleReservaVuelo> detallesVuelo = new ArrayList<>();
    private List<DetalleReservaHotel> detallesHotel = new ArrayList<>();
    private List<DetalleReservaAuto> detallesAuto = new ArrayList<>();

    // BUILDER METHODS

    public ReservaBuilder usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public ReservaBuilder tipo(String tipoReserva) {
        this.tipoReserva = tipoReserva;
        return this;
    }

    public ReservaBuilder divisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa;
        return this;
    }

    public ReservaBuilder total(BigDecimal total) {
        this.totalPagado = total;
        return this;
    }

    public ReservaBuilder fechaReserva(Timestamp fecha) {
        this.fechaReserva = fecha;
        return this;
    }

    /**
     * Agrega un vuelo a la reserva
     */
    public ReservaBuilder agregarVuelo(Vuelo vuelo, int cantidadPasajeros) {
        if (vuelo == null) {
            throw new IllegalArgumentException("El vuelo no puede ser nulo");
        }
        DetalleReservaVuelo detalle = new DetalleReservaVuelo(vuelo, cantidadPasajeros);
        detallesVuelo.add(detalle);
        // Usar la divisa del vuelo si no está seteada
        if (this.codigoDivisa == null) {
            this.codigoDivisa = vuelo.getCodigoDivisa();
        }
        return this;
    }

    /**
     * Agrega un hotel a la reserva
     */
    public ReservaBuilder agregarHotel(Hotel hotel, java.time.LocalDate checkIn,
                                       java.time.LocalDate checkOut, int cantidadHabitaciones) {
        if (hotel == null) {
            throw new IllegalArgumentException("El hotel no puede ser nulo");
        }
        DetalleReservaHotel detalle = new DetalleReservaHotel(hotel, checkIn, checkOut, cantidadHabitaciones);
        detallesHotel.add(detalle);
        // Usar la divisa del hotel si no está seteada
        if (this.codigoDivisa == null) {
            this.codigoDivisa = hotel.getCodigoDivisa();
        }
        return this;
    }

    /**
     * Agrega un auto a la reserva
     */
    public ReservaBuilder agregarAuto(Auto auto, java.time.LocalDate fechaInicio,
                                      java.time.LocalDate fechaFin) {
        if (auto == null) {
            throw new IllegalArgumentException("El auto no puede ser nulo");
        }
        DetalleReservaAuto detalle = new DetalleReservaAuto(auto, fechaInicio, fechaFin);
        detallesAuto.add(detalle);
        // Usar la divisa del auto si no está seteada
        if (this.codigoDivisa == null) {
            this.codigoDivisa = auto.getCodigoDivisa();
        }
        return this;
    }

    /**
     * Calcula el total automáticamente sumando todos los detalles
     */
    public ReservaBuilder calcularTotal() {
        BigDecimal suma = BigDecimal.ZERO;

        for (DetalleReservaVuelo detalle : detallesVuelo) {
            suma = suma.add(detalle.getSubtotal());
        }

        for (DetalleReservaHotel detalle : detallesHotel) {
            suma = suma.add(detalle.getSubtotal());
        }

        for (DetalleReservaAuto detalle : detallesAuto) {
            suma = suma.add(detalle.getSubtotal());
        }

        this.totalPagado = suma;
        return this;
    }

    /**
     * Establece el tipo de reserva según los detalles agregados
     */
    public ReservaBuilder inferirTipo() {
        int cantidadTipos = 0;
        StringBuilder tipoInferido = new StringBuilder();

        if (!detallesVuelo.isEmpty()) {
            cantidadTipos++;
            tipoInferido.append("Vuelo");
        }

        if (!detallesHotel.isEmpty()) {
            cantidadTipos++;
            if (tipoInferido.length() > 0) tipoInferido.append("+");
            tipoInferido.append("Hotel");
        }

        if (!detallesAuto.isEmpty()) {
            cantidadTipos++;
            if (tipoInferido.length() > 0) tipoInferido.append("+");
            tipoInferido.append("Auto");
        }

        if (cantidadTipos == 1) {
            this.tipoReserva = "Individual";
        } else if (cantidadTipos > 1) {
            this.tipoReserva = "Paquete " + tipoInferido;
        }

        return this;
    }

    /**
     * Valida que la reserva esté completa
     */
    public ReservaBuilder validar() {
        if (usuario == null) {
            throw new IllegalStateException("Usuario es requerido");
        }
        if (codigoDivisa == null) {
            throw new IllegalStateException("Divisa es requerida");
        }
        if (tipoReserva == null || tipoReserva.isEmpty()) {
            throw new IllegalStateException("Tipo de reserva es requerido");
        }
        if (detallesVuelo.isEmpty() && detallesHotel.isEmpty() && detallesAuto.isEmpty()) {
            throw new IllegalStateException("Debe agregar al menos un detalle de reserva");
        }
        return this;
    }

    /**
     * Construye la reserva final
     */
    public Reserva build() {
        // Validar antes de construir
        validar();

        // Si no se calculó el total, calcularlo automáticamente
        if (totalPagado.compareTo(BigDecimal.ZERO) == 0) {
            calcularTotal();
        }

        // Si no se inferió el tipo, hacerlo
        if (tipoReserva == null || tipoReserva.equals("Individual")) {
            inferirTipo();
        }

        // Si no se seteó fecha de reserva, usar la actual
        if (fechaReserva == null) {
            fechaReserva = new Timestamp(System.currentTimeMillis());
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setTipoReserva(tipoReserva);
        reserva.setCodigoDivisa(codigoDivisa);
        reserva.setTotalPagado(totalPagado);
        reserva.setFechaReserva(fechaReserva);
        reserva.setDetallesVuelo(new ArrayList<>(detallesVuelo));
        reserva.setDetallesHotel(new ArrayList<>(detallesHotel));
        reserva.setDetallesAuto(new ArrayList<>(detallesAuto));

        return reserva;
    }

    // FACTORY METHODS

    /**
     * Factory method para crear un builder vacío
     */
    public static ReservaBuilder crear() {
        return new ReservaBuilder();
    }

    /**
     * Factory method para crear un builder desde una reserva existente
     */
    public static ReservaBuilder desde(Reserva reserva) {
        ReservaBuilder builder = new ReservaBuilder();
        builder.usuario = reserva.getUsuario();
        builder.tipoReserva = reserva.getTipoReserva();
        builder.totalPagado = reserva.getTotalPagado();
        builder.codigoDivisa = reserva.getCodigoDivisa();
        builder.fechaReserva = reserva.getFechaReserva();
        builder.detallesVuelo = new ArrayList<>(reserva.getDetallesVuelo());
        builder.detallesHotel = new ArrayList<>(reserva.getDetallesHotel());
        builder.detallesAuto = new ArrayList<>(reserva.getDetallesAuto());
        return builder;
    }

    /**
     * Factory method para reserva de solo vuelo
     */
    public static ReservaBuilder soloVuelo(Usuario usuario, Vuelo vuelo, int pasajeros) {
        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(vuelo.getCodigoDivisa())
                .agregarVuelo(vuelo, pasajeros)
                .tipo("Individual")
                .calcularTotal();
    }

    /**
     * Factory method para reserva de solo hotel
     */
    public static ReservaBuilder soloHotel(Usuario usuario, Hotel hotel,
                                           java.time.LocalDate checkIn,
                                           java.time.LocalDate checkOut,
                                           int habitaciones) {
        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(hotel.getCodigoDivisa())
                .agregarHotel(hotel, checkIn, checkOut, habitaciones)
                .tipo("Individual")
                .calcularTotal();
    }

    /**
     * Factory method para reserva de solo auto
     */
    public static ReservaBuilder soloAuto(Usuario usuario, Auto auto,
                                          java.time.LocalDate fechaInicio,
                                          java.time.LocalDate fechaFin) {
        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(auto.getCodigoDivisa())
                .agregarAuto(auto, fechaInicio, fechaFin)
                .tipo("Individual")
                .calcularTotal();
    }
}
