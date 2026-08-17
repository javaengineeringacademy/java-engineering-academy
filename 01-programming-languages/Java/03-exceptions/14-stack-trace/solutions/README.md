# Solutions: Stack Trace in Java

These are complete solutions for all three exercises. Review your own implementation before reading these.

---

## Solution 1: Get Top Frame

```java
public class Exercise1 {
    static String topFrame(Throwable t) {
        StackTraceElement[] trace = t.getStackTrace();
        if (trace.length == 0) {
            return "(empty)";
        }
        return trace[0].getMethodName();
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Top frame: " + topFrame(ex));
    }
}
```

**Output:**
```
Top frame: main
```

**Key points:**
- `getStackTrace()[0]` is the most recent method call.
- Defensive check prevents `ArrayIndexOutOfBoundsException`.
- `getMethodName()` returns the simple method name.

---

## Solution 2: Count Frames

```java
public class Exercise2 {
    static int frameCount(Throwable t) {
        return t.getStackTrace().length;
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Frames: " + frameCount(ex));
    }
}
```

**Output:**
```
Frames: 1
```

**Key points:**
- `getStackTrace()` returns an array.
- `.length` gives the number of frames.
- Single-frame exception from `main`.

---

## Solution 3: Method at Index

```java
public class Exercise3 {
    static String methodAt(Throwable t, int index) {
        StackTraceElement[] trace = t.getStackTrace();
        if (index < 0 || index >= trace.length) {
            return "N/A";
        }
        return trace[index].getMethodName();
    }

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("test");
        System.out.println("Frame 0: " + methodAt(ex, 0));
        System.out.println("Frame 1: " + methodAt(ex, 1));
    }
}
```

**Output:**
```
Frame 0: main
Frame 1: N/A
```

**Key points:**
- Bounds check prevents `ArrayIndexOutOfBoundsException`.
- Returns a default value for invalid indices.
- Useful for safely inspecting stack traces.
