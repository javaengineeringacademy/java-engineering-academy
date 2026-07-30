package com.javaacademy.sprint1.controlflow;

/**
 * WhileDoWhileLoop - Demonstrates while and do-while loops.
 * 
 * <p><b>Loop Types:</b>
 * <ul>
 *   <li><b>while (condition):</b> Pre-test loop (checks before each iteration)</li>
 *   <li><b>do-while (condition):</b> Post-test loop (runs at least once)</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b>
 * - while = "While there's coffee in the pot, pour a cup" (check first)
 * - do-while = "Taste the coffee, then decide if you want more" (try first)
 * 
 * <p><b>Key Difference:</b> do-while executes body AT LEAST ONCE.
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Use while when zero iterations is valid</li>
 *   <li>Use do-when when at least one iteration is required</li>
 *   <li>Ensure loop variable changes toward termination</li>
 *   <li>Avoid infinite loops unless intentional (with break)</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class WhileDoWhileLoop {

    private WhileDoWhileLoop() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== While and Do-While Loops ===\n");

        // While loop - pre-test
        System.out.println("--- While Loop ---");
        int count = 5;
        while (count > 0) {
            System.out.println("Countdown: " + count);
            count--;
        }

        // While with complex condition
        System.out.println("\n--- While with Compound Condition ---");
        int x = 0, y = 10;
        while (x < 5 && y > 5) {
            System.out.println("x=" + x + ", y=" + y);
            x++;
            y--;
        }

        // While true with break (common for input validation)
        System.out.println("\n--- While True with Break ---");
        int attempts = 0;
        while (true) {
            attempts++;
            System.out.println("Attempt " + attempts);
            if (attempts >= 3) {
                System.out.println("Max attempts reached");
                break;
            }
        }

        // Do-while loop - post-test (guaranteed at least one execution)
        System.out.println("\n--- Do-While Loop ---");
        int number;
        java.util.Scanner scanner = new java.util.Scanner("42"); // Simulated input
        do {
            System.out.print("Enter a positive number: ");
            number = scanner.nextInt();
            if (number <= 0) {
                System.out.println("Must be positive! Try again.");
            }
        } while (number <= 0);
        System.out.println("You entered: " + number);
        scanner.close();

        // Practical do-while: menu system
        System.out.println("\n--- Do-While Menu Example ---");
        int choice;
        do {
            System.out.println("1. View Profile");
            System.out.println("2. Settings");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            // Simulated choices: 1, 2, 3
            choice = new java.util.Scanner("1\n2\n3\n").nextInt();
            switch (choice) {
                case 1 -> System.out.println("Profile viewed");
                case 2 -> System.out.println("Settings opened");
                case 3 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice");
            }
        } while (choice != 3);

        // Nested loops
        System.out.println("\n--- Nested While Loops ---");
        int rows = 3;
        int i = 1;
        while (i <= rows) {
            int j = 1;
            while (j <= i) {
                System.out.print("* ");
                j++;
            }
            System.out.println();
            i++;
        }

        // Expected output shows all while/do-while variants
    }
}