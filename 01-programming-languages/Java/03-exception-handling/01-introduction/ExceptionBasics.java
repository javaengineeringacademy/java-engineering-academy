package academy.javaengineering.exceptionhandling;

public class ExceptionBasics {

    public static void main(String[] args) {
        basicExceptionDemo();
        exceptionMessageDemo();
        exceptionTypesDemo();
    }

    public static void basicExceptionDemo() {
        System.out.println("=== Basic Exception Demo ===");
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    public static void exceptionMessageDemo() {
        System.out.println("\n=== Exception Message Demo ===");
        try {
            String text = null;
            text.length();
        } catch (NullPointerException e) {
            System.out.println("Exception type: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
        }
    }

    public static void exceptionTypesDemo() {
        System.out.println("\n=== Exception Types Demo ===");
        try {
            int[] numbers = {1, 2, 3};
            System.out.println(numbers[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array error: " + e.getMessage());
        }

        try {
            String str = "abc";
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Number format error: " + e.getMessage());
        }
    }

    public static void demonstrateExceptionHierarchy() {
        System.out.println("\n=== Exception Hierarchy ===");
        Exception[] exceptions = {
            new RuntimeException("Runtime"),
            new IOException("IO"),
            new ClassNotFoundException("ClassNotFound")
        };

        for (Exception e : exceptions) {
            System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
