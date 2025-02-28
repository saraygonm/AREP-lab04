package org.example;

import org.junit.jupiter.api.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerConnectionTest {

    private static final String BASE_URL = "http://localhost:8080";
    @BeforeAll
    static void esperarServidor() throws InterruptedException {
        Thread.sleep(5000); // Esperar 5 segundos antes de ejecutar los tests
    }
    @Test
    public void testServerRunning() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL).openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "El servidor debería responder con 200 OK.");
    }
}
