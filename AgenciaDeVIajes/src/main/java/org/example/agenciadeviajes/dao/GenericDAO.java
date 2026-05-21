package org.example.agenciadeviajes.dao;


import java.util.List;

/**
 * Interfaz genérica CRUD que todos los DAOs deben implementar.
 * Cubre el requerimiento de programación genérica de la materia.
 *
 * @param <T> Tipo del modelo de dominio (Usuario, Vuelo, Hotel, Auto...)
 */
public interface GenericDAO<T> {

    /**
     * Inserta un nuevo registro en la BD.
     * @return true si la operación fue exitosa
     */
    boolean insertar(T objeto);

    /**
     * Busca un registro por su id primario.
     * @return El objeto encontrado, o null si no existe
     */
    T obtenerPorId(int id);

    /**
     * Obtiene todos los registros de la tabla.
     */
    List<T> obtenerTodos();

    /**
     * Actualiza un registro existente.
     * @return true si se modificó al menos una fila
     */
    boolean actualizar(T objeto);

    /**
     * Elimina un registro por su id.
     * @return true si se eliminó al menos una fila
     */
    boolean eliminar(int id);
}
