package academy.javaengineering.minibanking.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Centralized audit logging for all banking operations.
 *
 * <p>Engineering Decision: SLF4J facade with Logback implementation.
 * WHY: SLF4J provides abstraction over logging implementations. If we need
 * to switch to Log4j2 or java.util.logging later, only configuration changes.
 * Logback is chosen as the native SLF4J implementation with better performance.</p>
 *
 * <p>Engineering Decision: MDC (Mapped Diagnostic Context) for request tracking.
 * WHY: MDC allows attaching context data (like request ID, user ID) to all
 * log messages within a thread. This enables correlating logs across multiple
 * operations for debugging and audit trails.</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - SLF4J logging facade
 * - MDC context for request tracking
 * - Structured logging with parameters
 * - Log levels (info, warn, error)</p>
 */
public final class AuditLogger {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogger.class);
    private static final String REQUEST_ID_KEY = "requestId";
    private static final String ACCOUNT_ID_KEY = "accountId";

    /**
     * Private constructor - utility class with static methods only.
     */
    private AuditLogger() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Sets up MDC context for request tracking.
     *
     * <p>Engineering Decision: Auto-generate request ID if not provided.
     * WHY: Every operation should be traceable. Generating UUID ensures
     * uniqueness without requiring caller to manage IDs.</p>
     *
     * @param requestId optional request ID (generated if null)
     */
    public static void setupContext(String requestId) {
        String actualRequestId = requestId != null ? requestId : UUID.randomUUID().toString();
        MDC.put(REQUEST_ID_KEY, actualRequestId);
        logger.debug("MDC context initialized with requestId={}", actualRequestId);
    }

    /**
     * Clears MDC context for the current thread.
     *
     * <p>Engineering Decision: Always clear MDC in finally blocks.
     * WHY: MDC is thread-local. In thread pools, MDC values leak to the next
     * task if not cleared, causing incorrect log context.</p>
     */
    public static void clearContext() {
        MDC.remove(REQUEST_ID_KEY);
        MDC.remove(ACCOUNT_ID_KEY);
    }

    /**
     * Sets account ID in MDC for operation context.
     *
     * @param accountId the account being operated on
     */
    public static void setAccountId(String accountId) {
        MDC.put(ACCOUNT_ID_KEY, accountId);
    }

    /**
     * Logs account creation.
     *
     * @param accountId the new account ID
     * @param owner     the account owner
     */
    public static void logAccountCreated(String accountId, String owner) {
        setAccountId(accountId);
        logger.info("Account created: id={}, owner={}", accountId, owner);
    }

    /**
     * Logs a deposit transaction.
     *
     * @param accountId the target account
     * @param amount    the deposited amount
     * @param newBalance the balance after deposit
     */
    public static void logDeposit(String accountId, double amount, double newBalance) {
        setAccountId(accountId);
        logger.info("Deposit: accountId={}, amount={}, newBalance={}", accountId, amount, newBalance);
    }

    /**
     * Logs a withdrawal transaction.
     *
     * @param accountId  the source account
     * @param amount     the withdrawn amount
     * @param newBalance the balance after withdrawal
     */
    public static void logWithdrawal(String accountId, double amount, double newBalance) {
        setAccountId(accountId);
        logger.info("Withdrawal: accountId={}, amount={}, newBalance={}", accountId, amount, newBalance);
    }

    /**
     * Logs a failed withdrawal attempt.
     *
     * @param accountId the target account
     * @param amount    the attempted amount
     * @param reason    the failure reason
     */
    public static void logWithdrawalFailed(String accountId, double amount, String reason) {
        setAccountId(accountId);
        logger.warn("Withdrawal failed: accountId={}, amount={}, reason={}", accountId, amount, reason);
    }

    /**
     * Logs an error during operation.
     *
     * @param operation the failed operation name
     * @param error     the exception message
     */
    public static void logError(String operation, String error) {
        logger.error("Operation failed: operation={}, error={}", operation, error);
    }

    /**
     * Logs account retrieval.
     *
     * @param accountId the retrieved account ID
     */
    public static void logAccountRetrieved(String accountId) {
        setAccountId(accountId);
        logger.debug("Account retrieved: accountId={}", accountId);
    }
}
