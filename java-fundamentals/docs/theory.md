# Sprint 1: Java Fundamentals - Theory

## 1. Java Program Structure

### What is a Java Program?
A Java program is a collection of classes. Every Java application has at least one class with a `main` method that serves as the entry point.

### Real-World Analogy
Think of a Java program like a **recipe book**:
- **Class** = A recipe (e.g., "ChocolateCake")
- **Main method** = The "Start Here" instruction
- **Methods** = Individual steps ("mixIngredients", "bake")
- **Variables** = Ingredients ("flour", "sugar", "eggs")
- **Statements** = Actions ("add 2 cups flour")

### Anatomy of a Java Program

```java
// 1. Package declaration (optional but recommended)
package com.javaacademy.basics;

// 2. Import statements (optional)
import java.util.Scanner;

// 3. Class declaration
public class HelloWorld {
    // 4. Fields (variables) - State
    private static final String GREETING = "Hello";
    
    // 5. Main method - Entry point
    public static void main(String[] args) {
        // 6. Statements - Behavior
        System.out.println(GREETING + ", World!");
    }
    
    // 7. Other methods
    private static void helper() { }
}
```

### Compilation & Execution Flow

```
┌─────────────┐    javac    ┌─────────────┐    java     ┌─────────────┐
│ HelloWorld.java │ ──────▶ │ HelloWorld.class │ ──────▶ │ JVM         │
│ (Source)       │           │ (Bytecode)      │           │ (Runtime)   │
└─────────────┘            └─────────────┘            └─────────────┘
```

1. **Source Code** (`.java`) - Human-readable
2. **Compile** (`javac`) → **Bytecode** (`.class`) - Platform-independent
3. **Execute** (`java`) → **JVM** translates bytecode to machine code

### JVM Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    JVM                                  │
├─────────────┬─────────────┬─────────────┬───────────────┤
│ Class       │ Runtime     │ Execution   │ Native        │
│ Loader      │ Data Area   │ Engine      │ Interface     │
├─────────────┼─────────────┼─────────────┼───────────────┤
│ • Bootstrap │ • Method    │ • Interpreter│ • JNI        │
│ • Extension │   Area      │ • JIT       │ • Native      │
│ • Application│ • Heap     │   Compiler  │   Libraries   │
│   ClassLoader│ • Stack   │ • GC        │               │
│             │ • PC Register│             │               │
└─────────────┴─────────────┴─────────────┴───────────────┘
```

## 2. Variables & Data Types

### Primitive Types (8 total)

| Type | Size | Default | Range | Use Case |
|------|------|---------|-------|----------|
| `byte` | 8-bit | 0 | -128 to 127 | Raw binary data |
| `short` | 16-bit | 0 | -32K to 32K | Memory-constrained |
| `int` | 32-bit | 0 | -2B to 2B | **Default integer** |
| `long` | 64-bit | 0L | -9E18 to 9E18 | Large integers |
| `float` | 32-bit | 0.0f | ~7 digits | Memory-constrained decimals |
| `double` | 64-bit | 0.0d | ~15 digits | **Default decimal** |
| `char` | 16-bit | '\u0000' | 0 to 65,535 | Single Unicode char |
| `boolean` | 1-bit* | false | true/false | Flags, conditions |

*JVM stores as 1 byte/word

### Reference Types
- **String** - Immutable sequence of characters
- **Arrays** - Fixed-size collections
- **Classes/Interfaces** - Custom types
- **Wrapper Classes** - Object versions of primitives (Integer, Double, etc.)

### Type Conversion

**Widening (Implicit - Safe):**
```
byte → short → int → long → float → double
            ↑
            └─ char
