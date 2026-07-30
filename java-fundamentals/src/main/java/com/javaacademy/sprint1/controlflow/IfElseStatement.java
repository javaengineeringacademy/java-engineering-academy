package com.javaacademy.sprint1.controlflow;

/**
 * IfElseStatement - Demonstrates if, if-else, if-else-if ladder, and nested if.
 * 
 * <p><b>Decision Making in Java:</b>
 * <ul>
 *   <li><b>if:</b> Execute block if condition true</li>
 *   <li><b>if-else:</b> Two paths - true or false</li>
 *   <li><b>if-else-if:</b> Multiple mutually exclusive conditions</li>
 *   <li><b>Nested if:</b> if inside another if</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like a flowchart - "Is it raining? Yes → take umbrella. No → enjoy sun."
 * 
 * <p><b>Best Practices:</b>
 * <ul>
 *   <li>Always use braces {} even for single statements</li>
 *   <li>Put most likely condition first</li>
 *   <li>Use else-if ladder instead of multiple independent ifs when mutually exclusive</li>
 *   <li>Consider switch for many equal-value checks</li>
 * </ul>
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class IfElseStatement {

    private IfElseStatement() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        int score = 85;
        int age = 20;
        boolean hasLicense = true;

        System.out.println("=== If-Else Statements ===\n");

        // Simple if
        System.out.println("--- Simple if ---");
        if (score > 50) {
            System.out.println("Passed! Score: " + score);
        }

        // If-else
        System.out.println("\n--- If-Else ---");
        if (score >= 60) {
            System.out.println("Grade: Pass");
        } else {
            System.out.println("Grade: Fail");
        }

        // If-else-if ladder (mutually exclusive)
        System.out.println("\n--- If-Else-If Ladder ---");
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade: B");
        } else if (score >= 70) {
            System.out.println("Grade: C");
        } else if (score >= 60) {
            System.out.println("Grade: D");
        } else {
            System.out.println("Grade: F");
        }

        // Multiple conditions with logical operators
        System.out.println("\n--- Multiple Conditions ---");
        if (age >= 18 && hasLicense) {
            System.out.println("Can drive");
        } else if (age >= 18 && !hasLicense) {
            System.out.println("Can learn to drive");
        } else {
            System.out.println("Too young to drive");
        }

        // Nested if
        System.out.println("\n--- Nested If ---");
        if (age >= 18) {
            if (hasLicense) {
                System.out.println("Adult with license");
            } else {
                System.out.println("Adult without license");
            }
        } else {
            System.out.println("Minor");
        }

        // Ternary operator (conditional operator)
        System.out.println("\n--- Ternary Operator ---");
        String result = (score >= 60) ? "Pass" : "Fail";
        System.out.println("Result: " + result);
        
        // Nested ternary (avoid in production!)
        String grade = score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : "F";
        System.out.println("Grade (ternary): " + grade);

        // Common mistake: dangling else
        System.out.println("\n--- Dangling Else (avoid with braces) ---");
        int x = 5, y = 10;
        if (x > 0)
            if (y > 0)
                System.out.println("Both positive");
        // else belongs to inner if!
        // Use braces to make it clear:
        if (x > 0) {
            if (y > 0) {
                System.out.println("Both positive (with braces)");
            }
        } else {
            System.out.println("x not positive");
        }

        // Expected output
    }
}