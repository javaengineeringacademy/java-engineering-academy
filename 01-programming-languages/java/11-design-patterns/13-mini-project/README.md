# Mini Project: Order Management System

## 1. Introduction

This mini project combines multiple design patterns to build a comprehensive Order Management System. You will apply Creational, Structural, and Behavioral patterns in a realistic enterprise scenario.

---

## 2. Learning Objectives

By the end of this project, you will be able to:

- Apply multiple design patterns together in a real-world scenario
- Understand how patterns complement each other
- Design scalable, maintainable systems using patterns
- Make informed decisions about pattern selection
- Document pattern usage in architecture

---

## 3. Prerequisites

- Completion of all previous pattern topics
- Understanding of SOLID principles
- Knowledge of Java collections and streams
- Familiarity with exception handling

---

## 4. Project Overview

### 4.1 System Requirements

Build an Order Management System with:

1. **Order Creation**: Builder pattern for complex order construction
2. **Payment Processing**: Strategy pattern for multiple payment methods
3. **Notification System**: Observer pattern for event notifications
4. **Discount Calculation**: Strategy pattern for discount algorithms
5. **Order Processing**: Template Method for standardized processing
6. **Third-party Integration**: Adapter pattern for payment gateways
7. **Caching**: Proxy pattern for order caching
8. **Logging**: Decorator pattern for logging

### 4.2 Pattern Integration Map

```
Order Management System
├── Builder: Order construction
├── Factory: Payment method creation
├── Strategy: Discount calculation
├── Observer: Order notifications
├── Template Method: Order processing flow
├── Adapter: Payment gateway integration
├── Decorator: Logging, validation
├── Proxy: Caching layer
└── Singleton: Configuration management
```

---

## 5. Architecture

### 5.1 Domain Model

```
Order
├── orderId: String
├── customer: Customer
├── items: List<OrderItem>
├── payment: PaymentMethod
├── status: OrderStatus
├── totalAmount: BigDecimal
└── discount: DiscountStrategy
```

### 5.2 Pattern Application

| Component | Pattern | Purpose |
|-----------|---------|---------|
| Order | Builder | Complex construction |
| Payment | Strategy | Multiple payment methods |
| Notification | Observer | Event notifications |
| Discount | Strategy | Discount algorithms |
| Processing | Template Method | Standardized flow |
| Gateway | Adapter | Third-party integration |
| Logging | Decorator | Cross-cutting concerns |
| Cache | Proxy | Performance optimization |
| Config | Singleton | Global configuration |

---

## 6. Implementation

### 6.1 Core Domain Classes

```java
// Builder Pattern for Order
public class Order {
    private final String orderId;
    private final Customer customer;
    private final List<OrderItem> items;
    private final PaymentMethod payment;
    private final DiscountStrategy discount;
    private final OrderStatus status;
    private final BigDecimal totalAmount;

    private Order(Builder builder) {
        this.orderId = builder.orderId;
        this.customer = builder.customer;
        this.items = List.copyOf(builder.items);
        this.payment = builder.payment;
        this.discount = builder.discount;
        this.status = builder.status;
        this.totalAmount = builder.totalAmount;
    }

    public static class Builder {
        private String orderId;
        private Customer customer;
        private List<OrderItem> items = new ArrayList<>();
        private PaymentMethod payment;
        private DiscountStrategy discount = new NoDiscount();
        private OrderStatus status = OrderStatus.PENDING;
        private BigDecimal totalAmount = BigDecimal.ZERO;

        public Builder(String orderId) {
            this.orderId = orderId;
        }

        public Builder customer(Customer customer) {
            this.customer = customer;
            return this;
        }

        public Builder addItem(OrderItem item) {
            this.items.add(item);
            this.totalAmount = totalAmount.add(item.getPrice());
            return this;
        }

        public Builder payment(PaymentMethod payment) {
            this.payment = payment;
            return this;
        }

        public Builder discount(DiscountStrategy discount) {
            this.discount = discount;
            return this;
        }

        public Order build() {
            Objects.requireNonNull(orderId, "Order ID required");
            Objects.requireNonNull(customer, "Customer required");
            if (items.isEmpty()) {
                throw new IllegalStateException("Order must have at least one item");
            }
            totalAmount = discount.calculate(totalAmount);
            return new Order(this);
        }
    }
}
```

### 6.2 Strategy Pattern - Payment Methods

