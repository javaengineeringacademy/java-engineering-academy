package academy.javaengineering.testing;

/**
 * Test Fundamentals - AAA Pattern, Test Naming, Test Isolation.
 */
public class TestFundamentalsExample {

    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }

    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    public String formatName(String first, String last) {
        if (first == null || last == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        return first.trim() + " " + last.trim();
    }

    public static void main(String[] args) {
        TestFundamentalsExample example = new TestFundamentalsExample();
        System.out.println("Add: " + example.add(2, 3));
        System.out.println("Divide: " + example.divide(10, 2));
        System.out.println("Is Even: " + example.isEven(4));
        System.out.println("Format Name: " + example.formatName("John", "Doe"));
    }
}
