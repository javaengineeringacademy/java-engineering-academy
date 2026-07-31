package academy.javaengineering.oop.interfaces;

/**
 * Predicate - Functional interface for boolean evaluation.
 * 
 * @param <T> the type of input to the predicate
 * @author Java Engineering Academy
 * @version 1.0
 */
@FunctionalInterface
public interface Predicate<T> {

    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        return t -> test(t) && other.test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        return t -> test(t) || other.test(t);
    }

    default Predicate<T> negate() {
        return t -> !test(t);
    }

    static <T> Predicate<T> alwaysTrue() {
        return t -> true;
    }

    static <T> Predicate<T> alwaysFalse() {
        return t -> false;
    }
}