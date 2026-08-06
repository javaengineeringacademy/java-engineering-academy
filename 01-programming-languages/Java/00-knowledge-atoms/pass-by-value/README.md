# Pass by Value in Java

## Overview

Java is **always pass by value**. This is a fundamental concept that is often misunderstood. There is no pass by reference in Java, even though it may appear so in certain cases.

---

## Java is Always Pass by Value

### What "Pass by Value" Means

When a variable is passed to a method, a **copy** of its value is made. The original variable is not modified.

```java
public class PassByValueDemo {
    public static void main(String[] args) {
        int x = 10;
        modify(x);
        System.out.println(x);  // Still 10
    }

    public static void modify(int value) {
        value = 20;  // Modifies the copy, not the original
    }
}
```

### What "Pass by Reference" Would Mean

If Java used pass by reference, the method would receive a reference to the original variable, and modifications would affect the original.

```java
// This is NOT how Java works (pseudocode):
public static void modify(int &value) {  // Pass by reference
    value = 20;  // Would modify the original
}
```

---

## Primitives: Copy of Value

When primitives are passed to methods, a copy of the value is made.

```java
public class PrimitiveDemo {
    public static void main(String[] args) {
        int a = 10;
        double b = 3.14;
        boolean c = true;

        modifyPrimitive(a, b, c);

        System.out.println(a);  // 10 (unchanged)
        System.out.println(b);  // 3.14 (unchanged)
        System.out.println(c);  // true (unchanged)
    }

    public static void modifyPrimitive(int x, double y, boolean z) {
        x = 100;
        y = 2.71;
        z = false;
    }
}
```

### Array Primitives

For arrays, the reference is passed by value, but the array contents can be modified.

```java
public class ArrayPrimitiveDemo {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};

        modifyArray(numbers);

        System.out.println(numbers[0]);  // 100 (changed!)
        System.out.println(numbers.length);  // 5 (unchanged)
    }

    public static void modifyArray(int[] arr) {
        arr[0] = 100;  // Modifies the array contents
        arr = new int[]{10, 20, 30};  // Modifies local reference only
    }
}
```

---

## Objects: Copy of Reference

When objects are passed to methods, a **copy of the reference** is made. The object itself is not copied.

```java
public class ObjectDemo {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");

        modifyObject(sb);

        System.out.println(sb);  // "Hello World" (changed!)
    }

    public static void modifyObject(StringBuilder builder) {
        builder.append(" World");  // Modifies the object
        builder = new StringBuilder("Goodbye");  // Modifies local reference only
    }
}
```

### Object Reference Diagram

```
Before method call:
  sb ──────→ StringBuilder("Hello")

Inside method (copy of reference):
  builder ──→ StringBuilder("Hello")
  sb ────────→ StringBuilder("Hello")

After builder.append(" World"):
  builder ──→ StringBuilder("Hello World")
  sb ────────→ StringBuilder("Hello World")

After builder = new StringBuilder("Goodbye"):
  builder ──→ StringBuilder("Goodbye")
  sb ────────→ StringBuilder("Hello World")
```

### Why It Appears to Pass by Reference

```java
public class SwapDemo {
    public static void main(String[] args) {
        StringBuilder a = new StringBuilder("Hello");
        StringBuilder b = new StringBuilder("World");

        swap(a, b);

        System.out.println(a);  // "Hello" (unchanged!)
        System.out.println(b);  // "World" (unchanged!)
    }

    public static void swap(StringBuilder x, StringBuilder y) {
        StringBuilder temp = x;
        x = y;
        y = temp;
        // Only local references are swapped!
    }
}
```

---

## Common Misconceptions

### Misconception 1: "Objects are passed by reference"

```java
// WRONG thinking:
public static void modify(StringBuilder s) {
    s = new StringBuilder("New");  // This doesn't change the original!
}

// RIGHT understanding:
// The reference is passed by value (copied)
// Reassigning the parameter doesn't affect the original reference
```

### Misconception 2: "Primitives are passed by value, objects by reference"

```java
// WRONG:
// - Primitives: passed by value
// - Objects: passed by reference

// RIGHT:
// - Primitives: passed by value (copy of value)
// - Objects: passed by value (copy of reference)
```

### Misconception 3: "return can modify the original"

```java
public class ReturnDemo {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Hello");
        sb = modifyAndReturn(sb);
        System.out.println(sb);  // "Hello World"
    }

    public static StringBuilder modifyAndReturn(StringBuilder s) {
        s.append(" World");
        return s;  // Returns the same reference
    }
}
```

This works because:
1. The method receives a copy of the reference
2. It modifies the object through that reference
3. It returns the same reference
4. The original reference is reassigned to point to the same object

### Misconception 4: "Arrays are special"

```java
// Arrays follow the same rules as objects
public static void main(String[] args) {
    int[] arr = {1, 2, 3};
    modifyArray(arr);
    System.out.println(arr[0]);  // 100 (modified)
}

public static void modifyArray(int[] a) {
    a[0] = 100;  // Modifies the array object
    a = new int[]{4, 5, 6};  // Modifies local reference only
}
```

---

## Practical Examples

### Example 1: Method Cannot Reassign Caller's Variable

```java
public class ReassignDemo {
    public static void main(String[] args) {
        String name = "Alice";
        changeName(name);
        System.out.println(name);  // "Alice" (unchanged)
    }

    public static void changeName(String n) {
        n = "Bob";  // Only modifies local copy of reference
    }
}
```

### Example 2: Method Can Modify Object State

```java
public class ModifyStateDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Alice");
        
        addName(list);
        System.out.println(list);  // ["Alice", "Bob"] (modified)
    }

    public static void addName(List<String> l) {
        l.add("Bob");  // Modifies the list object
    }
}
```

### Example 3: Method Can Replace Object

```java
public class ReplaceDemo {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Alice");
        
        replaceList(list);
        System.out.println(list);  // ["Alice"] (unchanged)
    }

    public static void replaceList(List<String> l) {
        l = new ArrayList<>();  // Only modifies local reference
        l.add("Bob");
    }
}
```

---

## Summary

| Scenario | What Happens | Original Modified? |
|----------|--------------|-------------------|
| Primitive parameter | Copy of value | No |
| Object parameter | Copy of reference | Yes (if method modifies object state) |
| Reassign parameter | Local reference changes | No |
| Modify object through reference | Object state changes | Yes |
| Return modified object | Same reference returned | Depends on reassignment |

### Key Takeaways

1. **Java is always pass by value** - no exceptions
2. **Primitives**: Copy of the value is passed
3. **Objects**: Copy of the reference is passed
4. **Reassigning parameter**: Doesn't affect caller's variable
5. **Modifying object state**: Does affect the original object
6. **"Pass by reference" is a myth in Java**
