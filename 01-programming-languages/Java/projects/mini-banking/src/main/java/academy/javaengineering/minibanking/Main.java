package academy.javaengineering.minibanking;

import academy.javaengineering.minibanking.concurrent.ConcurrentAccountManager;
import academy.javaengineering.minibanking.exception.AccountNotFoundException;
import academy.javaengineering.minibanking.exception.InsufficientFundsException;
import academy.javaengineering.minibanking.exception.InvalidAmountException;
import academy.javaengineering.minibanking.logging.AuditLogger;
import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;
import academy.javaengineering.minibanking.repository.AccountRepository;
import academy.javaengineering.minibanking.repository.InMemoryAccountRepository;
import academy.javaengineering.minibanking.service.AccountService;
import academy.javaengineering.minibanking.service.ReportingService;
import academy.javaengineering.minibanking.service.TransactionService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main entry point demonstrating all Java concepts in the Mini Banking System.
 *
 * <p>Engineering Topics Demonstrated in this file:
 * - OOP: Creating and using objects, polymorphism via interfaces
 * - Collections: List operations
 * - Generics: Using type-safe repository
 * - Streams: Processing collections
 * - Concurrency: Thread pool and concurrent operations
 * - Exception Handling: Try-catch-finally with custom exceptions
 * - Logging: SLF4J with MDC</p>
 *
 * <p>Engineering Decision: Demonstrate all concepts in main().
 * WHY: Shows how all individual concepts work together in a realistic scenario.
 * Each section is clearly labeled for educational purposes.</p>
 */
public class Main {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        AuditLogger.setupContext("main-session");

