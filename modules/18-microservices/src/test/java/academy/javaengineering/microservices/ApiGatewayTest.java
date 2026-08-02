package academy.javaengineering.microservices;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiGatewayTest {

    @Test
    void testAddRoute() {
        ApiGatewayExample gateway = new ApiGatewayExample();
        ApiGatewayExample.Route route = new ApiGatewayExample.Route(
                "test", "/api/test", "http://localhost:8080", List.of());
        gateway.addRoute(route);
        assertNotNull(gateway);
    }

    @Test
    void testRouteRequest() {
        ApiGatewayExample gateway = new ApiGatewayExample();
        gateway.addRoute(new ApiGatewayExample.Route(
                "test", "/api/test", "http://localhost:8080", List.of()));
        ApiGatewayExample.Request request = new ApiGatewayExample.Request(
                "GET", "/api/test", Map.of(), null);
        ApiGatewayExample.Response response = gateway.route(request);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testRouteNotFound() {
        ApiGatewayExample gateway = new ApiGatewayExample();
        ApiGatewayExample.Request request = new ApiGatewayExample.Request(
                "GET", "/api/nonexistent", Map.of(), null);
        ApiGatewayExample.Response response = gateway.route(request);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testFilterExecution() {
        ApiGatewayExample gateway = new ApiGatewayExample();
        gateway.addRoute(new ApiGatewayExample.Route(
                "test", "/api/test", "http://localhost:8080", List.of()));
        gateway.addFilter(req -> System.out.println("Filter executed"));
        ApiGatewayExample.Request request = new ApiGatewayExample.Request(
                "GET", "/api/test", Map.of(), null);
        ApiGatewayExample.Response response = gateway.route(request);
        assertNotNull(response);
    }
}
