package academy.javaengineering.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RestControllerTest {

    private RestControllerExample controller;

    @BeforeEach
    void setUp() {
        controller = new RestControllerExample();
    }

    @Test
    void testCreateUser() {
        RestControllerExample.RestResponse response = controller.createUser(
                Map.of("name", "John", "email", "john@example.com"));
        assertEquals(201, response.getStatus());
        assertNotNull(response.getLocation());
    }

    @Test
    void testCreateUserInvalid() {
        RestControllerExample.RestResponse response = controller.createUser(Map.of("name", "John"));
        assertEquals(400, response.getStatus());
    }

    @Test
    void testGetUser() {
        controller.createUser(Map.of("name", "John", "email", "john@example.com"));
        RestControllerExample.RestResponse response = controller.getUser(1L);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testGetUserNotFound() {
        RestControllerExample.RestResponse response = controller.getUser(999L);
        assertEquals(404, response.getStatus());
    }

    @Test
    void testUpdateUser() {
        controller.createUser(Map.of("name", "John", "email", "john@example.com"));
        RestControllerExample.RestResponse response = controller.updateUser(1L, Map.of("name", "Updated"));
        assertEquals(200, response.getStatus());
    }

    @Test
    void testDeleteUser() {
        controller.createUser(Map.of("name", "John", "email", "john@example.com"));
        RestControllerExample.RestResponse response = controller.deleteUser(1L);
        assertEquals(204, response.getStatus());
    }

    @Test
    void testDeleteUserNotFound() {
        RestControllerExample.RestResponse response = controller.deleteUser(999L);
        assertEquals(404, response.getStatus());
    }
}
