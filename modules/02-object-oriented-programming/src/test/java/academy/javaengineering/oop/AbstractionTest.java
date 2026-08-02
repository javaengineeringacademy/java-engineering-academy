package academy.javaengineering.oop;

import academy.javaengineering.oop.`05-abstraction`.AbstractionExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Abstraction Tests")
class AbstractionTest {

    @Test
    @DisplayName("Email notification validates recipient")
    void emailNotification() {
        NotificationChannel email = new EmailNotification("smtp.test.com");

        String result = email.sendNotification("user@test.com", "Hello");
        assertTrue(result.contains("EMAIL"));
        assertTrue(result.contains("user@test.com"));

        String invalid = email.sendNotification("invalid", "Hello");
        assertTrue(invalid.contains("Invalid recipient"));
    }

    @Test
    @DisplayName("SMS notification validates phone format")
    void smsNotification() {
        NotificationChannel sms = new SmsNotification(160);

        String valid = sms.sendNotification("+15551234567", "Code: 1234");
        assertTrue(valid.contains("SMS"));

        String invalid = sms.sendNotification("not-a-phone", "Test");
        assertTrue(invalid.contains("Invalid recipient"));
    }

    @Test
    @DisplayName("Disabled channel queues notifications")
    void disabledChannel() {
        NotificationChannel email = new EmailNotification("smtp.test.com");
        email.setEnabled(false);

        String result = email.sendNotification("user@test.com", "Hello");
        assertTrue(result.contains("disabled"));
    }

    @Test
    @DisplayName("SMS truncates long messages")
    void smsTruncation() {
        NotificationChannel sms = new SmsNotification(10);
        String result = sms.sendNotification("+15551234567", "This is a very long message");
        assertTrue(result.contains("..."));
    }

    @Test
    @DisplayName("Repository CRUD operations")
    void repositoryCRUD() {
        InMemoryCustomerRepository repo = new InMemoryCustomerRepository();

        repo.save(new Customer(1, "Alice"));
        repo.save(new Customer(2, "Bob"));

        assertEquals(2, repo.count());
        assertEquals("Alice", repo.findById(1L).getName());

        repo.delete(1L);
        assertEquals(1, repo.count());
        assertNull(repo.findById(1L));
    }

    @Test
    @DisplayName("Repository bulk save via default method")
    void repositoryBulkSave() {
        InMemoryCustomerRepository repo = new InMemoryCustomerRepository();
        List<Customer> bulk = List.of(
                new Customer(10, "Bulk A"),
                new Customer(11, "Bulk B")
        );

        repo.saveAll(bulk);
        assertEquals(2, repo.count());
    }

    @Test
    @DisplayName("Repository implements Auditable interface")
    void repositoryAudit() {
        InMemoryCustomerRepository repo = new InMemoryCustomerRepository();
        String audit = repo.auditEntry();
        assertTrue(audit.contains("CustomerRepository"));
        assertTrue(audit.contains("AUDIT"));
    }
}
