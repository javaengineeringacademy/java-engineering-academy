import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Exercise5 {
    public static void main(String[] args) {
        HttpResponse response = HttpResponse.builder()
            .statusCode(200)
            .body("Hello, World!")
            .header("Content-Type", "text/plain")
            .header("X-Custom", "value")
            .build();

        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Body: " + response.getBody());
        System.out.println("Headers: " + response.getHeaders());

        // Try to modify headers
        try {
            response.getHeaders().put("Hacked", "true");
        } catch (UnsupportedOperationException e) {
            System.out.println("Headers are immutable: " + e.getClass().getSimpleName());
        }
    }
}

/*
 * TODO: Implement the immutable HttpResponse class with Builder pattern.
 *
 * Requirements:
 * - All HttpResponse fields are private and final
 * - No setter methods on HttpResponse
 * - Static builder() method returns a new Builder
 * - Builder has statusCode(int), body(String), header(String, String) methods
 * - Builder.build() returns an immutable HttpResponse
 * - Headers map is unmodifiable in the final HttpResponse
 * - Proper getters on HttpResponse
 */
final class HttpResponse {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    private HttpResponse(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    // TODO: Getters

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private int statusCode;
        private String body;
        private final Map<String, String> headers = new HashMap<>();

        // TODO: statusCode method

        // TODO: body method

        // TODO: header method

        // TODO: build method (create immutable HttpResponse with unmodifiable headers)
    }
}
