# Text Processing - Decision Guide

## Choosing the Right Text Class

### String vs StringBuilder vs StringBuffer
- **String** - Immutable, use when text won't change; thread-safe by default
- **StringBuilder** - Mutable, single-threaded string manipulation (preferred over StringBuffer)
- **StringBuffer** - Mutable, thread-safe (synchronized), slower; rare need

### String Methods
- **Pattern matching**: `matches()`, `contains()`, `startsWith()`, `endsWith()`
- **Extraction**: `substring()`, `charAt()`, `indexOf()`
- **Transformation**: `toUpperCase()`, `toLowerCase()`, `trim()`, `strip()`
- **Splitting**: `split()` with regex support
- **Formatting**: `String.format()`, `formatted()` (Java 15+)

### Text Blocks (Java 15+)
- Use text blocks for multi-line strings: SQL, JSON, HTML, XML
- Avoids escape sequences and concatenation
- Supports `stripIndent()`, `translateEscapes()`, `formatted()`

### StringBuilder Best Practices
- Pre-allocate capacity when size is known: `new StringBuilder(initialCapacity)`
- Chain methods for readability: `sb.append("a").append("b")`
- Use `toString()` only when building is complete

### Character and Unicode
- `char` is 16-bit, represents a single UTF-16 code unit
- Use `Character.isLetter()`, `Character.isDigit()`, `Character.isWhitespace()`
- For full Unicode support (emoji, CJK), use `codePointAt()` and `Character.toCodePoint()`

### Charset and Encoding
- Always specify charset: `String.getBytes(StandardCharsets.UTF_8)`
- Never use `String.getBytes()` without charset (platform-dependent)
- Convert between charsets: `new String(bytes, StandardCharsets.UTF_8)`

### Regular Expressions
- Compile patterns once: `Pattern.compile(regex)` for reuse
- Use `Matcher.find()` for iteration, `Matcher.matches()` for full match
- Prefer named groups: `(?<name>pattern)` for readability
- Use `Pattern.quote()` for literal string matching

## Performance Tips
- Use `StringBuilder` in loops instead of string concatenation
- Pre-size `StringBuilder` to avoid resizing
- Avoid creating unnecessary String objects from byte arrays
- Use `intern()` cautiously for heavily repeated strings
- Prefer `String.join()` over manual concatenation with delimiters
