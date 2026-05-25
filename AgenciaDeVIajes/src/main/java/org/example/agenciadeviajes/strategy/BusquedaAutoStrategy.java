package org.example.agenciadeviajes.strategy;

import org.example.agenciadeviajes.dao.AutoDAO;
import org.example.agenciadeviajes.model.Auto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy para búsqueda de Automóviles
 *
 * Implementa la estrategia específica para buscar autos según:
 * Ciudad
 * - Fechas de inicio y fin de renta
 * - Categoría o modelo preferido (opcional)
 * - Proveedor preferido (opcional)
 *
 * Ejemplo:
 * BusquedaStrategy<Auto> estrategia = new BusquedaAutoStrategy(
 *     autoDAO, idCiudad, fechaInicio, fechaFin
 * );
 * List<Auto> resultados = estrategia.buscar();
 */
public class BusquedaAutoStrategy implements BusquedaStrategy<Auto> {

    private final AutoDAO autoDAO;
    private final int idCiudadDisponibilidad;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private String categoriaPreferida; // null = cualquier categoría
    private String proveedorPreferido; // null = cualquier proveedor

    /**
     * Constructor básico sin filtros adicionales
     */
    public BusquedaAutoStrategy(AutoDAO autoDAO, int idCiudad,
                              LocalDate inicio, LocalDate fin) {
        this.autoDAO = autoDAO;
        this.idCiudadDisponibilidad = idCiudad;
        this.fechaInicio = inicio;
        this.fechaFin = fin;
    }

    /**
     * Constructor con filtro de categoría
     */
    public BusquedaAutoStrategy(AutoDAO autoDAO, int idCiudad,
                              LocalDate inicio, LocalDate fin,
                              String categoriaPreferida) {
        this(autoDAO, idCiudad, inicio, fin);
        this.categoriaPreferida = categoriaPreferida;
    }

    /**
     * Constructor con filtro de categoría y proveedor
     */
    public BusquedaAutoStrategy(AutoDAO autoDAO, int idCiudad,
                              LocalDate inicio, LocalDate fin,
                              String categoriaPreferida, String proveedorPreferido) {
        this(autoDAO, idCiudad, inicio, fin, categoriaPreferida);
        this.proveedorPreferido = proveedorPreferido;
    }

    @Override
    public List<Auto> buscar() {
        validar();

        // Obtener todos los autos
        List<Auto> todosAutos = autoDAO.obtenerTodos();

        // Filtrar según criterios
        return todosAutos.stream()
                .filter(a -> a.getCiudadDisponibilidad().getIdCiudad() == idCiudadDisponibilidad)
                .filter(a -> categoriaPreferida == null ||
                           a.getModeloAuto().getCategoria().equalsIgnoreCase(categoriaPreferida))
                .filter(a -> proveedorPreferido == null ||
                           a.getProveedor().equalsIgnoreCase(proveedorPreferido))
                .filter(a -> !estaOcupado(a))
                .collect(Collectors.toList());
    }

    @Override
    public String obtenerDescripcion() {
        String desc = String.format(
                "Búsqueda de Autos: Ciudad=%d, Inicio=%s, Fin=%s, Días=%d",
                idCiudadDisponibilidad, fechaInicio, fechaFin,
                ChronoUnit.DAYS.between(fechaInicio, fechaFin)
        );

        if (categoriaPreferida != null) {
            desc += ", Categoría=" + categoriaPreferida;
        }

        if (proveedorPreferido != null) {
            desc += ", Proveedor=" + proveedorPreferido;
        }

        return desc;
    }

    @Override
    public String getNombreEstrategia() {
        return "BusquedaAuto";
    }

    @Override
    public void validar() throws IllegalArgumentException {
        if (idCiudadDisponibilidad <= 0) {
            throw new IllegalArgumentException("ID de ciudad debe ser válido");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fechas de inicio y fin son requeridas");
        }

        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("Fecha de fin debe ser posterior a fecha de inicio");
        }

        // Las categorías y proveedores son opcionales, solo validar si se proporcionan
        if (categoriaPreferida != null && categoriaPreferida.isEmpty()) {
            throw new IllegalArgumentException("Categoría no puede estar vacía");
        }

        if (proveedorPreferido != null && proveedorPreferido.isEmpty()) {
            throw new IllegalArgumentException("Proveedor no puede estar vacío");
        }
    }

    /**
     * Verifica si el auto está ocupado en las fechas especificadas
     * (Simulado: en una implementación real consultaría las reservas)
     */
    private boolean estaOcupado(Auto auto) {
        // Por ahora retorna false
        // En una implementación real:
        // return detalleAutoDAO.obtenerPorFechas(fechaInicio, fechaFin)
        //     .stream()
        //     .anyMatch(d -> d.getAuto().getIdAuto() == auto.getIdAuto());
        return false;
    }

    /**
     * Calcula el número de días de la renta
     */
    public long getNumeroDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Establece la categoría preferida
     */
    public void setCategoriaPreferida(String categoria) {
        this.categoriaPreferida = categoria;
    }

    /**
     * Establece el proveedor preferido
     */
    public void setProveedorPreferido(String proveedor) {
        this.proveedorPreferido = proveedor;
    }

    // GETTERS

    public int getIdCiudadDisponibilidad() {
        return idCiudadDisponibilidad;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getCategoriaPreferida() {
        return categoriaPreferida;
    }

    public String getProveedorPreferido() {
        return proveedorPreferido;
    }
}
