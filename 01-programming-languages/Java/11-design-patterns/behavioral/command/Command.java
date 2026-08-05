package academy.javaengineering.patterns.behavioral.command;

/**
 * Command interface for encapsulating a request as an object.
 * Allows parameterizing clients with different requests,
 * queuing requests, and supporting undoable operations.
 */
public interface Command {

    /**
     * Execute the command.
     */
    void execute();

    /**
     * Undo the command (optional).
     */
    default void undo() {
        System.out.println("Undo not implemented");
    }
}
