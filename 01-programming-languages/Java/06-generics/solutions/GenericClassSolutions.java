package academy.javaengineering.generics.solutions;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.EmptyStackException;
import java.util.function.Function;

/**
 * Solutions for Generic Class Practice Exercises
 *
 * <p>Complexity: O(1) for most operations, O(n) for list operations</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Complete implementations of Box, Pair, Stack, Result, and Repository generic classes</p>
 */
public class GenericClassSolutions {

    // ============================================================
    // Exercise 1: Box Class Solution
    // ============================================================
    static class Box<T> {
        private T value;

        public Box(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Box[" + value + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Box<?> box = (Box<?>) obj;
            return Objects.equals(value, box.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    // ============================================================
    // Exercise 2: Pair Class Solution
    // ============================================================
    static class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }

        public Pair<S, F> swap() {
            return new Pair<>(second, first);
        }

        @Override
        public String toString() {
            return "(" + first + ", " + second + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) obj;
            return Objects.equals(first, pair.first) &&
                   Objects.equals(second, pair.second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

    // ============================================================
    // Exercise 3: Stack Class Solution
    // ============================================================
    static class Stack<T> {
        private final List<T> elements;

        public Stack() {
            elements = new ArrayList<>();
        }

        public void push(T item) {
            elements.add(item);
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return elements.remove(elements.size() - 1);
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return elements.get(elements.size() - 1);
        }

        public boolean isEmpty() {
            return elements.isEmpty();
        }

        public int size() {
            return elements.size();
        }

        public void clear() {
            elements.clear();
        }

        @Override
        public String toString() {
            return "Stack" + elements;
        }
    }

    // ============================================================
    // Exercise 4: Result Class Solution
    // ============================================================
    static class Result<T, E> {
        private final T value;
        private final E error;
        private final boolean success;

        private Result(T value, E error, boolean success) {
            this.value = value;
            this.error = error;
            this.success = success;
        }

        public static <T, E> Result<T, E> success(T value) {
            return new Result<>(value, null, true);
        }

        public static <T, E> Result<T, E> failure(E error) {
            return new Result<>(null, error, false);
        }

        public boolean isSuccess() {
            return success;
        }

        public boolean isFailure() {
            return !success;
        }

        public T getValue() {
            if (!success) {
                throw new IllegalStateException("No value in a failure result");
            }
            return value;
        }

        public E getError() {
            if (success) {
                throw new IllegalStateException("No error in a success result");
            }
            return error;
        }

        public <U> Result<U, E> map(Function<T, U> mapper) {
            if (success) {
                return Result.success(mapper.apply(value));
            }
            return Result.failure(error);
        }

        @Override
        public String toString() {
            if (success) {
                return "Result.success(" + value + ")";
            }
            return "Result.failure(" + error + ")";
        }
    }

    // ============================================================
    // Exercise 5: Repository Interface Solution
    // ============================================================
    interface Repository<T, ID> {
        T findById(ID id);
        List<T> findAll();
        T save(T entity);
        void delete(T entity);
        boolean existsById(ID id);
        long count();
    }

    // Concrete implementation for testing
    static class InMemoryRepository<T, ID> implements Repository<T, ID> {
        private final List<T> entities = new ArrayList<>();

        @Override
        public T findById(ID id) {
            // Simple implementation - in real code, would search by ID
            return entities.isEmpty() ? null : entities.get(0);
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(entities);
        }

        @Override
        public T save(T entity) {
            entities.add(entity);
            return entity;
        }

        @Override
        public void delete(T entity) {
            entities.remove(entity);
        }

        @Override
        public boolean existsById(ID id) {
            return !entities.isEmpty();
        }

        @Override
        public long count() {
            return entities.size();
        }
    }

    // ============================================================
    // Test all implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Box Class ===");
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        System.out.println(stringBox);
        System.out.println(intBox);
        stringBox.setValue("World");
        System.out.println("Updated: " + stringBox);

        System.out.println("\n=== Exercise 2: Pair Class ===");
        Pair<String, Integer> pair = new Pair<>("Age", 25);
        System.out.println(pair);
        Pair<Integer, String> swapped = pair.swap();
        System.out.println("Swapped: " + swapped);

        System.out.println("\n=== Exercise 3: Stack Class ===");
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("Top: " + stack.peek());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Size: " + stack.size());
        System.out.println("Stack: " + stack);

        System.out.println("\n=== Exercise 4: Result Class ===");
        Result<String, String> success = Result.success("It worked!");
        Result<String, String> failure = Result.failure("Something went wrong");
        System.out.println(success.isSuccess() + " - " + success.getValue());
        System.out.println(failure.isFailure() + " - " + failure.getError());
        Result<Integer, String> mapped = success.map(s -> s.length());
        System.out.println("Mapped: " + mapped);

        System.out.println("\n=== Exercise 5: Repository Interface ===");
        Repository<String, Long> repo = new InMemoryRepository<>();
        repo.save("Entity1");
        repo.save("Entity2");
        System.out.println("Count: " + repo.count());
        System.out.println("FindAll: " + repo.findAll());
        System.out.println("Exists: " + repo.existsById(1L));
    }
}
