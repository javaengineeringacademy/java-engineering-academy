package academy.javaengineering.exceptions.questions;

/**
 * Question 10: Throwable hierarchy
 *
 * Task: Complete the method to demonstrate understanding of the Throwable hierarchy.
 * Catch the correct exception type for each scenario.
 */
public class Question10_ThrowableHierarchy {

    public static String classify(Throwable t) {
        // TODO: Return a string describing the type:
        // - If t is Error: "Error: " + t.getClass().getSimpleName()
        // - If t is RuntimeException: "Runtime: " + t.getClass().getSimpleName()
        // - If t is Exception: "Checked: " + t.getClass().getSimpleName()
        // - Otherwise: "Other Throwable: " + t.getClass().getSimpleName()
        return "Unknown";
    }

    public static void triggerError() {
        int[] arr = new int[3];
        arr[10] = 5; // ArrayIndexOutOfBoundsException
    }

    public static void triggerRuntimeException() {
        String s = null;
        s.length(); // NullPointerException
    }

    public static void triggerCheckedException() throws Exception {
        throw new Exception("Checked exception thrown");
    }

    public static void main(String[] args) {
        try {
            triggerError();
        } catch (Throwable t) {
            System.out.println("Scenario 1: " + classify(t));
        }

        try {
            triggerRuntimeException();
        } catch (Throwable t) {
            System.out.println("Scenario 2: " + classify(t));
        }

        try {
            triggerCheckedException();
        } catch (Throwable t) {
            System.out.println("Scenario 3: " + classify(t));
        }
    }
}
