package academy.javaengineering.testing.mockito.solutions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise2ArgumentCaptorSolution {

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
        Email email = new Email("user@test.com", "Welcome", "Hello!");

        emailService.send(email);

        ArgumentCaptor<Email> captor = ArgumentCaptor.forClass(Email.class);
        verify(emailService).send(captor.capture());

        Email captured = captor.getValue();
        assertEquals("user@test.com", captured.getTo());
        assertEquals("Welcome", captured.getSubject());
        assertEquals("Hello!", captured.getBody());
    }
}