```java
public interface PaymentMethod {
    PaymentResult pay(BigDecimal amount);
}

public class CreditCardPayment implements PaymentMethod {
    private final String cardNumber;
    private final String cvv;

    public CreditCardPayment(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        // Credit card payment logic
        return new PaymentResult(true, "CC-" + UUID.randomUUID());
    }
}

public class PayPalPayment implements PaymentMethod {
    private final String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public PaymentResult pay(BigDecimal amount) {
        // PayPal payment logic
        return new PaymentResult(true, "PP-" + UUID.randomUUID());
    }
}

// Factory for payment methods
public class PaymentFactory {
    private static final Map<String, Supplier<PaymentMethod>> PAYMENTS = Map.of(
        "creditcard", () -> new CreditCardPayment("card", "123"),
        "paypal", () -> new PayPalPayment("user@example.com")
    );

    public static PaymentMethod create(String type) {
        Supplier<PaymentMethod> supplier = PAYMENTS.get(type.toLowerCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown payment type: " + type);
        }
        return supplier.get();
    }
}
```

### 6.3 Strategy Pattern - Discount Calculation

```java
public interface DiscountStrategy {
    BigDecimal calculate(BigDecimal amount);
}

public class NoDiscount implements DiscountStrategy {
    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount;
    }
}

public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(1 - percentage / 100));
    }
}

public class FixedAmountDiscount implements DiscountStrategy {
    private final BigDecimal discount;

    public FixedAmountDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public BigDecimal calculate(BigDecimal amount) {
        return amount.subtract(discount).max(BigDecimal.ZERO);
    }
}
```

### 6.4 Observer Pattern - Notifications

```java
public interface OrderObserver {
    void onOrderCreated(Order order);
    void onOrderPaid(Order order);
    void onOrderShipped(Order order);
}

public class EmailNotificationObserver implements OrderObserver {
    @Override
    public void onOrderCreated(Order order) {
        System.out.println("Email: Order " + order.getOrderId() + " created");
    }

    @Override
    public void onOrderPaid(Order order) {
        System.out.println("Email: Order " + order.getOrderId() + " paid");
    }

    @Override
    public void onOrderShipped(Order order) {
        System.out.println("Email: Order " + order.getOrderId() + " shipped");
    }
}

public class InventoryObserver implements OrderObserver {
    @Override
    public void onOrderCreated(Order order) {
        System.out.println("Inventory: Reserving items for " + order.getOrderId());
    }

    @Override
    public void onOrderPaid(Order order) {
        // No action needed
    }

    @Override
    public void onOrderShipped(Order order) {
        System.out.println("Inventory: Deducting items for " + order.getOrderId());
    }
}

public class OrderEventPublisher {
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    public void notifyOrderCreated(Order order) {
        observers.forEach(o -> o.onOrderCreated(order));
    }

    public void notifyOrderPaid(Order order) {
        observers.forEach(o -> o.onOrderPaid(order));
    }

    public void notifyOrderShipped(Order order) {
        observers.forEach(o -> o.onOrderShipped(order));
    }
}
```

### 6.5 Template Method - Order Processing

```java
public abstract class OrderProcessor {
    public final OrderResult process(Order order) {
        validateOrder(order);
        PaymentResult paymentResult = processPayment(order);
        if (!paymentResult.isSuccess()) {
            return OrderResult.failure("Payment failed");
        }
        updateInventory(order);
        sendNotifications(order);
        OrderResult result = createResult(order);
        logTransaction(order, result);
        return result;
    }

    protected abstract void validateOrder(Order order);

    protected abstract PaymentResult processPayment(Order order);

    protected abstract void updateInventory(Order order);

    protected void sendNotifications(Order order) {
        // Default implementation
    }

    protected abstract OrderResult createResult(Order order);

    protected abstract void logTransaction(Order order, OrderResult result);
}

public class StandardOrderProcessor extends OrderProcessor {
    private final OrderEventPublisher publisher;

    public StandardOrderProcessor(OrderEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    protected void validateOrder(Order order) {
        if (order.getCustomer() == null) {
            throw new IllegalArgumentException("Customer required");
        }
    }

    @Override
    protected PaymentResult processPayment(Order order) {
        return order.getPayment().pay(order.getTotalAmount());
    }

    @Override
    protected void updateInventory(Order order) {
        System.out.println("Updating inventory for " + order.getOrderId());
    }

    @Override
    protected void sendNotifications(Order order) {
        publisher.notifyOrderPaid(order);
    }

    @Override
    protected OrderResult createResult(Order order) {
        return OrderResult.success(order.getOrderId());
    }

    @Override
    protected void logTransaction(Order order, OrderResult result) {
        System.out.println("Transaction logged: " + order.getOrderId());
    }
}
```

