public class CompileTimeCheck {
    public static void main(String[] args) {
        // 1. Direct assignment - compiler catches type mismatch
        String name = "Hello";
        // int number = name; // Error: incompatible types

        // 2. Method parameter type checking
        int result = add(5, 3);
        System.out.println("add(5, 3) = " + result);
        // add("5", "3"); // Error: String cannot be converted to int

        // 3. Return type checking
        String greeting = getGreeting();
        System.out.println("greeting = " + greeting);
        // int wrong = getGreeting(); // Error: incompatible types

        // 4. Diamond operator infers type
        var list = java.util.List.of(1, 2, 3);
        System.out.println("Inferred type: " + list.getClass().getSimpleName());

        System.out.println("All compile-time checks passed!");
    }

    static int add(int a, int b) {
        return a + b;
    }

    static String getGreeting() {
        return "Hello, World!";
    }
}
