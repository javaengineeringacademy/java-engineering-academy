package academy.javaengineering.oop.dependencyinjection;

/**
 * TightCoupledOrderService - Example of tight coupling (BAD).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class TightCoupledOrderService {

    // Hard-coded dependencies - cannot change without modifying this class
    private final Notifier notifier = new EmailNotifier();
    private final Repository repository = new DatabaseRepository();

    public void placeOrder(String product, int quantity) {
        System.out.println("  Order placed: " + product + " x" + quantity);
        repository.save("Order: " + product);
        notifier.send("Order confirmation for " + product);
    }
}