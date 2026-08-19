# 12. Module System - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Basic module creation |
| `Solution2.java` | Service provider interface |
| `Solution3.java` | Classpath to module migration |

## Running Solutions

```bash
javac --module-source-path src -m com.example.app
java --module-path mods -m com.example.app/com.example.app.Main
jdeps --module-source-path src com.example.app
```

## Common Mistakes to Avoid

1. **Split packages**: Same package in multiple modules is prohibited
2. **Missing opens for reflection**: Frameworks need opens for annotation processing
3. **Forgetting requires transitive**: Dependents lose access to transitive dependencies
4. **Using --add-opens excessively**: Defeats the purpose of module encapsulation
