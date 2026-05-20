package org.example.agenciadeviajes.model;


public class Pais {
    private int idPais;
    private String nombre;
    private String codigoIso;

    public Pais() {}

    public Pais(int idPais, String nombre, String codigoIso) {
        this.idPais = idPais;
        this.nombre = nombre;
        this.codigoIso = codigoIso;
    }

    public int getIdPais() {
        return idPais; }
    public void setIdPais(int idPais) {
        this.idPais = idPais; }

    public String getNombre()
    { return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getCodigoIso() {
        return codigoIso; }
    public void setCodigoIso(String codigoIso) {
        this.codigoIso = codigoIso; }

    @Override
    public String toString() {
        return nombre; }
}
