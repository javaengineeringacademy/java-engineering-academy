package academy.javaengineering.microservices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApiGatewayExample {

    private final Map<String, Route> routes = new ConcurrentHashMap<>();
    private final List<GatewayFilter> filters = new ArrayList<>();

    public static class Route {
        private final String id;
        private final String path;
        private final String targetUri;
        private final List<String> predicates;

        public Route(String id, String path, String targetUri, List<String> predicates) {
            this.id = id;
            this.path = path;
            this.targetUri = targetUri;
            this.predicates = predicates;
        }

        public String getId() { return id; }
        public String getPath() { return path; }
        public String getTargetUri() { return targetUri; }
        public List<String> getPredicates() { return predicates; }
    }

    public interface GatewayFilter {
        void doFilter(Request request, Response response, FilterChain chain);
    }

    public static class Request {
        private final String method;
        private final String path;
        private final Map<String, String> headers;
        private final String body;

        public Request(String method, String path, Map<String, String> headers, String body) {
            this.method = method;
            this.path = path;
            this.headers = headers != null ? headers : new HashMap<>();
            this.body = body;
        }

        public String getMethod() { return method; }
        public String getPath() { return path; }
        public Map<String, String> getHeaders() { return headers; }
        public String getBody() { return body; }
    }

    public static class Response {
        private int status;
        private String body;
        private final Map<String, String> headers = new HashMap<>();

        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public Map<String, String> getHeaders() { return headers; }
    }

    public interface FilterChain {
        void doFilter(Request request, Response response);
    }

    public void addRoute(Route route) {
        routes.put(route.getId(), route);
        System.out.println("Route added: " + route.getPath() + " -> " + route.getTargetUri());
    }

    public void addFilter(GatewayFilter filter) {
        filters.add(filter);
    }

    public Response route(Request request) {
        Response response = new Response();

        Route matchedRoute = matchRoute(request.getPath());
        if (matchedRoute == null) {
            response.setStatus(404);
            response.setBody("Route not found");
            return response;
        }

        FilterChain chain = buildFilterChain();
        chain.doFilter(request, response);

        response.setStatus(200);
        response.setBody("Response from " + matchedRoute.getTargetUri());
        return response;
    }

    private Route matchRoute(String path) {
        return routes.values().stream()
                .filter(r -> path.startsWith(r.getPath()))
                .findFirst()
                .orElse(null);
    }

    private FilterChain buildFilterChain() {
        return (request, response) -> {
            System.out.println("Processing request: " + request.getMethod() + " " + request.getPath());
            response.setBody("Processed by gateway");
        };
    }

    public static void main(String[] args) {
        ApiGatewayExample gateway = new ApiGatewayExample();

        System.out.println("=== API Gateway Demo ===\n");

        gateway.addRoute(new Route("user-service", "/api/users", "http://user-service:8081", List.of()));
        gateway.addRoute(new Route("order-service", "/api/orders", "http://order-service:8082", List.of()));

        gateway.addFilter(request -> System.out.println("Logging filter"));

        System.out.println("\n--- Routing Request ---");
        Request request = new Request("GET", "/api/users/1", Map.of("Authorization", "Bearer token"), null);
        Response response = gateway.route(request);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());
    }
}
