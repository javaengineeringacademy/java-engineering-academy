package academy.javaengineering.exercises;

/**
 * Exercises: Control Flow (if-else, switch, loops)
 *
 * Complete the TODO sections below.
 */
public class ControlFlowExercises {

    // TODO 1: FizzBuzz variant
    // For numbers 1 to n, return a String array where:
    // - Multiples of 3 AND 5: "FizzBuzz"
    // - Multiples of 3 only: "Fizz"
    // - Multiples of 5 only: "Buzz"
    // - Otherwise: the number itself as a String
    public String[] fizzBuzz(int n) {
        // TODO: implement this
        return new String[0];
    }

    // TODO 2: Find the second largest number in an array
    // If array has fewer than 2 distinct elements, return Integer.MIN_VALUE
    public int secondLargest(int[] numbers) {
        // TODO: implement this
        return Integer.MIN_VALUE;
    }

    // TODO 3: Check if a string is a valid password
    // A valid password must:
    // - Be at least 8 characters long
    // - Contain at least one uppercase letter
    // - Contain at least one lowercase letter
    // - Contain at least one digit
    // - Contain at least one special character (!@#$%^&*)
    public boolean isValidPassword(String password) {
        // TODO: implement this
        return false;
    }

    // TODO 4: Count vowels and consonants in a string
    // Return int array: [vowelCount, consonantCount]
    // Ignore non-alphabetic characters
    public int[] countVowelsConsonants(String text) {
        // TODO: implement this
        return new int[]{0, 0};
    }

    // TODO 5: Implement a simple calculator using switch
    // Supported operations: +, -, *, /, %
    // For division by zero, throw IllegalArgumentException
    // For invalid operator, throw IllegalArgumentException
    public double calculate(double a, double b, char operator) {
        // TODO: implement this using switch statement
        return 0.0;
    }

    // TODO 6: Generate Pascal's triangle up to n rows
    // Return as a 2D array where each row is a row of the triangle
    public int[][] pascalsTriangle(int n) {
        // TODO: implement this
        return new int[0][0];
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        ControlFlowExercises exercises = new ControlFlowExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ControlFlowExercises Tests ===\n");

        // Test 1
        total++;
        String[] fb = exercises.fizzBuzz(15);
        if (fb.length == 15
            && "FizzBuzz".equals(fb[14])
            && "Fizz".equals(fb[2])
            && "Buzz".equals(fb[4])
            && "7".equals(fb[6])) {
            System.out.println("Test 1 PASSED: fizzBuzz");
            passed++;
        } else {
            System.out.println("Test 1 FAILED: fizzBuzz");
        }

        // Test 2
        total++;
        int sl1 = exercises.secondLargest(new int[]{1, 2, 3, 4, 5});
        int sl2 = exercises.secondLargest(new int[]{5, 5, 5});
        int sl3 = exercises.secondLargest(new int[]{10, 20, 20});
        if (sl1 == 4 && sl2 == Integer.MIN_VALUE && sl3 == 10) {
            System.out.println("Test 2 PASSED: secondLargest");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: secondLargest - expected [4, MIN_VALUE, 10], got [" + sl1 + ", " + sl2 + ", " + sl3 + "]");
        }

        // Test 3
        total++;
        boolean vp1 = exercises.isValidPassword("MyP@ss123");
        boolean vp2 = exercises.isValidPassword("short");
        boolean vp3 = exercises.isValidPassword("nouppercase1@");
        boolean vp4 = exercises.isValidPassword("NOLOWERCASE1@");
        boolean vp5 = exercises.isValidPassword("NoDigit@here");
        if (vp1 && !vp2 && !vp3 && !vp4 && !vp5) {
            System.out.println("Test 3 PASSED: isValidPassword");
            passed++;
        } else {
            System.out.println("Test 3 FAILED: isValidPassword");
        }

        // Test 4
        total++;
        int[] vc = exercises.countVowelsConsonants("Hello World");
        if (vc[0] == 3 && vc[1] == 7) {
            System.out.println("Test 4 PASSED: countVowelsConsonants");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: countVowelsConsonants - expected [3, 7], got [" + vc[0] + ", " + vc[1] + "]");
        }

        // Test 5
        total++;
        try {
            double calc1 = exercises.calculate(10, 5, '+');
            double calc2 = exercises.calculate(10, 3, '%');
            if (calc1 == 15.0 && calc2 == 1.0) {
                System.out.println("Test 5 PASSED: calculate");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: calculate");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Test 5 FAILED: calculate - " + e.getMessage());
        }

        // Test 6
        total++;
        int[][] triangle = exercises.pascalsTriangle(5);
        if (triangle.length == 5
            && triangle[0][0] == 1
            && triangle[1][0] == 1 && triangle[1][1] == 1
            && triangle[2][1] == 2
            && triangle[4][2] == 6) {
            System.out.println("Test 6 PASSED: pascalsTriangle");
            passed++;
        } else {
            System.out.println("Test 6 FAILED: pascalsTriangle");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
