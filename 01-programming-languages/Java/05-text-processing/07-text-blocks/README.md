# Text Blocks in Java (Java 15+)

Text blocks provide a multi-line string literal that avoids the need
for most escape sequences and automatically formats the string in a
predictable way. Introduced as a preview feature in Java 13 and
finalized in Java 15.

## What are Text Blocks?

Text blocks are string literals that can span multiple lines. They
simplify the creation of multi-line strings while maintaining
readability.

```java
// Regular string
String regular = "Line 1\nLine 2\nLine 3";

// Text block
String textBlock = """
        Line 1
        Line 2
        Line 3
        """;
```

## Syntax

### Basic Syntax

```java
String textBlock = """
        Content
        goes
        here
        """;
```

### Key Rules

- Starts with `"""` followed by a newline
- Ends with `"""`
- Indentation is stripped based on closing `"""`
- Trailing whitespace is stripped

## When to Use Text Blocks

### Good Use Cases

- **JSON/XML/HTML templates**
- **SQL queries**
- **Multi-line messages**
- **Code examples in documentation**
- **Any multi-line string content**

### Example: JSON

```java
String json = """
        {
            "name": "John",
            "age": 30,
            "city": "New York"
        }
        """;
```

### Example: SQL

```java
String sql = """
        SELECT
            id,
            name,
            email
        FROM
            users
        WHERE
            active = true
        ORDER BY
            name
        """;
```

### Example: HTML

```java
String html = """
        <html>
            <body>
                <p>Hello, World!</p>
            </body>
        </html>
        """;
```

## Line Continuation

Use backslash `\` to continue a line without adding a newline:

```java
String text = """
        This is a very long \
        line that continues \
        on the same line\
        .
        """;
// Result: "This is a very long line that continues on the same line."
```

## Indentation Stripping

Text blocks strip common leading whitespace based on the closing `"""`:

```java
String text = """
            {
                "key": "value"
            }
        """;
// The indentation is stripped:
// {
//     "key": "value"
// }
```

## Escape Sequences

Text blocks support escape sequences:

```java
// Quote (no escape needed)
String withQuotes = """
        He said "Hello" to me.
        """;

// Escaped quote
String escapedQuote = """
        He said \\"Hello\\" to me.
        """;

// Backslash
String withBackslash = """
        Path: C:\\Users\\test
        """;

// Unicode escape
String withUnicode = """
        Greek letter: \\u03B1 (alpha)
        """;
```

## Migration from Regular Strings

### Before (Regular String)

```java
String json = "{\n" +
              "    \"name\": \"John\",\n" +
              "    \"age\": 30\n" +
              "}";
```

### After (Text Block)

```java
String json = """
        {
            "name": "John",
            "age": 30
        }
        """;
```

### Benefits

- More readable
- No need for `\n` escape sequences
- No string concatenation
- Proper indentation automatically handled

## Summary

- Text blocks simplify multi-line strings
- Use `"""` to start and end
- Indentation is automatically stripped
- Backslash continues lines without newlines
- Great for JSON, SQL, HTML, and other multi-line content
- Finalized in Java 15 (preview in Java 13-14)
