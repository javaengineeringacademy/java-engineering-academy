package academy.javaengineering.patterns.enterprise.repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Database simulation repository demonstrating how a real JDBC/JPA
 * implementation would be structured. Uses an internal map to simulate
 * database storage with SQL-like operations.
 */
public class DatabaseUserRepository implements UserRepository {

    private final Map<Long, User> database = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public DatabaseUserRepository() {
        simulateConnection();
    }

    private void simulateConnection() {
        System.out.println("[DB] Connection established to simulated database");
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(sequence.getAndIncrement());
            System.out.println("[DB] INSERT user: " + user.getEmail());
        } else {
            System.out.println("[DB] UPDATE user id=" + user.getId());
        }
        database.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        System.out.println("[DB] SELECT * FROM users WHERE id = " + id);
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        System.out.println("[DB] SELECT * FROM users WHERE email = '" + email + "'");
        return database.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        System.out.println("[DB] SELECT * FROM users");
        return new ArrayList<>(database.values());
    }

    @Override
    public List<User> findByNameContaining(String namePart) {
        System.out.println("[DB] SELECT * FROM users WHERE name LIKE '%" + namePart + "%'");
        String lower = namePart.toLowerCase();
        return database.values().stream()
                .filter(u -> u.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        System.out.println("[DB] DELETE FROM users WHERE id = " + id);
        return database.remove(id) != null;
    }

    @Override
    public long count() {
        System.out.println("[DB] SELECT COUNT(*) FROM users");
        return database.size();
    }

    @Override
    public boolean existsByEmail(String email) {
        System.out.println("[DB] SELECT COUNT(*) FROM users WHERE email = '" + email + "'");
        return database.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }
}
