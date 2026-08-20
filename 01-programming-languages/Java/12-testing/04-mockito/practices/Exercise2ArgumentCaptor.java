package academy.javaengineering.testing.mockito.practices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 2: Argument Captor
 *
 * Tasks:
 * 1. Capture arguments passed to mock methods
 * 2. Verify captured argument values
 * 3. Test multiple invocations
 */
@ExtendWith(MockitoExtension.class)
class Exercise2ArgumentCaptor {

    static class Email {
        private final String to;
        private final String subject;
        private final String body;
        Email(String to, String subject, String body) {
            this.to = to; this.subject = subject; this.body = body;
        }
        String getTo() { return to; }
        String getSubject() { return subject; }
        String getBody() { return body; }
    }

    interface EmailService {
        void send(Email email);
    }

    @Mock
    private EmailService emailService;

    @Test
    void shouldCaptureSentEmail() {
        // Arrange
        Email email = new Email("user@test.com", "Welcome", "Hello!");

        // Act
        emailService.send(email);

        // Assert: capture and verify
        ArgumentCaptor<Email> captor = ArgumentCaptor.forClass(Email.class);
        verify(emailService).send(captor.capture());
        // TODO: Verify captured email details
    }
}
