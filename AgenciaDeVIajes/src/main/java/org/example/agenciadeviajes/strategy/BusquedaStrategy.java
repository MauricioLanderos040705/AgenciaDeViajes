package org.example.agenciadeviajes.strategy;

import java.util.List;

/**
 * Strategy Pattern - Interfaz para diferentes estrategias de búsqueda
 *
 * Define el contrato para todas las estrategias de búsqueda en el sistema.
 * Permite cambiar el algoritmo de búsqueda en tiempo de ejecución.
 *
 * @param <T> Tipo de objeto a buscar (Vuelo, Hotel, Auto)
 */
public interface BusquedaStrategy<T> {

    /**
     * Ejecuta la búsqueda según la estrategia específica
     *
     * @return Lista de resultados encontrados
     */
    List<T> buscar();

    /**
     * Retorna una descripción de la búsqueda realizada
     *
     * @return Descripción en texto de los criterios aplicados
     */
    String obtenerDescripcion();

    /**
     * Retorna el nombre de la estrategia
     *
     * @return Nombre de la estrategia (Vuelo, Hotel, Auto)
     */
    String getNombreEstrategia();

    /**
     * Valida que los parámetros de búsqueda sean válidos
     *
     * @throws IllegalArgumentException si los parámetros son inválidos
     */
    void validar() throws IllegalArgumentException;
}
