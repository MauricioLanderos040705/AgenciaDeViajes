package org.example.agenciadeviajes.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DetalleReservaAuto {
    private int idDetalle;
    private Auto auto;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal subtotal;

    public DetalleReservaAuto() {}

    public DetalleReservaAuto(Auto auto, LocalDate fechaInicio, LocalDate fechaFin) {
        this.auto = auto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        // días × precio/día
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        this.subtotal = auto.getPrecioDia().multiply(BigDecimal.valueOf(dias));
    }

    public int getIdDetalle() {
        return idDetalle; }
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle; }

    public Auto getAuto() {
        return auto; }
    public void setAuto(Auto auto) {
        this.auto = auto; }

    public LocalDate getFechaInicio() {
        return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() {
        return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin; }

    public BigDecimal getSubtotal() {
        return subtotal; }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal; }

    public long getNumDias() {

        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    @Override
    public String toString() {
        return "Auto: " + auto.getModeloAuto() + " · " + getNumDias() +
                " días → $" + subtotal;
    }
}
