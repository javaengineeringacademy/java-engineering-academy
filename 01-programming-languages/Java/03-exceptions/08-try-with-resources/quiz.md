# Try-with-Resources Quiz

## Question 1: Basic Syntax

Which is the correct try-with-resources syntax?

A) `try { Resource r = new Resource(); } finally { r.close(); }`
B) `try (Resource r = new Resource()) { // body }`
C) `try (Resource r = new Resource()) { // body } finally { r.close(); }`
D) `try { Resource r = new Resource(); } close { r.close(); }`

## Question 2: Close Order

Given:
```java
try (A a = new A(); B b = new B(); C c = new C()) {
    // body
}
```

In what order are resources closed?

A) A, B, C
B) C, B, A
C) Random order
D) They are not closed automatically

## Question 3: Exception Handling

What happens if both the try body and close() throw exceptions?

A) Only the body exception is thrown
B) Only the close exception is thrown
C) The close exception is suppressed and attached to the body exception
D) A compile error occurs

## Question 4: Effective Final (Java 9+)

Which is valid Java 9+ syntax?

A) `Resource r = new Resource(); try (r) { }`
B) `try (r = new Resource()) { }`
C) `try (var r = new Resource()) { }`
D) Both A and C

## Question 5: Multiple Resources

What happens if the first resource's close() throws?

A) Remaining resources are not closed
B) Remaining resources are still closed
C) A compile error occurs
D) The exception is swallowed

## Question 6: Interface Difference

What is the difference between AutoCloseable and Closeable?

A) No difference
B) Closeable narrows the exception to IOException
C) AutoCloseable is for files only
D) Closeable is deprecated

## Question 7: Variable Scope

Can you reassign a TWR variable inside the try body?

A) Yes, it's a normal local variable
B) No, it's implicitly final
C) Only in Java 9+
D) Only if it's not used

## Question 8: Empty Resources

Is this valid?

```java
try {
    // no resources declared
    System.out.println("hello");
}
```

A) Yes, but pointless
B) No, compile error
C) Yes, and it's useful
D) Only in Java 11+

## Question 9: Custom Resource

To use a custom class in TWR, it must:

A) Extend Closeable
B) Implement AutoCloseable or Closeable
C) Have a public close() method
D) Both B and C

## Question 10: Suppressed Exceptions

How do you access suppressed exceptions?

A) `e.getSuppressed()`
B) `e.getStackTrace()`
C) `e.getCause()`
D) They are automatically printed

---

## Answers

1. B
2. B (reverse declaration order)
3. C
4. D (both A and C are valid)
5. B (all resources are closed)
6. B
7. B
8. A
9. B
10. A
