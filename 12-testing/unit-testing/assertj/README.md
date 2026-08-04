# AssertJ - Fluent Assertions Library

## Overview

AssertJ is a Java library that provides a rich set of assertions and a fluent API for writing test assertions. It aims to improve test readability and provides better error messages than standard JUnit assertions.

## Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <version>3.25.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Configuration

```groovy
dependencies {
    testImplementation 'org.assertj:assertj-core:3.25.1'
}
```

## Basic Assertions

### Object Assertions

```java
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ObjectAssertionTest {

    @Test
    void shouldAssertObjects() {
        String name = "John";

        assertThat(name).isNotNull();
        assertThat(name).isEqualTo("John");
        assertThat(name).isNotEqualTo("Jane");
        assertThat(name).contains("oh");
        assertThat(name).startsWith("Jo");
        assertThat(name).endsWith("hn");
        assertThat(name).hasSize(4);
    }

    @Test
    void shouldAssertWithDescriptions() {
        String actual = "test";
        assertThat(actual)
            .as("Check if string is 'test'")
            .isEqualTo("test");
    }
}
```

### Null Assertions

```java
class NullAssertionTest {

    @Test
    void shouldAssertNullability() {
        String nonNull = "value";
        String nullValue = null;

        assertThat(nonNull).isNotNull();
        assertThat(nullValue).isNull();
        assertThat(nullValue).isEmpty(); // For strings
    }

    @Test
    void shouldAssertNullWithMessage() {
        String nullValue = null;
        assertThat(nullValue)
            .as("Null check")
            .isNull();
    }
}
```

## String Assertions

```java
class StringAssertionTest {

    @Test
    void shouldAssertStringContent() {
        assertThat("Hello World")
            .isEqualTo("Hello World")
            .isNotEmpty()
            .hasSize(11)
            .contains("World")
            .contains("Hello", "World")
            .startsWith("Hello")
            .endsWith("World")
            .matches("Hello.*")
            .doesNotContain("foo")
            .doesNotMatch(".*test.*");
    }

    @Test
    void shouldAssertStringCase() {
        assertThat("Hello")
            .isEqualToIgnoringCase("hello")
            .startsWithIgnoringCase("hELLO")
            .endsWithIgnoringCase("ello");
    }

    @Test
    void shouldAssertStringNormalization() {
        assertThat("  Hello  ")
            .isEqualToIgnoringWhitespace("Hello");
    }

    @Test
    void shouldAssertStringLines() {
        assertThat("line1\nline2\nline3")
            .contains("line1")
            .contains("line2")
            .contains("line3");
    }
}
```

## Number Assertions

```java
class NumberAssertionTest {

    @Test
    void shouldAssertNumbers() {
        assertThat(42)
            .isPositive()
            .isGreaterThan(40)
            .isGreaterThanOrEqualTo(42)
            .isLessThan(100)
            .isBetween(0, 100);
    }

    @Test
    void shouldAssertDecimals() {
        assertThat(3.14)
            .isCloseTo(3.14, within(0.01))
            .isPositive()
            .isGreaterThan(3.0);
    }

    @Test
    void shouldAssertComparisons() {
        assertThat(5).isEqualTo(5);
        assertThat(5).isNotEqualTo(6);
        assertThat(5).isGreaterThan(3);
        assertThat(5).isLessThan(10);
    }
}
```

## Collection Assertions

```java
import java.util.*;

class CollectionAssertionTest {

    @Test
    void shouldAssertLists() {
        List<String> list = Arrays.asList("a", "b", "c");

        assertThat(list)
            .isNotEmpty()
            .hasSize(3)
            .contains("a", "b", "c")
            .containsExactly("a", "b", "c")
            .containsExactlyInAnyOrder("c", "a", "b")
            .containsOnly("a", "b", "c")
            .startsWith("a")
            .endsWith("c")
            .element(0).isEqualTo("a");
    }

    @Test
    void shouldAssertSets() {
        Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));

        assertThat(set)
            .hasSize(3)
            .contains(1, 2, 3)
            .doesNotContain(4, 5)
            .allMatch(n -> n > 0)
            .anyMatch(n -> n == 2)
            .noneMatch(n -> n < 0);
    }

    @Test
    void shouldAssertMaps() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);

        assertThat(map)
            .hasSize(2)
            .containsKey("one")
            .containsEntry("one", 1)
            .containsValue(2)
            .doesNotContainKey("three")
            .doesNotContainEntry("three", 3);
    }

    @Test
    void shouldAssertCollections() {
        Collection<String> collection = Arrays.asList("a", "b", "c");

        assertThat(collection)
            .hasSize(3)
            .contains("a")
            .filteredOn(s -> s.startsWith("a"))
            .hasSize(1)
            .first().isEqualTo("a");
    }
}
```

