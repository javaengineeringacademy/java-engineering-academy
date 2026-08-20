package academy.javaengineering.testing.unit.solutions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise3TestableDesignSolution {

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
    void shouldRegisterNewUser() {
        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        RegistrationService service = new RegistrationService(userRepository);
        service.register("new@test.com");
        verify(userRepository).save("new@test.com");
    }

    @Test
    void shouldRejectDuplicate() {
        when(userRepository.existsByEmail("dup@test.com")).thenReturn(true);
        RegistrationService service = new RegistrationService(userRepository);
        assertThrows(IllegalArgumentException.class,
            () -> service.register("dup@test.com"));
        verify(userRepository, never()).save(any());
    }
}
