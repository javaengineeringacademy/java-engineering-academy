package academy.javaengineering.oop.methodoverloading;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public double add(double a, double b) {
        return a + b;
    }

    public int add(int a, int b, int c) {
        return a + b + c;
    }

    public String add(String a, String b) {
        return a + b;
    }

    public int add(int... numbers) {
        int sum = 0;
        for (int n : numbers) sum += n;
        return sum;
    }
}
