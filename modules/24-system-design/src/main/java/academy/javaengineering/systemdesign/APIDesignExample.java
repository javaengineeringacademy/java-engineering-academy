package academy.javaengineering.systemdesign;

import java.util.Map;

public class APIDesignExample {

    public static void main(String[] args) {
        System.out.println("=== API Design Examples ===\n");
        demonstrateAPITypes();
        demonstratePrinciples();
    }

    public static void demonstrateAPITypes() {
        System.out.println("--- API Types ---");
        Map<String, String> types = Map.of(
            "REST", "Resource-based, HTTP methods",
            "GraphQL", "Query language for APIs",
            "gRPC", "High-performance RPC",
            "WebSocket", "Full-duplex communication"
        );
        types.forEach((k, v) -> System.out.printf("  %-12s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstratePrinciples() {
        System.out.println("--- REST Principles ---");
        String[] principles = {
            "Stateless: Each request contains all info",
            "Uniform Interface: Consistent resource naming",
            "Client-Server: Separation of concerns",
            "Cacheable: Responses can be cached"
        };
        for (String p : principles) {
            System.out.println("  " + p);
        }
        System.out.println();
    }
}
