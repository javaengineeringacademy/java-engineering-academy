package academy.javaengineering.patterns.behavioral.command;

/**
 * Concrete Command implementation - Turn Light Off.
 * Encapsulates the request to turn off a light.
 */
public class LightOffCommand implements Command {

    private final Light light;

    public LightOffCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOff();
    }

    @Override
    public void undo() {
        light.turnOn();
    }

    public Light getLight() {
        return light;
    }
}
