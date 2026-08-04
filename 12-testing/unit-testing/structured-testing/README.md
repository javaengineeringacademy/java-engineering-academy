# Structured Testing

## Overview

Structured testing is a systematic approach to software testing that emphasizes organized test design, clear separation of concerns, and repeatable test execution patterns. It promotes consistency across test suites and makes tests easier to maintain and understand.

## Core Principles

### Separation of Concerns

Each test should focus on a single behavior or aspect of the system under test.

```java
class OrderServiceStructuredTest {

    @Nested
    @DisplayName("Order Creation")
    class OrderCreation {

        @Test
        @DisplayName("should create order with valid items")
        void shouldCreateOrderWithValidItems() {
            // Single responsibility: test order creation
        }

        @Test
        @DisplayName("should reject order with empty cart")
        void shouldRejectOrderWithEmptyCart() {
            // Single responsibility: test validation
        }
    }

    @Nested
    @DisplayName("Order Payment")
    class OrderPayment {

        @Test
        @DisplayName("should process payment successfully")
        void shouldProcessPaymentSuccessfully() {
            // Single responsibility: test payment
        }
    }
}
```

### AAA Pattern (Arrange-Act-Assert)

```java
@Test
void shouldCalculateTotalPrice() {
    // Arrange
    List<Item> items = List.of(
        new Item("Book", 29.99),
        new Item("Pen", 2.99)
    );
    Order order = new Order(items);

    // Act
    double total = order.calculateTotal();

    // Assert
    assertEquals(32.98, total, 0.01);
}
```

### Given-When-Then Pattern

```java
@Test
void shouldApplyDiscountForPremiumUsers() {
    // Given
    User user = new User("John", Membership.PREMIUM);
    Order order = new Order(List.of(new Item("Laptop", 999.99)));

    // When
    double total = order.calculateTotalFor(user);

    // Then
    assertEquals(899.99, total, 0.01); // 10% discount
}
```

## Test Organization Strategies

### By Feature

```java
@DisplayName("Shopping Cart Features")
class ShoppingCartFeatureTest {

    @Nested
    @DisplayName("Add Item")
    class AddItem {

        @Test
        void shouldAddNewItemToCart() { }

        @Test
        void shouldIncrementQuantityForExistingItem() { }

        @Test
        void shouldNotExceedMaxQuantity() { }
    }

    @Nested
    @DisplayName("Remove Item")
    class RemoveItem {

        @Test
        void shouldRemoveItemFromCart() { }

        @Test
        void shouldDecreaseQuantityBeforeRemoving() { }

        @Test
        void shouldReturnEmptyCartWhenAllRemoved() { }
    }

    @Nested
    @DisplayName("Calculate Total")
    class CalculateTotal {

        @Test
        void shouldSumAllItemPrices() { }

        @Test
        void shouldApplyPromotionalDiscounts() { }

        @Test
        void shouldIncludeTaxInTotal() { }
    }
}
```

### By Layer

```
tests/
├── unit/
│   ├── service/
│   │   ├── OrderServiceTest.java
│   │   ├── PaymentServiceTest.java
│   │   └── InventoryServiceTest.java
│   └── repository/
│       ├── OrderRepositoryTest.java
│       └── UserRepositoryTest.java
├── integration/
│   ├── api/
│   │   ├── OrderApiTest.java
│   │   └── UserApiTest.java
│   └── database/
│       ├── OrderRepositoryIntegrationTest.java
│       └── UserRepositoryIntegrationTest.java
└── e2e/
    └── checkout/
        └── CheckoutFlowTest.java
```

### By Behavior (BDD Style)

```java
@Feature("User Authentication")
class AuthenticationTest {

    @Scenario("Successful login with valid credentials")
    @Test
    void successfulLogin() {
        given()
            .validCredentials("user@example.com", "password123")
        .when()
            .userAttemptsToLogin()
        .then()
            .userIsAuthenticated()
            .dashboardIsDisplayed();
    }

    @Scenario("Failed login with invalid password")
    @Test
    void failedLogin() {
        given()
            .validEmailButWrongPassword("user@example.com", "wrong")
        .when()
            .userAttemptsToLogin()
        .then()
            .authenticationFails()
            .errorMessageIsShown("Invalid credentials");
    }
}
```

## Test Data Management

### Builder Pattern for Test Data

