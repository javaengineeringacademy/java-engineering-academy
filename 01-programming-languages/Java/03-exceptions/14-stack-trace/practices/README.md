# Exercises: Stack Trace in Java

Work through these exercises in order. Each builds on the previous one. Starter code is provided; fill in the missing logic.

---

## Exercise 1: Get Top Frame

### Problem

Write a method `topFrame(Throwable t)` that returns the method name of the topmost stack frame.

### Starter Code

```java
public class Exercise1 {
    static String topFrame(Throwable t) {
        // TODO: Get stack trace and return top frame's method name
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Top frame: " + topFrame(ex));
    }
}
```

### Expected Output

```
Top frame: main
```

### Hints

1. Call `t.getStackTrace()` to get the array.
2. Return `t.getStackTrace()[0].getMethodName()`.
3. Handle empty stack trace case.

---

## Exercise 2: Count Frames

### Problem

Write a method `frameCount(Throwable t)` that returns the number of stack frames in the exception.

### Starter Code

```java
public class Exercise2 {
    static int frameCount(Throwable t) {
        // TODO: Return the length of the stack trace array
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Frames: " + frameCount(ex));
    }
}
```

### Expected Output

```
Frames: 1
```

### Hints

1. Call `t.getStackTrace()`.
2. Return `.length`.

---

## Exercise 3: Method at Index

### Problem

Write a method `methodAt(Throwable t, int index)` that returns the method name at the given stack frame index, or `"N/A"` if the index is out of bounds.

### Starter Code

```java
public class Exercise3 {
    static String methodAt(Throwable t, int index) {
        // TODO: Return method name at index, or "N/A"
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Frame 0: " + methodAt(ex, 0));
        System.out.println("Frame 1: " + methodAt(ex, 1));
    }
}
```

### Expected Output

```
Frame 0: main
Frame 1: N/A
```

### Hints

1. Get the stack trace array.
2. Check if `index` is within bounds.
3. Return the method name or `"N/A"`.
