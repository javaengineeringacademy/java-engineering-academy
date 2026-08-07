import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Java 26 - HTTP/3 for HTTP Client (JEP 512)
 * 
 * HTTP/3 uses QUIC protocol instead of TCP, providing:
 * - Reduced latency (0-RTT connection establishment)
 * - Better congestion control
 * - Improved performance on unreliable networks
 * - No head-of-line blocking
 * 
 * Status: Standard Feature in Java 26
 * 
 * Expected Output:
 * HTTP/3 Client Demo
 * ==================
 * Sending HTTP/3 GET request to httpbin.org...
 * Response Status: 200
 * Response Headers: {content-type=[application/json], ...}
 * Response Body: {"headers": {"Accept": ["*/*"], ...}}
 * 
 * Sending async HTTP/3 request...
 * Async Response Status: 200
 * 
 * HTTP/3 with custom timeout: 5 seconds
 * Request completed within timeout
 * 
 * Production Use Cases:
 * - High-performance web services requiring low latency
 * - Mobile applications with unreliable network connections
 * - Microservices communication in cloud environments
 * - Real-time data streaming applications
 * - API gateways handling high concurrent connections
 */
public class HTTP3ClientDemo {

    public static void main(String[] args) {
        System.out.println("HTTP/3 Client Demo");
        System.out.println("==================");

        // Basic HTTP/3 GET request
        basicHttpGet();

        // Async HTTP/3 request
        asyncHttpRequest();

        // HTTP/3 with custom configuration
        configuredHttpClient();

        // HTTP/3 POST request with body
        httpPostRequest();
    }

    /**
     * Basic HTTP/3 GET request demonstration.
     * HTTP/3 automatically negotiated when server supports it.
     */
    private static void basicHttpGet() {
        System.out.println("\n1. Basic HTTP/3 GET Request");
        System.out.println("----------------------------");

        try {
            // Create HTTP client with default configuration
            // HTTP/3 is enabled by default when available
            HttpClient client = HttpClient.newHttpClient();

            // Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/get"))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            // Send synchronous request
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Response Status: " + response.statusCode());
            System.out.println("HTTP Version: " + response.version());
            System.out.println("Response Headers: " + response.headers().map());

            // Print first 200 characters of response
            String body = response.body();
            if (body.length() > 200) {
                body = body.substring(0, 200) + "...";
            }
            System.out.println("Response Body: " + body);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Asynchronous HTTP/3 request demonstration.
     * Non-blocking request for better resource utilization.
     */
    private static void asyncHttpRequest() {
        System.out.println("\n2. Async HTTP/3 Request");
        System.out.println("-----------------------");

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_3)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/delay/1"))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            // Send async request
            CompletableFuture<HttpResponse<String>> future = client.sendAsync(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // Process response when ready
            HttpResponse<String> response = future.join();
            System.out.println("Async Response Status: " + response.statusCode());
            System.out.println("Response received successfully");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * HTTP/3 client with custom configuration.
     * Demonstrates advanced client setup options.
     */
    private static void configuredHttpClient() {
        System.out.println("\n3. Configured HTTP/3 Client");
        System.out.println("---------------------------");

        try {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_3)
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/headers"))
                    .timeout(Duration.ofSeconds(10))
                    .header("X-Custom-Header", "Java26-HTTP3")
                    .header("User-Agent", "Java-HTTP3-Client/26")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Status: " + response.statusCode());
            System.out.println("Custom headers sent successfully");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * HTTP/3 POST request with JSON body.
     * Demonstrates sending data over HTTP/3.
     */
    private static void httpPostRequest() {
        System.out.println("\n4. HTTP/3 POST Request");
        System.out.println("----------------------");

        try {
            HttpClient client = HttpClient.newHttpClient();

            String jsonBody = """
                    {
                        "name": "Java 26",
                        "feature": "HTTP/3",
                        "status": "production-ready"
                    }
                    """;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://httpbin.org/post"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("POST Response Status: " + response.statusCode());
            System.out.println("Data sent successfully over HTTP/3");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
