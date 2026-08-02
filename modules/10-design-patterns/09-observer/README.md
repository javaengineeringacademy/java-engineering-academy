# Observer Pattern

## 1. Introduction

The Observer Pattern is a behavioral design pattern that defines a one-to-many dependency between objects so that when one object (the subject) changes state, all its dependents (observers) are notified and updated automatically. It's also known as Publish-Subscribe pattern.

The Observer pattern is particularly useful for event handling, GUI systems, notification systems, and any scenario where multiple objects need to react to state changes.

---

## 2. Learning Objectives

By the end of this topic, you will be able to:

- Implement the Observer pattern with proper loose coupling
- Understand Observable vs. Observer roles
- Handle thread safety in observer notifications
- Recognize observer usage in Java (Swing, JavaBeans, RxJava)
- Implement event-driven architectures

---

## 3. Prerequisites

- Understanding of interfaces and abstract classes
- Knowledge of collection frameworks
- Familiarity with event-driven programming
- Understanding of loose coupling principles

---

## 4. Why This Concept Exists

The Observer pattern exists because:

- **Loose coupling**: Subject doesn't know concrete observer types
- **Dynamic relationships**: Observers can be added/removed at runtime
- **Event notification**: Automatic notification of state changes
- **Single Responsibility**: Subject manages state, observers handle reactions
- **Open/Closed Principle**: New observers can be added without modifying subject

Without Observer, you'd have tight coupling between subjects and their dependents.

---

## 5. Problem Statement

Consider an order system:

```java
// BAD: Tight coupling
public class Order {
    public void place() {
        // Save to database
        saveToDatabase();

        // Notify email service (tight coupling!)
        EmailService.sendEmail("Order placed");

        // Notify inventory system (tight coupling!)
        InventoryService.updateStock();

        // Notify analytics (tight coupling!)
        AnalyticsService.track("order_placed");

        // Adding new notification requires modifying Order class
    }
}
```

**Problems:**
1. **Tight coupling**: Order depends on concrete services
2. **Violation of OCP**: Must modify Order to add new notifications
3. **Single responsibility**: Order handles placement AND notification
4. **Hard to test**: Cannot mock notification services

---

## 6. Theory

### 6.1 Observer Roles

| Role | Responsibility | Example |
|------|----------------|---------|
| Subject | Maintains list of observers, notifies them | Order |
| Observer | Receives notifications, updates itself | EmailService |
| ConcreteSubject | Stores state, sends notifications | OrderImpl |
| ConcreteObserver | Implements update behavior | OrderEmailObserver |

### 6.2 Push vs. Pull Models

| Model | Subject Sends | Observer Gets |
|-------|---------------|---------------|
| Push | Full state | Everything |
| Pull | Just notification | Requests what it needs |

### 6.3 Observer vs. Similar Patterns

| Pattern | Purpose | Relationship |
|---------|---------|--------------|
| Observer | Notify multiple objects | One-to-many |
| Mediator | Centralize communication | Many-to-many |
| Event Bus | Decouple event producers/consumers | Many-to-many |

---

## 7. Internal Working

### 7.1 Observer Notification Flow

```
1. Subject changes state
2. Subject notifies all observers
3. Each observer updates itself
4. Observers may query subject for details
```

### 7.2 Registration Flow

```
1. Observer registers with subject
2. Subject adds observer to list
3. When state changes, subject iterates observers
4. Observer calls update() on each
```

---

## 8. JVM Perspective

### 8.1 Memory Management

- Observers stored in collection (List/Set)
- Subject holds strong references to observers
- Careful with memory leaks (observers not unregistered)
- Weak references can auto-clean

### 8.2 Thread Considerations

- Notification must handle concurrent modification
- Observer update should be thread-safe
- Consider copy-on-write for observer list

---

## 9. Memory Representation

### 9.1 Observer Memory Model

```
┌─────────────────────────────────────┐
│            Subject                  │
│  - observers: List<Observer>        │
│  - state: Object                    │
│  + register(observer)               │
│  + notify()                         │
└──────────────┬──────────────────────┘
               │ notifies
     ┌─────────┼─────────┐
     ↓         ↓         ↓
┌─────────┐ ┌─────────┐ ┌─────────┐
│Observer1│ │Observer2│ │Observer3│
│ +update()│ │ +update()│ │ +update()│
└─────────┘ └─────────┘ └─────────┘
```

