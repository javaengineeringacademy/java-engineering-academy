package academy.javaengineering.testing.unit.practices;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 3: Testing with Mocks
 *
 * Tasks:
 * 1. Test service with mocked dependencies
 * 2. Verify interactions
 * 3. Test error scenarios
 * 4. Use argument captors
 */
@ExtendWith(MockitoExtension.class)
class Exercise3TestableDesign {

    interface UserRepository {
        boolean existsByEmail(String email);
        void save(Object user);
    }

    static class RegistrationService {
        private final UserRepository userRepository;
        RegistrationService(UserRepository repo) { this.userRepository = repo; }
        void register(String email) {
            if (userRepository.existsByEmail(email)) throw new IllegalArgumentException("Email exists");
            userRepository.save(email);
        }
    }

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("should register new user")
    void shouldRegisterNewUser() {
        // Arrange, Act, Assert
    }

    @Test
    @DisplayName("should reject duplicate email")
    void shouldRejectDuplicate() {
        // Arrange, Act, Assert
    }
}
