# JUnit 5 - Java Testing Framework

## Overview

JUnit 5 is the latest major version of the Java testing framework, providing a modern and extensible platform for writing and running tests. It consists of three main components: JUnit Platform, JUnit Jupiter (the new programming model), and JUnit Vintage (backward compatibility).

## Architecture

```
JUnit 5
├── JUnit Platform    - Launches testing frameworks on the JVM
├── JUnit Jupiter     - New programming model and extension model
└── JUnit Vintage     - Backward compatibility with JUnit 3/4
```

## Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Configuration

```groovy
test {
    useJUnitPlatform()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.10.1'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.10.1'
    testImplementation 'org.junit.jupiter:junit-jupiter-params:5.10.1'
}
```

## Basic Annotations

### @Test - Test Method

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void shouldAddTwoNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.add(2, 3);
        assertEquals(5, result);
    }

    @Test
    void shouldSubtractTwoNumbers() {
        Calculator calculator = new Calculator();
        int result = calculator.subtract(5, 3);
        assertEquals(2, result);
    }
}
```

### @DisplayName - Readable Test Names

```java
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRegistrationTest {

    @Test
    @DisplayName("Should successfully register a new user with valid data")
    void shouldRegisterUserWithValidData() {
        // Test implementation
    }

    @Test
    @DisplayName("Should fail registration when email is invalid")
    void shouldFailRegistrationWithInvalidEmail() {
        // Test implementation
    }

    @Test
    @DisplayName("Should fail registration when password is too weak")
    void shouldFailRegistrationWithWeakPassword() {
        // Test implementation
    }
}
```

### @Disabled - Skip Tests

```java
@Test
@Disabled("Feature not implemented yet")
void shouldImplementFutureFeature() {
    // Placeholder for future implementation
}

@Test
@Disabled(reason = "Bug #1234 - Known issue, will fix in next sprint")
void shouldBeFixedInNextSprint() {
    // Currently disabled
}
```

## Lifecycle Annotations

### @BeforeEach and @AfterEach

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        connection = Database.getConnection();
        connection.open();
        System.out.println("Connection opened");
    }

    @AfterEach
    void tearDown() {
        connection.close();
        System.out.println("Connection closed");
    }

    @Test
    void shouldExecuteQuery() {
        ResultSet result = connection.executeQuery("SELECT * FROM users");
        assertNotNull(result);
    }

    @Test
    void shouldInsertRecord() {
        boolean inserted = connection.insert("INSERT INTO users VALUES (1, 'John')");
        assertTrue(inserted);
    }
}
```

### @BeforeAll and @AfterAll

```java
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static Connection connection;
    private UserRepository repository;

    @BeforeAll
    static void setUpClass() {
        // Expensive operation - runs once for all tests
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb");
        connection.createStatement().execute(
            "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(100))"
        );
    }

    @AfterAll
    static void tearDownClass() throws Exception {
        // Clean up after all tests
        connection.close();
    }

    @BeforeEach
    void setUp() {
        repository = new UserRepository(connection);
    }

    @Test
    void shouldFindUserById() {
        repository.save(new User(1, "John"));
        User found = repository.findById(1);
        assertEquals("John", found.getName());
    }
}
```

### Nested Tests

```java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ShoppingCart cart;

    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
    }

    @Nested
    @DisplayName("When cart is empty")
    class EmptyCart {

        @Test
        @DisplayName("Should have zero total")
        void shouldHaveZeroTotal() {
            assertEquals(0.0, cart.getTotal());
        }

        @Test
        @DisplayName("Should report empty status")
        void shouldReportEmptyStatus() {
            assertTrue(cart.isEmpty());
        }

        @Nested
        @DisplayName("When adding first item")
        class AddingFirstItem {

            @Test
            @DisplayName("Should contain one item")
            void shouldContainOneItem() {
                cart.addItem(new Product("Book", 29.99));
                assertEquals(1, cart.getItemCount());
            }

            @Test
            @DisplayName("Should have item price as total")
            void shouldHaveItemPriceAsTotal() {
                cart.addItem(new Product("Book", 29.99));
                assertEquals(29.99, cart.getTotal());
            }
        }
    }

    @Nested
    @DisplayName("When cart has items")
    class CartWithItems {

        @BeforeEach
        void setUpItems() {
            cart.addItem(new Product("Book", 29.99));
            cart.addItem(new Product("Pen", 5.99));
        }

        @Test
        @DisplayName("Should calculate total correctly")
        void shouldCalculateTotal() {
            assertEquals(35.98, cart.getTotal());
        }

        @Test
        @DisplayName("Should remove item successfully")
        void shouldRemoveItem() {
            cart.removeItem(0);
            assertEquals(1, cart.getItemCount());
        }
    }
}
```

