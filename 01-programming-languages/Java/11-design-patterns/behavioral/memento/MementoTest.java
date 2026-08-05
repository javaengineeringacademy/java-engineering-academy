package academy.javaengineering.patterns.behavioral.memento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MementoTest {

    private Editor editor;
    private History history;

    @BeforeEach
    void setUp() {
        editor = new Editor();
        history = new History();
    }

    @Test
    void mementoShouldStoreContent() {
        Memento memento = new Memento("test content");
        assertEquals("test content", memento.getContent());
    }

    @Test
    void editorShouldSaveAndRestore() {
        editor.write("original");
        Memento saved = editor.save();
        editor.write("modified");
        editor.restore(saved);
        assertEquals("original", editor.getContent());
    }

    @Test
    void historyShouldTrackMementos() {
        history.push(editor.save());
        history.push(editor.save());
        assertEquals(2, history.size());
    }

    @Test
    void historyShouldAllowUndo() {
        editor.write("v1");
        history.push(editor.save());
        editor.write("v2");
        history.push(editor.save());

        editor.restore(history.pop());
        assertEquals("v1", editor.getContent());
    }

    @Test
    void historyShouldReportCanUndo() {
        assertFalse(history.canUndo());
        history.push(editor.save());
        assertTrue(history.canUndo());
    }
}
