package academy.javaengineering.buildtools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradleFundamentalsTest {

    @Test
    void shouldGenerateBuildScript() {
        GradleFundamentalsExample.GradleBuild build = new GradleFundamentalsExample.GradleBuild("my-app");
        build.applyPlugin("java");
        String script = build.getBuildScript();
        assertTrue(script.contains("id 'java'"));
    }

    @Test
    void shouldIncludeDependencies() {
        GradleFundamentalsExample.GradleBuild build = new GradleFundamentalsExample.GradleBuild("my-app");
        build.applyPlugin("java");
        build.addDependency("implementation", "org.springframework:spring-core:6.1.0");
        String script = build.getBuildScript();
        assertTrue(script.contains("implementation 'org.springframework:spring-core:6.1.0'"));
    }

    @Test
    void shouldHandleMultiplePlugins() {
        GradleFundamentalsExample.GradleBuild build = new GradleFundamentalsExample.GradleBuild("my-app");
        build.applyPlugin("java");
        build.applyPlugin("org.springframework.boot");
        String script = build.getBuildScript();
        assertTrue(script.contains("id 'java'"));
        assertTrue(script.contains("id 'org.springframework.boot'"));
    }
}
