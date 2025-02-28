# WebServer Sistema de Reservas

Este proyecto es un sistema de reservas que permite hacer su gestión a 
través de una interfaz web y una API REST. Además, implementa un servidor web en Java utilizando
Maven, capaz de manejar solicitudes HTTP para proporcionar archivos estáticos
y responder a solicitudes REST para gestionar reservas y mostrar imágenes.



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
   git clone https://github.com/saraygonm/AREP-Taller02.git
   ```

**2. Entra en la carpeta del proyecto:**
   ```sh
   cd AREP-Taller02
   ```

**3. Compila el proyecto con Maven (debe estar previamente configurado):**
   ```sh
   mvn clean package
   ```

**4. Inicia el servidor(Server.java):**
   ```sh
   mvn exec:java
   ```


<p align="center">
<img src="static/images/read/exec.png" alt="" width="700px">
</p>


**5. Accede a la aplicación, buscando en tu navegador:**
   ```
   http://localhost:8080
   ```
*Una vez iniciado el servidor, podrás visualizar la página web en tu navegador.
Además, en la terminal de tu IDE seleccionado, podrás monitorear las 
peticiones HTTP junto con la información relevante que se esté procesando en 
la aplicación.*

<!-- Creación de tabla para alinear las imágenes lado a lado.-->
| <img src="static/images/read/localhost.png" alt="Descarga local" width="500px"> | <img src="static/images/read/terminal.png" alt="Importar carpetas" width="500px"> |
|------------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| **Imagen 1: Página web en navegador**                                        | **Imagen 2: Terminal IDE**                                                        |

---------------------
### ✅ Ejecutar las pruebas

Para ejecutar las pruebas automáticas, el servidor debe estar en ejecución
 ```sh
   mvn exec:java
   ```

<!-- Creación de tabla para alinear las imágenes lado a lado.-->
| <img src="static/images/read/test1.png" alt="Descarga local" width="500px"> | <img src="static/images/read/test2.png" alt="Importar carpetas" width="500px"> |
|-----------------------------------------------------------------------------|--------------------------------------------------------------------------------|
| **Imagen 1: Página web en navegador**                                       | **Imagen 2: Terminal IDE**                                                     |


Las pruebas incluyen validaciones de:
- Endpoints principales (/App/hello, /App/pi)
- API de reservas (GET, POST, DELETE)
- Archivos estáticos (index.html, imágenes)

------------------

###  📚 Endpoints Disponibles en el Servidor

#### ➕ 1. Endpoints para archivos estáticos
- 🌍 [http://localhost:8080/index.html](http://localhost:8080/index.html) → Página principal

<p align="center">
<img src="static/images/read/1.png" alt="" width="700px">
</p>

- 🌍 [http://localhost:8080/static/images/calendario.png](http://localhost:8080/static/images/calendario.png) → Imagen de calendario

<p align="center">
<img src="static/images/read/4.png" alt="" width="700px">
</p>

- 🌍 [http://localhost:8080/static/images/tarjeta.png](http://localhost:8080/static/images/tarjeta.png) → Imagen de tarjeta

<p align="center">
<img src="static/images/read/3.png" alt="" width="700px">
</p>

- 🌍 [http://localhost:8080/static/style.css](http://localhost:8080/static/style.css) → CSS del sitio

<p align="center">
<img src="static/images/read/5.png" alt="" width="700px">
</p>

#### 🔢 2. Endpoints REST (API de reservas)
#### 🔹 Obtener todas las reservas:
- **GET** `http://localhost:8080/api/services`

#### 🟢 Agregar una nueva reserva:
- **POST** `http://localhost:8080/api/services`
- **Cuerpo JSON:**
```json
{
  "nombre": "Juan",
  "fecha": "2025-02-06",
  "hora": "12:00",
  "tarjeta": "1234 5678 9012 3456"
}
```
<p align="center">
<img src="static/images/read/api.png" alt="" width="700px">
</p>

