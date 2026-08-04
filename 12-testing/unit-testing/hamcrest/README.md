# Hamcrest - Matcher-Based Assertions

## Overview

Hamcrest is a framework for writing matcher objects that can be used in testing. It provides a rich library of matcher classes for creating flexible and expressive test assertions. Hamcrest is widely used with JUnit and other testing frameworks.

## Setup

### Maven Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest</artifactId>
        <version>2.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.hamcrest</groupId>
        <artifactId>hamcrest-library</artifactId>
        <version>2.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Gradle Configuration

```groovy
dependencies {
    testImplementation 'org.hamcrest:hamcrest:2.2'
    testImplementation 'org.hamcrest:hamcrest-library:2.2'
}
```

## Core Concept

Hamcrest uses a matcher pattern where you describe what you expect, and the matcher checks if the actual value matches.

```java
// Traditional assertion
assertEquals(5, calculator.add(2, 3));

// Hamcrest matcher
assertThat(calculator.add(2, 3), is(5));
```

## Basic Matchers

### Equality Matchers

```java
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class EqualityMatcherTest {

    @Test
    void shouldMatchEqualValues() {
        assertThat(5, is(5));
        assertThat("hello", is("hello"));
        assertThat(5, equalTo(5));
        assertThat("hello", equalTo("hello"));
    }

    @Test
    void shouldNotMatchUnequalValues() {
        assertThat(5, not(6));
        assertThat(5, is(not(equalTo(6))));
        assertThat("hello", not("world"));
    }
}
```

### Type Matchers

```java
class TypeMatcherTest {

    @Test
    void shouldCheckTypes() {
        assertThat("Hello", instanceOf(String.class));
        assertThat(42, instanceOf(Number.class));
        assertThat(new ArrayList<>(), isA(List.class));
    }

    @Test
    void shouldCheckNullability() {
        String nonNull = "value";
        String nullValue = null;

        assertThat(nonNull, is(notNullValue()));
        assertThat(nullValue, is(nullValue()));
        assertThat(nonNull, is(notNull()));
    }
}
```

### Comparison Matchers

```java
class ComparisonMatcherTest {

    @Test
    void shouldCompareValues() {
        assertThat(10, greaterThan(5));
        assertThat(5, greaterThanOrEqualTo(5));
        assertThat(3, lessThan(10));
        assertThat(5, lessThanOrEqualTo(5));
        assertThat(5, is(greaterThanOrEqualTo(5)));
    }

    @Test
    void shouldCheckRanges() {
        assertThat(5, both(greaterThan(0)).and(lessThan(10)));
        assertThat(15, either(greaterThan(10)).or(lessThan(5)));
    }
}
```

### String Matchers

```java
class StringMatcherTest {

    @Test
    void shouldMatchStringPatterns() {
        assertThat("Hello World", startsWith("Hello"));
        assertThat("Hello World", endsWith("World"));
        assertThat("Hello World", containsString("llo"));
        assertThat("Hello World", containsStringIgnoringCase("hello"));
    }

    @Test
    void shouldMatchStringEquality() {
        assertThat("hello", equalToIgnoringCase("HELLO"));
        assertThat("  Hello  ", equalToIgnoringWhiteSpace("Hello"));
        assertThat("Hello", hasToString("Hello"));
    }

    @Test
    void shouldMatchEmptyStrings() {
        assertThat("", emptyString());
        assertThat("   ", blankString());
        assertThat(null, blankOrNullString());
    }
}
```

### Collection Matchers

```java
import java.util.*;

class CollectionMatcherTest {

    @Test
    void shouldMatchCollections() {
        List<String> list = Arrays.asList("a", "b", "c");

        assertThat(list, hasSize(3));
        assertThat(list, hasItem("a"));
        assertThat(list, hasItems("a", "b", "c"));
        assertThat(list, contains("a", "b", "c"));
        assertThat(list, containsInAnyOrder("c", "a", "b"));
    }

    @Test
    void shouldMatchMaps() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        assertThat(map, hasKey("a"));
        assertThat(map, hasEntry("a", 1));
        assertThat(map, hasValue(2));
    }

    @Test
    void shouldMatchArrays() {
        String[] array = {"x", "y", "z"};

        assertThat(array, arrayWithSize(3));
        assertThat(array, hasItemInArray("y"));
        assertThat(array, arrayContaining("x", "y", "z"));
        assertThat(array, arrayContainingInAnyOrder("z", "x", "y"));
    }
}
```

### Logical Matchers

