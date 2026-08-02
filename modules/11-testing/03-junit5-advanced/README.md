## 15. Hard Example

```java
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

// Custom annotation combining multiple extensions
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TimingExtension.class)
@ExtendWith(CsvDataExtension.class)
@interface TimedCsvTest {
}

// Custom Extension for Timing
class TimingExtension implements BeforeEachCallback, AfterEachCallback {
    
    private static final String START_TIME = "start time";
    
    @Override
    public void beforeEach(ExtensionContext context) {
        context.getStore(ExtensionContext.Namespace.create(getClass()))
            .put(START_TIME, System.nanoTime());
    }
    
    @Override
    public void afterEach(ExtensionContext context) {
        long startTime = context.getStore(ExtensionContext.Namespace.create(getClass()))
            .get(START_TIME, Long.class);
        long duration = System.nanoTime() - startTime;
        System.out.printf("Test [%s] took %d ms%n", 
            context.getDisplayName(), 
            TimeUnit.NANOSECONDS.toMillis(duration));
    }
}

// Custom Extension for CSV Data
class CsvDataExtension implements ParameterResolver {
    
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, 
                                      ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == CsvData.class;
    }
    
    @Override
    public Object resolveParameter(ParameterContext parameterContext, 
                                    ExtensionContext extensionContext) {
        return new CsvData("test", 42);
    }
}

record CsvData(String name, int value) {}

// Complex Parameterized Test with Multiple Sources
class AdvancedParameterizedTest {
    
    @ParameterizedTest(name = "Factorial of {0} = {1}")
    @MethodSource("factorialProvider")
    @DisplayName("Should calculate factorial correctly")
    void shouldCalculateFactorial(long input, long expected) {
        assertEquals(expected, factorial(input));
    }
    
    static Stream<Arguments> factorialProvider() {
        return Stream.of(
            Arguments.arguments(0L, 1L),
            Arguments.arguments(1L, 1L),
            Arguments.arguments(5L, 120L),
            Arguments.arguments(10L, 3628800L),
            Arguments.arguments(20L, 2432902008176640000L)
        );
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    @DisplayName("Should load data from CSV file")
    void shouldLoadFromCsvFile(String name, int age, String email) {
        assertNotNull(name);
        assertTrue(age > 0);
        assertTrue(email.contains("@"));
    }
    
    private long factorial(long n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
}

// Dynamic Tests with Complex Generation
class DynamicTestExample {
    
    @TestFactory
    @DisplayName("Dynamic Math Tests")
    Stream<DynamicTest> mathTests() {
        return Stream.iterate(1, i -> i + 1)
            .limit(10)
            .map(i -> dynamicTest("Square of " + i, 
                () -> assertEquals(i * i, i * i)));
    }
    
    @TestFactory
    @DisplayName("Dynamic Tests with Containers")
    Collection<DynamicContainer> complexTests() {
        List<DynamicContainer> containers = new ArrayList<>();
        
        for (int i = 1; i <= 3; i++) {
            final int multiplier = i;
            List<DynamicTest> tests = new ArrayList<>();
            
            for (int j = 1; j <= 3; j++) {
                final int operand = j;
                tests.add(dynamicTest(
                    String.format("%d x %d = %d", multiplier, operand, multiplier * operand),
                    () -> assertEquals(multiplier * operand, multiplier * operand)
                ));
            }
            
            containers.add(dynamicContainer("Group " + multiplier, tests));
        }
        
        return containers;
    }
}

// Extension with Store for Shared State
class SharedStateExtension implements BeforeAllCallback, AfterAllCallback {
    
    private static final String SHARED_LIST = "shared list";
    
    @Override
    public void beforeAll(ExtensionContext context) {
        List<String> sharedList = new ArrayList<>();
        sharedList.add("Initial");
        context.getStore(ExtensionContext.Namespace.GLOBAL)
            .put(SHARED_LIST, sharedList);
    }
    
    @Override
    public void afterAll(ExtensionContext context) {
        List<String> sharedList = context.getStore(ExtensionContext.Namespace.GLOBAL)
            .get(SHARED_LIST, List.class);
        assertNotNull(sharedList);
        assertFalse(sharedList.isEmpty());
    }
}

@ExtendWith(SharedStateExtension.class)
class ExtensionWithStateTest {
    
    @Test
    void shouldAccessSharedState() {
        // Access shared state through extension
        assertTrue(true);
    }
}
```

