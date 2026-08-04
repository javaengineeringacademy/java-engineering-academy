package academy.javaengineering.testing;

import java.util.List;

/**
 * Unit Test Example - Pure Functions, Boundary Tests.
 */
public class UnitTestExample {

    public int factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial not defined for negative");
        }
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }

    public int fibonacci(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = a + b;
            a = b;
            b = temp;
        }
        return b;
    }

    public boolean isPrime(int number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }
        return true;
    }

    public List<Integer> filterEven(List<Integer> numbers) {
        return numbers.stream().filter(n -> n % 2 == 0).toList();
    }

    public static void main(String[] args) {
        UnitTestExample example = new UnitTestExample();
        System.out.println("Factorial(5): " + example.factorial(5));
        System.out.println("Fibonacci(7): " + example.fibonacci(7));
        System.out.println("Is Prime(13): " + example.isPrime(13));
        System.out.println("Filter Even: " + example.filterEven(List.of(1, 2, 3, 4, 5, 6)));
    }
}
