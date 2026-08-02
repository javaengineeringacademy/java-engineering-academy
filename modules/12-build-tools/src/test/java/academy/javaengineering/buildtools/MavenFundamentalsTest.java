package academy.javaengineering.buildtools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MavenFundamentalsTest {

    @Test
    void shouldCreatePomWithDependencies() {
        MavenFundamentalsExample.MavenProject project =
                new MavenFundamentalsExample.MavenProject("com.example", "app", "1.0.0");
        project.addDependency("org.junit", "junit", "4.13.2", "test");
        String pom = project.getPomXml();
        assertTrue(pom.contains("<groupId>com.example</groupId>"));
        assertTrue(pom.contains("<artifactId>junit</artifactId>"));
        assertTrue(pom.contains("<scope>test</scope>"));
    }

    @Test
    void shouldGenerateValidPomStructure() {
        MavenFundamentalsExample.MavenProject project =
                new MavenFundamentalsExample.MavenProject("com.test", "lib", "2.0.0");
        String pom = project.getPomXml();
        assertTrue(pom.startsWith("<project>"));
        assertTrue(pom.endsWith("</project>"));
    }

    @Test
    void shouldHandleMultipleDependencies() {
        MavenFundamentalsExample.MavenProject project =
                new MavenFundamentalsExample.MavenProject("com.test", "app", "1.0.0");
        project.addDependency("org.springframework", "spring-core", "6.1.0", "compile");
        project.addDependency("junit", "junit", "4.13.2", "test");
        String pom = project.getPomXml();
        assertTrue(pom.contains("spring-core"));
        assertTrue(pom.contains("junit"));
    }
}
