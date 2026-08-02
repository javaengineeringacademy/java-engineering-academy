package academy.javaengineering.migration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Migration Guides Tests")
class MigrationTest {

    @Test
    @DisplayName("Should have Java version migrations")
    void testJavaMigrations() {
        var migrations = MigrationGuide.getMigrations();
        assertFalse(migrations.isEmpty());
        assertTrue(migrations.stream().anyMatch(m -> m.toVersion().equals("Java 21")));
    }

    @Test
    @DisplayName("Database migrations should have up and down scripts")
    void testDatabaseMigrations() {
        var migrations = DatabaseMigration.getMigrations();
        assertFalse(migrations.isEmpty());
        migrations.forEach(m -> {
            assertNotNull(m.upScript());
            assertNotNull(m.downScript());
        });
    }
}
