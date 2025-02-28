package org.example;

import static org.junit.jupiter.api.Assertions.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ServerManagerTest {

    private ServiceManager serviceManager;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        serviceManager = new ServiceManager();
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void testPostServices() throws IOException {
        String requestBody = new JSONObject()
                .put("nombre", "Juan")
                .put("fecha", "2025-03-01")
                .put("hora", "14:00")
                .put("tarjeta", "1234 5678 9012 3456")
                .toString();

        serviceManager.PostServices(outputStream, requestBody);
        String response = outputStream.toString(StandardCharsets.UTF_8);

        assertTrue(response.contains("\"status\":\"success\""));
    }

    @Test
    void testGetAll() throws IOException {
        serviceManager.GetAll(outputStream);
        String response = outputStream.toString(StandardCharsets.UTF_8);

        assertTrue(response.contains("\"services\": ["));
    }

    @Test
    void testDeleteService() throws IOException {
        ServiceManager.PostServices(outputStream, "{\"nombre\": \"Pedro\", \"fecha\": \"2025-03-02\", \"hora\": \"16:00\", \"tarjeta\": \"5678 1234 9012 3456\"}");
        ServiceManager.DeleteService(outputStream, 0);

        String response = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(response.contains("\"status\":\"success\""));
    }

    @Test
    void testClearAllServices() throws IOException {
        ServiceManager.ClearAllServices(outputStream);

        String response = outputStream.toString(StandardCharsets.UTF_8);
        assertTrue(response.contains("\"status\":\"success\""));
    }
}
