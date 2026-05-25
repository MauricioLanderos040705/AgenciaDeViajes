package org.example.agenciadeviajes.strategy;

import java.util.List;

/**
 * Context del Strategy Pattern
 *
 * Mantiene una referencia a la estrategia de búsqueda actual y
 * delega la ejecución a la estrategia específica.
 *
 * Permite cambiar la estrategia de búsqueda en tiempo de ejecución
 * sin alterar el código cliente.
 *
 * Ejemplo:
 * BusquedaContext<Vuelo> contexto = new BusquedaContext<>();
 * contexto.setEstrategia(new BusquedaVueloStrategy(...));
 * List<Vuelo> resultados = contexto.ejecutarBusqueda();
 *
 * @param <T> Tipo de objeto a buscar
 */
public class BusquedaContext<T> {

    private BusquedaStrategy<T> estrategia;

    public BusquedaContext() {
    }

    public BusquedaContext(BusquedaStrategy<T> estrategiaInicial) {
        this.estrategia = estrategiaInicial;
    }

    /**
     * Establece la estrategia de búsqueda a usar
     */
    public void setEstrategia(BusquedaStrategy<T> nuevaEstrategia) {
        if (nuevaEstrategia == null) {
            throw new IllegalArgumentException("La estrategia no puede ser nula");
        }
        this.estrategia = nuevaEstrategia;
    }

    /**
     * Ejecuta la búsqueda usando la estrategia actual
     *
     * @return Lista de resultados según la estrategia
     * @throws IllegalStateException si no hay estrategia configurada
     */
    public List<T> ejecutarBusqueda() throws IllegalStateException {
        if (estrategia == null) {
            throw new IllegalStateException("No hay estrategia de búsqueda configurada");
        }

        try {
            estrategia.validar();
            return estrategia.buscar();
        } catch (IllegalArgumentException e) {
            System.err.println("Error en validación: " + e.getMessage());
            throw new IllegalStateException("La búsqueda no cumple los criterios requeridos", e);
        }
    }

    /**
     * Obtiene una descripción de la búsqueda actual
     */
    public String obtenerDescripcionBusqueda() {
        if (estrategia == null) {
            return "Sin estrategia configurada";
        }
        return estrategia.obtenerDescripcion();
    }

    /**
     * Obtiene el nombre de la estrategia actual
     */
    public String getNombreEstrategiaActual() {
        if (estrategia == null) {
            return "Ninguna";
        }
        return estrategia.getNombreEstrategia();
    }

    /**
     * Verifica si hay estrategia configurada
     */
    public boolean tieneEstrategia() {
        return estrategia != null;
    }

    /**
     * Ejecuta la búsqueda y maneja excepciones
     *
     * @return Lista de resultados (vacía si hay error)
     */
    public List<T> ejecutarBusquedaSegura() {
        try {
            return ejecutarBusqueda();
        } catch (IllegalStateException e) {
            System.err.println("[BusquedaContext] Error: " + e.getMessage());
            return List.of(); // Retorna lista vacía en caso de error
        }
    }

    /**
     * Valida la estrategia actual sin ejecutar la búsqueda
     *
     * @return true si la estrategia es válida, false si hay error
     */
    public boolean validarEstrategia() {
        if (estrategia == null) {
            return false;
        }

        try {
            estrategia.validar();
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Validación fallida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el mensaje de error de validación
     *
     * @return Mensaje de error o null si es válida
     */
    public String obtenerErrorValidacion() {
        if (estrategia == null) {
            return "Estrategia no configurada";
        }

        try {
            estrategia.validar();
            return null; // Sin errores
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
    public BusquedaStrategy<T> getEstrategiaActual() {
        return estrategia;
    }
}