        try {
            demonstrateAccountCreation();
            demonstrateDepositsAndWithdrawals();
            demonstrateTransactionHistory();
            demonstrateReporting();
            demonstrateConcurrentOperations();
            demonstrateErrorHandling();
        } finally {
            AuditLogger.clearContext();
        }
    }

    /**
     * Demonstrates OOP concepts: classes, encapsulation, constructors.
     */
    private static void demonstrateAccountCreation() {
        logger.info("=== ACCOUNT CREATION DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);

        Account alice = accountService.createAccount("Alice Johnson");
        Account bob = accountService.createAccount("Bob Smith");

        logger.info("Created accounts:");
        logger.info("  {}", alice);
        logger.info("  {}", bob);

        System.out.println("\n--- Account Creation ---");
        System.out.println("Alice: " + alice);
        System.out.println("Bob: " + bob);
    }

    /**
     * Demonstrates business logic with deposits and withdrawals.
     */
    private static void demonstrateDepositsAndWithdrawals() {
        logger.info("=== DEPOSIT & WITHDRAWAL DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);

        Account account = accountService.createAccount("Charlie Brown");

        System.out.println("\n--- Deposits & Withdrawals ---");
        System.out.println("Initial balance: " + account.getBalance());

        accountService.deposit(account.getId(), 1000.00);
        System.out.println("After $1000 deposit: " + accountService.getBalance(account.getId()));

        accountService.deposit(account.getId(), 500.00);
        System.out.println("After $500 deposit: " + accountService.getBalance(account.getId()));

        accountService.withdraw(account.getId(), 200.00);
        System.out.println("After $200 withdrawal: " + accountService.getBalance(account.getId()));

        accountService.withdraw(account.getId(), 300.00);
        System.out.println("After $300 withdrawal: " + accountService.getBalance(account.getId()));

        System.out.println("Final balance: " + accountService.getBalance(account.getId()));
    }

    /**
     * Demonstrates transaction history and Streams.
     */
    private static void demonstrateTransactionHistory() {
        logger.info("=== TRANSACTION HISTORY DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);
        TransactionService transactionService = new TransactionService(accountService);

        Account account = accountService.createAccount("Diana Prince");

        accountService.deposit(account.getId(), 500.00);
        accountService.deposit(account.getId(), 300.00);
        accountService.withdraw(account.getId(), 100.00);
        accountService.deposit(account.getId(), 200.00);
        accountService.withdraw(account.getId(), 50.00);

        System.out.println("\n--- Transaction History ---");
        List<Transaction> history = transactionService.getTransactionHistory(account.getId());
        history.forEach(t -> System.out.printf("  %s: %s $%.2f%n",
                t.getType().getDisplayName(), t.getAccountId(), t.getAmount()));

        System.out.println("\n--- Deposits Only ---");
        List<Transaction> deposits = transactionService.getTransactionsByType(TransactionType.DEPOSIT);
        deposits.forEach(t -> System.out.printf("  Deposit: $%.2f%n", t.getAmount()));

        System.out.println("\n--- Grouped by Account ---");
        transactionService.groupTransactionsByAccount().forEach((id, txns) ->
                System.out.printf("  Account %s: %d transactions%n", id, txns.size()));
    }

    /**
     * Demonstrates ReportingService with Streams analytics.
     */
    private static void demonstrateReporting() {
        logger.info("=== REPORTING DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);
        ReportingService reportingService = new ReportingService(repository, accountService);

        // Create accounts with transactions
        Account acc1 = accountService.createAccount("Eve Wilson");
        Account acc2 = accountService.createAccount("Frank Miller");

        accountService.deposit(acc1.getId(), 1000.00);
        accountService.deposit(acc1.getId(), 500.00);
        accountService.withdraw(acc1.getId(), 200.00);

        accountService.deposit(acc2.getId(), 800.00);
        accountService.withdraw(acc2.getId(), 300.00);
        accountService.deposit(acc2.getId(), 150.00);

        System.out.println("\n--- Reporting Analytics ---");
        System.out.printf("Total Deposits: $%.2f%n", reportingService.getTotalDeposits());
        System.out.printf("Total Withdrawals: $%.2f%n", reportingService.getTotalWithdrawals());

        System.out.println("\n--- Account Summaries ---");
        reportingService.getAccountSummary().forEach(s -> System.out.println("  " + s));

        System.out.println("\n--- Transactions Above $400 ---");
        reportingService.getTransactionsAboveAmount(400.00)
                .forEach(t -> System.out.printf("  %s: $%.2f%n", t.getType(), t.getAmount()));

        System.out.println("\n--- Transaction Counts by Type ---");
        reportingService.getTransactionCountByType().forEach((type, count) ->
                System.out.printf("  %s: %d%n", type, count));

        System.out.println("\n--- Net Flow by Account ---");
        reportingService.getNetFlowByAccount().forEach((id, net) ->
                System.out.printf("  Account %s: net $%.2f%n", id, net));
    }

    /**
     * Demonstrates concurrent operations with thread safety.
     */
    private static void demonstrateConcurrentOperations() {
        logger.info("=== CONCURRENT OPERATIONS DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);
        ConcurrentAccountManager concurrentManager = new ConcurrentAccountManager(repository);

        Account account = accountService.createAccount("Grace Lee");
        concurrentManager.deposit(account.getId(), 1000.00);

        System.out.println("\n--- Concurrent Operations ---");
        System.out.println("Initial balance: " + account.getBalance());

        // Execute concurrent deposits
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            final int amount = 100 * (i + 1);
            executor.submit(() -> {
                try {
                    concurrentManager.deposit(account.getId(), amount);
                    System.out.printf("Thread %s: Deposited $%d%n",
                            Thread.currentThread().getName(), amount);
                } catch (Exception e) {
                    logger.error("Concurrent deposit failed", e);
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final balance after concurrent deposits: " + account.getBalance());
        System.out.println("Lock hold count: " + concurrentManager.getLockHoldCount());
    }

    /**
     * Demonstrates exception handling with custom exceptions.
     */
    private static void demonstrateErrorHandling() {
        logger.info("=== ERROR HANDLING DEMONSTRATION ===");

        AccountRepository<Account> repository = new InMemoryAccountRepository();
        AccountService accountService = new AccountService(repository);

        Account account = accountService.createAccount("Henry Davis");
        accountService.deposit(account.getId(), 100.00);

        System.out.println("\n--- Error Handling ---");

        // Test invalid amount
        try {
            accountService.deposit(account.getId(), -50.00);
        } catch (InvalidAmountException e) {
            System.out.println("Caught InvalidAmountException: " + e.getMessage());
        }

        // Test insufficient funds
        try {
            accountService.withdraw(account.getId(), 500.00);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
            System.out.printf("  Requested: $%.2f, Available: $%.2f%n",
                    e.getRequestedAmount(), e.getAvailableBalance());
        }

        // Test account not found
        try {
            accountService.getAccount("nonexistent-id");
        } catch (AccountNotFoundException e) {
            System.out.println("Caught AccountNotFoundException: " + e.getMessage());
        }

        System.out.println("\n--- Exception Hierarchy ---");
        System.out.println("InvalidAmountException extends RuntimeException");
        System.out.println("InsufficientFundsException extends RuntimeException");
        System.out.println("AccountNotFoundException extends RuntimeException");
        System.out.println("All are unchecked for cleaner API design.");
    }
}
