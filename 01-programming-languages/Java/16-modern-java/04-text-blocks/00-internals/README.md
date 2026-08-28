# Text Blocks Internals

## Compilation

Text blocks are compiled to regular String objects:

### String Concatenation
The compiler converts text blocks to concatenated string literals:
```java
// Your text block:
String text = """
        Hello
        World
        """;

// Compiler generates:
String text = "Hello\nWorld\n";
```

### Indentation Processing
The compiler strips leading whitespace based on the closing `"""` position.

### Escape Sequence Processing
Escape sequences like `\s` and `\` are processed during compilation.

## String Pool Behavior

Text blocks follow the same string interning rules as regular strings:
- Constant text blocks are interned
- Dynamic text blocks (with concatenation) are not

## Performance

Text blocks have the same performance as equivalent concatenated strings:
- No runtime overhead
- Same memory usage
- Same GC behavior

## Best Practices

1. **Use text blocks for readability** - They're just syntactic sugar
2. **Consider indentation carefully** - Closing `"""` position matters
3. **Use `\s` for trailing spaces** - When whitespace matters
4. **Use `\` for long lines** - When you need line continuation
