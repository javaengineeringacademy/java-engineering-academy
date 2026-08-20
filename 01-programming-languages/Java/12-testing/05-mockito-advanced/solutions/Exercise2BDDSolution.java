package academy.javaengineering.testing.mockito.advanced.solutions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class Exercise2BDDSolution {

    static class User {
        private final Long id;
        private final String name;
        User(Long id, String name) { this.id = id; this.name = name; }
        String getName() { return name; }
    }

    interface NotificationService {
        void sendWelcomeEmail(String name);
    }

    static class RegistrationService {
        private final NotificationService notificationService;
        RegistrationService(NotificationService ns) { this.notificationService = ns; }
        String register(String name) {
            notificationService.sendWelcomeEmail(name);
            return "Welcome " + name;
        }
    }

    @Mock
    private NotificationService notificationService;

    @Test
    void shouldRegisterUserAndSendEmail() {
        RegistrationService service = new RegistrationService(notificationService);

        String result = service.register("Alice");

        assertEquals("Welcome Alice", result);
        then(notificationService).should().sendWelcomeEmail("Alice");
    }
}
