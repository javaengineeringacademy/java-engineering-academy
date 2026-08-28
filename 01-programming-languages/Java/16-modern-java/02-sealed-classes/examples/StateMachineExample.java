package academy.javaengineering.modern.sealed;

import java.util.List;

/**
 * Sealed classes for state machines.
 */
public class StateMachineExample {

    // Traffic light states
    public sealed interface TrafficLight permits Red, Yellow, Green {}
    public record Red() implements TrafficLight {
        public TrafficLight next() { return new Green(); }
        public String color() { return "RED"; }
    }
    public record Yellow() implements TrafficLight {
        public TrafficLight next() { return new Red(); }
        public String color() { return "YELLOW"; }
    }
    public record Green() implements TrafficLight {
        public TrafficLight next() { return new Yellow(); }
        public String color() { return "GREEN"; }
    }

    // Order states
    public sealed interface OrderState permits Pending, Processing, Shipped, Delivered, Cancelled {}
    public record Pending() implements OrderState {
        public OrderState process() { return new Processing(); }
        public OrderState cancel() { return new Cancelled(); }
    }
    public record Processing() implements OrderState {
        public OrderState ship() { return new Shipped(); }
        public OrderState cancel() { return new Cancelled(); }
    }
    public record Shipped() implements OrderState {
        public OrderState deliver() { return new Delivered(); }
    }
    public record Delivered() implements OrderState {}
    public record Cancelled() implements OrderState {}

    public static void main(String[] args) {
        // Traffic light simulation
        System.out.println("=== Traffic Light Simulation ===");
        TrafficLight light = new Red();
        for (int i = 0; i < 6; i++) {
            System.out.println("Light: " + light.color());
            light = light.next();
        }

        // Order state machine
        System.out.println("\n=== Order State Machine ===");
        OrderState order = new Pending();
        System.out.println("Order state: " + getStateName(order));

        order = ((Pending) order).process();
        System.out.println("After process: " + getStateName(order));

        order = ((Processing) order).ship();
        System.out.println("After ship: " + getStateName(order));

        order = ((Shipped) order).deliver();
        System.out.println("After deliver: " + getStateName(order));

        // Pattern matching for state description
        System.out.println("\n=== State Descriptions ===");
        List<OrderState> states = List.of(
            new Pending(),
            new Processing(),
            new Shipped(),
            new Delivered(),
            new Cancelled()
        );

        for (OrderState state : states) {
            System.out.println(getStateDescription(state));
        }
    }

    static String getStateName(OrderState state) {
        return switch (state) {
            case Pending p -> "Pending";
            case Processing p -> "Processing";
            case Shipped s -> "Shipped";
            case Delivered d -> "Delivered";
            case Cancelled c -> "Cancelled";
        };
    }

    static String getStateDescription(OrderState state) {
        return switch (state) {
            case Pending p -> "Order is waiting to be processed";
            case Processing p -> "Order is being prepared";
            case Shipped s -> "Order is on its way";
            case Delivered d -> "Order has been delivered";
            case Cancelled c -> "Order has been cancelled";
        };
    }
}
