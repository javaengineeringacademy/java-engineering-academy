package academy.javaengineering.testing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MockitoExampleTest {

    @Test
    void shouldGetUserWhenExists() {
        MockitoExample.UserRepository repo = new MockitoExample.UserRepository() {
            @Override public String findById(Long id) { return "John"; }
            @Override public void save(String user) {}
        };
        MockitoExample example = new MockitoExample(repo);
        assertEquals("John", example.getUser(1L));
    }

    @Test
    void shouldReturnUnknownWhenUserNotFound() {
        MockitoExample.UserRepository repo = new MockitoExample.UserRepository() {
            @Override public String findById(Long id) { return null; }
            @Override public void save(String user) {}
        };
        MockitoExample example = new MockitoExample(repo);
        assertEquals("Unknown", example.getUser(1L));
    }

    @Test
    void shouldThrowExceptionWhenCreatingEmptyUser() {
        MockitoExample.UserRepository repo = new MockitoExample.UserRepository() {
            @Override public String findById(Long id) { return null; }
            @Override public void save(String user) {}
        };
        MockitoExample example = new MockitoExample(repo);
        assertThrows(IllegalArgumentException.class, () -> example.createUser(""));
    }
}
