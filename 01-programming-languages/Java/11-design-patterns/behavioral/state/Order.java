package academy.javaengineering.patterns.behavioral.state;

/**
 * Context class - Order that changes behavior based on its state.
 * Maintains a reference to the current state and delegates to it.
 */
public class Order {

    private State currentState;
    private final String orderId;

    public Order(String orderId) {
        this.orderId = orderId;
        this.currentState = new NewState();
    }

    public void process() {
        currentState.handle(this);
    }

    public void setState(State state) {
        System.out.println("Order " + orderId + ": " +
                currentState.getStateName() + " -> " + state.getStateName());
        this.currentState = state;
    }

    public State getCurrentState() {
        return currentState;
    }

    public String getOrderId() {
        return orderId;
    }
}