## Assertions

### Basic Assertions

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AssertionExamplesTest {

    @Test
    void basicAssertions() {
        // assertEquals - Check equality
        assertEquals(4, 2 + 2, "Basic arithmetic should work");

        // assertTrue and assertFalse
        assertTrue(10 > 5);
        assertFalse(5 > 10);

        // assertNotNull and assertNull
        String text = "Hello";
        assertNotNull(text);
        assertNull(null);

        // assertThrows - Exception testing
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Invalid value");
        });

        // assertTimeout - Performance testing
        assertTimeout(Duration.ofSeconds(1), () -> {
            // This should complete within 1 second
            Thread.sleep(100);
        });
    }

    @Test
    void arrayAssertions() {
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};

        assertArrayEquals(expected, actual);
        assertIterableEquals(List.of(1, 2, 3), List.of(1, 2, 3));
    }

    @Test
    void groupAssertions() {
        // Execute multiple assertions and report all failures
        assertAll("Grouped assertions",
            () -> assertEquals(4, 2 + 2),
            () -> assertTrue("Hello".length() > 0),
            () -> assertNotNull(new Object())
        );
    }
}
```

### Advanced Assertions

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdvancedAssertionExamplesTest {

    @Test
    void assertLinesMatch() {
        List<String> expected = List.of("Java", "JUnit", "Testing");
        List<String> actual = List.of("Java", "JUnit", "Testing");

        assertLinesMatch(expected, actual);
    }

    @Test
    void assertTimeoutPreemptively() {
        // Fails immediately if timeout exceeded (doesn't wait for completion)
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            // Long-running operation
            Thread.sleep(50);
            assertEquals(4, 2 + 2);
        });
    }

    @Test
    void customMessage() {
        int result = calculateDiscount(100, 0.1);
        assertEquals(90, result, 
            "Discount calculation failed: expected 90 for 10% off 100");
    }

    @Test
    void exceptionAssertions() {
        // Test exception message
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validateAge(-1)
        );
        assertEquals("Age cannot be negative", exception.getMessage());

        // Test exception with cause
        assertThrows(RuntimeException.class, () -> {
            try {
                throw new IOException("File not found");
            } catch (IOException e) {
                throw new RuntimeException("Processing failed", e);
            }
        });
    }
}
```

## Parameterized Tests

### @ValueSource

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PalindromeTest {

    @ParameterizedTest
    @ValueSource(strings = {"racecar", "madam", "level", "radar"})
    void shouldDetectPalindrome(String word) {
        assertTrue(Palindrome.isPalindrome(word));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 5, 7, 11})
    void shouldIdentifyPrimeNumbers(int number) {
        assertTrue(Prime.isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 2.0, 3.0, 4.0})
    void shouldCalculateSquareRoot(double number) {
        assertEquals(Math.sqrt(number), SquareRoot.calculate(number), 0.0001);
    }
}
```

### @CsvSource

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculatorTest {

    @ParameterizedTest
    @CsvSource({
        "1, 2, 3",
        "0, 0, 0",
        "-1, 1, 0",
        "100, 200, 300"
    })
    void shouldAddNumbers(int a, int b, int expected) {
        assertEquals(expected, Calculator.add(a, b));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "HELLO, hello",
        "JUnit5, junit5",
        "TEST, test"
    }, delimiter = ',')
    void shouldConvertToLowerCase(String input, String expected) {
        assertEquals(expected, input.toLowerCase());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data.csv", numLinesToSkip = 1)
    void shouldProcessCsvFile(int id, String name, boolean active) {
        User user = new User(id, name, active);
        assertNotNull(user);
    }
}
```

