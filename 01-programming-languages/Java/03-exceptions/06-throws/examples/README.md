# Practice Exercises: The throws Declaration

## Exercise 1: Required Declaration
Write a method `readFile(String path)` that:
1. Opens a file using `java.io.FileReader`
2. Returns the first line as a String
3. The method MUST declare `throws IOException` in its signature

---

## Exercise 2: Multiple Exceptions
Write a method `processOrder(String orderId, String paymentMethod)` that declares both `throws OrderException, PaymentException` where both are custom checked exceptions.

---

## Exercise 3: Exception Translation
Write a `UserService` class with a method `findUser(long id)` that:
1. Calls a `UserRepository.findById(id)` that throws `SQLException`
2. Catches `SQLException` and wraps it in a custom `ServiceException` (unchecked)
3. The `ServiceException` must preserve the original cause

---

## Exercise 4: Catch and Handle
Write a method `fetchData(String url)` that:
1. Tries to fetch from a primary URL
2. If it fails with `IOException`, tries a fallback URL
3. If both fail, throws a `DataUnavailableException` (custom checked exception) with both causes chained

---

## Exercise 5: throws in Interface
Define an interface `Repository<T>` with methods:
- `T findById(long id) throws DataException;`
- `void save(T entity) throws DataException;`
- `void delete(long id) throws DataException;`

Then implement a concrete class `InMemoryRepository<T>` that handles all exceptions internally (does not propagate).

---

## Exercise 6: Unchecked vs Checked Decision
Write two versions of a method `validateAndSave(String input)`:
1. Version A: throws a checked `ValidationException`
2. Version B: throws an unchecked `IllegalArgumentException`

Discuss which version is more appropriate for each scenario.

---

## Exercise 7: Exception Hierarchy
Create an exception hierarchy:
- `AppException` (unchecked, base)
- `DatabaseException extends AppException` (unchecked)
- `NetworkException extends AppException` (unchecked)

Write a method that declares `throws AppException` but throws the specific subtypes.

---

## Exercise 8: Generic Method with throws
Write a generic method `retry(Callable<T> task, int maxAttempts)` that:
1. Calls the task up to `maxAttempts` times
2. If the task throws an exception, retries up to the limit
3. After all attempts fail, throws the last exception
4. The method signature should use `throws Exception`

---

## Exercise 9: Interrupted Exception Handling
Write a method `waitAndProcess(long millis)` that:
1. Calls `Thread.sleep(millis)`
2. Catches `InterruptedException`
3. Restores the interrupt status using `Thread.currentThread().interrupt()`
4. Throws a custom `InterruptedExceptionWrapper` (unchecked) that wraps the original

---

## Exercise 10: Layered Architecture
Implement a simple 3-layer architecture:
- `Controller` layer that calls `Service`
- `Service` layer that calls `Repository`
- `Repository` layer that throws `IOException`

The `Service` layer should translate `IOException` to `ServiceException`. The `Controller` layer should handle `ServiceException` and return an error message string.

---

## Instructions
- Each exercise should be implemented in a single Java file
- Use proper Javadoc comments
- Verify that the compiler enforces the throws contracts
- Test each method with both valid and invalid inputs
