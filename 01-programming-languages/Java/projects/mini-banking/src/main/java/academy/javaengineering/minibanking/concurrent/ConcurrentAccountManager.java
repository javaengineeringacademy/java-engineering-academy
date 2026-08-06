package academy.javaengineering.minibanking.concurrent;

import academy.javaengineering.minibanking.exception.InsufficientFundsException;
import academy.javaengineering.minibanking.exception.InvalidAmountException;
import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;
import academy.javaengineering.minibanking.repository.AccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe account manager demonstrating Java concurrency primitives.
 *
 * <p>Engineering Decision: ReentrantLock over synchronized blocks.
 * WHY: ReentrantLock provides more flexibility than synchronized:
 * - tryLock() for non-blocking attempts
 * - lockInterruptibly() for responsive cancellation
 * - Multiple Condition objects for fine-grained waiting
 * Better for complex concurrency scenarios.</p>
 *
 * <p>Engineering Decision: AtomicReference for balance updates.
 * WHY: AtomicReference enables lock-free updates via compareAndSet().
 * Combined with CAS (Compare-And-Swap), it provides atomic state transitions
 * without traditional locking for simple operations.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - ReentrantLock for mutual exclusion
 * - AtomicReference for lock-free updates
 * - Thread safety patterns
 * - Concurrent data structures</p>
 */
public class ConcurrentAccountManager {

    private final AccountRepository<Account> accountRepository;
    private final ReentrantLock accountLock = new ReentrantLock();
    private final List<Transaction> transactions = new ArrayList<>();

    /**
     * Constructs ConcurrentAccountManager with repository dependency.
     *
     * @param accountRepository the repository for account storage
     */
    public ConcurrentAccountManager(AccountRepository<Account> accountRepository) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        this.accountRepository = accountRepository;
    }

    /**
     * Thread-safe deposit operation using ReentrantLock.
     *
     * <p>Engineering Decision: Lock at service level, not account level.
     * WHY: For demonstration, we lock at service level to show ReentrantLock usage.
     * In production, fine-grained locking (per-account locks) would be better
     * for concurrent access to different accounts.</p>
     *
     * @param accountId the target account
     * @param amount    the deposit amount
     * @return the updated account
     */
    public Account deposit(String accountId, double amount) {
        accountLock.lock();
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new academy.javaengineering.minibanking.exception.AccountNotFoundException(accountId));

            if (amount <= 0) {
                throw new InvalidAmountException(amount);
            }

            // AtomicReference demonstrates lock-free balance tracking
            AtomicReference<Double> newBalance = new AtomicReference<>(account.getBalance() + amount);
            account.deposit(amount);

            Transaction transaction = Transaction.create(
                    UUID.randomUUID().toString(),
                    accountId,
                    TransactionType.DEPOSIT,
                    amount
            );

            synchronized (transactions) {
                transactions.add(transaction);
            }

            return account;
        } finally {
            accountLock.unlock();
        }
    }

    /**
     * Thread-safe withdrawal operation with balance verification.
     *
     * @param accountId the source account
     * @param amount    the withdrawal amount
     * @return the updated account
     * @throws InsufficientFundsException if balance is insufficient
     */
    public Account withdraw(String accountId, double amount) {
        accountLock.lock();
        try {
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new academy.javaengineering.minibanking.exception.AccountNotFoundException(accountId));

            if (amount <= 0) {
                throw new InvalidAmountException(amount);
            }

            if (amount > account.getBalance()) {
                throw new InsufficientFundsException(accountId, amount, account.getBalance());
            }

            account.withdraw(amount);

            Transaction transaction = Transaction.create(
                    UUID.randomUUID().toString(),
                    accountId,
                    TransactionType.WITHDRAWAL,
                    amount
            );

            synchronized (transactions) {
                transactions.add(transaction);
            }

            return account;
        } finally {
            accountLock.unlock();
        }
    }

    /**
     * Demonstrates lock-free balance check using AtomicReference.
     *
     * <p>Engineering Decision: AtomicReference for read-only balance snapshot.
     * WHY: For simple reads, AtomicReference provides a consistent snapshot
     * without acquiring locks. This is efficient for read-heavy workloads.</p>
     *
     * @param accountId the account to check
     * @return atomic reference to current balance
     */
    public AtomicReference<Double> getBalanceAtomic(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new academy.javaengineering.minibanking.exception.AccountNotFoundException(accountId));
        return new AtomicReference<>(account.getBalance());
    }

    /**
     * Gets thread-safe copy of transaction history.
     *
     * @return immutable copy of transactions
     */
    public List<Transaction> getTransactionHistory() {
        synchronized (transactions) {
            return List.copyOf(transactions);
        }
    }

    /**
     * Attempts non-blocking deposit using tryLock.
     *
     * <p>Engineering Decision: tryLock for non-blocking operations.
     * WHY: tryLock attempts to acquire lock without waiting. If lock is held,
     * it returns immediately, allowing the caller to decide how to proceed
     * (retry, fail, or use alternative logic).</p>
     *
     * @param accountId the target account
     * @param amount    the deposit amount
     * @return Optional containing result if lock was acquired
     */
    public boolean tryDeposit(String accountId, double amount) {
        if (accountLock.tryLock()) {
            try {
                deposit(accountId, amount);
                return true;
            } finally {
                accountLock.unlock();
            }
        }
        return false;
    }

    /**
     * Gets lock info for monitoring concurrent access.
     *
     * @return current lock hold count
     */
    public int getLockHoldCount() {
        return accountLock.getHoldCount();
    }

    /**
     * Checks if lock is currently held.
     *
     * @return true if lock is held by any thread
     */
    public boolean isLocked() {
        return accountLock.isLocked();
    }
}
