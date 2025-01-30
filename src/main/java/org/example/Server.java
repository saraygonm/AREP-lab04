package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "static"; //Ruta del sistema
    private static final List<String> services = new ArrayList<>();

    public static void main(String[] args){ //java MyserverProgram 8080
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);
            while (true) {
                //.accept espera y acepta la conexion con los clientes
                Socket clientSocket = serverSocket.accept();
              new Thread(new Requestsupport(clientSocket)).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //clase ejecutada por un hilo
    static class Requestsupport implements Runnable {
        private Socket clientSocket;
        /*Cada vez que el servidor recibe una nueva conexión a través del Socket,
         se crea un nuevo objeto (RequestSupport), y el constructor
         asegura que esa conexión quede guardada en el objeto.*/
        public Requestsupport(Socket clientSocket) {
            this.clientSocket=clientSocket;
        }
        @Override
        public void run(){
            //Establece un canal para leer texto enviado por el cliente al servidor
            try (BufferedReader lector = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 OutputStream locutor = clientSocket.getOutputStream()) {

                String requestLine = lector.readLine();
                if (requestLine == null || requestLine.isEmpty()) return;
                System.out.println("Request: " + requestLine);


                String[] tokens = requestLine.split(" ");
                if (tokens.length < 2) return;
                String servicio = tokens[0];
                String requestfile = tokens[1];

                if (servicio.equals("GET")) {
                    if (requestfile.equals("/api/services")) {
                        GetAll(locutor);
                    } else {
                        GetRequest(locutor, requestfile);
                    }
                } else if (servicio.equals("POST")) {
                    int contentLength = 0;
                    String line;
                    //la linea no este vacia
                    while (!(line = lector.readLine()).isEmpty()) {
                        if (line.startsWith("Content-Length:")) {
                            //Indicar la longitud en # del mensaje para que el servidor sepa cuántos carácteres debe leer
                            contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                        }
                    }
                    //arreglo de caracteres con el tamaño del contentLength
                    char[] bodyChars = new char[contentLength];
                    lector.read(bodyChars); //lee cada caracter
                    String body = new String(bodyChars);

                    if (requestfile.equals("/api/services")) {
                        System.out.println("Recibiendo POST en /api/services");
                        PostServices(locutor, body);
                    } else {
                        sendError(locutor, 404, "Not Found");
                    }
                }
                else if (servicio.equals("DELETE") && requestfile.startsWith("/api/services/")) {
                    deleteReserva(locutor, requestfile);
                }
                else{
                    sendError(locutor, 405, "Method Not Allowed");
                }
            } catch (IOException exep) {
                     exep.printStackTrace();
            }
        }

        private void deleteReserva(OutputStream locutor, String requestfile) throws IOException {
            System.out.println("Request de eliminación recibida: " + requestfile);

            // Extraer el índice correctamente desde la URL
            String[] parts = requestfile.split("/");
            if (parts.length < 3) { // La URL debe ser "/api/services/{index}"
                sendError(locutor, 400, "Bad Request");
                return;
            }

            try {
                int index = Integer.parseInt(parts[3]); // Extrae el índice correctamente
                System.out.println("Índice recibido para eliminar: " + index);

                if (index >= 0 && index < services.size()) {
                    String removedReserva = services.remove(index);
                    System.out.println("Reserva eliminada: " + removedReserva);
                    System.out.println("Reservas restantes: " + services);

                    String responseJson = "{\"status\":\"success\",\"message\":\"Reserva eliminada correctamente\"}";
                    sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
                } else {
                    sendError(locutor, 404, "Reserva no encontrada");
                }
            } catch (NumberFormatException e) {
                sendError(locutor, 400, "Índice inválido");
            }
        }



        private void GetRequest(OutputStream locutor, String requestfile) throws IOException{
            if (requestfile.contains("?")) {
                requestfile = requestfile.substring(0, requestfile.indexOf("?"));
            }
            if (requestfile.equals("/api/imgTarjeta")) {
                //Creacion de la ruta para la imagen
                Path imagePath = Paths.get(WEB_ROOT, "images/tarjeta.png");
                if (Files.exists(imagePath)){
                    sendResponse(locutor,200,"OK","tarjeta/png",Files.readAllBytes(imagePath));
                }else{
                    sendError(locutor,404,"Image Not Found");
                }
            }else{
                //Construccion ruta al archivo, en respuesta a una solicitudpara dirigir al /index.html si el request file es /
                Path filePath = Paths.get(WEB_ROOT, requestfile.equals("/") ? "index.html" : requestfile.substring(1));
                if (Files.exists(filePath)) {
                    byte[] content = Files.readAllBytes(filePath);
                    String contentType = Files.probeContentType(filePath);
                    sendResponse(locutor, 200, "OK", contentType, content);
                }else {
                    sendError(locutor,404, "Not Found");
                }
            }
        }

        //Conversion de \  a \\ y " a \"
        private String escapeJson(String a) {
            return a.replace("\\", "\\\\").replace("\"", "\\\"");
        }

        private void GetAll(OutputStream locutor) throws IOException{
            System.out.println("Reservas actuales: " + services);
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\"services\":");
            jsonBuilder.append("[");
            for (int i = 0; i < services.size(); i++){
                jsonBuilder.append("\"").append(escapeJson(services.get(i))).append("\"");
                // i debe ser el último indice de la lista
                if (i < services.size() -1){
                    jsonBuilder.append(",");
                }
            }
            jsonBuilder.append("]}");
            byte [] jsonByte = jsonBuilder.toString().getBytes(StandardCharsets.UTF_8);
            sendResponse(locutor,200,"OK", "application/json",jsonByte);
        }

        private void PostServices(OutputStream locutor, String body) throws IOException {
            System.out.println("Cuerpo recibido: " + body);

            String reserva = reservationFromJson(body);
            if (reserva != null && !reserva.isEmpty()){
                services.add(reserva);
                System.out.println("Reserva añadida: " + reserva);
                System.out.println("Reservas actuales: " + services);

                String responseJson = "{\"status\":\"success\",\"message\":\"Reserva agregada exitosamente\"}";
                sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
            } else {
                System.out.println("Error: No se pudo agregar la reserva");
                sendError(locutor, 400, "Bad Request");
            }
        }

        private void apiServiceClear(OutputStream locutor) throws IOException {
            services.clear();
            String responseJson = "{\"status\":\"success\",\"message\":\"All reservation cleared\"}";
            sendResponse(locutor, 200, "OK", "application/json", responseJson.getBytes(StandardCharsets.UTF_8));
        }

        private void sendResponse(OutputStream out, int statusCode, String statusText, String contentType, byte[] content) throws IOException {
            PrintWriter pw = new PrintWriter(out, false);
            pw.printf("HTTP/1.1 %d %s\r\n", statusCode, statusText);
            pw.printf("Content-Type: %s\r\n", contentType);
            pw.printf("Content-Length: %d\r\n", content.length);
            pw.print("\r\n");
            pw.flush();
            out.write(content);
            out.flush();
        }

        private void sendError(OutputStream locutor,int statusCode, String statusText) throws IOException {
            String errorMessage = String.format("{\"error\":\"%s\"}", statusText);
            sendResponse(locutor, statusCode, statusText, "application/json", errorMessage.getBytes(StandardCharsets.UTF_8));
        }

        private String reservationFromJson(String json) {
            if(json == null || json.isEmpty()) {
                System.out.println("Error: JSON vacío o nulo");
                return null;
            }

            json = json.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                System.out.println("Error: Formato de JSON incorrecto");
                return null;
            }

            System.out.println("JSON recibido para procesar: " + json);

            String[] parts = json.replaceAll("[{}\"]", "").split(",");
            String nombre = "", fecha = "", hora = "", tarjeta = "";

            for (String part : parts) {
                String[] keyValue = part.split(":",2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    //System.out.println("Clave: " + key + " - Valor: " + value);

                    if (key.equals("nombre")) {
                        nombre = value;
                    } else if (key.equals("fecha")) {
                        fecha = value;
                    } else if (key.equals("hora")) {
                        hora = value;
                    } else if (key.equals("tarjeta")) {
                        tarjeta = value;
                    }
                }
            }

            System.out.println("Datos extraídos: Nombre=" + nombre + ", Fecha=" + fecha + ", Hora=" + hora + ", Tarjeta=" + tarjeta);

            // Validar que todos los campos están presentes
            if (!nombre.isEmpty() && !fecha.isEmpty() && !hora.isEmpty() && !tarjeta.isEmpty()) {
                return nombre + ", " + fecha + ", " + hora + ", " + tarjeta;
            }

            System.out.println("Error: Falta algún campo en la reserva");
            return null;
        }


    }

}


