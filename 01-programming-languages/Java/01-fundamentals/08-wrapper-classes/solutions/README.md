# Wrapper Classes - Solutions

```java
import java.util.List;
import java.util.ArrayList;

public class WrapperSolutions {

    // Safe conversion
    static int safeParseInt(String s, int defaultValue) {
        try {
            return (s != null) ? Integer.parseInt(s) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // List<Integer> to int[]
    static int[] toPrimitiveArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }

    // Power of 2 check
    static boolean isPowerOfTwo(int n) {
        return n > 0 && Integer.bitCount(n) == 1;
    }

    public static void main(String[] args) {
        // Safe parsing
        System.out.println(safeParseInt("42", 0));     // 42
        System.out.println(safeParseInt(null, 0));      // 0
        System.out.println(safeParseInt("abc", -1));    // -1

        // Cache demo
        Integer a = 127, b = 127;
        Integer c = 128, d = 128;
        System.out.println("127 == 127: " + (a == b));   // true
        System.out.println("128 == 128: " + (c == d));   // false
        System.out.println("128 equals: " + c.equals(d)); // true

        // Power of 2
        System.out.println("isPowerOfTwo(16): " + isPowerOfTwo(16));  // true
        System.out.println("isPowerOfTwo(10): " + isPowerOfTwo(10));  // false

        // List to array
        List<Integer> list = List.of(1, 2, 3, 4, 5);
        int[] arr = toPrimitiveArray(list);
        System.out.println("Array: " + java.util.Arrays.toString(arr));
    }
}
```