### 6.6 Adapter Pattern - Payment Gateway

```java
// Target interface
public interface PaymentGateway {
    GatewayResponse processPayment(BigDecimal amount, String token);
}

// Adaptee - Third-party PayPal SDK
public class PayPalSDK {
    public PayPalResponse makePayment(double amountInCents, String apiKey) {
        return new PayPalResponse(true, "PP-" + UUID.randomUUID());
    }
}

// Adapter
public class PayPalAdapter implements PaymentGateway {
    private final PayPalSDK sdk;
    private final String apiKey;

    public PayPalAdapter(PayPalSDK sdk, String apiKey) {
        this.sdk = sdk;
        this.apiKey = apiKey;
    }

    @Override
    public GatewayResponse processPayment(BigDecimal amount, String token) {
        PayPalResponse response = sdk.makePayment(
            amount.multiply(BigDecimal.valueOf(100)).doubleValue(),
            apiKey
        );
        return new GatewayResponse(response.isSuccessful(), response.getTransactionId());
    }
}
```

### 6.7 Decorator Pattern - Logging

```java
public interface OrderService {
    Order createOrder(Order.Builder builder);
}

public class BasicOrderService implements OrderService {
    @Override
    public Order createOrder(Order.Builder builder) {
        return builder.build();
    }
}

public abstract class OrderServiceDecorator implements OrderService {
    protected final OrderService delegate;

    protected OrderServiceDecorator(OrderService delegate) {
        this.delegate = delegate;
    }
}

public class LoggingOrderServiceDecorator extends OrderServiceDecorator {
    public LoggingOrderServiceDecorator(OrderService delegate) {
        super(delegate);
    }

    @Override
    public Order createOrder(Order.Builder builder) {
        System.out.println("Creating order...");
        Order order = delegate.createOrder(builder);
        System.out.println("Order created: " + order.getOrderId());
        return order;
    }
}

public class ValidationOrderServiceDecorator extends OrderServiceDecorator {
    public ValidationOrderServiceDecorator(OrderService delegate) {
        super(delegate);
    }

    @Override
    public Order createOrder(Order.Builder builder) {
        // Validation logic
        return delegate.createOrder(builder);
    }
}
```

### 6.8 Proxy Pattern - Caching

```java
public interface OrderRepository {
    Order findById(String orderId);
    void save(Order order);
}

public class DatabaseOrderRepository implements OrderRepository {
    private final Map<String, Order> database = new HashMap<>();

    @Override
    public Order findById(String orderId) {
        System.out.println("Fetching from database: " + orderId);
        return database.get(orderId);
    }

    @Override
    public void save(Order order) {
        database.put(order.getOrderId(), order);
    }
}

public class CachingOrderRepositoryProxy implements OrderRepository {
    private final OrderRepository delegate;
    private final Map<String, Order> cache = new ConcurrentHashMap<>();

    public CachingOrderRepositoryProxy(OrderRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Order findById(String orderId) {
        Order cached = cache.get(orderId);
        if (cached != null) {
            System.out.println("Cache hit: " + orderId);
            return cached;
        }
        System.out.println("Cache miss: " + orderId);
        Order order = delegate.findById(orderId);
        if (order != null) {
            cache.put(orderId, order);
        }
        return order;
    }

    @Override
    public void save(Order order) {
        delegate.save(order);
        cache.put(order.getOrderId(), order);
    }
}
```

### 6.9 Singleton Pattern - Configuration

```java
public enum SystemConfiguration {
    INSTANCE;

    private final Map<String, String> properties = new ConcurrentHashMap<>();

    SystemConfiguration() {
        loadDefaults();
    }

    private void loadDefaults() {
        properties.put("app.name", "OrderManagementSystem");
        properties.put("app.version", "1.0.0");
        properties.put("payment.gateway", "paypal");
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
```

---

## 7. Putting It All Together

```java
public class OrderManagementApplication {
    public static void main(String[] args) {
        // Singleton - Configuration
        SystemConfiguration config = SystemConfiguration.INSTANCE;

        // Observer - Notifications
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.addObserver(new EmailNotificationObserver());
        publisher.addObserver(new InventoryObserver());

        // Proxy - Caching
        OrderRepository repository = new CachingOrderRepositoryProxy(
            new DatabaseOrderRepository()
        );

        // Decorator - Logging
        OrderService service = new LoggingOrderServiceDecorator(
            new ValidationOrderServiceDecorator(
                new BasicOrderService()
            )
        );

        // Builder - Order construction
        Order.Builder builder = new Order.Builder("ORD-001")
            .customer(new Customer("CUST-001", "John Doe"))
            .addItem(new OrderItem("ITEM-001", "Laptop", BigDecimal.valueOf(999.99)))
            .addItem(new OrderItem("ITEM-002", "Mouse", BigDecimal.valueOf(29.99)))
            .payment(PaymentFactory.create("creditcard"))
            .discount(new PercentageDiscount(10));

        // Build order
        Order order = service.createOrder(builder);

        // Template Method - Processing
        OrderProcessor processor = new StandardOrderProcessor(publisher);
        OrderResult result = processor.process(order);

        System.out.println("Order result: " + result);
    }
}
```

