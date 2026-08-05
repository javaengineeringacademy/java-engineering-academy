package academy.javaengineering.patterns.creational;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    @Test
    void buildUserWithAllFields() {
        User user = new User.Builder()
                .firstName("John")
                .lastName("Doe")
                .age(30)
                .email("john@example.com")
                .phone("555-0100")
                .addresses(List.of("123 Main St"))
                .build();

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals(30, user.getAge());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(1, user.getAddresses().size());
    }

    @Test
    void buildUserWithRequiredFieldsOnly() {
        User user = new User.Builder()
                .firstName("Jane")
                .lastName("Smith")
                .build();

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals(0, user.getAge());
        assertEquals("", user.getEmail());
    }

    @Test
    void buildUserWithUserBuilder() {
        User user = new UserBuilder()
                .firstName("Bob")
                .lastName("Wilson")
                .age(25)
                .addAddress("789 Pine Rd")
                .build();

        assertEquals("Bob", user.getFirstName());
        assertEquals(25, user.getAge());
        assertEquals(1, user.getAddresses().size());
    }

    @Test
    void buildWithoutFirstNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new User.Builder().lastName("Doe").build());
    }

    @Test
    void buildWithoutLastNameThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> new User.Builder().firstName("John").build());
    }
}
