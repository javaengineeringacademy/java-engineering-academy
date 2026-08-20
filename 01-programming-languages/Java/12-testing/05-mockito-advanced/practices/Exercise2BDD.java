package academy.javaengineering.testing.mockito.advanced.practices;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 2: BDD-Style Testing
 *
 * Tasks:
 * 1. Use given().willReturn() for stubbing
 * 2. Use then().should() for verification
 * 3. Apply BDD to a service with dependencies
 */
@ExtendWith(MockitoExtension.class)
class Exercise2BDD {

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
        // Arrange: create service
        // Act: register user
        // Assert: verify notification was sent using BDD style
    }
}
