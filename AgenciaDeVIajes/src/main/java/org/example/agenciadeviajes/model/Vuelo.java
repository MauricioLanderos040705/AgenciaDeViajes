package org.example.agenciadeviajes.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Vuelo {
    private int idVuelo;
    private Aerolinea aerolinea;
    private Ciudad ciudadOrigen;
    private Ciudad ciudadDestino;
    private LocalDateTime fechaSalida;
    private LocalDateTime fechaLlegada;
    private boolean esRedondo;
    private BigDecimal precioAsiento;
    private int asientosDisponibles;
    private String codigoDivisa;

    public Vuelo() {}

    public Vuelo(int idVuelo, Aerolinea aerolinea, Ciudad ciudadOrigen, Ciudad ciudadDestino,
                 LocalDateTime fechaSalida, LocalDateTime fechaLlegada, boolean esRedondo,
                 BigDecimal precioAsiento, int asientosDisponibles, String codigoDivisa) {
        this.idVuelo = idVuelo;
        this.aerolinea = aerolinea;
        this.ciudadOrigen = ciudadOrigen;
        this.ciudadDestino = ciudadDestino;
        this.fechaSalida = fechaSalida;
        this.fechaLlegada = fechaLlegada;
        this.esRedondo = esRedondo;
        this.precioAsiento = precioAsiento;
        this.asientosDisponibles = asientosDisponibles;
        this.codigoDivisa = codigoDivisa;
    }

    public int getIdVuelo() {
        return idVuelo; }
    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo; }

    public Aerolinea getAerolinea() {
        return aerolinea; }
    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea; }

    public Ciudad getCiudadOrigen() {
        return ciudadOrigen; }
    public void setCiudadOrigen(Ciudad ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen; }

    public Ciudad getCiudadDestino() {
        return ciudadDestino; }
    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino; }

    public LocalDateTime getFechaSalida() {
        return fechaSalida; }
    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida; }

    public LocalDateTime getFechaLlegada() {
        return fechaLlegada; }
    public void setFechaLlegada(LocalDateTime fechaLlegada) {
        this.fechaLlegada = fechaLlegada; }

    public boolean isEsRedondo() {
        return esRedondo; }
    public void setEsRedondo(boolean esRedondo) {
        this.esRedondo = esRedondo; }

    public BigDecimal getPrecioAsiento() {
        return precioAsiento; }
    public void setPrecioAsiento(BigDecimal precioAsiento) {
        this.precioAsiento = precioAsiento; }

    public int getAsientosDisponibles() {
        return asientosDisponibles; }
    public void setAsientosDisponibles(int asientosDisponibles) {
        this.asientosDisponibles = asientosDisponibles; }

    public String getCodigoDivisa() {
        return codigoDivisa; }
    public void setCodigoDivisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa; }

    @Override
    public String toString() {
        return aerolinea.getCodigoIata() + " | " +
                ciudadOrigen.getCodigoIata() + " → " + ciudadDestino.getCodigoIata() +
                " | $" + precioAsiento + " " + codigoDivisa;
    }
}