## Array Assertions

```java
class ArrayAssertionTest {

    @Test
    void shouldAssertArrays() {
        String[] array = {"a", "b", "c"};

        assertThat(array)
            .hasSize(3)
            .contains("a", "b", "c")
            .containsExactly("a", "b", "c")
            .containsExactlyInAnyOrder("c", "a", "b")
            .startsWith("a")
            .endsWith("c");
    }

    @Test
    void shouldAssertPrimitiveArrays() {
        int[] numbers = {1, 2, 3, 4, 5};

        assertThat(numbers)
            .hasSize(5)
            .contains(1, 2, 3)
            .containsExactly(1, 2, 3, 4, 5);
    }
}
```

## Exception Assertions

```java
class ExceptionAssertionTest {

    @Test
    void shouldAssertExceptionThrown() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                throw new IllegalArgumentException("Invalid value");
            })
            .withMessage("Invalid value")
            .withNoCause();
    }

    @Test
    void shouldAssertExceptionWithCause() {
        assertThatExceptionOfType(RuntimeException.class)
            .isThrownBy(() -> {
                try {
                    throw new IOException("File not found");
                } catch (IOException e) {
                    throw new RuntimeException("Processing failed", e);
                }
            })
            .withMessage("Processing failed")
            .withCauseInstanceOf(IOException.class);
    }

    @Test
    void shouldAssertNoException() {
        assertThatCode(() -> {
            // This code should not throw any exception
            int result = 2 + 2;
        }).doesNotThrowAnyException();
    }

    @Test
    void shouldAssertExceptionMessage() {
        assertThatThrownBy(() -> {
            throw new IllegalArgumentException("Value must be positive");
        })
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("positive")
            .hasMessageContainingAll("Value", "positive");
    }
}
```

## Date and Time Assertions

```java
import java.time.*;

class DateAssertionTest {

    @Test
    void shouldAssertLocalDate() {
        LocalDate date = LocalDate.of(2024, 1, 15);

        assertThat(date)
            .isBefore(LocalDate.of(2024, 12, 31))
            .isAfter(LocalDate.of(2024, 1, 1))
            .isBetween(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
    }

    @Test
    void shouldAssertLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();

        assertThat(dateTime)
            .isBefore(LocalDateTime.now().plusDays(1))
            .isAfter(LocalDateTime.now().minusDays(1));
    }

    @Test
    void shouldAssertDuration() {
        Duration duration = Duration.ofSeconds(30);

        assertThat(duration)
            .isGreaterThan(Duration.ofSeconds(10))
            .isLessThan(Duration.ofMinutes(1));
    }
}
```

## File and Path Assertions

```java
import java.io.File;
import java.nio.file.Path;

class FileAssertionTest {

    @Test
    void shouldAssertFiles() {
        File file = new File("test.txt");

        assertThat(file)
            .exists()
            .isFile()
            .canRead()
            .canWrite();
    }

    @Test
    void shouldAssertDirectories() {
        File directory = new File("src/main");

        assertThat(directory)
            .exists()
            .isDirectory();
    }

    @Test
    void shouldAssertFilePaths() {
        Path path = Path.of("src/main/java");

        assertThat(path)
            .exists()
            .isDirectory()
            .hasFileName("java");
    }
}
```

## Custom Assertions

### Creating Custom Assert Class

