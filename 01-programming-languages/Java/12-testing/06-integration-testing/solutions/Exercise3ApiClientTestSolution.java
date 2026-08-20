package academy.javaengineering.testing.integration.solutions;

import org.junit.jupiter.api.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class Exercise3ApiClientTestSolution {

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
                } catch (Exception e) { attempts.incrementAndGet(); }
            }
            throw new RuntimeException("Failed after " + maxRetries + " attempts");
        }
    }

    @Test
    void shouldFetchData() {
        HttpClient mock = url -> "success";
        ApiClient client = new ApiClient(mock, 3);
        assertEquals("success", client.fetchData("/api/data"));
    }

    @Test
    void shouldRetryOnFailure() {
        AtomicInteger count = new AtomicInteger(0);
        HttpClient mock = url -> {
            if (count.incrementAndGet() < 3) throw new RuntimeException("fail");
            return "success";
        };
        ApiClient client = new ApiClient(mock, 5);
        assertEquals("success", client.fetchData("/api/data"));
    }

    @Test
    void shouldThrowAfterMaxRetries() {
        HttpClient mock = url -> { throw new RuntimeException("fail"); };
        ApiClient client = new ApiClient(mock, 3);
        assertThrows(RuntimeException.class, () -> client.fetchData("/api/data"));
    }
}
