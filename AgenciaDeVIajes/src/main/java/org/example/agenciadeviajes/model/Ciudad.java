package org.example.agenciadeviajes.model;
public class Ciudad {
    private int idCiudad;
    private String nombre;
    private String codigoIata;
    private Pais pais;

    public Ciudad() {}

    public Ciudad(int idCiudad, String nombre, String codigoIata, Pais pais) {
        this.idCiudad = idCiudad;
        this.nombre = nombre;
        this.codigoIata = codigoIata;
        this.pais = pais;
    }

    public int getIdCiudad() {
        return idCiudad; }
    public void setIdCiudad(int idCiudad) {
        this.idCiudad = idCiudad; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getCodigoIata() {
        return codigoIata; }
    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata; }

    public Pais getPais() {
        return pais; }
    public void setPais(Pais pais) {
        this.pais = pais; }

    @Override
    public String toString() {
        return nombre + " (" + codigoIata + ")"; }
}
