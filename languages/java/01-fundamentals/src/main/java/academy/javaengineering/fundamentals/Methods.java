package academy.javaengineering.fundamentals;

/**
 * Demonstrates Java methods: declaration, parameters, return types,
 * varargs, method overloading, recursion, static vs instance methods,
 * and the main method.
 *
 * <p>Methods are blocks of code that run when called. They can accept
 * parameters, return values, and help organize code into reusable units.</p>
 */
public class Methods {

    // Instance field
    private String name;
    private int callCount = 0;

    // Constructor
    public Methods(String name) {
        this.name = name;
    }

    // --- Main Method ---

    /**
     * The main method - entry point of the application.
     */
    public static void main(String[] args) {
        System.out.println("=== Methods Demo ===\n");

        demoMethodBasics();
        demoParameterTypes();
        demoReturnTypes();
        demoVarargs();
        demoMethodOverloading();
        demoRecursion();
        demoStaticVsInstance();
        demoMethodReferences();
    }

    // --- Method Basics ---

    /**
     * Demonstrates basic method declaration and invocation.
     */
    public static void demoMethodBasics() {
        System.out.println("--- Method Basics ---");

        // Calling void methods
        greet("Alice");
        greet("Bob");

        // Calling methods with return values
        int sum = add(10, 20);
        System.out.println("add(10, 20) = " + sum);

        // Method chaining with StringBuilder
        String result = new StringBuilder()
                .append("Hello")
                .append(" ")
                .append("World")
                .toString();
        System.out.println("Chained result: " + result);
        System.out.println();
    }

    /**
     * Simple void method.
     */
    public static void greet(String name) {
        System.out.println("Hello, " + name + "!");
    }

    /**
     * Method that returns a value.
     */
    public static int add(int a, int b) {
        return a + b;
    }

    // --- Parameter Types ---

    /**
     * Demonstrates different parameter passing mechanisms.
     */
    public static void demoParameterTypes() {
        System.out.println("--- Parameter Types ---");

        // Pass by value (primitives)
        int x = 10;
        System.out.println("Before modifyPrimitive: x = " + x);
        modifyPrimitive(x);
        System.out.println("After modifyPrimitive:  x = " + x);

        // Pass by value (object references)
        int[] arr = {1, 2, 3};
        System.out.println("\nBefore modifyArray: " + java.util.Arrays.toString(arr));
        modifyArray(arr);
        System.out.println("After modifyArray:  " + java.util.Arrays.toString(arr));

        // Pass by value (String - immutable)
        String str = "Hello";
        System.out.println("\nBefore modifyString: " + str);
        modifyString(str);
        System.out.println("After modifyString:  " + str);
        System.out.println();
    }

    public static void modifyPrimitive(int x) {
        x = 99; // Doesn't affect original
        System.out.println("  Inside method: x = " + x);
    }

    public static void modifyArray(int[] arr) {
        arr[0] = 99; // Affects original array contents
        System.out.println("  Inside method: " + java.util.Arrays.toString(arr));
    }

    public static void modifyString(String str) {
        str = str + " World"; // Creates new String, doesn't affect original
        System.out.println("  Inside method: " + str);
    }

    // --- Return Types ---

    /**
     * Demonstrates different return type scenarios.
     */
    public static void demoReturnTypes() {
        System.out.println("--- Return Types ---");

        // Return primitive
        int max = findMax(10, 20);
        System.out.println("findMax(10, 20) = " + max);

        // Return wrapper
        Integer nullableResult = findNullableMax(null, 20);
        System.out.println("findNullableMax(null, 20) = " + nullableResult);

        // Return array
        int[] range = getRange(1, 5);
        System.out.println("getRange(1, 5) = " + java.util.Arrays.toString(range));

        // Return multiple values via array
        int[] minMax = findMinAndMax(new int[]{3, 1, 4, 1, 5, 9, 2, 6});
        System.out.println("Min: " + minMax[0] + ", Max: " + minMax[1]);

        // Return void (no return statement needed)
        printLine(40);
        System.out.println();

        // Early return
        String validation = validateAge(15);
        System.out.println("validateAge(15) = " + validation);
        validation = validateAge(25);
        System.out.println("validateAge(25) = " + validation);
        System.out.println();
    }

    public static int findMax(int a, int b) {
        return (a > b) ? a : b;
    }