#### 🔴 Eliminar una reserva específica (por índice):
Los navegadores bloquean solicitudes DELETE por seguridad, entonces para probar que funciona la podemos hacer en el terminal de tu IDE de la siguiente manera:
- **DELETE** `http://localhost:8080/api/services/{id}`
- **Ejemplo:** `http://localhost:8080/api/services/0`

<p align="center">
<img src="static/images/read/delete.png" alt="" width="700px">
</p>


#### 💡 3. Endpoints adicionales solicitados en el enunciado
- 🌍 [http://localhost:8080/App/hello?name=Pedro](http://localhost:8080/App/hello?name=Pedro) → Devuelve **"Hello Pedro!"**
<p align="center">
<img src="static/images/read/pedro.png" alt="" width="700px">
</p>

- 🌍 [http://localhost:8080/App/pi](http://localhost:8080/App/pi) → Devuelve **"3.141592653589793"**

<p align="center">
<img src="static/images/read/pi.png" alt="" width="700px">
</p>

----------

### 🏗️ Arquitectura


###  Arquitectura desplegada en Docker
- Construcción de la imagen Docker

<p align="center">
<img src="static/images/docker/DockerConstruyaImagen.png" alt="" width="700px">
</p>

- Verificación de que la imagen fue creada
<p align="center">
<img src="static/images/docker/Dockerimages.png" alt="" width="700px">
</p>

- Se crean y se ejecutan tres contenedores independientes 
<p align="center">
<img src="static/images/docker/docker3.png" alt="" width="700px">
</p>

- Verificación que los contenedores están corriendo

<p align="center">
<img src="static/images/docker/docker-ps.png" alt="" width="700px">
</p>

De igual manera, utilizamos Postman para realizar una solicitud de tipo POST 
<!-- Creación de tabla para alinear las imágenes lado a lado.-->
| <img src="static/images/docker/post-postman.png"  width="500px"> | <img src="static/images/docker/post-postman2.png" width="500px"> | <img src="static/images/docker/post3.png" width="500px"> |
|------------------------------------------------------------------|------------------------------------------------------------------|----------------------------------------------------------|
| **Solicitud Post reserva1**                                      | **Solicitud Post reserva2**                                      | **Solicitud Post reserva3**                              |

Verificación de que los contenedores funcionen correctamente en el navegador.

| <img src="static/images/docker/navegador-postman.png"  width="500px"> | <img src="static/images/docker/navegador-postman2.png" width="500px"> | <img src="static/images/docker/navegador3.png"  width="500px"> |
|-----------------------------------------------------------------------|-----------------------------------------------------------------------|----------------------------------------------------------------|
| **Visualizacion localhost:34000**                 | **Visualizacion localhost:34001**                                     | **Visualizacion localhost:34002**                              |

- Ahora se inician todos los servicios definidos en el docker-compose.yml
<p align="center">
<img src="static/images/docker/dockercompose.png" alt="" width="700px">
</p>


- Luego se crea un nuevo repositorio en Dockerhub,  creamos una etiqueta para la imagen desde la terminal y verificamos su existencia 
<!-- Creación de tabla para alinear las imágenes lado a lado.-->
| <img src="static/images/docker/dockhub.png" width="500px"> | <img src="static/images/docker/dockhub1.png" width="500px"> |
|------------------------------------------------------------|-------------------------------------------------------------|
| **Repositorio en DockerHub**                               | **Verificación en la terminal**                             |

- Para subir la imagen  debemos seguir los siguientes comandos 
<p align="center">
<img src="static/images/docker/comands.png" alt="" width="700px">
</p>

- Luego veremos nuestra imagen en DockerHub
<p align="center">
<img src="static/images/docker/comand1.png" alt="" width="700px">
</p>




#### Diagrama de clases 

<p align="center">
<img src="static/images/read/class_diagram.png" alt="" width="700px">
</p>

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


