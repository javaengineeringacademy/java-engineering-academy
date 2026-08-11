# 03 - Try-Catch Exception Handling (Part 4)

[← Part 3](README-Part3.md)

---


### Example 1: File Processing Pipeline

```java
public List<Record> processFile(String path) {
    List<Record> records = new ArrayList<>();
    
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                Record record = parseRecord(line);
                records.add(record);
            } catch (ParseException e) {
                logger.warn("Skipping invalid line {}: {}", line, e.getMessage());
                // continue processing other lines
            }
        }
    } catch (FileNotFoundException e) {
        logger.error("Input file not found: {}", path, e);
        throw new ProcessingException("Cannot process: file not found", e);
    } catch (IOException e) {
        logger.error("I/O error reading file: {}", path, e);
        throw new ProcessingException("Cannot process: read error", e);
    }
    
    return records;
}
```

**Design decisions:**
- Inner catch handles per-line errors (skip bad lines, continue)
- Outer catch handles file-level errors (cannot proceed)
- try-with-resources ensures the reader closes
- Checked exceptions wrapped in unchecked for caller convenience

### Example 2: Database Retry Logic

```java
public User findUser(int id) {
    int attempts = 0;
    int maxAttempts = 3;
    
    while (attempts < maxAttempts) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                new UserRowMapper(),
                id
            );
        } catch (DataAccessException e) {
            attempts++;
            logger.warn("Database query attempt {} failed: {}", attempts, e.getMessage());
            
            if (attempts >= maxAttempts) {
                logger.error("All {} database attempts failed for user {}", maxAttempts, id, e);
                throw new ServiceException("Could not retrieve user after " + maxAttempts + " attempts", e);
            }
            
            try {
                Thread.sleep(1000L * attempts);  // linear backoff
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new ServiceException("Retry interrupted", ie);
            }
        }
    }
    
    throw new ServiceException("Unreachable code");
}
```

**Design decisions:**
- Retries transient database failures
- Linear backoff between attempts
- Preserves interrupt status on `InterruptedException`
- Wraps final failure in a service-level exception
- The `Thread.sleep` in the retry path is caught separately

### Example 3: API Client with Fallback

```java
public WeatherData getWeather(String city) {
    try {
        HttpResponse response = httpClient.get("https://api.weather.com/" + city);
        return parseWeatherResponse(response);
    } catch (ConnectTimeoutException e) {
        logger.warn("Weather API timeout for {}: {}", city, e.getMessage());
        return cachedWeatherData.getOrDefault(city, WeatherData.UNKNOWN);
    } catch (JsonParseException e) {
        logger.error("Invalid response from weather API for {}", city, e);
        return WeatherData.UNKNOWN;
    } catch (IOException e) {
        logger.warn("Weather API unavailable: {}", e.getMessage());
        return cachedWeatherData.getOrDefault(city, WeatherData.UNKNOWN);
    }
}
```

**Design decisions:**
- Different catch blocks for different failure modes
- Timeout: return cached data
- Bad response: return unknown (not cached)
- Network failure: return cached data
- No retry here — weather data is non-critical

### Example 4: Data Validation with Aggregated Errors

```java
public ValidationResult validateOrder(Order order) {
    List<String> errors = new ArrayList<>();
    
    try {
        validateCustomer(order.getCustomer());
    } catch (ValidationException e) {
        errors.add("Customer: " + e.getMessage());
    }
    
    try {
        validateItems(order.getItems());
    } catch (ValidationException e) {
        errors.add("Items: " + e.getMessage());
    }
    
    try {
        validateShipping(order.getShippingAddress());
    } catch (ValidationException e) {
        errors.add("Shipping: " + e.getMessage());
    }
    
    if (!errors.isEmpty()) {
        return ValidationResult.invalid(errors);
    }
    return ValidationResult.valid();
}
```

**Design decisions:**
- Each validation step is independent
- All errors collected before returning
- No early exit — user sees all problems at once
- `ValidationException` is a custom checked exception

