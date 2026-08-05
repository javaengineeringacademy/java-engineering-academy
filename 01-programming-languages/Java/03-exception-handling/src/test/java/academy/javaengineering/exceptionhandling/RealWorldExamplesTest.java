package academy.javaengineering.exceptionhandling;

import academy.javaengineering.exceptionhandling.RealWorldExamples.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for enterprise exception handling patterns.
 */
class RealWorldExamplesTest {

    @Nested
    @DisplayName("Service Layer Pattern")
    class ServiceLayerTests {

        @Test
        @DisplayName("createUser throws ValidationException for null name")
        void testCreateUserNullName() {
            var service = new UserService();
            ValidationException thrown = assertThrows(ValidationException.class, () -> {
                service.createUser(null, "test@example.com");
            });
            assertEquals("Name is required", thrown.getMessage());
            assertEquals("name", thrown.getFieldName());
        }

        @Test
        @DisplayName("createUser throws ValidationException for invalid email")
        void testCreateUserInvalidEmail() {
            var service = new UserService();
            ValidationException thrown = assertThrows(ValidationException.class, () -> {
                service.createUser("John", "invalid-email");
            });
            assertEquals("Invalid email format", thrown.getMessage());
            assertEquals("email", thrown.getFieldName());
        }

        @Test
        @DisplayName("createUser throws DuplicateEmailException for existing email")
        void testCreateUserDuplicateEmail() {
            var service = new UserService();
            assertDoesNotThrow(() -> {
                service.createUser("John", "duplicate@example.com");
            });
            DuplicateEmailException thrown = assertThrows(DuplicateEmailException.class, () -> {
                service.createUser("Jane", "duplicate@example.com");
            });
            assertEquals("Email already exists: duplicate@example.com", thrown.getMessage());
        }

        @Test
        @DisplayName("createUser succeeds with valid inputs")
        void testCreateUserValid() {
            var service = new UserService();
            assertDoesNotThrow(() -> {
                service.createUser("John", "john@example.com");
            });
        }

        @Test
        @DisplayName("ValidationException carries field name metadata")
        void testValidationExceptionMetadata() {
            ValidationException ex = new ValidationException("email", "Invalid format");
            assertEquals("email", ex.getFieldName());
            assertEquals("Invalid format", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Repository Pattern")
    class RepositoryTests {

        @Test
        @DisplayName("findById throws EntityNotFoundException for id 999")
        void testFindByIdNotFound() {
            var repo = new UserRepository();
            EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
                repo.findById(999L);
            });
            assertEquals("User not found with id: 999", thrown.getMessage());
        }

        @Test
        @DisplayName("findById returns user for valid id")
        void testFindByIdFound() throws EntityNotFoundException {
            var repo = new UserRepository();
            String result = repo.findById(1L);
            assertEquals("User-1", result);
        }

        @Test
        @DisplayName("save throws DataAccessException for null entity")
        void testSaveNull() {
            var repo = new UserRepository();
            DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
                repo.save(null);
            });
            assertEquals("Cannot save null entity", thrown.getMessage());
        }

        @Test
        @DisplayName("save succeeds for valid entity")
        void testSaveValid() {
            var repo = new UserRepository();
            assertDoesNotThrow(() -> {
                repo.save("valid");
            });
        }

        @Test
        @DisplayName("DataAccessException is unchecked")
        void testDataAccessExceptionIsUnchecked() {
            assertInstanceOf(RuntimeException.class, new DataAccessException("test"));
        }
    }

    @Nested
    @DisplayName("Result Pattern")
    class ResultPatternTests {

        @Test
        @DisplayName("divide by zero returns error Result")
        void testDivideByZero() {
            var calc = new SafeCalculator();
            Result<Integer> result = calc.divide(10, 0);
            result.ifError(error -> assertEquals("Division by zero", error));
        }

        @Test
        @DisplayName("successful division returns success Result")
        void testDivideSuccess() {
            var calc = new SafeCalculator();
            Result<Integer> result = calc.divide(10, 2);
            result.ifPresent(value -> assertEquals(5, value));
        }

        @Test
        @DisplayName("Result.success creates success result")
        void testResultSuccess() {
            Result<String> result = Result.success("hello");
            result.ifPresent(value -> assertEquals("hello", value));
        }

        @Test
        @DisplayName("Result.error creates error result")
        void testResultError() {
            Result<String> result = Result.error("failure");
            result.ifError(error -> assertEquals("failure", error));
        }

        @Test
        @DisplayName("ifPresent does not execute for error result")
        void testIfPresentSkipsOnError() {
            Result<Integer> result = Result.error("failure");
            boolean[] executed = {false};
            result.ifPresent(value -> executed[0] = true);
            assertFalse(executed[0]);
        }

        @Test
        @DisplayName("ifError does not execute for success result")
        void testIfErrorSkipsOnSuccess() {
            Result<Integer> result = Result.success(42);
            boolean[] executed = {false};
            result.ifError(error -> executed[0] = true);
            assertFalse(executed[0]);
        }
    }

    @Nested
    @DisplayName("Exception Translation")
    class TranslationTests {

        @Test
        @DisplayName("processOrder throws PaymentException with order ID")
        void testProcessOrderPaymentException() {
            var service = new OrderService();
            PaymentException thrown = assertThrows(PaymentException.class, () -> {
                service.processOrder(1L);
            });
            assertEquals("Insufficient funds", thrown.getMessage());
            assertEquals(1L, thrown.getOrderId());
        }

        @Test
        @DisplayName("processOrder throws OrderNotFoundException for null ID")
        void testProcessOrderNotFound() {
            var service = new OrderService();
            OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class, () -> {
                service.processOrder(null);
            });
            assertEquals("Order not found with id: null", thrown.getMessage());
        }

        @Test
        @DisplayName("PaymentException carries orderId metadata")
        void testPaymentExceptionMetadata() {
            PaymentException ex = new PaymentException("Declined", 42L);
            assertEquals("Declined", ex.getMessage());
            assertEquals(42L, ex.getOrderId());
        }
    }
}
