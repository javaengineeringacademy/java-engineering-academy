package academy.javaengineering.generics.practices;

/**
 * Generic Class Practice Exercises
 *
 * <p>Complexity: Varies by exercise</p>
 * <p>Thread-safety: Not thread-safe</p>
 * <p>Key characteristics: Practice exercises for implementing Box, Pair, Stack, Result, and Repository generic classes</p>
 */
public class GenericClassPractice {

    // ============================================================
    // Exercise 1: Box Class
    // ============================================================
    // Create a generic Box class that:
    // - Has a private field of type T to store a value
    // - Has a constructor that accepts a value of type T
    // - Has getter and setter methods for the value
    // - Has a toString method that returns the string representation of the value
    // - Has an equals method that compares values of the same type

    // TODO: Implement the Box<T> class here


    // ============================================================
    // Exercise 2: Pair Class
    // ============================================================
    // Create a generic Pair class with two type parameters:
    // - Has private fields first (type F) and second (type S)
    // - Has a constructor that accepts both values
    // - Has getters for both fields
    // - Has a toString method that returns "(first, second)"
    // - Has a swap() method that returns a new Pair with swapped values

    // TODO: Implement the Pair<F, S> class here


    // ============================================================
    // Exercise 3: Stack Class
    // ============================================================
    // Create a generic Stack class that:
    // - Uses an ArrayList internally to store elements
    // - Has a push(T item) method to add elements
    // - Has a pop() method that removes and returns the top element
    //   (throw EmptyStackException if empty)
    // - Has a peek() method that returns the top element without removing
    // - Has an isEmpty() method
    // - Has a size() method
    // - Has a clear() method

    // TODO: Implement the Stack<T> class here


    // ============================================================
    // Exercise 4: Result Class
    // ============================================================
    // Create a generic Result class that represents either success or failure:
    // - Has two type parameters: T (success value) and E (error value)
    // - Has a private field to store either a success value or an error
    // - Has a private boolean field to indicate success or failure
    // - Has a static factory method success(T value)
    // - Has a static factory method failure(E error)
    // - Has isSuccess() and isFailure() methods
    // - Has getValue() and getError() methods (throw IllegalStateException if wrong type accessed)
    // - Has a map() method that transforms the success value: Result<U, E> map(Function<T, U> mapper)

    // TODO: Implement the Result<T, E> class here


    // ============================================================
    // Exercise 5: Repository Interface
    // ============================================================
    // Create a generic Repository interface for data access:
    // - Type parameter T for the entity type
    // - Type parameter ID for the identifier type
    // - Methods to implement:
    //   - T findById(ID id)
    //   - List<T> findAll()
    //   - T save(T entity)
    //   - void delete(T entity)
    //   - boolean existsById(ID id)
    //   - long count()

    // TODO: Implement the Repository<T, ID> interface here


    // ============================================================
    // Test your implementations
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Exercise 1: Box Class ===");
        // TODO: Uncomment and test your Box class
        // Box<String> stringBox = new Box<>("Hello");
        // Box<Integer> intBox = new Box<>(42);
        // System.out.println(stringBox);
        // System.out.println(intBox);
        // stringBox.setValue("World");
        // System.out.println(stringBox);

        System.out.println("\n=== Exercise 2: Pair Class ===");
        // TODO: Uncomment and test your Pair class
        // Pair<String, Integer> pair = new Pair<>("Age", 25);
        // System.out.println(pair);
        // Pair<Integer, String> swapped = pair.swap();
        // System.out.println(swapped);

        System.out.println("\n=== Exercise 3: Stack Class ===");
        // TODO: Uncomment and test your Stack class
        // Stack<Integer> stack = new Stack<>();
        // stack.push(1);
        // stack.push(2);
        // stack.push(3);
        // System.out.println("Top: " + stack.peek());
        // System.out.println("Pop: " + stack.pop());
        // System.out.println("Size: " + stack.size());

        System.out.println("\n=== Exercise 4: Result Class ===");
        // TODO: Uncomment and test your Result class
        // Result<String, String> success = Result.success("It worked!");
        // Result<String, String> failure = Result.failure("Something went wrong");
        // System.out.println(success.isSuccess() + " - " + success.getValue());
        // System.out.println(failure.isFailure() + " - " + failure.getError());

        System.out.println("\n=== Exercise 5: Repository Interface ===");
        // TODO: Uncomment and test your Repository interface
        // (You would need to create a concrete implementation first)
    }
}
