# TDD Approaches: London vs Chicago

## Overview
Two main schools: London (Mockist) and Chicago (Classicist).

## London School (Mockist)
- Mock all collaborators from the start
- Test interactions between objects

```java
@Test
void shouldProcessOrder() {
    OrderRepository repository = mock(OrderRepository.class);
    PaymentGateway gateway = mock(PaymentGateway.class);
    NotificationService notification = mock(NotificationService.class);
    OrderProcessor processor = new OrderProcessor(repository, gateway, notification);
    Order order = new Order("ITEM-1", 2, 10.00);
    processor.process(order);
    verify(repository).save(order);
    verify(gateway).charge(order.getTotal());
    verify(notification).sendOrderConfirmation(order);
}
```

## Chicago School (Classicist)
- Use real objects when possible
- Mock only at boundaries

```java
@Test
void shouldProcessOrder() {
    InMemoryOrderRepository repository = new InMemoryOrderRepository();
    FakePaymentGateway gateway = new FakePaymentGateway();
    InMemoryNotificationService notification = new InMemoryNotificationService();
    OrderProcessor processor = new OrderProcessor(repository, gateway, notification);
    Order order = new Order("ITEM-1", 2, 10.00);
    processor.process(order);
    assertEquals(1, repository.findAll().size());
    assertTrue(gateway.wasCharged(order.getTotal()));
}
```

## Comparison

| Factor | London | Chicago |
|--------|--------|---------|
| Test speed | Fast | Medium |
| Mock maintenance | High | Low |
| Design feedback | Strong | Moderate |
| Realism | Low | High |

## Hybrid Strategy
Use mocks for external dependencies, real objects for internal logic.
