package org.example.agenciadeviajes.util;

/**
 * Utilidad para validar permisos de usuario según su rol.
 * Facilita el control de acceso en la aplicación.
 */
public class PermisosUtil {

    /**
     * Verifica si el usuario actual puede realizar operaciones de ADMIN
     * @return true si el usuario es ADMIN y está autenticado
     */
    public static boolean puedeRegistrarAerolineas() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEditarAerolineas() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEliminarAerolineas() {
        return Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede registrar hoteles
     */
    public static boolean puedeRegistrarHoteles() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEditarHoteles() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEliminarHoteles() {
        return Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede registrar autos
     */
    public static boolean puedeRegistrarAutos() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEditarAutos() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEliminarAutos() {
        return Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede registrar vuelos
     */
    public static boolean puedeRegistrarVuelos() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEditarVuelos() {
        return Sesion.esAdmin();
    }

    public static boolean puedeEliminarVuelos() {
        return Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede crear reservas
     */
    public static boolean puedeCrearReservas() {
        return Sesion.esCliente() || Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede ver sus propias reservas
     */
    public static boolean puedeVerReservas() {
        return Sesion.haySesion();
    }

    /**
     * Verifica si el usuario actual puede ver reportes
     */
    public static boolean puedeVerReportes() {
        return Sesion.esAdmin();
    }

    /**
     * Verifica si el usuario actual puede gestionar otros usuarios
     */
    public static boolean puedeGestionarUsuarios() {
        return Sesion.esAdmin();
    }

    /**
     * Método genérico para verificar si el usuario tiene un rol específico
     */
    public static boolean tienePermiso(String rol) {
        return Sesion.tieneRol(rol);
    }

    /**
     * Muestra un mensaje de error cuando el usuario no tiene permiso
     */
    public static String getMensajeNoAutorizado() {
        return "No tienes permiso para realizar esta acción. Requiere rol ADMIN.";
    }
}
