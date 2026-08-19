# 11. JVM Security - Examples

## Example Files

| File | Description |
|------|-------------|
| *(See practices/ and solutions/ for runnable examples)* | Security manager, bytecode verification, and deserialization exercises |

## Running Examples

```bash
# Run with Security Manager
java -Djava.security.manager -Djava.security.policy=policy.txt Exercise1

# Run with deserialization filter
java -Djdk.serialFilter='!academy.javaengineering.jvm.security.*' Exercise2
```

## Key Concepts Demonstrated

- Security Manager configuration
- Bytecode verification behavior
- Deserialization security
- Permission checking
