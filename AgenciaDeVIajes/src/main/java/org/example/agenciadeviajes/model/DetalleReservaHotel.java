package org.example.agenciadeviajes.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DetalleReservaHotel {
    private int idDetalle;
    private Hotel hotel;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int cantidadHabitaciones;
    private BigDecimal subtotal;

    public DetalleReservaHotel() {}

    public DetalleReservaHotel(Hotel hotel, LocalDate checkIn, LocalDate checkOut,
                               int cantidadHabitaciones) {
        this.hotel = hotel;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.cantidadHabitaciones = cantidadHabitaciones;
        long noches = ChronoUnit.DAYS.between(checkIn, checkOut);
        this.subtotal = hotel.getPrecioNoche()
                .multiply(BigDecimal.valueOf(noches))
                .multiply(BigDecimal.valueOf(cantidadHabitaciones));
    }

    public int getIdDetalle() {
        return idDetalle; }
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle; }

    public Hotel getHotel() {
        return hotel; }
    public void setHotel(Hotel hotel) {
        this.hotel = hotel; }

    public LocalDate getCheckIn() {
        return checkIn; }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn; }

    public LocalDate getCheckOut() {
        return checkOut; }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut; }

    public int getCantidadHabitaciones() {
        return cantidadHabitaciones; }
    public void setCantidadHabitaciones(int cantidadHabitaciones) {

        this.cantidadHabitaciones = cantidadHabitaciones;
    }

    public BigDecimal getSubtotal() {
        return subtotal; }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal; }

    public long getNumNoches() {

        return ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @Override
    public String toString() {
        return "Hotel: " + hotel.getNombre() + " · " + getNumNoches() +
                " noches → $" + subtotal;
    }
}

