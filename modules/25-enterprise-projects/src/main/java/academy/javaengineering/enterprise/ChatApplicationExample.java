package academy.javaengineering.enterprise;

import java.util.Map;

public class ChatApplicationExample {
    public static void main(String[] args) {
        System.out.println("=== Chat Application Examples ===\n");
        demonstrateComponents();
        demonstrateFlow();
    }

    public static void demonstrateComponents() {
        System.out.println("--- Chat Components ---");
        Map<String, String> components = Map.of(
            "WebSocket Server", "Real-time communication",
            "Message Broker", "Message distribution",
            "Presence Service", "Online status tracking",
            "Message Store", "Message persistence",
            "Push Notifications", "Offline delivery"
        );
        components.forEach((k, v) -> System.out.printf("  %-20s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateFlow() {
        System.out.println("--- Message Flow ---");
        String[] flow = {
            "1. User connects via WebSocket",
            "2. Server registers connection",
            "3. User sends message",
            "4. Server processes message",
            "5. Server distributes to recipients",
            "6. Recipients receive message",
            "7. Server stores message"
        };
        for (String step : flow) {
            System.out.println("  " + step);
        }
        System.out.println();
    }
}
