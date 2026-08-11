# Multi-Catch Quiz

## Questions

**1.** What is the correct syntax for catching `IOException` and `SQLException` in one block?

- A) `catch (IOException, SQLException e)`
- B) `catch (IOException | SQLException e)`
- C) `catch (IOException or SQLException e)`
- D) `catch ({IOException, SQLException} e)`

**2.** Can the exception types in a multi-catch have an inheritance relationship?

- A) Yes, the child type must come first
- B) Yes, the parent type must come first
- C) No, the compiler rejects it
- D) Only if both are checked exceptions

**3.** What happens to the exception variable inside a multi-catch block?

- A) It is mutable
- B) It is effectively final
- C) It is static
- D) It has no type at runtime

**4.** What does the compiler generate for a multi-catch at the bytecode level?

- A) Multiple catch clauses
- B) A synthetic exception class
- C) An `instanceof` chain
- D) A switch statement

**5.** Which scenario is the best use case for multi-catch?

- A) Each exception needs different recovery logic
- B) Exceptions are unrelated but you want to save lines
- C) Multiple exceptions need identical handling and are semantically related
- D) You want to catch `Throwable`

**6.** Given this code, what can you do with `e` inside the catch block?

```java
catch (IOException | SQLException e) {
    // ?
}
```

- A) `e = new IOException("x");`
- B) Call `e.getMessage()`
- C) Call `e.getSQLState()` directly
- D) Both A and B

**7.** What error does this code produce?

```java
catch (IOException | Exception e) { }
```

- A) No error — this is valid
- B) `Types in multi-catch must be disjoint`
- C) `Incompatible types`
- D) `Variable 'e' is already defined`

**8.** Which statement about multi-catch performance is true?

- A) It is slower than individual catch blocks
- B) It is faster than individual catch blocks
- C) It has equivalent performance to individual catch blocks
- D) Performance depends on the number of exception types

## Answers

1. **B** — The pipe `|` operator separates exception types.
2. **C** — The compiler requires disjoint (non-inheriting) types.
3. **B** — The variable is implicitly final and cannot be reassigned.
4. **B** — The compiler creates a synthetic class extending the common superclass.
5. **C** — Identical handling of related exceptions is the ideal case.
6. **B** — You can call common methods like `getMessage()`, but not type-specific methods.
7. **B** — `Exception` is a superclass of `IOException`, so they are not disjoint.
8. **C** — The synthetic class approach yields equivalent bytecode performance.
