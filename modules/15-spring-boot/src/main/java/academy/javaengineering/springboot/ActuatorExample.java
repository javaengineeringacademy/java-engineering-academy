package academy.javaengineering.springboot;

import java.util.HashMap;
import java.util.Map;

/**
 * Actuator - Health, Metrics Endpoints.
 */
public class ActuatorExample {

    public static class HealthEndpoint {
        private final Map<String, String> components = new HashMap<>();

        public void addComponent(String name, String status) { components.put(name, status); }

        public String getHealth() {
            boolean allUp = components.values().stream().allMatch(s -> "UP".equals(s));
            return allUp ? "UP" : "DOWN";
        }

        public Map<String, String> getComponents() { return components; }
    }

    public static void main(String[] args) {
        HealthEndpoint health = new HealthEndpoint();
        health.addComponent("db", "UP");
        health.addComponent("redis", "UP");
        System.out.println("Health: " + health.getHealth());
        System.out.println("Components: " + health.getComponents());
    }
}
