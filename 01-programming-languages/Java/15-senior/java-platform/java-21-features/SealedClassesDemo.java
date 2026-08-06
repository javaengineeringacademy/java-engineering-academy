package academy.javaengineering.senior.java21;

import java.util.List;

public class SealedClassesDemo {

    // ==================== Sealed Interfaces ====================

    sealed interface Payment permits CreditCardPayment, PayPalPayment, BankTransfer {}
    record CreditCardPayment(String cardNumber, double amount) implements Payment {}
    record PayPalPayment(String email, double amount) implements Payment {}
    record BankTransfer(String accountNumber, double amount) implements Payment {}

    sealed interface Vehicle permits Car, Truck, Motorcycle {}
    record Car(String model, int seats) implements Vehicle {}
    record Truck(String model, double loadCapacity) implements Vehicle {}
    record Motorcycle(String model, boolean hasSidecar) implements Vehicle {}

    sealed interface Result<T> permits Success, Failure {}
    record Success<T>(T value) implements Result<T> {}
    record Failure<T>(String error) implements Result<T> {}

    // ==================== Permitted Subtypes ====================

    sealed interface State permits IdleState, RunningState, ErrorState, ShutdownState {}
    record IdleState() implements State {}
    record RunningState(String taskName, long startTime) implements State {}
    record ErrorState(String message, Exception cause) implements State {}
    record ShutdownState() implements State {}

    sealed interface Expression permits Literal, Addition, Multiplication {}
    record Literal(double value) implements Expression {}
    record Addition(Expression left, Expression right) implements Expression {}
    record Multiplication(Expression left, Expression right) implements Expression {}

    public static void main(String[] args) {
        sealedInterfacesDemo();
        permittedSubtypesDemo();
        patternMatchingWithSealed();
    }

    // ==================== Sealed Interfaces Demo ====================

    static void sealedInterfacesDemo() {
        System.out.println("=== Sealed Interfaces ===\n");

        List<Payment> payments = List.of(
            new CreditCardPayment("4111-1111-1111-1111", 100.0),
            new PayPalPayment("user@example.com", 250.0),
            new BankTransfer("DE89370400440532013000", 500.0)
        );

        for (Payment payment : payments) {
            String desc = switch (payment) {
                case CreditCardPayment cc -> "Credit Card: " + cc.cardNumber().substring(0, 4) + "****, $" + cc.amount();
                case PayPalPayment pp -> "PayPal: " + pp.email() + ", $" + pp.amount();
                case BankTransfer bt -> "Bank Transfer: " + bt.accountNumber().substring(0, 6) + "..., $" + bt.amount();
            };
            System.out.println("  " + desc);
        }

        System.out.println();

        Result<String> success = new Success<>("Data loaded");
        Result<String> failure = new Failure<>("Connection timeout");

        System.out.println("  Success: " + processResult(success));
        System.out.println("  Failure: " + processResult(failure));
    }

    static <T> String processResult(Result<T> result) {
        return switch (result) {
            case Success<T> s -> "OK: " + s.value();
            case Failure<T> f -> "Error: " + f.error();
        };
    }

    // ==================== Permitted Subtypes Demo ====================

    static void permittedSubtypesDemo() {
        System.out.println("\n=== Permitted Subtypes ===\n");

        State state = new RunningState("data-processing", System.currentTimeMillis());
        System.out.println("  Current state: " + stateToString(state));

        State[] states = {
            new IdleState(),
            new RunningState("backup", 1000L),
            new ErrorState("Disk full", new RuntimeException("No space")),
            new ShutdownState()
        };

        for (State s : states) {
            System.out.println("  " + stateToString(s));
        }

        Expression expr = new Addition(
            new Multiplication(new Literal(2), new Literal(3)),
            new Literal(4)
        );

        System.out.println("\n  Expression: " + exprToString(expr) + " = " + evaluate(expr));
    }

    static String stateToString(State state) {
        return switch (state) {
            case IdleState _ -> "Idle";
            case RunningState r -> "Running '" + r.taskName() + "' since " + r.startTime();
            case ErrorState e -> "Error: " + e.message();
            case ShutdownState _ -> "Shutdown";
        };
    }

    static String exprToString(Expression expr) {
        return switch (expr) {
            case Literal l -> String.valueOf(l.value());
            case Addition a -> "(" + exprToString(a.left()) + " + " + exprToString(a.right()) + ")";
            case Multiplication m -> "(" + exprToString(m.left()) + " * " + exprToString(m.right()) + ")";
        };
    }

    static double evaluate(Expression expr) {
        return switch (expr) {
            case Literal l -> l.value();
            case Addition a -> evaluate(a.left()) + evaluate(a.right());
            case Multiplication m -> evaluate(m.left()) * evaluate(m.right());
        };
    }

    // ==================== Pattern Matching with Sealed Types ====================

    static void patternMatchingWithSealed() {
        System.out.println("\n=== Pattern Matching with Sealed Types ===\n");

        record Engine(int horsepower, String type) {}
        sealed interface Drivable permits Car, Truck, Motorcycle {}

        Vehicle car = new Car("Tesla Model 3", 5);
        Vehicle truck = new Truck("Ford F-150", 1500.0);
        Vehicle bike = new Motorcycle("Harley", true);

        Vehicle[] vehicles = {car, truck, bike};

        for (Vehicle v : vehicles) {
            String info = switch (v) {
                case Car(String model, int seats) && seats > 4 ->
                    "Family car: " + model + " (" + seats + " seats)";
                case Car(String model, int seats) ->
                    "Car: " + model + " (" + seats + " seats)";
                case Truck(String model, double capacity) && capacity > 1000 ->
                    "Heavy truck: " + model + " (capacity: " + capacity + " kg)";
                case Truck(String model, double capacity) ->
                    "Light truck: " + model;
                case Motorcycle(String model, boolean sidecar) && sidecar ->
                    "Motorcycle with sidecar: " + model;
                case Motorcycle(String model, _) ->
                    "Motorcycle: " + model;
            };
            System.out.println("  " + info);
        }

        System.out.println("\n--- Exhaustiveness with sealed types ---");

        for (Vehicle v : vehicles) {
            System.out.println("  " + categorize(v));
        }
    }

    static String categorize(Vehicle v) {
        return switch (v) {
            case Car _ -> "Passenger vehicle";
            case Truck _ -> "Commercial vehicle";
            case Motorcycle _ -> "Two-wheeler";
        };
    }
}
