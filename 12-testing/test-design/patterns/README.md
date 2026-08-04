# Test Design Patterns

## AAA Pattern (Arrange-Act-Assert)
```java
@Test
void shouldCalculateTotalPrice() {
    // Arrange
    ShoppingCart cart = new ShoppingCart();
    Product widget = new Product("Widget", 10.00);
    // Act
    cart.add(widget, 2);
    // Assert
    assertEquals(20.00, cart.getTotal(), 0.01);
}
```

## Given-When-Then
```java
@Test
void shouldProcessPayment() {
    // Given
    PaymentService service = new PaymentService(mockGateway);
    // When
    PaymentResult result = service.process(new PaymentRequest("card-123", 100.00));
    // Then
    assertTrue(result.isSuccess());
}
```

## Builder Pattern
```java
public class UserBuilder {
    private String name = "John Doe";
    private String email = "john@example.com";
    private Role role = Role.USER;
    public static UserBuilder aUser() { return new UserBuilder(); }
    public UserBuilder withRole(Role role) { this.role = role; return this; }
    public User build() { return new User(name, email, role); }
}
// Usage: User admin = UserBuilder.aUser().withRole(Role.ADMIN).build();
```

## Parameterized Tests
```java
@ParameterizedTest
@CsvSource({"100, 10, 90.00", "200, 15, 170.00"})
void shouldCalculateDiscount(double price, double discount, double expected) {
    assertEquals(expected, new Product("Test", price).applyDiscount(discount), 0.01);
}
```

## Best Practices
1. AAA as default structure
2. Test data builders for complex objects
3. Descriptive test names
4. One assertion per test concept
