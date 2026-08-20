package academy.javaengineering.testing.mockito.advanced.solutions;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Exercise1SpySolution {

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
        assertEquals("Hello", spyUtils.capitalize("hello"));
        assertNull(spyUtils.capitalize(null));
    }

    @Test
    void shouldStubLengthMethod() {
        doReturn(-1).when(spyUtils).length(anyString());
        assertEquals(-1, spyUtils.length("test"));
        // Other methods still real
        assertEquals("Hello", spyUtils.capitalize("hello"));
    }
}
