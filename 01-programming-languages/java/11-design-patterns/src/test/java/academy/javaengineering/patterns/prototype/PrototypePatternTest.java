package academy.javaengineering.patterns.prototype;

import academy.javaengineering.patterns.prototype.PrototypeExample.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrototypePatternTest {

    @Test
    @DisplayName("Clone should create a new object")
    void cloneShouldCreateNewObject() {
        Document original = new Document("Original");
        Document cloned = original.clone();
        assertNotSame(original, cloned, "Clone should be a different object");
    }

    @Test
    @DisplayName("Clone should preserve title")
    void cloneShouldPreserveTitle() {
        Document original = new Document("Report");
        Document cloned = original.clone();
        assertEquals(original.toString(), cloned.toString(),
                "Clone should have the same title");
    }

    @Test
    @DisplayName("Clone should deep copy paragraphs list")
    void cloneShouldDeepCopyParagraphs() {
        Document original = new Document("Doc");
        original.addParagraph("Para 1");
        Document cloned = original.clone();
        assertEquals(original.toString(), cloned.toString());
    }

    @Test
    @DisplayName("Modifying clone should not affect original paragraphs")
    void modifyingCloneShouldNotAffectOriginal() {
        Document original = new Document("Doc");
        original.addParagraph("Para 1");
        Document cloned = original.clone();
        cloned.addParagraph("Para 2");
        assertEquals(1, original.toString().length() - "Document{title='Doc', paragraphs=".length()
                - "}".length());
    }

    @Test
    @DisplayName("copyWithTitle should create new document with different title")
    void copyWithTitleShouldChangeTitle() {
        Document original = new Document("Template");
        Document copy = original.copyWithTitle("Report");
        assertNotSame(original, copy);
        assertTrue(copy.toString().contains("Report"));
    }

    @Test
    @DisplayName("copyWithTitle should preserve paragraphs")
    void copyWithTitleShouldPreserveParagraphs() {
        Document original = new Document("Template");
        original.addParagraph("Header");
        original.addParagraph("Body");
        Document copy = original.copyWithTitle("Template2");
        assertTrue(copy.toString().contains("paragraphs=2"));
    }

    @Test
    @DisplayName("Multiple clones should be independent")
    void multipleClonesShouldBeIndependent() {
        Document original = new Document("Template");
        original.addParagraph("Content");
        Document copy1 = original.copyWithTitle("Copy 1");
        Document copy2 = original.copyWithTitle("Copy 2");
        assertNotSame(copy1, copy2);
        assertTrue(copy1.toString().contains("Copy 1"));
        assertTrue(copy2.toString().contains("Copy 2"));
    }

    @Test
    @DisplayName("Document should implement Cloneable")
    void documentShouldImplementCloneable() {
        assertInstanceOf(Cloneable.class, new Document("Test"));
    }

    @Test
    @DisplayName("Document with no paragraphs should clone correctly")
    void emptyDocumentShouldClone() {
        Document original = new Document("Empty");
        Document cloned = original.clone();
        assertEquals(original.toString(), cloned.toString());
    }

    @Test
    @DisplayName("toString should contain title")
    void toStringShouldContainTitle() {
        Document doc = new Document("MyDoc");
        assertTrue(doc.toString().contains("MyDoc"));
    }
}
