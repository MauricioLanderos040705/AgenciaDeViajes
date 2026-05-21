package org.example.agenciadeviajes.model;

import java.math.BigDecimal;

public class Hotel {
    private int idHotel;
    private String nombre;
    private Ciudad ciudad;
    private int estrellas;
    private BigDecimal precioNoche;
    private int habitacionesDisponibles;
    private String codigoDivisa;

    public Hotel() {}

    public Hotel(int idHotel, String nombre, Ciudad ciudad, int estrellas,
                 BigDecimal precioNoche, int habitacionesDisponibles, String codigoDivisa) {
        this.idHotel = idHotel;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.estrellas = estrellas;
        this.precioNoche = precioNoche;
        this.habitacionesDisponibles = habitacionesDisponibles;
        this.codigoDivisa = codigoDivisa;
    }

    public int getIdHotel() {
        return idHotel; }
    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public Ciudad getCiudad() {
        return ciudad; }
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad; }

    public int getEstrellas() {
        return estrellas; }
    public void setEstrellas(int estrellas) {
        this.estrellas = estrellas; }

    public BigDecimal getPrecioNoche() {
        return precioNoche; }
    public void setPrecioNoche(BigDecimal precioNoche) {
        this.precioNoche = precioNoche; }

    public int getHabitacionesDisponibles() {
        return habitacionesDisponibles; }
    public void setHabitacionesDisponibles(int habitacionesDisponibles) {
        this.habitacionesDisponibles = habitacionesDisponibles;
    }


    public String getCodigoDivisa() {
        return codigoDivisa; }
    public void setCodigoDivisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa; }

    public String getEstrellasStr() {
        return "★".repeat(estrellas);
    }
    @Override
    public String toString() {
        return nombre + " " + getEstrellasStr() + " | $" + precioNoche + "/noche";
    }
}

