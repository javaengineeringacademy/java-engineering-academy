# Text Blocks - Decision Guide

## Use Text Blocks When

### You Have Multi-line Strings
```java
// Before
String html = "<html>\n" +
              "    <body>\n" +
              "        <p>Hello</p>\n" +
              "    </body>\n" +
              "</html>";

// After
String html = """
        <html>
            <body>
                <p>Hello</p>
            </body>
        </html>
        """;
```

### SQL Queries or Templates
```java
String sql = """
        SELECT id, name, email
        FROM users
        WHERE active = true
        ORDER BY name
        """;
```

### JSON or XML Templates
```java
String json = """
        {
            "name": "%s",
            "age": %d
        }
        """.formatted(name, age);
```

## Don't Use Text Blocks When

### Single-line Strings
Regular strings are clearer for single lines.

### Dynamic Content Only
Use `String.format()` or template strings for dynamic content.

### Very Short Strings
Regular strings are more concise for short content.

## Comparison with Alternatives

| Approach | Pros | Cons |
|----------|------|------|
| Text Blocks | Natural formatting, readable | Requires Java 15+ |
| String concatenation | Compatible | Verbose, error-prone |
| String.format | Dynamic | Less readable |
| String.join | Flexible | More complex |

## Best Practices

1. **Use consistent indentation** - Match closing `"""` to desired indentation
2. **Use `\s` for trailing spaces** - When trailing whitespace matters
3. **Use `\` for line continuation** - When you need to break long lines
4. **Combine with `formatted()`** - For dynamic content
