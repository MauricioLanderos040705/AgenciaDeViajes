package org.example.agenciadeviajes.model;


import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasenia;   // almacenado como hash SHA-256
    private Timestamp fechaRegistro;
    private String rol;            // NUEVO: 'ADMIN' o 'CLIENTE'

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido, String correo,
                   String contrasenia, Timestamp fechaRegistro) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
        this.rol = "CLIENTE"; // rol por defecto
    }

    // Constructor completo CON rol
    public Usuario(int idUsuario, String nombre, String apellido, String correo,
                   String contrasenia, Timestamp fechaRegistro, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
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

    // NUEVO: getters y setters para rol
    public String getRol() {
        return rol; }
    public void setRol(String rol) {
        this.rol = rol; }

    // NUEVO: métodos helper para validar rol
    public boolean esAdmin() {
        System.out.println("ADMIN");

        return "ADMIN".equalsIgnoreCase(rol);
    }

    public boolean esCliente()   {
        System.out.println("cliente");

        return "CLIENTE".equalsIgnoreCase(rol);
    }

    @Override
    public String toString() { return nombre + " " + apellido + " <" + correo + "> [" + rol + "]"; }
}
