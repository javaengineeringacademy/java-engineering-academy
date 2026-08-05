package academy.javaengineering.patterns.behavioral.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Invoker class - The Remote Control that triggers commands.
 * Maintains history for undo functionality.
 */
public class RemoteControl {

    private final List<Command> commandHistory = new ArrayList<>();
    private final Stack<Command> undoStack = new Stack<>();

    public void pressButton(Command command) {
        command.execute();
        commandHistory.add(command);
        undoStack.push(command);
    }

    public void pressUndo() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            System.out.println("Undoing: " + lastCommand.getClass().getSimpleName());
            lastCommand.undo();
        } else {
            System.out.println("Nothing to undo");
        }
    }

    public List<Command> getCommandHistory() {
        return commandHistory;
    }

    public Stack<Command> getUndoStack() {
        return undoStack;
    }
}
