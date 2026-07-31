package academy.javaengineering.oop.bank;

import java.time.LocalDateTime;

/**
 * TransactionLogger - Logs all banking transactions.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class TransactionLogger {

    public void log(String message) {
        System.out.println("  [" + LocalDateTime.now() + "] " + message);
    }
}