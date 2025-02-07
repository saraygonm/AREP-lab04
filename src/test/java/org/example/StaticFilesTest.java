package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

public class StaticFilesTest {

    @Test
    void testIndexHtmlExists() throws IOException {
        URL url = new URL("http://localhost:8080/index.html");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());
        assertEquals("text/html", conn.getContentType());
    }

    @Test
    void testImageExists() throws IOException {
        URL url = new URL("http://localhost:8080/images/calendario.png");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        assertEquals(200, conn.getResponseCode());
        assertEquals("image/png", conn.getContentType());
    }
}
