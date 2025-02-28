package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

public class ServiceManager {
    private static final Logger LOGGER = Logger.getLogger(ServiceManager.class.getName());
    private static final String WEB_ROOT = "target/classes/webroot";
    private static final List<String> services = new ArrayList<>();

    public static void GetRequest(OutputStream locutor, String requestfile) throws IOException {
        Path filePath = Paths.get(WEB_ROOT, requestfile.equals("/") ? "index.html" : requestfile.substring(1));
        if (Files.exists(filePath)) {
            byte[] content = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            sendResponse(locutor, 200, "OK", contentType, content);
        } else {
            sendError(locutor, 404, "Not Found");
        }
    }

    public static void GetAll(OutputStream locutor) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"services\": [");

        for (int i = 0; i < services.size(); i++) {
            jsonBuilder.append(services.get(i)); // Ya es JSON, no requiere comillas
            if (i < services.size() - 1) {
                jsonBuilder.append(",");
            }
        }

        jsonBuilder.append("]}");
        System.out.println("Enviando JSON: " + jsonBuilder); // Depuración

        byte[] jsonByte = jsonBuilder.toString().getBytes(StandardCharsets.UTF_8);
        sendResponse(locutor, 200, "OK", "application/json", jsonByte);
    }

    private static String escapeJson(String a) {
        return a.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static void PostServices(OutputStream locutor, String body) throws IOException {
        try {
            JSONObject jsonReserva = new JSONObject(body);
            services.add(jsonReserva.toString()); // Almacenar como JSON válido
            System.out.println("Lista de reservas actualizada: " + services);
            String responseJson = "{\"status\":\"success\",\"message\":\"Reserva agregada exitosamente\"}";
            sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            sendError(locutor, 400, "Bad Request");
        }
    }

    public static void DeleteService(OutputStream locutor, int index) throws IOException {
        if (index >= 0 && index < services.size()) {
            services.remove(index);
            System.out.println("Reserva eliminada en posición: " + index);

            String responseJson = "{\"status\":\"success\",\"message\":\"Reserva eliminada correctamente\"}";
            sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
        } else {
            sendError(locutor, 404, "Reserva no encontrada");
        }
    }
    public static void ClearAllServices(OutputStream locutor) throws IOException {
        services.clear(); // Vaciar la lista de reservas
        System.out.println("Todas las reservas han sido eliminadas.");

        String responseJson = "{\"status\":\"success\",\"message\":\"Todas las reservas han sido eliminadas\"}";
        sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
    }
    public static void GetImage(OutputStream locutor) throws IOException {
        Path imagePath = Paths.get(WEB_ROOT, "tarjeta.png"); // Cambia el nombre según tu archivo
        if (Files.exists(imagePath)) {
            byte[] imageBytes = Files.readAllBytes(imagePath);
            sendResponse(locutor, 200, "OK", "image/png", imageBytes);
        } else {
            sendError(locutor, 404, "Imagen no encontrada");
        }
    }




    public static void sendResponse(OutputStream out, int statusCode, String statusText, String contentType, byte[] content) throws IOException {
        PrintWriter pw = new PrintWriter(out, true);
        pw.printf("HTTP/1.1 %d %s\r\n", statusCode, statusText);
        pw.printf("Content-Type: %s\r\n", contentType);
        pw.printf("Content-Length: %d\r\n", content.length);
        pw.print("\r\n");
        pw.flush();
        out.write(content);
        out.flush();
    }

    public static void sendError(OutputStream locutor, int statusCode, String statusText) throws IOException {
        String errorMessage = String.format("{\"error\":\"%s\"}", statusText);
        sendResponse(locutor, statusCode, statusText, "application/json", errorMessage.getBytes(StandardCharsets.UTF_8));
    }
}