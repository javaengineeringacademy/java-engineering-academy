package academy.javaengineering.minibanking.service;

import academy.javaengineering.minibanking.model.Account;
import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;
import academy.javaengineering.minibanking.repository.AccountRepository;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Reporting service using Java Streams for analytics and summaries.
 *
 * <p>Engineering Decision: Separate ReportingService for read-heavy operations.
 * WHY: Reporting queries are read-heavy and may need different optimization
 * strategies (caching, materialized views). Separating them from write operations
 * allows independent scaling and optimization.</p>
 *
 * <p>Engineering Decision: Use Streams extensively in this service.
 * WHY: Demonstrates Stream API capabilities for real-world scenarios:
 * - mapToDouble for numeric extraction
 * - reduce for aggregation
 * - groupingBy for categorization
 * - filter for conditional logic
 * - DoubleSummaryStatistics for statistical summaries</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Streams (mapToDouble, reduce, groupingBy, filtering)
 * - Collectors (summarizingDouble, groupingBy, counting)
 * - Method references
 * - Functional composition</p>
 */
public class ReportingService {

    private final AccountRepository<Account> accountRepository;
    private final AccountService accountService;

    /**
     * Constructs ReportingService with dependencies.
     *
     * @param accountRepository for account data access
     * @param accountService    for transaction data
     */
    public ReportingService(AccountRepository<Account> accountRepository, AccountService accountService) {
        if (accountRepository == null) {
            throw new IllegalArgumentException("AccountRepository cannot be null");
        }
        if (accountService == null) {
            throw new IllegalArgumentException("AccountService cannot be null");
        }
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    /**
     * Calculates total deposits across all accounts.
     *
     * <p>Engineering Decision: Using mapToDouble + sum.
     * WHY: mapToDouble converts to DoubleStream which has optimized sum().
     * More efficient than reduce() for summing operations and avoids boxing overhead.</p>
     *
     * @return total deposited amount
     */
    public double getTotalDeposits() {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Calculates total withdrawals across all accounts.
     *
     * @return total withdrawn amount
     */
    public double getTotalWithdrawals() {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    /**
     * Generates account summary with balance and transaction counts.
     *
     * <p>Engineering Decision: Using Collectors.groupingBy with downstream collector.
     * WHY: groupingBy can apply a downstream collector to aggregate values
     * within each group. This single-pass approach is more efficient than
     * multiple separate streams.</p>
     *
     * @return list of summary strings for each account
     */
    public List<String> getAccountSummary() {
        return accountRepository.findAll().stream()
                .map(account -> {
                    List<Transaction> accountTransactions = accountService.getTransactionHistory(account.getId());
                    long depositCount = accountTransactions.stream()
                            .filter(t -> t.getType() == TransactionType.DEPOSIT)
                            .count();
                    long withdrawalCount = accountTransactions.stream()
                            .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                            .count();
                    return String.format("Account %s (%s): Balance=%.2f, Deposits=%d, Withdrawals=%d",
                            account.getId(), account.getOwner(),
                            account.getBalance(), depositCount, withdrawalCount);
                })
                .toList();
    }

    /**
     * Finds transactions above a specified amount.
     *
     * @param threshold the minimum amount (exclusive)
     * @return list of transactions exceeding the threshold
     */
    public List<Transaction> getTransactionsAboveAmount(double threshold) {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getAmount() > threshold)
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .toList();
    }

    /**
     * Generates deposit statistics using DoubleSummaryStatistics.
     *
     * <p>Engineering Decision: Using summarizingDouble collector.
     * WHY: DoubleSummaryStatistics provides count, sum, min, max, and average
     * in a single pass through the data. More efficient than computing each
     * statistic separately.</p>
     *
     * @return summary statistics for deposits
     */
    public DoubleSummaryStatistics getDepositStatistics() {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .summaryStatistics();
    }

    /**
     * Groups transactions by type with count.
     *
     * @return map of transaction type to count
     */
    public Map<TransactionType, Long> getTransactionCountByType() {
        return accountService.getAllTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getType, Collectors.counting()));
    }

    /**
     * Calculates net flow (deposits - withdrawals) per account.
     *
     * @return map of accountId to net flow
     */
    public Map<String, Double> getNetFlowByAccount() {
        return accountRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Account::getId,
                        account -> {
                            List<Transaction> txns = accountService.getTransactionHistory(account.getId());
                            double deposits = txns.stream()
                                    .filter(t -> t.getType() == TransactionType.DEPOSIT)
                                    .mapToDouble(Transaction::getAmount)
                                    .sum();
                            double withdrawals = txns.stream()
                                    .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                                    .mapToDouble(Transaction::getAmount)
                                    .sum();
                            return deposits - withdrawals;
                        }
                ));
    }
}
