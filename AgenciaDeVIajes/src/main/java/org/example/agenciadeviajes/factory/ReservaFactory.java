package org.example.agenciadeviajes.factory;

import org.example.agenciadeviajes.model.*;

import java.time.LocalDate;

/**
 * Factory Pattern para crear diferentes tipos de Reservas
 * Centraliza la lógica de creación de reservas según el tipo
 *
 * Ejemplo:
 * Reserva reservaVuelo = ReservaFactory.crearReservaVuelo(usuario, vuelo, 2);
 * Reserva reservaHotel = ReservaFactory.crearReservaHotel(usuario, hotel, checkIn, checkOut, 3);
 * Reserva reservaPaquete = ReservaFactory.crearReservaPaquete(usuario, vuelo, hotel, auto, ...);
 */
public class ReservaFactory {

    /**
     * Crea una reserva de solo vuelo
     */
    public static Reserva crearReservaVuelo(Usuario usuario, Vuelo vuelo, int cantidadPasajeros) {
        if (usuario == null || vuelo == null) {
            throw new IllegalArgumentException("Usuario y Vuelo no pueden ser nulos");
        }

        return ReservaBuilder.soloVuelo(usuario, vuelo, cantidadPasajeros)
                .build();
    }

    /**
     * Crea una reserva de solo hotel
     */
    public static Reserva crearReservaHotel(Usuario usuario, Hotel hotel,
                                           LocalDate checkIn, LocalDate checkOut,
                                           int cantidadHabitaciones) {
        if (usuario == null || hotel == null) {
            throw new IllegalArgumentException("Usuario y Hotel no pueden ser nulos");
        }

        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-Out debe ser posterior a Check-In");
        }

        return ReservaBuilder.soloHotel(usuario, hotel, checkIn, checkOut, cantidadHabitaciones)
                .build();
    }

    /**
     * Crea una reserva de solo auto
     */
    public static Reserva crearReservaAuto(Usuario usuario, Auto auto,
                                          LocalDate fechaInicio, LocalDate fechaFin) {
        if (usuario == null || auto == null) {
            throw new IllegalArgumentException("Usuario y Auto no pueden ser nulos");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("Fecha de fin debe ser posterior a fecha de inicio");
        }

        return ReservaBuilder.soloAuto(usuario, auto, fechaInicio, fechaFin)
                .build();
    }

    /**
     * Crea una reserva de paquete: Vuelo + Hotel
     */
    public static Reserva crearReservaPaqueteVueloHotel(Usuario usuario,
                                                       Vuelo vuelo, int pasajeros,
                                                       Hotel hotel, LocalDate checkIn,
                                                       LocalDate checkOut, int habitaciones) {
        if (usuario == null || vuelo == null || hotel == null) {
            throw new IllegalArgumentException("Usuario, Vuelo y Hotel no pueden ser nulos");
        }

        validarFechasHotel(checkIn, checkOut);

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(vuelo.getCodigoDivisa()) // Usar divisa del vuelo como principal
                .agregarVuelo(vuelo, pasajeros)
                .agregarHotel(hotel, checkIn, checkOut, habitaciones)
                .inferirTipo()
                .calcularTotal()
                .build();
    }

    /**
     * Crea una reserva de paquete: Vuelo + Auto
     */
    public static Reserva crearReservaPaqueteVueloAuto(Usuario usuario,
                                                      Vuelo vuelo, int pasajeros,
                                                      Auto auto, LocalDate fechaInicio,
                                                      LocalDate fechaFin) {
        if (usuario == null || vuelo == null || auto == null) {
            throw new IllegalArgumentException("Usuario, Vuelo y Auto no pueden ser nulos");
        }

        validarFechasAuto(fechaInicio, fechaFin);

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(vuelo.getCodigoDivisa())
                .agregarVuelo(vuelo, pasajeros)
                .agregarAuto(auto, fechaInicio, fechaFin)
                .inferirTipo()
                .calcularTotal()
                .build();
    }

    /**
     * Crea una reserva de paquete: Hotel + Auto
     */
    public static Reserva crearReservaPaqueteHotelAuto(Usuario usuario,
                                                      Hotel hotel, LocalDate checkIn,
                                                      LocalDate checkOut, int habitaciones,
                                                      Auto auto, LocalDate fechaInicio,
                                                      LocalDate fechaFin) {
        if (usuario == null || hotel == null || auto == null) {
            throw new IllegalArgumentException("Usuario, Hotel y Auto no pueden ser nulos");
        }

        validarFechasHotel(checkIn, checkOut);
        validarFechasAuto(fechaInicio, fechaFin);

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(hotel.getCodigoDivisa())
                .agregarHotel(hotel, checkIn, checkOut, habitaciones)
                .agregarAuto(auto, fechaInicio, fechaFin)
                .inferirTipo()
                .calcularTotal()
                .build();
    }

    /**
     * Crea una reserva de paquete completo: Vuelo + Hotel + Auto
     */
    public static Reserva crearReservaPaqueteCompleto(Usuario usuario,
                                                     Vuelo vuelo, int pasajeros,
                                                     Hotel hotel, LocalDate checkIn,
                                                     LocalDate checkOut, int habitaciones,
                                                     Auto auto, LocalDate fechaInicio,
                                                     LocalDate fechaFin) {
        if (usuario == null || vuelo == null || hotel == null || auto == null) {
            throw new IllegalArgumentException("Todos los parámetros (Usuario, Vuelo, Hotel, Auto) son requeridos");
        }

        validarFechasHotel(checkIn, checkOut);
        validarFechasAuto(fechaInicio, fechaFin);

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(vuelo.getCodigoDivisa())
                .agregarVuelo(vuelo, pasajeros)
                .agregarHotel(hotel, checkIn, checkOut, habitaciones)
                .agregarAuto(auto, fechaInicio, fechaFin)
                .inferirTipo()
                .calcularTotal()
                .build();
    }

    /**
     * Crea una reserva personalizada usando el builder
     */
    public static ReservaBuilder crearCustomizado() {
        return new ReservaBuilder();
    }

    /**
     * Modifica una reserva existente
     */
    public static Reserva modificarReserva(Reserva reservaExistente) {
        return ReservaBuilder.desde(reservaExistente).build();
    }

    // MÉTODOS PRIVADOS DE VALIDACIÓN

    private static void validarFechasHotel(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Las fechas del hotel no pueden ser nulas");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Check-Out debe ser posterior a Check-In");
        }
    }

    private static void validarFechasAuto(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas del auto no pueden ser nulas");
        }
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("Fecha de fin debe ser posterior a fecha de inicio");
        }
    }
}
