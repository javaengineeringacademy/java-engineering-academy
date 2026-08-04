# Test Pyramid

## Overview
The test pyramid is a model for balancing different types of automated tests.

## The Pyramid
```
        /\
       /  \        E2E Tests (Few)
      /    \       Slow, expensive, high confidence
     /------\
    /        \     Integration Tests (Some)
   /          \    Moderate speed and cost
  /------------\
 /              \  Unit Tests (Many)
/                \ Fast, cheap, focused
```

## Distribution

| Test Type | Count | Speed | Cost | Confidence |
|-----------|-------|-------|------|------------|
| Unit | Many | Fast | Low | Code logic |
| Integration | Some | Medium | Medium | Component interaction |
| E2E | Few | Slow | High | Full system |

## Unit Tests (Base)
```java
@Test
void shouldCalculateTotal() {
    ShoppingCart cart = new ShoppingCart();
    cart.add(new Product("Widget", 10.00), 2);
    assertEquals(20.00, cart.getTotal(), 0.01);
}
```

## Integration Tests (Middle)
```java
@SpringBootTest
class OrderRepositoryIntegrationTest {
    @Autowired
    private OrderRepository repository;

    @Test
    void shouldPersistOrder() {
        Order order = new Order("ITEM-1", 2, 10.00);
        Order saved = repository.save(order);
        assertNotNull(saved.getId());
        assertEquals(20.00, saved.getTotal(), 0.01);
    }
}
```

## E2E Tests (Top)
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderE2ETest {
    @Test
    void shouldPlaceOrderEndToEnd() {
        // Create order via API
        // Verify payment processed
        // Verify inventory updated
        // Verify email sent
    }
}
```

## Inverted Pyramid (Anti-pattern)
Too many E2E tests, not enough unit tests. Results in slow, fragile test suite.

## Honeycomb Pattern
Alternative to pyramid for microservices: many integration tests, fewer unit/E2E.

## Best Practices
1. Most tests should be unit tests
2. Integration tests for component boundaries
3. E2E tests for critical user journeys only
4. Never skip the base of the pyramid
5. Use contract testing between services
