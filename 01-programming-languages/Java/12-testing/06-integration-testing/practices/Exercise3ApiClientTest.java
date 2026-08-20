package academy.javaengineering.testing.integration.practices;

import org.junit.jupiter.api.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 3: API Client Integration Test
 *
 * Tasks:
 * 1. Test API client with mock HTTP server
 * 2. Test retry logic
 * 3. Test timeout handling
 * 4. Test error response handling
 */
class Exercise3ApiClientTest {

    interface HttpClient {
        String get(String url);
        String post(String url, String body);
    }

    static class ApiClient {
        private final HttpClient httpClient;
        private final int maxRetries;

        ApiClient(HttpClient httpClient, int maxRetries) {
            this.httpClient = httpClient;
            this.maxRetries = maxRetries;
        }

        String fetchData(String url) {
            AtomicInteger attempts = new AtomicInteger(0);
            while (attempts.get() < maxRetries) {
                try {
                    String response = httpClient.get(url);
                    if (response != null) return response;
                } catch (Exception e) {
                    attempts.incrementAndGet();
                }
            }
            throw new RuntimeException("Failed after " + maxRetries + " attempts");
        }
    }

    @Test
    @DisplayName("should fetch data successfully")
    void shouldFetchData() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should retry on failure")
    void shouldRetryOnFailure() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should throw after max retries")
    void shouldThrowAfterMaxRetries() {
        // Arrange, Act, Assert
    }
}
