package academy.javaengineering.exceptions.solutions;

/**
 * Solution 10: Throwable hierarchy
 *
 * Classify throwable types based on hierarchy position.
 */
public class Solution10_ThrowableHierarchy {

    public static String classify(Throwable t) {
        if (t instanceof Error) {
            return "Error: " + t.getClass().getSimpleName();
        } else if (t instanceof RuntimeException) {
            return "Runtime: " + t.getClass().getSimpleName();
        } else if (t instanceof Exception) {
            return "Checked: " + t.getClass().getSimpleName();
        } else {
            return "Other Throwable: " + t.getClass().getSimpleName();
        }
    }

    public static void triggerError() {
        int[] arr = new int[3];
        arr[10] = 5;
    }

    public static void triggerRuntimeException() {
        String s = null;
        s.length();
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