---

## 8. Project Structure

```
src/main/java/academy/javaengineering/patterns/miniproject/
├── model/
│   ├── Order.java
│   ├── Customer.java
│   ├── OrderItem.java
│   ├── OrderStatus.java
│   └── PaymentResult.java
├── payment/
│   ├── PaymentMethod.java
│   ├── CreditCardPayment.java
│   ├── PayPalPayment.java
│   └── PaymentFactory.java
├── discount/
│   ├── DiscountStrategy.java
│   ├── NoDiscount.java
│   ├── PercentageDiscount.java
│   └── FixedAmountDiscount.java
├── notification/
│   ├── OrderObserver.java
│   ├── EmailNotificationObserver.java
│   ├── InventoryObserver.java
│   └── OrderEventPublisher.java
├── processor/
│   ├── OrderProcessor.java
│   └── StandardOrderProcessor.java
├── adapter/
│   ├── PaymentGateway.java
│   ├── PayPalSDK.java
│   └── PayPalAdapter.java
├── decorator/
│   ├── OrderService.java
│   ├── BasicOrderService.java
│   ├── LoggingOrderServiceDecorator.java
│   └── ValidationOrderServiceDecorator.java
├── proxy/
│   ├── OrderRepository.java
│   ├── DatabaseOrderRepository.java
│   └── CachingOrderRepositoryProxy.java
├── config/
│   └── SystemConfiguration.java
└── OrderManagementApplication.java
```

---

## 9. Testing

### Unit Tests

```java
@Test
void shouldCreateOrderWithBuilder() {
    Order order = new Order.Builder("ORD-001")
        .customer(new Customer("CUST-001", "John"))
        .addItem(new OrderItem("ITEM-001", "Laptop", BigDecimal.valueOf(999.99)))
        .payment(new CreditCardPayment("card", "123"))
        .build();

    assertEquals("ORD-001", order.getOrderId());
    assertEquals(1, order.getItems().size());
}

@Test
void shouldApplyPercentageDiscount() {
    DiscountStrategy discount = new PercentageDiscount(10);
    BigDecimal result = discount.calculate(BigDecimal.valueOf(100));
    assertEquals(BigDecimal.valueOf(90), result);
}

@Test
void shouldNotifyObservers() {
    OrderEventPublisher publisher = new OrderEventPublisher();
    List<String> notifications = new ArrayList<>();
    publisher.addObserver(new OrderObserver() {
        @Override
        public void onOrderCreated(Order order) {
            notifications.add("created");
        }
        @Override
        public void onOrderPaid(Order order) {
            notifications.add("paid");
        }
        @Override
        public void onOrderShipped(Order order) {
            notifications.add("shipped");
        }
    });

    Order order = createTestOrder();
    publisher.notifyOrderCreated(order);

    assertEquals(1, notifications.size());
    assertEquals("created", notifications.get(0));
}
```

---

## 10. Extension Ideas

1. Add more payment methods (Crypto, Bank Transfer)
2. Implement loyalty points system
3. Add order tracking with Observer
4. Implement multi-currency support
5. Add A/B testing for discount strategies
6. Implement audit logging with Decorator
7. Add retry logic with Proxy
8. Implement circuit breaker pattern

---

## 11. Summary

This mini project demonstrates:

- **Builder Pattern**: Complex order construction
- **Factory Pattern**: Payment method creation
- **Strategy Pattern**: Discount and payment algorithms
- **Observer Pattern**: Event notifications
- **Template Method**: Standardized processing flow
- **Adapter Pattern**: Third-party integration
- **Decorator Pattern**: Cross-cutting concerns
- **Proxy Pattern**: Caching and access control
- **Singleton Pattern**: Global configuration

The patterns work together to create a flexible, maintainable, and extensible system.

---

## 12. References

1. Gamma, E., et al. (1994). Design Patterns
2. Bloch, J. (2018). Effective Java
3. Martin, R. C. (2017). Clean Architecture
4. Refactoring Guru: https://refactoring.guru
