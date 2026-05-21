package org.example.agenciadeviajes.model;
import java.math.BigDecimal;
public class Auto {
    private int idAuto;
    private ModeloAuto modeloAuto;
    private String proveedor;
    private Ciudad ciudadDisponibilidad;
    private BigDecimal precioDia;
    private String codigoDivisa;
    private int idProveedor;



    public Auto(int idAuto, ModeloAuto modeloAuto, String proveedor, int idProveedor,
                Ciudad ciudadDisponibilidad, BigDecimal precioDia, String codigoDivisa) {
        this.idAuto = idAuto;
        this.modeloAuto = modeloAuto;
        this.proveedor = proveedor;
        this.idProveedor = idProveedor;
        this.ciudadDisponibilidad = ciudadDisponibilidad;
        this.precioDia = precioDia;
        this.codigoDivisa = codigoDivisa;
    }
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
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

    public String getCodigoDivisa() {
        return codigoDivisa; }
    public void setCodigoDivisa(String codigoDivisa) {
        this.codigoDivisa = codigoDivisa; }

    @Override
    public String toString() {

        return modeloAuto + " | " + proveedor + " | $" + precioDia + "/día";
    }
}
