package academy.javaengineering.patterns.behavioral.command;

/**
 * Concrete Command implementation - Turn Light On.
 * Encapsulates the request to turn on a light.
 */
public class LightOnCommand implements Command {

    private final Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }

    @Override
    public void undo() {
        light.turnOff();
    }

    public Light getLight() {
        return light;
    }
}
