package academy.javaengineering.patterns.enterprise.dao;

import java.util.Optional;

/**
 * Demonstrates the DAO pattern with CRUD operations.
 */
public class DAOExample {

    public static void main(String[] args) {
        System.out.println("=== DAO Pattern Demo ===\n");

        UserDao dao = new UserDaoImpl();

        // Create
        System.out.println("--- Create ---");
        User alice = dao.create(new User("Alice", "alice@example.com"));
        User bob = dao.create(new User("Bob", "bob@test.com"));
        User charlie = dao.create(new User("Charlie", "charlie@example.com"));
        System.out.println("Created: " + alice);
        System.out.println("Created: " + bob);
        System.out.println("Created: " + charlie);

        // Read
        System.out.println("\n--- Read ---");
        Optional<User> found = dao.findById(alice.getId());
        found.ifPresent(u -> System.out.println("Found: " + u));

        System.out.println("All users: " + dao.findAll());
        System.out.println("Count: " + dao.count());

        // Query
        System.out.println("\n--- Query ---");
        dao.findByName("Bob").ifPresent(u -> System.out.println("By name: " + u));
        System.out.println("By domain: " + dao.findByEmailDomain("example.com"));

        // Update
        System.out.println("\n--- Update ---");
        alice.setEmail("alice.updated@example.com");
        dao.update(alice);
        dao.findById(alice.getId()).ifPresent(u -> System.out.println("Updated: " + u));

        // Delete
        System.out.println("\n--- Delete ---");
        System.out.println("Exists before: " + dao.exists(bob.getId()));
        dao.delete(bob.getId());
        System.out.println("Exists after: " + dao.exists(bob.getId()));
        System.out.println("Count after delete: " + dao.count());
    }
}
