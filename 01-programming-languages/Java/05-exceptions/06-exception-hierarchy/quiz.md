# Quiz: Java Exception Hierarchy

## Questions

### 1. What is the base class for all exceptions and errors in Java?

- A) `Exception`
- B) `Error`
- C) `Throwable`
- D) `RuntimeException`

### 2. Which of the following is an unchecked exception?

- A) `IOException`
- B) `SQLException`
- C) `NullPointerException`
- D) `ClassNotFoundException`

### 3. Which of the following is a checked exception?

- A) `NullPointerException`
- B) `IllegalArgumentException`
- C) `FileNotFoundException`
- D) `ArithmeticException`

### 4. What is the correct order for catching exceptions?

- A) General first, then specific
- B) Specific first, then general
- C) Any order is fine
- D) Only catch the specific exception

### 5. Which of the following is an Error (not an Exception)?

- A) `IOException`
- B) `RuntimeException`
- C) `OutOfMemoryError`
- D) `FileNotFoundException`

### 6. What happens if you catch `Exception` before `IOException`?

- A) Compiler error
- B) Runtime error
- C) `IOException` handler is unreachable
- D) Both handlers are executed

### 7. What is the purpose of `Error` class?

- A) Recoverable application errors
- B) Serious JVM-level failures
- C) Programming bugs
- D) Input validation failures

### 8. Which of the following is NOT a subclass of `RuntimeException`?

- A) `NullPointerException`
- B) `IOException`
- C) `IllegalArgumentException`
- D) `ClassCastException`

### 9. What does `Throwable.getCause()` return?

- A) The exception message
- B) The original exception that caused this one
- C) The stack trace
- D) The exception type

### 10. Should you catch `Error` in application code?

- A) Yes, always
- B) Yes, but only if you can recover
- C) Generally no, unless there's a specific reason
- D) No, never

## Answers

1. C) `Throwable`
2. C) `NullPointerException`
3. C) `FileNotFoundException`
4. B) Specific first, then general
5. C) `OutOfMemoryError`
6. C) `IOException` handler is unreachable
7. B) Serious JVM-level failures
8. B) `IOException`
9. B) The original exception that caused this one
10. C) Generally no, unless there's a specific reason

## Scoring

- 9-10 correct: Excellent
- 7-8 correct: Good
- 5-6 correct: Needs review
- Below 5: Review the exception hierarchy topic
