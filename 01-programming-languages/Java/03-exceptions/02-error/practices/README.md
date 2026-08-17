# Exercises: Error in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic. Do not look at the solutions until you have attempted each exercise.

---

## Exercise 1: Error vs Exception

### Problem

Write a method `classify(Throwable t)` that returns `"Error"` if the throwable is an instance of `Error`, and `"Exception"` otherwise.

### Starter Code

```java
public class Exercise1 {
    static String classify(Throwable t) {
        // TODO: Return "Error" if t is an Error, "Exception" otherwise
    }

    public static void main(String[] args) {
        System.out.println(classify(new RuntimeException("test")));
        System.out.println(classify(new StackOverflowError()));
        System.out.println(classify(new Exception("checked")));
    }
}
```

### Expected Output

```
Exception
Error
Exception
```

### Hints

1. Use `instanceof Error` to check the type.
2. Return the appropriate string based on the check.
3. Both `Error` and `Exception` extend `Throwable`.

---

## Exercise 2: Catch Specific Error

### Problem

Write a method `safeDivide(int a, int b)` that returns `a / b`. If an `ArithmeticException` occurs, return -1. If any `Error` occurs, print `"JVM error"` and return -2.

### Starter Code

```java
public class Exercise2 {
    static int safeDivide(int a, int b) {
        // TODO: Try division, catch ArithmeticException, catch Error
    }

    public static void main(String[] args) {
        System.out.println("10/2 = " + safeDivide(10, 2));
        System.out.println("10/0 = " + safeDivide(10, 0));
    }
}
```

### Expected Output

```
10/2 = 5
10/0 = -1
```

### Hints

1. Use try-catch for `ArithmeticException`.
2. Use a separate catch for `Error`.
3. Return different sentinel values for each case.

---

## Exercise 3: Stack Trace Depth

### Problem

Write a method `measureStackDepth(int maxDepth)` that recursively calls itself until `maxDepth` is reached, then returns the depth. Catch `StackOverflowError` and return the actual depth.

### Starter Code

```java
public class Exercise3 {
    static int depth = 0;

    static int measureStackDepth(int maxDepth) {
        depth = 0;
        try {
            recurse(maxDepth);
        } catch (StackOverflowError e) {
            return depth;
        }
        return depth;
    }

    static void recurse(int remaining) {
        if (remaining <= 0) return;
        depth++;
        recurse(remaining - 1);
    }

    public static void main(String[] args) {
        System.out.println("Requested depth 10: " + measureStackDepth(10));
        System.out.println("Requested depth 100000: " + measureStackDepth(100000));
    }
}
```

### Expected Output

```
Requested depth 10: 10
Requested depth 100000: ~10000
```

### Hints

1. The `StackOverflowError` is caught when the stack is exhausted.
2. The `depth` variable tracks how deep we got before overflow.
3. Small depths complete normally; large depths overflow.

---

## Exercise 4: Assertion Practice

### Problem

Write a method `validateRange(int value, int min, int max)` that uses assertions to validate that `value` is between `min` and `max`. Throw `IllegalArgumentException` if the range is invalid (min > max).

### Starter Code

```java
public class Exercise4 {
    static void validateRange(int value, int min, int max) {
        // TODO: Assert min <= max
        // TODO: Assert value >= min
        // TODO: Assert value <= max
    }

    public static void main(String[] args) {
        validateRange(5, 1, 10);
        System.out.println("5 is in range [1, 10]");
        validateRange(5, 10, 1);
    }
}
```

### Expected Output

```
5 is in range [1, 10]
Exception in thread "main" java.lang.AssertionError
```

### Hints

1. Use `assert min <= max : "Invalid range"` to validate the range.
2. Use `assert value >= min : "Value too small"` and `assert value <= max : "Value too large"`.
3. Run with `-ea` flag to enable assertions.
