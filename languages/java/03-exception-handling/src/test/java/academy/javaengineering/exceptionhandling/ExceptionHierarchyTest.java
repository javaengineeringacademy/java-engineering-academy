package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for exception hierarchy, checked vs unchecked, and multi-catch.
 */
class ExceptionHierarchyTest {

    @Nested
    @DisplayName("Checked Exceptions")
    class CheckedExceptionTests {

        @Test
        @DisplayName("readFile throws IOException for nonexistent file")
        void testReadFileThrowsIOException() {
            IOException thrown = assertThrows(IOException.class, () -> {
                ExceptionHierarchy.readFile("/nonexistent/file.txt");
            });
            assertTrue(thrown.getMessage().contains("/nonexistent/file.txt"));
        }

        @Test
        @DisplayName("readFile does not throw for existing path")
        void testReadFileForExistingPath() {
            assertDoesNotThrow(() -> {
                ExceptionHierarchy.readFile("/tmp");
            });
        }

        @Test
        @DisplayName("IOException is a checked exception (extends Exception)")
        void testIOExceptionIsChecked() {
            assertThrows(Exception.class, () -> {
                throw new IOException("test");
            });
            assertFalse(IOException.class.isAssignableFrom(RuntimeException.class));
        }
    }

    @Nested
    @DisplayName("Unchecked Exceptions")
    class UncheckedExceptionTests {

        @Test
        @DisplayName("processInput throws NullPointerException for null input")
        void testProcessInputNull() {
            NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
                ExceptionHierarchy.processInput(null);
            });
            assertEquals("Input cannot be null", thrown.getMessage());
        }

        @Test
        @DisplayName("processInput throws IllegalArgumentException for 'invalid' input")
        void testProcessInputInvalid() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                ExceptionHierarchy.processInput("invalid");
            });
            assertEquals("Invalid input: invalid", thrown.getMessage());
        }

        @Test
        @DisplayName("processInput throws UnsupportedOperationException for 'unsupported' input")
        void testProcessInputUnsupported() {
            UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
                ExceptionHierarchy.processInput("unsupported");
            });
            assertEquals("Unsupported operation", thrown.getMessage());
        }

        @Test
        @DisplayName("NullPointerException is unchecked (extends RuntimeException)")
        void testNPEIsUnchecked() {
            assertTrue(RuntimeException.class.isAssignableFrom(NullPointerException.class));
        }
    }

    @Nested
    @DisplayName("Error Types")
    class ErrorTests {

        @Test
        @DisplayName("StackOverflowError can be caught (not recommended)")
        void testCatchStackOverflowError() {
            StackOverflowError thrown = assertThrows(StackOverflowError.class, () -> {
                throw new StackOverflowError("Simulated stack overflow");
            });
            assertEquals("Simulated stack overflow", thrown.getMessage());
        }

        @Test
        @DisplayName("StackOverflowError extends Error, not Exception")
        void testErrorHierarchy() {
            assertInstanceOf(Error.class, new StackOverflowError("test"));
            assertFalse(new StackOverflowError("test").getClass().isAssignableFrom(Exception.class));
        }
    }

    @Nested
    @DisplayName("Multi-Catch Block")
    class MultiCatchTests {

        @Test
        @DisplayName("multi-catch catches IllegalArgumentException")
        void testMultiCatchWithIllegalArgument() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                ExceptionHierarchy.processInput("invalid");
            });
            assertEquals("Invalid input: invalid", thrown.getMessage());
        }

        @Test
        @DisplayName("multi-catch catches UnsupportedOperationException")
        void testMultiCatchWithUnsupportedOperation() {
            UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
                ExceptionHierarchy.processInput("unsupported");
            });
            assertEquals("Unsupported operation", thrown.getMessage());
        }

        @Test
        @DisplayName("processInput succeeds with valid input")
        void testProcessInputValid() {
            assertDoesNotThrow(() -> {
                ExceptionHierarchy.processInput("valid");
            });
        }
    }
}
