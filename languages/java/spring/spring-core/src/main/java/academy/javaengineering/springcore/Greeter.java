package academy.javaengineering.springcore;

/**
 * Demonstrates Spring dependency injection.
 */
public class Greeter {
    private final String message;

    public Greeter(String message) {
        this.message = message;
    }

    public String greet() {
        return message;
    }
}