```java
class LogicalMatcherTest {

    @Test
    void shouldCombineMatchers() {
        assertThat(5, allOf(greaterThan(0), lessThan(10)));
        assertThat(5, anyOf(equalTo(3), equalTo(5), equalTo(7)));
        assertThat(5, not(allOf(equalTo(3), equalTo(7))));
    }

    @Test
    void shouldUseCombinators() {
        assertThat("hello", both(containsString("h")).and(containsString("o")));
        assertThat("hello", both(startsWith("h")).and(not(emptyString())));
    }
}
```

### Custom Property Matchers

```java
import static org.hamcrest.Matchers.*;

class PropertyMatcherTest {

    @Test
    void shouldMatchObjectProperties() {
        User user = new User("John", 25);

        assertThat(user, hasProperty("name", equalTo("John")));
        assertThat(user, hasProperty("age", equalTo(25)));
        assertThat(user, hasToString("User{name='John', age=25}"));
    }

    @Test
    void shouldMatchBeanProperties() {
        Product product = new Product("Laptop", 999.99);

        assertThat(product, hasProperty("name"));
        assertThat(product, hasProperty("price"));
    }
}
```

## Custom Matchers

### Creating Custom Matcher

```java
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class IsEvenNumber extends TypeSafeMatcher<Integer> {

    @Override
    protected boolean matchesSafely(Integer number) {
        return number % 2 == 0;
    }

    @Override
    protected void describeMismatchSafely(Integer number, Description mismatchDescription) {
        mismatchDescription.appendText("was ").appendValue(number)
                          .appendText(" which is odd");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an even number");
    }

    // Factory method
    public static Matcher<Integer> isEvenNumber() {
        return new IsEvenNumber();
    }
}

// Usage
class EvenNumberTest {

    @Test
    void shouldDetectEvenNumbers() {
        assertThat(4, isEvenNumber());
        assertThat(6, isEvenNumber());
        assertThat(8, isEvenNumber());
    }
}
```

### Custom String Matcher

```java
public class IsValidEmail extends TypeSafeMatcher<String> {

    private static final String EMAIL_REGEX = 
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    protected boolean matchesSafely(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    @Override
    protected void describeMismatchSafely(String email, Description mismatchDescription) {
        mismatchDescription.appendText("was invalid email: ").appendValue(email);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a valid email address");
    }

    public static Matcher<String> validEmail() {
        return new IsValidEmail();
    }
}

// Usage
class EmailValidatorTest {

    @Test
    void shouldValidateEmails() {
        assertThat("user@example.com", validEmail());
        assertThat("test@test.co", validEmail());
    }
}
```

### Custom Collection Matcher

```java
public class IsEmptyCollection extends TypeSafeMatcher<Collection<?>> {

    @Override
    protected boolean matchesSafely(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    @Override
    protected void describeMismatchSafely(Collection<?> collection, 
                                          Description mismatchDescription) {
        mismatchDescription.appendText("was not empty, had ")
                          .appendValue(collection.size())
                          .appendText(" elements");
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("an empty collection");
    }

    public static Matcher<Collection<?>> emptyCollection() {
        return new IsEmptyCollection();
    }
}

// Usage
class CollectionTest {

    @Test
    void shouldCheckCollectionIsEmpty() {
        List<String> emptyList = new ArrayList<>();
        List<String> nonEmptyList = Arrays.asList("a", "b");

        assertThat(emptyList, emptyCollection());
        assertThat(nonEmptyList, not(emptyCollection()));
    }
}
```

### Custom Range Matcher

```java
public class IsWithinRange extends TypeSafeDiagnosingMatcher<Integer> {

    private final int min;
    private final int max;

    public IsWithinRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean matchesSafely(Integer item, Description mismatchDescription) {
        if (item < min || item > max) {
            mismatchDescription.appendText("was ").appendValue(item)
                              .appendText(" which is outside range [")
                              .appendValue(min).appendText(", ")
                              .appendValue(max).appendText("]");
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a number between ")
                  .appendValue(min).appendText(" and ").appendValue(max);
    }

    public static Matcher<Integer> withinRange(int min, int max) {
        return new IsWithinRange(min, max);
    }
}

// Usage
class RangeTest {

    @Test
    void shouldCheckRange() {
        assertThat(5, withinRange(1, 10));
        assertThat(15, not(withinRange(1, 10)));
    }
}
```

## Hamcrest with JUnit 5

