package academy.javaengineering.patterns.behavioral.memento;

/**
 * Real-world example demonstrating the Memento pattern.
 * Shows an editor with save/restore functionality.
 */
public class MementoExample {

    public static void main(String[] args) {
        Editor editor = new Editor();
        History history = new History();

        System.out.println("=== Editor with Undo/Redo ===");
        editor.write("First version");
        history.push(editor.save());

        editor.write("Second version");
        history.push(editor.save());

        editor.write("Third version");
        System.out.println("Current: " + editor.getContent());

        System.out.println("\n=== Undo ===");
        editor.restore(history.pop());
        System.out.println("After undo: " + editor.getContent());

        System.out.println("\n=== Undo Again ===");
        editor.restore(history.pop());
        System.out.println("After undo: " + editor.getContent());
    }
}
