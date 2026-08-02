package academy.javaengineering.springboot;

import java.util.ArrayList;
import java.util.List;

/**
 * Web Starter - @RestController, Request Mapping.
 */
public class WebStarterExample {

    public static class RestController {
        private final String path;
        private final List<String> routes = new ArrayList<>();

        public RestController(String path) { this.path = path; }

        public void addRoute(String method, String endpoint) {
            routes.add(method.toUpperCase() + " " + path + endpoint);
        }

        public List<String> getRoutes() { return routes; }
    }

    public static void main(String[] args) {
        RestController controller = new RestController("/api/users");
        controller.addRoute("GET", "");
        controller.addRoute("GET", "/{id}");
        controller.addRoute("POST", "");
        System.out.println("Routes: " + controller.getRoutes());
    }
}
