package atu.distributed.systems;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class TuneifyAPI {

    public static void main(String[] args) {
        try {
            // Create an HTTP server listening on port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Define the /plays endpoint
            server.createContext("/plays", (exchange -> {
                if ("POST".equals(exchange.getRequestMethod())) {
                    handlePostRequest(exchange);
                } else {
                    exchange.sendResponseHeaders(405, -1); // Method Not Allowed
                }
            }));

            // Set a thread pool to handle multiple requests concurrently
            server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(10));

            // Start the server
            server.start();
            System.out.println("Server started on port 8000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handlePostRequest(HttpExchange exchange) throws IOException {
        // Read and print the request body
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        System.out.println("Received request: " + requestBody);

        // Split the request body into components
        String[] parts = requestBody.split(",");
        if (parts.length == 4) {
            long userId = Long.parseLong(parts[0]);
            String artistName = parts[1];
            String trackName = parts[2];
            String albumName = parts[3];

            // Create a PlayEvent from the request data
            PlayEvent playEvent = new PlayEvent(userId, artistName, trackName, albumName);

            // Use Kafka producer to publish the event
            TuneifyKafkaProducer producer = new TuneifyKafkaProducer();
            producer.publishEvent(userId, playEvent);

            // Send a response back to the client
            String response = "Play event received and published!";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            // Invalid data format
            String response = "Invalid event data!";
            exchange.sendResponseHeaders(400, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
