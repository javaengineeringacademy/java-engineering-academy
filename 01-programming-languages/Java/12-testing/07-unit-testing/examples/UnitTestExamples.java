package academy.javaengineering.testing.unit.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnitTestExamples {

    // Pure function - easy to test
    static class MathUtils {
        static int factorial(int n) {
            if (n < 0) throw new IllegalArgumentException("Negative input");
            if (n <= 1) return 1;
            return n * factorial(n - 1);
        }

        static boolean isPrime(int n) {
            if (n < 2) return false;
            for (int i = 2; i <= Math.sqrt(n); i++) {
                if (n % i == 0) return false;
            }
            return true;
        }
    }

    @Nested
    class PureFunctionTests {
        @Test
        void shouldCalculateFactorial() {
            assertEquals(1, MathUtils.factorial(0));
            assertEquals(1, MathUtils.factorial(1));
            assertEquals(120, MathUtils.factorial(5));
        }

        @Test
        void shouldRejectNegativeFactorial() {
            assertThrows(IllegalArgumentException.class,
                () -> MathUtils.factorial(-1));
        }

        @Test
        void shouldIdentifyPrimes() {
            assertFalse(MathUtils.isPrime(1));
            assertTrue(MathUtils.isPrime(2));
            assertTrue(MathUtils.isPrime(17));
            assertFalse(MathUtils.isPrime(15));
        }
    }

    // Stateful object
    static class BankAccount {
        private double balance;
        private final List<String> transactions = new ArrayList<>();

        BankAccount(double initialBalance) {
            this.balance = initialBalance;
        }

        void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Positive amount required");
            balance += amount;
            transactions.add("DEPOSIT:" + amount);
        }

        void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("Positive amount required");
            if (amount > balance) throw new IllegalStateException("Insufficient funds");
            balance -= amount;
            transactions.add("WITHDRAW:" + amount);
        }

        double getBalance() { return balance; }
        List<String> getTransactions() { return Collections.unmodifiableList(transactions); }
    }

    @Nested
    class StatefulObjectTests {
        private BankAccount account;

        @BeforeEach
        void setUp() {
            account = new BankAccount(1000);
        }

        @Test
        void shouldInitializeWithBalance() {
            assertEquals(1000, account.getBalance(), 0.01);
        }

        @Test
        void shouldDeposit() {
            account.deposit(500);
            assertEquals(1500, account.getBalance(), 0.01);
        }

        @Test
        void shouldWithdraw() {
            account.withdraw(300);
            assertEquals(700, account.getBalance(), 0.01);
        }

        @Test
        void shouldRejectWithdrawMoreThanBalance() {
            assertThrows(IllegalStateException.class,
                () -> account.withdraw(2000));
        }

        @Test
        void shouldTrackTransactions() {
            account.deposit(100);
            account.withdraw(50);
            assertEquals(2, account.getTransactions().size());
        }
    }

    // Service with mocked dependency
    interface NotificationService {
        void send(String message);
    }

    static class OrderService {
        private final NotificationService notificationService;
        private final List<String> orders = new ArrayList<>();

        OrderService(NotificationService notificationService) {
            this.notificationService = notificationService;
        }

        String createOrder(String product) {
            String orderId = "ORD-" + (orders.size() + 1);
            orders.add(orderId);
            notificationService.send("Order " + orderId + " created for " + product);
            return orderId;
        }
    }

    @ExtendWith(MockitoExtension.class)
    @Nested
    class ServiceWithMockTests {
        @Mock
        private NotificationService notificationService;

        @Test
        void shouldCreateOrderAndNotify() {
            OrderService service = new OrderService(notificationService);
            String orderId = service.createOrder("Laptop");
            assertNotNull(orderId);
            verify(notificationService).send("Order " + orderId + " created for Laptop");
        }
    }
}
