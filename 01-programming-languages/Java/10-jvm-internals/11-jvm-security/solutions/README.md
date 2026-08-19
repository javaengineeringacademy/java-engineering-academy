# 11. JVM Security - Solutions

## Solution Files

| File | Exercise |
|------|----------|
| `Solution1.java` | Security Manager policy testing |
| `Solution2.java` | Deserialization filtering |
| `Solution3.java` | Bytecode verification analysis |

## Running Solutions

```bash
java -Djava.security.manager Solution1
java -Djdk.serialFilter='!academy.javaengineering.jvm.security.*' Solution2
javap -v Solution3
```

## Common Mistakes to Avoid

1. **Running with AllPermission**: Grants unrestricted access; defeats the purpose of Security Manager
2. **Not filtering deserialization**: Exposes applications to deserialization attacks
3. **Ignoring Security Manager deprecation**: Plan migration to alternative security mechanisms
4. **Trusting bytecode from untrusted sources**: Always verify bytecode origin
