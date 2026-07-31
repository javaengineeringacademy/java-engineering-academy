package academy.javaengineering.oop.solid;

/**
 * MessageService - Interface for Dependency Inversion Principle.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface MessageService {

    void sendMessage(String message, String recipient);
}