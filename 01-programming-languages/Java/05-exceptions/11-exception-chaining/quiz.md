# Exception Chaining Quiz

## Question 1: What is exception chaining?

A) Throwing multiple exceptions in a single try block
B) Wrapping one exception inside another to preserve the cause
C) Catching exceptions and rethrowing them
D) Creating exception hierarchies

**Answer:** B

---

## Question 2: Which constructor is used for exception chaining?

A) `new Exception()`
B) `new Exception(String message)`
C) `new Exception(String message, Throwable cause)`
D) `new Exception(Throwable cause)`

**Answer:** C

---

## Question 3: What does `getCause()` return?

A) The exception message
B) The root cause exception
C) The cause exception (may be null)
D) The stack trace

**Answer:** C

---

## Question 4: What happens if you call `initCause()` twice?

A) The second call overwrites the first cause
B) The first cause is preserved and the second is ignored
C) An `IllegalStateException` is thrown
D) Nothing happens

**Answer:** C

---

## Question 5: What is the exception translation pattern?

A) Converting checked exceptions to unchecked exceptions
B) Catching a low-level exception and wrapping it in a higher-level exception
C) Throwing exceptions from a static method
D) Using `finally` blocks to clean up resources

**Answer:** B

---

## Question 6: What is the root cause of an exception?

A) The first exception in the chain
B) The last exception in the chain
C) The exception that originally triggered the chain
D) The exception with the most detailed message

**Answer:** C

---

## Question 7: How do you print the full exception chain?

A) `System.out.println(exception)`
B) `exception.toString()`
C) `exception.printStackTrace()`
D) `exception.getCause().printStackTrace()`

**Answer:** C

---

## Question 8: Which of the following is a common pitfall?

A) Using the constructor with cause parameter
B) Creating circular cause chains
C) Using `initCause()` before the exception is constructed
D) Logging the full exception chain

**Answer:** B

---

## Question 9: When should you wrap an exception?

A) When you want to hide the original exception
B) When the exception crosses a layer boundary
C) When you want to add more context
D) When you want to make the exception message longer

**Answer:** B

---

## Question 10: What is the purpose of exception chaining?

A) To make exceptions more readable
B) To preserve the causal chain of exceptions
C) To reduce the number of exceptions thrown
D) To make exception handling easier

**Answer:** B

---

## Question 11: Which exception type should be used in the service layer?

A) `SQLException`
B) `IOException`
C) `ServiceException`
D) `RuntimeException`

**Answer:** C

---

## Question 12: How do you avoid losing the root cause when wrapping exceptions?

A) Use `initCause()` after catching the exception
B) Pass the original exception as the cause when creating the new exception
C) Use `printStackTrace()` to log the original exception
D) Use `getCause()` to get the original exception

**Answer:** B

---

## Question 13: What is double wrapping?

A) Wrapping an exception in a try-finally block
B) Wrapping an exception that is already wrapped
C) Throwing two exceptions at the same time
D) Catching the same exception twice

**Answer:** B

---

## Question 14: Which method is used to traverse the cause chain?

A) `getStackTrace()`
B) `getCause()`
C) `getMessage()`
D) `getThrowable()`

**Answer:** B

---

## Question 15: What is the recommended approach for exception chaining?

A) Always use `initCause()` over constructors
B) Always use constructors over `initCause()`
C) Never use either
D) Use `initCause()` only for checked exceptions

**Answer:** B
