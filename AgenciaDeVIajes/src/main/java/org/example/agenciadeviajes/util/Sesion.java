package org.example.agenciadeviajes.util;

import org.example.agenciadeviajes.model.Usuario;

/**
 * Clase de sesión para mantener el usuario autenticado en la aplicación.
 * Implementa patrón Singleton implícito con métodos estáticos.
 */
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

    /**
     * NUEVO: Verifica si el usuario actual es ADMIN
     * @return true si el usuario está autenticado y tiene rol ADMIN
     */
    public static boolean esAdmin() {
        return usuarioActual != null && usuarioActual.esAdmin();
    }

    /**
     * NUEVO: Verifica si el usuario actual es CLIENTE
     * @return true si el usuario está autenticado y tiene rol CLIENTE
     */
    public static boolean esCliente() {
        return usuarioActual != null && usuarioActual.esCliente();
    }

    /**
     * NUEVO: Obtiene el nombre completo del usuario actual
     */
    public static String getNombreActual() {
        return usuarioActual != null ? usuarioActual.getNombreCompleto() : "Invitado";
    }

    /**
     * NUEVO: Obtiene el rol del usuario actual
     */
    public static String getRolActual() {
        return usuarioActual != null ? usuarioActual.getRol() : null;
    }

    /**
     * NUEVO: Obtiene el ID del usuario actual
     */
    public static int getIdActual() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : -1;
    }

    /**
     * NUEVO: Verifica si el usuario tiene un rol específico
     */
    public static boolean tieneRol(String rol) {
        return usuarioActual != null && rol.equalsIgnoreCase(usuarioActual.getRol());
    }
}
