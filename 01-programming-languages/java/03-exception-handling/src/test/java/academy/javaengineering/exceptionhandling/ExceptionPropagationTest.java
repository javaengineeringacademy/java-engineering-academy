package academy.javaengineering.exceptionhandling;

import academy.javaengineering.exceptionhandling.ExceptionPropagation.CustomCheckedException;
import academy.javaengineering.exceptionhandling.ExceptionPropagation.DataProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for exception propagation, throw/throws, and exception chaining.
 */
class ExceptionPropagationTest {

    @Nested
    @DisplayName("Call Stack Propagation")
    class PropagationTests {

        @Test
        @DisplayName("Exception propagates through methodA -> methodB -> methodC")
        void testExceptionPropagatesUpCallStack() {
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                ExceptionPropagation.methodA();
            });
            assertEquals("Error in methodC", thrown.getMessage());
        }

        @Test
        @DisplayName("methodC throws RuntimeException directly")
        void testMethodCThrowsException() {
            RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                ExceptionPropagation.methodC();
            });
            assertEquals("Error in methodC", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("Throw vs Throws Keywords")
    class ThrowVsThrowsTests {

        @Test
        @DisplayName("throw keyword creates and throws IllegalArgumentException")
        void testThrowKeyword() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                ExceptionPropagation.throwException();
            });
            assertEquals("Invalid argument", thrown.getMessage());
        }

        @Test
        @DisplayName("throws keyword declares checked exception that must be caught")
        void testThrowsKeyword() {
            assertThrows(CustomCheckedException.class, () -> {
                ExceptionPropagation.declareException();
            });
        }

        @Test
        @DisplayName("declared checked exception carries correct message")
        void testDeclaredExceptionMessage() {
            CustomCheckedException thrown = assertThrows(CustomCheckedException.class, () -> {
                ExceptionPropagation.declareException();
            });
            assertEquals("Declared exception", thrown.getMessage());
        }
    }

    @Nested
    @DisplayName("Exception Chaining")
    class ChainingTests {

        @Test
        @DisplayName("processData wraps IllegalArgumentException in DataProcessingException")
        void testExceptionChainingPreservesCause() {
            DataProcessingException thrown = assertThrows(DataProcessingException.class, () -> {
                ExceptionPropagation.processData();
            });
            assertEquals("Failed to process data", thrown.getMessage());
            assertNotNull(thrown.getCause());
            assertInstanceOf(IllegalArgumentException.class, thrown.getCause());
            assertEquals("Invalid data format", thrown.getCause().getMessage());
        }

        @Test
        @DisplayName("validateData throws IllegalArgumentException for null input")
        void testValidateDataWithNull() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                ExceptionPropagation.validateData(null);
            });
            assertEquals("Invalid data format", thrown.getMessage());
        }

        @Test
        @DisplayName("validateData accepts non-null input without exception")
        void testValidateDataWithValidInput() {
            assertDoesNotThrow(() -> {
                ExceptionPropagation.validateData("valid");
            });
        }

        @Test
        @DisplayName("CustomCheckedException is a checked exception")
        void testCustomCheckedExceptionIsChecked() {
            assertThrows(Exception.class, () -> {
                throw new CustomCheckedException("test");
            });
        }
    }
}
