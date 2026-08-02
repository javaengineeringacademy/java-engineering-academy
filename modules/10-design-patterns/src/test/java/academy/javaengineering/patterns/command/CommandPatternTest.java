package academy.javaengineering.patterns.command;

import academy.javaengineering.patterns.command.CommandExample.Command;
import academy.javaengineering.patterns.command.CommandExample.Light;
import academy.javaengineering.patterns.command.CommandExample.LightOnCommand;
import academy.javaengineering.patterns.command.CommandExample.RemoteControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandPatternTest {

    private Light light;
    private RemoteControl remote;

    @BeforeEach
    void setUp() {
        light = new Light();
        remote = new RemoteControl();
    }

    @Test
    @DisplayName("Light should start off")
    void lightShouldStartOff() {
        assertFalse(light.isOn(), "Light should be off by default");
    }

    @Test
    @DisplayName("LightOnCommand should turn light on")
    void lightOnCommandShouldTurnOn() {
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        assertTrue(light.isOn(), "Light should be on after pressing button");
    }

    @Test
    @DisplayName("Undo should turn light off")
    void undoShouldTurnOff() {
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        remote.pressUndo();
        assertFalse(light.isOn(), "Light should be off after undo");
    }

    @Test
    @DisplayName("Should handle multiple execute-undo cycles")
    void shouldHandleMultipleCycles() {
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        assertTrue(light.isOn());
        remote.pressUndo();
        assertFalse(light.isOn());
        remote.pressButton();
        assertTrue(light.isOn());
        remote.pressUndo();
        assertFalse(light.isOn());
    }

    @Test
    @DisplayName("LightOnCommand should not toggle if already on")
    void lightOnCommandShouldNotToggleIfAlreadyOn() {
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        assertTrue(light.isOn());
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        assertTrue(light.isOn(), "Should remain on, not toggle off");
    }

    @Test
    @DisplayName("Pressing undo with empty history should not throw")
    void undoWithEmptyHistoryShouldNotThrow() {
        assertDoesNotThrow(remote::pressUndo,
                "Undo with no history should be a no-op");
    }

    @Test
    @DisplayName("Light should implement toggle correctly")
    void lightShouldToggleCorrectly() {
        assertFalse(light.isOn());
        light.toggle();
        assertTrue(light.isOn());
        light.toggle();
        assertFalse(light.isOn());
    }

    @Test
    @DisplayName("Command interface should have execute and undo methods")
    void commandInterfaceShouldHaveMethods() throws Exception {
        var executeMethod = Command.class.getDeclaredMethod("execute");
        var undoMethod = Command.class.getDeclaredMethod("undo");
        assertNotNull(executeMethod);
        assertNotNull(undoMethod);
    }

    @Test
    @DisplayName("LightOnCommand should implement Command")
    void lightOnCommandShouldImplementCommand() {
        assertInstanceOf(Command.class, new LightOnCommand(light));
    }

    @Test
    @DisplayName("RemoteControl should manage command history")
    void remoteControlShouldManageHistory() {
        remote.setCommand(new LightOnCommand(light));
        remote.pressButton();
        remote.pressButton(); // no-op since light is already on
        remote.pressUndo();   // turns off
        assertFalse(light.isOn());
        remote.pressUndo();   // no-op since light is already off
        assertFalse(light.isOn());
    }
}