---

## 10. Syntax

### 10.1 Basic Observer Structure

```java
public interface Observer {
    void update(Object event);
}

public interface Subject {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers();
}

public class ConcreteSubject implements Subject {
    private final List<Observer> observers = new ArrayList<>();
    private Object state;

    @Override
    public void register(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }

    public void setState(Object state) {
        this.state = state;
        notifyObservers();
    }
}
```

---

## 11. Easy Example

### Weather Station

```java
public interface WeatherObserver {
    void update(float temperature, float humidity, float pressure);
}

public class WeatherStation {
    private final List<WeatherObserver> observers = new ArrayList<>();
    private float temperature;
    private float humidity;
    private float pressure;

    public void register(WeatherObserver observer) {
        observers.add(observer);
    }

    public void unregister(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(temperature, humidity, pressure);
        }
    }
}

public class PhoneDisplay implements WeatherObserver {
    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.printf("Phone: Temp=%.1f°C, Humidity=%.1f%%%n", temperature, humidity);
    }
}

public class DesktopDisplay implements WeatherObserver {
    @Override
    public void update(float temperature, float humidity, float pressure) {
        System.out.printf("Desktop: Temp=%.1f°F, Pressure=%.1fhPa%n",
            temperature * 9/5 + 32, pressure);
    }
}

// Usage
WeatherStation station = new WeatherStation();
station.register(new PhoneDisplay());
station.register(new DesktopDisplay());

station.setMeasurements(25.0f, 65.0f, 1013.25f);
```

---

## 12. Medium Example

### Event Bus Implementation

```java
public interface EventBus {
    <T> void subscribe(Class<T> eventType, EventConsumer<T> consumer);
    <T> void publish(T event);
    <T> void unsubscribe(Class<T> eventType, EventConsumer<T> consumer);
}

@FunctionalInterface
public interface EventConsumer<T> {
    void accept(T event);
}

public class SimpleEventBus implements EventBus {
    private final Map<Class<?>, List<EventConsumer<?>>> subscribers = new ConcurrentHashMap<>();

    @Override
    public <T> void subscribe(Class<T> eventType, EventConsumer<T> consumer) {
        subscribers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                   .add(consumer);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void publish(T event) {
        List<EventConsumer<?>> consumers = subscribers.get(event.getClass());
        if (consumers != null) {
            for (EventConsumer<?> consumer : consumers) {
                ((EventConsumer<T>) consumer).accept(event);
            }
        }
    }

    @Override
    public <T> void unsubscribe(Class<T> eventType, EventConsumer<T> consumer) {
        List<EventConsumer<?>> consumers = subscribers.get(eventType);
        if (consumers != null) {
            consumers.remove(consumer);
        }
    }
}

// Event classes
public record OrderPlacedEvent(String orderId, BigDecimal amount) {}
public record OrderShippedEvent(String orderId, String trackingNumber) {}
public record PaymentReceivedEvent(String orderId, BigDecimal amount) {}

// Usage
EventBus eventBus = new SimpleEventBus();

eventBus.subscribe(OrderPlacedEvent.class, event ->
    System.out.println("Order placed: " + event.orderId()));

eventBus.subscribe(OrderShippedEvent.class, event ->
    System.out.println("Order shipped: " + event.trackingNumber()));

eventBus.publish(new OrderPlacedEvent("ORD-001", BigDecimal.valueOf(99.99)));
eventBus.publish(new OrderShippedEvent("ORD-001", "TRACK-123"));
```

---

## 13. Hard Example

### Thread-Safe Observable with Weak References

