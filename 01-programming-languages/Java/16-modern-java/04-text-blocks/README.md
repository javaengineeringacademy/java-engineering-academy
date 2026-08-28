# Text Blocks (Java 15)

Text Blocks provide a multi-line string literal that avoids the need for most escape sequences and automatically formats the string in a predictable way.

## Key Features

- **Multi-line strings** - Span multiple lines naturally
- **Automatic formatting** - Indentation is preserved
- **Escape sequences** - Still supported
- **No concatenation** - No need for `+` operator

## Syntax

```java
String text = """
        This is a
        multi-line
        string
        """;
```

## Features

| Feature | Description |
|---------|-------------|
| Line terminators | Automatically included |
| Indentation | Stripped based on closing `"""` |
| `\s` | Preserves trailing whitespace |
| `\` | Line terminator escape (continuation) |
| `"""` | Closing delimiter (must be on own line) |

## Examples

### Basic Text Block
```java
String json = """
        {
            "name": "John",
            "age": 30
        }
        """;
```

### With Escape Sequences
```java
String html = """
        <html>
            <body>
                <p>Hello, World!</p>
            </body>
        </html>
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

## When to Use

- SQL queries
- JSON/XML templates
- HTML snippets
- Multi-line output
- Any string that needs natural formatting
