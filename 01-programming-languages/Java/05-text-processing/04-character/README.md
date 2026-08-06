# Character in Java

The `Character` class wraps a value of the primitive type `char` in
an object. It provides numerous static methods for performing operations
on characters and determining their properties.

## Key Characteristics

### char vs Character

- `char` is a primitive type (16 bits)
- `Character` is a wrapper class (object)
- Auto-boxing converts char to Character automatically
- Auto-unboxing converts Character to char automatically

```java
char primitive = 'A';        // Primitive
Character wrapper = 'A';     // Wrapper (auto-boxing)
char unboxed = wrapper;      // Auto-unboxing
```

### Character Cache

Java caches Character objects for values -128 to 127. This means
equal values in this range share the same object.

```java
Character a = 127;
Character b = 127;
a == b    // true (cached)

Character c = 128;
Character d = 128;
c == d    // false (not cached, different objects)
```

## Common Methods

### Type Checking

```java
char ch = 'A';

Character.isDigit(ch)        // false
Character.isLetter(ch)       // true
Character.isLetterOrDigit(ch) // true
Character.isUpperCase(ch)    // true
Character.isLowerCase(ch)    // false
Character.isWhitespace(ch)   // false
Character.isAlphabetic(ch)   // true
```

### Case Conversion

```java
char lower = 'a';
Character.toUpperCase(lower)    // 'A'

char upper = 'Z';
Character.toLowerCase(upper)    // 'z'

Character.toTitleCase('i')      // 'I' (Java 11+)
```

### Numeric Conversion

```java
char digit = '5';
int value = Character.getNumericValue(digit);  // 5

char hex = 'A';
int hexValue = Character.digit(hex, 16);       // 10

// Get character from numeric value
char ch = Character.forDigit(5, 10);           // '5'
```

### Unicode Methods

```java
char ch = 'A';
int codePoint = (int) ch;                    // 65
Character.UnicodeScript script = Character.UnicodeScript.of(codePoint);
Character.UnicodeBlock block = Character.UnicodeBlock.of(codePoint);

// Code point operations
int cp = 0x1D574; // Mathematical Double-Struck A
Character.charCount(cp);  // 2 (surrogate pair)
Character.isBmpCodePoint(cp);  // false
Character.isSupplementaryCodePoint(cp);  // true
```

### Utility Methods

```java
Character.toString('A')           // "A"
Character.compare('A', 'B')      // negative (A < B)
Character.hashCode('A')           // 65
```

## char Array Operations

```java
// String to char array
char[] chars = "Hello".toCharArray();

// Char array to String
String str = new String(chars);

// Subarray
String sub = new String(chars, 0, 3);  // "Hel"
```

## Summary

- Character is a wrapper class for char primitive
- Use static methods for character operations
- Character cache improves performance for small values
- Use isDigit, isLetter, isUpperCase for type checking
- Use toUpperCase, toLowerCase for case conversion
- Use getNumericValue for digit to int conversion
