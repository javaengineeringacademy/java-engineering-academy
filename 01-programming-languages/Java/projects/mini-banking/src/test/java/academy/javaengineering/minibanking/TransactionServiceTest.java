package academy.javaengineering.minibanking;

import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;
import academy.javaengineering.minibanking.repository.AccountRepository;
import academy.javaengineering.minibanking.service.AccountService;
import academy.javaengineering.minibanking.service.ReportingService;
import academy.javaengineering.minibanking.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * JUnit 5 + Mockito tests for TransactionService and ReportingService.
 *
 * <p>Engineering Decision: Using Mockito for dependency isolation.
 * WHY: Mockito creates mock objects that simulate real dependencies.
 * This isolates the unit under test from external concerns (database, network)
 * and allows verification of interactions between components.</p>
 *
 * <p>Engineering Decision: Testing both TransactionService and ReportingService here.
 * WHY: Both services depend on AccountService and use Streams for filtering.
 * Testing them together demonstrates Mockito's capability to mock dependencies
 * while testing real Stream-based logic.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Mockito mocking (@Mock, @InjectMocks)
 * - Mock behavior stubbing (when...thenReturn)
 * - Verification (verify, times)
 * - Argument matchers (anyString)
 * - Nested test classes
 * - Display names for readable output</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private AccountRepository<Account> accountRepository;

    private TransactionService transactionService;
    private ReportingService reportingService;

    private Account testAccount;
    private List<Transaction> testTransactions;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(accountService);
        reportingService = new ReportingService(accountRepository, accountService);

        // Setup test data
        testAccount = new Account("test-123", "Test User");

        testTransactions = Arrays.asList(
                Transaction.create("tx-1", "test-123", TransactionType.DEPOSIT, 100.0),
                Transaction.create("tx-2", "test-123", TransactionType.WITHDRAWAL, 50.0),
                Transaction.create("tx-3", "test-123", TransactionType.DEPOSIT, 200.0),
                Transaction.create("tx-4", "other-456", TransactionType.DEPOSIT, 300.0),
                Transaction.create("tx-5", "test-123", TransactionType.WITHDRAWAL, 75.0)
        );
    }

    @Nested
    @DisplayName("Transaction Filtering Tests")
    class TransactionFilteringTests {

        @Test
        @DisplayName("Should filter transactions by type using Streams")
        void shouldFilterTransactionsByType() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            List<Transaction> deposits = transactionService.getTransactionsByType(TransactionType.DEPOSIT);

            // Assert
            assertEquals(3, deposits.size());
            assertTrue(deposits.stream().allMatch(t -> t.getType() == TransactionType.DEPOSIT));
        }

        @Test
        @DisplayName("Should return empty list when no transactions match type")
        void shouldReturnEmptyListWhenNoTransactionsMatchType() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            List<Transaction> transfers = transactionService.getTransactionsByType(
                    TransactionType.valueOf("TRANSFER"));

            // Assert - using a type that doesn't exist would throw, but we test with valid types
            // This test verifies filtering works correctly
            assertTrue(transfers.isEmpty());
        }

        @Test
        @DisplayName("Should get transaction history for specific account")
        void shouldGetTransactionHistoryForSpecificAccount() {
            // Arrange
            when(accountService.getTransactionHistory("test-123"))
                    .thenReturn(Arrays.asList(
                            testTransactions.get(0),
                            testTransactions.get(1),
                            testTransactions.get(2),
                            testTransactions.get(4)
                    ));

            // Act
            List<Transaction> history = transactionService.getTransactionHistory("test-123");

            // Assert
            assertEquals(4, history.size());
            assertTrue(history.stream().allMatch(t -> t.getAccountId().equals("test-123")));
        }

        @Test
        @DisplayName("Should filter by account and type simultaneously")
        void shouldFilterByAccountAndTypeSimultaneously() {
            // Arrange
            when(accountService.getTransactionHistory("test-123"))
                    .thenReturn(Arrays.asList(
                            testTransactions.get(0),
                            testTransactions.get(1),
                            testTransactions.get(2),
                            testTransactions.get(4)
                    ));

            // Act
            List<Transaction> result = transactionService.getTransactionsByAccountAndType(
                    "test-123", TransactionType.DEPOSIT);

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.stream()
                    .allMatch(t -> t.getType() == TransactionType.DEPOSIT));
        }

        @Test
        @DisplayName("Should group transactions by account using Collectors")
        void shouldGroupTransactionsByAccount() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            var grouped = transactionService.groupTransactionsByAccount();

            // Assert
            assertEquals(2, grouped.size());
            assertEquals(4, grouped.get("test-123").size());
            assertEquals(1, grouped.get("other-456").size());
        }

        @Test
        @DisplayName("Should count transactions by type")
        void shouldCountTransactionsByType() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            long depositCount = transactionService.countTransactionsByType(TransactionType.DEPOSIT);
            long withdrawalCount = transactionService.countTransactionsByType(TransactionType.WITHDRAWAL);

            // Assert
            assertEquals(3, depositCount);
            assertEquals(2, withdrawalCount);
        }
    }

    @Nested
    @DisplayName("Reporting Tests")
    class ReportingTests {

        @Test
        @DisplayName("Should calculate total deposits using Streams")
        void shouldCalculateTotalDeposits() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            double totalDeposits = reportingService.getTotalDeposits();

            // Assert
            assertEquals(600.0, totalDeposits, 0.001);
            verify(accountService, times(1)).getAllTransactions();
        }

        @Test
        @DisplayName("Should calculate total withdrawals using Streams")
        void shouldCalculateTotalWithdrawals() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            double totalWithdrawals = reportingService.getTotalWithdrawals();

            // Assert
            assertEquals(125.0, totalWithdrawals, 0.001);
        }

        @Test
        @DisplayName("Should get transactions above threshold")
        void shouldGetTransactionsAboveThreshold() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            List<Transaction> result = reportingService.getTransactionsAboveAmount(150.0);

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(t -> t.getAmount() > 150.0));
        }

        @Test
        @DisplayName("Should get deposit statistics using summarizingDouble")
        void shouldGetDepositStatistics() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            var stats = reportingService.getDepositStatistics();

            // Assert
            assertEquals(3, stats.getCount());
            assertEquals(600.0, stats.getSum(), 0.001);
            assertEquals(100.0, stats.getMin(), 0.001);
            assertEquals(300.0, stats.getMax(), 0.001);
        }

        @Test
        @DisplayName("Should generate account summary")
        void shouldGenerateAccountSummary() {
            // Arrange
            when(accountRepository.findAll()).thenReturn(List.of(testAccount));
            when(accountService.getTransactionHistory("test-123"))
                    .thenReturn(Arrays.asList(
                            testTransactions.get(0),
                            testTransactions.get(1),
                            testTransactions.get(2),
                            testTransactions.get(4)
                    ));

            // Act
            List<String> summary = reportingService.getAccountSummary();

            // Assert
            assertEquals(1, summary.size());
            assertTrue(summary.get(0).contains("test-123"));
            assertTrue(summary.get(0).contains("Test User"));
        }

        @Test
        @DisplayName("Should group transactions by type with count")
        void shouldGroupTransactionsByType() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            var grouped = reportingService.getTransactionCountByType();

            // Assert
            assertEquals(2, grouped.size());
            assertEquals(3L, grouped.get(TransactionType.DEPOSIT));
            assertEquals(2L, grouped.get(TransactionType.WITHDRAWAL));
        }

        @Test
        @DisplayName("Should calculate net flow by account")
        void shouldCalculateNetFlowByAccount() {
            // Arrange
            when(accountRepository.findAll()).thenReturn(List.of(testAccount));
            when(accountService.getTransactionHistory("test-123"))
                    .thenReturn(Arrays.asList(
                            testTransactions.get(0),  // +100
                            testTransactions.get(1),  // -50
                            testTransactions.get(2),  // +200
                            testTransactions.get(4)   // -75
                    ));

            // Act
            var netFlow = reportingService.getNetFlowByAccount();

            // Assert
            assertEquals(175.0, netFlow.get("test-123"), 0.001);
        }

        @Test
        @DisplayName("Should return empty list when no transactions above threshold")
        void shouldReturnEmptyListWhenNoTransactionsAboveThreshold() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            List<Transaction> result = reportingService.getTransactionsAboveAmount(1000.0);

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Mock Verification Tests")
    class MockVerificationTests {

        @Test
        @DisplayName("Should verify service interactions")
        void shouldVerifyServiceInteractions() {
            // Arrange
            when(accountService.getAllTransactions()).thenReturn(testTransactions);

            // Act
            transactionService.getTransactionsByType(TransactionType.DEPOSIT);

            // Assert
            verify(accountService, times(1)).getAllTransactions();
            verify(accountService, never()).getTransactionHistory(anyString());
        }

        @Test
        @DisplayName("Should verify repository interactions through reporting")
        void shouldVerifyRepositoryInteractions() {
            // Arrange
            when(accountRepository.findAll()).thenReturn(List.of(testAccount));
            when(accountService.getTransactionHistory(anyString())).thenReturn(List.of());

            // Act
            reportingService.getAccountSummary();

            // Assert
            verify(accountRepository, times(1)).findAll();
        }
    }
}
