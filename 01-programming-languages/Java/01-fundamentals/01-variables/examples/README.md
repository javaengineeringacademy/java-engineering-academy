# Variables - Examples

## Primitive Type Examples

```java
public class PrimitiveExamples {
    public static void main(String[] args) {
        // Integer types
        byte age = 25;                    // 8-bit, -128 to 127
        short distance = 15000;           // 16-bit, -32768 to 32767
        int population = 1_400_000_000;   // 32-bit, ~±2 billion
        long planetDistance = 150_000_000L; // 64-bit

        // Floating-point types
        float price = 19.99f;             // 32-bit IEEE 754
        double pi = 3.141592653589793;    // 64-bit IEEE 754

        // Other primitives
        char grade = 'A';                 // 16-bit Unicode
        boolean isActive = true;          // true or false

        System.out.println("Age: " + age);
        System.out.println("Pi: " + pi);
        System.out.println("Grade: " + grade);
        System.out.println("Active: " + isActive);
    }
}
```

## Variable Scope Examples

```java
public class VariableScope {
    static int classVar = 10;  // Class variable (static field)

    void method() {
        int methodVar = 20;    // Method-local variable

        if (true) {
            int blockVar = 30; // Block-local variable
            System.out.println(classVar + methodVar + blockVar);
        }
        // blockVar is NOT accessible here
    }

    public static void main(String[] args) {
        new VariableScope().method();
    }
}
```

## Type Casting Examples

```java
public class TypeCasting {
    public static void main(String[] args) {
        // Widening (implicit)
        int intValue = 100;
        long longValue = intValue;      // int -> long
        double doubleValue = longValue;  // long -> double

        // Narrowing (explicit)
        double pi = 3.14;
        int truncated = (int) pi;        // 3 (precision lost)

        // Overflow example
        byte b = (byte) 128;            // -128 (wraps around)

        System.out.println("Widened: " + doubleValue);
        System.out.println("Truncated: " + truncated);
        System.out.println("Overflow: " + b);
    }
}
```

## Final Variables Examples

```java
public class FinalVariables {
    static final double PI = 3.14159;
    final String name;

    FinalVariables(String name) {
        this.name = name;  // Can assign in constructor
    }

    public static void main(String[] args) {
        final int MAX = 100;
        // MAX = 200;  // Compilation error: cannot reassign

        FinalVariables obj = new FinalVariables("Java");
        System.out.println("PI: " + PI);
        System.out.println("Name: " + obj.name);
    }
}
```
