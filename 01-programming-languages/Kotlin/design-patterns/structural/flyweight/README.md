# Flyweight Pattern (Kotlin)

## Overview

The Flyweight pattern minimizes memory usage by sharing as much data as possible with
similar objects. Kotlin's data classes and object declarations enable efficient flyweight
implementations.

## When to Use

- Application uses large number of objects
- Object state can be made extrinsic
- Memory costs are high
- Many objects can be replaced with fewer shared ones

## Kotlin Implementation

### Data Class Flyweight

```kotlin
data class Flyweight(val sharedState: String)

class FlyweightFactory {
    private val flyweights = mutableMapOf<String, Flyweight>()

    fun getFlyweight(key: String): Flyweight {
        return flyweights.getOrPut(key) {
            println("Creating flyweight for $key")
            Flyweight(key)
        }
    }

    fun getCount(): Int = flyweights.size
}
```

### Object Pool

```kotlin
class ObjectPool<T>(
    private val factory: () -> T,
    private val maxSize: Int = 100
) {
    private val pool = mutableListOf<T>()

    fun acquire(): T {
        return if (pool.isNotEmpty()) {
            pool.removeLast()
        } else {
            factory()
        }
    }

    fun release(obj: T) {
        if (pool.size < maxSize) {
            pool.add(obj)
        }
    }
}
```

### WeakReference Flyweight

```kotlin
import java.lang.ref.WeakReference

class WeakFlyweightFactory<T> {
    private val flyweights = mutableMapOf<String, WeakReference<T>>()

    fun get(key: String, factory: () -> T): T {
        return flyweights[key]?.get() ?: factory().also {
            flyweights[key] = WeakReference(it)
        }
    }
}
```

### String Interning

```kotlin
object StringInterner {
    private val interned = mutableMapOf<String, String>()

    fun intern(str: String): String {
        return interned.getOrPut(str) { str }
    }
}
```

## Best Practices

- Use data classes for value-based flyweights
- Consider object pooling for reuse
- Use WeakReference for automatic cleanup
- Document flyweight lifecycle
- Use flyweight for memory optimization

## Interview Questions

1. What is the difference between Flyweight and Singleton?
2. How do you handle thread safety in Flyweight?
3. What is intrinsic vs extrinsic state?
4. When should you use Flyweight over caching?
5. How do you manage flyweight lifecycle?

## References

- Kotlin documentation: Data classes
- "Kotlin in Action" by Svetlana Isakova
- "Object-Oriented Software Construction" by Meyer
