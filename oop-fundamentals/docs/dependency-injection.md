# Dependency Injection

## What is DI?
Providing dependencies from outside rather than creating them internally.

## Types

### Constructor Injection (Recommended)
```java
public class OrderService {
    private final PaymentProcessor processor;

    public OrderService(PaymentProcessor processor) {
        this.processor = Objects.requireNonNull(processor);
    }
}
```

### Setter Injection
```java
public class OrderService {
    private PaymentProcessor processor;

    public void setProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }
}
```

### Field Injection (Avoid)
```java
@Autowired
private PaymentProcessor processor;  // Hard to test
```

## Simple DI Container

```java
public final class DIContainer {
    private final Map<Class<?>, Object> instances = new HashMap<>();
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    public <T> void bind(Class<T> type, Class<? extends T> impl) {
        bindings.put(type, impl);
    }

    public <T> void register(Class<T> type, T instance) {
        instances.put(type, instance);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        if (instances.containsKey(type)) return (T) instances.get(type);
        Class<?> impl = bindings.get(type);
        if (impl == null) throw new IllegalStateException("No binding for " + type);
        return createInstance(impl);
    }

    private <T> T createInstance(Class<?> impl) {
        try {
            Constructor<?> ctor = impl.getDeclaredConstructors()[0];
            Object[] params = Arrays.stream(ctor.getParameterTypes())
                .map(this::resolve)
                .toArray();
            T instance = (T) ctor.newInstance(params);
            instances.put((Class<T>) impl, instance);
            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create " + impl, e);
        }
    }
}
```

## Benefits

| Benefit | Description |
|---------|-------------|
| Testability | Mock dependencies easily |
| Flexibility | Swap implementations |
| Loose Coupling | Depend on abstractions |
| Single Responsibility | Separates creation from use |

## Related Topics
← [Composition](composition-aggregation.md) | → [SOLID](solid.md)