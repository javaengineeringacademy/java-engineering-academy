package academy.javaengineering.generics;

import academy.javaengineering.generics.GenericEnumDemo.Operation;
import academy.javaengineering.generics.GenericEnumDemo.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for generic enum behavior.
 */
@DisplayName("Generic Enum Tests")
class GenericEnumTest {

  @Nested
  @DisplayName("Status Enum Tests")
  class StatusTests {

    @Test
    @DisplayName("Should have correct code for each constant")
    void shouldHaveCorrectCodes() {
      assertEquals(200, Status.OK.getCode());
      assertEquals(404, Status.NOT_FOUND.getCode());
      assertEquals(500, Status.INTERNAL_ERROR.getCode());
    }

    @Test
    @DisplayName("Should have correct message for each constant")
    void shouldHaveCorrectMessages() {
      assertEquals("Success", Status.OK.getMessage());
      assertEquals("Not Found", Status.NOT_FOUND.getMessage());
      assertEquals("Internal Error", Status.INTERNAL_ERROR.getMessage());
    }

    @Test
    @DisplayName("Should find status by code using generic method")
    void shouldFindByCode() {
      Status found = Status.findByCode(404, Status.class);
      assertEquals(Status.NOT_FOUND, found);
    }

    @Test
    @DisplayName("Should throw for unknown status code")
    void shouldThrowForUnknownCode() {
      assertThrows(IllegalArgumentException.class,
          () -> Status.findByCode(999, Status.class));
    }
  }

  @Nested
  @DisplayName("Operation Enum Tests")
  class OperationTests {

    @Test
    @DisplayName("Should add numbers")
    void shouldAdd() {
      assertEquals(8.0, Operation.ADD.compute(5.0, 3.0));
    }

    @Test
    @DisplayName("Should subtract numbers")
    void shouldSubtract() {
      assertEquals(7.0, Operation.SUBTRACT.compute(10.0, 3.0));
    }

    @Test
    @DisplayName("Should multiply numbers")
    void shouldMultiply() {
      assertEquals(15.0, Operation.MULTIPLY.compute(5.0, 3.0));
    }

    @Test
    @DisplayName("Should divide numbers")
    void shouldDivide() {
      assertEquals(5.0, Operation.DIVIDE.compute(10.0, 2.0));
    }

    @Test
    @DisplayName("Should throw on division by zero")
    void shouldThrowOnDivisionByZero() {
      assertThrows(ArithmeticException.class,
          () -> Operation.DIVIDE.compute(10.0, 0.0));
    }

    @Test
    @DisplayName("Should have correct symbols")
    void shouldHaveCorrectSymbols() {
      assertEquals("+", Operation.ADD.getSymbol());
      assertEquals("-", Operation.SUBTRACT.getSymbol());
      assertEquals("*", Operation.MULTIPLY.getSymbol());
      assertEquals("/", Operation.DIVIDE.getSymbol());
    }
  }

  @Nested
  @DisplayName("Generic Enum Utility Methods")
  class UtilityTests {

    @Test
    @DisplayName("Should get enum constant by name")
    void shouldGetConstantByName() {
      Operation op = GenericEnumDemo.getEnumConstant(Operation.class, "ADD");
      assertNotNull(op);
      assertEquals(Operation.ADD, op);
    }

    @Test
    @DisplayName("Should throw for invalid constant name")
    void shouldThrowForInvalidName() {
      assertThrows(IllegalArgumentException.class,
          () -> GenericEnumDemo.getEnumConstant(Operation.class, "INVALID"));
    }
  }
}
