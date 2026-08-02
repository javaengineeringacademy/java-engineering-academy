package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JdbcFundamentalsTest {

    @Test
    void shouldCreateDatabaseConfig() {
        JdbcFundamentalsExample.DatabaseConfig config =
                new JdbcFundamentalsExample.DatabaseConfig("jdbc:h2:mem:test", "sa", "");
        assertNotNull(config);
    }

    @Test
    void shouldCreateUserRepository() {
        JdbcFundamentalsExample.DatabaseConfig config =
                new JdbcFundamentalsExample.DatabaseConfig("jdbc:h2:mem:test", "sa", "");
        JdbcFundamentalsExample.UserRepository repo = new JdbcFundamentalsExample.UserRepository(config);
        assertNotNull(repo);
    }
}
