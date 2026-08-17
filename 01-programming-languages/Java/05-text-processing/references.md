# Text Processing - References

## Official Documentation
- [Java Tutorials: Strings](https://docs.oracle.com/javase/tutorial/java/data/strings.html)
- [Java Tutorials: Regular Expressions](https://docs.oracle.com/javase/tutorial/essential/regex/)
- [Java API: java.lang.String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)
- [Java API: java.util.regex Package](https://docs.oracle.com/javase/8/docs/api/java/util/regex/package-summary.html)
- [Text Blocks (JEP 378)](https://openjdk.org/jeps/378)

## Books
- *Mastering Regular Expressions* (Jeffrey Friedl) - Definitive regex reference
- *Effective Java* (Joshua Bloch) - Item on String usage and performance

## Key Classes

| Class | Purpose | Thread-Safe |
|-------|---------|-------------|
| String | Immutable text | Yes |
| StringBuilder | Mutable, fast | No |
| StringBuffer | Mutable, synchronized | Yes |
| Pattern | Compiled regex | Yes (stateless) |
| Matcher | Regex matching | No |
| Character | Character operations | Yes (stateless) |

## Common String Operations
| Operation | Method | Example |
|-----------|--------|---------|
| Search | `contains()` | `"hello".contains("ell")` |
| Split | `split()` | `"a,b,c".split(",")` |
| Replace | `replace()` | `"hello".replace("l", "r")` |
| Format | `String.format()` | `String.format("%s is %d", name, age)` |
| Join | `String.join()` | `String.join("-", "a", "b", "c")` |
| Strip | `strip()` | `" hi ".strip()` (Unicode-safe) |

## Regex Patterns Reference
- `\d` - Digit `[0-9]`
- `\w` - Word character `[a-zA-Z0-9_]`
- `\s` - Whitespace
- `.` - Any character
- `*`, `+`, `?` - Quantifiers (zero+, one+, zero or one)
- `{n,m}` - Range quantifier
- `^`, `$` - Anchors (start, end)
- `(...)` - Capturing group
- `(?:...)` - Non-capturing group
- `(?<name>...)` - Named capturing group

## Text Blocks Syntax
```java
String json = """
        {
            "name": "Java",
            "version": 21
        }
        """;
```