```java
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class JUnit5HamcrestTest {

    @Test
    void shouldUseHamcrestWithJUnit5() {
        assertThat(42, is(42));
        assertThat("hello", containsString("ell"));
        assertThat(List.of(1, 2, 3), hasSize(3));
    }

    @Test
    void shouldCombineWithAssertions() {
        // Hamcrest works with standard assertions
        int result = calculator.add(2, 3);
        assertEquals(5, result);  // JUnit assertion
        assertThat(result, is(5)); // Hamcrest assertion
    }
}
```

## Matcher Composition

```java
class MatcherCompositionTest {

    @Test
    void shouldComposeMatchers() {
        // All must match
        assertThat(5, allOf(greaterThan(0), lessThan(10), not(equalTo(7))));

        // Any must match
        assertThat(5, anyOf(equalTo(3), equalTo(5), equalTo(7)));

        // None must match
        assertThat(5, noneOf(equalTo(1), equalTo(2), equalTo(3)));
    }

    @Test
    void shouldUseDecoratorPattern() {
        // Wrapping matchers
        assertThat("hello", is(not(nullValue())));
        assertThat(5, is(greaterThan(0)));
        assertThat(List.of(1, 2), is(hasSize(2)));
    }
}
```

## Custom Matcher with Feature Matching

```java
public class HasAge extends FeatureMatcher<User, Integer> {

    public HasAge(Matcher<? super Integer> subMatcher) {
        super(subMatcher, "a user with age", "age");
    }

    @Override
    protected Integer featureValueOf(User actual) {
        return actual.getAge();
    }

    public static Matcher<User> hasAge(Matcher<? super Integer> ageMatcher) {
        return new HasAge(ageMatcher);
    }
}

// Usage
class UserMatcherTest {

    @Test
    void shouldMatchUserAge() {
        User user = new User("John", 25);

        assertThat(user, hasAge(equalTo(25)));
        assertThat(user, hasAge(greaterThan(18)));
    }
}
```

## Advanced Patterns

### Custom Collection Size Matcher

```java
public class CollectionOfSize extends TypeSafeDiagnosingMatcher<Collection<?>> {

    private final int expectedSize;

    public CollectionOfSize(int expectedSize) {
        this.expectedSize = expectedSize;
    }

    @Override
    protected boolean matchesSafely(Collection<?> collection, 
                                    Description mismatchDescription) {
        if (collection.size() != expectedSize) {
            mismatchDescription.appendText("collection size was ")
                              .appendValue(collection.size());
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a collection of size ").appendValue(expectedSize);
    }

    public static Matcher<Collection<?>> collectionOfSize(int size) {
        return new CollectionOfSize(size);
    }
}
```

### Custom Exception Matcher

```java
public class ThrowsException extends TypeSafeMatcher<Executable> {

    private final Class<? extends Throwable> expectedType;
    private final Matcher<? extends Throwable> messageMatcher;

    public ThrowsException(Class<? extends Throwable> expectedType,
                          Matcher<? extends Throwable> messageMatcher) {
        this.expectedType = expectedType;
        this.messageMatcher = messageMatcher;
    }

    @Override
    protected boolean matchesSafely(Executable executable) {
        try {
            executable.execute();
            return false;
        } catch (Throwable actual) {
            return expectedType.isInstance(actual) 
                && messageMatcher.matches(actual);
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("throw ").appendText(expectedType.getSimpleName())
                  .appendText(" with message ").appendDescriptionOf(messageMatcher);
    }

    public static Matcher<Executable> throwsException(
            Class<? extends Throwable> type,
            Matcher<? extends Throwable> messageMatcher) {
        return new ThrowsException(type, messageMatcher);
    }
}
```

## Best Practices

### Use Descriptive Matchers

```java
// Bad - unclear
assertThat(result, is(true));

// Good - descriptive
assertThat(user.isActive(), is(true));
assertThat(order.getStatus(), equalTo(OrderStatus.CONFIRMED));
```

### Combine Matchers Effectively

```java
// Complex assertions
assertThat(order, allOf(
    hasProperty("status", equalTo(OrderStatus.SHIPPED)),
    hasProperty("items", hasSize(greaterThan(0))),
    hasProperty("total", greaterThan(0.0))
));
```

### Use Custom Matchers for Domain Logic

```java
// Domain-specific matcher
assertThat(order, isValidOrder());
assertThat(user, hasValidEmail());
assertThat(product, isInStock());
```

## Resources

- [Hamcrest Documentation](http://hamcrest.org/JavaHamcrest/)
- [Hamcrest GitHub](https://github.com/hamcrest/JavaHamcrest)
- [Hamcrest Matcher Tutorial](https://www.baeldung.com/hamcrest-matchers)
- [Creating Custom Matchers](https://www.baeldung.com/creating-custom-matchers)
