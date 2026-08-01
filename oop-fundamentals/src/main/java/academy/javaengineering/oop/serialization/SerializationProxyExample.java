package academy.javaengineering.oop.serialization;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * Demonstrates the Serialization Proxy pattern for safe serialization.
 *
 * <p>The Serialization Proxy pattern prevents attacks like the {@code readObjectNoData}
 * attack and allows you to serialize objects without exposing their internal structure.
 * The outer class delegates serialization to a static inner class (the proxy).</p>
 *
 * <p>Expected Output:</p>
 * <pre>
 * Original: Money{amount=100.00, currency=USD}
 * Deserialized: Money{amount=100.00, currency=USD}
 * Values match: true
 * </pre>
 */
public class SerializationProxyExample {

  /**
   * A Money class that uses the Serialization Proxy pattern.
   */
  private static final class Money implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double amount;
    private final String currency;

    Money(double amount, String currency) {
      this.amount = amount;
      this.currency = currency;
    }

    private Object writeReplace() {
      return new MoneyProxy(this);
    }

    private void readObject(ObjectInputStream in) throws java.io.InvalidObjectException {
      throw new java.io.InvalidObjectException("Proxy required");
    }

    @Override
    public String toString() {
      return "Money{amount=%.2f, currency='%s'}".formatted(amount, currency);
    }

    /**
     * The serialization proxy for Money.
     */
    private static final class MoneyProxy implements Serializable {

      private static final long serialVersionUID = 1L;

      private double amount;
      private String currency;

      MoneyProxy(Money money) {
        this.amount = money.amount;
        this.currency = money.currency;
      }

      private Object readResolve() {
        return new Money(amount, currency);
      }
    }
  }

  /**
   * Demonstrates serialization proxy pattern in action.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    Money original = new Money(100.00, "USD");
    System.out.println("Original: " + original);

    try (var bos = new java.io.ByteArrayOutputStream();
        var oos = new java.io.ObjectOutputStream(bos)) {
      oos.writeObject(original);

      try (var bis = new java.io.ByteArrayInputStream(bos.toByteArray());
          var ois = new java.io.ObjectInputStream(bis)) {
        Money deserialized = (Money) ois.readObject();
        System.out.println("Deserialized: " + deserialized);
        System.out.println("Values match: "
            + (original.toString().equals(deserialized.toString())));
      }
    } catch (Exception e) {
      System.err.println("Serialization failed: " + e.getMessage());
    }
  }
}
