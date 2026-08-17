# Methods - Solutions

## Solution 1: Overloaded display

```java
public class OverloadedDisplay {
    static void display(int value) {
        System.out.println("Integer: " + value);
    }
    static void display(String value) {
        System.out.println("String: " + value);
    }
    static void display(boolean value) {
        System.out.println("Boolean: " + value);
    }
    public static void main(String[] args) {
        display(42);
        display("hello");
        display(true);
    }
}
```

## Solution 2: Recursive Power

```java
public class RecursivePower {
    static long power(int base, int exp) {
        if (exp == 0) return 1;
        if (exp < 0) return 1 / power(base, -exp);
        return base * power(base, exp - 1);
    }
    public static void main(String[] args) {
        System.out.println("2^10 = " + power(2, 10));
        System.out.println("3^0 = " + power(3, 0));
    }
}
```

## Solution 3: Varargs Average

```java
public class VarargsAverage {
    static double average(double... values) {
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.length;
    }
    public static void main(String[] args) {
        System.out.println("Average: " + average(10, 20, 30));
        System.out.println("Average: " + average(1.5, 2.5, 3.5, 4.5));
    }
}
```
