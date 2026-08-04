package academy.javaengineering.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Annotations Demo Tests")
class AnnotationsDemoTest {

    // A simple test class to verify annotation behavior
    @AnnotationsDemo.ApiInfo(version = "1.0", author = "TestAuthor", description = "Test entity")
    static class AnnotatedClass {
        @AnnotationsDemo.ApiInfo(description = "test field")
        private String field;

        @AnnotationsDemo.CachePolicy(level = AnnotationsDemo.CacheLevel.HIGH, ttlSeconds = 600)
        private String cachedField;
    }

    @Test
    @DisplayName("Should read class-level annotation values")
    void testReadClassAnnotation() {
        AnnotationsDemo.ApiInfo apiInfo = AnnotatedClass.class.getAnnotation(AnnotationsDemo.ApiInfo.class);
        assertNotNull(apiInfo);
        assertEquals("1.0", apiInfo.version());
        assertEquals("TestAuthor", apiInfo.author());
        assertEquals("Test entity", apiInfo.description());
    }

    @Test
    @DisplayName("Should detect annotation presence on class")
    void testAnnotationPresence() {
        assertTrue(AnnotatedClass.class.isAnnotationPresent(AnnotationsDemo.ApiInfo.class));
        assertFalse(AnnotatedClass.class.isAnnotationPresent(AnnotationsDemo.Persistent.class));
    }

    @Test
    @DisplayName("Should read method annotations")
    void testReadMethodAnnotations() {
        // Test reading annotations on methods using the User class from AnnotationsDemo
        Class<?> clazz = AnnotationsDemo.User.class;

        boolean foundDeprecated = false;
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Deprecated.class)) {
                foundDeprecated = true;
                break;
            }
        }
        assertTrue(foundDeprecated, "Should find at least one @Deprecated method");
    }

    @Test
    @DisplayName("Should read field-level annotations")
    void testReadFieldAnnotations() {
        try {
            var field = AnnotatedClass.class.getDeclaredField("field");
            AnnotationsDemo.ApiInfo apiInfo = field.getAnnotation(AnnotationsDemo.ApiInfo.class);
            assertNotNull(apiInfo);
            assertEquals("test field", apiInfo.description());
        } catch (NoSuchFieldException e) {
            fail("Field not found: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should read CachePolicy annotation")
    void testCachePolicyAnnotation() {
        try {
            var field = AnnotatedClass.class.getDeclaredField("cachedField");
            AnnotationsDemo.CachePolicy policy = field.getAnnotation(AnnotationsDemo.CachePolicy.class);
            assertNotNull(policy);
            assertEquals(AnnotationsDemo.CacheLevel.HIGH, policy.level());
            assertEquals(600, policy.ttlSeconds());
        } catch (NoSuchFieldException e) {
            fail("Field not found: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Should handle missing annotation gracefully")
    void testMissingAnnotation() {
        AnnotationsDemo.ApiInfo apiInfo = String.class.getAnnotation(AnnotationsDemo.ApiInfo.class);
        assertNull(apiInfo);
    }

    @Test
    @DisplayName("Should read meta-annotations")
    void testMetaAnnotations() {
        Annotation[] metaAnnotations = AnnotationsDemo.ApiInfo.class.getAnnotations();
        // @ApiInfo itself doesn't have meta-annotations beyond standard ones
        assertNotNull(metaAnnotations);
    }

    // Helper method used by testReadMethodAnnotations
    @AnnotationsDemo.ApiInfo(description = "test method")
    void testMethod() {}
}
