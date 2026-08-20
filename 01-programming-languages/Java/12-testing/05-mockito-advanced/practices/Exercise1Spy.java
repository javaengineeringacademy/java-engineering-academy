package academy.javaengineering.testing.mockito.advanced.practices;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Exercise 1: Spy Usage
 *
 * Tasks:
 * 1. Create a spy of a real service
 * 2. Stub one method while keeping others real
 * 3. Verify real method was called
 * 4. Use doReturn for spy stubbing
 */
@ExtendWith(MockitoExtension.class)
class Exercise1Spy {

    static class StringUtils {
        String capitalize(String input) {
            if (input == null || input.isEmpty()) return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
        int length(String input) { return input == null ? 0 : input.length(); }
        boolean isEmpty(String input) { return input == null || input.isEmpty(); }
    }

    @Spy
    private StringUtils spyUtils;

    @Test
    void shouldCallRealCapitalize() {
        // Act & Assert: verify real method runs
    }

    @Test
    void shouldStubLengthMethod() {
        // Arrange: stub length to return -1
        // Act: call length
        // Assert: verify stubbed value
    }
}
