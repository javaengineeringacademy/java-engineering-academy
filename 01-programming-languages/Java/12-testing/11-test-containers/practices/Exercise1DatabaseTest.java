package academy.javaengineering.testing.testcontainers.practices;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercise 1: Database Container Test
 *
 * Tasks:
 * 1. Configure PostgreSQL container
 * 2. Create a table and insert data
 * 3. Query and verify data
 * 4. Test error scenarios
 */
@Testcontainers
class Exercise1DatabaseTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @Test
    @DisplayName("should create table and insert data")
    void shouldCreateAndInsert() throws SQLException {
        // Arrange: get connection
        // Act: create table, insert data
        // Assert: verify data exists
    }

    @Test
    @DisplayName("should handle duplicate key error")
    void shouldHandleDuplicateKey() throws SQLException {
        // Arrange: create table with unique constraint
        // Act: insert duplicate
        // Assert: verify exception
    }
}
