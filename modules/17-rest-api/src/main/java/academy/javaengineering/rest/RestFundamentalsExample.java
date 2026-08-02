package academy.javaengineering.rest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RestFundamentalsExample {

    private final Map<Long, Map<String, Object>> resources = new ConcurrentHashMap<>();
    private long idCounter = 1;

    public enum HttpMethod {
        GET, POST, PUT, PATCH, DELETE
    }

    public enum StatusCode {
        OK(200, "OK"),
        CREATED(201, "Created"),
        NO_CONTENT(204, "No Content"),
        BAD_REQUEST(400, "Bad Request"),
        NOT_FOUND(404, "Not Found"),
        CONFLICT(409, "Conflict"),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error");

        private final int code;
        private final String reason;

        StatusCode(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        public int getCode() { return code; }
        public String getReason() { return reason; }
    }

    public static class HttpResponse {
        private final StatusCode statusCode;
        private final Object body;
        private final Map<String, String> headers;

        public HttpResponse(StatusCode statusCode, Object body, Map<String, String> headers) {
            this.statusCode = statusCode;
            this.body = body;
            this.headers = headers != null ? headers : new HashMap<>();
        }

        public StatusCode getStatusCode() { return statusCode; }
        public Object getBody() { return body; }
        public Map<String, String> getHeaders() { return headers; }
    }

    public HttpResponse handleRequest(HttpMethod method, String path, Object body) {
        System.out.println("Handling " + method + " " + path);

        if (path.matches("/api/resources$")) {
            return handleCollection(method, body);
        } else if (path.matches("/api/resources/\\d+$")) {
            Long id = Long.parseLong(path.split("/")[3]);
            return handleResource(method, id, body);
        }

        return new HttpResponse(StatusCode.NOT_FOUND, "Endpoint not found", null);
    }

    private HttpResponse handleCollection(HttpMethod method, Object body) {
        return switch (method) {
            case GET -> {
                List<Map<String, Object>> list = new ArrayList<>(resources.values());
                yield new HttpResponse(StatusCode.OK, list, Map.of("X-Total-Count", String.valueOf(list.size())));
            }
            case POST -> {
                Map<String, Object> resource = (Map<String, Object>) body;
                resource.put("id", idCounter++);
                resources.put((Long) resource.get("id"), resource);
                yield new HttpResponse(StatusCode.CREATED, resource, Map.of("Location", "/api/resources/" + resource.get("id")));
            }
            default -> new HttpResponse(StatusCode.BAD_REQUEST, "Method not allowed on collection", null);
        };
    }

    private HttpResponse handleResource(HttpMethod method, Long id, Object body) {
        if (!resources.containsKey(id)) {
            return new HttpResponse(StatusCode.NOT_FOUND, "Resource not found", null);
        }

        return switch (method) {
            case GET -> new HttpResponse(StatusCode.OK, resources.get(id), null);
            case PUT -> {
                Map<String, Object> resource = (Map<String, Object>) body;
                resource.put("id", id);
                resources.put(id, resource);
                yield new HttpResponse(StatusCode.OK, resource, null);
            }
            case PATCH -> {
                Map<String, Object> existing = resources.get(id);
                Map<String, Object> updates = (Map<String, Object>) body;
                existing.putAll(updates);
                yield new HttpResponse(StatusCode.OK, existing, null);
            }
            case DELETE -> {
                resources.remove(id);
                yield new HttpResponse(StatusCode.NO_CONTENT, null, null);
            }
            default -> new HttpResponse(StatusCode.BAD_REQUEST, "Method not allowed on resource", null);
        };
    }

    public static void main(String[] args) {
        RestFundamentalsExample rest = new RestFundamentalsExample();

        System.out.println("=== REST Fundamentals Demo ===\n");

        System.out.println("--- POST /api/resources ---");
        Map<String, Object> user = Map.of("name", "John", "email", "john@example.com");
        HttpResponse response = rest.handleRequest(HttpMethod.POST, "/api/resources", user);
        System.out.println("Status: " + response.getStatusCode().getCode() + " " + response.getStatusCode().getReason());
        System.out.println("Body: " + response.getBody());
        System.out.println("Headers: " + response.getHeaders());

        System.out.println("\n--- GET /api/resources ---");
        response = rest.handleRequest(HttpMethod.GET, "/api/resources", null);
        System.out.println("Status: " + response.getStatusCode().getCode());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- GET /api/resources/1 ---");
        response = rest.handleRequest(HttpMethod.GET, "/api/resources/1", null);
        System.out.println("Status: " + response.getStatusCode().getCode());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- PUT /api/resources/1 ---");
        Map<String, Object> updated = Map.of("name", "John Updated", "email", "john.updated@example.com");
        response = rest.handleRequest(HttpMethod.PUT, "/api/resources/1", updated);
        System.out.println("Status: " + response.getStatusCode().getCode());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- PATCH /api/resources/1 ---");
        Map<String, Object> patch = Map.of("email", "john.new@example.com");
        response = rest.handleRequest(HttpMethod.PATCH, "/api/resources/1", patch);
        System.out.println("Status: " + response.getStatusCode().getCode());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- DELETE /api/resources/1 ---");
        response = rest.handleRequest(HttpMethod.DELETE, "/api/resources/1", null);
        System.out.println("Status: " + response.getStatusCode().getCode() + " " + response.getStatusCode().getReason());
    }
}
