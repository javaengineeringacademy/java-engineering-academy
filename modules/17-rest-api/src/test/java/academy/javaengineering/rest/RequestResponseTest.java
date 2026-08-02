package academy.javaengineering.rest;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestResponseTest {

    @Test
    void testGetRequest() {
        RequestResponseExample example = new RequestResponseExample();
        RequestResponseExample.Request request = new RequestResponseExample.Request(
                "GET", "/api/users", Map.of("Accept", "application/json"), null, null);
        RequestResponseExample.Response response = example.processRequest(request);
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    void testPostRequest() {
        RequestResponseExample example = new RequestResponseExample();
        RequestResponseExample.Request request = new RequestResponseExample.Request(
                "POST", "/api/users",
                Map.of("Content-Type", "application/json", "Authorization", "Bearer token"),
                Map.of("name", "John"), null);
        RequestResponseExample.Response response = example.processRequest(request);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testUnauthorizedRequest() {
        RequestResponseExample example = new RequestResponseExample();
        RequestResponseExample.Request request = new RequestResponseExample.Request(
                "GET", "/api/users", Map.of("Authorization", "Invalid"), null, null);
        RequestResponseExample.Response response = example.processRequest(request);
        assertEquals(401, response.getStatus());
    }

    @Test
    void testContentTypeNegotiation() {
        RequestResponseExample example = new RequestResponseExample();
        RequestResponseExample.Request request = new RequestResponseExample.Request(
                "GET", "/api/users", Map.of("Accept", "application/xml"), null, null);
        RequestResponseExample.Response response = example.processRequest(request);
        assertEquals("application/xml", response.getContentType());
    }

    @Test
    void testRequestId() {
        RequestResponseExample example = new RequestResponseExample();
        RequestResponseExample.Request request = new RequestResponseExample.Request(
                "GET", "/api/users", null, null, null);
        RequestResponseExample.Response response = example.processRequest(request);
        assertTrue(response.getHeaders().containsKey("X-Request-Id"));
    }
}
