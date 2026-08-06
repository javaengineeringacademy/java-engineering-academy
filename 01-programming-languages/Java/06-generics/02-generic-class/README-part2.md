# 02 - Generic Classes (Part 2)

[📖 Back to Part 1](README.md)
 | [📖 Continue to Part 3](README-part3.md)

---

    public int compareTo(ClassName<T> other) {
        return this.value.compareTo(other.value);
    }
}

// Multiple bounds
public class ClassName<T extends Number & Comparable<T>> {
    private T value;
    
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}
```

### Generic Interface Implementation

```java
// Generic interface
public interface Pair<A, B> {
    A getFirst();
    B getSecond();
}

// Concrete implementation
public class ImmutablePair<A, B> implements Pair<A, B> {
    private final A first;
    private final B second;
    
    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public A getFirst() { return first; }
    
    @Override
    public B getSecond() { return second; }
}

// Specialized implementation
public class StringIntegerPair implements Pair<String, Integer> {
    // Type parameters fixed to String and Integer
}
```

---

## Easy Example

### Generic Box

```java
public class Box<T> {
    private T content;

    public Box() {
    }

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Box[" + content + "]";
    }

    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        
        System.out.println(stringBox);  // Box[Hello]
        System.out.println(intBox);     // Box[42]
        
        stringBox.setContent("World");
        System.out.println(stringBox);  // Box[World]
    }
}
```

---

## Medium Example

### Generic Pair with Comparison

```java
public class Pair<A extends Comparable<A>, B extends Comparable<B>> 
        implements Comparable<Pair<A, B>> {
    
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }

    @Override
    public int compareTo(Pair<A, B> other) {
        int firstCompare = this.first.compareTo(other.first);
        if (firstCompare != 0) {
            return firstCompare;
        }
        return this.second.compareTo(other.second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair<?, ?>)) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return first.equals(other.first) && second.equals(other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public static <A extends Comparable<A>, B extends Comparable<B>> 
            Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public static void main(String[] args) {
        Pair<String, Integer> alice = Pair.of("Alice", 30);
        Pair<String, Integer> bob = Pair.of("Bob", 25);
        
        System.out.println(alice);  // (Alice, 30)
        System.out.println(alice.compareTo(bob));  // negative (Alice < Bob)
    }
}
```

---

## Hard Example

### Generic Binary Tree

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BinaryTree<T extends Comparable<T>> {
    
    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node<T> root;
    private int size;

    public BinaryTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        root = insertRecursive(root, value);
        size++;
    }

    private Node<T> insertRecursive(Node<T> current, T value) {
        if (current == null) {
            return new Node<>(value);
        }

        int compare = value.compareTo(current.data);
        if (compare < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (compare > 0) {
            current.right = insertRecursive(current.right, value);
        }
        // Duplicate values are ignored

        return current;
    }

    public boolean contains(T value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(Node<T> current, T value) {
        if (current == null) {
            return false;
        }

        int compare = value.compareTo(current.data);
        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return containsRecursive(current.left, value);
        } else {
            return containsRecursive(current.right, value);
        }
    }

    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderRecursive(node.left, result);
            result.add(node.data);
            inOrderRecursive(node.right, result);
        }
    }

    public T findMin() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        return findMinRecursive(root);
    }

    private T findMinRecursive(Node<T> node) {
        if (node.left == null) {
            return node.data;
        }
        return findMinRecursive(node.left);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.println("In-order: " + tree.inOrderTraversal());
        // [20, 30, 40, 50, 60, 70, 80]

        System.out.println("Contains 40: " + tree.contains(40));  // true
        System.out.println("Contains 25: " + tree.contains(25));  // false
        System.out.println("Min: " + tree.findMin());              // 20

        // Type safety enforced
        // BinaryTree<String> stringTree = new BinaryTree<>();
        // stringTree.insert(42);  // Compile error!
    }
}
```

---

## Enterprise Example

### Generic Result Type with Error Handling

```java
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Result<T> {
    
    private final T value;
    private final String error;
    private final boolean success;

    private Result(T value, String error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> success(T value) {
        Objects.requireNonNull(value, "Success value cannot be null");
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String error) {
        Objects.requireNonNull(error, "Error message cannot be null");
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
            throw new IllegalStateException("Cannot get value from failure: " + error);
        }
        return value;
    }

    public String getError() {
        if (success) {
            throw new IllegalStateException("Cannot get error from success");
        }
        return error;
    }

    public T orElse(T defaultValue) {
        return success ? value : defaultValue;
    }

    public T orElseGet(Supplier<T> supplier) {
        return success ? value : supplier.get();
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        if (success) {
            try {
                return Result.success(mapper.apply(value));
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        } else {
            return Result.failure(error);
        }
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        if (success) {
            try {
                return mapper.apply(value);
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        } else {
            return Result.failure(error);
        }
    }

    @Override
    public String toString() {
        return success ? "Success[" + value + "]" : "Failure[" + error + "]";
    }

    public static void main(String[] args) {
        Result<Integer> success = Result.success(42);
        Result<Integer> failure = Result.failure("Something went wrong");

        System.out.println(success);  // Success[42]
        System.out.println(failure);  // Failure[Something went wrong]

        // Chaining operations
        Result<String> mapped = success.map(i -> "Number: " + i);
        System.out.println(mapped);  // Success[Number: 42]

        // Error propagation
        Result<String> chained = failure.map(i -> "Number: " + i);
        System.out.println(chained);  // Failure[Something went wrong]

        // Safe value access
        int value1 = success.orElse(0);  // 42
        int value2 = failure.orElse(0);  // 0
    }
