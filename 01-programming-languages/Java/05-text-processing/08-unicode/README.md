# Unicode in Java

Java has built-in support for Unicode characters, enabling the handling
of international character sets and supplementary characters beyond the
Basic Multilingual Plane (BMP).

## Unicode Basics

Unicode is a standard for encoding characters from all writing systems.
It assigns a unique number (code point) to each character.

```java
char ch = 'A';
int codePoint = (int) ch;  // 65
System.out.println("Unicode: U+" + Integer.toHexString(codePoint).toUpperCase());
```

## Code Points vs Code Units

### Code Points

Each Unicode character has a unique code point (number).

```java
String text = "Hello, 世界!";
int codePoints = text.codePointCount(0, text.length());
System.out.println("Code points: " + codePoints);

// Iterate by code points
text.codePoints().forEach(cp -> {
    System.out.print("U+" + String.format("%04X", cp) + " ");
});
```

### Code Units

UTF-16 uses code units (16-bit values). Some characters require
two code units (surrogate pairs).

```java
String text = "𝕳𝖊𝖑𝖑𝖔";  // Supplementary characters
System.out.println("Length (code units): " + text.length());
System.out.println("Code points: " +
    text.codePointCount(0, text.length()));
```

## Supplementary Characters

Characters outside the BMP (U+10000 and above) require surrogate
pairs in UTF-16 encoding.

### Creating Supplementary Characters

```java
// From code point
int codePoint = 0x1D574;  // Mathematical Double-Struck A
String str = new String(Character.toChars(codePoint));

// From string with supplementary characters
String supplementary = "𝕳𝖊𝖑𝖑𝖔";
```

### Working with Supplementary Characters

```java
String text = "𝕳𝖊𝖑𝖑𝖔";

// Code point at position
int cp = text.codePointAt(0);

// Character count
int count = Character.charCount(cp);  // 2 for supplementary

// Iterate safely
for (int i = 0; i < text.length(); ) {
    int codePoint = text.codePointAt(i);
    System.out.println("U+" + String.format("%04X", codePoint));
    i += Character.charCount(codePoint);
}
```

## Unicode Escape Sequences

Java supports Unicode escape sequences in string literals:

```java
// Unicode escape (processed at compile time)
String greek = "\u03B1";  // Greek letter alpha (α)
System.out.println(greek);

// Multiple escapes
String text = "\u0048\u0065\u006C\u006C\u006F";  // "Hello"
System.out.println(text);

// In char
char alpha = '\u03B1';
```

## Unicode String Operations

### Length vs Code Points

```java
String text = "Hello, 世界!";

// Length counts code units (char)
text.length();  // 12

// Code point count
text.codePointCount(0, text.length());  // 11
```

### Substring Operations

```java
String text = "Hello, 世界!";

// Standard substring (by code units)
text.substring(0, 5);  // "Hello"
text.substring(7, 9);  // "世界"

// Offset by code points
int offset = text.offsetByCodePoints(0, 5);
```

### Comparison and Normalization

```java
String str1 = "cafe";
String str2 = "caf\u00E9";  // é with combining accent

// May not be equal due to different representations
str1.equals(str2);  // false

// Normalize before comparison
String norm1 = java.text.Normalizer.normalize(
    str1, java.text.Normalizer.Form.NFC);
String norm2 = java.text.Normalizer.normalize(
    str2, java.text.Normalizer.Form.NFC);
norm1.equals(norm2);  // true
```

## Summary

- Java uses UTF-16 internally for strings
- Code points are unique character identifiers
- Code units are 16-bit values used in UTF-16
- Supplementary characters require surrogate pairs
- Use codePointCount() instead of length() for accurate count
- Use offsetByCodePoints() for index navigation
- Normalize strings before comparison for Unicode correctness
