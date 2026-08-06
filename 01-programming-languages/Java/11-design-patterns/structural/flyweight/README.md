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

## Performance

Flyweight reduces memory from O(n) to O(k) where k is the number of distinct intrinsic states. For text editors with millions of characters but only a few font styles, memory savings are enormous. Lookup is O(1) via HashMap. The overhead of separating intrinsic/extrinsic state is minimal. String pool in Java is a built-in flyweight.

## Examples

```java
// Text character flyweight
class CharacterFlyweight {
    private final char character;
    private final String font;
    private final int size;
    
    CharacterFlyweight(char character, String font, int size) {
        this.character = character;
        this.font = font;
        this.size = size;
    }
    
    void render(int row, int col) {
        System.out.printf("'%c' at (%d,%d) in %s size %d%n",
            character, row, col, font, size);
    }
}

class FlyweightFactory {
    private final Map<String, CharacterFlyweight> cache = new HashMap<>();
    
    CharacterFlyweight get(char c, String font, int size) {
        String key = c + ":" + font + ":" + size;
        return cache.computeIfAbsent(key, 
            k -> new CharacterFlyweight(c, font, size));
    }
    
    int getCacheSize() { return cache.size(); }
}

// Usage
FlyweightFactory factory = new FlyweightFactory();
String text = "Hello World";
for (char c : text.toCharArray()) {
    factory.get(c, "Arial", 12).render(0, 0);
}
System.out.println("Flyweights created: " + factory.getCacheSize());
// Only 8 unique characters, not 11
```

## Internal Working

The flyweight factory maintains a cache of shared objects. When a request comes in, it checks the cache for an existing instance with the same intrinsic state. If found, it returns the cached instance. If not, it creates a new one and caches it. Extrinsic state (position, context) is passed as parameters to the flyweight's methods. The flyweight is immutable — all state is set in the constructor.

## Why This Concept Exists

Applications like text editors, games, and compilers create millions of similar objects. Each character in a document might have the same font and size — storing duplicate data wastes memory. Flyweight extracts shared state (intrinsic: character, font, size) into reusable objects and passes unique state (extrinsic: position, color) as method parameters. This reduces memory from O(n) to O(k).

## Pitfalls

1. **State separation complexity**: Correctly dividing intrinsic vs extrinsic state requires careful design
2. **Thread safety**: Flyweight cache must be thread-safe if accessed from multiple threads
3. **Immutability requirement**: Flyweights must be immutable — mutable shared state causes race conditions
4. **Overhead for small objects**: The factory lookup overhead may exceed savings for small object counts
5. **Testing difficulty**: Shared state makes unit testing harder — test with different extrinsic states

## References

- [Refactoring.Guru - Flyweight Pattern](https://refactoring.guru/design-patterns/flyweight)
- [Java String Pool](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html#intern())
- [Head First Design Patterns - Flyweight Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
