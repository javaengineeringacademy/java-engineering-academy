# Wrapper Classes - Examples

```java
import java.util.ArrayList;
import java.util.List;

public class WrapperExamples {
    public static void main(String[] args) {
        // Autoboxing: primitive → wrapper
        Integer intObj = 42;            // autoboxed
        Double doubleObj = 3.14;        // autoboxed
        Boolean boolObj = true;         // autoboxed
        Character charObj = 'A';        // autoboxed

        System.out.println("Integer: " + intObj);
        System.out.println("Double: " + doubleObj);

        // Unboxing: wrapper → primitive
        int intVal = intObj;            // unboxed
        double doubleVal = doubleObj;   // unboxed
        System.out.println("Unboxed int: " + intVal);

        // Collections require wrapper types
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);    // autoboxing
        numbers.add(20);
        int first = numbers.get(0);  // unboxing
        System.out.println("First: " + first);

        // Conversion methods
        String numStr = "123";
        int parsed = Integer.parseInt(numStr);
        Integer wrapped = Integer.valueOf(numStr);
        System.out.println("Parsed: " + parsed);
        System.out.println("Wrapped: " + wrapped);

        // Useful methods
        System.out.println("Max int: " + Integer.MAX_VALUE);
        System.out.println("Min int: " + Integer.MIN_VALUE);
        System.out.println("Binary of 10: " + Integer.toBinaryString(10));
        System.out.println("Hex of 255: " + Integer.toHexString(255));
        System.out.println("Compare: " + Integer.compare(5, 10));

        // Caching behavior
        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println("127 == 127: " + (a == b));  // true (cached)
        System.out.println("128 == 128: " + (c == d));  // false (not cached)
        System.out.println("128 equals: " + c.equals(d)); // true
    }
}
```
