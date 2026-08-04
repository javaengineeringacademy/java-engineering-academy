package academy.javaengineering.generics;

import java.util.Arrays;

/**
 * Demonstrates enums working with generics in Java.
 *
 * <p>While Java enums cannot be generic themselves, enums can implement
 * generic interfaces, work with generic methods, and be used as type
 * arguments in generic contexts.</p>
 */
public class GenericEnumDemo {

  /**
   * Generic interface for computations.
   *
   * @param <T> the type of operands
   */
  public interface Computable<T> {
    T compute(T a, T b);
  }

  /**
   * Enum implementing a generic interface.
   */
  public enum Operation implements Computable<Double> {
    ADD("+") {
      @Override
      public Double compute(Double a, Double b) {
        return a + b;
      }
    },
    SUBTRACT("-") {
      @Override
      public Double compute(Double a, Double b) {
        return a - b;
      }
    },
    MULTIPLY("*") {
      @Override
      public Double compute(Double a, Double b) {
        return a * b;
      }
    },
    DIVIDE("/") {
      @Override
      public Double compute(Double a, Double b) {
        if (b == 0) {
          throw new ArithmeticException("Division by zero");
        }
        return a / b;
      }
    };

    private final String symbol;

    Operation(String symbol) {
      this.symbol = symbol;
    }

    public String getSymbol() {
      return symbol;
    }
  }

  /**
   * Enum representing status codes with associated descriptions.
   */
  public enum Status {
    OK(200, "Success"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "Internal Error");

    private final int code;
    private final String message;

    Status(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    /**
     * Finds a status by its code using a generic method.
     *
     * @param code the status code
     * @param <T>  the enum type
     * @return the matching status
     * @throws IllegalArgumentException if code not found
     */
    public static <T extends Enum<T>> T findByCode(int code,
        Class<T> enumClass) {
      for (T constant : enumClass.getEnumConstants()) {
        if (constant instanceof Status status && status.code == code) {
          return constant;
        }
      }
      throw new IllegalArgumentException("No constant with code: " + code);
    }
  }

  /**
   * Generic method that works with any enum type.
   *
   * @param enumClass the enum class
   * @param name      the constant name
   * @param <T>       the enum type
   * @return the enum constant
   */
  public static <T extends Enum<T>> T getEnumConstant(Class<T> enumClass,
      String name) {
    return Enum.valueOf(enumClass, name);
  }

  /**
   * Generic method to print all enum constants.
   *
   * @param enumClass the enum class
   * @param <T>       the enum type
   */
  public static <T extends Enum<T>> void printAllConstants(Class<T> enumClass) {
    for (T constant : enumClass.getEnumConstants()) {
      System.out.println("  " + constant.name());
    }
  }

  /**
   * Demonstrates enums with generics.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Operation enum implementing Computable<Double>
    System.out.println("5 + 3 = " + Operation.ADD.compute(5.0, 3.0));
    // Expected: 5 + 3 = 8.0
    System.out.println("10 / 2 = " + Operation.DIVIDE.compute(10.0, 2.0));
    // Expected: 10 / 2 = 5.0
    System.out.println("Operation symbol: " + Operation.MULTIPLY.getSymbol());
    // Expected: Operation symbol: *

    // Status enum
    System.out.println("OK code: " + Status.OK.getCode());
    // Expected: OK code: 200
    System.out.println("NOT_FOUND message: " + Status.NOT_FOUND.getMessage());
    // Expected: NOT_FOUND message: Not Found

    // Generic method with enum class parameter
    Status found = Status.findByCode(404, Status.class);
    System.out.println("Found by code 404: " + found.name());
    // Expected: Found by code 404: NOT_FOUND

    // Generic method to get enum constant
    Operation op = getEnumConstant(Operation.class, "ADD");
    System.out.println("Got constant: " + op);
    // Expected: Got constant: ADD

    // Print all Operation constants
    System.out.println("\nAll Operations:");
    printAllConstants(Operation.class);

    // Enum used as generic type argument
    java.util.List<Status> statusList = Arrays.asList(Status.values());
    System.out.println("\nStatus list size: " + statusList.size());
    // Expected: Status list size: 3
  }
}
