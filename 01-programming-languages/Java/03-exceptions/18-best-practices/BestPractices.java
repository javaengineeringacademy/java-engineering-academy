/**
 * Exception Handling Best Practices Examples
 * 
 * This file demonstrates good and bad exception handling patterns
 * that you will encounter in production code.
 * 
 * Key patterns covered:
 * - Specific exception catching vs generic
 * - Resource management with try-with-resources
 * - Exception translation patterns
 * - Proper logging practices
 * - Common anti-patterns to avoid
 * 
 * @author Java Learning Module
 * @version 1.5
 * @since 2026-01-15
 */
public class BestPractices {

    // ========================================================================
    // PATTERN 1: Specific vs Generic Exception Catching
    // ========================================================================

    /**
     * BAD: Catching generic Exception
     * 
     * Problems:
     * - Catches programming errors (NullPointerException, etc.)
     * - Masks the actual failure type
     * - Hard to handle different failures appropriately
     * 
     * @param filePath path to file
     * @return file contents
     * @throws IOException if reading fails
     */
    public String readFileBad(String filePath) throws IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {  // BAD: Too generic
            throw new IOException("Failed to read file", e);
        }
    }

    /**
     * GOOD: Catching specific exceptions
     * 
     * Benefits:
     * - Catches exactly what you expect
     * - Programming errors propagate naturally
     * - Clear handling for each failure type
     * 
     * @param filePath path to file
     * @return file contents
     * @throws FileNotFoundException if file doesn't exist
     * @throws IOException if reading fails
     */
    public String readFileGood(String filePath) 
            throws FileNotFoundException, IOException {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (FileNotFoundException e) {  // GOOD: Specific
            throw e;  // Let caller handle file-not-found
        } catch (IOException e) {  // GOOD: Specific
            throw new IOException("Failed to read file: " + filePath, e);
        }
        // Programming errors (NullPointerException, etc.) propagate naturally
    }

    // ========================================================================
    // PATTERN 2: Resource Management
    // ========================================================================

    /**
     * BAD: Manual resource management
     * 
     * Problems:
     * - Multiple finally blocks needed
     * - Risk of resource leak if exception occurs between acquisitions
     * - Null checks required for each resource
     * - Cleanup order must be manually managed
     * 
     * @param sql SQL query to execute
     * @return result set contents
     */
    public List<Map<String, Object>> queryBad(String sql) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Map<String, Object>> results = new ArrayList<>();
            while (rs.next()) {
                results.add(extractRow(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new DataAccessException("Query failed", e);
        } finally {
            // BAD: Risk of NPE if connection failed
            // Risk: Each close() could throw and mask original exception
            try { if (rs != null) rs.close(); } catch (SQLException e) { }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { }
            try { if (conn != null) conn.close(); } catch (SQLException e) { }
        }
    }

    /**
     * GOOD: Try-with-resources (AutoCloseable)
     * 
     * Benefits:
     * - Automatic cleanup in reverse declaration order
     * - Exception-safe: resources always closed
     * - No null checks needed
     * - Clean, readable code
     * 
     * @param sql SQL query to execute
     * @return result set contents
     */
    public List<Map<String, Object>> queryGood(String sql) {
        // Resources declared in order of dependency
        // Cleaned up in reverse order: rs -> stmt -> conn
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            List<Map<String, Object>> results = new ArrayList<>();
            while (rs.next()) {
                results.add(extractRow(rs));
            }
            return results;
        } catch (SQLException e) {
            throw new DataAccessException("Query failed: " + sql, e);
        }
    }

    // ========================================================================
    // PATTERN 3: Exception Translation
    // ========================================================================

    /**
     * BAD: Exposing low-level exceptions to callers
     * 
     * Problems:
     * - Callers must handle JDBC-specific exceptions
     * - Implementation details leak into API
     * - Harder to change database implementation later
     * 
     * @param id user ID
     * @return user data
     * @throws SQLException if database access fails
     */
    public User findUserBad(String id) throws SQLException {
        // Caller must catch SQLException - why does they care about JDBC?
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?",
            userRowMapper, id);
    }

    /**
     * GOOD: Exception translation pattern
     * 
     * Benefits:
     * - Callers get domain-specific exceptions
     * - Implementation details hidden
     * - Easier to change database implementation
     * - Original exception preserved as cause
     * 
     * @param id user ID
     * @return user data
     * @throws UserNotFoundException if user doesn't exist
     * @throws DataAccessException if database access fails
     */
    public User findUserGood(String id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                userRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("User not found: " + id, e);
        } catch (DataAccessException e) {
            throw new DataAccessException(
                "Failed to find user: " + id, e);
        }
    }

    // ========================================================================
    // PATTERN 4: Exception Message Quality
    // ========================================================================

    /**
     * BAD: Vague exception messages
     * 
     * Problem: Messages don't help diagnose the issue
     */
    public void processPaymentBad(PaymentRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");  // BAD
        }
        if (request.getCardNumber() == null) {
            throw new IllegalArgumentException("Invalid card");  // BAD
        }
        // ... processing
    }

    /**
     * GOOD: Informative exception messages
     * 
     * Benefits:
     * - Messages include relevant context
     * - Easy to identify what went wrong
     * - No sensitive data logged
     */
    public void processPaymentGood(PaymentRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Payment amount must be positive, got: " + request.getAmount());
        }
        if (request.getCardNumber() == null) {
            throw new IllegalArgumentException(
                "Payment card number is required");
        }
        // ... processing
    }

    // ========================================================================
    // PATTERN 5: Exception Propagation
    // ========================================================================

    /**
     * BAD: Swallowing exceptions
     * 
     * Problem: Bugs go undetected, data corruption possible
     */
    public void updateInventoryBad(Order order) {
        try {
            for (OrderItem item : order.getItems()) {
                inventoryService.decrement(item.getProductId(), item.getQuantity());
            }
        } catch (Exception e) {
            log.error("Inventory update failed");  // BAD: Exception swallowed!
            // Order thinks inventory was updated, but it wasn't
        }
    }

    /**
     * GOOD: Proper exception propagation
     * 
     * Benefits:
     * - Failure is communicated to caller
     * - Transaction can be rolled back
     * - Caller decides how to handle failure
     */
    public void updateInventoryGood(Order order) {
        try {
            for (OrderItem item : order.getItems()) {
                inventoryService.decrement(item.getProductId(), item.getQuantity());
            }
        } catch (InventoryException e) {
            throw new OrderProcessingException(
                "Failed to update inventory for order: " + order.getId(), e);
        }
    }

    // ========================================================================
    // PATTERN 6: Multi-Catch and Finally
    // ========================================================================

    /**
     * BAD: Exception in finally block masks original
     * 
     * Problem: If finally throws, original exception is lost
     */
    public void processDataBad(byte[] data) {
        try {
            processData(data);
        } finally {
            cleanup();  // BAD: If cleanup() throws, original exception lost
        }
    }

    /**
     * GOOD: Safe finally block
     * 
     * Benefits:
     * - Original exception preserved
     * - Cleanup errors handled separately
     */
    public void processDataGood(byte[] data) {
        try {
            processData(data);
        } finally {
            try {
                cleanup();
            } catch (CleanupException e) {
                log.warn("Cleanup failed, but data processing may have succeeded", e);
                // Original exception not masked
            }
        }
    }

    // ========================================================================
    // PATTERN 7: Logging Best Practices
    // ========================================================================

    /**
     * BAD: Logging without context or exception
     */
    public void transferFundsBad(Account from, Account to, BigDecimal amount) {
        try {
            performTransfer(from, to, amount);
        } catch (Exception e) {
            log.error("Transfer failed");  // BAD: No context, no exception!
        }
    }

    /**
     * GOOD: Logging with full context and exception
     */
    public void transferFundsGood(Account from, Account to, BigDecimal amount) {
        try {
            performTransfer(from, to, amount);
        } catch (TransferException e) {
            log.error("Transfer failed: from={}, to={}, amount={}: {}", 
                from.getId(), to.getId(), amount, e.getMessage(), e);
            // Exception includes stack trace in logs
            // Context helps identify which transfer failed
        }
    }

    // ========================================================================
    // PATTERN 8: Null Return vs Exception
    // ========================================================================

    /**
     * BAD: Returning null on failure
     * 
     * Problem: Caller gets NullPointerException later, hard to debug
     */
    public User findUserOrNull(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            log.error("Find user failed");
            return null;  // BAD: Caller will NPE later
        }
    }

    /**
     * GOOD: Throwing exception on failure
     * 
     * Benefits:
     * - Failure is immediate and obvious
     * - Caller can handle appropriately
     * - Stack trace shows where problem occurred
     */
    public User findUserOrThrow(String id) {
        try {
            return userRepository.findById(id);
        } catch (DataAccessException e) {
            throw new UserServiceException(
                "Failed to find user: " + id, e);
        }
    }

    // ========================================================================
    // PATTERN 9: Retry Logic
    // ========================================================================

    /**
     * GOOD: Retry with exponential backoff
     * 
     * Use case: Transient failures (network timeouts, etc.)
     */
    public <T> T executeWithRetry(Supplier<T> operation, int maxRetries) {
        int attempt = 0;
        while (true) {
            try {
                return operation.get();
            } catch (TransientException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw new ServiceException(
                        "Operation failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep((long) Math.pow(2, attempt) * 100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new ServiceException("Interrupted during retry", ie);
                }
            }
        }
    }

    // ========================================================================
    // PATTERN 10: Validation with Exceptions
    // ========================================================================

    /**
     * GOOD: Validate early, throw meaningful exceptions
     * 
     * Principle: Fail fast with clear error messages
     */
    public Order createOrder(CreateOrderRequest request) {
        // Validate all inputs before processing
        Objects.requireNonNull(request, "Request must not be null");
        Objects.requireNonNull(request.getCustomerId(), "Customer ID required");
        Objects.requireNonNull(request.getItems(), "Order items required");
        
        if (request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                "Order must contain at least one item");
        }
        
        if (request.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "Order total must be positive, got: " + request.getTotal());
        }

        // All validations passed, proceed with order creation
        return orderService.create(request);
    }
}
