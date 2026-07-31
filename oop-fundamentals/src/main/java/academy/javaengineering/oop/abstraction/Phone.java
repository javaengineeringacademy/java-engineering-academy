package academy.javaengineering.oop.abstraction;

/**
 * Phone - Interface for phone functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Phone {

    void call(String number);

    default void endCall() {
        System.out.println("  Call ended");
    }

    String getPhoneNumber();
}