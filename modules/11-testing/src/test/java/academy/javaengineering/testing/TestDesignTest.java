package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestDesignTest {

    private final TestDesignExample example = new TestDesignExample();

    @Test
    void shouldValidateEmail() {
        assertTrue(example.validateEmail("test@example.com"));
        assertFalse(example.validateEmail("invalid"));
        assertFalse(example.validateEmail(null));
    }

    @Test
    void shouldCheckCanVote() {
        TestDesignExample.User adult = new TestDesignExample.User.Builder().age(20).build();
        TestDesignExample.User minor = new TestDesignExample.User.Builder().age(16).build();
        assertTrue(example.canVote(adult));
        assertFalse(example.canVote(minor));
    }

    @Test
    void shouldBuildUserWithDefaults() {
        TestDesignExample.User user = new TestDesignExample.User.Builder().build();
        assertEquals("Default", user.getName());
        assertEquals(25, user.getAge());
    }

    @Test
    void shouldBuildUserWithCustomValues() {
        TestDesignExample.User user = new TestDesignExample.User.Builder()
                .name("John").email("john@test.com").age(30).build();
        assertEquals("John", user.getName());
        assertEquals("john@test.com", user.getEmail());
        assertEquals(30, user.getAge());
    }
}
