package org.example.agenciadeviajes.util;

import org.example.agenciadeviajes.model.Usuario;

public class Sesion {

    private static Usuario usuarioActual;

    // GUARDAR USUARIO EN SESIÓN
    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    // OBTENER USUARIO ACTUAL
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // CERRAR SESIÓN
    public static void cerrarSesion() {
        usuarioActual = null;
    }

    // VALIDAR SI HAY SESIÓN
    public static boolean haySesion() {
        return usuarioActual != null;
    }
}