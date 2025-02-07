package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ejecutar pruebas en orden
public class ServerTest {

    private static Thread serverThread;

    @BeforeAll
    static void startServer() {
        serverThread = new Thread(() -> Server.main(new String[]{}));
        serverThread.start();

        try {
            Thread.sleep(2000); // Esperar a que el servidor inicie
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void stopServer() {
        serverThread.interrupt();
    }

    @Test
    @Order(1)
    void testHelloEndpoint() throws IOException {
        URL url = new URL("http://localhost:8080/App/hello?name=Pedro");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();

        assertEquals("Hello Pedro!", response);
    }

    @Test
    @Order(2)
    void testPiEndpoint() throws IOException {
        URL url = new URL("http://localhost:8080/App/pi");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = in.readLine();
        in.close();

        assertEquals(String.valueOf(Math.PI), response);
    }
}
