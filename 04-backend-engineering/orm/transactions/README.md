# Transactions

## Comprehensive Guide to ACID Properties and Transaction Management

Transactions ensure data consistency. This guide covers ACID properties, propagation, isolation levels, and Spring transaction management.

---

## Table of Contents

1. [ACID Properties](#acid-properties)
2. [Transaction Management](#transaction-management)
3. [Propagation](#propagation)
4. [Isolation Levels](#isolation-levels)
5. [Best Practices](#best-practices)

---

## ACID Properties

### ACID Overview

```
A - Atomicity: All or nothing
C - Consistency: Valid state transitions
I - Isolation: Concurrent transactions don't interfere
D - Durability: Committed data persists
```

### Example

```java
@Transactional
public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
    Account from = accountRepository.findById(fromId);
    Account to = accountRepository.findById(toId);

    from.setBalance(from.getBalance().subtract(amount));
    to.setBalance(to.getBalance().add(amount));

    accountRepository.save(from);
    accountRepository.save(to);

    // If any operation fails, all changes are rolled back
}
```

---

## Transaction Management

### Programmatic

```java
@Service
public class OrderService {

    private final TransactionTemplate transactionTemplate;
    private final EntityManager em;

    public Order createOrder(OrderRequest request) {
        return transactionTemplate.execute(status -> {
            try {
                Order order = new Order(request);
                em.persist(order);
                return order;
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
        });
    }
}
```

### Declarative (Recommended)

```java
@Service
public class OrderService {

    @Transactional
    public Order createOrder(OrderRequest request) {
        Order order = new Order(request);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id);
        order.cancel();
        orderRepository.save(order);
    }
}
```

---

## Propagation

### Propagation Types

```java
// REQUIRED (default) - Join existing or create new
@Transactional(propagation = Propagation.REQUIRED)
public void required() { }

// REQUIRES_NEW - Always create new
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void requiresNew() { }

// NESTED - Create nested transaction
@Transactional(propagation = Propagation.NESTED)
public void nested() { }

// SUPPORTS - Join existing if exists
@Transactional(propagation = Propagation.SUPPORTS)
public void supports() { }

// NOT_SUPPORTED - Don't join existing
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public void notSupported() { }

// MANDATORY - Must join existing
@Transactional(propagation = Propagation.MANDATORY)
public void mandatory() { }

// NEVER - Must not have existing
@Transactional(propagation = Propagation.NEVER)
public void never() { }
```

### Example

```java
@Service
public class OrderService {

    @Transactional
    public void processOrder(Long orderId) {
        // Joins existing transaction
        orderRepository.updateStatus(orderId, "PROCESSING");

        // Creates new transaction
        auditService.logOrderProcessed(orderId);
    }
}

@Service
public class AuditService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logOrderProcessed(Long orderId) {
        // Runs in separate transaction
        // Even if order processing rolls back
        auditRepository.save(new AuditLog("ORDER_PROCESSED", orderId));
    }
}
```

---

## Isolation Levels

### Isolation Levels

```java
// READ_UNCOMMITTED - Dirty reads possible
@Transactional(isolation = Isolation.READ_UNCOMMITTED)

// READ_COMMITTED - No dirty reads
@Transactional(isolation = Isolation.READ_COMMITTED)

// REPEATABLE_READ - No non-repeatable reads
@Transactional(isolation = Isolation.REPEATABLE_READ)

// SERIALIZABLE - Full isolation
@Transactional(isolation = Isolation.SERIALIZABLE)
```

### Read Phenomena

```
Dirty Read:     Reading uncommitted data
Non-Repeatable: Reading same row twice gives different results
Phantom Read:   New rows appear between reads
```

### Example

```java
@Service
public class InventoryService {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reserveStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId);

        if (product.getStock() < quantity) {
            throw new InsufficientStockException();
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
```

---

## Best Practices

### 1. Keep Transactions Short

```java
// Good - Short transaction
@Transactional
public void updateUser(Long id, UserUpdates updates) {
    User user = userRepository.findById(id);
    user.update(updates);
    userRepository.save(user);
}

// Bad - Long transaction
@Transactional
public void processLargeBatch(List<Item> items) {
    // Long-running operation
    for (Item item : items) {
        processItem(item); // Too much work in transaction
    }
}
```

### 2. Use readOnly for Reads

```java
@Transactional(readOnly = true)
public User findById(Long id) {
    return userRepository.findById(id);
}
```

### 3. Handle Exceptions Properly

```java
@Transactional
public void processOrder(Long orderId) {
    try {
        Order order = orderRepository.findById(orderId);
        order.process();
        orderRepository.save(order);
    } catch (Exception e) {
        // Transaction will be rolled back
        throw new OrderProcessingException(orderId, e);
    }
}
```

### 4. Use Propagation Wisely

```java
@Transactional
public void processOrder(Long orderId) {
    orderRepository.updateStatus(orderId, "PROCESSING");

    // Audit in separate transaction
    auditService.logOrderProcessed(orderId);
}

@Service
public class AuditService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logOrderProcessed(Long orderId) {
        auditRepository.save(new AuditLog("ORDER_PROCESSED", orderId));
    }
}
```

### 5. Avoid @Transactional on Private Methods

```java
// Bad - AOP won't work
@Transactional
private void privateMethod() { }

// Good
@Transactional
public void publicMethod() { }
```

---

## Further Reading

- [Spring Transaction Management](https://docs.spring.io/spring-framework/reference/data-access/transaction.html)
- [ACID Properties](https://en.wikipedia.org/wiki/ACID)
- [Transaction Isolation Levels](https://en.wikipedia.org/wiki/Isolation_(database_systems))
