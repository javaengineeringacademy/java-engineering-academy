# Observer Pattern

## Overview
The Observer pattern defines a one-to-many dependency between objects so that when one object changes state, all its dependents are notified and updated automatically. It implements a publish-subscribe mechanism.

## When to Use
- When changes to one object require changing others, and you don't know how many objects need to be changed
- When an object should notify other objects without being tightly coupled to them
- Event handling systems, GUI frameworks, message queues, stock price updates

## Code Structure
```
Subject (interface)          Observer (interface)
    |                             |
NewsAgency (concrete)        NewsReader (concrete)
```

## Key Benefits
- Loose coupling between subject and observers
- Dynamic relationships at runtime
- Open/Closed Principle: new observers can be added without modifying the subject

## Common Mistakes
- Memory leaks from not properly detaching observers
- Circular dependencies between subjects and observers
- Cascading updates causing performance issues

## Interview Questions
1. What is the difference between Observer and Mediator?
2. How does the Observer pattern relate to the Publish-Subscribe model?
3. What problems can occur with notification order?
4. How do you prevent memory leaks in Observer pattern?

## Performance

Notification cost is O(n) per observer list iteration. For thousands of observers, consider batched notifications or async delivery. Observer registration/deregistration is O(1) with a HashSet, O(n) with a List. In high-frequency event systems, use a concurrent copy-on-write list to avoid lock contention during iteration.

## Examples

```java
interface StockObserver {
    void update(String stock, double price);
}

class StockMarket {
    private final List<StockObserver> observers = new ArrayList<>();
    private final Map<String, Double> prices = new HashMap<>();
    
    void addObserver(StockObserver observer) {
        observers.add(observer);
    }
    
    void removeObserver(StockObserver observer) {
        observers.remove(observer);
    }
    
    void setPrice(String stock, double price) {
        prices.put(stock, price);
        notifyObservers(stock, price);
    }
    
    private void notifyObservers(String stock, double price) {
        for (StockObserver observer : observers) {
            observer.update(stock, price);
        }
    }
}

class MobileApp implements StockObserver {
    @Override
    public void update(String stock, double price) {
        System.out.println("Mobile alert: " + stock + " now $" + price);
    }
}

class TradingBot implements StockObserver {
    @Override
    public void update(String stock, double price) {
        if (price > 100) System.out.println("Bot buying " + stock);
    }
}

// Usage
StockMarket market = new StockMarket();
market.addObserver(new MobileApp());
market.addObserver(new TradingBot());
market.setPrice("AAPL", 150.0); // Both notified
```

## Internal Working

The subject maintains a list of observer references. When state changes, it iterates the list and calls each observer's update method. Java's `PropertyChangeListener` and `EventListenerList` use this pattern. Swing/AWT event dispatch uses an observer-like mechanism with event queues. The key is that the subject does not know the concrete type of observers — it only depends on the observer interface.

## Why This Concept Exists

Many objects need to react to state changes in other objects without tight coupling. GUI frameworks need to notify buttons when data changes. Message queues need to fan out events. Stock tickers need to push price updates. Observer decouples the event source from consumers, enabling open/closed principle — new observers can be added without modifying the subject.

## Pitfalls

1. **Memory leaks**: Forgetting to remove observers causes memory leaks, especially in long-lived subjects
2. **Update order**: Notification order is not guaranteed; observers should not depend on each other
3. **Cascading updates**: Observer A triggers subject update which triggers observer B which triggers A — infinite loop
4. **Thread safety**: Subject and observer list must be synchronized if accessed from multiple threads
5. **Granularity**: Fine-grained notifications flood observers; coarse-grained ones waste cycles

## References

- [Refactoring.Guru - Observer Pattern](https://refactoring.guru/design-patterns/observer)
- [Oracle Java Documentation - PropertyChangeListener](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/java/beans/PropertyChangeListener.html)
- [Head First Design Patterns - Observer Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
