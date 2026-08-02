package academy.javaengineering.rest;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RestFundamentalsTest {

    @Test
    void testPostResource() {
        RestFundamentalsExample rest = new RestFundamentalsExample();
        Map<String, Object> user = Map.of("name", "John", "email", "john@example.com");
        RestFundamentalsExample.HttpResponse response = rest.handleRequest(
                RestFundamentalsExample.HttpMethod.POST, "/api/resources", user);
        assertEquals(201, response.getStatusCode().getCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAllResources() {
        RestFundamentalsExample rest = new RestFundamentalsExample();
        RestFundamentalsExample.HttpResponse response = rest.handleRequest(
                RestFundamentalsExample.HttpMethod.GET, "/api/resources", null);
        assertEquals(200, response.getStatusCode().getCode());
    }

    @Test
    void testGetResourceById() {
        RestFundamentalsExample rest = new RestFundamentalsExample();
        rest.handleRequest(RestFundamentalsExample.HttpMethod.POST, "/api/resources",
                Map.of("name", "Test"));
        RestFundamentalsExample.HttpResponse response = rest.handleRequest(
                RestFundamentalsExample.HttpMethod.GET, "/api/resources/1", null);
        assertEquals(200, response.getStatusCode().getCode());
    }

    @Test
    void testResourceNotFound() {
        RestFundamentalsExample rest = new RestFundamentalsExample();
        RestFundamentalsExample.HttpResponse response = rest.handleRequest(
                RestFundamentalsExample.HttpMethod.GET, "/api/resources/999", null);
        assertEquals(404, response.getStatusCode().getCode());
    }

    @Test
    void testDeleteResource() {
        RestFundamentalsExample rest = new RestFundamentalsExample();
        rest.handleRequest(RestFundamentalsExample.HttpMethod.POST, "/api/resources",
                Map.of("name", "Test"));
        RestFundamentalsExample.HttpResponse response = rest.handleRequest(
                RestFundamentalsExample.HttpMethod.DELETE, "/api/resources/1", null);
        assertEquals(204, response.getStatusCode().getCode());
    }
}
