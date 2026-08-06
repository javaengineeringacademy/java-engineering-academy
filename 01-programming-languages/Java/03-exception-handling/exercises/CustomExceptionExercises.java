package academy.javaengineering.exercises;

/**
 * Exercises: Custom Exceptions
 *
 * Complete the TODO sections below.
 */
public class CustomExceptionExercises {

    // TODO 1: Create a custom exception InsufficientFundsException
    // It should have a constructor that takes a double (the deficit amount)
    // and a method getDeficit() that returns that amount
    // The exception should extend Exception
    // Uncomment and implement the class below:
    /*
    public static class InsufficientFundsException extends Exception {
        // TODO: implement
    }
    */

    // TODO 2: Create InsufficientFundsException with message and cause support
    // It should support constructors: (String message), (Throwable cause),
    // (String message, Throwable cause)
    // Uncomment and implement:
    /*
    public static class InsufficientFundsException extends Exception {
        // TODO: implement with all constructors
    }
    */

    // TODO 3: Implement BankAccount that uses InsufficientFundsException
    // Fields: balance (double)
    // Constructor takes initial balance
    // deposit(double amount) adds to balance, throws IllegalArgumentException if amount <= 0
    // withdraw(double amount) subtracts from balance, throws InsufficientFundsException if amount > balance
    // getBalance() returns current balance
    public static class BankAccount {
        private double balance;

        public BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        public void deposit(double amount) {
            // TODO: implement
        }

        public void withdraw(double amount) throws InsufficientFundsException {
            // TODO: implement
        }

        public double getBalance() {
            return balance;
        }
    }

    // TODO 4: Implement validateAge that creates a custom exception
    // Create AgeOutOfRangeException (extends IllegalArgumentException)
    // if age is not between 0 and 150
    public int validateAge(int age) {
        // TODO: implement, throw AgeOutOfRangeException if invalid
        return age;
    }

    // TODO 5: Implement processWithRetry that catches an exception
    // and retries up to maxRetries times before rethrowing
    // Use a functional interface: () -> { ... } that may throw Exception
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    public <T> T processWithRetry(ThrowingSupplier<T> operation, int maxRetries) throws Exception {
        // TODO: implement retry logic
        return operation.get();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        CustomExceptionExercises exercises = new CustomExceptionExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== CustomExceptionExercises Tests ===\n");

        // Test 3
        total++;
        try {
            BankAccount account = new BankAccount(100);
            account.deposit(50);
            if (Math.abs(account.getBalance() - 150) < 0.01) {
                System.out.println("Test 3a PASSED: BankAccount deposit");
                passed++;
            } else {
                System.out.println("Test 3a FAILED: BankAccount deposit - balance " + account.getBalance());
            }
        } catch (Exception e) {
            System.out.println("Test 3a FAILED: BankAccount deposit - " + e.getMessage());
        }

        total++;
        try {
            BankAccount account = new BankAccount(100);
            account.withdraw(150);
            System.out.println("Test 3b FAILED: should throw InsufficientFundsException");
        } catch (InsufficientFundsException e) {
            System.out.println("Test 3b PASSED: BankAccount withdraw insufficient");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 3b FAILED: wrong exception type");
        }

        // Test 4
        total++;
        try {
            exercises.validateAge(25);
            System.out.println("Test 4 PASSED: validateAge valid");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: validateAge - " + e.getMessage());
        }

        total++;
        try {
            exercises.validateAge(-5);
            System.out.println("Test 4b FAILED: should throw on negative age");
        } catch (IllegalArgumentException e) {
            System.out.println("Test 4b PASSED: validateAge throws on negative");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 4b FAILED: wrong exception type");
        }

        // Test 5
        total++;
        try {
            int[] counter = {0};
            Integer result = exercises.processWithRetry(() -> {
                counter[0]++;
                if (counter[0] < 3) throw new RuntimeException("not yet");
                return 42;
            }, 5);
            if (result == 42 && counter[0] == 3) {
                System.out.println("Test 5 PASSED: processWithRetry");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: processWithRetry - result=" + result + " attempts=" + counter[0]);
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: processWithRetry - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
