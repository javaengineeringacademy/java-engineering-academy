package academy.javaengineering.generics.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generic Repository Pattern example.
 *
 * <p>This example demonstrates a type-safe repository interface
 * that works with any entity type and ID type.</p>
 */
public class RepositoryExample {

    /**
     * Generic repository interface.
     *
     * @param <T>  the entity type
     * @param <ID> the identifier type
     */
    public interface Repository<T, ID> {
        T findById(ID id);
        List<T> findAll();
        T save(T entity);
        void delete(T entity);
    }

    /**
     * In-memory implementation of the repository.
     *
     * @param <T>  the entity type
     * @param <ID> the identifier type
     */
    public static class InMemoryRepository<T, ID> implements Repository<T, ID> {
        private final Map<ID, T> store = new HashMap<>();

        @Override
        public T findById(ID id) {
            return store.get(id);
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public T save(T entity) {
            // In a real implementation, extract ID from entity
            // Here we just store it
            return entity;
        }

        @Override
        public void delete(T entity) {
            // In a real implementation, extract ID from entity
            // Here we just remove it
        }
    }

    /**
     * Simple User entity.
     */
    public static class User {
        private final Long id;
        private final String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() { return id; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "'}";
        }
    }

    public static void main(String[] args) {
        Repository<User, Long> userRepo = new InMemoryRepository<>();

        User user1 = new User(1L, "Alice");
        User user2 = new User(2L, "Bob");

        userRepo.save(user1);
        userRepo.save(user2);

        List<User> allUsers = userRepo.findAll();
        System.out.println("All users: " + allUsers);
    }
}
