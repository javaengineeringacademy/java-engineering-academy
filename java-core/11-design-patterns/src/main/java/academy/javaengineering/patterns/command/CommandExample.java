package academy.javaengineering.patterns.command;

import java.util.Stack;

/**
 * Demonstrates the Command design pattern for encapsulating requests.
 *
 * <p>The Command pattern encapsulates a request as an object, allowing parameterization
 * of clients with different requests, queueing, logging, and support for undoable
 * operations.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Command interface with execute and undo</li>
 *   <li>Concrete command implementations</li>
 *   <li>Invoker (remote control) and receiver (light)</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class CommandExample {

    /**
     * Command interface defining execute and undo operations.
     */
    public interface Command {
        /**
         * Executes the command.
         */
        void execute();

        /**
         * Undoes the command.
         */
        void undo();
    }

    /**
     * Light receiver class that performs the actual work.
     */
    public static class Light {
        private boolean on = false;

        /**
         * Toggles the light state.
         */
        public void toggle() {
            on = !on;
            System.out.println("Light is " + (on ? "ON" : "OFF"));
        }

        /**
         * Checks if the light is on.
         *
         * @return true if light is on
         */
        public boolean isOn() {
            return on;
        }
    }

    /**
     * Command to turn on the light.
     */
    public static class LightOnCommand implements Command {
        private final Light light;

        /**
         * Creates a command for the specified light.
         *
         * @param light the light to control
         */
        public LightOnCommand(Light light) {
            this.light = light;
        }

        @Override
        public void execute() {
            if (!light.isOn()) {
                light.toggle();
            }
        }

        @Override
        public void undo() {
            if (light.isOn()) {
                light.toggle();
            }
        }
    }

    /**
     * Remote control invoker that manages commands.
     */
    public static class RemoteControl {
        private Command command;
        private final Stack<Command> history = new Stack<>();

        /**
         * Sets the current command.
         *
         * @param command the command to execute
         */
        public void setCommand(Command command) {
            this.command = command;
        }

        /**
         * Executes the current command and saves to history.
         */
        public void pressButton() {
            command.execute();
            history.push(command);
        }

        /**
         * Undoes the last executed command.
         */
        public void pressUndo() {
            if (!history.isEmpty()) {
                history.pop().undo();
            }
        }
    }

    /**
     * Demonstrates command pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Light light = new Light();
        RemoteControl remote = new RemoteControl();

        remote.setCommand(new LightOnCommand(light));
        remote.pressButton(); // Light is ON
        remote.pressUndo();   // Light is OFF
    }
}
