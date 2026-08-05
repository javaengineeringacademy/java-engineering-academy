package academy.javaengineering.springcore;

public class Greeter {
    private final String greeting;

    public Greeter(String greeting) {
        this.greeting = greeting;
    }

    public String sayHello(String name) {
        return greeting + ", " + name + "!";
    }

    public String sayGoodbye(String name) {
        return "Goodbye, " + name + "!";
    }

    public String getGreeting() {
        return greeting;
    }
}
