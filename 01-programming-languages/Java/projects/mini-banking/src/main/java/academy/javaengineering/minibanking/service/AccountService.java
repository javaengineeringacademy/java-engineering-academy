package academy.javaengineering.minibanking.service;

import academy.javaengineering.minibanking.exception.AccountNotFoundException;
import academy.javaengineering.minibanking.exception.InsufficientFundsException;
import academy.javaengineering.minibanking.exception.InvalidAmountException;
import academy.javaengineering.minibanking.logging.AuditLogger;
import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;
import academy.javaengineering.minibanking.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Business logic service for account operations.
 *
 * <p>Engineering Decision: Service layer separate from repository.
 * WHY: Separation of concerns - repository handles data access, service handles
 * business logic. This enables testing business logic independently and
 * swapping data access implementations (in-memory, database, etc.).</p>
 *
 * <p>Engineering Decision: Store transactions in service, not repository.
 * WHY: Transactions are derived from account operations, not independent entities.
 * Storing them in the service keeps the repository focused on account CRUD
 * and allows the service to maintain transaction history as a business concern.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Dependency Injection (repository injected via constructor)
 * - Exception handling (custom exceptions)
 * - Collections (ArrayList for transaction storage)
 * - Logging integration</p>
 */
public class AccountService {

    private final AccountRepository<Account> accountRepository;
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Constructs AccountService with injected repository.
     *
     * <p>Engineering Decision: Constructor injection.
     * WHY: Constructor injection ensures the service cannot be created without
     * a repository, failing fast at initialization rather than at runtime.</p>
     *
     * @param accountRepository the repository for account persistence
     */
    public AccountService(AccountRepository<Account> accountRepository) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new account with zero initial balance.
     *
     * @param owner the account owner's name
     * @return the created account
     * @throws IllegalArgumentException if owner is null or blank
     */
    public Account createAccount(String owner) {
        AuditLogger.setupContext(null);
        try {
            String id = UUID.randomUUID().toString();
            Account account = new Account(id, owner);
            accountRepository.save(account);
            AuditLogger.logAccountCreated(id, owner);
            return account;
        } finally {
            AuditLogger.clearContext();
        }
    }

    /**
     * Retrieves an account by ID.
     *
     * @param accountId the account ID
     * @return the account
     * @throws AccountNotFoundException if account doesn't exist
     */
    public Account getAccount(String accountId) {
        AuditLogger.setupContext(null);
        try {
            return accountRepository.findById(accountId)
                    .orElseThrow(() -> new AccountNotFoundException(accountId));
        } finally {
            AuditLogger.clearContext();
        }
    }

    /**
     * Deposits amount into specified account.
     *
     * @param accountId the target account ID
     * @param amount    the amount to deposit
     * @return the updated account
     * @throws AccountNotFoundException  if account doesn't exist
     * @throws InvalidAmountException    if amount is zero or negative
     */
    public Account deposit(String accountId, double amount) {
        AuditLogger.setupContext(null);
        try {
            Account account = getAccount(accountId);
            account.deposit(amount);

            Transaction transaction = Transaction.create(
                    UUID.randomUUID().toString(),
                    accountId,
                    TransactionType.DEPOSIT,
                    amount
            );
            transactions.add(transaction);

            AuditLogger.logDeposit(accountId, amount, account.getBalance());
            return account;
        } finally {
            AuditLogger.clearContext();
        }
    }

    /**
     * Withdraws amount from specified account.
     *
     * @param accountId the source account ID
     * @param amount    the amount to withdraw
     * @return the updated account
     * @throws AccountNotFoundException   if account doesn't exist
     * @throws InvalidAmountException     if amount is zero or negative
     * @throws InsufficientFundsException if amount exceeds balance
     */
    public Account withdraw(String accountId, double amount) {
        AuditLogger.setupContext(null);
        try {
            Account account = getAccount(accountId);
            account.withdraw(amount);

            Transaction transaction = Transaction.create(
                    UUID.randomUUID().toString(),
                    accountId,
                    TransactionType.WITHDRAWAL,
                    amount
            );
            transactions.add(transaction);

            AuditLogger.logWithdrawal(accountId, amount, account.getBalance());
            return account;
        } catch (InvalidAmountException | InsufficientFundsException e) {
            AuditLogger.logWithdrawalFailed(accountId, amount, e.getMessage());
            throw e;
        } finally {
            AuditLogger.clearContext();
        }
    }

    /**
     * Gets current balance for an account.
     *
     * @param accountId the account ID
     * @return the current balance
     * @throws AccountNotFoundException if account doesn't exist
     */
    public double getBalance(String accountId) {
        return getAccount(accountId).getBalance();
    }

    /**
     * Gets transaction history for an account.
     *
     * <p>Engineering Decision: Return new ArrayList copy.
     * WHY: Prevents external modification of internal transaction list.
     * Defensive copying maintains encapsulation.</p>
     *
     * @param accountId the account ID
     * @return list of transactions for this account
     * @throws AccountNotFoundException if account doesn't exist
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        getAccount(accountId); // Validate account exists
        return transactions.stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .toList();
    }

    /**
     * Gets all transactions across all accounts.
     *
     * @return unmodifiable list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return List.copyOf(transactions);
    }
}
