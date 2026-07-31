package academy.javaengineering.oop.dependencyinjection;

/**
 * OrderProcessor - Demonstrates constructor injection.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class OrderProcessor {

    private final NotificationService notificationService;

    public OrderProcessor(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void processOrder(String product, int quantity) {
        System.out.println("  Processing order: " + product + " x" + quantity);
        notificationService.notify("Order processed: " + product);
    }
}