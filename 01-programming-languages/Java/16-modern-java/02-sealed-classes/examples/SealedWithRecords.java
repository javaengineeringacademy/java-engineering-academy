package academy.javaengineering.modern.sealed;

import java.util.List;

/**
 * Sealed classes with records for algebraic data types.
 */
public class SealedWithRecords {

    // Result type
    public sealed interface Result<T> permits Success, Failure {}
    public record Success<T>(T value) implements Result<T> {}
    public record Failure<T>(Exception error) implements Result<T> {}

    // Optional type
    public sealed interface Optional<T> permits Present, Empty {}
    public record Present<T>(T value) implements Optional<T> {}
    public record Empty<T>() implements Optional<T> {}

    // Either type
    public sealed interface Either<L, R> permits Left, Right {}
    public record Left<L, R>(L value) implements Either<L, R> {}
    public record Right<L, R>(R value) implements Either<L, R> {}

    public static void main(String[] args) {
        // Result example
        Result<Integer> success = new Success<>(42);
        Result<Integer> failure = new Failure<>(new RuntimeException("Error"));

        System.out.println("Result examples:");
        System.out.println("Success: " + describeResult(success));
        System.out.println("Failure: " + describeResult(failure));

        // Optional example
        Optional<String> present = new Present<>("Hello");
        Optional<String> empty = new Empty<>();

        System.out.println("\nOptional examples:");
        System.out.println("Present: " + describeOptional(present));
        System.out.println("Empty: " + describeOptional(empty));

        // Either example
        Either<String, Integer> left = new Left<>("Error");
        Either<String, Integer> right = new Right<>(42);

        System.out.println("\nEither examples:");
        System.out.println("Left: " + describeEither(left));
        System.out.println("Right: " + describeEither(right));

        // Process results
        List<Result<Integer>> results = List.of(
            new Success<>(10),
            new Failure<>(new RuntimeException("First error")),
            new Success<>(20),
            new Failure<>(new RuntimeException("Second error"))
        );

        System.out.println("\nProcessing results:");
        results.forEach(r -> System.out.println("  " + processResult(r)));
    }

    static <T> String describeResult(Result<T> result) {
        return switch (result) {
            case Success<T> s -> "Success: " + s.value();
            case Failure<T> f -> "Failure: " + f.error().getMessage();
        };
    }

    static <T> String describeOptional(Optional<T> optional) {
        return switch (optional) {
            case Present<T> p -> "Present: " + p.value();
            case Empty<T> e -> "Empty";
        };
    }

    static <L, R> String describeEither(Either<L, R> either) {
        return switch (either) {
            case Left<L, R> l -> "Left: " + l.value();
            case Right<L, R> r -> "Right: " + r.value();
        };
    }

    static <T> String processResult(Result<T> result) {
        return switch (result) {
            case Success<T> s -> "Processed: " + s.value();
            case Failure<T> f -> "Handled: " + f.error().getMessage();
        };
    }
}
