package academy.javaengineering.patterns.enterprise.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory repository implementation backed by a ConcurrentHashMap.
 * Suitable for testing, prototyping, and lightweight applications.
 */
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        store.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<User> findByNameContaining(String namePart) {
        String lower = namePart.toLowerCase();
        return store.values().stream()
                .filter(u -> u.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public boolean existsByEmail(String email) {
        return store.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }
}
