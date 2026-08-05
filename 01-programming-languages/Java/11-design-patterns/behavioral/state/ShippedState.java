package academy.javaengineering.patterns.behavioral.state;

/**
 * Concrete State implementation - Shipped Order.
 * Final state when the order has been shipped.
 */
public class ShippedState implements State {

    @Override
    public void handle(Order context) {
        System.out.println("Order has been shipped! Delivery in 3-5 days.");
        System.out.println("Order lifecycle complete.");
    }

    @Override
    public String getStateName() {
        return "SHIPPED";
    }
}
