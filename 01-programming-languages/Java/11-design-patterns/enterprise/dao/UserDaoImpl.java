package academy.javaengineering.patterns.enterprise.dao;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * DAO implementation simulating JDBC-style database operations.
 * Demonstrates the data access layer with SQL-like semantics.
 */
public class UserDaoImpl implements UserDao {

    private final Map<Long, User> table = new LinkedHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong(1);

    @Override
    public User create(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null) {
            user.setId(autoIncrement.getAndIncrement());
        }
        System.out.println("[DAO] INSERT INTO users (id, name, email) VALUES ("
                + user.getId() + ", '" + user.getName() + "', '" + user.getEmail() + "')");
        table.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        System.out.println("[DAO] SELECT * FROM users WHERE id = " + id);
        return Optional.ofNullable(table.get(id));
    }

    @Override
    public List<User> findAll() {
        System.out.println("[DAO] SELECT * FROM users");
        return new ArrayList<>(table.values());
    }

    @Override
    public Optional<User> findByName(String name) {
        System.out.println("[DAO] SELECT * FROM users WHERE name = '" + name + "'");
        return table.values().stream()
                .filter(u -> u.getName().equals(name))
                .findFirst();
    }

    @Override
    public List<User> findByEmailDomain(String domain) {
        System.out.println("[DAO] SELECT * FROM users WHERE email LIKE '%@" + domain + "'");
        return table.values().stream()
                .filter(u -> u.getEmail().endsWith("@" + domain))
                .collect(Collectors.toList());
    }

    @Override
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User with ID required for update");
        }
        if (!table.containsKey(user.getId())) {
            throw new NoSuchElementException("User not found with id: " + user.getId());
        }
        System.out.println("[DAO] UPDATE users SET name='" + user.getName()
                + "', email='" + user.getEmail() + "' WHERE id=" + user.getId());
        table.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        System.out.println("[DAO] DELETE FROM users WHERE id = " + id);
        return table.remove(id) != null;
    }

    @Override
    public boolean exists(Long id) {
        System.out.println("[DAO] SELECT 1 FROM users WHERE id = " + id);
        return table.containsKey(id);
    }

    @Override
    public int count() {
        System.out.println("[DAO] SELECT COUNT(*) FROM users");
        return table.size();
    }
}
