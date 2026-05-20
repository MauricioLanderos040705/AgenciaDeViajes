package org.example.agenciadeviajes.model;
import java.math.BigDecimal;

public class DetalleReservaVuelo {
    private int idDetalle;
    private Vuelo vuelo;
    private int cantidadPasajeros;
    private BigDecimal subtotal;

    public DetalleReservaVuelo() {}

    public DetalleReservaVuelo(Vuelo vuelo, int cantidadPasajeros) {
        this.vuelo = vuelo;
        this.cantidadPasajeros = cantidadPasajeros;
        // subtotal se calcula al construir la reserva
        this.subtotal = vuelo.getPrecioAsiento()
                .multiply(BigDecimal.valueOf(cantidadPasajeros));
    }

    public int getIdDetalle() {
        return idDetalle; }
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle; }

    public Vuelo getVuelo() {
        return vuelo; }
    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo; }

    public int getCantidadPasajeros() {
        return cantidadPasajeros; }
    public void setCantidadPasajeros(int cantidadPasajeros) {

        this.cantidadPasajeros = cantidadPasajeros;
    }

    public BigDecimal getSubtotal() {
        return subtotal; }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal; }

    @Override
    public String toString() {
        return "Vuelo: " + vuelo.getAerolinea().getCodigoIata() +
                " x" + cantidadPasajeros + " pas. → $" + subtotal;
    }
}

