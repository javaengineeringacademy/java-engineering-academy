package academy.javaengineering.exceptions.questions;

/**
 * Question 7: Custom checked exception
 *
 * Task: Create a custom checked exception called InsufficientFundsException.
 * It should have a field for the deficit amount and a method to get it.
 */
public class Question07_CustomCheckedException {

    // TODO: Create InsufficientFundsException that extends Exception
    // It should have:
    // - A private final double deficit field
    // - A constructor that takes the deficit amount
    // - A getDeficit() method
    // - A message that includes the deficit

    public static void withdraw(double balance, double amount) throws Exception {
        // TODO: If amount > balance, throw InsufficientFundsException
        // with the deficit (amount - balance)
    }

    public static void main(String[] args) {
        try {
            withdraw(100.0, 50.0);
            System.out.println("Withdrawal successful");
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }

        try {
            withdraw(100.0, 150.0);
            System.out.println("Withdrawal successful");
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }
}
