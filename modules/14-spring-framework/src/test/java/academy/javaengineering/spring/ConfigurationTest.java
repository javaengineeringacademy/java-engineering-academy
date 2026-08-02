package academy.javaengineering.spring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {

    @Test
    void shouldReturnCorrectDataSourcePerProfile() {
        ConfigurationExample.AppConfig config = new ConfigurationExample.AppConfig();
        config.registerProfile("dev", new ConfigurationExample.H2DataSource());
        config.registerProfile("prod", new ConfigurationExample.PostgresDataSource());
        assertTrue(config.getDataSource("dev").getConnection().contains("H2"));
        assertTrue(config.getDataSource("prod").getConnection().contains("PostgreSQL"));
    }
}
