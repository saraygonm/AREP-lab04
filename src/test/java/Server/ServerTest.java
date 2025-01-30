package Server;

import org.junit.Test;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest{
    private static final String BASE_URL = "http://localhost:8080";

    @Test
    @Order(1)
    public void testServerIsRunning() throws IOException {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        assertTrue(responseCode == 200 || responseCode == 404); // Verifica que el servidor responde
    }

    @Test
    @Order(2)
    public void testAddReservation() throws IOException {
        URL url = new URL(BASE_URL + "/api/services");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String jsonInput = "{\"nombre\":\"Pedro\",\"fecha\":\"2025-01-31\",\"hora\":\"12:00\",\"tarjeta\":\"1234 5678 9012 3456\"}";
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    @Order(3)
    public void testGetAllReservations() throws IOException {
        URL url = new URL(BASE_URL + "/api/services");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        Scanner scanner = new Scanner(connection.getInputStream());
        String responseBody = scanner.useDelimiter("\\A").next();
        scanner.close();
        assertTrue(responseBody.contains("services")); // Verifica que la respuesta contiene reservas
    }

    @Test
    @Order(4)
    public void testDeleteReservation() throws IOException {
        URL url = new URL(BASE_URL + "/api/services/0"); // Eliminamos la primera reserva
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
    }

    @Test
    @Order(5)
    public void testFetchImage() throws IOException {
        URL url = new URL(BASE_URL + "/api/imgTarjeta");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);
        assertEquals("tarjeta/png", connection.getContentType()); // Verifica que devuelve imagen
    }
}