```java
public class Observable<T> {
    private final List<WeakReference<Observer<T>>> observers = new CopyOnWriteArrayList<>();
    private T value;

    public void addObserver(Observer<T> observer) {
        observers.add(new WeakReference<>(observer));
    }

    public void removeObserver(Observer<T> observer) {
        observers.removeIf(ref -> ref.get() == null || ref.get() == observer);
    }

    public void setValue(T value) {
        this.value = value;
        notifyObservers();
    }

    public T getValue() {
        return value;
    }

    private void notifyObservers() {
        List<WeakReference<Observer<T>>> toRemove = new ArrayList<>();

        for (WeakReference<Observer<T>> ref : observers) {
            Observer<T> observer = ref.get();
            if (observer == null) {
                toRemove.add(ref);
            } else {
                observer.update(value);
            }
        }

        observers.removeAll(toRemove);
    }
}

public interface Observer<T> {
    void update(T value);
}

// Usage
Observable<String> observable = new Observable<>();

Observer<String> observer1 = System.out::println;
Observer<String> observer2 = value -> System.out.println("Observer 2: " + value);

observable.addObserver(observer1);
observable.addObserver(observer2);

observable.setValue("Hello");
observable.setValue("World");
```

---

## 14. Enterprise Example

### Order Processing Event System

```java
// Event base
public abstract class DomainEvent {
    private final String eventId;
    private final Instant timestamp;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }

    public String getEventId() { return eventId; }
    public Instant getTimestamp() { return timestamp; }
}

// Specific events
public class OrderCreatedEvent extends DomainEvent {
    private final String orderId;
    private final BigDecimal totalAmount;
    private final List<String> items;

    public OrderCreatedEvent(String orderId, BigDecimal totalAmount, List<String> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Getters
}

public class PaymentProcessedEvent extends DomainEvent {
    private final String orderId;
    private final String paymentId;
    private final boolean success;

    public PaymentProcessedEvent(String orderId, String paymentId, boolean success) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.success = success;
    }

    // Getters
}

// Observer interface
public interface EventListener<T extends DomainEvent> {
    void handle(T event);
    Class<T> getEventType();
}

// Event publisher
public class EventPublisher {
    private final Map<Class<?>, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public <T extends DomainEvent> void subscribe(EventListener<T> listener) {
        listeners.computeIfAbsent(listener.getEventType(), k -> new CopyOnWriteArrayList<>())
                 .add(listener);
    }

    public <T extends DomainEvent> void publish(T event) {
        List<EventListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) listener;
                typedListener.handle(event);
            }
        }
    }
}

// Concrete listeners
public class OrderNotificationListener implements EventListener<OrderCreatedEvent> {
    @Override
    public void handle(OrderCreatedEvent event) {
        System.out.println("Sending order confirmation for: " + event.getOrderId());
    }

    @Override
    public Class<OrderCreatedEvent> getEventType() {
        return OrderCreatedEvent.class;
    }
}

public class InventoryListener implements EventListener<OrderCreatedEvent> {
    @Override
    public void handle(OrderCreatedEvent event) {
        System.out.println("Updating inventory for order: " + event.getOrderId());
    }

    @Override
    public Class<OrderCreatedEvent> getEventType() {
        return OrderCreatedEvent.class;
    }
}

public class PaymentListener implements EventListener<PaymentProcessedEvent> {
    @Override
    public void handle(PaymentProcessedEvent event) {
        System.out.println("Payment processed for order: " + event.getOrderId());
    }

    @Override
    public Class<PaymentProcessedEvent> getEventType() {
        return PaymentProcessedEvent.class;
    }
}

// Usage
EventPublisher publisher = new EventPublisher();
publisher.subscribe(new OrderNotificationListener());
publisher.subscribe(new InventoryListener());
publisher.subscribe(new PaymentListener());

publisher.publish(new OrderCreatedEvent("ORD-001", BigDecimal.valueOf(100), List.of("Item1")));
publisher.publish(new PaymentProcessedEvent("ORD-001", "PAY-001", true));
```

---

## 15. Performance

### 15.1 Performance Metrics

| Operation | Time Complexity | Notes |
|-----------|----------------|-------|
| Register | O(1) | List.add() |
| Unregister | O(n) | List.remove() |
| Notify | O(n) | Iterate all observers |

### 15.2 Optimization Tips

