# Exception Best Practices — Quiz

Test your understanding of exception best practices with these questions.

---

**Q1. Which of the following is the preferred way to handle a missing required argument?**

A) `throw new Exception("Missing argument")`
B) `throw new IllegalArgumentException("Missing argument: " + name)`
C) `throw new RuntimeException("Missing argument")`
D) Log the error and return null

<details><summary>Answer</summary>B — IllegalArgumentException is the standard unchecked exception for invalid arguments.</details>

---

**Q2. Why should you chain exceptions?**

A) To make the stack trace shorter
B) To preserve the original cause for debugging
C) To avoid checked exception declarations
D) To prevent the exception from being caught

<details><summary>Answer</summary>B — Chaining preserves the full causal chain, making root-cause analysis possible.</details>

---

**Q3. What is wrong with this code?**

```java
try {
    processOrder(order);
} catch (Exception e) {
    // handled
}
```

A) Exception cannot be caught directly
B) The exception is swallowed — no logging, no rethrowing
C) processOrder must declare checked exceptions
D) Nothing — this is correct

<details><summary>Answer</summary>B — The exception is completely swallowed. At minimum, log it. Ideally, rethrow or chain it.</details>

---

**Q4. When is it acceptable to catch `Throwable`?**

A) In business logic as a safety net
B) In a unit test's setup method
C) At infrastructure entry points (servlets, schedulers) to prevent process crashes
D) Never

<details><summary>Answer</summary>C — Infrastructure boundaries need a safety net. Business logic should never catch Throwable.</details>

---

**Q5. Should file-not-found be a checked or unchecked exception?**

A) Unchecked — it's a programming bug
B) Checked — it's an external failure the caller can recover from
C) Either — it doesn't matter
D) Unchecked — all exceptions should be unchecked

<details><summary>Answer</summary>B — FileNotFoundException is checked because the caller can meaningfully recover (use default, prompt user, etc.).</details>

---

**Q6. What is the primary reason not to use exceptions for control flow?**

A) It's not syntactically valid
B) It's significantly slower and obscures intent
C) The compiler prevents it
D) Checked exceptions cannot be used this way

<details><summary>Answer</summary>B — Exception handling is orders of magnitude slower than a conditional check and makes code hard to follow.</details>

---

**Q7. Which practice is recommended for resource management?**

A) Manual try-finally with close()
B) Rely on garbage collection
C) try-with-resources for AutoCloseable types
D) Avoid resources that need closing

<details><summary>Answer</summary>C — try-with-resources is guaranteed correct, handles multiple resources, and is the idiomatic approach.</details>

---

**Q8. What should a well-written exception message include?**

A) The class name where the exception was thrown
B) Enough context to diagnose the problem without reading the code
C) The full stack trace as a string
D) A UUID for correlation

<details><summary>Answer</summary>B — The message should describe what went wrong, including relevant values and identifiers.</details>

---

**Q9. You have a custom exception hierarchy. Where should HTTP status codes be mapped?**

A) Inside each exception class as a field
B) At the API boundary (controller, global handler), not in business logic
C) In the service layer before throwing
D) In a static utility class called from every catch block

<details><summary>Answer</summary>B — HTTP status codes are presentation concerns. Business logic should not know about HTTP.</details>

---

**Q10. What is the purpose of a domain exception's error code field?**

A) To make exceptions serializable
B) To provide a machine-readable identifier for API responses and logging
C) To replace the exception message
D) To satisfy a compiler requirement

<details><summary>Answer</summary>B — Error codes allow API consumers and monitoring systems to programmatically identify error types.</details>
