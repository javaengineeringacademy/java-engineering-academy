package war.stories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WAR STORY: Database Connection Leak - The Slow Death
 * 
 * Scenario: A reporting service ran fine for hours, then started timing out.
 * After 6 hours, all database connections were exhausted.
 * The service appeared healthy but couldn't process any queries.
 * 
 * Investigation Process:
 * 1. Check HikariCP metrics: active connections, pending threads
 * 2. Enable leak detection: leakDetectionThreshold=5000
 * 3. Analyze thread dumps for JDBC connection holders
 * 4. Use connection pool monitoring to find leak patterns
 * 
 * Root Cause: A reporting endpoint had an unclosed connection in an error path.
 * When queries failed, the connection was never returned to the pool.
 * Over 6 hours, leaked connections accumulated until the pool was exhausted.
 */
public class ConnectionLeakStory {

    // Simulated database connection pool
    static class SimpleConnectionPool {
        private final BlockingQueue<Connection> pool;
        private final AtomicInteger activeConnections = new AtomicInteger(0);
        private final AtomicInteger totalConnections = new AtomicInteger(0);
        private final int maxSize;
        private final long borrowTimeoutMillis;

        public SimpleConnectionPool(int maxSize, long borrowTimeoutMillis) {
            this.maxSize = maxSize;
            this.borrowTimeoutMillis = borrowTimeoutMillis;
            this.pool = new ArrayBlockingQueue<>(maxSize);
            
            // Pre-create connections
            for (int i = 0; i < maxSize; i++) {
                try {
                    Connection conn = createConnection();
                    pool.offer(conn);
                    totalConnections.incrementAndGet();
                } catch (SQLException e) {
                    throw new RuntimeException("Failed to create connection", e);
                }
            }
        }

        public Connection borrowConnection() throws SQLException {
            try {
                Connection conn = pool.poll(borrowTimeoutMillis, TimeUnit.MILLISECONDS);
                if (conn == null) {
                    throw new SQLException("Connection timeout: pool exhausted");
                }
                activeConnections.incrementAndGet();
                return conn;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SQLException("Interrupted while waiting for connection", e);
            }
        }

        public void returnConnection(Connection conn) {
            if (conn != null) {
                activeConnections.decrementAndGet();
                if (!pool.offer(conn)) {
                    // Pool is full, close the extra connection
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        // Log and ignore
                    }
                }
            }
        }

        public int getActiveConnections() {
            return activeConnections.get();
        }

        public int getAvailableConnections() {
            return pool.size();
        }

        private Connection createConnection() throws SQLException {
            // In real code, this would use DriverManager.getConnection()
            // For simulation, we return a mock connection
            return new MockConnection();
        }

