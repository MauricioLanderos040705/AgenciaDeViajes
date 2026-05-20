package org.example.agenciadeviajes.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
    private int idReserva;
    private String folio;
    private Usuario usuario;
    private Timestamp fechaReserva;
    private String tipoReserva;
    private BigDecimal totalPagado;
    private String codigoDivisa;

    private List<DetalleReservaVuelo>  detallesVuelo  = new ArrayList<>();
    private List<DetalleReservaHotel>  detallesHotel  = new ArrayList<>();
    private List<DetalleReservaAuto>   detallesAuto   = new ArrayList<>();

    public Reserva() {}

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
        // genera el folio automáticamente al recibir el id de BD
        this.folio = String.format("#RVJ-%tY-%04d",
                fechaReserva != null ? fechaReserva : new Timestamp(System.currentTimeMillis()),
                idReserva);
    }

    public String getFolio() {
        return folio; }
    public void setFolio(String folio) {
        this.folio = folio; }

    public Usuario getUsuario() {
        return usuario; }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; }

    public Timestamp getFechaReserva() {
        return fechaReserva; }
    public void setFechaReserva(Timestamp fechaReserva) {
        this.fechaReserva = fechaReserva; }

    public String getTipoReserva() {
        return tipoReserva; }
    public void setTipoReserva(String tipoReserva) {
        this.tipoReserva = tipoReserva; }

    public BigDecimal getTotalPagado() {
        return totalPagado; }
    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado; }

    public String getCodigoDivisa() {
        return codigoDivisa; }
    public void setCodigoDivisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa; }

    public List<DetalleReservaVuelo> getDetallesVuelo() {
        return detallesVuelo; }
    public void setDetallesVuelo(List<DetalleReservaVuelo> detallesVuelo) {

        this.detallesVuelo = detallesVuelo;
    }

    public List<DetalleReservaHotel> getDetallesHotel() {
        return detallesHotel; }
    public void setDetallesHotel(List<DetalleReservaHotel> detallesHotel) {

        this.detallesHotel = detallesHotel;
    }

    public List<DetalleReservaAuto> getDetallesAuto() {
        return detallesAuto; }
    public void setDetallesAuto(List<DetalleReservaAuto> detallesAuto) {
        this.detallesAuto = detallesAuto;
    }

    @Override
    public String toString() {

        return folio + " | " + tipoReserva + " | $" + totalPagado + " " + codigoDivisa;
    }
}
