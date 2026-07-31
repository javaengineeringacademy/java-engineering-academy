package academy.javaengineering.oop.dependencyinjection;

/**
 * ServiceContainer - Simple DI container simulation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceContainer {

    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

    public ServiceContainer() {
        // Register default implementations
        register(UserService.class, new UserServiceImpl());
    }

    public <T> void register(Class<T> type, T implementation) {
        services.put(type, implementation);
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> type) {
        T service = (T) services.get(type);
        if (service == null) {
            throw new IllegalArgumentException("No service registered for: " + type.getSimpleName());
        }
        return service;
    }
}