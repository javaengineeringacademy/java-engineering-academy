package academy.javaengineering.patterns.enterprise.dao;

import java.util.Optional;

/**
 * Tests for the DAO pattern implementation.
 */
public class DAOTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=== DAO Pattern Tests ===\n");

        testCreate();
        testFindById();
        testFindByIdNotFound();
        testFindAll();
        testFindByName();
        testFindByEmailDomain();
        testUpdate();
        testDelete();
        testExists();
        testCount();

        System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
    }

    private static void testCreate() {
        UserDao dao = new UserDaoImpl();
        User user = dao.create(new User("Test", "test@test.com"));
        assertTest("Create assigns ID", user.getId() != null);
        assertTest("Create stores data", user.getName().equals("Test"));
    }

    private static void testFindById() {
        UserDao dao = new UserDaoImpl();
        User created = dao.create(new User("A", "a@b.com"));
        Optional<User> found = dao.findById(created.getId());
        assertTest("FindById found", found.isPresent());
        assertTest("FindById name", found.get().getName().equals("A"));
    }

    private static void testFindByIdNotFound() {
        UserDao dao = new UserDaoImpl();
        assertTest("FindById not found", dao.findById(999L).isEmpty());
    }

    private static void testFindAll() {
        UserDao dao = new UserDaoImpl();
        dao.create(new User("A", "a@b.com"));
        dao.create(new User("B", "b@b.com"));
        assertTest("FindAll size", dao.findAll().size() == 2);
    }

    private static void testFindByName() {
        UserDao dao = new UserDaoImpl();
        dao.create(new User("Alice", "a@b.com"));
        assertTest("FindByName found", dao.findByName("Alice").isPresent());
        assertTest("FindByName not found", dao.findByName("Nobody").isEmpty());
    }

    private static void testFindByEmailDomain() {
        UserDao dao = new UserDaoImpl();
        dao.create(new User("A", "a@example.com"));
        dao.create(new User("B", "b@test.com"));
        assertTest("FindByDomain", dao.findByEmailDomain("example.com").size() == 1);
    }

    private static void testUpdate() {
        UserDao dao = new UserDaoImpl();
        User user = dao.create(new User("Old", "old@b.com"));
        user.setName("New");
        dao.update(user);
        assertTest("Update name", dao.findById(user.getId()).get().getName().equals("New"));
    }

    private static void testDelete() {
        UserDao dao = new UserDaoImpl();
        User user = dao.create(new User("Del", "d@b.com"));
        assertTest("Delete existing", dao.delete(user.getId()));
        assertTest("Delete not found", !dao.delete(999L));
    }

    private static void testExists() {
        UserDao dao = new UserDaoImpl();
        User user = dao.create(new User("E", "e@b.com"));
        assertTest("Exists true", dao.exists(user.getId()));
        assertTest("Exists false", !dao.exists(999L));
    }

    private static void testCount() {
        UserDao dao = new UserDaoImpl();
        assertTest("Count empty", dao.count() == 0);
        dao.create(new User("A", "a@b.com"));
        assertTest("Count one", dao.count() == 1);
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
