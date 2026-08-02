package academy.javaengineering.springboot;

import java.util.HashMap;
import java.util.Map;

/**
 * Data JPA Starter - JPA Repository, Entity, Auditing.
 */
public class DataJpaStarterExample {

    public static class Entity {
        private Long id;
        private String name;
        private java.time.LocalDateTime createdAt;

        public Entity(Long id, String name) {
            this.id = id;
            this.name = name;
            this.createdAt = java.time.LocalDateTime.now();
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    }

    public interface CrudRepository<T> {
        void save(T entity);
        T findById(Long id);
        void delete(Long id);
    }

    public static class SimpleRepository implements CrudRepository<Entity> {
        private final Map<Long, Entity> store = new HashMap<>();

        @Override public void save(Entity entity) { store.put(entity.getId(), entity); }
        @Override public Entity findById(Long id) { return store.get(id); }
        @Override public void delete(Long id) { store.remove(id); }
    }

    public static void main(String[] args) {
        CrudRepository<Entity> repo = new SimpleRepository();
        Entity user = new Entity(1L, "John");
        repo.save(user);
        System.out.println("Found: " + repo.findById(1L).getName());
    }
}
