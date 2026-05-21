package org.example.agenciadeviajes.model;


import java.math.BigDecimal;

public class Divisa {
    private int idDivisa;
    private String nombre;
    private String simbolo;
    private String codigo;
    private BigDecimal tasaCambio;

    public Divisa() {}

    public Divisa(int idDivisa, String nombre, String simbolo, String codigo, BigDecimal tasaCambio) {
        this.idDivisa = idDivisa;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.codigo = codigo;
        this.tasaCambio = tasaCambio;
    }

    public int getIdDivisa() {
        return idDivisa; }
    public void setIdDivisa(int idDivisa) {
        this.idDivisa = idDivisa; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getSimbolo() {
        return simbolo; }
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo; }

    public String getCodigo() {
        return codigo; }
    public void setCodigo(String codigo) {
        this.codigo = codigo; }

    public BigDecimal getTasaCambio() {
        return tasaCambio; }
    public void setTasaCambio(BigDecimal tasaCambio) {
        this.tasaCambio = tasaCambio; }

    @Override
    public String toString() { return codigo + " - " + nombre; }
}
