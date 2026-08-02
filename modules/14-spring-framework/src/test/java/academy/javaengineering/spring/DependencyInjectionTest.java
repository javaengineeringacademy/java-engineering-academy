package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DependencyInjectionTest {

    @Test
    void shouldInjectViaConstructor() {
        DependencyInjectionExample.UserRepository repo = new DependencyInjectionExample.UserRepositoryImpl();
        DependencyInjectionExample.UserService service = new DependencyInjectionExample.UserService(repo);
        assertEquals("User-1", service.getUser(1L));
    }
}
