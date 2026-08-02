package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WebStarterTest {

    @Test
    void shouldAddRoutes() {
        WebStarterExample.RestController controller = new WebStarterExample.RestController("/api");
        controller.addRoute("GET", "/users");
        controller.addRoute("POST", "/users");
        assertEquals(2, controller.getRoutes().size());
    }

    @Test
    void shouldFormatRouteCorrectly() {
        WebStarterExample.RestController controller = new WebStarterExample.RestController("/api");
        controller.addRoute("GET", "/users");
        assertTrue(controller.getRoutes().get(0).contains("GET /api/users"));
    }
}