## 16. Enterprise Example

```java
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

// Custom annotation for retry logic
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(RetryExtension.class)
@interface Retry {
    int value() default 3;
    long delay() default 1000;
}

// Extension implementing retry logic
class RetryExtension implements TestExecutionExceptionHandler {
    
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Retry retry = context.getRequiredTestMethod().getAnnotation(Retry.class);
        if (retry == null) {
            throw throwable;
        }
        
        int maxRetries = retry.value();
        long delay = retry.delay();
        
        for (int i = 0; i < maxRetries - 1; i++) {
            try {
                Thread.sleep(delay);
                Method method = context.getRequiredTestMethod();
                Object instance = context.getRequiredTestInstance();
                method.invoke(instance);
                return; // Test passed on retry
            } catch (Exception e) {
                if (i == maxRetries - 2) {
                    throw throwable; // Last retry failed
                }
            }
        }
    }
}

// Test monitoring extension
class TestMonitoringExtension implements TestExecutionListener {
    
    private static final Map<String, Long> testStartTimes = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> testResults = new ConcurrentHashMap<>();
    
    @Override
    public void testStarted(TestIdentifier testIdentifier) {
        testStartTimes.put(testIdentifier.getUniqueId(), System.nanoTime());
    }
    
    @Override
    public void testFinished(TestIdentifier testIdentifier) {
        long startTime = testStartTimes.getOrDefault(testIdentifier.getUniqueId(), 0L);
        long duration = System.nanoTime() - startTime;
        testResults.put(testIdentifier.getUniqueId(), duration < TimeUnit.SECONDS.toNanos(1));
    }
    
    public Map<String, Boolean> getResults() {
        return Collections.unmodifiableMap(testResults);
    }
}

// Enterprise test suite with multiple extensions
@ExtendWith({TimingExtension.class, RetryExtension.class})
@DisplayName("Enterprise Test Suite")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EnterpriseAdvancedTest {
    
    @Test
    @Order(1)
    @DisplayName("Critical transaction processing")
    @Retry(value = 3, delay = 500)
    void shouldProcessCriticalTransaction() {
        // Simulate critical operation
        Transaction transaction = new Transaction("TXN-001", 1000.00, "USD");
        assertTrue(transaction.isValid());
    }
    
    @Test
    @Order(2)
    @DisplayName("User authentication flow")
    void shouldAuthenticateUser() {
        AuthResult result = authenticate("admin", "password123");
        assertTrue(result.isSuccess());
        assertNotNull(result.getToken());
    }
    
    @ParameterizedTest
    @Order(3)
    @CsvSource({
        "USD, EUR, 0.85",
        "GBP, USD, 1.27",
        "JPY, USD, 0.0067"
    })
    @DisplayName("Currency conversion accuracy")
    void shouldConvertCurrency(String from, String to, double rate) {
        double amount = 100.0;
        double result = convertCurrency(amount, from, to);
        assertEquals(amount * rate, result, 0.01);
    }
    
    @Nested
    @DisplayName("API Response Validation")
    class ApiValidationTests {
        
        @ParameterizedTest
        @ValueSource(strings = {"GET", "POST", "PUT", "DELETE"})
        @DisplayName("Should handle all HTTP methods")
        void shouldHandleHttpMethods(String method) {
            ApiResponse response = makeApiCall(method, "/api/test");
            assertNotNull(response);
            assertTrue(response.getStatusCode() >= 200 && response.getStatusCode() < 300);
        }
        
        @TestFactory
        @DisplayName("Dynamic endpoint validation")
        Stream<DynamicTest> validateEndpoints() {
            List<String> endpoints = List.of("/users", "/products", "/orders");
            return endpoints.stream()
                .map(endpoint -> dynamicTest(
                    "Validate " + endpoint,
                    () -> {
                        ApiResponse response = makeApiCall("GET", endpoint);
                        assertNotNull(response.getBody());
                    }
                ));
        }
    }
    
    // Helper methods
    private record Transaction(String id, double amount, String currency) {
        boolean isValid() {
            return id != null && amount > 0 && currency != null;
        }
    }
    
    private record AuthResult(boolean success, String token) {}
    
    private record ApiResponse(int statusCode, String body) {}
    
    private AuthResult authenticate(String username, String password) {
        if ("admin".equals(username) && "password123".equals(password)) {
            return new AuthResult(true, "token-" + UUID.randomUUID());
        }
        return new AuthResult(false, null);
    }
    
    private double convertCurrency(double amount, String from, String to) {
        Map<String, Double> rates = Map.of(
            "USD_EUR", 0.85,
            "GBP_USD", 1.27,
            "JPY_USD", 0.0067
        );
        String key = from + "_" + to;
        return amount * rates.getOrDefault(key, 1.0);
    }
    
    private ApiResponse makeApiCall(String method, String endpoint) {
        return new ApiResponse(200, "{\"status\": \"ok\"}");
    }
}
```

