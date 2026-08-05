# Strings

Strings are everywhere — names, addresses, messages, file paths, URLs. Java treats strings as objects, not primitives.

---

## Creating Strings

```java
// String literal (preferred for simple strings)
String name = "Pooja";

// Using new keyword (less common, but you'll see it)
String greeting = new String("Hello");
```

String literals are more efficient because Java reuses them internally. Use literals unless you have a reason not to.

---

## String Length

```java
String text = "Hello";
System.out.println(text.length());  // 5
```

Note: `length()` is a method (has parentheses), unlike arrays where `length` is a property.

---

## Accessing Characters

```java
String word = "Java";
char first = word.charAt(0);   // 'J'
char last = word.charAt(3);    // 'a'
```

Strings are indexed starting at 0, just like arrays.

---

## Immutability

Strings in Java are **immutable** — once created, they cannot be changed. Every "modification" creates a new String.

```java
String original = "Hello";
String modified = original.concat(" World");

System.out.println(original);   // "Hello" — unchanged
System.out.println(modified);   // "Hello World"
```

This is important for security and performance, but it means concatenating strings in loops is inefficient.

```java
// Bad: creates many temporary strings
String result = "";
for (int i = 0; i < 1000; i++) {
    result += i + " ";  // each += creates a new String
}

// Good: use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(" ");
}
String result = sb.toString();
```

---

## Common String Operations

### Case Conversion

```java
String text = "Hello World";
System.out.println(text.toUpperCase());  // HELLO WORLD
System.out.println(text.toLowerCase());  // hello world
```

### Trimming Whitespace

```java
String padded = "   Hello   ";
System.out.println(padded.trim());  // "Hello"
```

### Checking Content

```java
String email = "user@example.com";

System.out.println(email.isEmpty());           // false
System.out.println(email.contains("@"));       // true
System.out.println(email.startsWith("user"));  // true
System.out.println(email.endsWith(".com"));    // true
```

### Comparing Strings

```java
String a = "Hello";
String b = "Hello";
String c = "hello";

// == checks if they're the same object (usually not what you want)
System.out.println(a == b);   // true (but unreliable for strings)
System.out.println(a == c);   // false

// .equals() checks if they have the same content (what you usually want)
System.out.println(a.equals(b));      // true
System.out.println(a.equals(c));      // false
System.out.println(a.equalsIgnoreCase(c));  // true (ignores case)
```

**Rule: Always use `.equals()` to compare strings, never `==`.**

### Finding Substrings

```java
String sentence = "The quick brown fox jumps";

System.out.println(sentence.indexOf("fox"));      // 16 (position of "fox")
System.out.println(sentence.indexOf("cat"));      // -1 (not found)
System.out.println(sentence.substring(4, 9));     // "quick"
System.out.println(sentence.substring(16));       // "fox jumps"
```

### Replacing Content

```java
String text = "Hello World";
System.out.println(text.replace("World", "Java"));  // "Hello Java"
System.out.println(text.replace('l', 'L'));          // "HeLLo WorLd"
```

### Splitting and Joining

```java
// Split
String csv = "apple,banana,cherry";
String[] fruits = csv.split(",");
// fruits is {"apple", "banana", "cherry"}

// Join
String joined = String.join(" - ", fruits);
// joined is "apple - banana - cherry"
```

---

## String Formatting

```java
String name = "Pooja";
int age = 25;
double gpa = 3.85;

// String.format (like printf)
String profile = String.format("Name: %s, Age: %d, GPA: %.2f", name, age, gpa);
// "Name: Pooja, Age: 25, GPA: 3.85"

// Text blocks (Java 15+)
String html = """
        <html>
            <body>
                <p>Hello, %s</p>
            </body>
        </html>
        """.formatted(name);
```

### Format Specifiers

| Specifier | Type | Example |
|-----------|------|---------|
| `%s` | String | `"Pooja"` |
| `%d` | int/long | `25` |
| `%f` | float/double | `3.85` |
| `%.2f` | double (2 decimals) | `3.85` |
| `%b` | boolean | `true` |
| `%c` | char | `'A'` |
| `%n` | newline | (platform-specific) |

---

## StringBuilder

When you need to build strings through multiple modifications, use `StringBuilder`.

```java
StringBuilder sb = new StringBuilder();

sb.append("Hello");
sb.append(" ");
sb.append("World");

String result = sb.toString();  // "Hello World"
```

### Useful StringBuilder Methods

```java
StringBuilder sb = new StringBuilder("Hello");
sb.insert(5, " World");    // "Hello World"
sb.delete(5, 11);          // "Hello"
sb.replace(0, 5, "Hi");    // "Hi"
sb.reverse();              // "iH"
```

---

## Real-World Example: Name Formatter

```java
public class NameFormatter {
    public static String formatFullName(String firstName, String lastName) {
        String first = firstName.trim().substring(0, 1).toUpperCase()
                      + firstName.trim().substring(1).toLowerCase();
        String last = lastName.trim().substring(0, 1).toUpperCase()
                     + lastName.trim().substring(1).toLowerCase();
        return first + " " + last;
    }

    public static void main(String[] args) {
        System.out.println(formatFullName("pooja", "SHARMA"));   // Pooja Sharma
        System.out.println(formatFullName("  alice", "smith ")); // Alice Smith
    }
}
```

---

## Common Mistakes

**Using == instead of equals():**
```java
String a = new String("Hello");
String b = new String("Hello");
System.out.println(a == b);       // false (different objects)
System.out.println(a.equals(b));  // true (same content)
```

**NullPointerException on null strings:**
```java
String s = null;
// s.length();  // NullPointerException!
if (s != null && !s.isEmpty()) {
    // safe to use
}
```

**Forgetting strings are 0-indexed:**
```java
String text = "Hello";
char last = text.charAt(text.length() - 1);  // 'o', not text.charAt(5)
```

---

## Practice

1. Write a method that counts vowels in a string
2. Reverse a string without using StringBuilder.reverse()
3. Check if two strings are anagrams (same letters, different order)
4. Format a phone number from 10 digits to (XXX) XXX-XXXX

---

**Previous:** [05-Arrays](../05-arrays/)
