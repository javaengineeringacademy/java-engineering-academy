package academy.javaengineering.exercises;

/**
 * Exercises: Operators (Arithmetic, Logical, Ternary)
 *
 * Complete the TODO sections below.
 */
public class OperatorExercises {

    // TODO 1: Calculate the day of year given month (1-12), day, and whether it's a leap year
    // Return the day number (1-366) within the year
    // Example: March 1 in a non-leap year = 60
    public int dayOfYear(int month, int day, boolean isLeapYear) {
        // TODO: implement this
        return 0;
    }

    // TODO 2: Check if a number is a power of two using bitwise AND
    // Must use the bitwise trick (n & (n-1)) == 0 for powers of two
    public boolean isPowerOfTwo(int n) {
        // TODO: implement this
        return false;
    }

    // TODO 3: Implement a grade calculator using ternary operator
    // Return letter grade based on score:
    // 90-100: "A", 80-89: "B", 70-79: "C", 60-69: "D", below 60: "F"
    // Use a single return statement with nested ternary operators
    public String calculateGrade(int score) {
        // TODO: implement this using ternary operator
        return "";
    }

    // TODO 4: Check if a year is a leap year
    // Rules: divisible by 4, but not by 100 unless also by 400
    public boolean isLeapYear(int year) {
        // TODO: implement this
        return false;
    }

    // TODO 5: Calculate shipping cost based on weight and distance
    // Base rate: $5.00
    // Weight surcharge: $0.50 per kg over 5kg
    // Distance surcharge: $0.10 per km over 100km
    // Return total cost rounded to 2 decimal places
    public double calculateShipping(double weightKg, double distanceKm) {
        // TODO: implement this
        return 0.0;
    }

    // TODO 6: Determine eligibility using logical operators
    // A person is eligible if:
    // - Age is between 18 and 65 (inclusive)
    // - AND (has insurance OR income > 30000)
    // - AND NOT is blacklisted
    public boolean isEligible(int age, boolean hasInsurance, double income, boolean isBlacklisted) {
        // TODO: implement this using logical operators
        return false;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        OperatorExercises exercises = new OperatorExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== OperatorExercises Tests ===\n");

        // Test 1
        total++;
        int day1 = exercises.dayOfYear(1, 1, false);
        int day2 = exercises.dayOfYear(3, 1, false);
        int day3 = exercises.dayOfYear(12, 31, false);
        int day4 = exercises.dayOfYear(2, 29, true);
        if (day1 == 1 && day2 == 60 && day3 == 365 && day4 == 60) {
            System.out.println("Test 1 PASSED: dayOfYear");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: dayOfYear - expected [1, 60, 365, 60], got [" + day1 + ", " + day2 + ", " + day3 + ", " + day4 + "]");
        }

        // Test 2
        total++;
        boolean t2a = exercises.isPowerOfTwo(16);
        boolean t2b = exercises.isPowerOfTwo(18);
        boolean t2c = exercises.isPowerOfTwo(1);
        boolean t2d = exercises.isPowerOfTwo(0);
        if (t2a && !t2b && t2c && !t2d) {
            System.out.println("Test 2 PASSED: isPowerOfTwo");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: isPowerOfTwo");
        }

        // Test 3
        total++;
        String g1 = exercises.calculateGrade(95);
        String g2 = exercises.calculateGrade(85);
        String g3 = exercises.calculateGrade(75);
        String g4 = exercises.calculateGrade(65);
        String g5 = exercises.calculateGrade(55);
        if ("A".equals(g1) && "B".equals(g2) && "C".equals(g3) && "D".equals(g4) && "F".equals(g5)) {
            System.out.println("Test 3 PASSED: calculateGrade");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: calculateGrade - got [" + g1 + ", " + g2 + ", " + g3 + ", " + g4 + ", " + g5 + "]");
        }

        // Test 4
        total++;
        boolean l1 = exercises.isLeapYear(2000);
        boolean l2 = exercises.isLeapYear(1900);
        boolean l3 = exercises.isLeapYear(2024);
        boolean l4 = exercises.isLeapYear(2023);
        if (l1 && !l2 && l3 && !l4) {
            System.out.println("Test 4 PASSED: isLeapYear");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: isLeapYear");
        }

        // Test 5
        total++;
        double cost1 = exercises.calculateShipping(3, 50);
        double cost2 = exercises.calculateShipping(10, 200);
        if (Math.abs(cost1 - 5.0) < 0.01 && Math.abs(cost2 - 17.5) < 0.01) {
            System.out.println("Test 5 PASSED: calculateShipping");
            passed++;
        } else {
            System.out.println("Test 5 FAILED: calculateShipping - expected [5.0, 17.5], got [" + cost1 + ", " + cost2 + "]");
        }

        // Test 6
        total++;
        boolean e1 = exercises.isEligible(30, true, 40000, false);
        boolean e2 = exercises.isEligible(17, true, 40000, false);
        boolean e3 = exercises.isEligible(30, false, 20000, false);
        boolean e4 = exercises.isEligible(30, true, 40000, true);
        if (e1 && !e2 && !e3 && !e4) {
            System.out.println("Test 6 PASSED: isEligible");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: isEligible");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
