# Exercises: finally Block in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Basic finally

### Problem

Write a method `safeDivide(int a, int b)` that prints `"Dividing"` before the division, catches `ArithmeticException` if it occurs, and always prints `"Done"` in a `finally` block.

### Starter Code

```java
public class Exercise1 {
    static int safeDivide(int a, int b) {
        // TODO: Print "Dividing", try division, catch ArithmeticException, finally print "Done"
    }

    public static void main(String[] args) {
        System.out.println("Result: " + safeDivide(10, 2));
        System.out.println("Result: " + safeDivide(10, 0));
    }
}
```

### Expected Output

```
Dividing
Done
Result: 5
Dividing
Done
Result: -1
```

### Hints

1. Print "Dividing" before the try block.
2. Return the result of `a / b` in the try block.
3. Catch `ArithmeticException` and return -1.
4. Print "Done" in the finally block.

---

## Exercise 2: finally Always Runs

### Problem

Write a method `testFinally(String action)` that demonstrates that `finally` always runs. Print `"Start"` at the beginning, perform the action (return normally, throw exception, or throw Error), and always print `"End"` in finally.

### Starter Code

```java
public class Exercise2 {
    static String testFinally(String action) {
        System.out.println("Start");
        try {
            if ("return".equals(action)) return "normal";
            if ("exception".equals(action)) throw new RuntimeException("boom");
            if ("error".equals(action)) throw new StackOverflowError();
        } catch (RuntimeException e) {
            System.out.println("Caught: " + e.getMessage());
        } finally {
            System.out.println("End");
        }
        return "fell through";
    }

    public static void main(String[] args) {
        System.out.println("Result: " + testFinally("return"));
        System.out.println("---");
        System.out.println("Result: " + testFinally("exception"));
        System.out.println("---");
        System.out.println("Result: " + testFinally("other"));
    }
}
```

### Expected Output

```
Start
End
Result: normal
---
Start
Caught: boom
End
Result: fell through
---
Start
End
Result: fell through
```

### Hints

1. "Start" prints before the try block.
2. "End" prints in finally for all three paths.
3. The "return" action returns before "End" prints (but finally still runs).
4. The "exception" action is caught and execution continues.

---

## Exercise 3: Cleanup Pattern

### Problem

Write a method `processWithCleanup(String data)` that simulates processing with guaranteed cleanup. Print `"Processing: <data>"` in try, `"Cleanup complete"` in finally, and handle any exceptions.

### Starter Code

```java
public class Exercise3 {
    static void processWithCleanup(String data) {
        // TODO: Try processing, catch RuntimeException, finally cleanup
    }

    public static void main(String[] args) {
        processWithCleanup("valid");
        processWithCleanup(null);
    }
}
```

### Expected Output

```
Processing: valid
Cleanup complete
Processing: null
Error: null data
Cleanup complete
```

### Hints

1. Print "Processing: " + data in the try block.
2. Check for null data and throw RuntimeException.
3. Print "Cleanup complete" in the finally block.
4. Catch RuntimeException and print "Error: " + e.getMessage().
