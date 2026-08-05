package academy.javaengineering.springcore.di;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DITest {

    @Autowired
    private DIExamples diExamples;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService.clearHistory();
    }

    @Test
    void constructorInjectionWorks() {
        assertNotNull(diExamples);
        assertEquals("DefaultApp", diExamples.getAppName());
    }

    @Test
    void setterInjectionWorks() {
        assertNotNull(emailService);
        diExamples.sendNotification("Test message");
        assertEquals(1, emailService.getSentCount());
        assertEquals("Test message", emailService.getSentEmails().get(0));
    }

    @Test
    void emailServiceStateIsManaged() {
        emailService.send("First");
        emailService.send("Second");
        assertEquals(2, emailService.getSentCount());

        emailService.clearHistory();
        assertEquals(0, emailService.getSentCount());
    }
}
