public class TryCatchDemo {

    public static void main(String[] args) {
        singleCatchDemo();
        multipleCatchDemo();
        multiCatchDemo();
    }

    public static void singleCatchDemo() {
        System.out.println("=== Single Catch Block ===");
        try {
            String text = "Hello";
            System.out.println(text.charAt(10));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Index error: " + e.getMessage());
        }
    }

    public static void multipleCatchDemo() {
        System.out.println("\n=== Multiple Catch Blocks ===");
        try {
            String input = "abc";
            int value = Integer.parseInt(input);
            int result = 100 / value;
            System.out.println("Result: " + result);
        } catch (NumberFormatException e) {
            System.out.println("Number format: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("General: " + e.getMessage());
        }
    }

    public static void multiCatchDemo() {
        System.out.println("\n=== Multi-Catch (Java 7+) ===");
        try {
            String data = null;
            if (data == null) {
                throw new IllegalArgumentException("Null data");
            }
            data.toString();
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
        }
    }

    public static String handleInput(String input) {
        try {
            if (input == null || input.isEmpty()) {
                throw new IllegalArgumentException("Input cannot be null or empty");
            }
            return input.toUpperCase();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
            return "DEFAULT";
        }
    }

    public static int divideSafely(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero");
            return 0;
        }
    }
}
