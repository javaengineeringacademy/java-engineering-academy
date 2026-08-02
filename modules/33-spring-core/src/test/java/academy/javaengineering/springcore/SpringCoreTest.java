package academy.javaengineering.springcore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

@DisplayName("Spring Core Tests")
class SpringCoreTest {

    @Test
    @DisplayName("UserService should find user by ID")
    void testFindUser() {
        UserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);
        
        User user = new User("1", "John", "john@example.com");
        service.saveUser(user);
        
        Optional<User> found = service.findUser("1");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getName());
    }

    @Test
    @DisplayName("UserService should return empty for unknown user")
    void testFindUserNotFound() {
        UserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);
        
        Optional<User> found = service.findUser("unknown");
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Greeter should return message")
    void testGreeter() {
        Greeter greeter = new Greeter("Hello, World!");
        assertEquals("Hello, World!", greeter.greet());
    }
}
