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


---

**Continue to Part 2**: README-part2.md

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## Examples

[Code examples demonstrating the concept]

## Performance

[Performance considerations and benchmarks]

## Pitfalls

[Common mistakes and anti-patterns]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
