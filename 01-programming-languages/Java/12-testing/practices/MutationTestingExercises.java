package academy.javaengineering.testing.practices;

/**
 * Mutation Testing Exercises
 * Practice writing tests that catch mutations
 */
class MutationTestingExercises {

    // ============================================
    // Exercise 1: Grade Calculator
    // ============================================

    static class GradeCalculator {
        static String calculateGrade(int score) {
            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("Score must be 0-100");
            }
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        }
    }

    /*
     * TODO: Write tests that would kill these mutants:
     * 1. score >= 90 -> score > 90 (boundary mutation)
     * 2. return "A" -> return "B" (return value mutation)
     * 3. if (score >= 90) -> if (!(score >= 90)) (negate condition)
     */

    // ============================================
    // Exercise 2: Discount Calculator
    // ============================================

    static class DiscountCalculator {
        static double calculateDiscount(double price, int percentage) {
            if (price <= 0) throw new IllegalArgumentException("Invalid price");
            if (percentage < 0 || percentage > 100) throw new IllegalArgumentException("Invalid percentage");
            return price * (percentage / 100.0);
        }

        static double applyMembershipDiscount(double price, boolean isMember) {
            if (isMember) {
                return price * 0.9; // 10% discount
            }
            return price;
        }
    }

    /*
     * TODO: Write tests that catch mutations:
     * 1. price * (percentage / 100.0) -> price * (percentage / 100) (integer division)
     * 2. return price * 0.9 -> return price * 0.8 (return value)
     * 3. if (isMember) -> if (!isMember) (negate condition)
     */

    // ============================================
    // Exercise 3: String Manipulator
    // ============================================

    static class StringManipulator {
        static String capitalizeFirst(String input) {
            if (input == null || input.isEmpty()) return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }

        static String repeatString(String input, int times) {
            if (input == null || times <= 0) return "";
            return input.repeat(times);
        }

        static boolean containsWord(String text, String word) {
            if (text == null || word == null) return false;
            return text.toLowerCase().contains(word.toLowerCase());
        }
    }

    /*
     * TODO: Write tests that catch mutations:
     * 1. input.substring(0, 1) -> input.substring(0, 2) (boundary)
     * 2. return "" -> return null (return value)
     * 3. text.toLowerCase().contains -> text.contains (remove method)
     */

    // ============================================
    // Exercise 4: Number Utils
    // ============================================

    static class NumberUtils {
        static int factorial(int n) {
            if (n < 0) throw new IllegalArgumentException("Negative input");
            if (n == 0 || n == 1) return 1;
            int result = 1;
            for (int i = 2; i <= n; i++) {
                result *= i;
            }
            return result;
        }

        static boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;
            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }
    }

    /*
     * TODO: Write tests that catch mutations:
     * 1. result *= i -> result += i (arithmetic mutation)
     * 2. if (n <= 1) return false -> if (n <= 1) return true (return value)
     * 3. for (int i = 5; ...) -> for (int i = 6; ...) (increment mutation)
     */

    // ============================================
    // Exercise 5: Array Processor
    // ============================================

    static class ArrayProcessor {
        static int sum(int[] arr) {
            if (arr == null || arr.length == 0) return 0;
            int sum = 0;
            for (int num : arr) sum += num;
            return sum;
        }

        static int max(int[] arr) {
            if (arr == null || arr.length == 0) throw new IllegalArgumentException("Empty array");
            int max = arr[0];
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] > max) max = arr[i];
            }
            return max;
        }

        static int[] filterPositive(int[] arr) {
            if (arr == null) return new int[0];
            return java.util.Arrays.stream(arr).filter(n -> n > 0).toArray();
        }
    }

    /*
     * TODO: Write tests that catch mutations:
     * 1. sum += num -> sum -= num (arithmetic)
     * 2. if (arr[i] > max) -> if (arr[i] < max) (comparison)
     * 3. n > 0 -> n >= 0 (boundary)
     */

    public static void main(String[] args) {
        System.out.println("=== Mutation Testing Exercises ===");
        System.out.println("Write tests that catch mutations in the code.");
        System.out.println("Use PIT mutation testing to verify your tests.");
    }
}
