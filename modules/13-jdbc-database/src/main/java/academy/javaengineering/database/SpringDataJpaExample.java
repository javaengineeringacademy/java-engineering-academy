package academy.javaengineering.database;

import java.util.*;

/**
 * Spring Data JPA - Repository Pattern, Query Methods.
 */
public class SpringDataJpaExample {

    public static class User {
        private Long id;
        private String name;
        private String email;
        private boolean active;

        public User(Long id, String name, String email, boolean active) {
            this.id = id; this.name = name; this.email = email; this.active = active;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public boolean isActive() { return active; }
    }

    public interface UserRepository {
        Optional<User> findById(Long id);
        List<User> findByActiveTrue();
        List<User> findByNameContaining(String name);
        Optional<User> findByEmail(String email);
    }

    public static class InMemoryUserRepo implements UserRepository {
        private final Map<Long, User> store = new HashMap<>();

        public void save(User user) { store.put(user.getId(), user); }

        @Override
        public Optional<User> findById(Long id) { return Optional.ofNullable(store.get(id)); }

        @Override
        public List<User> findByActiveTrue() {
            return store.values().stream().filter(User::isActive).toList();
        }

        @Override
        public List<User> findByNameContaining(String name) {
            return store.values().stream().filter(u -> u.getName().contains(name)).toList();
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return store.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        }
    }

    public static void main(String[] args) {
        InMemoryUserRepo repo = new InMemoryUserRepo();
        repo.save(new User(1L, "John Doe", "john@test.com", true));
        repo.save(new User(2L, "Jane Doe", "jane@test.com", false));
        System.out.println("Active: " + repo.findByActiveTrue().size());
        System.out.println("By name: " + repo.findByNameContaining("Doe").size());
    }
}
