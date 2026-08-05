# Flyweight Design Pattern

## Overview
Flyweight pattern uses sharing to support large numbers of fine-grained objects efficiently. It separates intrinsic state (shared) from extrinsic state (unique).

## When to Use
- An application uses a large number of objects
- Storage costs are high due to the quantity of objects
- Most object state can be made extrinsic
- Many groups of objects can be replaced by relatively few shared objects

## Code Example

```java
public class FlyweightFactory {
    private final Map<String, Flyweight> flyweights = new HashMap<>();

    public Flyweight getFlyweight(String type) {
        Flyweight flyweight = flyweights.get(type);
        if (flyweight == null) {
            flyweight = new ConcreteFlyweight(type, "state_" + type);
            flyweights.put(type, flyweight);
        }
        return flyweight;
    }
}
```

## Common Mistakes
- Making extrinsic state intrinsic (not sharing properly)
- Not separating intrinsic and extrinsic state correctly
- Creating flyweights for objects that are not reused

## Interview Questions
1. What is the difference between Flyweight and Singleton patterns?
2. How does Flyweight reduce memory usage?
3. When would you NOT use the Flyweight pattern?
