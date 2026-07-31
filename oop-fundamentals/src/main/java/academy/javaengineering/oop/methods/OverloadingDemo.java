package academy.javaengineering.oop.methods;

/**
 * Demonstrates method overloading.
 */
public final class OverloadingDemo {

    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public int add(int... numbers) {
        return java.util.Arrays.stream(numbers).sum();
    }

    public static void main(String[] args) {
        OverloadingDemo demo = new OverloadingDemo();
        System.out.println("add(10, 20) = " + demo.add(10, 20));
        System.out.println("add(10.5, 20.3) = " + demo.add(10.5, 20.3));
        System.out.println("add(1, 2, 3) = " + demo.add(1, 2, 3));
        System.out.println("add(1,2,3,4,5) = " + demo.add(1, 2, 3, 4, 5));
    }
}