package academy.javaengineering.oop.solid;

/**
 * EmailService - Concrete implementation of MessageService.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class EmailService implements MessageService {

    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("  [EMAIL] To " + recipient + ": " + message);
    }
}