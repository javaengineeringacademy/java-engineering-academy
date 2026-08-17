# Operators - Examples

## Arithmetic Operators

```java
public class ArithmeticOperators {
    public static void main(String[] args) {
        int a = 17, b = 5;
        System.out.println(a + " + " + b + " = " + (a + b));
        System.out.println(a + " - " + b + " = " + (a - b));
        System.out.println(a + " * " + b + " = " + (a * b));
        System.out.println(a + " / " + b + " = " + (a / b));   // 3
        System.out.println(a + " % " + b + " = " + (a % b));   // 2

        // Increment/Decrement
        int x = 10;
        System.out.println("x++ = " + x++);  // prints 10, then x=11
        System.out.println("++x = " + ++x);  // x=12, prints 12
    }
}
```

## Bitwise Operators

```java
public class BitwiseOperators {
    public static void main(String[] args) {
        int a = 0b1010;  // 10
        int b = 0b1100;  // 12

        System.out.printf("AND:  %s%n", Integer.toBinaryString(a & b));   // 1000
        System.out.printf("OR:   %s%n", Integer.toBinaryString(a | b));   // 1110
        System.out.printf("XOR:  %s%n", Integer.toBinaryString(a ^ b));   // 0110
        System.out.printf("NOT:  %s%n", Integer.toBinaryString(~a));      // ...0101
        System.out.printf("SHL:  %s%n", Integer.toBinaryString(a << 2));  // 101000
        System.out.printf("SHR:  %s%n", Integer.toBinaryString(a >> 1));  // 101
        System.out.printf("USHR: %s%n", Integer.toBinaryString(a >>> 1)); // 101
    }
}
```

## Comparison and Logical Operators

```java
public class ComparisonLogical {
    public static void main(String[] args) {
        int x = 5, y = 10;

        // Comparison
        System.out.println(x == y);  // false
        System.out.println(x != y);  // true
        System.out.println(x < y);   // true
        System.out.println(x >= 5);  // true

        // Logical
        boolean a = true, b = false;
        System.out.println(a && b);  // false
        System.out.println(a || b);  // true
        System.out.println(!a);      // false

        // Short-circuit demo
        int val = 0;
        boolean result = (val != 0) && (10 / val > 1);
        // Safe: second operand not evaluated because first is false
        System.out.println("Short-circuit safe: " + result);
    }
}
```

## Ternary Operator

```java
public class TernaryDemo {
    public static void main(String[] args) {
        int age = 20;
        String status = (age >= 18) ? "Adult" : "Minor";
        System.out.println("Status: " + status);

        // Nested ternary (use sparingly)
        int score = 85;
        String grade = (score >= 90) ? "A" :
                        (score >= 80) ? "B" :
                        (score >= 70) ? "C" : "F";
        System.out.println("Grade: " + grade);
    }
}
```
