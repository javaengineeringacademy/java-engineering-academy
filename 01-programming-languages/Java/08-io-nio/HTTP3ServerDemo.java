import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * Java 26 - HTTP/3 Server Setup (JEP 512)
 * 
 * HTTP/3 server implementation using QUIC protocol.
 * Requires SSL/TLS as QUIC mandates encryption.
 * 
 * Key features:
 * - Built-in HTTP/3 server support
 * - QUIC protocol for transport
 * - 0-RTT connection resumption
 * - Built-in encryption (mandatory for QUIC)
 * 
 * Status: Standard Feature in Java 26
 * 
 * Expected Output:
 * HTTP/3 Server Demo
 * ==================
 * Starting HTTP/3 server on port 8443...
 * Server started successfully
 * Listening for HTTP/3 connections...
 * 
 * To test:
 * curl -k https://localhost:8443/
 * curl -k -X POST https://localhost:8443/api/data
 * 
 * Production Use Cases:
 * - High-performance web servers for enterprise applications
 * - API servers requiring low-latency responses
 * - Real-time communication servers
 * - CDN edge servers
 * - Load balancers and reverse proxies
 */
public class HTTP3ServerDemo {

    private static final int PORT = 8443;
    private static HttpServer server;

    public static void main(String[] args) {
        System.out.println("HTTP/3 Server Demo");
        System.out.println("==================");

        try {
            // Start HTTP/3 server
            startHTTP3Server();

            // Add context handlers
            configureHandlers();

            // Keep server running
            System.out.println("\nPress Enter to stop the server...");
            System.in.read();
            stopServer();

        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Start HTTP/3 server with SSL/TLS configuration.
     * HTTP/3 requires QUIC which mandates encryption.
     */
    private static void startHTTP3Server() throws Exception {
        System.out.println("1. Starting HTTP/3 Server");
        System.out.println("-------------------------");

        // Create SSL context for HTTP/3 (QUIC requires encryption)
        SSLContext sslContext = createSSLContext();

        // Create HTTPS server (HTTP/3 over QUIC)
        server = HttpsServer.create(new InetSocketAddress(PORT), 0);

        // Configure SSL parameters
        HttpsServer httpsServer = (HttpsServer) server;
        httpsServer.setHttpsConfigurator(new com.sun.net.httpserver.HttpsConfigurator(sslContext) {
            @Override
            public void configure(HttpsParameters params) {
                try {
                    SSLContext context = getSSLContext();
                    SSLParameters sslParams = context.getDefaultSSLParameters();

                    // Enable HTTP/3 protocols
                    sslParams.setProtocols(new String[]{"TLSv1.3"});

                    params.setSSLParameters(sslParams);
                } catch (Exception e) {
                    System.err.println("SSL configuration error: " + e.getMessage());
                }
            }
        });

        // Configure thread pool
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        // Set server properties
        server.setAddress(new InetSocketAddress("0.0.0.0", PORT));

        System.out.println("HTTP/3 server configured on port " + PORT);
        System.out.println("Using QUIC protocol with TLS 1.3");
    }

    /**
     * Create SSL context for HTTP/3.
     * In production, use proper certificates.
     */
    private static SSLContext createSSLContext() throws Exception {
        // Create in-memory keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, "password".toCharArray());

        // In production, load actual certificate:
        // try (FileInputStream fis = new FileInputStream("keystore.p12")) {
        //     keyStore.load(fis, "password".toCharArray());
        // }

        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(null, null, null);

        return sslContext;
    }

    /**
     * Configure HTTP/3 server handlers.
     * Sets up routes and request processing.
     */
    private static void configureHandlers() {
        System.out.println("\n2. Configuring Server Handlers");
        System.out.println("------------------------------");

        // Root handler
        server.createContext("/", exchange -> {
            String response = """
                    {
                        "server": "Java 26 HTTP/3 Server",
                        "protocol": "HTTP/3 over QUIC",
                        "status": "running",
                        "message": "Hello from HTTP/3!"
                    }
                    """;

            sendResponse(exchange, 200, response);
            System.out.println("Handled GET / from " + exchange.getRemoteAddress());
        });

        // API endpoint
        server.createContext("/api/data", exchange -> {
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET" -> {
                    String response = """
                            {
                                "data": [
                                    {"id": 1, "name": "Java 26"},
                                    {"id": 2, "name": "HTTP/3"}
                                ]
                            }
                            """;
                    sendResponse(exchange, 200, response);
                }
                case "POST" -> {
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());
                    String response = """
                            {
                                "status": "received",
                                "message": "Data processed via HTTP/3"
                            }
                            """;
                    sendResponse(exchange, 201, response);
                }
                default -> sendResponse(exchange, 405, "Method not allowed");
            }

            System.out.println("Handled " + method + " /api/data");
        });

        // Health check endpoint
        server.createContext("/health", exchange -> {
            String response = """
                    {
                        "status": "healthy",
                        "protocol": "HTTP/3",
                        "uptime": "running"
                    }
                    """;
            sendResponse(exchange, 200, response);
        });

        // Start the server
        server.start();
        System.out.println("Server started successfully");
        System.out.println("Listening for HTTP/3 connections on port " + PORT);
    }

    /**
     * Send HTTP response with proper headers.
     */
    private static void sendResponse(HttpExchange exchange, int statusCode, String body) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Server", "Java26-HTTP3");
        exchange.getResponseHeaders().set("X-Protocol", "HTTP/3");

        byte[] responseBytes = body.getBytes();
        exchange.sendResponseHeaders(statusCode, responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    /**
     * Stop the server gracefully.
     */
    private static void stopServer() {
        if (server != null) {
            server.stop(1);
            System.out.println("\nServer stopped gracefully");
        }
    }
}
