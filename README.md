Agencia de Viajes
Aplicación JavaFX para gestión integral de viajes
Descripción General
Agencia de Viajes es una aplicación desarrollada en Java que emplea JavaFX para la interfaz gráfica y MySQL como sistema de gestión de bases de datos.
Funcionalidades principales
•	Reservas de vuelos
•	Reservas de hoteles
•	Reservas de autos
•	Gestión de usuarios y autenticación
•	Administración de catálogos
•	Gestión de reservaciones
La aplicación está estructurada siguiendo una arquitectura por capas, separando la lógica de negocio, acceso a datos, controladores e interfaces.
Tecnologías utilizadas
•	Java
•	JavaFX
•	MySQL
•	JDBC
•	IntelliJ IDEA
•	JavaDoc
Estructura del Proyecto
El proyecto está organizado por paquetes para mantener una estructura limpia.
•	org.example.agenciadeviajes
•	admin
•	controller
•	dao
•	factory
•	model
•	strategy
•	util
Descripción de Paquetes
Admin
Este paquete integra todas las funcionalidades destinadas a los administradores del sistema. Permite gestionar información crítica sin modificar directamente la base de datos, diferenciando claramente las acciones administrativas de las normales de los clientes.
•	Agregar nuevos hoteles
•	Agregar vuelos
•	Modificar información existente
•	Eliminar registros
•	Controlar información importante del sistema
•	Administrar catálogos principales
Sin este paquete, toda la información tendría que agregarse manualmente desde MySQL. El módulo administrativo convierte el sistema en una aplicación realmente funcional y administrable.
Ejemplo de uso
1.	El administrador accede al panel administrativo
2.	Captura los datos del vuelo
3.	El controlador administra el evento
4.	El DAO guarda la información en MySQL
5.	El sistema actualiza automáticamente los datos visibles para los usuarios
Controller
Contiene los controladores JavaFX encargados de conectar la interfaz gráfica, la lógica del sistema y la base de datos, sirviendo de puente entre lo que el usuario ve y lo que el sistema hace.
•	Escuchar eventos de la interfaz
•	Validar información
•	Comunicarse con los DAO
•	Actualizar vistas
•	Navegar entre ventanas
•	Mostrar mensajes al usuario
DAO
Implementa el patrón DAO (Data Access Object), separando la lógica de acceso a datos del resto del sistema. Todas las consultas SQL se concentran aquí, facilitando el mantenimiento.
•	Conectarse a MySQL
•	Ejecutar consultas SQL
•	Insertar, actualizar y eliminar registros
•	Obtener resultados
•	Funciones típicas: insertar(), actualizar(), eliminar(), obtenerTodos(), buscarPorId()
Model
Contiene las entidades principales del sistema, como usuarios, vuelos, hoteles, autos y reservaciones. Cada objeto model normalmente representa una tabla en MySQL y almacena información relevante.
Factory
Centraliza y controla la creación de objetos dentro del sistema, evitando código repetido, dependencia excesiva y facilitando el mantenimiento.
•	Decide qué objeto crear
•	Cómo inicializarlo
•	Qué implementación devolver
Ejemplo: crea automáticamente objetos de reservación según el tipo solicitado (vuelo, hotel, auto). Si en el futuro se agregan cruceros o tours, solo se modifica la Factory.
Strategy
Permite cambiar comportamientos del sistema dinámicamente sin modificar el código principal. Cada lógica (por ejemplo, distintos tipos de descuentos) se separa en clases independientes.
•	Intercambiar algoritmos fácilmente
•	Reutilizar comportamientos
•	Evitar grandes bloques de if/else
Util
Contiene herramientas auxiliares reutilizables, como conexiones JDBC, validaciones, utilidades generales, configuraciones y helpers. La conexión a base de datos habitualmente se gestiona aquí.
Funcionalidades del Sistema
•	Inicio de Sesión: Autenticación por correo y contraseña, acceso personalizado según el usuario.
•	Registro de Usuarios: Permite registrarse proporcionando nombre, apellido, correo y contraseña.
•	Gestión de Vuelos: Consulta y reserva de vuelos, visualización de información de aerolínea, ciudades, fechas y horarios.
•	Gestión de Hoteles: Consulta y reserva de hoteles, información de ubicación, fechas de hospedaje y habitaciones.
•	Gestión de Autos: Consulta y reserva de vehículos, categorías y fechas de renta.
•	Gestión de Reservaciones: Visualización de reservas realizadas, historial del usuario y detalles.
Patrones de Diseño Utilizados
•	DAO: Separación de lógica de acceso a datos, mejor mantenimiento, responsabilidades claras y código limpio.
•	Factory: Creación centralizada de objetos, reutilización de código, escalabilidad y menor acoplamiento.
•	Strategy: Flexibilidad para implementar diferentes comportamientos, mantenimiento fácil y extensible.
Base de Datos
El sistema utiliza MySQL como gestor de base de datos. Tablas principales: usuarios, vuelos, hoteles, autos, reservas, detalles de reserva, ciudades y aerolíneas.
Configuración del Proyecto
•	Java JDK 17 o superior
•	MySQL Server
•	Maven
•	IntelliJ IDEA
1.	Clonar el repositorio con git clone
2.	Abrir en IntelliJ IDEA la carpeta raíz del proyecto
3.	Configurar MySQL actualizando las credenciales de conexión
4.	Instalar dependencias con mvn clean install
5.	Ejecutar la aplicación desde Launcher.java o HelloApplication.java
6.	Generar JavaDoc desde IntelliJ IDEA (Tools → Generate JavaDoc) o terminal
Características Técnicas Destacadas
•	Arquitectura por capas
•	Interfaz moderna con JavaFX
•	Persistencia con JDBC
•	Uso de patrones de diseño
•	Modularidad y código reutilizable
•	Separación de responsabilidades

