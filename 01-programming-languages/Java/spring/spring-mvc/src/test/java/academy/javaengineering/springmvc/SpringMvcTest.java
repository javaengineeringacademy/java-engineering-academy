package academy.javaengineering.springmvc;

import academy.javaengineering.springmvc.model.User;
import academy.javaengineering.springmvc.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spring MVC Tests")
class SpringMvcTest {

    @Test
    @DisplayName("UserService should manage users")
    void testUserService() {
        var service = new UserService();
        
        User user = new User(null, "John", "john@example.com", 25);
        User saved = service.save(user);
        
        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        
        var found = service.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("UserService should delete users")
    void testDeleteUser() {
        var service = new UserService();
        
        User user = new User(null, "John", "john@example.com", 25);
        User saved = service.save(user);
        
        service.delete(saved.getId());
        assertFalse(service.findById(saved.getId()).isPresent());
    }

    @Test
    @DisplayName("User model should validate fields")
    void testUserValidation() {
        User user = new User(1L, "John", "john@example.com", 25);
        
        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals(25, user.getAge());
    }
}
