package academy.javaengineering.testing.mockito.solutions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise1BasicMockingSolution {

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
        User expected = new User(1L, "Alice");
        when(userRepository.findById(1L)).thenReturn(expected);

        User result = userService.getUser(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldCreateUser() {
        User user = new User(2L, "Bob");

        userService.createUser(user);

        verify(userRepository).save(user);
    }
}
