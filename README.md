# Proyecto API "usuariofacto" GlobalLogic
API RESTful construida con SpringBoot para el registro y autenticación de usuarios, utilizando almacenamiento en memoria con H2 y validaciones personalizadas.

## 📌 Tecnologías utilizadas
- Java 11
- SpringBoot 2.5.14
- Spring Data JPA
- Base de datos en memoria H2
- JWT (Json Web Token)
- JUnit 5 para pruebas
- Gradle 7.4

## 🧱 Arquitectura del proyecto

El proyecto sigue una arquitectura limpia dividida en capas:
- entrypoints/
  - Controladores REST (_UserController_)
- service/
  - Lógica de negocio (_UsuarioService_)
- usercase/
  - Interfaces para casos de uso (_UsuarioUseCase_)
- data/
  - Repositorios JPA (_UsuarioRepository, JpaUserRepository_)
- helpers/
  - Utilitarios (_JwtUtil, PasswordEncryptor_)
- model/
  - Clases de dominio (_Usuario, Phone_)
- entities/
  - Entidades JPA (_UserEntity, PhoneEntity_)

## ⚙️ Construcción del proyecto

### 1. Requisitos

- JDK 11
- Gradle 7.4
- IDE: IntelliJ IDEA 2025.1.1.1 (Community Edition)
 
### 2. Compilar y empaquetar

Se puede compilar y generar el JAR mediante:

`./gradlew build`

### 3. Ejecutar la aplicación
Se puede ejecutar la aplicación utilizando el JAR generado:

`java -jar target/usuario-api.jar`

O se puede ejecutar utilizando el IDE con solo ejecutar la clase principal:
_UsuariofactoApplication.java_

## 🚀 Endpoints principales
### URL: http://localhost:8080/sign_up

Método: POST

Header:
- Content-Type: application/json

Body (Json):
<pre>{ 
"name": "Pedro Perez", 
"password": "a2asfGfdfdf4", 
"email": "a2asfGfdfdf4A1bcdefgh9@example.com", 
"phones": [{
        "number": 3002224567, 
        "citycode": 5, 
        "contrycode": "57" 
    }] 
}
</pre>

Validaciones:
- El email debe tener un formato válido.
- El password debe tener:
  - Exactamente una letra mayúscula
  - Exactamente dos números
  - Solo letras y números (sin símbolos)
  - Longitud entre 8 y 12 caracteres
- Si el correo ya existe, se lanza un error.

Respuesta (Json) (Code 200 OK):
<pre>
{
"id": "84814cb1-54de-40ae-880a-ba504c149bde",
"name": "Pedro Perez",
"email": "A1bcdefgh9@example.com",
"password": "97a599eaf776a7bcbe4a778a380b5915f2fa11ec0e5fb668f4c950b46a03401f",
"phones": [{
    "number": 3002224567,
    "citycode": 5,
    "contrycode": "57"
}],
"created": "2025-06-16T11:48:27.4664266",
"lastLogin": "2025-06-16T11:48:27.4664266",
"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBMWJjZGVmZ2g5QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUwMDkyNTA3fQ.Vtb6FPJuI57dmfv6K0kkrEXxbNNFG-XaFZ03hvYtpVhBhIV1b4rYUaqaSvt5d_-MDrmGTDnQvBGqhGiDDyjGEA",
"active": true
}
</pre>

Respuesta (Json)(Error):
<pre>
{
    "error": [
        {
            "timestamp": "2025-06-16T11:56:42.5426643",
            "codigo": 400,
            "detail": "El password debe tener entre 8 y 12 caracteres: ************"
        }
    ]
}
</pre>


### URL: http://localhost:8080/login
Método: GET

Header:
- Content-Type: application/json
- Authorization (<JWT Token obtenido en /sign_up>): Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBMWJjZGVmZ2g5QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUwMDkyNTA3fQ.Vtb6FPJuI57dmfv6K0kkrEXxbNNFG-XaFZ03hvYtpVhBhIV1b4rYUaqaSvt5d_-MDrmGTDnQvBGqhGiDDyjGEA

Body (Json): vacio

Respuesta (Json) (Code 200 OK) (El mismo usuario con un nuevo token):
<pre>
{
    "id": "84814cb1-54de-40ae-880a-ba504c149bde",
    "name": "Pedro Perez",
    "email": "A1bcdefgh9@example.com",
    "password": "97a599eaf776a7bcbe4a778a380b5915f2fa11ec0e5fb668f4c950b46a03401f",
    "phones": [
        {
            "number": 3002224567,
            "citycode": 5,
            "contrycode": "57"
        }
    ],
    "created": "2025-06-16T11:48:27.466427",
    "lastLogin": "2025-06-16T11:55:50.888361",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBMWJjZGVmZ2g5QGV4YW1wbGUuY29tIiwiaWF0IjoxNzUwMDkyOTUwfQ.DawUxuMxR9hqNvZU61a5wrlY1tFgG1ICoUAFmpC-0-xSim2pcXonriYxH7YNdf3Sj7yNG0YROBchFCz_vFUPNg",
    "active": true
}
</pre>

Respuesta (Json)(Error):
<pre>
{
    "error": [{
            "timestamp": "2025-06-16T12:00:33.0882616",
            "codigo": 400,
            "detail": "Usuario no encontrado con el token proporcionado"
        }]
}
</pre>

## 🔐 Seguridad
- El token JWT se genera en JwtUtil con algoritmo HS512.
- Se almacena el hash del password usando SHA-256 en PasswordEncryptor.

## 🚀 Manejo de Excepciones
Se manejan las excepciones con anotación `@ExceptionHandler` para manejo global controlado por la clase `GlobalExceptionHandler.java`

## 🧪 Pruebas
Se contruyeron test uniarios utilizando Junit 5, se obtienen 100% de coberturas y se incluyen pruebas para:
- Servicio: _UsuarioService_
- Utilitarios: _JwtUtil, PasswordEncryptor_

## 📂 Base de datos H2
Se utiliza base de datos en memoria H2 y utilizamos JPA para interacción.<br>
La configuración para creación y accesos se realiza en _**application.properties**_

Para acceso a la consola en tiempo de ejecución:<br>
http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:usuariosdb
- User: sa
- Password: (vacío)

Luego de ejecutar el endpoint sign-up con resultado satisfactorio, se puede consultar las tablas en la consola con las instrucciones:

<pre>select * from users;
select * from phones;</pre>

## 📌 Características de Java 11 utilizadas
- `String.isBlank()` para validar campos vacíos.
- `String.repeat(12)` para construcción dinámica de mensajes de error.

## 🧩 Diagramas del proyecto
Se incluyen los digramas de componente, diagrama de secuencia Sign-Up y diagrama de secuencia Login en la carpeta _**diagramas**_

## ✍️ Autor
**Silvio Walton**
