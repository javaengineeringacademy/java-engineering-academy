# Keywords Internals

## How Java Uses Keywords

### Keyword Parsing

Java compiler recognizes keywords during lexical analysis:

```
Source code → Lexer → Tokens (keywords, identifiers, literals) → Parser → AST
```

### Keyword Categories

**Reserved Keywords (67):**
- Data types: `byte`, `short`, `int`, `long`, `float`, `double`, `boolean`, `char`
- Control flow: `if`, `else`, `switch`, `case`, `for`, `while`, `do`, `break`, `continue`, `return`
- Modifiers: `public`, `private`, `protected`, `static`, `final`, `abstract`, `synchronized`, `volatile`, `transient`, `native`, `strictfp`
- Class-related: `class`, `interface`, `enum`, `extends`, `implements`, `package`, `import`, `instanceof`, `new`, `this`, `super`
- Exception handling: `try`, `catch`, `finally`, `throw`, `throws`
- Reserved (unused): `const`, `goto`
- Literals: `true`, `false`, `null`
- Type inference: `var` (Java 10+)

**Contextual Keywords:**
- `module`, `requires`, `exports`, `opens`, `uses`, `providers`, `with`, `to`, `open`

### Keyword Restrictions

```java
// Cannot use keywords as identifiers:
int class = 10;      // COMPILATION ERROR
String new = "test";  // COMPILATION ERROR

// Exception: var in limited contexts (Java 10+)
// var is a restricted identifier, not a keyword
int var = 10;  // Valid (not in local variable declaration context)
```

### Module System Keywords

```java
// module-info.java
module com.company.project {
    requires java.sql;           // Dependency
    requires transitive java.logging;  // Transitive dependency
    
    exports com.company.api;     // Public API
    exports com.company.internal to com.company.tests;  // Restricted export
    
    opens com.company.model;     // Reflective access
    uses com.company.spi.Service;  // Service usage
    provides com.company.spi.Service with com.company.impl.ServiceImpl;  // Service provider
}
```

### Pattern Matching Keywords

```java
// instanceof with pattern matching (Java 16+)
if (obj instanceof String s) {
    System.out.println(s.toUpperCase());
}

// switch expressions (Java 14+)
String result = switch (status) {
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    default -> "Unknown";
};
```
