# Service Locator Pattern

## Overview

The Service Locator Pattern provides a centralized registry for obtaining services and dependencies. Instead of each component creating or receiving its dependencies directly, they query a central locator to obtain references to the services they need.

While dependency injection is generally preferred for most applications, the Service Locator Pattern remains useful in specific scenarios where a central registry simplifies service discovery.

## When to Use

- Building plugin architectures where services are discovered dynamically
- Legacy systems where dependency injection frameworks are not available
- Simple applications where DI infrastructure feels excessive
- Runtime service selection based on configuration
- Frameworks that need to provide services to loosely-coupled components

## Implementation

### TypeScript

```typescript
class ServiceLocator {
  private static instance: ServiceLocator;
  private services: Map<string, any> = new Map();
  private factories: Map<string, () => any> = new Map();

  static getInstance(): ServiceLocator {
    if (!ServiceLocator.instance) {
      ServiceLocator.instance = new ServiceLocator();
    }
    return ServiceLocator.instance;
  }

  register<T>(name: string, service: T): void {
    this.services.set(name, service);
  }

  registerFactory<T>(name: string, factory: () => T): void {
    this.factories.set(name, factory);
  }

  resolve<T>(name: string): T {
    if (this.services.has(name)) {
      return this.services.get(name) as T;
    }
    if (this.factories.has(name)) {
      const service = this.factories.get(name)!() as T;
      this.services.set(name, service);
      return service;
    }
    throw new Error(`Service not found: ${name}`);
  }
}

// Usage
const locator = ServiceLocator.getInstance();
locator.register('database', new PostgresClient());
locator.registerFactory('userRepository', () => new UserRepository(locator.resolve('database')));

const userRepo = locator.resolve<UserRepository>('userRepository');
```

### Java

```java
public class ServiceLocator {
    private static final ServiceLocator INSTANCE = new ServiceLocator();
    private final Map<String, Object> services = new ConcurrentHashMap<>();

    private ServiceLocator() {}

    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    public <T> void register(String name, T service) {
        services.put(name, service);
    }

    @SuppressWarnings("unchecked")
    public <T> T resolve(String name) {
        T service = (T) services.get(name);
        if (service == null) {
            throw new RuntimeException("Service not found: " + name);
        }
        return service;
    }
}
```

### Python

```python
class ServiceLocator:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._services = {}
            cls._instance._factories = {}
        return cls._instance

    def register(self, name: str, service):
        self._services[name] = service

    def register_factory(self, name: str, factory):
        self._factories[name] = factory

    def resolve(self, name: str):
        if name in self._services:
            return self._services[name]
        if name in self._factories:
            service = self._factories[name]()
            self._services[name] = service
            return service
        raise KeyError(f'Service not found: {name}')
```

### C\#

```csharp
public class ServiceLocator {
    private static readonly Lazy<ServiceLocator> _instance =
        new Lazy<ServiceLocator>(() => new ServiceLocator());

    public static ServiceLocator Instance => _instance.Value;

    private readonly Dictionary<string, object> _services = new();

    private ServiceLocator() { }

    public void Register<T>(string name, T service) {
        _services[name] = service;
    }

    public T Resolve<T>(string name) {
        if (_services.TryGetValue(name, out var service))
            return (T)service;
        throw new KeyNotFoundException($"Service not found: {name}");
    }
}
```

## Best Practices

- Prefer dependency injection over service locator for new development
- Use service locator mainly for framework-level service discovery
- Keep the service registry centralized and avoid scattered registrations
- Consider thread safety for concurrent service resolution
- Register services at application startup, not at runtime hot paths
- Document which services are available and their expected lifetimes

## Interview Questions

1. How does Service Locator differ from Dependency Injection?
2. What are the testability implications of using a Service Locator?
3. When might Service Locator be preferred over dependency injection?
4. How do you handle circular dependencies with a service locator?
5. What thread safety concerns exist with a singleton service locator?

## References

- Fowler, Martin. *Inversion of Control Containers and the Dependency Injection Pattern*
- Martin Fowler. *Service Locator*
- Gamma, Erich. *Design Patterns*, chapter on Behavioral Patterns
- Microsoft. *Service Locator Pattern*
