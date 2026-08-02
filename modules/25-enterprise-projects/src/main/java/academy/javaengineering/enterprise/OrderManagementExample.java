package academy.javaengineering.enterprise;

import java.util.Map;

public class OrderManagementExample {
    public static void main(String[] args) {
        System.out.println("=== Order Management Examples ===\n");
        demonstrateSagaPattern();
        demonstrateOrderStates();
    }

    public static void demonstrateSagaPattern() {
        System.out.println("--- Saga Pattern ---");
        Map<String, String> saga = Map.of(
            "Create Order", "Initialize order",
            "Reserve Inventory", "Hold stock",
            "Process Payment", "Charge customer",
            "Confirm Order", "Finalize order",
            "Ship Order", "Dispatch delivery"
        );
        saga.forEach((k, v) -> System.out.printf("  %-20s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateOrderStates() {
        System.out.println("--- Order States ---");
        String[] states = {
            "CREATED → INVENTORY_RESERVED → PAYMENT_PROCESSED → CONFIRMED → SHIPPED → DELIVERED",
            "CANCELLED (at any point before SHIPPED)"
        };
        for (String state : states) {
            System.out.println("  " + state);
        }
        System.out.println();
    }
}