```

**Narrowing (Explicit Cast - Data Loss Possible):**
```
double → float → long → int → short → byte
```

```java
int i = 100;
double d = i;        // Implicit widening
int j = (int) d;     // Explicit narrowing
```

### Best Practices
1. **Use `int` for integers** (default, fast, sufficient)
2. **Use `double` for decimals** (default, precise)
3. **Use `boolean` for flags** (not int 0/1)
4. **Prefer primitives over wrappers** (performance)
5. **Use `var` for local variables** (Java 10+) when type is obvious

## 3. Operators

### Arithmetic Operators
| Operator | Name | Example | Result |
|----------|------|---------|--------|
| `+` | Addition | `10 + 3` | 13 |
| `-` | Subtraction | `10 - 3` | 7 |
| `*` | Multiplication | `10 * 3` | 30 |
| `/` | Division | `10 / 3` | **3** (int!) |
| `%` | Modulus/Remainder | `10 % 3` | 1 |
| `++` | Increment | `++x / x++` | Pre/Post |
| `--` | Decrement | `--x / x--` | Pre/Post |

⚠️ **Integer Division Gotcha:** `10 / 3 = 3` not `3.333`. Use `10.0 / 3` for decimals.

### Relational Operators (return boolean)
| Operator | Name | Example |
|----------|------|---------|
| `==` | Equal | `5 == 3` → false |
| `!=` | Not equal | `5 != 3` → true |
| `>` | Greater than | `5 > 3` → true |
| `<` | Less than | `5 < 3` → false |
| `>=` | Greater/equal | `5 >= 5` → true |
| `<=` | Less/equal | `5 <= 3` → false |

### Logical Operators
| Operator | Name | Short-Circuit? |
|----------|------|----------------|
| `&&` | AND | Yes (stops at first false) |
| `||` | OR | Yes (stops at first true) |
| `!` | NOT | N/A |
| `&` | Bitwise AND | No (evaluates both) |
| `|` | Bitwise OR | No (evaluates both) |
| `^` | XOR | No |

### Bitwise Operators
| Op | Name | Example |
|----|------|---------|
| `&` | AND | `5 & 3` = 1 (0101 & 0011 = 0001) |
| `|` | OR | `5 | 3` = 7 (0101 | 0011 = 0111) |
| `^` | XOR | `5 ^ 3` = 6 (0101 ^ 0011 = 0110) |
| `~` | NOT | `~5` = -6 (flips all bits) |
| `<<` | Left Shift | `5 << 1` = 10 (×2) |
| `>>` | Right Shift | `5 >> 1` = 2 (÷2, sign) |
| `>>>` | Unsigned Right | `-8 >>> 1` = large positive |

### Precedence (High to Low)
1. `[] . ()` (postfix)
2. `++ -- ! ~` (unary)
3. `* / %` (multiplicative)
4. `+ -` (additive)
5. `<< >> >>>` (shift)
6. `< > <= >=` (relational)
7. `== !=` (equality)
8. `&` (bitwise AND)
9. `^` (bitwise XOR)
10. `|` (bitwise OR)
11. `&&` (logical AND)
12. `||` (logical OR)
13. `? :` (ternary)
14. `= += -= *= ...` (assignment)

## 4. Control Flow

### if-else
```java
if (condition) {
    // true block
} else if (otherCondition) {
    // else-if
} else {
    // false block
}
```

### switch Statement (Traditional)
```java
switch (value) {
    case 1: 
        // code
        break; // Required to prevent fall-through
    case 2:
    case 3:
        // multiple cases
        break;
    default:
        // fallback
}
```

### switch Expression (Java 12+)
```java
String result = switch (value) {
    case 1, 2 -> "One or Two";  // Multiple cases, no fall-through
    case 3 -> {
        yield "Three";          // Block with yield
    }
    default -> "Other";
};
```

### Loops

**for loop:**
```java
for (int i = 0; i < 10; i++) { }
for (int i = 0, j = 10; i < j; i++, j--) { } // Multiple vars
```

**for-each (enhanced for):**
```java
for (int num : numbers) { } // No index access
```

**while loop:**
```java
while (condition) { } // Pre-test
```

**do-while loop:**
```java
do { } while (condition); // Post-test (runs at least once)
```

### Break & Continue
```java
for (int i = 0; i < 10; i++) {
    if (i == 5) break;    // Exit loop entirely
    if (i % 2 == 0) continue; // Skip to next iteration
}
```

**Labeled loops** (use sparingly):
```java
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) break outer; // Breaks OUTER loop
    }
}
```

## 5. Arrays

### Declaration & Creation
```java
int[] numbers;              // Declaration
numbers = new int[5];       // Creation: [0,0,0,0,0]
int[] scores = {90, 85, 95}; // Literal (only in declaration)
int[] copy = scores.clone(); // Copy
```

### Multi-dimensional Arrays
```java
int[][] matrix = new int[3][4]; // 3 rows, 4 cols
int[][] jagged = {{1,2}, {3,4,5}, {6}}; // Different row lengths
int[][][] cube = new int[2][3][4]; // 3D
```

### Common Operations
```java
// Length
numbers.length

// Sorting
Arrays.sort(arr);

// Searching
Arrays.binarySearch(sortedArr, key);

// Copying
Arrays.copyOf(arr, newLength);
System.arraycopy(src, srcPos, dest, destPos, length);

