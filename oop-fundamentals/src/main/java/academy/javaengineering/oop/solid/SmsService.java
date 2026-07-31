package academy.javaengineering.oop.solid;

/**
 * SmsService - Concrete implementation of MessageService.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SmsService implements MessageService {

    @Override
    public void sendMessage(String message, String recipient) {
        System.out.println("  [SMS] To " + recipient + ": " + message);
    }
}