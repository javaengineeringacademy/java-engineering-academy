package academy.javaengineering.exceptionhandling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for exception handling best practices.
 */
class BestPracticesTest {

    @Nested
    @DisplayName("Fail-Fast Validation")
    class FailFastTests {

        @Test
        @DisplayName("validateUser fails fast on null name")
        void testValidateUserNullName() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                BestPractices.validateUser(null, "test@example.com", 25);
            });
            assertEquals("Name cannot be empty", thrown.getMessage());
        }

        @Test
        @DisplayName("validateUser fails fast on blank name")
        void testValidateUserBlankName() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                BestPractices.validateUser("  ", "test@example.com", 25);
            });
            assertEquals("Name cannot be empty", thrown.getMessage());
        }

        @Test
        @DisplayName("validateUser fails fast on invalid email")
        void testValidateUserInvalidEmail() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                BestPractices.validateUser("John", "invalid-email", 25);
            });
            assertEquals("Invalid email format", thrown.getMessage());
        }

        @Test
        @DisplayName("validateUser fails fast on invalid age")
        void testValidateUserInvalidAge() {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                BestPractices.validateUser("John", "test@example.com", -5);
            });
            assertEquals("Invalid age: -5", thrown.getMessage());
        }

        @Test
        @DisplayName("validateUser succeeds with valid inputs")
        void testValidateUserValid() {
            assertTrue(BestPractices.validateUser("John", "test@example.com", 25));
        }
    }

    @Nested
    @DisplayName("Specific Exception Handling")
    class SpecificExceptionTests {

        @Test
        @DisplayName("SpecificException is thrown by riskyOperation")
        void testRiskyOperationThrows() {
            BestPractices.SpecificException thrown = assertThrows(BestPractices.SpecificException.class, () -> {
                BestPractices.riskyOperation();
            });
            assertEquals("Expected failure", thrown.getMessage());
        }

        @Test
        @DisplayName("SpecificException extends RuntimeException")
        void testSpecificExceptionHierarchy() {
            assertInstanceOf(RuntimeException.class, new BestPractices.SpecificException("test"));
        }
    }

    @Nested
    @DisplayName("Exception Recovery Pattern")
    class RecoveryTests {

        @Test
        @DisplayName("riskyOperation always throws SpecificException")
        void testRiskyOperationAlwaysFails() {
            for (int i = 0; i < 3; i++) {
                assertThrows(BestPractices.SpecificException.class, () -> {
                    BestPractices.riskyOperation();
                });
            }
        }

        @Test
        @DisplayName("Exception has consistent message across retries")
        void testExceptionMessageConsistency() {
            for (int i = 0; i < 3; i++) {
                BestPractices.SpecificException thrown = assertThrows(BestPractices.SpecificException.class, () -> {
                    BestPractices.riskyOperation();
                });
                assertEquals("Expected failure", thrown.getMessage());
            }
        }
    }
}
