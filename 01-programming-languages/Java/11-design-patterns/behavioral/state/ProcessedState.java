package academy.javaengineering.patterns.behavioral.state;

/**
 * Concrete State implementation - Processed Order.
 * State when the order is being processed/prepared.
 */
public class ProcessedState implements State {

    @Override
    public void handle(Order context) {
        System.out.println("Order is being prepared for shipment...");
        context.setState(new ShippedState());
    }

    @Override
    public String getStateName() {
        return "PROCESSED";
    }
}