## 17. Performance

| Feature | Performance Impact | Optimization |
|---------|-------------------|--------------|
| Parameterized Tests | Medium | Limit parameter combinations |
| Dynamic Tests | Low | Avoid heavy computation in factory |
| Extensions | Low | Minimize callback overhead |
| Nested Tests | Minimal | Organize logically |

**Performance Tips:**
- Use @MethodSource with streams for lazy evaluation
- Cache expensive test data in extensions
- Avoid heavy operations in extension callbacks
- Use @TestFactory for runtime test generation

## 18. Time & Space Complexity

| Feature | Time Complexity | Space Complexity |
|---------|-----------------|------------------|
| Parameterized Test | O(p × t) | O(p × a) |
| Dynamic Test Generation | O(g × t) | O(g) |
| Extension Callbacks | O(1) per callback | O(e) |
| Nested Test Discovery | O(n × d) | O(n × d) |

Where: p = parameter count, t = test time, a = argument size, g = generated tests, e = extension state, n = tests, d = nesting depth

## 19. Thread Safety

- Parameterized tests run sequentially by default
- Dynamic tests can be parallelized safely
- Extension state must be thread-safe
- Use ConcurrentHashMap for shared extension state
- Avoid mutable shared state in extensions

## 20. Best Practices

1. **Use meaningful parameterized test names** for clarity
2. **Group related tests** with nested classes
3. **Keep extensions focused** on single responsibility
4. **Use extension stores** for state management
5. **Prefer @MethodSource** for complex argument generation
6. **Avoid heavy logic** in test factories
7. **Document extension behavior** clearly
8. **Test extensions** themselves with unit tests
9. **Use custom annotations** for common patterns
10. **Monitor extension performance** in large suites

## 21. Common Mistakes

1. **Too many parameters** - Makes tests hard to debug
2. **Complex dynamic test generation** - Hard to understand
3. **Extension ordering issues** - Callbacks not executing
4. **State leaks between extensions** - Using wrong namespace
5. **Over-engineering extensions** - Simple problems, complex solutions
6. **Ignoring extension cleanup** - Resource leaks
7. **Hardcoded test data** - Should be externalized
8. **Missing test names** - Hard to identify failures
9. **Circular dependencies** between extensions
10. **Not testing extensions** in isolation

## 22. Pitfalls

- **Parameterized test explosions** - Too many combinations
- **Dynamic test fragility** - Tests change at runtime
- **Extension interference** - Extensions affecting each other
- **Nested test complexity** - Hard to navigate
- **Custom annotation misuse** - Over-abstracting simple tests
- **Extension memory leaks** - Not cleaning up stores

## 23. Debugging Tips

1. **Use descriptive test names** in parameterized tests
2. **Print test parameters** in assertions for clarity
3. **Log extension callbacks** for debugging
4. **Use TestInfo** to get test metadata
5. **Check extension store** for state issues
6. **Verify extension order** with multiple extensions
7. **Test extensions independently** first

## 24. Comparison Table

| Feature | Basic | Advanced |
|---------|-------|----------|
| Parameterized | @ValueSource | @MethodSource, @CsvFileSource |
| Dynamic | Simple list | Containers, streams |
| Extensions | Basic callbacks | Full lifecycle control |
| Nested | Simple grouping | Complex hierarchies |
| Custom Annotations | Not supported | Full support |

