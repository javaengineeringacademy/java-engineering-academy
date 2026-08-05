package academy.javaengineering.patterns.behavioral.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    private Light light;
    private LightOnCommand onCommand;
    private LightOffCommand offCommand;
    private RemoteControl remote;

    @BeforeEach
    void setUp() {
        light = new Light("Test");
        onCommand = new LightOnCommand(light);
        offCommand = new LightOffCommand(light);
        remote = new RemoteControl();
    }

    @Test
    void lightOnCommandShouldTurnLightOn() {
        onCommand.execute();
        assertTrue(light.isOn());
    }

    @Test
    void lightOffCommandShouldTurnLightOff() {
        light.turnOn();
        offCommand.execute();
        assertFalse(light.isOn());
    }

    @Test
    void undoShouldReverseCommand() {
        onCommand.execute();
        assertTrue(light.isOn());

        onCommand.undo();
        assertFalse(light.isOn());
    }

    @Test
    void remoteShouldExecuteCommands() {
        remote.pressButton(onCommand);
        assertTrue(light.isOn());
        assertEquals(1, remote.getCommandHistory().size());
    }

    @Test
    void remoteShouldUndoLastCommand() {
        remote.pressButton(onCommand);
        remote.pressUndo();
        assertFalse(light.isOn());
        assertTrue(remote.getUndoStack().isEmpty());
    }
}
