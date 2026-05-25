package org.example.agenciadeviajes.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // ── Configuración
    private static final String URL = "jdbc:mysql://localhost:3306/agencia_viajes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Mexico_City";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "mauricio";

    // ── Singleton
    private static ConexionDB instancia;
    private Connection conexion;

    /** Constructor privado — impide instanciación externa */
    private ConexionDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[ConexionDB] Conexión establecida con éxito.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la BD: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna la única instancia de ConexionDB.
     * Crea la conexión la primera vez que se invoca.
     */
    public static ConexionDB getInstance() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection para usar en los DAOs.
     * Si la conexión fue cerrada o perdida, la reconecta automáticamente.
     */
    public Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                System.out.println("[ConexionDB] Reconectando...");
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la conexión: " + e.getMessage(), e);
        }
        return conexion;
    }

    /** Cierra la conexión. Llama al cerrar la aplicación (opcional). */
    public void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                instancia = null;
                System.out.println("[ConexionDB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
