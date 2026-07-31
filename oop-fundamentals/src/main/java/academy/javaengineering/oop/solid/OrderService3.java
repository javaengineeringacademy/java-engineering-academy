package academy.javaengineering.oop.solid;

/**
 * OrderService3 - Demonstrates Dependency Inversion Principle.
 * 
 * <p>High-level module (OrderService) depends on abstraction (MessageService),
 * not on low-level implementations (EmailService, SmsService).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class OrderService3 {

    private final MessageService messageService;

    // Depends on abstraction, not concretion
    public OrderService3(MessageService messageService) {
        this.messageService = messageService;
    }

    public void placeOrder(String product) {
        System.out.println("  [ORDER] Placing order for: " + product);
        messageService.sendMessage("Order confirmed: " + product, "customer@example.com");
    }
}