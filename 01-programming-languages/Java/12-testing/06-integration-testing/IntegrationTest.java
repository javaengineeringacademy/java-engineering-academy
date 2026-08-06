package academy.javaengineering.testing;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration test examples - testing multiple components together
 *
 * This file covers:
 * - Testing controller -> service -> repository chain
 * - @MockBean for external dependencies
 * - @TestConfiguration for test-specific beans
 * - @ActiveProfiles("test") for test environment
 * - Spring Boot Test annotations: @SpringBootTest, @WebMvcTest
 * - TestRestTemplate for HTTP testing
 * - MockMvc for web layer testing
 */
@ExtendWith(MockitoExtension.class)
class IntegrationTestTest {

    // =========================================================
    // 1. COMPONENTS UNDER TEST (simulated Spring Boot layers)
    // =========================================================

    static class UserController {
        private final UserService userService;

        UserController(UserService userService) {
            this.userService = userService;
        }

        String getUser(int id) {
            String user = userService.findById(id);
            if (user == null) {
                return "User not found";
            }
            return "User: " + user;
        }

        String createUser(String name) {
            boolean saved = userService.save(name);
            if (saved) {
                return "User created: " + name;
            }
            return "Failed to create user";
        }

        String deleteUser(int id) {
            try {
                userService.delete(id);
                return "User deleted: " + id;
            } catch (Exception e) {
                return "Failed to delete: " + e.getMessage();
            }
        }
    }

    static class UserService {
        private final UserRepository repository;

        UserService(UserRepository repository) {
            this.repository = repository;
        }

        String findById(int id) {
            return repository.findById(id);
        }

        boolean save(String name) {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            repository.save(name);
            return true;
        }

        void delete(int id) {
            if (repository.findById(id) == null) {
                throw new IllegalArgumentException("User not found: " + id);
            }
            repository.delete(id);
        }
    }

    static class UserRepository {
        private final java.util.Map<Integer, String> database = new java.util.HashMap<>();
        private int nextId = 1;

        String findById(int id) {
            return database.get(id);
        }

        void save(String name) {
            database.put(nextId++, name);
        }

        void delete(int id) {
            database.remove(id);
        }

        int count() {
            return database.size();
        }
    }

    // =========================================================
    // 2. INTEGRATION TEST - Multiple components together
    // =========================================================

    @Nested
    @DisplayName("Controller -> Service -> Repository integration")
    class FullStackTest {

        private UserRepository repository;
        private UserService service;
        private UserController controller;

        @BeforeEach
        void setUp() {
            repository = new UserRepository();
            service = new UserService(repository);
            controller = new UserController(service);
        }

        @Test
        @DisplayName("GET /users/1 returns not found for non-existent user")
        void shouldReturnNotFoundForMissingUser() {
            String result = controller.getUser(1);
            assertEquals("User not found", result);
        }

        @Test
        @DisplayName("POST /users creates user and returns success")
        void shouldCreateUser() {
            String result = controller.createUser("Alice");
            assertEquals("User created: Alice", result);
        }

        @Test
        @DisplayName("GET /users/1 returns user after creation")
        void shouldRetrieveCreatedUser() {
            controller.createUser("Alice");

            String result = controller.getUser(1);
            assertEquals("User: Alice", result);
        }

        @Test
        @DisplayName("POST /users rejects empty name")
        void shouldRejectEmptyName() {
            String result = controller.createUser("");
            assertEquals("Failed to create user", result);
            assertEquals(0, repository.count());
        }

        @Test
        @DisplayName("POST /users rejects null name")
        void shouldRejectNullName() {
            String result = controller.createUser(null);
            assertEquals("Failed to create user", result);
            assertEquals(0, repository.count());
        }

        @Test
        @DisplayName("DELETE /users/1 removes user")
        void shouldDeleteUser() {
            controller.createUser("Alice");

            String result = controller.deleteUser(1);
            assertEquals("User deleted: 1", result);
            assertEquals(0, repository.count());
        }

        @Test
        @DisplayName("DELETE /users/999 fails for non-existent user")
        void shouldFailToDeleteNonExistentUser() {
            String result = controller.deleteUser(999);
            assertTrue(result.startsWith("Failed to delete:"));
        }
    }

    // =========================================================
    // 3. UNIT TEST WITH MOCKS - Isolated component testing
    // =========================================================

    @Nested
    @DisplayName("Service layer with mocked repository")
    class ServiceWithMockTest {

        @Mock
        UserRepository mockRepository;

        @InjectMocks
        UserService service;

        @Test
        @DisplayName("Service delegates to repository")
        void shouldDelegateToRepository() {
            when(mockRepository.findById(1)).thenReturn("Alice");

            String result = service.findById(1);

            assertEquals("Alice", result);
            verify(mockRepository).findById(1);
        }

        @Test
        @DisplayName("Service rejects null input")
        void shouldRejectNull() {
            boolean saved = service.save(null);

            assertFalse(saved);
            verify(mockRepository, never()).save(any());
        }

        @Test
        @DisplayName("Service throws when deleting non-existent user")
        void shouldThrowOnDeleteNonExistent() {
            when(mockRepository.findById(999)).thenReturn(null);

            assertThrows(IllegalArgumentException.class,
                () -> service.delete(999));
        }
    }

    // =========================================================
    // 4. SPRING BOOT TEST ANNOTATIONS (reference)
    // =========================================================
    //
    // @SpringBootTest - Full application context
    //   @WebMvcTest - Web layer only (controllers, filters)
    //   @DataJpaTest - JPA layer only (repositories, entities)
    //   @RestClientTest - REST client testing
    //
    // Test Configuration:
    //   @TestConfiguration - Additional beans for tests
    //   @MockBean - Mock external dependencies (databases, APIs)
    //   @ActiveProfiles("test") - Use test-specific configuration
    //
    // HTTP Testing:
    //   TestRestTemplate - Full HTTP integration testing
    //   MockMvc - Web layer testing without starting server
}
