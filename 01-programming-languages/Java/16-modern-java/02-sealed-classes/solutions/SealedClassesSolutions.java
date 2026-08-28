package academy.javaengineering.modern.sealed;

import java.util.List;

/**
 * Solutions for Sealed Classes practice exercises.
 */
public class SealedClassesSolutions {

    // Exercise 1: Payment Hierarchy
    public sealed interface Payment permits CreditCard, BankTransfer, PayPal, Cryptocurrency {}
    public record CreditCard(String cardNumber, String expiry) implements Payment {}
    public record BankTransfer(String accountNumber, String routingNumber) implements Payment {}
    public record PayPal(String email) implements Payment {}
    public record Cryptocurrency(String walletAddress, String currency) implements Payment {}

    public static String processPayment(Payment payment) {
        return switch (payment) {
            case CreditCard cc -> "Processing credit card: " + cc.cardNumber().substring(0, 4) + "****";
            case BankTransfer bt -> "Processing bank transfer to: " + bt.accountNumber();
            case PayPal pp -> "Processing PayPal payment to: " + pp.email();
            case Cryptocurrency cr -> "Processing " + cr.currency() + " payment to: " + cr.walletAddress();
        };
    }

    // Exercise 2: Result Type
    public sealed interface Result<T> permits Success, Failure {}
    public record Success<T>(T value) implements Result<T> {}
    public record Failure<T>(Exception error) implements Result<T> {}

    public static <T, R> Result<R> map(Result<T> result, java.util.function.Function<T, R> mapper) {
        return switch (result) {
            case Success<T> s -> new Success<>(mapper.apply(s.value()));
            case Failure<T> f -> new Failure<>(f.error());
        };
    }

    public static <T, R> Result<R> flatMap(Result<T> result, 
            java.util.function.Function<T, Result<R>> mapper) {
        return switch (result) {
            case Success<T> s -> mapper.apply(s.value());
            case Failure<T> f -> new Failure<>(f.error());
        };
    }

    public static <T> T orElse(Result<T> result, T defaultValue) {
        return switch (result) {
            case Success<T> s -> s.value();
            case Failure<T> f -> defaultValue;
        };
    }

    // Exercise 3: Expression AST
    public sealed interface Expression permits Number, Add, Multiply, Negate {}
    public record Number(double value) implements Expression {}
    public record Add(Expression left, Expression right) implements Expression {}
    public record Multiply(Expression left, Expression right) implements Expression {}
    public record Negate(Expression operand) implements Expression {}

    public static double eval(Expression expr) {
        return switch (expr) {
            case Number n -> n.value();
            case Add a -> eval(a.left()) + eval(a.right());
            case Multiply m -> eval(m.left()) * eval(m.right());
            case Negate n -> -eval(n.operand());
        };
    }

    // Exercise 4: Permission System
    public sealed interface Permission permits Read, Write, Execute, Admin {}
    public record Read(String resource) implements Permission {}
    public record Write(String resource) implements Permission {}
    public record Execute(String resource) implements Permission {}
    public record Admin(String resource) implements Permission {}

    public static boolean hasPermission(Permission required, List<Permission> userPermissions) {
        return userPermissions.stream().anyMatch(p -> 
            p.getClass().equals(required.getClass()) &&
            ((Object) p).equals(required)
        );
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Payment Processing ---");
        List<Payment> payments = List.of(
            new CreditCard("4111111111111111", "12/25"),
            new BankTransfer("123456789", "021000021"),
            new PayPal("user@example.com"),
            new Cryptocurrency("0x1234567890abcdef", "ETH")
        );
        payments.forEach(p -> System.out.println(processPayment(p)));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Result Type ---");
        Result<Integer> success = new Success<>(10);
        Result<Integer> failure = new Failure<>(new RuntimeException("Error"));

        Result<String> mapped = map(success, i -> "Number: " + i);
        System.out.println("Mapped success: " + orElse(mapped, "default"));

        Result<Integer> flatMapped = flatMap(success, i -> 
            i > 0 ? new Success<>(i * 2) : new Failure<>(new RuntimeException("Negative")));
        System.out.println("FlatMapped: " + orElse(flatMapped, 0));

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Expression AST ---");
        Expression expr = new Add(
            new Multiply(new Number(2), new Number(3)),
            new Negate(new Number(4))
        );
        System.out.println("2 * 3 + (-4) = " + eval(expr));

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Permission System ---");
        List<Permission> userPermissions = List.of(
            new Read("file.txt"),
            new Write("file.txt")
        );
        System.out.println("Has Read: " + hasPermission(new Read("file.txt"), userPermissions));
        System.out.println("Has Execute: " + hasPermission(new Execute("file.txt"), userPermissions));
    }
}
