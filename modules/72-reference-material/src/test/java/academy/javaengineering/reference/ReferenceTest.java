package academy.javaengineering.reference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reference Material Tests")
class ReferenceTest {

    @Test
    @DisplayName("Should have Java keywords")
    void testKeywords() {
        var keywords = ReferenceMaterial.getJavaKeywords();
        assertFalse(keywords.isEmpty());
        assertTrue(keywords.containsKey("class"));
        assertTrue(keywords.containsKey("public"));
    }

    @Test
    @DisplayName("Should have string methods")
    void testStringMethods() {
        var methods = ApiQuickReference.getStringMethods();
        assertFalse(methods.isEmpty());
        assertTrue(methods.containsKey("length()"));
        assertTrue(methods.containsKey("charAt(int)"));
    }

    @Test
    @DisplayName("Should have collection methods")
    void testCollectionMethods() {
        var methods = ApiQuickReference.getCollectionMethods();
        assertFalse(methods.isEmpty());
        assertTrue(methods.containsKey("add(E)"));
        assertTrue(methods.containsKey("size()"));
    }
}
