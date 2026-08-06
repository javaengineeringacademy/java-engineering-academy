package academy.javaengineering.fundamentals;

/**
 * Control Flow in Java
 *
 * This file covers:
 * - if / else / else if statements
 * - switch statement (traditional and enhanced)
 * - for loop
 * - enhanced for loop (for-each)
 * - while loop
 * - do-while loop
 * - break and continue statements
 */
public class ControlFlow {

    public static void main(String[] args) {

        // =========================================================
        // 1. IF / ELSE / ELSE IF
        // =========================================================
        System.out.println("=== if / else / else if ===");

        int temperature = 28;

        if (temperature > 35) {
            System.out.println("It's very hot outside!");
        } else if (temperature > 25) {
            System.out.println("It's warm outside.");   // This executes
        } else if (temperature > 15) {
            System.out.println("It's cool outside.");
        } else {
            System.out.println("It's cold outside.");
        }

        // Nested if statements
        int age = 20;
        boolean hasID = true;

        if (age >= 18) {
            if (hasID) {
                System.out.println("Entry allowed with ID verification.");
            } else {
                System.out.println("Please bring your ID.");
            }
        } else {
            System.out.println("Entry restricted to 18+ only.");
        }

        // Shorthand if without braces (not recommended for complex code)
        int score = 85;
        String result = (score >= 60) ? "Pass" : "Fail";
        System.out.println("Score " + score + ": " + result);

        // =========================================================
        // 2. SWITCH STATEMENT
        // =========================================================
        System.out.println("\n=== Switch Statement ===");

        // Traditional switch
        String day = "Wednesday";
        String dayType;

        switch (day) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
            case "Friday":
                dayType = "Weekday";
                break;
            case "Saturday":
            case "Sunday":
                dayType = "Weekend";
                break;
            default:
                dayType = "Invalid day";
                break;
        }
        System.out.println(day + " is a " + dayType);

        // Switch with fall-through
        int month = 3;
        String season;

        switch (month) {
            case 12: case 1: case 2:
                season = "Winter";
                break;
            case 3: case 4: case 5:
                season = "Spring";  // This executes
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
        System.out.println("Month " + month + " is in " + season);

        // Switch with expressions (Java 14+)
        int code = 200;
        String httpMessage = switch (code) {
            case 200 -> "OK";
            case 301 -> "Moved Permanently";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
        System.out.println("HTTP " + code + ": " + httpMessage);

        // =========================================================
        // 3. FOR LOOP
        // =========================================================
        System.out.println("\n=== For Loop ===");

        // Basic for loop
        System.out.print("Count 1 to 5: ");
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // Counting backwards
        System.out.print("Countdown: ");
        for (int i = 5; i >= 1; i--) {
            System.out.print(i + " ");
        }
        System.out.println(" Go!");

        // Sum of numbers 1 to 100
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("Sum of 1 to 100: " + sum); // 5050

        // Multiple variables in for loop
        System.out.print("Even numbers 0-10: ");
        for (int i = 0, j = 0; i <= 10; i++) {
            if (i % 2 == 0) {
                System.out.print(i + " ");
            }
        }
        System.out.println();

        // Nested for loops - multiplication table
        System.out.println("\n5x5 Multiplication Table:");
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                System.out.printf("%4d", i * j);
            }
            System.out.println();
        }

        // =========================================================
        // 4. ENHANCED FOR LOOP (for-each)
        // =========================================================
        System.out.println("\n=== Enhanced For Loop ===");

        String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Elderberry"};

        System.out.print("Fruits: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();

        // Iterating over a 2D array
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("Matrix:");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.printf("%4d", val);
            }
            System.out.println();
        }

        // =========================================================
        // 5. WHILE LOOP
        // =========================================================
        System.out.println("\n=== While Loop ===");

        // Basic while loop
        int count = 5;
        System.out.print("Countdown: ");
        while (count > 0) {
            System.out.print(count + " ");
            count--;
        }
        System.out.println("Liftoff!");

        // While loop with condition
        int number = 1024;
        int powers = 0;
        while (number > 1) {
            number /= 2;
            powers++;
        }
        System.out.println("\n1024 is 2^" + powers); // 2^10

        // =========================================================
        // 6. DO-WHILE LOOP
        // =========================================================
        System.out.println("\n=== Do-While Loop ===");

        // Do-while always executes at least once
        int attempts = 0;
        do {
            attempts++;
            System.out.println("Attempt #" + attempts + ": trying...");
        } while (attempts < 3);

        System.out.println("Completed after " + attempts + " attempts");

        // Simulating user input validation
        int input = 42;
        int generatedAttempts = 0;
        int target = 42;

        do {
            generatedAttempts++;
            // Simulating random input
            input = (generatedAttempts == 2) ? target : generatedAttempts * 10;
            System.out.println("Generated value: " + input + ", Target: " + target);
        } while (input != target && generatedAttempts < 5);

        if (input == target) {
            System.out.println("Found target on attempt " + generatedAttempts + "!");
        }

        // =========================================================
        // 7. BREAK AND CONTINUE
        // =========================================================
        System.out.println("\n=== Break and Continue ===");

        // break - exits the loop immediately
        System.out.print("Break at 5: ");
        for (int i = 1; i <= 10; i++) {
            if (i == 6) {
                break;  // Exit loop when i reaches 6
            }
            System.out.print(i + " ");
        }
        System.out.println(); // Output: 1 2 3 4 5

        // continue - skips current iteration and continues to next
        System.out.print("Skip odd numbers: ");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 != 0) {
                continue;  // Skip odd numbers
            }
            System.out.print(i + " ");
        }
        System.out.println(); // Output: 2 4 6 8 10

        // Labeled break - exits specific outer loop
        System.out.println("\nLabeled break example:");
        outer:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 2 && j == 3) {
                    System.out.println("\nBreaking out of both loops at i=" + i + ", j=" + j);
                    break outer;
                }
                System.out.print("(" + i + "," + j + ") ");
            }
            System.out.println();
        }

        // Labeled continue
        System.out.println("\nLabeled continue - skip rest of outer loop:");
        outerLoop:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 1) {
                    continue outerLoop;  // Skip rest of outer loop
                }
                System.out.print("(" + i + "," + j + ") ");
            }
            System.out.println("This won't print");
        }

        // =========================================================
        // 8. INFINITE LOOPS AND EXIT CONDITIONS
        // =========================================================
        System.out.println("\n=== Practical Loop Examples ===");

        // Finding prime numbers up to 50
        System.out.print("Primes up to 50: ");
        for (int num = 2; num <= 50; num++) {
            boolean isPrime = true;
            for (int divisor = 2; divisor <= Math.sqrt(num); divisor++) {
                if (num % divisor == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                System.out.print(num + " ");
            }
        }
        System.out.println();

        // Fibonacci sequence
        System.out.print("Fibonacci (10 terms): ");
        int fib1 = 0, fib2 = 1;
        for (int i = 0; i < 10; i++) {
            System.out.print(fib1 + " ");
            int next = fib1 + fib2;
            fib1 = fib2;
            fib2 = next;
        }
        System.out.println();

        // Factorial
        int n = 10;
        long factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        System.out.println("\n" + n + "! = " + factorial); // 3628800

        System.out.println("\n=== Control Flow Demo Complete ===");
    }
}
