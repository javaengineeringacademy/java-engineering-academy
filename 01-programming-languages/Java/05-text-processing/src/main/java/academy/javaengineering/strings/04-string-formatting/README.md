# String Formatting

## Overview
This module covers String.format() method, text blocks, and various format specifiers for creating formatted strings in Java.

## Key Concepts

### 1. String.format() Syntax
```java
String formatted = String.format("Name: %s, Age: %d", "John", 30);
```

### 2. Common Format Specifiers
- `%s` - String
- `%d` - Decimal integer
- `%f` - Floating point
- `%c` - Character
- `%b` - Boolean
- `%t` - Date/Time
- `%%` - Literal percent sign

### 3. Format Flags
- `-` - Left justify
- `0` - Zero-pad
- `,` - Include grouping separators
- `+` - Include sign for positive numbers
- `%<` - Reuse previous argument

### 4. Text Blocks (Java 13+)
```java
String json = """
        {
            "name": "John",
            "age": 30
        }
        """;
```

## Code References
- `StringFormatting.java` - Detailed examples

## Common Mistakes
1. Using wrong format specifier for data type
2. Not escaping special characters
3. Forgetting to handle locale-specific formatting
4. Not considering performance impact of String.format()

## Interview Questions
1. What are the common format specifiers in Java?
2. How do you format numbers with grouping separators?
3. What are text blocks and when would you use them?
4. How do you handle locale-specific formatting?
5. What is the performance difference between String.format() and concatenation?
