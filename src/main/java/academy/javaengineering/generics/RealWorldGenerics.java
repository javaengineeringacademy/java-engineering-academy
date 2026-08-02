package academy.javaengineering.generics;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * Topic 08: Real-World Applications.
 *
 * <p>This class demonstrates generic patterns used in production
 * code, including Repository, Builder, and Event Bus patterns.</p>
 */
public final class RealWorldGenerics {

    private RealWorldGenerics() {
    }

    /**
     * Generic Repository interface.
     */
    public interface Repository<T, ID> {
        T findById(ID id);
        List<T> findAll();
        void save(T entity);
        void update(T entity);
        void delete(ID id);
        long count();
    }

    /**
     * In-memory repository implementation.
     */
    public static class InMemoryRepository<T, ID> implements Repository<T, ID> {
        private final Map<ID, T> store = new HashMap<>();
        private final Function<T, ID> idExtractor;

        public InMemoryRepository(Function<T, ID> idExtractor) {
            this.idExtractor = idExtractor;
        }

        @Override
        public T findById(ID id) {
            return store.get(id);
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void save(T entity) {
            ID id = idExtractor.apply(entity);
            store.put(id, entity);
        }

        @Override
        public void update(T entity) {
            ID id = idExtractor.apply(entity);
            if (!store.containsKey(id)) {
                throw new IllegalArgumentException("Entity not found: " + id);
            }
            store.put(id, entity);
        }

        @Override
        public void delete(ID id) {
            store.remove(id);
        }

        @Override
        public long count() {
            return store.size();
        }
    }

    /**
     * Generic Builder pattern.
     */
    public static class Builder<T> {
        private final Class<T> type;
        private final Map<String, Object> values = new HashMap<>();

        public Builder(Class<T> type) {
            this.type = type;
        }

        public <V> Builder<T> set(String field, V value) {
            values.put(field, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {
            try {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Map.Entry<String, Object> entry : values.entrySet()) {
                    java.lang.reflect.Field field = type.getDeclaredField(entry.getKey());
                    field.setAccessible(true);
                    field.set(instance, entry.getValue());
                }
                return instance;
            } catch (Exception e) {
                throw new RuntimeException("Build failed for " + type.getName(), e);
            }
        }

        public static <T> Builder<T> of(Class<T> type) {
            return new Builder<>(type);
        }
    }

    /**
     * Type-safe Event Bus.
     */
    public static class TypeSafeEventBus {
        private final Map<Class<?>, List<EventHandler<?>>> handlers = new ConcurrentHashMap<>();

        @FunctionalInterface
        public interface EventHandler<T> {
            void handle(T event);
        }

        public <T> void register(Class<T> eventType, EventHandler<T> handler) {
            handlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                    .add(handler);
        }

        @SuppressWarnings("unchecked")
        public <T> void post(T event) {
            List<EventHandler<?>> eventHandlers = handlers.get(event.getClass());
            if (eventHandlers != null) {
                for (EventHandler<?> handler : eventHandlers) {
                    ((EventHandler<T>) handler).handle(event);
                }
            }
        }
    }

    // Example domain classes
    public record User(Long id, String name) {}

    public record UserCreatedEvent(String userId, String name) {}

    public record UserDeletedEvent(String userId) {}

    /**
     * Demonstrates real-world generic patterns.
     */
    public static void main(String[] args) {
        // Repository pattern
        Repository<User, Long> userRepo = new InMemoryRepository<>(User::id);
        userRepo.save(new User(1L, "Alice"));
        userRepo.save(new User(2L, "Bob"));

        System.out.println("Find by ID: " + userRepo.findById(1L));
        System.out.println("Find all: " + userRepo.findAll());
        System.out.println("Count: " + userRepo.count());

        // Builder pattern
        User user = Builder.of(User.class)
                .set("id", 3L)
                .set("name", "Charlie")
                .build();
        System.out.println("Built user: " + user);

        // Event Bus
        TypeSafeEventBus eventBus = new TypeSafeEventBus();
        eventBus.register(UserCreatedEvent.class, e ->
                System.out.println("User created: " + e.name()));
        eventBus.register(UserDeletedEvent.class, e ->
                System.out.println("User deleted: " + e.userId()));

        eventBus.post(new UserCreatedEvent("U001", "David"));
        eventBus.post(new UserDeletedEvent("U001"));
    }
}
