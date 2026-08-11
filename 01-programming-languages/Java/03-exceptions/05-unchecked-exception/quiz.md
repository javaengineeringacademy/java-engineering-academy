# Quiz: Unchecked Exceptions

Test your understanding of unchecked exceptions in Java.

---

## Questions

**1.** Which of the following is an unchecked exception?

- A) `IOException`
- B) `SQLException`
- C) `NullPointerException`
- D) `ClassNotFoundException`

---

**2.** Unchecked exceptions extend which class (or its subclasses)?

- A) `Exception`
- B) `RuntimeException`
- C) `Throwable`
- D) `Error`

---

**3.** True or False: The Java compiler requires you to catch or declare
unchecked exceptions in a `throws` clause.

- A) True
- B) False

---

**4.** Which of these is a reason to use an unchecked exception?

- A) The file cannot be found on disk
- B) The database connection was refused
- C) A method received a null argument that should never be null
- D) The network connection timed out

---

**5.** What is the recommended approach when an unchecked exception occurs
that indicates a programming bug?

- A) Catch it silently and continue
- B) Log it and let it propagate, then fix the bug
- C) Wrap it in a checked exception
- D) Ignore it completely

---

**6.** Which exception is thrown when a `null` reference is dereferenced?

- A) `IllegalArgumentException`
- B) `IllegalStateException`
- C) `NullPointerException`
- D) `ClassCastException`

---

**7.** True or False: `Error` subtypes are checked exceptions.

- A) True
- B) False

---

**8.** Which of the following is an anti-pattern?

- A) Validating inputs at method boundaries with unchecked exceptions
- B) Using exceptions for control flow instead of if/else checks
- C) Creating a custom unchecked exception hierarchy for domain errors
- D) Setting a global uncaught exception handler for threads

---

**9.** What is the superclass of `IllegalArgumentException`?

- A) `Exception`
- B) `RuntimeException`
- C) `Error`
- D) `Throwable`

---

**10.** When should you catch an unchecked exception?

- A) Always, to prevent program termination
- B) Only when you can meaningfully recover from the error
- C) Never
- D) In every method that might throw one

---

## Answers

1. **C** — `NullPointerException` extends `RuntimeException` (unchecked).
2. **B** — Unchecked exceptions extend `RuntimeException` or `Error`.
3. **B** — False. The compiler does not enforce handling of unchecked exceptions.
4. **C** — A null argument is a programming error; file/network/DB failures are
   external and should use checked exceptions.
5. **B** — Log the exception, let it propagate, and fix the underlying bug.
6. **C** — `NullPointerException` is thrown when dereferencing a null reference.
7. **B** — False. `Error` subtypes are unchecked.
8. **B** — Using exceptions for control flow is slow, hides bugs, and is
   considered an anti-pattern.
9. **B** — `RuntimeException`.
10. **B** — Only catch when you have a meaningful recovery strategy; otherwise
    let it propagate and fix the bug.

---

Score: __ / 10
