Agencia de Viajes:
Proyecto desarrollado en JavaFX y MySQL para la gestión de una agencia de viajes.
El sistema permite a los usuarios registrarse, iniciar sesión, consultar servicios y realizar reservas de vuelos, hoteles y autos.

Descripción:
Este proyecto fue desarrollado como una aplicación de escritorio utilizando JavaFX y una base de datos MySQL.
La aplicación simula el funcionamiento básico de una agencia de viajes, permitiendo gestionar diferentes tipos de reservas desde una sola plataforma.
El sistema cuenta con autenticación de usuarios, consultas de vuelos, consultas de hoteles, consultas de autos, creación de reservas y visualización de reservas realizadas.

Tecnologías utilizadas

Java 21
JavaFX
MySQL
JDBC
Maven
IntelliJ IDEA

Funcionalidades principales

Login de usuarios:
Los usuarios pueden iniciar sesión usando correo y contraseña.
La contraseña se almacena en la base de datos utilizando hash SHA-256 para mayor seguridad.

Registro de usuarios:
Permite crear nuevas cuentas validando campos vacíos y correos duplicados.

Home principal:
Después de iniciar sesión el usuario accede a un menú principal desde donde puede navegar a vuelos, hoteles, autos y mis reservas.

Consultas:
Consulta de vuelos
El usuario puede consultar vuelos disponibles, buscar por origen, destino y fecha, visualizar información de aerolínea y precios, además de reservar vuelos.

Consulta de hoteles:
El usuario puede consultar hoteles disponibles, seleccionar fechas de hospedaje, seleccionar cantidad de habitaciones y realizar reservas.

Consulta de autos:
El usuario puede consultar autos disponibles, seleccionar fechas de renta y reservar autos.

Reservas:
El sistema permite generar reservas reales en la base de datos.
Cada reserva se guarda en la tabla reservas y almacena detalles específicos según el tipo de servicio reservado.

Mis Reservas:
El usuario puede visualizar todas las reservas realizadas desde su cuenta mostrando folio, fecha, tipo de reserva y total pagado.
También se muestran los detalles de cada reserva realizada.

Carpetas y estructuras:
La carpeta controller contiene la lógica de las interfaces JavaFX.
La carpeta dao contiene las clases encargadas de acceder a la base de datos.
La carpeta model contiene los modelos de las entidades del sistema.
La carpeta util contiene utilidades generales como conexión a base de datos, sesión global y hash SHA-256.
La carpeta view contiene los archivos .fxml de las interfaces gráficas.

Características implementadas:
Login
Registro
Sesión global
Consultas de vuelos
Consultas de hoteles
Consultas de autos
Reservas de vuelos
Reservas de hoteles
Reservas de autos
Visualización de reservas
Integración con MySQL
Arquitectura DAO
Hash de contraseñas SHA-256
