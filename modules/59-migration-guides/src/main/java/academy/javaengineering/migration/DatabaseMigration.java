package academy.javaengineering.migration;

/**
 * Demonstrates database migration patterns.
 */
public class DatabaseMigration {

    public record MigrationStep(
        int version,
        String description,
        String upScript,
        String downScript
    ) {}

    public static java.util.List<MigrationStep> getMigrations() {
        return java.util.List.of(
            new MigrationStep(
                1,
                "Create users table",
                "CREATE TABLE users (id BIGINT PRIMARY KEY, name VARCHAR(255));",
                "DROP TABLE users;"
            ),
            new MigrationStep(
                2,
                "Add email column",
                "ALTER TABLE users ADD COLUMN email VARCHAR(255);",
                "ALTER TABLE users DROP COLUMN email;"
            ),
            new MigrationStep(
                3,
                "Create orders table",
                "CREATE TABLE orders (id BIGINT PRIMARY KEY, user_id BIGINT);",
                "DROP TABLE orders;"
            )
        );
    }
}
