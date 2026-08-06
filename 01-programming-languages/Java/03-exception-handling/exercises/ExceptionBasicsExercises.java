package academy.javaengineering.exercises;

/**
 * Exercises: Exception Basics (try-catch, finally, throw, throws)
 *
 * Complete the TODO sections below.
 */
public class ExceptionBasicsExercises {

    // TODO 1: Implement safeDivide that returns Integer or null on ArithmeticException
    // Do NOT let the exception propagate
    public Integer safeDivide(int a, int b) {
        // TODO: implement this using try-catch
        return null;
    }

    // TODO 2: Implement parseAge that returns the parsed age
    // If input is null or not a valid number, throw IllegalArgumentException with a message
    public int parseAge(String ageStr) {
        // TODO: implement this
        return 0;
    }

    // TODO 3: Implement finallyBlockDemo
    // Append "opened" to the StringBuilder, then in finally append "closed"
    // If an exception occurs, append "error" before "closed"
    // Return the final string
    public String finallyBlockDemo(boolean shouldThrow) {
        StringBuilder sb = new StringBuilder();
        // TODO: implement this using try-catch-finally
        return sb.toString();
    }

    // TODO 4: Implement multiCatch that catches multiple specific exceptions
    // Return the exception type name: "NumberFormatException", "NullPointerException", or "OtherException"
    // Use a single catch block with multiple exception types
    public String multiCatch(String input, int index) {
        // TODO: implement this
        return "OtherException";
    }

    // TODO 5: Implement chainedException that creates an IOException
    // wrapping an IllegalStateException as the cause
    public Exception chainedException() {
        // TODO: implement this
        return new IOException("wrapper");
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        ExceptionBasicsExercises exercises = new ExceptionBasicsExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ExceptionBasicsExercises Tests ===\n");

        // Test 1
        total++;
        try {
            Integer result = exercises.safeDivide(10, 3);
            Integer nullResult = exercises.safeDivide(10, 0);
            if (result != null && result == 3 && nullResult == null) {
                System.out.println("Test 1 PASSED: safeDivide");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: safeDivide - expected 3 and null");
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: safeDivide - " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            int age = exercises.parseAge("25");
            if (age == 25) {
                System.out.println("Test 2 PASSED: parseAge valid input");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: parseAge - expected 25, got " + age);
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: parseAge - " + e.getMessage());
        }

        total++;
        try {
            exercises.parseAge("abc");
            System.out.println("Test 2b FAILED: parseAge should throw on invalid input");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2b PASSED: parseAge throws on invalid input");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 2b FAILED: parseAge threw wrong exception type");
        }

        total++;
        try {
            exercises.parseAge(null);
            System.out.println("Test 2c FAILED: parseAge should throw on null input");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 2c PASSED: parseAge throws on null input");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 2c FAILED: parseAge threw wrong exception type");
        }

        // Test 3
        total++;
        try {
            String noError = exercises.finallyBlockDemo(false);
            String withError = exercises.finallyBlockDemo(true);
            if ("openedclosed".equals(noError) && "openederrorclosed".equals(withError)) {
                System.out.println("Test 3 PASSED: finallyBlockDemo");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: finallyBlockDemo - got '" + noError + "' and '" + withError + "'");
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: finallyBlockDemo - " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            String result = exercises.multiCatch("abc", 0);
            if ("NumberFormatException".equals(result)) {
                System.out.println("Test 4 PASSED: multiCatch NumberFormatException");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: multiCatch - expected NumberFormatException, got " + result);
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: multiCatch - " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            Exception ex = exercises.chainedException();
            if (ex instanceof IOException
                && ex.getCause() instanceof IllegalStateException) {
                System.out.println("Test 5 PASSED: chainedException");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: chainedException - wrong exception type or cause");
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: chainedException - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
