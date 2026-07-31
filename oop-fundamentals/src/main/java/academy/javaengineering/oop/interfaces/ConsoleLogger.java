package academy.javaengineering.oop.interfaces;

/**
 * ConsoleLogger - Concrete implementation of AdvancedLogger interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ConsoleLogger implements AdvancedLogger {

    @Override
    public void log(String message) {
        System.out.println("  LOG: " + message);
    }

    @Override
    public void clearLogs() {
        System.out.println("  Logs cleared");
    }
}