```java
import org.assertj.core.api.AbstractAssert;

public class UserAssert extends AbstractAssert<UserAssert, User> {

    public UserAssert(User actual) {
        super(actual, UserAssert.class);
    }

    public static UserAssert assertThat(User actual) {
        return new UserAssert(actual);
    }

    public UserAssert hasName(String name) {
        isNotNull();
        if (!actual.getName().equals(name)) {
            failWithMessage("Expected user with name <%s> but was <%s>", 
                          name, actual.getName());
        }
        return this;
    }

    public UserAssert hasEmail(String email) {
        isNotNull();
        if (!actual.getEmail().equals(email)) {
            failWithMessage("Expected user with email <%s> but was <%s>", 
                          email, actual.getEmail());
        }
        return this;
    }

    public UserAssert isAdult() {
        isNotNull();
        if (actual.getAge() < 18) {
            failWithMessage("Expected user to be adult but age was <%s>", 
                          actual.getAge());
        }
        return this;
    }

    public UserAssert hasValidEmail() {
        isNotNull();
        if (!actual.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            failWithMessage("Expected user to have valid email but was <%s>", 
                          actual.getEmail());
        }
        return this;
    }
}

// Usage
class UserTest {

    @Test
    void shouldAssertUser() {
        User user = new User("John", "john@example.com", 25);

        assertThat(user)
            .hasName("John")
            .hasEmail("john@example.com")
            .isAdult()
            .hasValidEmail();
    }
}
```

### Custom Collection Assert

```java
public class OrderListAssert extends AbstractListAssert<OrderListAssert, 
                                                       List<Order>, 
                                                       Order, 
                                                       ObjectAssert<Order>> {

    public OrderListAssert(List<Order> actual) {
        super(actual, OrderListAssert.class, Order.class, ElementsAreValidators.checkObjectAreNotNull());
    }

    public static OrderListAssert assertThat(List<Order> actual) {
        return new OrderListAssert(actual);
    }

    public OrderListAssert hasTotalAmount(double expectedTotal) {
        double actualTotal = actual.stream()
            .mapToDouble(Order::getTotal)
            .sum();
        if (Double.compare(actualTotal, expectedTotal) != 0) {
            failWithMessage("Expected total amount <%s> but was <%s>", 
                          expectedTotal, actualTotal);
        }
        return this;
    }

    public OrderListAssert allOrdersAreConfirmed() {
        actual.forEach(order -> 
            assertThat(order.getStatus())
                .isEqualTo(OrderStatus.CONFIRMED));
        return this;
    }
}
```

## Filtering and Transforming

```java
class FilteringTest {

    @Test
    void shouldFilterCollections() {
        List<User> users = Arrays.asList(
            new User("John", 25),
            new User("Jane", 30),
            new User("Bob", 17)
        );

        assertThat(users)
            .filteredOn(user -> user.getAge() >= 18)
            .hasSize(2)
            .extracting(User::getName)
            .containsExactly("John", "Jane");
    }

    @Test
    void shouldTransformCollections() {
        List<User> users = Arrays.asList(
            new User("John", 25),
            new User("Jane", 30)
        );

        assertThat(users)
            .extracting(User::getName)
            .containsExactly("John", "Jane");

        assertThat(users)
            .extracting(User::getName, User::getAge)
            .containsExactly(
                tuple("John", 25),
                tuple("Jane", 30)
            );
    }

    @Test
    void shouldFlatExtract() {
        List<Order> orders = Arrays.asList(
            new Order(List.of("item1", "item2")),
            new Order(List.of("item3", "item4", "item5"))
        );

        assertThat(orders)
            .flatExtracting(Order::getItems)
            .containsExactly("item1", "item2", "item3", "item4", "item5");
    }
}
```

## Error Message Customization

```java
class CustomErrorMessagesTest {

    @Test
    void shouldProvideCustomErrorMessages() {
        assertThat(42)
            .as("Check if number is 42")
            .isEqualTo(42);
    }

    @Test
    void shouldDescribeAssertion() {
        User user = new User("John", 25);

        assertThat(user.getName())
            .as("Check user name")
            .isEqualTo("John");
    }
}
```

## Best Practices

### Use Fluent API

```java
// Fluent style - more readable
assertThat(user)
    .hasName("John")
    .hasEmail("john@example.com")
    .isAdult();
```

### Use Descriptions for Complex Assertions

```java
assertThat(order)
    .as("Order should be confirmed with valid total")
    .hasStatus(OrderStatus.CONFIRMED)
    .hasTotalGreaterThan(0);
```

### Filter Before Asserting

```java
assertThat(users)
    .filteredOn(User::isActive)
    .hasSize(5)
    .allMatch(User::isVerified);
```

## Resources

- [AssertJ Core GitHub](https://github.com/assertj/assertj)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [AssertJ Feature Matrix](https://assertj.github.io/doc/#feature-matrix)
- [Baeldung AssertJ Tutorial](https://www.baeldung.com/assertj)
