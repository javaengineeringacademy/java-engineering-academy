import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Solution5 {
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

        try {
            response.getHeaders().put("Hacked", "true");
        } catch (UnsupportedOperationException e) {
            System.out.println("Headers are immutable: " + e.getClass().getSimpleName());
        }
    }
}

final class HttpResponse {
    private final int statusCode;
    private final String body;
    private final Map<String, String> headers;

    private HttpResponse(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() { return statusCode; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return headers; }

    public static Builder builder() {
        return new Builder();
    }

    static class Builder {
        private int statusCode;
        private String body;
        private final Map<String, String> headers = new HashMap<>();

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(statusCode, body, Collections.unmodifiableMap(new HashMap<>(headers)));
        }
    }
}
