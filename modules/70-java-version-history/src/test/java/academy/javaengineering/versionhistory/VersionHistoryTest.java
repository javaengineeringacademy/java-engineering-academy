package academy.javaengineering.versionhistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Java Version History Tests")
class VersionHistoryTest {

    @Test
    @DisplayName("Should have Java versions")
    void testJavaVersions() {
        var versions = JavaVersionHistory.getVersions();
        assertFalse(versions.isEmpty());
        assertTrue(versions.stream().anyMatch(v -> v.version().equals("Java 21")));
    }

    @Test
    @DisplayName("Should have migration guides")
    void testMigrationGuides() {
        var guides = MigrationGuides.getGuides();
        assertFalse(guides.isEmpty());
        assertTrue(guides.stream().anyMatch(g -> g.to().equals("Java 17")));
    }
}
