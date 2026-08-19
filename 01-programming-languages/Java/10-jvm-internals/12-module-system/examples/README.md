# 12. Module System - Examples

## Example Files

| File | Description |
|------|-------------|
| `ModuleSystem.java` | Demonstrates module declaration, exports, and services |

## Running Examples

```bash
# Compile with module path
javac --module-source-path src -m com.example.app

# Run with module path
java --module-path mods -m com.example.app/com.example.app.Main

# Analyze dependencies
jdeps --module-source-path src com.example.app

# Create custom runtime
jlink --module-path mods --add-modules com.example.app --output runtime
```

## Key Concepts Demonstrated

- Module declaration syntax
- Exports and opens directives
- Service provider interfaces
- Module path vs classpath
- Strong encapsulation
