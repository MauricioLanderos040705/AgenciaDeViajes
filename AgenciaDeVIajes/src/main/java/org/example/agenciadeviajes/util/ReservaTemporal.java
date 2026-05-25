package org.example.agenciadeviajes.util;

import org.example.agenciadeviajes.factory.ReservaBuilder;
import org.example.agenciadeviajes.factory.ReservaFactory;
import org.example.agenciadeviajes.model.*;

import java.time.LocalDate;

public class ReservaTemporal {

    private static ReservaBuilder builder;

    public static void iniciar(Usuario usuario) {

        builder = ReservaFactory
                .crearCustomizado()
                .usuario(usuario);
    }

    public static boolean existeReservaActiva() {

        return builder != null;
    }

    public static ReservaBuilder getBuilder() {

        return builder;
    }

    public static void agregarVuelo(
            Vuelo vuelo,
            int pasajeros
    ) {

        builder
                .divisa(vuelo.getCodigoDivisa())
                .agregarVuelo(vuelo, pasajeros);
    }

    public static void agregarHotel(
            Hotel hotel,
            LocalDate checkIn,
            LocalDate checkOut,
            int habitaciones
    ) {

        builder
                .divisa(hotel.getCodigoDivisa())
                .agregarHotel(
                        hotel,
                        checkIn,
                        checkOut,
                        habitaciones
                );
    }

    public static void agregarAuto(
            Auto auto,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {

        builder
                .divisa(auto.getCodigoDivisa())
                .agregarAuto(
                        auto,
                        fechaInicio,
                        fechaFin
                );
    }

    public static Reserva construirReserva() {

        return builder
                .inferirTipo()
                .calcularTotal()
                .build();
    }

    public static void limpiar() {

        builder = null;
    }
}