        public void shutdown() {
            Connection conn;
            while ((conn = pool.poll()) != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Log and ignore
                }
            }
        }
    }

    // Mock connection for demonstration
    static class MockConnection implements Connection {
        private boolean closed = false;
        
        @Override
        public void close() throws SQLException {
            closed = true;
        }
        
        @Override
        public boolean isClosed() throws SQLException {
            return closed;
        }
        
        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            if (closed) throw new SQLException("Connection is closed");
            return new MockPreparedStatement();
        }
        
        // Implement other Connection methods...
        @Override public java.sql.DatabaseMetaData getMetaData() throws SQLException { return null; }
        @Override public void setAutoCommit(boolean autoCommit) throws SQLException {}
        @Override public boolean getAutoCommit() throws SQLException { return true; }
        @Override public void commit() throws SQLException {}
        @Override public void rollback() throws SQLException {}
        @Override public boolean isReadOnly() throws SQLException { return false; }
        @Override public void setReadOnly(boolean readOnly) throws SQLException {}
        @Override public int getTransactionIsolation() throws SQLException { return Connection.TRANSACTION_READ_COMMITTED; }
        @Override public void setTransactionIsolation(int level) throws SQLException {}
        @Override public java.sql.SQLWarning getWarnings() throws SQLException { return null; }
        @Override public void clearWarnings() throws SQLException {}
        @Override public java.sql.Statement createStatement() throws SQLException { return null; }
        @Override public java.sql.PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return null; }
        @Override public java.sql.CallableStatement prepareCall(String sql) throws SQLException { return null; }
        @Override public java.util.Map<String, Class<?>> getTypeMap() throws SQLException { return null; }
        @Override public void setTypeMap(java.util.Map<String, Class<?>> map) throws SQLException {}
        @Override public void setHoldability(int holdability) throws SQLException {}
        @Override public int getHoldability() throws SQLException { return 0; }
        @Override public java.sql.Savepoint setSavepoint() throws SQLException { return null; }
        @Override public java.sql.Savepoint setSavepoint(String name) throws SQLException { return null; }
        @Override public void rollback(java.sql.Savepoint savepoint) throws SQLException {}
        @Override public void releaseSavepoint(java.sql.Savepoint savepoint) throws SQLException {}
        @Override public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException { return null; }
        @Override public java.sql.CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException { return null; }
        @Override public java.sql.PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException { return null; }
        @Override public java.sql.PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException { return null; }
        @Override public java.sql.PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException { return null; }
        @Override public java.sql.Clob createClob() throws SQLException { return null; }
        @Override public java.sql.Blob createBlob() throws SQLException { return null; }
        @Override public java.sql.NClob createNClob() throws SQLException { return null; }
        @Override public java.sql.SQLXML createSQLXML() throws SQLException { return null; }
        @Override public boolean isValid(int timeout) throws SQLException { return !closed; }
        @Override public void setClientInfo(String name, String value) {}
        @Override public void setClientInfo(java.util.Properties properties) {}
        @Override public String getClientInfo(String name) throws SQLException { return null; }
        @Override public java.util.Properties getClientInfo() throws SQLException { return null; }
        @Override public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException { return null; }
        @Override public java.sql.Struct createStruct(String typeName, Object[] attributes) throws SQLException { return null; }
        @Override public void setSchema(String schema) {}
        @Override public String getSchema() throws SQLException { return null; }
        @Override public void abort(java.util.concurrent.Executor executor) {}
        @Override public void setNetworkTimeout(java.util.concurrent.Executor executor, int milliseconds) {}
        @Override public int getNetworkTimeout() throws SQLException { return 0; }
        @Override public <T> T unwrap(Class<T> iface) throws SQLException { return null; }
        @Override public boolean isWrapperFor(Class<?> iface) throws SQLException { return false; }
    }

    static class MockPreparedStatement implements PreparedStatement {
        @Override public void close() throws SQLException {}
        @Override public ResultSet executeQuery() throws SQLException { return new MockResultSet(); }
        @Override public int executeUpdate() throws SQLException { return 0; }
        @Override public void setNull(int parameterIndex, int sqlType) {}
        @Override public void setBoolean(int parameterIndex, boolean x) {}
        @Override public void setByte(int parameterIndex, byte x) {}
        @Override public void setShort(int parameterIndex, short x) {}
        @Override public void setInt(int parameterIndex, int x) {}
        @Override public void setLong(int parameterIndex, long x) {}
        @Override public void setFloat(int parameterIndex, float x) {}
        @Override public void setDouble(int parameterIndex, double x) {}
        @Override public void setBigDecimal(int parameterIndex, java.math.BigDecimal x) {}
        @Override public void setString(int parameterIndex, String x) {}
        @Override public void setBytes(int parameterIndex, byte[] x) {}
        @Override public void setDate(int parameterIndex, java.sql.Date x) {}
        @Override public void setTime(int parameterIndex, java.sql.Time x) {}
        @Override public void setTimestamp(int parameterIndex, java.sql.Timestamp x) {}
        @Override public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Override public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Override public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) {}
        @Override public void clearParameters() {}
        @Override public void setObject(int parameterIndex, Object x, int targetSqlType) {}
        @Override public void setObject(int parameterIndex, Object x) {}
        @Override public boolean execute() throws SQLException { return false; }
        @Override public void addBatch() {}
        @Override public void setCharacterStream(int parameterIndex, java.io.Reader reader, int length) {}
        @Override public void setRef(int parameterIndex, java.sql.Ref x) {}
        @Override public void setBlob(int parameterIndex, java.sql.Blob x) {}
        @Override public void setClob(int parameterIndex, java.sql.Clob x) {}
        @Override public void setArray(int parameterIndex, java.sql.Array x) {}
        @Override public java.sql.ResultSetMetaData getMetaData() throws SQLException { return null; }
        @Override public void setDate(int parameterIndex, java.sql.Date x, java.util.Calendar cal) {}
        @Override public void setTime(int parameterIndex, java.sql.Time x, java.util.Calendar cal) {}
        @Override public void setTimestamp(int parameterIndex, java.sql.Timestamp x, java.util.Calendar cal) {}
        @Override public void setNull(int parameterIndex, int sqlType, String typeName) {}
        @Override public void setURL(int parameterIndex, java.net.URL x) {}
        @Override public java.sql.ParameterMetaData getParameterMetaData() throws SQLException { return null; }
        @Override public void setRowId(int parameterIndex, java.sql.RowId x) {}
        @Override public void setNString(int parameterIndex, String value) {}
        @Override public void setNCharacterStream(int parameterIndex, java.io.Reader value, long length) {}
        @Override public void setNClob(int parameterIndex, java.sql.NClob value) {}
        @Override public void setSQLXML(int parameterIndex, java.sql.SQLXML xmlObject) {}
        @Override public void setLong(int parameterIndex, long x) {}
        @Override public void executeBatch() throws SQLException {}
        @Override public void clearBatch() {}
        @Override public java.sql.ResultSet getGeneratedKeys() throws SQLException { return null; }
        @Override public int executeUpdate(String sql, int autoGeneratedKeys) { return 0; }
        @Override public int executeUpdate(String sql, int[] columnIndexes) { return 0; }
        @Override public int executeUpdate(String sql, String[] columnNames) { return 0; }
        @Override public boolean execute(String sql, int autoGeneratedKeys) { return false; }
        @Override public boolean execute(String sql, int[] columnIndexes) { return false; }
        @Override public boolean execute(String sql, String[] columnNames) { return false; }
        @Override public int getUpdateCount() { return 0; }
        @Override public boolean getMoreResults() { return false; }
        @Override public boolean getMoreResults(int current) { return false; }
        @Override public java.sql.ResultSet getResultSet() { return null; }
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchDirection(int direction) {}
        @Override public int getFetchSize() { return 0; }
        @Override public void setFetchSize(int rows) {}
        @Override public int getMaxFieldSize() { return 0; }
        @Override public void setMaxFieldSize(int max) {}
        @Override public int getMaxRows() { return 0; }
        @Override public void setMaxRows(int max) {}
        @Override public int getQueryTimeout() { return 0; }
        @Override public void setQueryTimeout(int seconds) {}
        @Override public void cancel() {}
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public void setCursorName(String name) {}
        @Override public boolean isClosed() { return false; }
        @Override public void setPoolable(boolean poolable) {}
        @Override public boolean isPoolable() { return false; }
        @Override public void closeOnCompletion() {}
        @Override public boolean isCloseOnCompletion() { return false; }
        @Override public <T> T unwrap(Class<T> iface) { return null; }
        @Override public boolean isWrapperFor(Class<?> iface) { return false; }
    }

    static class MockResultSet implements ResultSet {
        @Override public boolean next() { return false; }
        @Override public void close() {}
        @Override public boolean wasNull() { return false; }
        @Override public String getString(int columnIndex) { return null; }
        @Override public boolean getBoolean(int columnIndex) { return false; }
        @Override public byte getByte(int columnIndex) { return 0; }
        @Override public short getShort(int columnIndex) { return 0; }
        @Override public int getInt(int columnIndex) { return 0; }
        @Override public long getLong(int columnIndex) { return 0; }
        @Override public float getFloat(int columnIndex) { return 0; }
        @Override public double getDouble(int columnIndex) { return 0; }
        @Override public java.math.BigDecimal getBigDecimal(int columnIndex) { return null; }
        @Override public byte[] getBytes(int columnIndex) { return null; }
        @Override public java.sql.Date getDate(int columnIndex) { return null; }
        @Override public java.sql.Time getTime(int columnIndex) { return null; }
        @Override public java.sql.Timestamp getTimestamp(int columnIndex) { return null; }
        @Override public java.io.InputStream getAsciiStream(int columnIndex) { return null; }
        @Override public java.io.InputStream getUnicodeStream(int columnIndex) { return null; }
        @Override public java.io.InputStream getBinaryStream(int columnIndex) { return null; }
        @Override public String getString(String columnLabel) { return null; }
        @Override public boolean getBoolean(String columnLabel) { return false; }
        @Override public byte getByte(String columnLabel) { return 0; }
        @Override public short getShort(String columnLabel) { return 0; }
        @Override public int getInt(String columnLabel) { return 0; }
        @Override public long getLong(String columnLabel) { return 0; }
        @Override public float getFloat(String columnLabel) { return 0; }
        @Override public double getDouble(String columnLabel) { return 0; }
        @Override public java.math.BigDecimal getBigDecimal(String columnLabel) { return null; }
        @Override public byte[] getBytes(String columnLabel) { return null; }
        @Override public java.sql.Date getDate(String columnLabel) { return null; }
        @Override public java.sql.Time getTime(String columnLabel) { return null; }
        @Override public java.sql.Timestamp getTimestamp(String columnLabel) { return null; }
        @Override public java.io.InputStream getAsciiStream(String columnLabel) { return null; }
        @Override public java.io.InputStream getUnicodeStream(String columnLabel) { return null; }
        @Override public java.io.InputStream getBinaryStream(String columnLabel) { return null; }
        @Override public java.sql.SQLWarning getWarnings() { return null; }
        @Override public void clearWarnings() {}
        @Override public String getCursorName() { return null; }
        @Override public java.sql.ResultSetMetaData getMetaData() { return null; }
        @Override public Object getObject(int columnIndex) { return null; }
        @Override public Object getObject(String columnLabel) { return null; }
        @Override public int findColumn(String columnLabel) { return 0; }
        @Override public java.io.Reader getCharacterStream(int columnIndex) { return null; }
        @Override public java.io.Reader getCharacterStream(String columnLabel) { return null; }
        @Override public java.math.BigDecimal getBigDecimal(int columnIndex, int scale) { return null; }
        @Override public java.math.BigDecimal getBigDecimal(String columnLabel, int scale) { return null; }
        @Override public boolean isBeforeFirst() { return false; }
        @Override public boolean isAfterLast() { return false; }
        @Override public boolean isFirst() { return false; }
        @Override public boolean isLast() { return false; }
        @Override public void beforeFirst() {}
        @Override public void afterLast() {}
        @Override public boolean first() { return false; }
        @Override public boolean last() { return false; }
        @Override public int getRow() { return 0; }
        @Override public boolean absolute(int row) { return false; }
        @Override public boolean relative(int rows) { return false; }
        @Override public boolean previous() { return false; }
        @Override public void setFetchDirection(int direction) {}
        @Override public int getFetchDirection() { return 0; }
        @Override public void setFetchSize(int rows) {}
        @Override public int getFetchSize() { return 0; }
        @Override public int getType() { return ResultSet.TYPE_FORWARD_ONLY; }
        @Override public int getConcurrency() { return ResultSet.CONCUR_READ_ONLY; }
        @Override public boolean rowUpdated() { return false; }
        @Override public boolean rowInserted() { return false; }
        @Override public boolean rowDeleted() { return false; }
        @Override public void updateNull(int columnIndex) {}
        @Override public void updateBoolean(int columnIndex, boolean x) {}
        @Override public void updateByte(int columnIndex, byte x) {}
        @Override public void updateShort(int columnIndex, short x) {}
        @Override public void updateInt(int columnIndex, int x) {}
        @Override public void updateLong(int columnIndex, long x) {}
        @Override public void updateFloat(int columnIndex, float x) {}
        @Override public void updateDouble(int columnIndex, double x) {}
        @Override public void updateBigDecimal(int columnIndex, java.math.BigDecimal x) {}
        @Override public void updateString(int columnIndex, String x) {}
        @Override public void updateBytes(int columnIndex, byte[] x) {}
        @Override public void updateDate(int columnIndex, java.sql.Date x) {}
        @Override public void updateTime(int columnIndex, java.sql.Time x) {}
        @Override public void updateTimestamp(int columnIndex, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(int columnIndex, java.io.InputStream x, int length) {}
        @Override public void updateBinaryStream(int columnIndex, java.io.InputStream x, int length) {}
        @Override public void updateCharacterStream(int columnIndex, java.io.Reader x, int length) {}
        @Override public void updateObject(int columnIndex, Object x, int targetSqlType) {}
        @Override public void updateObject(int columnIndex, Object x) {}
        @Override public void updateNull(String columnLabel) {}
        @Override public void updateBoolean(String columnLabel, boolean x) {}
        @Override public void updateByte(String columnLabel, byte x) {}
        @Override public void updateShort(String columnLabel, short x) {}
        @Override public void updateInt(String columnLabel, int x) {}
        @Override public void updateLong(String columnLabel, long x) {}
        @Override public void updateFloat(String columnLabel, float x) {}
        @Override public void updateDouble(String columnLabel, double x) {}
        @Override public void updateBigDecimal(String columnLabel, java.math.BigDecimal x) {}
        @Override public void updateString(String columnLabel, String x) {}
        @Override public void updateBytes(String columnLabel, byte[] x) {}
        @Override public void updateDate(String columnLabel, java.sql.Date x) {}
        @Override public void updateTime(String columnLabel, java.sql.Time x) {}
        @Override public void updateTimestamp(String columnLabel, java.sql.Timestamp x) {}
        @Override public void updateAsciiStream(String columnLabel, java.io.InputStream x, int length) {}
        @Override public void updateBinaryStream(String columnLabel, java.io.InputStream x, int length) {}
        @Override public void updateCharacterStream(String columnLabel, java.io.Reader x, int length) {}
        @Override public void updateObject(String columnLabel, Object x, int targetSqlType) {}
        @Override public void updateObject(String columnLabel, Object x) {}
        @Override public void insertRow() {}
        @Override public void updateRow() {}
        @Override public void deleteRow() {}
        @Override public void refreshRow() {}
        @Override public void cancelRowUpdates() {}
        @Override public void moveToInsertRow() {}
        @Override public void moveToCurrentRow() {}
        @Override public java.sql.Statement getStatement() { return null; }
        @Override public Object getObject(int columnIndex, java.util.Map<String, Class<?>> map) { return null; }
        @Override public java.sql.Ref getRef(int columnIndex) { return null; }
        @Override public java.sql.Blob getBlob(int columnIndex) { return null; }
        @Override public java.sql.Clob getClob(int columnIndex) { return null; }
        @Override public java.sql.Array getArray(int columnIndex) { return null; }
        @Override public Object getObject(String columnLabel, java.util.Map<String, Class<?>> map) { return null; }
        @Override public java.sql.Ref getRef(String columnLabel) { return null; }
        @Override public java.sql.Blob getBlob(String columnLabel) { return null; }
        @Override public java.sql.Clob getClob(String columnLabel) { return null; }
        @Override public java.sql.Array getArray(String columnLabel) { return null; }
        @Override public java.sql.Date getDate(int columnIndex, java.util.Calendar cal) { return null; }
        @Override public java.sql.Date getDate(String columnLabel, java.util.Calendar cal) { return null; }
        @Override public java.sql.Time getTime(int columnIndex, java.util.Calendar cal) { return null; }
        @Override public java.sql.Time getTime(String columnLabel, java.util.Calendar cal) { return null; }
        @Override public java.sql.Timestamp getTimestamp(int columnIndex, java.util.Calendar cal) { return null; }
        @Override public java.sql.Timestamp getTimestamp(String columnLabel, java.util.Calendar cal) { return null; }
        @Override public java.net.URL getURL(int columnIndex) { return null; }
        @Override public java.net.URL getURL(String columnLabel) { return null; }
        @Override public void updateRef(int columnIndex, java.sql.Ref x) {}
        @Override public void updateRef(String columnLabel, java.sql.Ref x) {}
        @Override public void updateBlob(int columnIndex, java.sql.Blob x) {}
        @Override public void updateBlob(String columnLabel, java.sql.Blob x) {}
        @Override public void updateClob(int columnIndex, java.sql.Clob x) {}
        @Override public void updateClob(String columnLabel, java.sql.Clob x) {}
        @Override public void updateArray(int columnIndex, java.sql.Array x) {}
        @Override public void updateArray(String columnLabel, java.sql.Array x) {}
        @Override public java.sql.RowId getRowId(int columnIndex) { return null; }
        @Override public java.sql.RowId getRowId(String columnLabel) { return null; }
        @Override public void updateRowId(int columnIndex, java.sql.RowId x) {}
        @Override public void updateRowId(String columnLabel, java.sql.RowId x) {}
        @Override public int getHoldability() { return 0; }
        @Override public void setHoldability(int holdability) {}
        @Override public java.sql.Statement getStatement() { return null; }
        @Override public boolean isClosed() { return false; }
        @Override public void updateNString(int columnIndex, String nString) {}
        @Override public void updateNString(String columnLabel, String nString) {}
        @Override public void updateNClob(int columnIndex, java.sql.NClob nClob) {}
        @Override public void updateNClob(String columnLabel, java.sql.NClob nClob) {}
        @Override public java.sql.NClob getNClob(int columnIndex) { return null; }
        @Override public java.sql.NClob getNClob(String columnLabel) { return null; }
        @Override public java.sql.SQLXML getSQLXML(int columnIndex) { return null; }
        @Override public java.sql.SQLXML getSQLXML(String columnLabel) { return null; }
        @Override public void updateSQLXML(int columnIndex, java.sql.SQLXML xmlObject) {}
        @Override public void updateSQLXML(String columnLabel, java.sql.SQLXML xmlObject) {}
        @Override public String getNString(int columnIndex) { return null; }
        @Override public String getNString(String columnLabel) { return null; }
        @Override public java.io.Reader getNCharacterStream(int columnIndex) { return null; }
        @Override public java.io.Reader getNCharacterStream(String columnLabel) { return null; }
        @Override public void updateNCharacterStream(int columnIndex, java.io.Reader x, long length) {}
        @Override public void updateNCharacterStream(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateAsciiStream(int columnIndex, java.io.InputStream x, long length) {}
        @Override public void updateBinaryStream(int columnIndex, java.io.InputStream x, long length) {}
        @Override public void updateCharacterStream(int columnIndex, java.io.Reader x, long length) {}
        @Override public void updateAsciiStream(String columnLabel, java.io.InputStream x, long length) {}
        @Override public void updateBinaryStream(String columnLabel, java.io.InputStream x, long length) {}
        @Override public void updateCharacterStream(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateBlob(int columnIndex, java.io.InputStream inputStream, long length) {}
        @Override public void updateBlob(String columnLabel, java.io.InputStream inputStream, long length) {}
        @Override public void updateClob(int columnIndex, java.io.Reader reader, long length) {}
        @Override public void updateClob(String columnLabel, java.io.Reader reader, long length) {}
        @Override public void updateNClob(int columnIndex, java.io.Reader reader, long length) {}
        @Override public void updateNClob(String columnLabel, java.io.Reader reader, long length) {}
        @Override public <T> T getObject(int columnIndex, Class<T> type) { return null; }
        @Override public <T> T getObject(String columnLabel, Class<T> type) { return null; }
        @Override public void updateObject(int columnIndex, Object x, java.sql.SQLType targetSqlType) {}
        @Override public void updateObject(String columnLabel, Object x, java.sql.SQLType targetSqlType) {}
        @Override public <T> T unwrap(Class<T> iface) { return null; }
        @Override public boolean isWrapperFor(Class<?> iface) { return false; }
    }

    // BUGGY VERSION: Connection leak in error path
    static class ReportServiceBuggy {
        private final SimpleConnectionPool pool;

        public ReportServiceBuggy(SimpleConnectionPool pool) {
            this.pool = pool;
        }

        public String generateReport(String reportType) throws SQLException {
            Connection conn = pool.borrowConnection();
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM reports WHERE type = ?");
                ps.setString(1, reportType);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return "Report: " + rs.getString(1);
                }
                return "No report found";
                
                // BUG: Connection never returned to pool!
                // If an exception occurs, or if we return early,
                // the connection is leaked.
            } catch (SQLException e) {
                // BUG: Connection leaked in error path!
                throw new RuntimeException("Failed to generate report", e);
            }
        }
    }

    // FIXED VERSION: Proper connection handling with try-with-resources
    static class ReportServiceFixed {
        private final SimpleConnectionPool pool;

        public ReportServiceFixed(SimpleConnectionPool pool) {
            this.pool = pool;
        }

        public String generateReport(String reportType) throws SQLException {
            Connection conn = null;
            try {
                conn = pool.borrowConnection();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM reports WHERE type = ?");
                ps.setString(1, reportType);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    return "Report: " + rs.getString(1);
                }
                return "No report found";
            } catch (SQLException e) {
                throw new RuntimeException("Failed to generate report", e);
            } finally {
                // ALWAYS return connection to pool in finally block
                pool.returnConnection(conn);
            }
        }
    }

    // BETTER VERSION: Use try-with-resources pattern
    static class ReportServiceBetter {
        private final SimpleConnectionPool pool;

        public ReportServiceBetter(SimpleConnectionPool pool) {
            this.pool = pool;
        }

        public String generateReport(String reportType) throws SQLException {
            try (Connection conn = pool.borrowConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM reports WHERE type = ?")) {
                
                ps.setString(1, reportType);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return "Report: " + rs.getString(1);
                    }
                    return "No report found";
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to generate report", e);
            }
            // Connection automatically returned when try block exits
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Connection Leak War Story ===\n");
        
        // Create a small pool to demonstrate exhaustion
        SimpleConnectionPool pool = new SimpleConnectionPool(5, 1000);
        
        System.out.println("--- Simulating Connection Leak (Buggy Version) ---");
        ReportServiceBuggy buggyService = new ReportServiceBuggy(pool);
        
        // Simulate multiple requests that leak connections
        for (int i = 0; i < 10; i++) {
            try {
                // This will fail after 5 requests (pool size = 5)
                buggyService.generateReport("monthly");
            } catch (Exception e) {
                System.out.println("Request " + i + " failed: " + e.getMessage());
            }
        }
        
        System.out.println("\nPool status after buggy requests:");
        System.out.println("Active connections: " + pool.getActiveConnections());
        System.out.println("Available connections: " + pool.getAvailableConnections());
        
        // Shutdown and recreate pool for fixed version
        pool.shutdown();
        pool = new SimpleConnectionPool(5, 1000);
        
        System.out.println("\n--- Demonstrating Fixed Version ---");
        ReportServiceFixed fixedService = new ReportServiceFixed(pool);
        
        for (int i = 0; i < 10; i++) {
            try {
                fixedService.generateReport("monthly");
                System.out.println("Request " + i + " succeeded");
            } catch (Exception e) {
                System.out.println("Request " + i + " failed: " + e.getMessage());
            }
        }
        
        System.out.println("\nPool status after fixed requests:");
        System.out.println("Active connections: " + pool.getActiveConnections());
        System.out.println("Available connections: " + pool.getAvailableConnections());
        
        pool.shutdown();
        
        // Print investigation checklist
        printInvestigationChecklist();
    }

    private static void printInvestigationChecklist() {
        System.out.println("\n=== Connection Leak Investigation Checklist ===");
        System.out.println("1. Enable leak detection in HikariCP:");
        System.out.println("   config.setLeakDetectionThreshold(5000); // 5 seconds");
        System.out.println("\n2. Monitor connection pool metrics:");
        System.out.println("   - hikaricp_connections_active");
        System.out.println("   - hikaricp_connections_pending");
        System.out.println("   - hikaricp_connections_timeout_total");
        System.out.println("\n3. Thread dump analysis:");
        System.out.println("   Look for threads holding JDBC connections");
        System.out.println("   Check for connections not in pool but not closed");
        System.out.println("\n4. HikariCP configuration best practices:");
        System.out.println("   - Set connectionTimeout (don't wait forever)");
        System.out.println("   - Set maxLifetime (recycle connections)");
        System.out.println("   - Set leakDetectionThreshold (find leaks early)");
        System.out.println("   - Pool size = (core_count * 2) + effective_spindle_count");
        System.out.println("\n5. Code patterns to prevent leaks:");
        System.out.println("   - Always use try-with-resources for JDBC");
        System.out.println("   - Never return connections to calling code");
        System.out.println("   - Close connections in finally blocks");
        System.out.println("   - Use connection proxies for monitoring");
    }
}
