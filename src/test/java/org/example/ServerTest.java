package org.example;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServerTest {

    private static final String BASE_URL = "http://localhost:8080";
    @BeforeAll
    static void esperarServidor() throws InterruptedException {
        Thread.sleep(5000); // Esperar 5 segundos antes de ejecutar los tests
    }
    @BeforeAll
    public static void setUp() throws Exception {
        new Thread(() -> {
            try {
                Server.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(3000); // Esperar a que el servidor arranque
    }

    @Test
    public void testHomePage() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL + "/").openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode, "La página de inicio debe responder con 200 OK");
    }

    @AfterAll
    public static void tearDown() throws Exception {
        Socket socket = new Socket("localhost", 8080);
        OutputStream out = socket.getOutputStream();
        out.write("SHUTDOWN".getBytes());
        out.close();
        socket.close();
    }
}
