# Quiz: Try-with-Resources

## Questions

### Q1: Which is the correct try-with-resources syntax?
**Answer:** B) `try (Resource r = new Resource()) { // body }` — Resources declared in the try parentheses are automatically closed.

### Q2: In what order are resources closed when using try-with-resources?
**Answer:** B) C, B, A — Resources are closed in reverse declaration order.

### Q3: What happens if both the try body and close() throw exceptions?
**Answer:** C) The close exception is suppressed and attached to the body exception — You can access suppressed exceptions via `getSuppressed()`.

### Q4: Which is valid Java 9+ syntax for try-with-resources?
**Answer:** D) Both A and C — Java 9 allows effectively final variables and `var` declarations.

### Q5: What happens if the first resource's close() throws?
**Answer:** B) Remaining resources are still closed — All resources are closed regardless of exceptions.

### Q6: What is the difference between AutoCloseable and Closeable?
**Answer:** B) Closeable narrows the exception to IOException — AutoCloseable throws `Exception`, while Closeable throws `IOException`.

### Q7: Can you reassign a TWR variable inside the try body?
**Answer:** B) No, it's implicitly final — Resources declared in the try parentheses are effectively final.

### Q8: Is this valid? `try { // no resources declared System.out.println("hello"); }`
**Answer:** A) Yes, but pointless — A try block without resources is valid but useless.

### Q9: To use a custom class in TWR, it must:
**Answer:** B) Implement AutoCloseable or Closeable — The class must implement the interface and provide a `close()` method.

### Q10: How do you access suppressed exceptions?
**Answer:** A) `e.getSuppressed()` — This returns an array of exceptions that were suppressed during resource closing.

### Q11: What happens when a try-with-resources block throws an exception?
**Answer:** The resource's `close()` method is called, and any exception from `close()` is added as a suppressed exception to the original.

### Q12: Why is try-with-resources preferred over manual try-finally?
**Answer:** It reduces boilerplate, ensures proper ordering of resource closure, and handles suppressed exceptions automatically.

### Q13: What is the bytecode-level difference between try-with-resources and manual try-finally?
**Answer:** The compiler generates synthetic code to manage resource closure and exception chaining, producing equivalent bytecode.

### Q14: When can you omit the parentheses in try-with-resources?
**Answer:** You cannot omit them; the resources must be declared in the try statement itself.