# 12. Module System - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Migrating from classpath to module path | **Must** |
| Building modular Java applications | **Must** |
| Using Java 9+ features (JShell, records, etc.) | **Should** |
| Working with frameworks that require module opens | **Should** |
| Optimizing startup with module system | **Should** |
| Simple single-module applications | **Nice to have** |

## When This Knowledge is Essential

- **New Java projects**: JPMS provides reliable configuration and strong encapsulation
- **Framework migration**: Spring Boot, Hibernate require module configuration
- **Internal API protection**: Modules hide internal APIs by default
- **Startup optimization**: Module system enables better JIT and AOT compilation
- **Dependency management**: Modules declare dependencies explicitly

## Key Decision Points

| Decision | Module Knowledge Impact |
|----------|------------------------|
| Classpath vs module path | Different visibility and encapsulation rules |
| Automatic vs named modules | Migration strategy from classpath |
| requires vs requires transitive | Dependency exposure to dependents |
| exports vs opens | Compile-time vs runtime access |
