FROM openjdk:17

WORKDIR /usrapp/bin

# Configurar la variable de entorno correctamente
ENV PORT=6000

# Copiar los archivos de clases y dependencias
COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency

# Ejecutar la aplicación
CMD ["java", "-cp", "./classes:./dependency/*", "org.example.Server"]