// Comparison
Arrays.equals(arr1, arr2);        // 1D
Arrays.deepEquals(arr1, arr2);    // Nested

// String representation
Arrays.toString(arr);       // 1D
Arrays.deepToString(arr);   // Nested
```

## 6. Strings

### Immutability
```java
String s = "Hello";
s = s + " World"; // Creates NEW string, original unchanged
```

### String Pool
```java
String a = "hello";
String b = "hello";      // Same object (pool)
String c = new String("hello"); // Different object (heap)
a == b  // true
a == c  // false
a.equals(c) // true
```

### Common Methods
| Method | Description |
|--------|-------------|
| `length()` | Character count |
| `charAt(i)` | Char at index |
| `substring(begin, end)` | Extract portion |
| `indexOf(str)` | First occurrence |
| `lastIndexOf(str)` | Last occurrence |
| `contains(str)` | Check existence |
| `startsWith/endsWith` | Prefix/suffix check |
| `equals/equalsIgnoreCase` | Content comparison |
| `compareTo` | Lexicographic comparison |
| `trim()` | Remove whitespace |
| `toUpperCase/toLowerCase` | Case conversion |
| `replace(old, new)` | Replace all |
| `split(regex)` | Split into array |
| `isEmpty/isBlank` | Empty check |

### StringBuilder (Mutable)
```java
StringBuilder sb = new StringBuilder();
sb.append("Hello").append(" ").append("World");
String result = sb.toString();
```

### Formatting
```java
String.format("Name: %s, Age: %d", "Alice", 30);
System.out.printf("Score: %.2f%n", 95.5);

// Text Blocks (Java 15+)
String html = """
    <html>
        <body>Hello</body>
    </html>
    """;
```

## 7. Methods

### Method Signature
```java
modifiers returnType methodName(parameters) throws exceptions {
    // body
}
```

### Pass-by-Value
```java
void modify(int x) { x = 20; }           // Primitive: copy of value
void modify(StringBuilder sb) { sb.append("!"); } // Reference: copy of reference
```

### Overloading
```java
int add(int a, int b) { return a + b; }
double add(double a, double b) { return a + b; }
int add(int a, int b, int c) { return a + b + c; }
int add(int... numbers) { return Arrays.stream(numbers).sum(); } // Varargs
```

### Varargs
```java
void print(int... numbers) { } // Called as print(1), print(1,2,3), print()
```

### Recursion
```java
long factorial(int n) {
    if (n <= 1) return 1;      // Base case
    return n * factorial(n-1); // Recursive case
}
```

---

## Summary: Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Java Program** | Class with `main` method, compiled to bytecode, runs on JVM |
| **Primitives** | 8 types, use `int`/`double` by default, prefer over wrappers |
| **Operators** | Know precedence, watch for integer division, use short-circuit |
| **Control Flow** | Prefer switch expressions, for-each when possible |
| **Arrays** | Fixed size, zero-based, use `Arrays` utility class |
| **Strings** | Immutable, use StringBuilder for building, String Pool for literals |
| **Methods** | Pass-by-value always, overload for convenience, recurse with base case |

---

## Interview Questions Preview

1. **Why is Java platform independent?** → Bytecode runs on JVM
2. **Difference between `==` and `.equals()`?** → Reference vs content comparison
3. **Why is String immutable?** → Security, thread-safety, caching, String Pool
4. **What is method overloading vs overriding?** → Same name different params vs same signature in subclass
5. **Explain pass-by-value in Java** → Always copies value (for primitives) or reference (for objects)
6. **What is the output of `10 / 3`?** → `3` (integer division)
7. **How does switch expression differ from statement?** → Returns value, no fall-through, exhaustive
8. **What is the maximum size of an array in Java?** → `Integer.MAX_VALUE - 5` (~2.14B elements)

---

## Further Reading

### Official Documentation
- [Java Language Specification](https://docs.oracle.com/javase/specs/jls/se21/html/index.html)
- [Java Tutorials - Oracle](https://docs.oracle.com/javase/tutorial/)
- [OpenJDK 21 Documentation](https://openjdk.org/projects/jdk/21/)

### Books
- *Effective Java* (3rd Ed.) — Joshua Bloch — Items 1-9
- *Java: The Complete Reference* — Herbert Schildt — Ch. 1-6
- *Head First Java* — Sierra & Bates — Ch. 1-5

### Articles & Blogs
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [JVM Internals](https://blog.codefx.org/java/jvm/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)