## 25. Decision Tree

```
When to use advanced features?
│
├─ Need multiple data sets?
│  ├─ Simple values → @ValueSource
│  ├─ CSV data → @CsvSource
│  └─ Complex data → @MethodSource
│
├─ Need runtime test generation?
│  └─ Use @TestFactory
│
├─ Need cross-cutting concerns?
│  └─ Create custom extension
│
├─ Need test organization?
│  └─ Use @Nested with @DisplayName
│
└─ Need reusable test patterns?
   └─ Create custom annotation
```

## 26. Interview Questions

1. **What are parameterized tests and when should you use them?**
   - Answer: Parameterized tests run the same test with different data; use when testing multiple input combinations.

2. **Explain the difference between @ValueSource and @MethodSource.**
   - Answer: @ValueSource provides simple values; @MethodSource references a method for complex argument generation.

3. **What is a dynamic test and how is it different from a regular test?**
   - Answer: Dynamic tests are generated at runtime via @TestFactory; regular tests are static and discovered at compile time.

4. **How do custom extensions work in JUnit 5?**
   - Answer: Extensions implement callback interfaces and are registered with @ExtendWith to hook into test lifecycle.

5. **What is the purpose of nested tests?**
   - Answer: Nested tests organize related tests hierarchically with shared setup and better readability.

6. **How do you handle test data externalization?**
   - Answer: Use @CsvFileSource for CSV files or @MethodSource for programmatic data generation.

7. **What are the available extension points in JUnit 5?**
   - Answer: BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback, ParameterResolver, etc.

8. **How do you ensure extension thread safety?**
   - Answer: Use thread-safe data structures and ExtensionContext.Namespace for isolation.

9. **Can you combine multiple extensions?**
   - Answer: Yes, using multiple @ExtendWith annotations or a custom composed annotation.

10. **What is the extension store and how is it used?**
    - Answer: A key-value store in ExtensionContext for sharing state between extension callbacks.

11. **How do you create a custom test annotation?**
    - Answer: Define an annotation with @ExtendWith to compose multiple extensions.

12. **What are the limitations of dynamic tests?**
    - Answer: They don't support lifecycle callbacks, parameterized tests, or nesting.

13. **How do you test extensions themselves?**
   - Answer: Write unit tests for extension logic and integration tests with test classes.

14. **What is the difference between TestExecutionListener and Extension?**
   - Answer: TestExecutionListener monitors all tests; Extension is per-class and can modify behavior.

15. **How do you manage test ordering with extensions?**
   - Answer: Use @Order with @TestMethodOrder or implement custom ordering in extensions.

## 27. Exercises

### Beginner

1. **Parameterized Test Practice**
   - Create tests for string length with @ValueSource
   - Test math operations with @CsvSource
   - Use @MethodSource for prime number testing

2. **Simple Extension**
   - Create an extension that logs test execution time
   - Register with @ExtendWith
   - Verify timing is printed for each test

### Intermediate

3. **Complex Parameterized Tests**
   - Create tests loading data from external CSV
   - Implement custom argument converter
   - Test with multiple parameter sources

4. **Nested Test Hierarchy**
   - Design a 3-level nested test structure
   - Share setup between levels
   - Use @DisplayName for clarity

### Advanced

5. **Custom Extension Suite**
   - Create extension for retry logic
   - Implement test monitoring extension
   - Build extension for test data generation

6. **Dynamic Test Generation**
   - Generate tests from database schema
   - Create test containers for grouping
   - Implement conditional test generation

## 28. Summary

Advanced JUnit 5 features enable sophisticated testing patterns. Parameterized tests reduce duplication, dynamic tests provide flexibility, extensions enable reuse, and nested tests improve organization. Mastering these features creates maintainable, comprehensive test suites.

## 29. References

- JUnit 5 Parameterized Tests: https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
- JUnit 5 Extensions: https://junit.org/junit5/docs/current/user-guide/#extensions
- JUnit 5 Dynamic Tests: https://junit.org/junit5/docs/current/user-guide/#writing-tests-dynamic-tests
- JUnit 5 Nested Tests: https://junit.org/junit5/docs/current/user-guide/#writing-tests-nested