1. **Copy-on-Write**: For concurrent notifications
2. **Weak references**: Auto-cleanup of observers
3. **Event filtering**: Observers subscribe to specific events
4. **Async notification**: Non-blocking observer updates

---

## 16. Best Practices

1. **Keep observers simple**: Single responsibility
2. **Handle errors gracefully**: Don't let observer failure affect others
3. **Use weak references**: Prevent memory leaks
4. **Document notification order**: If order matters
5. **Consider async notification**: For long-running observers
6. **Avoid circular notifications**: Observer updating subject
7. **Test observer behavior**: Unit test in isolation
8. **Use event classes**: Type-safe events over raw objects

---

## 17. Common Mistakes

1. **Memory leaks**: Not unregistering observers
2. **Concurrent modification**: Modifying observer list during notification
3. **Circular notifications**: Observer triggers subject update
4. **Too many observers**: Performance degradation
5. **Not handling errors**: Observer exceptions affect others

---

## 18. Pitfalls

- **Memory leaks**: Forgotten observers
- **Ordering issues**: Notification order not guaranteed
- **Error handling**: Observer exceptions
- **Performance**: Many observers slow notification
- **Testing complexity**: Mocking observers

---

## 19. Debugging Tips

1. **Log observer registration**: Track who's listening
2. **Log notifications**: Track what's being sent
3. **Use debugger**: Step through notification loop
4. **Check memory**: Monitor observer count
5. **Test error handling**: Verify observer failures handled

---

## 20. Comparison Table

| Pattern | Purpose | Coupling | Use Case |
|---------|---------|----------|----------|
| Observer | Notify multiple objects | Loose | Event systems |
| Mediator | Centralize communication | Loose | Complex interactions |
| Event Bus | Decouple producers/consumers | Very loose | Large systems |
| Callback | One-to-one notification | Medium | Simple events |

---

## 21. Decision Tree

```
Need to notify multiple objects?
├── One-to-many notification? → Observer
├── Many-to-many communication? → Mediator or Event Bus
├── Simple callback? → Callback interface
├── Complex event routing? → Event Bus
└── Synchronous requirement? → Consider direct calls
```

---

## 22. Interview Questions

### Q1: What is the Observer pattern?
**Answer**: A behavioral pattern defining a one-to-many dependency where when one object changes state, all dependents are notified automatically.

### Q2: Push vs. Pull model?
**Answer**: Push: Subject sends full state. Pull: Subject sends notification, observer requests what it needs. Pull is more flexible.

### Q3: How to prevent memory leaks?
**Answer**: Use weak references, unregister observers when done, use frameworks with lifecycle management.

### Q4: Observer vs. Mediator?
**Answer**: Observer is one-to-many (subject notifies observers). Mediator is many-to-many (objects communicate through mediator).

### Q5: Real-world examples?
**Answer**: Java Swing event listeners, JavaBeans property change listeners, RxJava observables, Spring event system.

---

## 23. Exercises

### Exercise 1: Simple Observer
Implement a stock price observer that notifies multiple displays.

### Exercise 2: Event Bus
Create a generic event bus with subscribe/publish/unsubscribe.

### Exercise 3: Thread-Safe Observer
Implement a thread-safe observer with weak references.

---

## 24. Assignments

1. **Assignment 1**: Create a notification system with observer pattern
2. **Assignment 2**: Build an event-driven order processing system
3. **Assignment 3**: Implement a reactive data binding system

---

## 25. Mini Project

### Real-Time Chat System
Create a chat system that:
- Uses observer for message notifications
- Supports multiple chat rooms
- Handles user join/leave events
- Implements typing indicators
- Is thread-safe

---

## 26. Summary

- Observer defines one-to-many dependency
- Subject notifies observers automatically
- Promotes loose coupling between objects
- Handle memory leaks with weak references
- Consider async notification for performance
- Use type-safe events over raw objects

---

## 27. References

1. Gamma, E., et al. (1994). *Design Patterns*, Chapter 5
2. Bloch, J. (2018). *Effective Java*, Item 86
3. Refactoring Guru: https://refactoring.guru/design-patterns/observer
4. Java Design Patterns: https://java-design-patterns.com/patterns/observer/
