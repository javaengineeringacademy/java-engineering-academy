public class FinallyDemo {

    public static void main(String[] args) {
        finallyWithException();
        finallyWithoutException();
        finallyInTryCatchFinally();
    }

    public static void finallyWithException() {
        System.out.println("=== Finally with Exception ===");
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("Finally block executed");
        }
    }

    public static void finallyWithoutException() {
        System.out.println("\n=== Finally without Exception ===");
        try {
            int result = 10 / 2;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("This won't execute");
        } finally {
            System.out.println("Finally block still executed");
        }
    }

    public static void finallyInTryCatchFinally() {
        System.out.println("\n=== Try-Catch-Finally Flow ===");
        boolean resourceReleased = false;
        try {
            String data = "Hello";
            System.out.println("Data: " + data);
        } catch (RuntimeException e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            resourceReleased = true;
            System.out.println("Resource released");
        }
        System.out.println("Resource status: " + resourceReleased);
    }

    public static int finallyWithReturn() {
        try {
            return 1;
        } finally {
            System.out.println("Finally executes before return");
        }
    }
}