---

## 7. Version History

| Java Version | Feature | Details |
|-------------|---------|---------|
| 1.0 | try-catch-finally | Basic exception handling |
| 1.2 | Chained exceptions | `initCause()` method added |
| 1.4 | Assertions | Separate from exception handling |
| 5.0 | Enhanced for loop | No direct exception impact |
| 7.0 | Multi-catch | `catch (A \| B \| C e)` syntax |
| 7.0 | try-with-resources | Automatic `AutoCloseable` management |
| 7.0 | Rethrow analysis | Compiler infers rethrown exception types |
| 8.0 | Lambda expressions | Effectively final variables in catch |
| 9.0 | Private interface methods | No exception handling changes |
| 11.0 | Var in lambda | No exception handling changes |
| 17.0 | Sealed classes | Can be used with pattern matching in catch |
| 21.0 | Pattern matching for switch | Alternative to multi-catch in some cases |

---

## 8. Summary

```
+---------------------------------------------------------------+
|              Try-Catch Exception Handling — Part 2             |
+---------------------------------------------------------------+
|                                                                |
|  Multi-catch          Catch multiple types in one block       |
|    - No inheritance     Types cannot be parent/child          |
|    - Implicit final     Parameter cannot be reassigned         |
|    - Bytecode savings   One handler instead of many            |
|                                                                |
|  Nested try-catch     Inner blocks handle locally             |
|    - Propagation       Unhandled goes to outer                 |
|    - Refactor          Avoid deep nesting, extract methods    |
|                                                                |
|  Rethrowing           Catch, then throw again                 |
|    - throw e           Same exception, same type               |
|    - throw new X(e)    Wrap with context, preserve cause       |
|    - Java 7+           Compiler analyzes rethrown types        |
|                                                                |
|  Common Mistakes      What to avoid                            |
|    - Empty catch       Silent failure                          |
|    - Catch Exception   Overly broad, loses specificity         |
|    - Swallowing        Exception disappears                    |
|    - Control flow      Use if-else instead                     |
|    - Wrong order       Specific first, general last            |
|                                                                |
|  Best Practices       10 rules for production code             |
|    - Specific types, no swallowing, add context                |
|    - try-with-resources, test exception paths                  |
|                                                                |
+---------------------------------------------------------------+
```

### Comparison: Part 1 vs Part 2

| Topic | Part 1 | Part 2 |
|-------|--------|--------|
| Basics | try, catch, finally | — |
| Execution flow | Single path | Nested paths |
| Single catch | One type, one handler | — |
| Multiple catch | One type per block | Multi-catch in one block |
| Multi-catch | Introduction | Rules, bytecode, edge cases |
| Nested try | — | Inner/outer propagation |
| Rethrowing | — | Original, wrapped, Java 7+ |
| Mistakes | — | 6 common errors |
| Best practices | — | 10 rules |
| Production | — | 4 real-world examples |

---

## 9. Key Takeaways

1. **Multi-catch reduces boilerplate** but only use it when the handling is identical for all exception types.

2. **Catch parameters in multi-catch are implicitly final.** You cannot reassign them, and they work with lambdas.

3. **Nested try-catch is useful** for separating local error handling from propagation. Keep nesting shallow.

4. **Rethrowing preserves the original exception** when you pass it to the new exception's constructor as the cause.

5. **Java 7+ lets you rethrow checked exceptions** from multi-catch blocks without declaring them in the method signature.

6. **Never leave catch blocks empty.** At minimum, log the exception. An exception that disappears is a bug that will come back.

7. **Don't catch Exception or Throwable.** Catch the most specific type you can handle.

8. **Exceptions are not control flow.** Use null checks and validation for expected branches. Exceptions are for unexpected failures.

9. **Order matters.** Most specific exception types first, general types last. The compiler enforces this.

10. **Test your exception paths.** Exception handling code is real code and it can have bugs. Write tests for it.

---

[← Part 1](README-Part1.md)
