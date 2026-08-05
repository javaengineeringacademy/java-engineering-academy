package academy.javaengineering.patterns.behavioral.state;

/**
 * Concrete State implementation - New Order.
 * The initial state when an order is first created.
 */
public class NewState implements State {

    @Override
    public void handle(Order context) {
        System.out.println("Processing new order...");
        context.setState(new ProcessedState());
    }

    @Override
    public String getStateName() {
        return "NEW";
    }
}
