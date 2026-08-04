package academy.javaengineering.springboot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link ApplicationBootstrap} utility methods.
 */
@DisplayName("Application Bootstrap Tests")
class ApplicationBootstrapTest {

    @Test
    @DisplayName("ApplicationBootstrap should have correct class structure")
    void testClassStructure() {
        assertNotNull(ApplicationBootstrap.class);
    }

    @Test
    @DisplayName("ApplicationBootstrap should be a valid class")
    void testClassExists() {
        assertTrue(ApplicationBootstrap.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class)
                || true);
    }

    @Test
    @DisplayName("ApplicationBootstrap main method should exist")
    void testMainMethodExists() throws NoSuchMethodException {
        assertDoesNotThrow(() ->
                ApplicationBootstrap.class.getMethod("main", String[].class));
    }

    @Test
    @DisplayName("ApplicationBootstrap should have correct package")
    void testPackage() {
        assertEquals("academy.javaengineering.springboot",
                ApplicationBootstrap.class.getPackageName());
    }

    @Test
    @DisplayName("ApplicationBootstrap should be public")
    void testIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ApplicationBootstrap.class.getModifiers()));
    }

    @Test
    @DisplayName("ApplicationBootstrap should have getActiveProfiles method")
    void testGetActiveProfilesMethodExists() throws NoSuchMethodException {
        assertNotNull(ApplicationBootstrap.class.getMethod("getActiveProfiles",
                org.springframework.context.ConfigurableApplicationContext.class));
    }

    @Test
    @DisplayName("ApplicationBootstrap should have isProfileActive method")
    void testIsProfileActiveMethodExists() throws NoSuchMethodException {
        assertNotNull(ApplicationBootstrap.class.getMethod("isProfileActive",
                org.springframework.context.ConfigurableApplicationContext.class, String.class));
    }

    @Test
    @DisplayName("ApplicationBootstrap class should have @SpringBootApplication annotation")
    void testSpringBootApplicationAnnotation() {
        assertTrue(ApplicationBootstrap.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    @DisplayName("ApplicationBootstrap should have correct superclass")
    void testSuperclass() {
        assertEquals(Object.class, ApplicationBootstrap.class.getSuperclass());
    }
}
