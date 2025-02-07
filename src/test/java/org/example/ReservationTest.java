package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationTest {

    private static Thread serverThread;

    @BeforeAll
    static void startServer() {
        serverThread = new Thread(() -> Server.main(new String[]{}));
        serverThread.start();

        try {
            Thread.sleep(2000); // Esperar a que el servidor inicie completamente
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void testPostReservation() throws IOException {
        URL url = new URL("http://localhost:8080/api/services");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String jsonInput = "{\"nombre\":\"Juan\",\"fecha\":\"2025-02-06\",\"hora\":\"12:00\",\"tarjeta\":\"1234 5678 9012 3456\"}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        assertEquals(200, conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();

        assertTrue(response.contains("\"status\":\"success\""));
    }

    @Test
    @Order(2)
    void testGetReservations() throws IOException {
        URL url = new URL("http://localhost:8080/api/services");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();

        assertTrue(response.contains("\"services\":"));
    }

    @Test
    @Order(3)
    void testDeleteReservation() throws IOException {
        URL url = new URL("http://localhost:8080/api/services/0");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");

        assertEquals(200, conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();

        assertTrue(response.contains("\"status\":\"success\""));
    }
}