### @MethodSource

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

class NumberTest {

    static Stream<Integer> evenNumbers() {
        return Stream.of(2, 4, 6, 8, 10, 12);
    }

    static Stream<Arguments> additionTestCases() {
        return Stream.of(
            Arguments.of(1, 2, 3),
            Arguments.of(0, 0, 0),
            Arguments.of(-1, 1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("evenNumbers")
    void shouldDetectEvenNumbers(int number) {
        assertTrue(number % 2 == 0);
    }

    @ParameterizedTest
    @MethodSource("additionTestCases")
    void shouldAddNumbers(int a, int b, int expected) {
        assertEquals(expected, Calculator.add(a, b));
    }

    // Instance method source
    @ParameterizedTest
    @MethodSource("testCaseProvider")
    void testCase(int input, String expected) {
        assertEquals(expected, String.valueOf(input));
    }

    Stream<Arguments> testCaseProvider() {
        return Stream.of(
            Arguments.of(1, "1"),
            Arguments.of(2, "2"),
            Arguments.of(3, "3")
        );
    }
}
```

### @CsvFileSource

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class UserDataTest {

    @ParameterizedTest
    @CsvFileSource(resources = "/users.csv", numLinesToSkip = 1)
    void shouldValidateUser(String name, String email, int age) {
        User user = new User(name, email, age);
        assertTrue(user.isValid());
    }
}

// resources/users.csv:
// name,email,age
// John,john@example.com,25
// Jane,jane@example.com,30
// Bob,bob@example.com,35
```

### @EnumSource

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class StatusTest {

    @ParameterizedTest
    @EnumSource(Status.class)
    void shouldHandleAllStatuses(Status status) {
        assertNotNull(status.getCode());
        assertNotNull(status.getDescription());
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, names = {"ACTIVE", "INACTIVE"})
    void shouldHandleSpecificStatuses(Status status) {
        assertTrue(status.isModifiable());
    }

    @ParameterizedTest
    @EnumSource(value = Status.class, mode = EnumSource.Mode.EXCLUDE, 
                names = {"DELETED"})
    void shouldExcludeDeletedStatus(Status status) {
        assertNotNull(status);
    }
}
```

### Custom ArgumentsProvider

```java
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import java.util.stream.Stream;

class CustomArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
            Arguments.of(1, "one"),
            Arguments.of(2, "two"),
            Arguments.of(3, "three")
        );
    }
}

class NumberConverterTest {

    @ParameterizedTest
    @ArgumentsSource(CustomArgumentProvider.class)
    void shouldConvertNumberToWord(int number, String expected) {
        assertEquals(expected, NumberConverter.convertToWord(number));
    }
}
```

## Extensions

### Extension Model

```java
import org.junit.jupiter.api.extension.*;

class TimingExtension implements BeforeEachCallback, AfterEachCallback {

    private long startTime;

    @Override
    public void beforeEach(ExtensionContext context) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Test " + context.getDisplayName() + 
                          " took " + duration + "ms");
    }
}

@ExtendWith(TimingExtension.class)
class PerformanceTest {

    @Test
    void shouldCompleteQuickly() {
        // Test implementation
    }
}
```

### Custom Extension Example

```java
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.Assertions;

class RandomExtension implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == int.class
            && parameterContext.isAnnotated(Random.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) {
        Random annotation = parameterContext.findAnnotation(Random.class).get();
        return (int) (Math.random() * annotation.max()) + annotation.min();
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@interface Random {
    int min() default 0;
    int max() default 100;
}

class RandomNumberTest {

    @Test
    void testWithRandomNumber(@Random(min = 1, max = 10) int number) {
        assertTrue(number >= 1 && number <= 10);
    }
}
```

### TempDirectory Extension

```java
import org.junit.jupiter.api.extension.TempDirectory;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;

class FileTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldWriteToFile() throws IOException {
        Path file = tempDir.resolve("test.txt");
        Files.writeString(file, "Hello, World!");

        assertTrue(Files.exists(file));
        assertEquals("Hello, World!", Files.readString(file));
    }

    @Test
    void shouldCreateMultipleFiles() throws IOException {
        for (int i = 0; i < 5; i++) {
            Files.writeString(tempDir.resolve("file" + i + ".txt"), 
                            "Content " + i);
        }

        assertEquals(5, Files.list(tempDir).count());
    }
}
```

## Test Suites

```java
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SelectPackages;

