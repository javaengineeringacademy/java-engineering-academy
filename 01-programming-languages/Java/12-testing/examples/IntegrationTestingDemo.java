package academy.javaengineering.testing.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Testing Demo - Spring Boot style integration tests
 * Demonstrates testing with real dependencies (database, services)
 */
class IntegrationTestingDemo {

    // ============================================
    // Simulated Spring Boot Components
    // ============================================

    // Entity
    static class User {
        private Long id;
        private String name;
        private String email;

        User() {}

        User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        // Getters and Setters
        Long getId() { return id; }
        void setId(Long id) { this.id = id; }
        String getName() { return name; }
        void setName(String name) { this.name = name; }
        String getEmail() { return email; }
        void setEmail(String email) { this.email = email; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return id != null && id.equals(user.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    // Repository Interface
    interface UserRepository {
        User save(User user);
        Optional<User> findById(Long id);
        List<User> findAll();
        void deleteById(Long id);
        Optional<User> findByEmail(String email);
    }

    // Service
    static class UserService {
        private final UserRepository userRepository;

        UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        User createUser(String name, String email) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + email);
            }
            User user = new User(name, email);
            return userRepository.save(user);
        }

        Optional<User> getUserById(Long id) {
            return userRepository.findById(id);
        }

        List<User> getAllUsers() {
            return userRepository.findAll();
        }

        void deleteUser(Long id) {
            userRepository.deleteById(id);
        }

        User updateUser(Long id, String name, String email) {
            Optional<User> existing = userRepository.findById(id);
            if (existing.isEmpty()) {
                throw new IllegalArgumentException("User not found: " + id);
            }
            User user = existing.get();
            user.setName(name);
            user.setEmail(email);
            return userRepository.save(user);
        }
    }

    // ============================================
    // In-Memory Repository (Simulating Database)
    // ============================================

    static class InMemoryUserRepository implements UserRepository {
        private final List<User> users = new ArrayList<>();
        private long nextId = 1;

        @Override
        public User save(User user) {
            if (user.getId() == null) {
                user.setId(nextId++);
            }
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
            return user;
        }

        @Override
        public Optional<User> findById(Long id) {
            return users.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(users);
        }

        @Override
        public void deleteById(Long id) {
            users.removeIf(u -> u.getId().equals(id));
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return users.stream().filter(u -> u.getEmail().equals(email)).findFirst();
        }
    }

    // ============================================
    // Integration Test - Full Service with Real Repository
    // ============================================

    static class UserServiceIntegrationTest {
        private UserRepository userRepository;
        private UserService userService;

        @BeforeEach
        void setUp() {
            userRepository = new InMemoryUserRepository();
            userService = new UserService(userRepository);
        }

        @Test
        @DisplayName("Create user - should persist and retrieve")
        void testCreateAndRetrieveUser() {
            // Act
            User created = userService.createUser("Alice", "alice@example.com");

            // Assert
            assertNotNull(created.getId());
            assertEquals("Alice", created.getName());
            assertEquals("alice@example.com", created.getEmail());

            // Verify persistence
            Optional<User> retrieved = userService.getUserById(created.getId());
            assertTrue(retrieved.isPresent());
            assertEquals("Alice", retrieved.get().getName());
        }

        @Test
        @DisplayName("Create duplicate email - should throw exception")
        void testDuplicateEmail() {
            // Arrange
            userService.createUser("Alice", "alice@example.com");

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> {
                userService.createUser("Bob", "alice@example.com");
            });
        }

        @Test
        @DisplayName("Get all users - should return all")
        void testGetAllUsers() {
            // Arrange
            userService.createUser("Alice", "alice@example.com");
            userService.createUser("Bob", "bob@example.com");

            // Act
            List<User> users = userService.getAllUsers();

            // Assert
            assertEquals(2, users.size());
        }

        @Test
        @DisplayName("Delete user - should remove from repository")
        void testDeleteUser() {
            // Arrange
            User user = userService.createUser("Alice", "alice@example.com");

            // Act
            userService.deleteUser(user.getId());

            // Assert
            assertTrue(userService.getUserById(user.getId()).isEmpty());
        }

        @Test
        @DisplayName("Update user - should modify existing user")
        void testUpdateUser() {
            // Arrange
            User user = userService.createUser("Alice", "alice@example.com");

            // Act
            User updated = userService.updateUser(user.getId(), "Alice Updated", "alice.new@example.com");

            // Assert
            assertEquals("Alice Updated", updated.getName());
            assertEquals("alice.new@example.com", updated.getEmail());
        }
    }

    // ============================================
    // Database Integration Test (Conceptual)
    // ============================================

    static class DatabaseIntegrationTest {
        /*
         * In a real Spring Boot application, you would use:
         * 
         * @SpringBootTest
         * @AutoConfigureMockMvc
         * @ActiveProfiles("test")
         * @TestPropertySource(locations = "classpath:application-test.properties")
         * 
         * Example:
         * 
         * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
         * class UserApiControllerTest {
         * 
         *     @Autowired
         *     private TestRestTemplate restTemplate;
         * 
         *     @Autowired
         *     private UserRepository userRepository;
         * 
         *     @Test
         *     void shouldCreateUser() {
         *         // Given
         *         UserDto newUser = new UserDto("John", "john@example.com");
         * 
         *         // When
         *         ResponseEntity<UserDto> response = restTemplate.postForEntity(
         *             "/api/users", newUser, UserDto.class);
         * 
         *         // Then
         *         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
         *         assertThat(response.getBody().getName()).isEqualTo("John");
         *     }
         * }
         */
        System.out.println("Database integration test concept shown in comments");
    }

    // ============================================
    // Test Configuration
    // ============================================

    static class TestConfiguration {
        // Simulating Spring's @TestConfiguration
        private static final InMemoryUserRepository testRepository = new InMemoryUserRepository();

        static UserRepository getTestRepository() {
            return testRepository;
        }

        static UserService getTestService() {
            return new UserService(testRepository);
        }

        static void resetDatabase() {
            testRepository.users.clear();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Integration Testing Demo ===\n");

        // Run integration tests
        UserServiceIntegrationTest test = new UserServiceIntegrationTest();
        
        System.out.println("--- Create and Retrieve User ---");
        test.setUp();
        test.testCreateAndRetrieveUser();
        System.out.println("PASSED\n");

        System.out.println("--- Duplicate Email ---");
        test.setUp();
        test.testDuplicateEmail();
        System.out.println("PASSED\n");

        System.out.println("--- Get All Users ---");
        test.setUp();
        test.testGetAllUsers();
        System.out.println("PASSED\n");

        System.out.println("--- Delete User ---");
        test.setUp();
        test.testDeleteUser();
        System.out.println("PASSED\n");

        System.out.println("--- Update User ---");
        test.setUp();
        test.testUpdateUser();
        System.out.println("PASSED\n");

        System.out.println("=== All integration tests passed ===");
    }
}
