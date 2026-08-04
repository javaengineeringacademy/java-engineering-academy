package academy.javaengineering.fundamentals;

import java.util.Random;

/**
 * Demonstrates Java control flow statements: conditional statements
 * (if/else, switch), loops (for, while, do-while), and loop control
 * (break, continue, labeled loops).
 *
 * <p>Control flow determines the order in which statements are executed.
 * Java provides several mechanisms for conditional execution and iteration.</p>
 */
public class ControlFlow {

    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("=== Control Flow Demo ===\n");

        demoIfElse();
        demoSwitchTraditional();
        demoSwitchExpressions();
        demoForLoops();
        demoWhileLoops();
        demoDoWhileLoops();
        demoBreakAndContinue();
        demoLabeledLoops();
        demoEnhancedForLoop();
    }

    // --- if/else Statements ---

    /**
     * Demonstrates if, if-else, if-else if-else chains.
     */
    public static void demoIfElse() {
        System.out.println("--- if/else Statements ---");

        // Simple if
        int temperature = 35;
        if (temperature > 30) {
            System.out.println("It's hot outside! (" + temperature + "°C)");
        }

        // if-else
        int hour = 22;
        if (hour < 12) {
            System.out.println("Good morning!");
        } else {
            System.out.println("Good afternoon/evening!");
        }

        // if-else if-else chain
        int score = 75;
        String grade;
        if (score >= 90) {
            grade = "A";
        } else if (score >= 80) {
            grade = "B";
        } else if (score >= 70) {
            grade = "C";
        } else if (score >= 60) {
            grade = "D";
        } else {
            grade = "F";
        }
        System.out.println("Score " + score + " -> Grade: " + grade);

        // Nested if
        boolean hasTicket = true;
        boolean isVip = false;
        if (hasTicket) {
            if (isVip) {
                System.out.println("VIP entrance - skip the line!");
            } else {
                System.out.println("Regular entrance - enjoy the show!");
            }
        } else {
            System.out.println("No ticket - please purchase one.");
        }

        // Ternary as compact if-else
        int age = 20;
        String category = (age < 13) ? "Child" : (age < 18) ? "Teenager" : "Adult";
        System.out.println("Age " + age + " -> " + category);
        System.out.println();
    }

    // --- Traditional Switch ---

    /**
     * Demonstrates traditional switch statement with fall-through.
     */
    public static void demoSwitchTraditional() {
        System.out.println("--- Traditional Switch Statement ---");

        int dayOfWeek = 3;
        String dayType;

        switch (dayOfWeek) {
            case 1:
                dayType = "Monday";
                break;
            case 2:
                dayType = "Tuesday";
                break;
            case 3:
                dayType = "Wednesday";
                break;
            case 4:
                dayType = "Thursday";
                break;
            case 5:
                dayType = "Friday";
                break;
            case 6:
            case 7:
                dayType = "Weekend";
                break;
            default:
                dayType = "Invalid day";
                break;
        }
        System.out.println("Day " + dayOfWeek + " is: " + dayType);

        // Fall-through behavior
        int month = 8;
        String season;
        switch (month) {
            case 12: case 1: case 2:
                season = "Winter";
                break;
            case 3: case 4: case 5:
                season = "Spring";
                break;
            case 6: case 7: case 8:
                season = "Summer";
                break;
            case 9: case 10: case 11:
                season = "Fall";
                break;
            default:
                season = "Unknown";
                break;
        }
        System.out.println("Month " + month + " is in: " + season);

        // Switch with String
        String command = "start";
        switch (command.toLowerCase()) {
            case "start":
                System.out.println("Starting application...");
                break;
            case "stop":
                System.out.println("Stopping application...");
                break;
            case "restart":
                System.out.println("Restarting application...");
                break;
            default:
                System.out.println("Unknown command: " + command);
                break;
        }

        // Switch with char
        char grade = 'B';
        switch (grade) {
            case 'A':
                System.out.println("Excellent!");
                break;
            case 'B':
                System.out.println("Good job!");
                break;
            case 'C':
                System.out.println("Average.");
                break;
            default:
                System.out.println("Needs improvement.");
                break;
        }
        System.out.println();
    }

    // --- Enhanced Switch Expressions (Java 14+) ---

    /**
     * Demonstrates enhanced switch with arrow syntax and yield.
     */
    public static void demoSwitchExpressions() {
        System.out.println("--- Enhanced Switch Expressions (Java 14+) ---");

        // Arrow syntax - no fall-through, no break needed
        int dayNumber = 6;
        String dayName = switch (dayNumber) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Invalid";
        };
        System.out.println("Day " + dayNumber + " = " + dayName);

        // Arrow syntax with multiple statements
        int score = 85;
        String feedback = switch (score / 10) {
            case 10, 9 -> {
                System.out.println("  Calculating bonus points...");
                yield "Excellent - A grade!";
            }
            case 8 -> {
                System.out.println("  Good work!");
                yield "Good - B grade!";
            }
            case 7 -> "Average - C grade";
            case 6 -> "Below average - D grade";
            default -> "Needs improvement - F grade";
        };
        System.out.println("Score " + score + ": " + feedback);

        // Null-safe switch with enhanced switch
        String input = null;
        String result = switch (input) {
            case null -> "Input is null";
            case "hello" -> "Greeting received";
            case "bye" -> "Farewell received";
            default -> "Unknown input: " + input;
        };
        System.out.println("Input '" + input + "' -> " + result);

        // Switch expression used directly in method call
        int month = 12;
        int daysInMonth = switch (month) {
            case 2 -> 28; // Simplified (ignoring leap years)
            case 4, 6, 9, 11 -> 30;
            case 1, 3, 5, 7, 8, 10, 12 -> 31;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };
        System.out.println("Month " + month + " has " + daysInMonth + " days");

        // Pattern matching in switch (Java 21+)
        Object obj = 42;
        String typeDescription = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String: \"" + s + "\"";
            case Double d -> "Double: " + d;
            case Boolean b -> "Boolean: " + b;
            case null -> "null";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
        System.out.println("Object type: " + typeDescription);
        System.out.println();
    }

    // --- For Loops ---

    /**
     * Demonstrates traditional for loop, for-each, and enhanced for.
     */
    public static void demoForLoops() {
        System.out.println("--- For Loops ---");

        // Basic for loop
        System.out.print("Counting 1-5: ");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Decrementing for loop
        System.out.print("Countdown: ");
        for (int i = 5; i >= 0; i--) {
            System.out.print(i + " ");
        }
        System.out.println("... Liftoff!");

        // Multiple variables in for loop
        System.out.print("Sum 1-10: ");
        int sum = 0;
        for (int i = 1, j = 10; i <= j; i++, j--) {
            sum += i + j;
        }
        System.out.println(sum);

        // Infinite for loop (with break)
        System.out.print("First multiple of 7 > 50: ");
        for (int i = 51; ; i++) {
            if (i % 7 == 0) {
                System.out.println(i);
                break;
            }
        }

        // Nested for loops - multiplication table (partial)
        System.out.println("\nMultiplication table (1-3):");
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                System.out.printf("%4d", i * j);
            }
            System.out.println();
        }
        System.out.println();
    }

    // --- While Loops ---

    /**
     * Demonstrates while loop behavior.
     */
    public static void demoWhileLoops() {
        System.out.println("--- While Loops ---");

        // Basic while loop
        int count = 0;
        System.out.print("Powers of 2 < 100: ");
        while (count < 100) {
            System.out.print(count + " ");
            count *= 2;
            if (count == 0) count = 1; // Handle initial 0
        }
        System.out.println();

        // While with condition
        int number = 12345;
        int reversed = 0;
        int temp = number;
        while (temp != 0) {
            reversed = reversed * 10 + temp % 10;
            temp /= 10;
        }
        System.out.println("Original: " + number + ", Reversed: " + reversed);

        // Sum of digits
        int num = 9876;
        int digitSum = 0;
        int n = num;
        while (n > 0) {
            digitSum += n % 10;
            n /= 10;
        }
        System.out.println("Sum of digits of " + num + ": " + digitSum);

        // While with complex condition
        int x = 1;
        while (x < 1000) {
            x *= 3;
        }
        System.out.println("First power of 3 >= 1000: " + x);
        System.out.println();
    }

    // --- Do-While Loops ---

    /**
     * Demonstrates do-while loop (executes at least once).
     */
    public static void demoDoWhileLoops() {
        System.out.println("--- Do-While Loops ---");

        // Basic do-while
        int i = 0;
        do {
            System.out.println("Iteration: " + i);
            i++;
        } while (i < 3);

        // Do-while always executes at least once
        int value = 100;
        do {
            System.out.println("Value is: " + value + " (always runs once)");
        } while (value < 10); // Condition is false but body executed once

        // Practical example: menu simulation
        int choice;
        int attempts = 0;
        do {
            attempts++;
            choice = random.nextInt(5); // Simulate random input
            System.out.println("Attempt " + attempts + ": choice = " + choice);
        } while (choice != 0 && attempts < 5);

        if (choice == 0) {
            System.out.println("Exit selected!");
        } else {
            System.out.println("Too many attempts.");
        }
        System.out.println();
    }

    // --- break and continue ---

    /**
     * Demonstrates break and continue statements.
     */
    public static void demoBreakAndContinue() {
        System.out.println("--- break and continue ---");

        // break exits the innermost loop
        System.out.print("break at 5: ");
        for (int i = 0; i < 10; i++) {
            if (i == 5) break;
            System.out.print(i + " ");
        }
        System.out.println();

        // continue skips to next iteration
        System.out.print("Skip even numbers: ");
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) continue;
            System.out.print(i + " ");
        }
        System.out.println();

        // break in while
        int num = 0;
        System.out.print("First 5 perfect squares: ");
        int count = 0;
        while (count < 5) {
            int sqrt = (int) Math.sqrt(num);
            if (sqrt * sqrt == num) {
                System.out.print(num + " ");
                count++;
            }
            num++;
        }
        System.out.println();

        // continue in while (careful with infinite loops)
        int n = 0;
        int sum = 0;
        while (n < 20) {
            n++;
            if (n % 3 == 0) continue; // Skip multiples of 3
            sum += n;
        }
        System.out.println("Sum of 1-20 excluding multiples of 3: " + sum);

        // break with label concept (preview)
        System.out.println("\nBreak with multiple conditions:");
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i * j > 6) {
                    System.out.println("Breaking at i=" + i + ", j=" + j);
                    break outer;
                }
            }
        }
        System.out.println();
    }

    // --- Labeled Loops ---

    /**
     * Demonstrates labeled break and continue for nested loops.
     */
    public static void demoLabeledLoops() {
        System.out.println("--- Labeled Loops ---");

        // Labeled break - exits the outer loop
        System.out.println("Labeled break (exit outer loop):");
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i + j == 5) {
                    System.out.println("  Breaking at i=" + i + ", j=" + j);
                    break outer;
                }
            }
        }

        // Labeled continue - continues the outer loop
        System.out.println("\nLabeled continue (skip rest of outer iteration):");
        outer2:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 2) {
                    continue outer2;
                }
                System.out.print("(" + i + "," + j + ") ");
            }
            System.out.println();
        }
        System.out.println();

        // Finding a value in a 2D array using labeled break
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int target = 5;
        int targetRow = -1, targetCol = -1;

        search:
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    targetRow = i;
                    targetCol = j;
                    break search;
                }
            }
        }
        System.out.println("Found " + target + " at [" + targetRow + "][" + targetCol + "]");
        System.out.println();
    }

    // --- Enhanced for-each Loop ---

    /**
     * Demonstrates enhanced for-each loop for arrays and collections.
     */
    public static void demoEnhancedForLoop() {
        System.out.println("--- Enhanced for-each Loop ---");

        // Array iteration
        int[] numbers = {10, 20, 30, 40, 50};
        System.out.print("Numbers: ");
        for (int num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();

        // String array
        String[] fruits = {"Apple", "Banana", "Cherry", "Date"};
        System.out.println("Fruits:");
        for (String fruit : fruits) {
            System.out.println("  - " + fruit);
        }

        // 2D array
        int[][] grid = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println("Grid:");
        for (int[] row : grid) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }

        // Iterating over a list
        var names = java.util.List.of("Alice", "Bob", "Charlie");
        System.out.print("Names: ");
        for (String name : names) {
            System.out.print(name + " ");
        }
        System.out.println();

        // Note: enhanced for loop doesn't provide index
        System.out.println("\nNote: Use traditional for loop when index is needed");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("  numbers[" + i + "] = " + numbers[i]);
        }
        System.out.println();
    }
}
