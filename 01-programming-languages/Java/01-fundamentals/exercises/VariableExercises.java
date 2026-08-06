package academy.javaengineering.exercises;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercises: Variables, Types, and Type Casting
 *
 * Complete the TODO sections below. Each method should return
 * the correct result based on the instructions.
 */
public class VariableExercises {

    // TODO 1: Declare and return variables of each primitive type
    // Return an Object array containing: byte, short, int, long, float, double, char, boolean
    // Use these exact values: 42 (byte), 1000 (short), 100000 (int), 999999999L (long),
    // 3.14f (float), 2.718281828 (double), 'A' (char), true (boolean)
    public Object[] primitiveTypeValues() {
        // TODO: implement this
        return new Object[0];
    }

    // TODO 2: Demonstrate widening and narrowing casting
    // Method should return an array of 4 values:
    // [0] = int promoted to long (widening) - value 100
    // [1] = long cast to int (narrowing) - value from 1000L cast to int
    // [2] = double cast to int (narrowing) - value from 9.99 cast to int
    // [3] = int cast to double (widening) - value 200
    public Object[] typeCasting() {
        // TODO: implement this
        return new Object[0];
    }

    // TODO 3: Calculate compound interest
    // Given: principal, annualRate (as percentage), years, compoundingsPerYear
    // Return the final amount rounded to 2 decimal places
    // Formula: A = P * (1 + r/n)^(n*t)
    public double compoundInterest(double principal, double annualRate, int years, int compoundingsPerYear) {
        // TODO: implement this
        return 0.0;
    }

    // TODO 4: Convert temperature between Celsius and Fahrenheit
    // Return a double array: [convertedCelsius, convertedFahrenheit]
    // The input celsius and fahrenheit are the original values to convert from
    public double[] temperatureConversion(double celsius, double fahrenheit) {
        // TODO: implement this
        // F = (C × 9/5) + 32
        // C = (F − 32) × 5/9
        return new double[]{0.0, 0.0};
    }

    // TODO 5: Variable naming conventions check
    // Given a variable name as a string, determine if it follows Java naming conventions
    // Variable names should start with a lowercase letter, no spaces, no underscores at start
    // Return true if valid, false otherwise
    public boolean isValidVariableName(String name) {
        // TODO: implement this
        return false;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        VariableExercises exercises = new VariableExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== VariableExercises Tests ===\n");

        // Test 1
        total++;
        try {
            Object[] values = exercises.primitiveTypeValues();
            if (values.length == 8
                && values[0] instanceof Byte && ((Byte) values[0]) == 42
                && values[1] instanceof Short && ((Short) values[1]) == 1000
                && values[2] instanceof Integer && ((Integer) values[2]) == 100000
                && values[3] instanceof Long && ((Long) values[3]) == 999999999L
                && values[4] instanceof Float && ((Float) values[4]) == 3.14f
                && values[5] instanceof Double && ((Double) values[5]) == 2.718281828
                && values[6] instanceof Character && ((Character) values[6]) == 'A'
                && values[7] instanceof Boolean && ((Boolean) values[7]) == true) {
                System.out.println("Test 1 PASSED: primitiveTypeValues");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: primitiveTypeValues - incorrect values or types");
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: primitiveTypeValues - " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            Object[] casts = exercises.typeCasting();
            if (casts.length == 4
                && casts[0] instanceof Long && ((Long) casts[0]) == 100L
                && casts[1] instanceof Integer && ((Integer) casts[1]) == 1000
                && casts[2] instanceof Integer && ((Integer) casts[2]) == 9
                && casts[3] instanceof Double && ((Double) casts[3]) == 200.0) {
                System.out.println("Test 2 PASSED: typeCasting");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: typeCasting - incorrect values");
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: typeCasting - " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            double result = exercises.compoundInterest(1000, 5, 10, 12);
            if (Math.abs(result - 1647.01) < 0.1) {
                System.out.println("Test 3 PASSED: compoundInterest");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: compoundInterest - expected ~1647.01, got " + result);
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: compoundInterest - " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            double[] temps = exercises.temperatureConversion(100, 32);
            if (Math.abs(temps[0] - 212.0) < 0.01 && Math.abs(temps[1] - 0.0) < 0.01) {
                System.out.println("Test 4 PASSED: temperatureConversion");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: temperatureConversion - expected [212.0, 0.0], got [" + temps[0] + ", " + temps[1] + "]");
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: temperatureConversion - " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            boolean test5a = exercises.isValidVariableName("myVariable");
            boolean test5b = exercises.isValidVariableName("_invalid");
            boolean test5c = exercises.isValidVariableName("123bad");
            boolean test5d = exercises.isValidVariableName("validName123");
            if (test5a && !test5b && !test5c && test5d) {
                System.out.println("Test 5 PASSED: isValidVariableName");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: isValidVariableName");
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: isValidVariableName - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