```java
public class UserBuilder {
    private String name = "Default User";
    private String email = "default@example.com";
    private int age = 25;
    private Membership membership = Membership.BASIC;

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder withAge(int age) {
        this.age = age;
        return this;
    }

    public UserBuilder premium() {
        this.membership = Membership.PREMIUM;
        return this;
    }

    public User build() {
        return new User(name, email, age, membership);
    }
}

// Usage
User premiumUser = aUser()
    .withName("John")
    .withEmail("john@example.com")
    .premium()
    .build();
```

### Test Data Fixtures

```java
public class TestData {

    public static Stream<Arguments> validUserProvider() {
        return Stream.of(
            Arguments.of("John", "john@example.com", 25),
            Arguments.of("Jane", "jane@example.com", 30),
            Arguments.of("Bob", "bob@example.com", 35)
        );
    }

    public static List<Item> sampleItems() {
        return List.of(
            new Item("Book", 29.99),
            new Item("Pen", 2.99),
            new Item("Notebook", 9.99)
        );
    }
}

@ParameterizedTest
@MethodSource("TestData#validUserProvider")
void shouldCreateValidUser(String name, String email, int age) {
    User user = new User(name, email, age);
    assertNotNull(user);
    assertEquals(name, user.getName());
}
```

### Random Test Data

```java
class RandomDataTest {

    @Test
    void shouldHandleRandomInput() {
        // Using Random instance
        Random random = new Random();
        int randomAge = random.nextInt(100);
        String randomName = "User_" + random.nextInt(10000);

        // Using UUID for unique values
        String uniqueId = UUID.randomUUID().toString();

        // Using Faker library
        Faker faker = new Faker();
        String fakeName = faker.name().fullName();
        String fakeEmail = faker.internet().emailAddress();
    }
}
```

## Parameterized Testing

### @ParameterizedTest

```java
class CalculatorParameterizedTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void shouldHandlePositiveNumbers(int number) {
        assertTrue(calculator.isPositive(number));
    }

    @ParameterizedTest
    @CsvSource({
        "1, 2, 3",
        "10, 20, 30",
        "100, 200, 300"
    })
    void shouldAddNumbers(int a, int b, int expected) {
        assertEquals(expected, calculator.add(a, b));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test-data/calculations.csv", numLinesToSkip = 1)
    void shouldCalculateFromCsv(int a, int b, String operation, int expected) {
        int result = calculator.calculate(a, b, operation);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @EnumSource(Operation.class)
    void shouldSupportAllOperations(Operation operation) {
        assertNotNull(calculator.getOperation(operation));
    }

    @ParameterizedTest
    @MethodSource("com.example.TestData#primeNumbers")
    void shouldIdentifyPrimeNumbers(int number) {
        assertTrue(calculator.isPrime(number));
    }
}
```

### Enum-based Testing

```java
@ParameterizedTest
@EnumSource(value = HttpStatus.class, names = {"OK", "CREATED", "ACCEPTED"})
void shouldHandleSuccessStatuses(HttpStatus status) {
    assertTrue(status.is2xxSuccessful());
}

@ParameterizedTest
@EnumSource(value = Currency.class, mode = EnumSource.Mode.EXCLUDE, names = "BITCOIN")
void shouldSupportFiatCurrencies(Currency currency) {
    assertTrue(paymentService.supportsCurrency(currency));
}
```

## Test Configuration

### Test Profiles

```java
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@ActiveProfiles("test")
class ApplicationConfigTest {

    @Autowired
    private ConfigProperties config;

    @Test
    void shouldLoadTestConfiguration() {
        assertEquals("test-value", config.getValue());
    }
}
```

### Test Containers Configuration

```java
@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:tc:postgresql:15:///testdb"
})
class DatabaseIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Test
    void shouldConnectToDatabase() {
        assertNotNull(dataSource);
    }
}
```

## Test Utilities

### Custom Assertions

```java
public class OrderAssert extends AbstractAssert<OrderAssert, Order> {

    protected OrderAssert(Order actual) {
        super(actual, OrderAssert.class);
    }

    public static OrderAssert assertThat(Order actual) {
        return new OrderAssert(actual);
    }

    public OrderAssert hasStatus(OrderStatus expected) {
        isNotNull();
        if (!actual.getStatus().equals(expected)) {
            failWithMessage("Expected status to be %s but was %s", expected, actual.getStatus());
        }
        return this;
    }

    public OrderAssert hasTotal(double expected) {
        isNotNull();
        if (Double.compare(actual.getTotal(), expected) != 0) {
            failWithMessage("Expected total to be %s but was %s", expected, actual.getTotal());
        }
        return this;
    }

    public OrderAssert hasItemCount(int expected) {
        isNotNull();
        if (actual.getItems().size() != expected) {
            failWithMessage("Expected %d items but found %d", expected, actual.getItems().size());
        }
        return this;
    }
}

// Usage
assertThat(order)
    .hasStatus(OrderStatus.CONFIRMED)
    .hasTotal(99.99)
    .hasItemCount(3);
```

