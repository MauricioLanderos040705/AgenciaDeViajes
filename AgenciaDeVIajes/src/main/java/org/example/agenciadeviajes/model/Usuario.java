package org.example.agenciadeviajes.model;


import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;   // almacenado como hash SHA-256
    private Timestamp fechaRegistro;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido, String correo,
                   String contrasenia, Timestamp fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuario() {
        return idUsuario; }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getApellido() {
        return apellido; }
    public void setApellido(String apellido) {
        this.apellido = apellido; }

    public String getNombreCompleto() {
        return nombre + " " + apellido; }

    public String getCorreo() {
        return correo; }
    public void setCorreo(String correo) {
        this.correo = correo; }

    public String getContrasenia() {
        return contrasenia; }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia; }

    public Timestamp getFechaRegistro() {
        return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() { return nombre + " " + apellido + " <" + correo + ">"; }
}
