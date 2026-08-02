package academy.javaengineering.cheatsheets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cheat Sheets Tests")
class CheatSheetTest {

    @Test
    @DisplayName("Java syntax should have key references")
    void testJavaSyntax() {
        var syntax = JavaCheatSheet.getSyntaxQuickReference();
        assertTrue(syntax.containsKey("Variable"));
        assertTrue(syntax.containsKey("Class"));
        assertTrue(syntax.containsKey("If"));
    }

    @Test
    @DisplayName("Collections should have all types")
    void testCollections() {
        var collections = JavaCheatSheet.getCollectionsQuickReference();
        assertTrue(collections.containsKey("ArrayList"));
        assertTrue(collections.containsKey("HashMap"));
        assertTrue(collections.containsKey("HashSet"));
    }

    @Test
    @DisplayName("Spring annotations should be documented")
    void testSpringAnnotations() {
        var annotations = SpringBootCheatSheet.getAnnotationsQuickReference();
        assertTrue(annotations.containsKey("@RestController"));
        assertTrue(annotations.containsKey("@Service"));
    }
}