### Test Helpers

```java
public class TestHelper {

    public static void assertThrowsWithMessage(
            Executable executable,
            Class<? extends Throwable> expectedType,
            String expectedMessage
    ) {
        Throwable thrown = assertThrows(expectedType, executable);
        assertEquals(expectedMessage, thrown.getMessage());
    }

    public static <T> void assertListEquals(
            List<T> expected,
            List<T> actual,
            Comparator<T> comparator
    ) {
        assertEquals(expected.size(), actual.size(), "List sizes differ");
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(0, comparator.compare(expected.get(i), actual.get(i)),
                "Elements at index " + i + " differ");
        }
    }
}
```

## Test Naming Conventions

### MethodNamingTest

```java
class MethodNamingTest {

    // Pattern: methodUnderTest_scenario_expectedResult

    @Test
    void getUser_validId_returnsUser() { }

    @Test
    void getUser_invalidId_throwsException() { }

    @Test
    void getUser_deletedUser_returnsEmpty() { }

    // Pattern: should_expectedBehavior_when_condition

    @Test
    void should_returnUser_when_idIsValid() { }

    @Test
    void should_throwException_when_idIsInvalid() { }

    @Test
    void should_returnEmpty_when_userIsDeleted() { }
}
```

## Test Data Builders (Advanced)

### Generic Builder

```java
public class Builder<T> {
    private final Supplier<T> initial;
    private final List<Consumer<T>> modifications = new ArrayList<>();

    private Builder(Supplier<T> initial) {
        this.initial = initial;
    }

    public static <T> Builder<T> of(Supplier<T> initial) {
        return new Builder<>(initial);
    }

    public <U> Builder<T> with(BiConsumer<T, U> setter, U value) {
        Consumer<T> modification = obj -> setter.accept(obj, value);
        modifications.add(modification);
        return this;
    }

    public T build() {
        T object = initial.get();
        modifications.forEach(mod -> mod.accept(object));
        return object;
    }
}

// Usage
User user = Builder.of(User::new)
    .with(User::setName, "John")
    .with(User::setEmail, "john@example.com")
    .with(User::setAge, 25)
    .build();
```

## Integration with CI/CD

### Test Reports

```yaml
# GitHub Actions
- name: Run Tests
  run: mvn test

- name: Publish Test Results
  uses: dorny/test-reporter@v1
  with:
    name: JUnit Tests
    path: '**/surefire-reports/TEST-*.xml'
    reporter: java-junit
```

### Test Coverage

```xml
<!-- Maven JaCoCo -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Common Anti-Patterns

### ❌ Test Interdependence

```java
// Bad: Tests depend on each other
@Test
void test1_shouldCreateUser() {
    User user = userService.create("John");
    userId = user.getId(); // Shared state
}

@Test
void test2_shouldUpdateUser() {
    userService.update(userId, "Jane"); // Depends on test1
}
```

### ✅ Independent Tests

```java
// Good: Each test is independent
@Test
void shouldCreateUser() {
    User user = userService.create("John");
    assertNotNull(user.getId());
}

@Test
void shouldUpdateUser() {
    User created = userService.create("John");
    User updated = userService.update(created.getId(), "Jane");
    assertEquals("Jane", updated.getName());
}
```

### ❌ Excessive Mocking

```java
// Bad: Too many mocks
@Mock UserRepository userRepo;
@Mock EmailService emailService;
@Mock PaymentService paymentService;
@Mock InventoryService inventoryService;
@Mock NotificationService notificationService;
// ... 10 more mocks
```

### ✅ Focused Testing

```java
// Good: Only mock what's necessary
@Test
void shouldSendWelcomeEmail() {
    EmailService mockEmail = mock(EmailService.class);
    UserService service = new UserService(mockEmail);

    service.createUser("John", "john@example.com");

    verify(mockEmail).sendWelcome("john@example.com");
}
```

## Resources

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Testing Patterns](https://martinfowler.com/articles/practical-test-pyramid.html)
- [Test Smells](https://dzone.com/articles/test-smells)
- [Google Testing Blog](https://testing.googleblog.com/)
