# Java Fundamentals Quiz

## Question 1 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int x = 10;
        double y = x;
        int z = (int) y;
        System.out.println(x + " " + y + " " + z);
    }
}
```

A) 10 10.0 10
B) 10 10 10
C) 10.0 10.0 10
D) Compilation error

**Answer: A**
**Explanation:** `int x = 10` stores an integer. `double y = x` performs implicit widening conversion, storing 10.0. `int z = (int) y` performs explicit narrowing cast back to 10. The output is `10 10.0 10`.

---

## Question 2 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int result = 2 + 3 * 4;
        int result2 = (2 + 3) * 4;
        System.out.println(result + " " + result2);
    }
}
```

A) 20 20
B) 14 20
C) 20 14
D) 14 14

**Answer: B**
**Explanation:** Operator precedence: multiplication (`*`) has higher precedence than addition (`+`). `2 + 3 * 4 = 2 + 12 = 14`. With parentheses: `(2 + 3) * 4 = 5 * 4 = 20`.

---

## Question 3 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        Integer c = 128;
        Integer d = 128;
        System.out.println(a == b);
        System.out.println(c == d);
    }
}
```

A) true true
B) false false
C) true false
D) false true

**Answer: C**
**Explanation:** Java caches Integer values from -128 to 127 in a cache (IntegerCache). `a` and `b` both reference the same cached object for 127, so `a == b` is true. `c` and `d` are outside the cache range, so each creates a new object on the heap, making `c == d` false.

---

## Question 4 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        Object obj = "Hello";
        String str = (String) obj;
        Integer num = (Integer) obj;
        System.out.println(str);
    }
}
```

A) Hello
B) ClassCastException at runtime
C) null
D) Compilation error

**Answer: B**
**Explanation:** `obj` is a String. The cast to `String` succeeds. However, `Integer num = (Integer) obj` attempts to cast a String to Integer, which throws a `ClassCastException` at runtime because String and Integer are unrelated types.

---

## Question 5 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        String s4 = s3.intern();

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s4);
    }
}
```

A) true false true
B) false false true
C) true true true
D) true false false

**Answer: A**
**Explanation:** `s1` and `s2` are string literals that reference the same object in the string pool, so `s1 == s2` is true. `s3` is a new String object on the heap, so `s1 == s3` is false. `s3.intern()` returns the pool reference (same as `s1`), so `s1 == s4` is true.

---

## Question 6 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        int[] arr3 = arr1;

        System.out.println(arr1 == arr2);
        System.out.println(arr1 == arr3);
        System.out.println(arr1.equals(arr2));
    }
}
```

A) false true false
B) true true true
C) false false false
D) Compilation error

**Answer: A**
**Explanation:** Arrays are objects. `arr1 == arr2` compares references — two different array objects with same content, so false. `arr1 == arr3` is true because `arr3` references the same array object as `arr1`. `arr1.equals(arr2)` calls Object.equals() which is reference equality (arrays don't override equals), so false.

---

## Question 7 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    static void display(int x, double y) {
        System.out.println("int-double: " + x + y);
    }
    static void display(double x, int y) {
        System.out.println("double-int: " + x + y);
    }

    public static void main(String[] args) {
        display(5, 10);
    }
}
```

A) int-double: 15
B) double-int: 15.0
C) Compilation error - ambiguous method call
D) int-double: 510

**Answer: A**
**Explanation:** Java resolves overloaded methods by finding the most specific applicable method. `display(5, 10)` with both int arguments matches `display(int, double)` (5 fits int, 10 widens to double) and `display(double, int)` (5 widens to double, 10 fits int). `display(int, double)` is more specific because the first parameter doesn't require widening. Output: `int-double: 510` (string concatenation).

---

## Question 8 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        Integer x = null;
        int y = x;
        System.out.println(y);
    }
}
```

A) 0
B) null
C) NullPointerException
D) Compilation error

**Answer: C**
**Explanation:** `Integer x = null` assigns null to the wrapper type. `int y = x` attempts auto-unboxing, which calls `x.intValue()`. Calling a method on null throws a `NullPointerException`. The compiler allows this assignment because auto-unboxing is a valid conversion, but it fails at runtime.

---

## Question 9 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        final int x = 10;
        int y = 20;
        Object obj = x;
        System.out.println(obj);
        // obj = y; // uncomment this line
    }
}
```

A) 10
B) Compilation error
C) 20
D) ClassCastException

**Answer: A**
**Explanation:** `final int x = 10` is a compile-time constant. `Object obj = x` performs autoboxing from `int` to `Integer` (a constant), then upcasting to `Object`. The Integer is printed as `10`. The commented line would also work via autoboxing.

---

## Question 10 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    public static void main(String[] args) {
        int a = 5;
        int b = 2;
        float c = a / b;
        float d = (float) a / b;
        System.out.println(c + " " + d);
    }
}
```

A) 2.5 2.5
B) 2.0 2.5
C) 2.5 2.0
D) 2 2.5

**Answer: B**
**Explanation:** `a / b` performs integer division (5 / 2 = 2), then assigns to float `c` as 2.0. `(float) a / b` casts `a` to float first, so `5.0 / 2` performs floating-point division, resulting in 2.5. Output: `2.0 2.5`.
