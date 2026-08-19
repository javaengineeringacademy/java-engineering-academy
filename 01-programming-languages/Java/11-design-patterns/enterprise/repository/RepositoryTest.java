package academy.javaengineering.patterns.enterprise.repository;

import java.util.Optional;

/**
 * Tests for the Repository pattern implementations.
 */
public class RepositoryTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== Repository Pattern Tests ===\n");

        testInMemorySave();
        testInMemoryFindById();
        testInMemoryFindByEmail();
        testInMemoryFindAll();
        testInMemoryFindByNameContaining();
        testInMemoryDeleteById();
        testInMemoryCount();
        testInMemoryExistsByEmail();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testInMemorySave() {
        UserRepository repo = new InMemoryUserRepository();
        User user = repo.save(new User("test@test.com", "Test"));
        assertTest("Save assigns ID", user.getId() != null);
        assertTest("Save stores user", repo.findById(user.getId()).isPresent());
    }

    private static void testInMemoryFindById() {
        UserRepository repo = new InMemoryUserRepository();
        User saved = repo.save(new User("a@b.com", "A"));
        Optional<User> found = repo.findById(saved.getId());
        assertTest("FindById returns user", found.isPresent());
        assertTest("FindById wrong ID empty", repo.findById(999L).isEmpty());
    }

    private static void testInMemoryFindByEmail() {
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new User("x@y.com", "X"));
        assertTest("FindByEmail found", repo.findByEmail("x@y.com").isPresent());
        assertTest("FindByEmail not found", repo.findByEmail("no@no.com").isEmpty());
    }

    private static void testInMemoryFindAll() {
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new User("a@b.com", "A"));
        repo.save(new User("c@d.com", "C"));
        assertTest("FindAll size", repo.findAll().size() == 2);
    }

    private static void testInMemoryFindByNameContaining() {
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new User("a@b.com", "Alice"));
        repo.save(new User("c@d.com", "Bob"));
        assertTest("FindByNameContaining", repo.findByNameContaining("Ali").size() == 1);
    }

    private static void testInMemoryDeleteById() {
        UserRepository repo = new InMemoryUserRepository();
        User user = repo.save(new User("a@b.com", "A"));
        assertTest("DeleteById existing", repo.deleteById(user.getId()));
        assertTest("DeleteById non-existing", !repo.deleteById(999L));
    }

    private static void testInMemoryCount() {
        UserRepository repo = new InMemoryUserRepository();
        assertTest("Count empty", repo.count() == 0);
        repo.save(new User("a@b.com", "A"));
        assertTest("Count after save", repo.count() == 1);
    }

    private static void testInMemoryExistsByEmail() {
        UserRepository repo = new InMemoryUserRepository();
        repo.save(new User("a@b.com", "A"));
        assertTest("ExistsByEmail true", repo.existsByEmail("a@b.com"));
        assertTest("ExistsByEmail false", !repo.existsByEmail("x@y.com"));
    }

    private static void assertTest(String name, boolean condition) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }
}
