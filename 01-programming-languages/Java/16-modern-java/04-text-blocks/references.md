# Text Blocks References

## Official Documentation

- [JEP 378: Text Blocks](https://openjdk.org/jeps/378)
- [JEP 355: Text Blocks (Preview)](https://openjdk.org/jeps/355)
- [Java Language Specification - Text Blocks](https://docs.oracle.com/javase/specs/jls/se17/html/jls-3.html#jls-3.15.2)

## Key Concepts

| Concept | Description |
|---------|-------------|
| Line Terminator | Newline character (`\n`) |
| Indentation | Whitespace at start of line |
| Trailing Whitespace | Whitespace at end of line |
| Escape Sequence | Special characters like `\s`, `\` |

## Code Examples

### Basic Text Block
```java
String text = """
        Line 1
        Line 2
        Line 3
        """;
```

### With Formatting
```java
String html = """
        <html>
            <body>
                <h1>%s</h1>
                <p>%s</p>
            </body>
        </html>
        """.formatted(title, content);
```

### Trailing Whitespace
```java
String text = """
        Hello\s
        World\s
        """;
```

### Line Continuation
```java
String long = """
        This is a very long \
        string that continues \
        on the next line\
        """;
```

## Common Patterns

1. **JSON templates:** `String json = """...""";`
2. **SQL queries:** `String sql = """...""";`
3. **HTML snippets:** `String html = """...""";`
4. **Multi-line output:** `String output = """...""";`
