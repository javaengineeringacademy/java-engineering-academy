package academy.javaengineering.springcore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringCoreTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context, "Application context should be loaded");
    }

    @Test
    void beansAreRegistered() {
        assertTrue(context.containsBean("greeter"));
        assertTrue(context.containsBean("userRepository"));
        assertTrue(context.containsBean("userService"));
    }

    @Test
    void userServiceIsFunctional() {
        UserService userService = context.getBean(UserService.class);
        assertNotNull(userService);

        User user = userService.createUser("Test", "test@example.com");
        assertNotNull(user);
        assertEquals("Test", user.getName());
        assertEquals(1L, userService.getUserCount());
    }
}