    public static Integer findNullableMax(Integer a, Integer b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a > b) ? a : b;
    }

    public static int[] getRange(int start, int end) {
        int[] range = new int[end - start + 1];
        for (int i = 0; i < range.length; i++) {
            range[i] = start + i;
        }
        return range;
    }

    public static int[] findMinAndMax(int[] arr) {
        int min = arr[0], max = arr[0];
        for (int val : arr) {
            if (val < min) min = val;
            if (val > max) max = val;
        }
        return new int[]{min, max};
    }

    public static void printLine(int length) {
        System.out.println("-".repeat(length));
    }

    public static String validateAge(int age) {
        if (age < 0) return "Invalid: negative age";
        if (age < 18) return "Minor";
        return "Adult";
    }

    // --- Varargs ---

    /**
     * Demonstrates varargs (variable number of arguments).
     */
    public static void demoVarargs() {
        System.out.println("--- Varargs ---");

        // Varargs method calls
        System.out.println("sum() = " + sum());
        System.out.println("sum(1) = " + sum(1));
        System.out.println("sum(1, 2) = " + sum(1, 2));
        System.out.println("sum(1, 2, 3, 4, 5) = " + sum(1, 2, 3, 4, 5));

        // Varargs with other parameters
        printStudentInfo("Alice", 95, 88, 92);

        // Varargs with arrays
        int[] scores = {70, 80, 90, 100};
        System.out.println("Average of scores: " + average(scores));

        // Varargs is actually an array
        System.out.println("\nVarargs is syntactic sugar for arrays");
        printArgs("a", "b", "c");
        System.out.println();
    }

    /**
     * Varargs method - accepts zero or more int arguments.
     */
    public static int sum(int... numbers) {
        int total = 0;
        for (int num : numbers) {
            total += num;
        }
        return total;
    }

    /**
     * Mixed parameters - varargs must be last.
     */
    public static void printStudentInfo(String name, int... scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        double avg = scores.length > 0 ? (double) total / scores.length : 0;
        System.out.println(name + " - Scores: " + java.util.Arrays.toString(scores) +
                " (avg: " + String.format("%.1f", avg) + ")");
    }

    public static double average(int... values) {
        if (values.length == 0) return 0;
        return (double) sum(values) / values.length;
    }

    public static void printArgs(String... args) {
        System.out.println("Number of args: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("  args[" + i + "] = " + args[i]);
        }
    }

    // --- Method Overloading ---

    /**
     * Demonstrates method overloading (same name, different parameters).
     */
    public static void demoMethodOverloading() {
        System.out.println("--- Method Overloading ---");

        // Same method name, different parameter types
        System.out.println("int result:    " + multiply(3, 4));
        System.out.println("double result: " + multiply(3.5, 2.0));
        System.out.println("String result: " + multiply("Ha", 3));

        // Overloaded constructors
        Methods obj1 = new Methods("Default");
        Methods obj2 = new Methods("Custom");
        System.out.println("obj1.name = " + obj1.getName());
        System.out.println("obj2.name = " + obj2.getName());

        // Overloaded methods with different parameter counts
        System.out.println("max(1, 2) = " + max(1, 2));
        System.out.println("max(1, 2, 3) = " + max(1, 2, 3));
        System.out.println("max(1, 2, 3, 4) = " + max(1, 2, 3, 4));
        System.out.println();
    }

    // Overloaded multiply methods
    public static int multiply(int a, int b) {
        return a * b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static String multiply(String str, int times) {
        return str.repeat(times);
    }

    // Overloaded max methods
    public static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    public static int max(int a, int b, int c) {
        return max(max(a, b), c);
    }

    public static int max(int a, int b, int c, int d) {
        return max(max(a, b), max(c, d));
    }

    // --- Recursion ---

    /**
     * Demonstrates recursive methods.
     */
    public static void demoRecursion() {
        System.out.println("--- Recursion ---");

        // Factorial
        System.out.println("Factorial of 5: " + factorial(5));
        System.out.println("Factorial of 10: " + factorial(10));

        // Fibonacci
        System.out.print("Fibonacci(10): ");
        for (int i = 0; i < 10; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        System.out.println();

        // Power
        System.out.println("power(2, 10) = " + power(2, 10));
        System.out.println("power(3, 4) = " + power(3, 4));

        // String reversal
        System.out.println("reverse(\"hello\") = " + reverse("hello"));

        // Palindrome check
        System.out.println("isPalindrome(\"racecar\") = " + isPalindrome("racecar"));
        System.out.println("isPalindrome(\"hello\") = " + isPalindrome("hello"));

        // Sum of digits
        System.out.println("sumOfDigits(12345) = " + sumOfDigits(12345));
        System.out.println();
    }

    /**
     * Recursive factorial: n! = n * (n-1)!
     */
    public static long factorial(int n) {
        if (n <= 1) return 1; // Base case
        return n * factorial(n - 1); // Recursive case
    }

    /**
     * Recursive Fibonacci: fib(n) = fib(n-1) + fib(n-2)
     */
    public static int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    /**
     * Recursive power: base^exp
     */
    public static long power(long base, int exp) {
        if (exp == 0) return 1;
        if (exp % 2 == 0) {
            long half = power(base, exp / 2);
            return half * half;
        }
        return base * power(base, exp - 1);
    }

    /**
     * Recursive string reversal.
     */
    public static String reverse(String str) {
        if (str.length() <= 1) return str;
        return reverse(str.substring(1)) + str.charAt(0);
    }

    /**
     * Recursive palindrome check.
     */
    public static boolean isPalindrome(String str) {
        if (str.length() <= 1) return true;
        if (str.charAt(0) != str.charAt(str.length() - 1)) return false;
        return isPalindrome(str.substring(1, str.length() - 1));
    }

    /**
     * Recursive sum of digits.
     */
    public static int sumOfDigits(int n) {
        n = Math.abs(n);
        if (n < 10) return n;
        return (n % 10) + sumOfDigits(n / 10);
    }

    // --- Static vs Instance Methods ---

    /**
     * Demonstrates the difference between static and instance methods.
     */
    public static void demoStaticVsInstance() {
        System.out.println("--- Static vs Instance Methods ---");

        // Static methods belong to the class
        System.out.println("Static method: Methods.add(3, 4) = " + Methods.add(3, 4));

        // Instance methods belong to an object
        Methods obj1 = new Methods("Alice");
        Methods obj2 = new Methods("Bob");

        obj1.incrementCalls();
        obj1.incrementCalls();
        obj2.incrementCalls();

        System.out.println(obj1.getName() + " calls: " + obj1.getCallCount());
        System.out.println(obj2.getName() + " calls: " + obj2.getCallCount());

        // Static factory method
        Methods created = Methods.create("Factory Created");
        System.out.println("Factory created: " + created.getName());

        // Static utility
        System.out.println("Math.sqrt(144) = " + Math.sqrt(144));
        System.out.println("Math.max(10, 20) = " + Math.max(10, 20));
        System.out.println("Integer.parseInt(\"42\") = " + Integer.parseInt("42"));
        System.out.println();
    }

    /**
     * Static factory method.
     */
    public static Methods create(String name) {
        return new Methods(name);
    }

    // Instance methods
    public String getName() {
        return name;
    }

    public int getCallCount() {
        return callCount;
    }

    public void incrementCalls() {
        callCount++;
    }

    // --- Method References ---

    /**
     * Demonstrates method references (functional interface shorthand).
     */
    public static void demoMethodReferences() {
        System.out.println("--- Method References ---");

        var names = java.util.List.of("alice", "bob", "charlie", "david");

        // Lambda expression
        System.out.print("Lambda:  ");
        names.stream()
             .map(name -> name.toUpperCase())
             .forEach(name -> System.out.print(name + " "));
        System.out.println();

        // Method reference (equivalent)
        System.out.print("Method ref: ");
        names.stream()
             .map(String::toUpperCase)
             .forEach(name -> System.out.print(name + " "));
        System.out.println();

        // Constructor reference
        var strings = java.util.List.of("1", "2", "3");
        var integers = strings.stream()
                              .map(Integer::new)
                              .toList();
        System.out.println("Constructor ref: " + integers);

        // Static method reference
        var numbers = java.util.List.of(-3, -1, 0, 2, 4);
        var absoluteValues = numbers.stream()
                                    .map(Math::abs)
                                    .toList();
        System.out.println("Static method ref (abs): " + absoluteValues);

        // Instance method reference on particular object
        String separator = ", ";
        var joined = String.join(separator, names);
        System.out.println("Joined: " + joined);
        System.out.println();
    }
}
