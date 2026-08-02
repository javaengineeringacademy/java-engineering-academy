package academy.javaengineering.patterns.command;

import java.util.Stack;

public class CommandExample {

    public interface Command {
        void execute();
        void undo();
    }

    public static class Light {
        private boolean on = false;

        public void toggle() {
            on = !on;
            System.out.println("Light is " + (on ? "ON" : "OFF"));
        }

        public boolean isOn() {
            return on;
        }
    }

    public static class LightOnCommand implements Command {
        private final Light light;

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

    public static class RemoteControl {
        private Command command;
        private final Stack<Command> history = new Stack<>();

        public void setCommand(Command command) {
            this.command = command;
        }

        public void pressButton() {
            command.execute();
            history.push(command);
        }

        public void pressUndo() {
            if (!history.isEmpty()) {
                history.pop().undo();
            }
        }
    }

    public static void main(String[] args) {
        Light light = new Light();
        RemoteControl remote = new RemoteControl();

        remote.setCommand(new LightOnCommand(light));
        remote.pressButton(); // Light is ON
        remote.pressUndo();   // Light is OFF
    }
}
