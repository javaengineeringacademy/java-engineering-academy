package academy.javaengineering.reactive;

/**
 * Demonstrates reactive programming concepts.
 */
public class ReactiveConcepts {

    public static java.util.List<String> getReactivePrinciples() {
        return java.util.List.of(
            "Responsive - Respond in reasonable time",
            "Resilient - Stay responsive under failure",
            "Elastic - Stay responsive under load",
            "Message Driven - Use async messaging"
        );
    }

    public record ReactiveOperator(
        String name,
        String description,
        String useCase
    ) {}

    public static java.util.List<ReactiveOperator> getOperators() {
        return java.util.List.of(
            new ReactiveOperator("map", "Transform each element", "Data transformation"),
            new ReactiveOperator("flatMap", "Transform to reactive stream", "Async operations"),
            new ReactiveOperator("filter", "Filter elements", "Data filtering"),
            new ReactiveOperator("reduce", "Combine elements", "Aggregation")
        );
    }
}
