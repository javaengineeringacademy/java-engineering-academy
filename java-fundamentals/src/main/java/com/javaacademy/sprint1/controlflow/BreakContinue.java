package com.javaacademy.sprint1.controlflow;

/**
 * BreakContinue - Demonstrates break, continue, and labeled statements.
 *
 * <p><b>Control Transfer Statements:</b>
 * <ul>
 *   <li><b>break:</b> Exit loop or switch immediately</li>
 *   <li><b>continue:</b> Skip to next iteration of loop</li>
 *   <li><b>label:</b> Identify loop for break/continue (use sparingly)</li>
 * </ul>
 *
 * <p><b>Real-world analogy:</b>
 * - break = "Fire alarm - everyone out NOW"
 * - continue = "Skip this person, next please"
 *
 * <p><b>Best Practice:</b> Avoid labels; refactor into methods instead.
 * Labels make code harder to read and maintain.
 *
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class BreakContinue {

    private BreakContinue() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        System.out.println("=== Break and Continue ===\n");

        // Break in for loop
        System.out.println("--- Break in For Loop ---");
        for (int i = 1; i <= 10; i++) {
            if (i == 5) {
                System.out.println("Breaking at " + i);
                break; // Exits loop entirely
            }
            System.out.println("i = " + i);
        }
        System.out.println("Loop ended\n");

        // Continue in for loop
        System.out.println("--- Continue in For Loop ---");
        for (int i = 1; i <= 5; i++) {
            if (i == 3) {
                System.out.println("Skipping " + i);
                continue; // Skips rest of this iteration
            }
            System.out.println("i = " + i);
        }
        System.out.println("Loop ended\n");

        // Break in while
        System.out.println("--- Break in While ---");
        int num = 0;
        while (true) {
            num++;
            if (num > 100) break; // Safety net
            if (num % 7 == 0) {
                System.out.println("First multiple of 7: " + num);
                break;
            }
        }

        // Continue in while
        System.out.println("\n--- Continue in While ---");
        int n = 0;
        while (n < 10) {
            n++;
            if (n % 2 == 0) continue; // Skip even numbers
            System.out.println("Odd: " + n);
        }

        // Labeled break (break outer loop)
        System.out.println("\n--- Labeled Break ---");
        outerLoop: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    System.out.println("Breaking outer at i=2, j=2");
                    break outerLoop; // Breaks OUTER loop
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // Labeled continue (continue outer loop)
        System.out.println("\n--- Labeled Continue ---");
        outerLoop: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (j == 2) {
                    System.out.println("Continue outer at i=" + i);
                    continue outerLoop; // Skips to next i
                }
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // Practical: Search in 2D array
        System.out.println("\n--- Practical: Search 2D Array ---");
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int target = 5;
        boolean found = false;

        search: for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == target) {
                    System.out.println("Found " + target + " at [" + i + "][" + j + "]");
                    found = true;
                    break search;
                }
            }
        }
        if (!found) System.out.println("Not found");

        // Break in switch (covered in SwitchStatement)
        System.out.println("\n--- Break in Switch (implicit) ---");
        // Switch uses break implicitly in traditional switch
        // switch expressions (->) don't need break

        // Expected output demonstrates all break/continue scenarios
    }
}