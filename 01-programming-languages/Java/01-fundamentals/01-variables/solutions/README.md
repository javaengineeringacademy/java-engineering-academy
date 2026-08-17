# Variables - Solutions

## Solution 1: Temperature Converter

```java
public class TemperatureConverter {
    public static void main(String[] args) {
        double celsius = 37.5;
        double fahrenheit = (celsius * 9.0 / 5.0) + 32.0;

        System.out.printf("%.1f°C = %.1f°F%n", celsius, fahrenheit);

        double backToCelsius = (fahrenheit - 32.0) * 5.0 / 9.0;
        System.out.printf("%.1f°F = %.1f°C%n", fahrenheit, backToCelsius);
    }
}
```

## Solution 2: Primitive Types

```java
public class PrimitiveTypes {
    public static void main(String[] args) {
        byte count = 42;
        short year = 2026;
        int population = 1_400_000_000;
        long nationalDebt = 31_000_000_000_000L;
        float temperature = 98.6f;
        double precise = 3.141592653589793;
        char symbol = '∞';
        boolean isValid = true;

        System.out.printf("byte: %d, short: %d, int: %d%n", count, year, population);
        System.out.printf("long: %d%n", nationalDebt);
        System.out.printf("float: %.1f, double: %.15f%n", temperature, precise);
        System.out.printf("char: %c, boolean: %b%n", symbol, isValid);
    }
}
```

## Solution 3: Overflow Demo

```java
public class OverflowDemo {
    public static void main(String[] args) {
        byte b = 127;
        System.out.println("byte max: " + b);
        b++;
        System.out.println("byte overflow: " + b);  // -128

        int i = Integer.MAX_VALUE;
        System.out.println("int max: " + i);
        i++;
        System.out.println("int overflow: " + i);  // -2147483648

        long l = Long.MAX_VALUE;
        System.out.println("long max: " + l);
        l++;
        System.out.println("long overflow: " + l);  // -9223372036854775808
    }
}
```

## Solution 4: Final Variables

```java
public class MathConstants {
    static final double PI = 3.141592653589793;
    static final double E = 2.718281828459045;
    static final double GRAVITY = 9.80665;

    public static void main(String[] args) {
        double radius = 5.0;
        double area = PI * radius * radius;
        System.out.printf("Circle area (r=%.1f): %.2f%n", radius, area);

        double sphereVolume = (4.0 / 3.0) * PI * Math.pow(radius, 3);
        System.out.printf("Sphere volume: %.2f%n", sphereVolume);
    }
}
```

## Solution 5: Scope Investigation

```java
public class ScopeDemo {
    static int classVar = 1;

    public static void main(String[] args) {
        int methodVar = 2;

        {
            int blockVar = 3;
            System.out.println("Inside block: " + classVar + methodVar + blockVar);
        }

        // blockVar = 4;  // ERROR: blockVar not accessible
        methodVar = 5;    // OK: methodVar is in scope
        System.out.println("Outside block: " + classVar + methodVar);
    }
}
```

## Solution 6: var Inference

```java
import java.util.List;

public class VarInference {
    public static void main(String[] args) {
        var number = 42;           // inferred as int
        var text = "Hello";        // inferred as String
        var decimal = 3.14;        // inferred as double
        var list = List.of(1, 2, 3); // inferred as List<Integer>
        var flag = true;           // inferred as boolean

        System.out.printf("number: %d (%s)%n", number, number.getClass().getSimpleName());
        System.out.printf("text: %s (%s)%n", text, text.getClass().getSimpleName());
        System.out.printf("decimal: %.2f (%s)%n", decimal, decimal.getClass().getSimpleName());
        System.out.printf("list: %s (%s)%n", list, list.getClass().getSimpleName());
    }
}
```
