package academy.javaengineering.patterns.behavioral.state;

/**
 * Real-world example demonstrating the State pattern.
 * Shows an order going through different states.
 */
public class StateExample {

    public static void main(String[] args) {
        Order order = new Order("ORD-001");

        System.out.println("=== Order State Transitions ===");
        System.out.println("Current state: " + order.getCurrentState().getStateName());

        order.process();
        order.process();
        order.process();

        System.out.println("\n=== Another Order ===");
        Order order2 = new Order("ORD-002");
        order2.process();
        order2.process();
    }
}
