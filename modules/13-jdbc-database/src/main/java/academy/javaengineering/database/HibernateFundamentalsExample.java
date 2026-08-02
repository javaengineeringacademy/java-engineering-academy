package academy.javaengineering.database;

import java.util.*;

/**
 * Hibernate Fundamentals - Entity, Session, CRUD.
 */
public class HibernateFundamentalsExample {

    public static class Entity {
        private Long id;
        private String name;
        private String email;

        public Entity() {}
        public Entity(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }

    public static class SessionSimulator {
        private final Map<Long, Entity> store = new HashMap<>();

        public void save(Entity entity) { store.put(entity.getId(), entity); }
        public Entity findById(Long id) { return store.get(id); }
        public void delete(Long id) { store.remove(id); }
        public List<Entity> findAll() { return new ArrayList<>(store.values()); }
    }

    public static void main(String[] args) {
        SessionSimulator session = new SessionSimulator();
        Entity user = new Entity(1L, "John", "john@test.com");
        session.save(user);
        System.out.println("Found: " + session.findById(1L).getName());
        session.delete(1L);
        System.out.println("After delete: " + session.findById(1L));
    }
}
