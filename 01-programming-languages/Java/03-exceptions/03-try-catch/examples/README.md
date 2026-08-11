# Practice Exercises — Try-Catch Exception Handling

Work through these exercises in order. Each builds on the previous one.

---

## Exercise 1: Basic Try-Catch (Warm-up)

Write a method `divide(int a, int b)` that:
- Returns `a / b`
- Catches `ArithmeticException` and prints "Cannot divide by zero"
- Returns `-1` when the exception occurs

Test with: `divide(10, 2)`, `divide(10, 0)`, `divide(0, 5)`

---

## Exercise 2: Single Catch with String Parsing

Write a method `parseAge(String input)` that:
- Parses the input string to an integer
- Returns the parsed age
- Catches `NumberFormatException` and returns `-1`
- Returns `-1` if input is null

Test with: `"25"`, `"abc"`, `null`, `"25.5"`

---

## Exercise 3: Multiple Catch Blocks

Write a method `getArrayElement(String[] arr, String indexStr)` that:
- Parses `indexStr` to an integer index
- Returns the element at that index
- Catches `NumberFormatException` if index is not a valid number
- Catches `ArrayIndexOutOfBoundsException` if index is out of range
- Catches `NullPointerException` if arr is null
- Returns `null` for any caught exception

Test with: `{"a","b","c"}`, `"1"` → `"b"`
Test with: `{"a","b","c"}`, `"abc"` → `null`
Test with: `{"a","b","c"}`, `"10"` → `null`
Test with: `null`, `"0"` → `null`

---

## Exercise 4: Multi-catch (Java 7+)

Write a method `processInput(String input)` that:
- If input is null, throws `NullPointerException`
- If input is empty, throws `IllegalArgumentException`
- If input is not a valid integer, throws `NumberFormatException`
- If input is a valid integer, returns the integer

Use multi-catch to handle `NullPointerException | IllegalArgumentException` and return `-1`
Use a separate catch for `NumberFormatException` and return `-2`

Test with: `"42"`, `""`, `null`, `"abc"`

---

## Exercise 5: Nested try-catch

Write a method `safeDivision(String numeratorStr, String denominatorStr)` that:
- Outer try: parses both strings to integers
- Inner try: divides numerator by denominator
- Catches `NumberFormatException` in outer catch
- Catches `ArithmeticException` in inner catch
- Returns the result or prints a meaningful error message

Test with: `"10", "3"` → `3`
Test with: `"10", "0"` → error message
Test with: `"abc", "5"` → error message

---

## Exercise 6: Rethrowing Exceptions

Write a method `validateAndProcess(String input)` that:
- Tries to parse input to an integer
- If the number is negative, throws `IllegalArgumentException` with message "Negative number not allowed"
- If parsing fails, wraps the `NumberFormatException` in a custom `ValidationException` and throws it
- The custom `ValidationException` should store the original input as a field

Define the `ValidationException` class with a constructor that accepts the message and the original input.

---

## Exercise 7: Exception Chaining

Write a method `loadAndParseData(String data)` that:
- Tries to parse comma-separated values from the string
- If the string is null, catches `NullPointerException` and chains it into a `DataLoadException`
- If parsing fails, catches the exception and chains it into a `DataLoadException`
- The `DataLoadException` should preserve the root cause

Define `DataLoadException extends Exception` with a constructor that accepts message and cause.

---

## Exercise 8: Retry Pattern

Write a method `retryOperation(Callable<T> operation, int maxRetries)` that:
- Tries to execute the operation
- If it fails, catches the exception, prints the attempt number and error
- Retries up to `maxRetries` times
- Returns the result on success
- Throws the last exception if all retries fail

---

## Exercise 9: File Processing Pipeline

Write a method `processLines(String[] lines)` that:
- Iterates over each line
- For each line, tries to parse it as a pipe-delimited record (name|age)
- If parsing fails, catches the exception, logs the line number and error, and skips that line
- Returns a list of successfully parsed records

Define a `Person` record (or class) with `name` (String) and `age` (int).

Test with: `{"Alice|25", "Bob|abc", "Charlie|30", "|", "Diana|22"}`

---

## Exercise 10: Production-Style Error Handler

Write a class `ErrorHandler` with a method `handle(Exception e, String context)` that:
- Checks if the exception is one of: `IOException`, `NullPointerException`, `IllegalArgumentException`
- For `IOException`: logs "IO error in [context]: [message]"
- For `NullPointerException`: logs "Null pointer in [context]"
- For `IllegalArgumentException`: logs "Bad argument in [context]: [message]"
- For any other exception: logs "Unexpected error in [context]: [message]"
- Use multi-catch where appropriate

Write a test method that calls `handle` with different exception types and contexts.
