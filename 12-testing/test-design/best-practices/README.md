# Test Best Practices

## Naming
```java
// BAD: test1(), shouldWork()
// GOOD:
@Test void shouldCalculateTotalPriceForMultipleItems() { }
@Test void shouldThrowExceptionWhenCartIsEmpty() { }
```

## Structure (AAA)
```java
@Test
void shouldProcessOrder() {
    // Arrange
    Order order = new Order("ITEM-1", 2, 10.00);
    // Act
    processor.process(order);
    // Assert
    assertEquals(OrderStatus.PROCESSED, order.getStatus());
}
```

## Assertions
```java
// BAD: assertTrue(result != null)
// GOOD:
assertNotNull(result);
assertEquals(3, list.size());
assertThat(order).hasStatus(OrderStatus.PROCESSED);
```

## Test Data
```java
// Use builders
Order order = OrderBuilder.anOrder().withItem("ITEM-1", 2, 10.00).build();
```

## Isolation
- No shared mutable state between tests
- Clean up resources in @AfterEach
- Tests should be deterministic

## Speed
- Unit tests: milliseconds
- Integration tests: seconds
- E2E tests: minutes (but still fast as possible)

## Coverage Targets
- Unit tests: 80-95%
- Integration tests: Key paths
- E2E tests: Critical user journeys
