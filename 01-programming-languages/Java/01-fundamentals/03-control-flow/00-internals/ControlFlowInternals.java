package academy.javaengineering.fundamentals.controlflow;

/**
 * Demonstrates control flow internals in Java.
 */
public class ControlFlowInternals {

    public static void main(String[] args) {
        System.out.println("=== Control Flow Internals Demo ===\n");

        // 1. If-else branching
        System.out.println("--- If-Else Branching ---");
        int x = 42;
        if (x > 0) {
            System.out.println(x + " is positive");
        } else if (x < 0) {
            System.out.println(x + " is negative");
        } else {
            System.out.println(x + " is zero");
        }

        // 2. Switch with jump table
        System.out.println("\n--- Switch Internals ---");
        String day = "WEDNESDAY";
        String type = switch (day) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println(day + " is a " + type);

        // 3. Loop internals
        System.out.println("\n--- For Loop Internals ---");
        for (int i = 0; i < 5; i++) {
            System.out.println("Iteration " + i);
        }

        System.out.println("\n--- While Loop Internals ---");
        int count = 0;
        while (count < 3) {
            System.out.println("While iteration " + count);
            count++;
        }

        // 4. Break and continue
        System.out.println("\n--- Break and Continue ---");
        for (int i = 0; i < 10; i++) {
            if (i == 3) continue; // Skip 3
            if (i == 7) break;   // Exit at 7
            System.out.print(i + " ");
        }
        System.out.println();

        // 5. Nested loop break
        System.out.println("\n--- Nested Loop Break ---");
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i * j > 6) {
                    System.out.println("\nBreaking at i=" + i + ", j=" + j);
                    break outer;
                }
                System.out.print(i * j + " ");
            }
            System.out.println();
        }

        System.out.println("\n=== Internals Demo Complete ===");
    }
}
