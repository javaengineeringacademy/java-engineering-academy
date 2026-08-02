package academy.javaengineering.rest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseExample {

    private final Map<String, String> requestHeaders = new ConcurrentHashMap<>();
    private final Map<String, String> responseHeaders = new ConcurrentHashMap<>();

    public static class Request {
        private final String method;
        private final String path;
        private final Map<String, String> headers;
        private final Object body;
        private final Map<String, String> queryParams;

        public Request(String method, String path, Map<String, String> headers,
                       Object body, Map<String, String> queryParams) {
            this.method = method;
            this.path = path;
            this.headers = headers != null ? headers : new HashMap<>();
            this.body = body;
            this.queryParams = queryParams != null ? queryParams : new HashMap<>();
        }

        public String getMethod() { return method; }
        public String getPath() { return path; }
        public Map<String, String> getHeaders() { return headers; }
        public Object getBody() { return body; }
        public Map<String, String> getQueryParams() { return queryParams; }
    }

    public static class Response {
        private final int status;
        private final Map<String, String> headers;
        private final Object body;
        private final String contentType;

        public Response(int status, Map<String, String> headers, Object body, String contentType) {
            this.status = status;
            this.headers = headers != null ? headers : new HashMap<>();
            this.body = body;
            this.contentType = contentType;
        }

        public int getStatus() { return status; }
        public Map<String, String> getHeaders() { return headers; }
        public Object getBody() { return body; }
        public String getContentType() { return contentType; }
    }

    public Response processRequest(Request request) {
        System.out.println("Processing: " + request.getMethod() + " " + request.getPath());

        Map<String, String> headers = new HashMap<>();
        headers.put("X-Request-Id", UUID.randomUUID().toString());
        headers.put("X-Response-Time", String.valueOf(System.currentTimeMillis()));

        if (request.getHeaders().containsKey("Accept")) {
            String accept = request.getHeaders().get("Accept");
            headers.put("Content-Type", negotiateContentType(accept));
        } else {
            headers.put("Content-Type", "application/json");
        }

        if (request.getHeaders().containsKey("Authorization")) {
            String token = request.getHeaders().get("Authorization");
            if (!validateToken(token)) {
                return new Response(401, headers, Map.of("error", "Unauthorized"), "application/json");
            }
        }

        Object body = processBody(request);
        int status = body != null ? 200 : 204;

        return new Response(status, headers, body, headers.get("Content-Type"));
    }

    private String negotiateContentType(String accept) {
        if (accept.contains("application/xml")) {
            return "application/xml";
        }
        return "application/json";
    }

    private boolean validateToken(String token) {
        return token.startsWith("Bearer ");
    }

    private Object processBody(Request request) {
        if ("GET".equals(request.getMethod())) {
            return Map.of("method", "GET", "path", request.getPath());
        }
        return request.getBody();
    }

    public static void main(String[] args) {
        RequestResponseExample example = new RequestResponseExample();

        System.out.println("=== Request/Response Demo ===\n");

        System.out.println("--- GET Request ---");
        Request getRequest = new Request(
                "GET", "/api/users", Map.of("Accept", "application/json"), null, null);
        Response response = example.processRequest(getRequest);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Headers: " + response.getHeaders());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- POST Request ---");
        Request postRequest = new Request(
                "POST", "/api/users",
                Map.of("Content-Type", "application/json", "Authorization", "Bearer token123"),
                Map.of("name", "John"), null);
        response = example.processRequest(postRequest);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- Unauthorized Request ---");
        Request unauthRequest = new Request(
                "GET", "/api/users", Map.of("Authorization", "Invalid"), null, null);
        response = example.processRequest(unauthRequest);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- XML Request ---");
        Request xmlRequest = new Request(
                "GET", "/api/users", Map.of("Accept", "application/xml"), null, null);
        response = example.processRequest(xmlRequest);
        System.out.println("Content-Type: " + response.getContentType());
    }
}
