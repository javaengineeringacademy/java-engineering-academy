package academy.javaengineering.oop.dependencyinjection;

/**
 * LooselyCoupledOrderService - Demonstrates proper dependency injection.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class LooselyCoupledOrderService {

    private final Notifier notifier;
    private final Repository repository;

    // Constructor injection - dependencies provided from outside
    public LooselyCoupledOrderService(Notifier notifier, Repository repository) {
        this.notifier = notifier;
        this.repository = repository;
    }

    public void placeOrder(String product, int quantity) {
        System.out.println("  Order placed: " + product + " x" + quantity);
        repository.save("Order: " + product);
        notifier.send("Order confirmation for " + product);
    }
}