package academy.javaengineering.systemdesign;

import java.util.Map;

public class SystemDesignFundamentalsExample {

    public static void main(String[] args) {
        System.out.println("=== System Design Fundamentals ===\n");
        demonstrateCAP();
        demonstrateScalability();
        demonstrateAvailability();
    }

    public static void demonstrateCAP() {
        System.out.println("--- CAP Theorem ---");
        Map<String, String> cap = Map.of(
            "Consistency", "All nodes see same data",
            "Availability", "Every request gets response",
            "Partition Tolerance", "System works despite network failures"
        );
        cap.forEach((k, v) -> System.out.printf("  %-20s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateScalability() {
        System.out.println("--- Scalability Types ---");
        Map<String, String> types = Map.of(
            "Horizontal", "Add more machines",
            "Vertical", "Add more power to machine",
            "Read Scaling", "Add read replicas",
            "Write Scaling", "Sharding, partitioning"
        );
        types.forEach((k, v) -> System.out.printf("  %-15s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateAvailability() {
        System.out.println("--- Availability Targets ---");
        Map<String, String> availability = Map.of(
            "99.9%", "8.76 hours downtime/year",
            "99.99%", "52.6 minutes downtime/year",
            "99.999%", "5.26 minutes downtime/year"
        );
        availability.forEach((k, v) -> System.out.printf("  %-10s - %s%n", k, v));
        System.out.println();
    }
}
