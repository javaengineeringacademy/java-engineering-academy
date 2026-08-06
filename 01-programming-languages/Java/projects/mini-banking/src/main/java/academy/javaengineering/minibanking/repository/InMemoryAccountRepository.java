package academy.javaengineering.minibanking.repository;

import academy.javaengineering.minibanking.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe in-memory implementation of AccountRepository using ConcurrentHashMap.
 *
 * <p>Engineering Decision: ConcurrentHashMap over synchronized HashMap.
 * WHY: ConcurrentHashMap provides better concurrency through lock striping.
 * Reads don't require locking, and writes only lock the specific segment.
 * This outperforms synchronized HashMap under high concurrent read loads.</p>
 *
 * <p>Engineering Decision: In-memory storage.
 * WHY: For demonstration purposes, in-memory storage is simple and fast.
 * Production systems would use JPA/Hibernate with a database. This design
 * allows easy swapping of repository implementations via dependency injection.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Generics (implements AccountRepository&lt;Account&gt;)
 * - Collections (ConcurrentHashMap, ArrayList)
 * - Thread safety (ConcurrentHashMap)
 * - Interface implementation
 * - Optional for null safety</p>
 */
public class InMemoryAccountRepository implements AccountRepository<Account> {

    /**
     * Thread-safe map storing accounts by ID.
     * ConcurrentHashMap chosen over Collections.synchronizedMap for better
     * read performance and iteration safety.
     */
    private final ConcurrentMap<String, Account> store = new ConcurrentHashMap<>();

    /**
     * Saves an account to the repository.
     *
     * @param account the account to save
     * @return the saved account (same reference)
     */
    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        store.put(account.getId(), account);
        return account;
    }

    /**
     * Finds an account by ID.
     *
     * @param id the account ID
     * @return Optional containing the account if found
     */
    @Override
    public Optional<Account> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(id));
    }

    /**
     * Returns all accounts as a new list.
     *
     * <p>Engineering Decision: Return new ArrayList copy.
     * WHY: Prevents callers from modifying the internal store through the returned list.
     * Defensive copying protects encapsulation.</p>
     *
     * @return list of all accounts
     */
    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * Deletes an account by ID.
     *
     * @param id the account ID to delete
     * @return true if account existed and was deleted
     */
    @Override
    public boolean delete(String id) {
        if (id == null) {
            return false;
        }
        return store.remove(id) != null;
    }

    /**
     * Checks if an account exists.
     *
     * @param id the account ID to check
     * @return true if account exists
     */
    @Override
    public boolean existsById(String id) {
        if (id == null) {
            return false;
        }
        return store.containsKey(id);
    }

    /**
     * Returns count of stored accounts.
     *
     * @return number of accounts
     */
    @Override
    public long count() {
        return store.size();
    }
}