@Suite
@SelectClasses({
    CalculatorTest.class,
    StringManipulatorTest.class,
    DateUtilsTest.class
})
class UnitTestSuite {
    // This class remains empty
}

@Suite
@SelectPackages("com.example.unit")
class PackageTestSuite {
    // Runs all tests in the specified package
}
```

## Best Practices

### Test Naming Conventions

```java
// Pattern 1: should_ExpectedBehavior_When_Condition
@Test
void should_CalculateTotal_When_ItemsAreAdded() { }

// Pattern 2: given_Precondition_when_Action_then_ExpectedResult
@Test
void given_ValidUser_when_Login_then_Success() { }

// Pattern 3: methodUnderTest_Scenario_ExpectedBehavior
@Test
void calculateDiscount_LargeOrder_Returns10Percent() { }

// Pattern 4: descriptive test names
@Test
@DisplayName("Should send welcome email when user registers")
void shouldSendWelcomeEmailOnRegistration() { }
```

### AAA Pattern

```java
@Test
void shouldCalculateDiscountedPrice() {
    // Arrange
    Product product = new Product("Laptop", 1000.0);
    Discount discount = new Discount(0.1); // 10%

    // Act
    double discountedPrice = product.applyDiscount(discount);

    // Assert
    assertEquals(900.0, discountedPrice);
}
```

### Test Isolation

```java
class UserServiceTest {

    private UserService service;
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(UserRepository.class);
        service = new UserService(repository);
    }

    @Test
    void shouldCreateUser() {
        // Each test has fresh mock and service
        when(repository.save(any())).thenReturn(new User(1, "John"));

        User user = service.createUser("John");

        assertNotNull(user);
        verify(repository).save(any());
    }
}
```

## Integration with Build Tools

### Maven Surefire Plugin

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.3</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
    </configuration>
</plugin>
```

### Gradle Test Configuration

```groovy
test {
    useJUnitPlatform()

    testLogging {
        events "passed", "skipped", "failed"
        showStandardStreams = true
    }

    maxParallelForks = Runtime.runtime.availableProcessors()

    // Filter tests
    filter {
        includeTestsMatching "*Test"
        includeTestsMatching "*Tests"
    }
}
```

## Common Patterns

### Testing Exceptions

```java
@Test
void shouldThrowExceptionWhenUserNotFound() {
    when(repository.findById(999L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
        service.getUser(999L);
    });
}

@Test
void shouldThrowExceptionWithMessage() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> validator.validateAge(-1)
    );

    assertEquals("Age cannot be negative", exception.getMessage());
}
```

### Testing Time-Dependent Code

```java
import org.junit.jupiter.api.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

class TimeServiceTest {

    @Test
    void shouldReturnCurrentTime() {
        Clock fixedClock = Clock.fixed(
            Instant.parse("2024-01-15T10:00:00Z"),
            ZoneId.of("UTC")
        );

        TimeService service = new TimeService(fixedClock);
        assertEquals("2024-01-15", service.getCurrentDate());
    }
}
```

### Testing Collections

```java
@Test
void shouldSortUsersByName() {
    List<User> users = List.of(
        new User("Charlie"),
        new User("Alice"),
        new User("Bob")
    );

    List<User> sorted = userService.sortByName(users);

    assertIterableEquals(
        List.of(new User("Alice"), new User("Bob"), new User("Charlie")),
        sorted
    );
}
```

## Resources

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [JUnit 5 GitHub](https://github.com/junit-team/junit5)
- [JUnit 5 Examples](https://github.com/junit-team/junit5-samples)
- [Baeldung JUnit 5 Tutorial](https://www.baeldung.com/junit-5)
