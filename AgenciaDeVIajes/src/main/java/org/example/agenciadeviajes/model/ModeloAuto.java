package org.example.agenciadeviajes.model;

public class ModeloAuto {
    private int idModelo;
    private String marca;
    private String modelo;
    private String categoria;

    public ModeloAuto() {}

    public ModeloAuto(int idModelo, String marca, String modelo, String categoria) {
        this.idModelo = idModelo;
        this.marca = marca;
        this.modelo = modelo;
        this.categoria = categoria;
    }

    public int getIdModelo() {
        return idModelo; }
    public void setIdModelo(int idModelo) {
        this.idModelo = idModelo; }

    public String getMarca() {
        return marca; }
    public void setMarca(String marca) {
        this.marca = marca; }

    public String getModelo() {
        return modelo; }
    public void setModelo(String modelo) {
        this.modelo = modelo; }

    public String getCategoria() {
        return categoria; }
    public void setCategoria(String categoria) {
        this.categoria = categoria; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + categoria + ")"; }
}
