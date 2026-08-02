package academy.javaengineering.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration - @Configuration, @Bean, @Profile.
 */
public class ConfigurationExample {

    public interface DataSource {
        String getConnection();
    }

    public static class H2DataSource implements DataSource {
        @Override
        public String getConnection() { return "H2 Connection"; }
    }

    public static class PostgresDataSource implements DataSource {
        @Override
        public String getConnection() { return "PostgreSQL Connection"; }
    }

    public static class AppConfig {
        private final Map<String, DataSource> dataSources = new HashMap<>();

        public void registerProfile(String profile, DataSource ds) { dataSources.put(profile, ds); }

        public DataSource getDataSource(String profile) { return dataSources.get(profile); }
    }

    public static void main(String[] args) {
        AppConfig config = new AppConfig();
        config.registerProfile("dev", new H2DataSource());
        config.registerProfile("prod", new PostgresDataSource());
        System.out.println("Dev: " + config.getDataSource("dev").getConnection());
        System.out.println("Prod: " + config.getDataSource("prod").getConnection());
    }
}
