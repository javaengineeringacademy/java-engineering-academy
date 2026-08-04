# Observer Pattern

The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified automatically.

## Table of Contents

1. [Concepts](#concepts)
2. [Basic Observer](#basic-observer)
3. [Event System](#event-system)
4. [Best Practices](#best-practices)
5. [Key Takeaways](#key-takeaways)

---

## Concepts

### What is Observer?

Observer defines a subscription mechanism to notify multiple objects about events.

```
Subject ◀── Observer1
   │
   └──── Observer2
   └──── Observer3
```

### When to Use

- Change notification to unknown number of objects
- Loose coupling between subject and observers
- Event-driven systems

---

## Basic Observer

### Weather Station

```java
// Observer interface
public interface WeatherObserver {
    void update(double temperature, double humidity);
}

// Subject
public class WeatherStation {
    private final List<WeatherObserver> observers = new ArrayList<>();
    private double temperature;
    private double humidity;

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void setMeasurements(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
        notifyObservers();
    }

    private void notifyObservers() {
        observers.forEach(o -> o.update(temperature, humidity));
    }
}

// Concrete observers
public class PhoneDisplay implements WeatherObserver {
    @Override
    public void update(double temperature, double humidity) {
        System.out.println("Phone: " + temperature + "°C, " + humidity + "% humidity");
    }
}

public class DesktopDisplay implements WeatherObserver {
    @Override
    public void update(double temperature, double humidity) {
        System.out.println("Desktop: " + temperature + "°C");
    }
}

// Usage
WeatherStation station = new WeatherStation();
station.addObserver(new PhoneDisplay());
station.addObserver(new DesktopDisplay());
station.setMeasurements(25.0, 65.0);
// Both observers notified
```

---

## Event System

### Generic Event Emitter

```java
public class EventBus {
    private final Map<String, List<Consumer<?>>> listeners = new HashMap<>();

    public <T> void on(String event, Consumer<T> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void emit(String event, T data) {
        List<Consumer<?>> eventListeners = listeners.getOrDefault(event, List.of());
        eventListeners.forEach(listener -> ((Consumer<T>) listener).accept(data));
    }

    public void off(String event) {
        listeners.remove(event);
    }
}

// Usage
EventBus bus = new EventBus();
bus.on("order.created", (Order order) ->
    System.out.println("Order created: " + order.getId()));
bus.on("order.shipped", (Order order) ->
    System.out.println("Order shipped: " + order.getId()));

bus.emit("order.created", new Order("123"));
```

---

## Best Practices

### Do

```java
// 1. Use interfaces for observers
public interface Observer<T> {
    void onEvent(T event);
}

// 2. Support removal
public void removeObserver(Observer<T> observer) {
    observers.remove(observer);
}
```

### Don't

```java
// 1. Don't cause memory leaks
// Remove observers when done

// 2. Don't cause stack overflow
// Don't modify observer list during notification
```

---

## Key Takeaways

| Concept | Key Point |
|---------|-----------|
| **Observer** | One-to-many dependency |
| **Subject** | Notifies observers |
| **Observer** | Receives notifications |
| **Loose Coupling** | Subject doesn't know observer details |
| **Event-Driven** | Reactive programming foundation |
| **Use Cases** | GUI, pub/sub, reactive systems |
