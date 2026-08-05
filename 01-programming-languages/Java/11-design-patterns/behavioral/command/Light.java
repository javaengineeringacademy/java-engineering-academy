package academy.javaengineering.patterns.behavioral.command;

/**
 * Receiver class - The object that performs the actual work.
 * Light is the receiver that knows how to turn on/off.
 */
public class Light {

    private boolean isOn = false;
    private final String location;

    public Light(String location) {
        this.location = location;
    }

    public void turnOn() {
        isOn = true;
        System.out.println(location + " light is ON");
    }

    public void turnOff() {
        isOn = false;
        System.out.println(location + " light is OFF");
    }

    public boolean isOn() {
        return isOn;
    }

    public String getLocation() {
        return location;
    }
}
