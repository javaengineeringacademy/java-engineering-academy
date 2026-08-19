package academy.javaengineering.patterns.enterprise.repository;

import java.util.Optional;

/**
 * Demonstrates the Repository pattern with both in-memory and
 * database-backed implementations.
 */
public class RepositoryExample {

    public static void main(String[] args) {
        System.out.println("=== Repository Pattern Demo ===\n");

        System.out.println("--- In-Memory Repository ---");
        UserRepository inMemoryRepo = new InMemoryUserRepository();
        demonstrateRepository(inMemoryRepo);

        System.out.println("\n--- Database Repository ---");
        UserRepository dbRepo = new DatabaseUserRepository();
        demonstrateRepository(dbRepo);
    }

    private static void demonstrateRepository(UserRepository repo) {
        repo.save(new User("alice@example.com", "Alice"));
        repo.save(new User("bob@example.com", "Bob"));
        repo.save(new User("charlie@example.com", "Charlie"));

        System.out.println("All users: " + repo.findAll());
        System.out.println("Count: " + repo.count());

        Optional<User> found = repo.findByEmail("bob@example.com");
        found.ifPresent(u -> System.out.println("Found: " + u));

        System.out.println("Contains 'li': " + repo.findByNameContaining("li"));
        System.out.println("Email exists: " + repo.existsByEmail("alice@example.com"));

        repo.deleteById(1L);
        System.out.println("After delete: " + repo.findAll());
    }
}
