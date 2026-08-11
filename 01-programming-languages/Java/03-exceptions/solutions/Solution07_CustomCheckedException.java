package academy.javaengineering.exceptions.solutions;

/**
 * Solution 7: Custom checked exception
 *
 * Create InsufficientFundsException with deficit field.
 */
public class Solution07_CustomCheckedException {

    public static class InsufficientFundsException extends Exception {
        private final double deficit;

        public InsufficientFundsException(double deficit) {
            super("Insufficient funds. Deficit: $" + deficit);
            this.deficit = deficit;
        }

        public double getDeficit() {
            return deficit;
        }
    }

    public static void withdraw(double balance, double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount - balance);
        }
        System.out.println("Withdrew: $" + amount);
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
