package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.example.ServiceManager.*;

public class RequestHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(RequestHandler.class.getName());
    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream locutor = clientSocket.getOutputStream()) {

            String requestLine = lector.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;
            System.out.println("Request: " + requestLine);

            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2) return;
            String metodo = tokens[0]; // Método HTTP (GET, POST, DELETE)
            String requestfile = tokens[1]; // Recurso solicitado

            if (metodo.equals("GET")) {
                if (requestfile.equals("/api/services")) {
                    GetAll(locutor);
                } else if (requestfile.equals("/api/imgTarjeta")) {
                    GetImage(locutor);
                } else {
                    GetRequest(locutor, requestfile);
                }
            } else if (metodo.equals("POST")) {
                int contentLength = 0;
                String line;
                while (!(line = lector.readLine()).isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                    }
                }
                char[] bodyChars = new char[contentLength];
                int charsRead = lector.read(bodyChars);
                if (charsRead != -1) {
                    String body = new String(bodyChars);
                    if (requestfile.equals("/api/services")) {
                        System.out.println("Recibiendo POST en /api/services");
                        PostServices(locutor, body);
                    } else {
                        sendError(locutor, 404, "Not Found");
                    }
                }
            } else if (metodo.equals("DELETE")) {
                if (requestfile.startsWith("/api/services/")) {
                    String[] parts = requestfile.split("/");
                    if (parts.length == 4) { // Esperamos /api/services/{index}
                        try {
                            int index = Integer.parseInt(parts[3]); // Extraer índice de la URL
                            DeleteService(locutor, index);
                        } catch (NumberFormatException e) {
                            sendError(locutor, 400, "Bad Request");
                        }
                    } else {
                        sendError(locutor, 400, "Bad Request");
                    }
                } else if (requestfile.equals("/api/services/clear")) {
                    ClearAllServices(locutor);
                } else {
                    sendError(locutor, 404, "Not Found");
                }
            } else {
                sendError(locutor, 405, "Method Not Allowed");
            }
        } catch (IOException exep) {
            LOGGER.log(Level.SEVERE, "Error en RequestHandler", exep);
        }
    }

    private void GetImage(OutputStream locutor) throws IOException {
        Path imagePath = Paths.get("target/classes/webroot/images/tarjeta.png");
        System.out.println("Intentando cargar imagen en: " + imagePath.toAbsolutePath());

        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            System.out.println("Imagen encontrada, enviando...");
            sendResponse(locutor, 200, "OK", "image/png", imageBytes);
        } else {
            System.out.println("Error: Imagen no encontrada en " + imagePath.toAbsolutePath());
            sendError(locutor, 404, "Imagen no encontrada");
        }
    }

}
