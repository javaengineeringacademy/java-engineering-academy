package academy.javaengineering.oop.abstraction;

/**
 * InternetConnectable - Interface for internet connectivity.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface InternetConnectable {

    void connectToInternet();

    default void sendData(String data) {
        System.out.println("  Sending data: " + data);
    }

    default void disconnect() {
        System.out.println("  Disconnected from internet");
    }
}