package academy.javaengineering.minibanking.service;

import academy.javaengineering.minibanking.model.Transaction;
import academy.javaengineering.minibanking.model.TransactionType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for transaction querying and filtering operations.
 *
 * <p>Engineering Decision: Separate TransactionService from AccountService.
 * WHY: Single Responsibility Principle - AccountService handles account mutations,
 * TransactionService handles transaction queries. This separation makes each
 * service easier to test, maintain, and potentially scale independently.</p>
 *
 * <p>Engineering Decision: Uses Java Streams for filtering.
 * WHY: Streams provide a declarative, functional approach to collection processing.
 * The pipeline (filter, collect) is more readable than imperative loops and
 * communicates intent clearly. Performance is comparable for moderate data sizes.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Streams (filter, collect, groupingBy)
 * - Collections (List, Map)
 * - Functional programming style
 * - Method references</p>
 */
public class TransactionService {

    private final AccountService accountService;

    /**
     * Constructs TransactionService with AccountService dependency.
     *
     * @param accountService the service providing transaction data
     */
    public TransactionService(AccountService accountService) {
        if (accountService == null) {
            throw new IllegalArgumentException("AccountService cannot be null");
        }
        this.accountService = accountService;
    }

    /**
     * Gets transaction history for a specific account.
     *
     * @param accountId the account ID
     * @return list of transactions
     */
    public List<Transaction> getTransactionHistory(String accountId) {
        return accountService.getTransactionHistory(accountId);
    }

    /**
     * Filters transactions by type using Streams.
     *
     * <p>Engineering Decision: Stream pipeline for filtering.
     * WHY: The filter().toList() pattern clearly expresses "give me only
     * transactions matching this type." It's declarative and immutable (toList()
     * returns unmodifiable list in Java 16+).</p>
     *
     * @param type the transaction type to filter by
     * @return filtered list of transactions
     */
    public List<Transaction> getTransactionsByType(TransactionType type) {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getType() == type)
                .toList();
    }

    /**
     * Gets transactions for a specific account filtered by type.
     *
     * @param accountId the account ID
     * @param type      the transaction type to filter by
     * @return filtered list of transactions
     */
    public List<Transaction> getTransactionsByAccountAndType(String accountId, TransactionType type) {
        return accountService.getTransactionHistory(accountId).stream()
                .filter(t -> t.getType() == type)
                .toList();
    }

    /**
     * Groups all transactions by account ID using Collectors.groupingBy.
     *
     * <p>Engineering Decision: Using Collectors.groupingBy.
     * WHY: groupingBy is a terminal operation that efficiently partitions
     * stream elements into a Map. It's more efficient than manual iteration
     * and clearly expresses the grouping intent.</p>
     *
     * @return map of accountId to list of transactions
     */
    public Map<String, List<Transaction>> groupTransactionsByAccount() {
        return accountService.getAllTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getAccountId));
    }

    /**
     * Counts transactions by type using stream counting.
     *
     * @param type the transaction type to count
     * @return number of transactions of this type
     */
    public long countTransactionsByType(TransactionType type) {
        return accountService.getAllTransactions().stream()
                .filter(t -> t.getType() == type)
                .count();
    }
}
