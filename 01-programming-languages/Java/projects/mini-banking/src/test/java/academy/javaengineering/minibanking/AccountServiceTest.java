package academy.javaengineering.minibanking;

import academy.javaengineering.minibanking.exception.AccountNotFoundException;
import academy.javaengineering.minibanking.exception.InsufficientFundsException;
import academy.javaengineering.minibanking.exception.InvalidAmountException;
import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.repository.AccountRepository;
import academy.javaengineering.minibanking.repository.InMemoryAccountRepository;
import academy.javaengineering.minibanking.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for AccountService.
 *
 * <p>Engineering Decision: Using JUnit 5 over JUnit 4.
 * WHY: JUnit 5 provides @Nested tests for better organization,
 * @DisplayName for readable output, and @ExtendWith for extensions.
 * These features improve test maintainability and reporting.</p>
 *
 * <p>Engineering Decision: Using @Nested for logical grouping.
 * WHY: Grouping related tests under nested classes improves readability
 * and allows independent setup/teardown for each group.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - JUnit 5 annotations (@Test, @BeforeEach, @Nested, @DisplayName)
 * - Assertions (assertEquals, assertThrows, assertNotNull)
 * - Exception testing with assertThrows
 * - Test organization and naming conventions</p>
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountRepository<Account> repository;
    private AccountService accountService;

    /**
     * Setup before each test.
     *
     * <p>Engineering Decision: Using real repository in integration tests.
     * WHY: For AccountService tests, we want to test real behavior including
     * repository interaction. InMemoryAccountRepository is fast and reliable.</p>
     */
    @BeforeEach
    void setUp() {
        repository = new InMemoryAccountRepository();
        accountService = new AccountService(repository);
    }

    @Nested
    @DisplayName("Account Creation Tests")
    class AccountCreationTests {

        @Test
        @DisplayName("Should create account with zero balance")
        void shouldCreateAccountWithZeroBalance() {
            // Arrange
            String owner = "Test User";

            // Act
            Account account = accountService.createAccount(owner);

            // Assert
            assertNotNull(account);
            assertNotNull(account.getId());
            assertEquals(owner, account.getOwner());
            assertEquals(0.0, account.getBalance());
        }

        @Test
        @DisplayName("Should create account with unique ID")
        void shouldCreateAccountWithUniqueId() {
            // Act
            Account account1 = accountService.createAccount("User 1");
            Account account2 = accountService.createAccount("User 2");

            // Assert
            assertNotEquals(account1.getId(), account2.getId());
        }

        @Test
        @DisplayName("Should throw exception for null owner")
        void shouldThrowExceptionForNullOwner() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> accountService.createAccount(null));
        }

        @Test
        @DisplayName("Should throw exception for blank owner")
        void shouldThrowExceptionForBlankOwner() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> accountService.createAccount(""));
        }
    }

    @Nested
    @DisplayName("Deposit Tests")
    class DepositTests {

        @Test
        @DisplayName("Should deposit amount successfully")
        void shouldDepositAmountSuccessfully() {
            // Arrange
            Account account = accountService.createAccount("Depositor");

            // Act
            accountService.deposit(account.getId(), 100.00);

            // Assert
            assertEquals(100.00, account.getBalance());
        }

        @Test
        @DisplayName("Should accumulate multiple deposits")
        void shouldAccumulateMultipleDeposits() {
            // Arrange
            Account account = accountService.createAccount("Depositor");

            // Act
            accountService.deposit(account.getId(), 100.00);
            accountService.deposit(account.getId(), 200.00);
            accountService.deposit(account.getId(), 50.00);

            // Assert
            assertEquals(350.00, account.getBalance());
        }

        @Test
        @DisplayName("Should throw exception for negative deposit")
        void shouldThrowExceptionForNegativeDeposit() {
            // Arrange
            Account account = accountService.createAccount("Depositor");

            // Act & Assert
            assertThrows(InvalidAmountException.class,
                    () -> accountService.deposit(account.getId(), -100.00));
        }

        @Test
        @DisplayName("Should throw exception for zero deposit")
        void shouldThrowExceptionForZeroDeposit() {
            // Arrange
            Account account = accountService.createAccount("Depositor");

            // Act & Assert
            assertThrows(InvalidAmountException.class,
                    () -> accountService.deposit(account.getId(), 0.0));
        }

        @Test
        @DisplayName("Should throw exception for non-existent account")
        void shouldThrowExceptionForNonExistentAccount() {
            // Act & Assert
            assertThrows(AccountNotFoundException.class,
                    () -> accountService.deposit("nonexistent", 100.00));
        }
    }

    @Nested
    @DisplayName("Withdrawal Tests")
    class WithdrawalTests {

        @Test
        @DisplayName("Should withdraw amount successfully")
        void shouldWithdrawAmountSuccessfully() {
            // Arrange
            Account account = accountService.createAccount("Withdrawer");
            accountService.deposit(account.getId(), 500.00);

            // Act
            accountService.withdraw(account.getId(), 200.00);

            // Assert
            assertEquals(300.00, account.getBalance());
        }

        @Test
        @DisplayName("Should throw exception for insufficient funds")
        void shouldThrowExceptionForInsufficientFunds() {
            // Arrange
            Account account = accountService.createAccount("Withdrawer");
            accountService.deposit(account.getId(), 100.00);

            // Act & Assert
            InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                    () -> accountService.withdraw(account.getId(), 200.00));

            assertEquals(account.getId(), exception.getAccountId());
            assertEquals(200.00, exception.getRequestedAmount());
            assertEquals(100.00, exception.getAvailableBalance());
        }

        @Test
        @DisplayName("Should throw exception for negative withdrawal")
        void shouldThrowExceptionForNegativeWithdrawal() {
            // Arrange
            Account account = accountService.createAccount("Withdrawer");
            accountService.deposit(account.getId(), 100.00);

            // Act & Assert
            assertThrows(InvalidAmountException.class,
                    () -> accountService.withdraw(account.getId(), -50.00));
        }

        @Test
        @DisplayName("Should throw exception for zero withdrawal")
        void shouldThrowExceptionForZeroWithdrawal() {
            // Arrange
            Account account = accountService.createAccount("Withdrawer");

            // Act & Assert
            assertThrows(InvalidAmountException.class,
                    () -> accountService.withdraw(account.getId(), 0.0));
        }
    }

    @Nested
    @DisplayName("Balance Tests")
    class BalanceTests {

        @Test
        @DisplayName("Should return correct balance")
        void shouldReturnCorrectBalance() {
            // Arrange
            Account account = accountService.createAccount("BalanceUser");
            accountService.deposit(account.getId(), 1000.00);
            accountService.withdraw(account.getId(), 300.00);

            // Act
            double balance = accountService.getBalance(account.getId());

            // Assert
            assertEquals(700.00, balance);
        }

        @Test
        @DisplayName("Should throw exception for non-existent account balance")
        void shouldThrowExceptionForNonExistentAccountBalance() {
            // Act & Assert
            assertThrows(AccountNotFoundException.class,
                    () -> accountService.getBalance("nonexistent"));
        }
    }

    @Nested
    @DisplayName("Transaction History Tests")
    class TransactionHistoryTests {

        @Test
        @DisplayName("Should record transaction history")
        void shouldRecordTransactionHistory() {
            // Arrange
            Account account = accountService.createAccount("HistoryUser");

            // Act
            accountService.deposit(account.getId(), 100.00);
            accountService.withdraw(account.getId(), 50.00);

            // Assert
            assertEquals(2, accountService.getTransactionHistory(account.getId()).size());
        }

        @Test
        @DisplayName("Should return empty history for new account")
        void shouldReturnEmptyHistoryForNewAccount() {
            // Arrange
            Account account = accountService.createAccount("NewUser");

            // Act & Assert
            assertTrue(accountService.getTransactionHistory(account.getId()).isEmpty());
        }
    }
}
