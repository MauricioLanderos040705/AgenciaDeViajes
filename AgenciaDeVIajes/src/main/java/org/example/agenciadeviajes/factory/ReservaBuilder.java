package org.example.agenciadeviajes.factory;

import org.example.agenciadeviajes.model.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class ReservaBuilder {

    private final Reserva reserva;

    public ReservaBuilder() {

        reserva = new Reserva();

        reserva.setFechaReserva(
                new Timestamp(System.currentTimeMillis())
        );
    }

    public static ReservaBuilder soloVuelo(
            Usuario usuario,
            Vuelo vuelo,
            int pasajeros
    ) {

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(vuelo.getCodigoDivisa())
                .agregarVuelo(vuelo, pasajeros)
                .inferirTipo()
                .calcularTotal();
    }

    public static ReservaBuilder soloHotel(
            Usuario usuario,
            Hotel hotel,
            LocalDate checkIn,
            LocalDate checkOut,
            int habitaciones
    ) {

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(hotel.getCodigoDivisa())
                .agregarHotel(
                        hotel,
                        checkIn,
                        checkOut,
                        habitaciones
                )
                .inferirTipo()
                .calcularTotal();
    }

    public static ReservaBuilder soloAuto(
            Usuario usuario,
            Auto auto,
            LocalDate inicio,
            LocalDate fin
    ) {

        return new ReservaBuilder()
                .usuario(usuario)
                .divisa(auto.getCodigoDivisa())
                .agregarAuto(auto, inicio, fin)
                .inferirTipo()
                .calcularTotal();
    }

    public static ReservaBuilder desde(
            Reserva reservaExistente
    ) {

        ReservaBuilder builder =
                new ReservaBuilder();

        builder.reserva.setUsuario(
                reservaExistente.getUsuario()
        );

        builder.reserva.setCodigoDivisa(
                reservaExistente.getCodigoDivisa()
        );

        builder.reserva.setTipoReserva(
                reservaExistente.getTipoReserva()
        );

        builder.reserva.setTotalPagado(
                reservaExistente.getTotalPagado()
        );

        builder.reserva.getDetallesVuelo()
                .addAll(reservaExistente.getDetallesVuelo());

        builder.reserva.getDetallesHotel()
                .addAll(reservaExistente.getDetallesHotel());

        builder.reserva.getDetallesAuto()
                .addAll(reservaExistente.getDetallesAuto());

        return builder;
    }

    public ReservaBuilder usuario(
            Usuario usuario
    ) {

        reserva.setUsuario(usuario);

        return this;
    }

    public ReservaBuilder divisa(
            String divisa
    ) {

        reserva.setCodigoDivisa(divisa);

        return this;
    }

    public ReservaBuilder agregarVuelo(
            Vuelo vuelo,
            int pasajeros
    ) {

        DetalleReservaVuelo detalle =
                new DetalleReservaVuelo(
                        vuelo,
                        pasajeros
                );

        reserva.getDetallesVuelo()
                .add(detalle);

        return this;
    }

    public ReservaBuilder agregarHotel(
            Hotel hotel,
            LocalDate checkIn,
            LocalDate checkOut,
            int habitaciones
    ) {

        DetalleReservaHotel detalle =
                new DetalleReservaHotel(
                        hotel,
                        checkIn,
                        checkOut,
                        habitaciones
                );

        reserva.getDetallesHotel()
                .add(detalle);

        return this;
    }

    public ReservaBuilder agregarAuto(
            Auto auto,
            LocalDate inicio,
            LocalDate fin
    ) {

        DetalleReservaAuto detalle =
                new DetalleReservaAuto(
                        auto,
                        inicio,
                        fin
                );

        reserva.getDetallesAuto()
                .add(detalle);

        return this;
    }

    public ReservaBuilder inferirTipo() {

        boolean tieneVuelo =
                !reserva.getDetallesVuelo().isEmpty();

        boolean tieneHotel =
                !reserva.getDetallesHotel().isEmpty();

        boolean tieneAuto =
                !reserva.getDetallesAuto().isEmpty();

        int totalTipos = 0;

        if (tieneVuelo) totalTipos++;
        if (tieneHotel) totalTipos++;
        if (tieneAuto) totalTipos++;

        if (totalTipos > 1) {

            reserva.setTipoReserva(
                    "Paquete"
            );

        } else {

            reserva.setTipoReserva(
                    "Individual"
            );
        }

        return this;
    }

    public ReservaBuilder calcularTotal() {

        BigDecimal total =
                BigDecimal.ZERO;

        for (DetalleReservaVuelo dv :
                reserva.getDetallesVuelo()) {

            total = total.add(
                    dv.getSubtotal()
            );
        }

        for (DetalleReservaHotel dh :
                reserva.getDetallesHotel()) {

            total = total.add(
                    dh.getSubtotal()
            );
        }

        for (DetalleReservaAuto da :
                reserva.getDetallesAuto()) {

            total = total.add(
                    da.getSubtotal()
            );
        }

        reserva.setTotalPagado(total);

        return this;
    }

    public Reserva build() {

        return reserva;
    }
}