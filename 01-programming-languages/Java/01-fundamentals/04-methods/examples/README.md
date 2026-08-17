# Methods - Examples

```java
public class MethodExamples {

    // Static method
    static int add(int a, int b) {
        return a + b;
    }

    // Instance method
    String greet(String name) {
        return "Hello, " + name + "!";
    }

    // Method overloading
    static double add(double a, double b) { return a + b; }
    static int add(int a, int b, int c) { return a + b + c; }

    // Varargs
    static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) total += n;
        return total;
    }

    // Recursion - factorial
    static long factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    // Recursion - Fibonacci
    static int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // Returning multiple values via array
    static int[] minMax(int[] arr) {
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (int v : arr) {
            if (v < min) min = v;
            if (v > max) max = v;
        }
        return new int[]{min, max};
    }

    public static void main(String[] args) {
        System.out.println(add(3, 4));
        System.out.println(add(3.5, 2.5));
        System.out.println(add(1, 2, 3));
        System.out.println(sum(1, 2, 3, 4, 5));
        System.out.println("5! = " + factorial(5));
        System.out.println("fib(10) = " + fibonacci(10));

        int[] result = minMax(new int[]{3, 1, 4, 1, 5, 9});
        System.out.printf("min=%d, max=%d%n", result[0], result[1]);
    }
}
```
