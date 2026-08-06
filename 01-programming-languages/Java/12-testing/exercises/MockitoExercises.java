package academy.javaengineering.exercises;

import java.util.*;
import java.util.function.*;

/**
 * Exercises: Mocking, Stubbing, and Verification Concepts
 *
 * Complete the TODO sections below.
 * These exercises implement mocking concepts without a framework.
 */
public class MockitoExercises {

    // TODO 1: Implement a simple Mock framework
    public static class Mock<T> {
        private final T realObject;
        private final Map<String, List<Object[]>> invocations = new LinkedHashMap<>();
        private final Map<String, Object> stubs = new HashMap<>();

        public Mock(T realObject) {
            this.realObject = realObject;
        }

        public void when(String method, Object... args) {
            // TODO: record invocation for stubbing
        }

        public void thenReturn(Object value) {
            // TODO: set return value for last when() call
        }

        public void recordInvocation(String method, Object[] args) {
            // TODO: record a method invocation
        }

        public int getInvocationCount(String method) {
            // TODO: return count of invocations for method
            return 0;
        }

        public List<Object[]> getInvocations(String method) {
            // TODO: return all invocations for method
            return new ArrayList<>();
        }

        public Object getStub(String method) {
            return stubs.get(method);
        }
    }

    // TODO 2: Implement a UserRepository interface and mock it
    public interface UserRepository {
        Optional<String> findById(String id);
        List<String> findAll();
        void save(String id, String data);
        boolean exists(String id);
    }

    public static class InMemoryUserRepository implements UserRepository {
        private final Map<String, String> store = new HashMap<>();

        @Override
        public Optional<String> findById(String id) {
            return Optional.ofNullable(store.get(id));
        }

        @Override
        public List<String> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void save(String id, String data) {
            store.put(id, data);
        }

        @Override
        public boolean exists(String id) {
            return store.containsKey(id);
        }
    }

    // TODO 3: Implement a UserService that uses UserRepository
    public static class UserService {
        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public String getUserName(String id) {
            // TODO: implement - return name or "Unknown"
            return "Unknown";
        }

        public boolean registerUser(String id, String name) {
            // TODO: implement - check exists, save if not
            return false;
        }

        public List<String> getAllUserNames() {
            // TODO: implement - return all user names
            return new ArrayList<>();
        }
    }

    // TODO 4: Implement Spy pattern - track real calls but allow overrides
    public static class SpyUserRepository implements UserRepository {
        private final UserRepository real;
        private final List<String> methodCalls = new ArrayList<>();

        public SpyUserRepository(UserRepository real) {
            this.real = real;
        }

        @Override
        public Optional<String> findById(String id) {
            methodCalls.add("findById:" + id);
            return real.findById(id);
        }

        @Override
        public List<String> findAll() {
            methodCalls.add("findAll");
            return real.findAll();
        }

        @Override
        public void save(String id, String data) {
            methodCalls.add("save:" + id);
            real.save(id, data);
        }

        @Override
        public boolean exists(String id) {
            methodCalls.add("exists:" + id);
            return real.exists(id);
        }

        public List<String> getMethodCalls() {
            return new ArrayList<>(methodCalls);
        }

        public int getCallCount(String method) {
            return (int) methodCalls.stream()
                .filter(c -> c.startsWith(method))
                .count();
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        MockitoExercises exercises = new MockitoExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== MockitoExercises Tests ===\n");

        // Test 2 - Real repository
        total++;
        UserRepository repo = new InMemoryUserRepository();
        repo.save("1", "Alice");
        repo.save("2", "Bob");
        if (repo.findById("1").orElse("").equals("Alice") && repo.findAll().size() == 2) {
            System.out.println("Test 2 PASSED: InMemoryUserRepository");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: InMemoryUserRepository");
        }

        // Test 3 - UserService
        total++;
        UserService service = new UserService(repo);
        String name = service.getUserName("1");
        if ("Alice".equals(name)) {
            System.out.println("Test 3a PASSED: UserService.getUserName");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: UserService.getUserName - " + name);
        }

        total++;
        boolean registered = service.registerUser("3", "Charlie");
        if (registered && service.getUserName("3").equals("Charlie")) {
            System.out.println("Test 3b PASSED: UserService.registerUser");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: UserService.registerUser");
        }

        total++;
        boolean duplicate = service.registerUser("1", "Duplicate");
        if (!duplicate) {
            System.out.println("Test 3c PASSED: UserService.registerUser duplicate");
            passed++;
        } else {
            System.out.println("Test 3c FAILED: UserService.registerUser should reject duplicate");
        }

        total++;
        List<String> all = service.getAllUserNames();
        if (all.size() == 2 && all.contains("Alice") && all.contains("Bob")) {
            System.out.println("Test 3d PASSED: UserService.getAllUserNames");
            passed++;
        } else {
            System.out.println("Test 3d FAILED: UserService.getAllUserNames - " + all);
        }

        // Test 4 - Spy
        total++;
        UserRepository realRepo = new InMemoryUserRepository();
        realRepo.save("1", "Alice");
        SpyUserRepository spyRepo = new SpyUserRepository(realRepo);
        spyRepo.findById("1");
        spyRepo.findAll();
        spyRepo.exists("2");
        if (spyRepo.getCallCount("findById") == 1
            && spyRepo.getCallCount("findAll") == 1
            && spyRepo.getMethodCalls().size() == 3) {
            System.out.println("Test 4 PASSED: SpyUserRepository tracking");
            passed++;
        } else {
            System.out.println("Test 4 FAILED: SpyUserRepository tracking - " + spyRepo.getMethodCalls());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
