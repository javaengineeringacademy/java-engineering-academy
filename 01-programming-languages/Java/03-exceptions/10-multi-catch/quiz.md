# Quiz: Multi-Catch

## Questions

### Q1: What is the correct syntax for catching `IOException` and `SQLException` in one block?
**Answer:** B) `catch (IOException | SQLException e)` — The pipe `|` operator separates exception types.

### Q2: Can the exception types in a multi-catch have an inheritance relationship?
**Answer:** C) No, the compiler rejects it — The compiler requires disjoint (non-inheriting) types.

### Q3: What happens to the exception variable inside a multi-catch block?
**Answer:** B) It is effectively final — The variable cannot be reassigned.

### Q4: What does the compiler generate for a multi-catch at the bytecode level?
**Answer:** B) A synthetic exception class — The compiler creates a synthetic class extending the common superclass.

### Q5: Which scenario is the best use case for multi-catch?
**Answer:** C) Multiple exceptions need identical handling and are semantically related — Identical handling of related exceptions is the ideal case.

### Q6: Given this code, what can you do with `e` inside the catch block?
```java
catch (IOException | SQLException e) {
    // ?
}
```
**Answer:** B) Call `e.getMessage()` — You can call common methods like `getMessage()`, but not type-specific methods.

### Q7: What error does this code produce?
```java
catch (IOException | Exception e) { }
```
**Answer:** B) `Types in multi-catch must be disjoint` — `Exception` is a superclass of `IOException`, so they are not disjoint.

### Q8: Which statement about multi-catch performance is true?
**Answer:** C) It has equivalent performance to individual catch blocks — The synthetic class approach yields equivalent bytecode performance.

### Q9: Why was multi-catch introduced in Java 7?
**Answer:** To reduce boilerplate when handling multiple unrelated exceptions with the same logic.

### Q10: What happens if you try to assign to the exception variable in a multi-catch?
**Answer:** The compiler throws an error because the variable is implicitly final.

### Q11: What is the benefit of using multi-catch over separate catch blocks?
**Answer:** It reduces code duplication and improves readability when handling multiple exceptions identically.

### Q12: Can you use multi-catch with checked exceptions?
**Answer:** Yes, as long as the types are disjoint (not related by inheritance).

### Q13: What is the bytecode-level difference between multi-catch and separate catch blocks?
**Answer:** They produce equivalent bytecode; the compiler generates a synthetic exception class for multi-catch.

### Q14: What is the risk of using multi-catch with too many exception types?
**Answer:** It can obscure the specific error conditions being handled, making debugging harder.