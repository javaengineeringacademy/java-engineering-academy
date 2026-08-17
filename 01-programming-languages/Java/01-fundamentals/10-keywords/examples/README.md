# Keywords - Examples

```java
public class KeywordsExamples {

    // final - constant
    static final double PI = 3.14159;
    final String name;

    // static - class level
    static int count = 0;

    // this - current instance
    KeywordsExamples(String name) {
        this.name = name;
        count++;
    }

    // super - parent access
    void display() {
        System.out.println("Name: " + name);
    }

    // instanceof - type checking
    static void checkType(Object obj) {
        if (obj instanceof String s) {  // pattern matching (Java 16+)
            System.out.println("String of length " + s.length());
        } else if (obj instanceof Integer i) {
            System.out.println("Integer: " + i);
        } else {
            System.out.println("Unknown type");
        }
    }

    // abstract example (separate class)
    // abstract class Animal { abstract void sound(); }

    // try-catch-finally
    static void exceptionDemo() {
        try {
            int result = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Always executes");
        }
    }

    public static void main(String[] args) {
        KeywordsExamples obj = new KeywordsExamples("Java");
        obj.display();
        System.out.println("Count: " + count);

        checkType("hello");
        checkType(42);

        exceptionDemo();

        // System.out.println(PI = 3.14);  // compilation error - final
    }
}
```
