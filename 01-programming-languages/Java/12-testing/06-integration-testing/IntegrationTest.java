package testing;

/**
 * IntegrationTest - Spring Boot Test example
 *
 * Covers:
 * - Spring Boot Test setup
 * - @SpringBootTest annotation
 * - TestRestTemplate
 * - MockMvc for web layer testing
 * - Test profiles and configuration
 */
public class IntegrationTest {

    // Simulated Spring Boot controller
    static class UserController {
        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        public String getUser(int id) {
            String user = userService.findById(id);
            if (user == null) {
                return "User not found";
            }
            return "User: " + user;
        }

        public String createUser(String name) {
            boolean saved = userService.save(name);
            if (saved) {
                return "User created: " + name;
            }
            return "Failed to create user";
        }
    }

    // Simulated service layer
    static class UserService {
        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public String findById(int id) {
            return repository.findById(id);
        }

        public boolean save(String name) {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            repository.save(name);
            return true;
        }
    }

    // Simulated repository
    static class UserRepository {
        private java.util.Map<Integer, String> database = new java.util.HashMap<>();
        private int nextId = 1;

        public String findById(int id) {
            return database.get(id);
        }

        public void save(String name) {
            database.put(nextId++, name);
        }

        public int count() {
            return database.size();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Boot Test Concepts ===\n");

        System.out.println("Spring Boot Test Annotations:");
        System.out.println("@SpringBootTest - Full application context");
        System.out.println("@WebMvcTest - Web layer only");
        System.out.println("@DataJpaTest - JPA layer only");
        System.out.println("@RestClientTest - REST clients");
        System.out.println();

        System.out.println("Test Configuration:");
        System.out.println("@TestConfiguration - Additional beans");
        System.out.println("@MockBean - Mock external dependencies");
        System.out.println("@ActiveProfiles - Set test profile");
        System.out.println();

        // Demonstrate integration test
        System.out.println("=== Integration Test Example ===\n");
        integrationTestDemo();
    }

    static void integrationTestDemo() {
        // Setup (like @BeforeEach)
        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        UserController controller = new UserController(service);

        // Test 1: GET user
        System.out.println("Test: GET /users/1");
        String result = controller.getUser(1);
        System.out.println("Result: " + result);
        assert "User not found".equals(result) : "Should return not found";

        // Test 2: POST user
        System.out.println("\nTest: POST /users");
        result = controller.createUser("Alice");
        System.out.println("Result: " + result);
        assert "User created: Alice".equals(result) : "Should create user";

        // Test 3: GET created user
        System.out.println("\nTest: GET /users/1 (after creation)");
        result = controller.getUser(1);
        System.out.println("Result: " + result);
        assert "User: Alice".equals(result) : "Should return user";

        // Test 4: Invalid input
        System.out.println("\nTest: POST /users (empty name)");
        result = controller.createUser("");
        System.out.println("Result: " + result);
        assert "Failed to create user".equals(result) : "Should fail for empty name";

        // Verify repository state
        System.out.println("\nFinal state:");
        System.out.println("Total users: " + repository.count());
        assert repository.count() == 1 : "Should have 1 user";

        System.out.println("\n=== Test Profiles ===");
        System.out.println("Use @ActiveProfiles(\"test\") to use test configuration");
        System.out.println("application-test.properties overrides main properties");
    }
}