package academy.javaengineering.enterprise;

import java.util.Map;

public class ECommercePlatformExample {
    public static void main(String[] args) {
        System.out.println("=== E-Commerce Platform Examples ===\n");
        demonstrateServices();
        demonstrateFlows();
    }

    public static void demonstrateServices() {
        System.out.println("--- E-Commerce Microservices ---");
        Map<String, String> services = Map.of(
            "Product Catalog", "Product information and search",
            "Shopping Cart", "Cart management",
            "Order Service", "Order processing",
            "Payment Service", "Payment processing",
            "Inventory Service", "Stock management",
            "User Service", "User management",
            "Notification Service", "Email, SMS, push"
        );
        services.forEach((k, v) -> System.out.printf("  %-20s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateFlows() {
        System.out.println("--- Order Flow ---");
        String[] flow = {
            "1. User browses products",
            "2. User adds items to cart",
            "3. User proceeds to checkout",
            "4. System validates inventory",
            "5. System processes payment",
            "6. System creates order",
            "7. System sends confirmation"
        };
        for (String step : flow) {
            System.out.println("  " + step);
        }
        System.out.println();
    }
}
