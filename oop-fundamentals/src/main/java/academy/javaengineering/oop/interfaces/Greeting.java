package academy.javaengineering.oop.interfaces;

/**
 * Greeting - Interface demonstrating static methods.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Greeting {

    String getMessage();

    static Greeting createGreeting(String message) {
        return () -> message;
    }

    static Greeting createFormalGreeting(String name) {
        return () -> "Dear " + name + ", greetings!";
    }

    static Greeting createCasualGreeting(String name) {
        return () -> "Hey " + name + "!";
    }
}