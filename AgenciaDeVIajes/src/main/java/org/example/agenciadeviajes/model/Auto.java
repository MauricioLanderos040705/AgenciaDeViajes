package org.example.agenciadeviajes.model;
import java.math.BigDecimal;
public class Auto {
    private int idAuto;
    private ModeloAuto modeloAuto;
    private String proveedor;
    private Ciudad ciudadDisponibilidad;
    private BigDecimal precioDia;
    private boolean disponible;
    private String codigoDivisa;

    public Auto() {}

    public Auto(int idAuto, ModeloAuto modeloAuto, String proveedor,
                Ciudad ciudadDisponibilidad, BigDecimal precioDia,
                boolean disponible, String codigoDivisa) {
        this.idAuto = idAuto;
        this.modeloAuto = modeloAuto;
        this.proveedor = proveedor;
        this.ciudadDisponibilidad = ciudadDisponibilidad;
        this.precioDia = precioDia;
        this.disponible = disponible;
        this.codigoDivisa = codigoDivisa;
    }

    public int getIdAuto() {
        return idAuto; }
    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto; }

    public ModeloAuto getModeloAuto() {
        return modeloAuto; }
    public void setModeloAuto(ModeloAuto modeloAuto) {
        this.modeloAuto = modeloAuto; }

    public String getProveedor() {
        return proveedor; }
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor; }

    public Ciudad getCiudadDisponibilidad() {
        return ciudadDisponibilidad; }
    public void setCiudadDisponibilidad(Ciudad ciudad) {
        this.ciudadDisponibilidad = ciudad; }

    public BigDecimal getPrecioDia() {
        return precioDia; }
    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia; }

    public boolean isDisponible() {
        return disponible; }
    public void setDisponible(boolean disponible) {
        this.disponible = disponible; }

    public String getCodigoDivisa() {
        return codigoDivisa; }
    public void setCodigoDivisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa; }

    @Override
    public String toString() {

        return modeloAuto + " | " + proveedor + " | $" + precioDia + "/día";
    }
}
