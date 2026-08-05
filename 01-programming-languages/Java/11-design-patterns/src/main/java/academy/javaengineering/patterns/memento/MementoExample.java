package academy.javaengineering.patterns.memento;

import java.util.ArrayList;
import java.util.List;

// Memento
class TextMemento {
    private final String content;
    private final int cursorPosition;
    private final java.time.LocalDateTime timestamp;
    
    public TextMemento(String content, int cursorPosition) {
        this.content = content;
        this.cursorPosition = cursorPosition;
        this.timestamp = java.time.LocalDateTime.now();
    }
    
    public String getContent() { return content; }
    public int getCursorPosition() { return cursorPosition; }
    public java.time.LocalDateTime getTimestamp() { return timestamp; }
}

// Originator
class TextEditor {
    private StringBuilder content;
    private int cursorPosition;
    
    public TextEditor() {
        this.content = new StringBuilder();
        this.cursorPosition = 0;
    }
    
    public void type(String text) {
        content.insert(cursorPosition, text);
        cursorPosition += text.length();
        System.out.println("Typed: " + text);
    }
    
    public void delete(int count) {
        int start = Math.max(0, cursorPosition - count);
        content.delete(start, cursorPosition);
        cursorPosition = start;
        System.out.println("Deleted " + count + " characters");
    }
    
    public void moveCursor(int position) {
        this.cursorPosition = Math.max(0, Math.min(position, content.length()));
        System.out.println("Cursor moved to position: " + cursorPosition);
    }
    
    public TextMemento save() {
        System.out.println("Saving state...");
        return new TextMemento(content.toString(), cursorPosition);
    }
    
    public void restore(TextMemento memento) {
        this.content = new StringBuilder(memento.getContent());
        this.cursorPosition = memento.getCursorPosition();
        System.out.println("Restored state from " + memento.getTimestamp());
    }
    
    public String getContent() { return content.toString(); }
    public int getCursorPosition() { return cursorPosition; }
    
    public void display() {
        System.out.println("Content: \"" + content + "\" | Cursor: " + cursorPosition);
    }
}

// Caretaker
class History {
    private final List<TextMemento> mementos = new ArrayList<>();
    private int currentIndex = -1;
    
    public void push(TextMemento memento) {
        // Remove any redo history
        if (currentIndex < mementos.size() - 1) {
            mementos.subList(currentIndex + 1, mementos.size()).clear();
        }
        mementos.add(memento);
        currentIndex++;
        System.out.println("History: Saved state #" + currentIndex);
    }
    
    public TextMemento pop() {
        if (currentIndex <= 0) {
            System.out.println("No more undo history");
            return null;
        }
        currentIndex--;
        System.out.println("History: Undo to state #" + currentIndex);
        return mementos.get(currentIndex);
    }
    
    public TextMemento redo() {
        if (currentIndex >= mementos.size() - 1) {
            System.out.println("No more redo history");
            return null;
        }
        currentIndex++;
        System.out.println("History: Redo to state #" + currentIndex);
        return mementos.get(currentIndex);
    }
    
    public void display() {
        System.out.println("History: " + mementos.size() + " states, current: " + currentIndex);
    }
}

public class MementoExample {
    public static void main(String[] args) {
        System.out.println("=== Memento Pattern ===\n");
        
        TextEditor editor = new TextEditor();
        History history = new History();
        
        editor.type("Hello");
        history.push(editor.save());
        editor.display();
        
        System.out.println();
        editor.type(" World");
        history.push(editor.save());
        editor.display();
        
        System.out.println();
        editor.type("!");
        history.push(editor.save());
        editor.display();
        
        System.out.println("\n--- Undo ---");
        TextMemento undo1 = history.pop();
        if (undo1 != null) editor.restore(undo1);
        editor.display();
        
        System.out.println();
        TextMemento undo2 = history.pop();
        if (undo2 != null) editor.restore(undo2);
        editor.display();
        
        System.out.println("\n--- Redo ---");
        TextMemento redo1 = history.redo();
        if (redo1 != null) editor.restore(redo1);
        editor.display();
    }
}
