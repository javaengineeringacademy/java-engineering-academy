package academy.javaengineering.patterns.behavioral.command;

/**
 * Real-world example demonstrating the Command pattern.
 * Shows a remote control operating lights with undo capability.
 */
public class CommandExample {

    public static void main(String[] args) {
        Light livingRoomLight = new Light("Living Room");
        Light bedroomLight = new Light("Bedroom");

        Command livingRoomOn = new LightOnCommand(livingRoomLight);
        Command livingRoomOff = new LightOffCommand(livingRoomLight);
        Command bedroomOn = new LightOnCommand(bedroomLight);

        RemoteControl remote = new RemoteControl();

        System.out.println("=== Pressing Buttons ===");
        remote.pressButton(livingRoomOn);
        remote.pressButton(bedroomOn);
        remote.pressButton(livingRoomOff);

        System.out.println("\n=== Undo Last Action ===");
        remote.pressUndo();

        System.out.println("\n=== Undo Another Action ===");
        remote.pressUndo();

        System.out.println("\n=== Command History ===");
        System.out.println("Commands executed: " + remote.getCommandHistory().size());
    }
}
