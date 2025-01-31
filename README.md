# WebServer Sistema de Reservas

Este proyecto es un sistema de reservas que permite hacer su gestión a 
través de una interfaz web y una API REST. Además, implementa un servidor web en Java utilizando
Maven, capaz de manejar solicitudes HTTP para proporcionar archivos estáticos
y responder a solicitudes REST para gestionar reservas y mostrar imágenes.

-------------
## 📍 Comenzando
Estas instrucciones te permitirán obtener una copia del proyecto en funcionamiento en tu máquina local para propósitos de desarrollo y pruebas.

### 🔧 Prerrequisitos

Para ejecutar el proyecto necesitas instalar:
- [Java JDK 8 o superior](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- Un navegador Web.
- Utiliza el entorno de desarrollo integrado (IDE) de tu preferencia, como por ejemplo: 
  - IntelliJ IDEA, 
  - Eclipse 
  - Apache NetBeans
------------------
### ⚙️ Instalación

**1. Clona el repositorio:**
   ```sh
   git clone https://github.com/saraygonm/AREP-Taller01.git
   ```

**2. Entra en la carpeta del proyecto:**
   ```sh
   cd AREP-Taller01
   ```

**3. Compila el proyecto con Maven (debe estar previamente configurado):**
   ```sh
   mvn clean package
   ```

**4. Inicia el servidor(Server.java):**
   ```sh
   mvn exec:java
   ```

**5. Accede a la aplicación, buscando en tu navegador:**
   ```
   http://localhost:8080
   ```
*Una vez iniciado el servidor, podrás visualizar la página web en tu navegador.
Además, en la terminal de tu IDE seleccionado, podrás monitorear las 
peticiones HTTP junto con la información relevante que se esté procesando en 
la aplicación.*


---------------------
### ✅ Ejecutar las pruebas

Para ejecutar las pruebas automáticas, usa el siguiente comando:
```sh
mvn test
```

Las pruebas incluyen validaciones de:
- Conexión al servidor
- Creación de reservas
- Eliminación de reservas
- Obtención de datos de la API
------------------------------
### 🏗️ Arquitectura

#### Servidor Java

- Clase principal: Server

- Puerto: 8080

- Raíz web: Recursos/static (directorio desde donde se sirven los archivos estáticos).

#### Manejo de solicitudes:

**GET:**

- `/api/services:` Devuelve una lista de todas las reservas en formato JSON.

- `/api/imgTarjeta:` Devuelve la imagen de la tarjeta almacenada en el servidor.

- Otros archivos estáticos como index.html, style.css y script.js se sirven desde el directorio Recursos/static.

**POST:**

- `/api/services:` Agrega una nueva reserva a la lista de reservas. El cuerpo de la solicitud debe contener un JSON con la reserva.

**DELETE:**

- `/api/services/{id}:` Elimina una reserva específica.

- `/api/services/clear:` Borra todas las reservas.

-------------------- 
### 🌐 Frontend

-- `HTML:` index.html – Interfaz básica para interactuar con el servidor.

-- `CSS:` style.css – Estilos aplicados para hacer que la interfaz se vea profesional y agradable.

-- `JavaScript:` script.js – Maneja la lógica del cliente, como enviar solicitudes al servidor y mostrar los resultados.

-------
### 🚀 Despliegue

Para desplegar este sistema en un entorno en vivo, asegúrate de tener un servidor donde ejecutar la aplicación con Java y Maven.


-----
### 🛠️ Construido con

- [Java](https://www.oracle.com/java/) - Lenguaje de programación
- [Maven](https://maven.apache.org/) - Gestión de dependencias y compilación
- [JUnit](https://junit.org/) - Framework de pruebas

### 👨🏼‍💻 Autora

- **Saray Mendivelso** - Desarrollo inicial


