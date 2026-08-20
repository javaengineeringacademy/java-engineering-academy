package academy.javaengineering.testing.mockito.practices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 1: Basic Mocking
 *
 * Tasks:
 * 1. Create a mock of UserRepository
 * 2. Stub findById to return a user
 * 3. Verify findById was called
 * 4. Test with any() matcher
 */
@ExtendWith(MockitoExtension.class)
class Exercise1BasicMocking {

    static class User {
        private final Long id;
        private final String name;
        User(Long id, String name) { this.id = id; this.name = name; }
        Long getId() { return id; }
        String getName() { return name; }
    }

    interface UserRepository {
        User findById(Long id);
        void save(User user);
    }

    static class UserService {
        private final UserRepository repo;
        UserService(UserRepository repo) { this.repo = repo; }
        User getUser(Long id) { return repo.findById(id); }
        void createUser(User user) { repo.save(user); }
    }

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldGetUserById() {
        // Arrange: stub findById
        // Act: call getUser
        // Assert: verify and check result
    }

    @Test
    void shouldCreateUser() {
        // Arrange: create user
        // Act: call createUser
        // Assert: verify save was called
    }
}
