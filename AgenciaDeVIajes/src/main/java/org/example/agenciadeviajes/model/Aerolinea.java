package org.example.agenciadeviajes.model;

public class Aerolinea {
    private int idAerolinea;
    private String nombre;
    private String codigoIata;

    public Aerolinea() {}

    public Aerolinea(int idAerolinea, String nombre, String codigoIata) {
        this.idAerolinea = idAerolinea;
        this.nombre = nombre;
        this.codigoIata = codigoIata;
    }

    public int getIdAerolinea() {
        return idAerolinea; }
    public void setIdAerolinea(int idAerolinea) {
        this.idAerolinea = idAerolinea; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getCodigoIata() {
        return codigoIata; }
    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata; }

    @Override
    public String toString() {
        return codigoIata + " - " + nombre; }
}
