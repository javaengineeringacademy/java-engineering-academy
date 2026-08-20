package academy.javaengineering.testing.testcontainers.solutions;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class Exercise1DatabaseTestSolution {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Test
    void shouldCreateAndInsert() throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(100))");
            stmt.execute("INSERT INTO users (name) VALUES ('Alice')");

            ResultSet rs = stmt.executeQuery("SELECT name FROM users WHERE name = 'Alice'");
            assertTrue(rs.next());
            assertEquals("Alice", rs.getString("name"));
        }
    }

    @Test
    void shouldHandleDuplicateKey() throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE unique_users (id SERIAL PRIMARY KEY, email VARCHAR(100) UNIQUE)");
            stmt.execute("INSERT INTO unique_users (email) VALUES ('test@test.com')");

            assertThrows(SQLException.class, () ->
                stmt.execute("INSERT INTO unique_users (email) VALUES ('test@test.com')"));
        }
    }
}
