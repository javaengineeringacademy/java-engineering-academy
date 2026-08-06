@FunctionalInterface
public interface MyFunction<T, R> {

    R apply(T input);

    default <V> MyFunction<T, V> andThen(MyFunction<R, V> after) {
        return input -> after.apply(this.apply(input));
    }

    default <V> MyFunction<V, R> compose(MyFunction<V, T> before) {
        return input -> this.apply(before.apply(input));
    }

    static <T> MyFunction<T, T> identity() {
        return input -> input;
    }
}