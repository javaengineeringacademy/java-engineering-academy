# Generics Quiz

## Question 1 (Production Scenario)
Your data processing framework needs to accept lists of any Comparable type (Integer, String, CustomEntity) and find the maximum element. A developer writes `<T>` without bounds and gets a compilation error when calling `compareTo()`. What should you do?

- A) Use `Object` as the type parameter and cast to `Comparable`
- B) Use `<T extends Comparable<T>>` to ensure T implements Comparable
- C) Create separate methods for each type
- D) Use raw types without generics

**Answer: B**
**Explanation:** `<T extends Comparable<T>>` is a bounded type parameter that restricts T to types implementing `Comparable`. This allows calling `compareTo()` directly while maintaining compile-time type safety. Without the bound, the compiler cannot verify that T has `compareTo()`, requiring unsafe casts.

---

## Question 2 (Production Scenario)
You are building a generic utility method that copies elements from a source list to a destination list. The source might be `List<Integer>` and the destination `List<Number>`. Which generic signature is correct?

- A) `void copy(List<Object> dest, List<Object> src)`
- B) `void copy(List<? super T> dest, List<? extends T> src)`
- C) `void copy(List dest, List src)`
- D) `void copy(List<Number> dest, List<Integer> src)`

**Answer: B**
**Explanation:** PECS (Producer Extends, Consumer Super): `? super T` for the destination (consumer that receives elements), `? extends T` for the source (producer that provides elements). This allows `List<Object>` to accept `Integer` elements, and `List<Integer>` to provide `Number` elements, while maintaining type safety.

---

## Question 3 (Debugging)
A generic repository class throws `ClassCastException` at runtime despite compile-time type safety. The code:

```java
public class Repository<T> {
    private List<Object> items = new ArrayList<>();
    
    public void add(T item) { items.add(item); }
    public T get(int index) { return (T) items.get(index); }
}

Repository<String> repo = new Repository<>();
repo.add("Hello");
repo.add(42);  // Compiled without error!
String s = repo.get(1);  // ClassCastException!
```

What is the bug?

- A) The `get()` method should use `items.get(index)` directly
- B) Type erasure removes the generic type at runtime, so the unchecked cast `(T)` fails
- C) The `add()` method should accept `Object` instead of `T`
- D) `Repository` should implement `Iterable`

**Answer: B**
**Explanation:** Due to type erasure, `Repository<String>` and `Repository<Integer>` are the same class at runtime. The `add(T)` method accepts any object because T is erased to Object. The cast `(T)` is unchecked and fails when retrieving an Integer as String. Fix: use `List<T>` instead of `List<Object>`.

---

## Question 4 (Production Scenario)
You need to design a generic repository pattern for a data access layer. The repository must support CRUD operations for any entity type and should be type-safe. How should you design it?

- A) Create a separate repository class for each entity type
- B) Define `interface Repository<T, ID>` with methods like `findById(ID id)`, `save(T entity)`, `delete(T entity)`
- C) Use Object for all entity types and cast as needed
- D) Use a single Repository class with string-based entity names

**Answer: B**
**Explanation:** A generic Repository interface provides type-safe CRUD operations for any entity. Each entity gets its own implementation or uses a single implementation with type parameters. This reduces code duplication while maintaining compile-time type safety.

---

## Question 5 (Code Output)
What does this code print?

```java
import java.util.*;

public class Main {
    public static <T extends Comparable<T>> T findMax(List<T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
        System.out.println(findMax(numbers));
    }
}
```

**Answer:** 9
**Explanation:** The method uses a bounded type parameter `<T extends Comparable<T>>` ensuring T implements Comparable. It iterates through the list comparing elements, finding the maximum value 9.

---

## Question 6 (Code Output)
What does this code print?

```java
import java.util.*;

public class Main {
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.print(item + " ");
        }
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("A", "B", "C");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        printList(strings);
        System.out.println();
        printList(integers);
    }
}
```

**Answer:** A B C and 1 2 3
**Explanation:** The unbounded wildcard `List<?>` accepts lists of any type. The method treats elements as Objects, which all types extend. This provides type-safe read-only access to heterogeneous list types.

---

## Question 7 (Bug Finding)
Find the bug:

```java
public class Box<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
        box.setValue(42);
        Object val = box.getValue();
        Integer num = (Integer) val;
        System.out.println(num + 1);
    }
}
```

**Bug:** The code actually works correctly, but the explicit cast `(Integer) val` is redundant and indicates a misunderstanding. Due to type erasure, the cast happens implicitly. However, there's a subtle issue: if someone changes the code to use raw types later, the cast will be needed.
**Fix:** Remove the unnecessary cast:
```java
Integer num = box.getValue();
```

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.util.*;

public class Main {
    public static void addNumber(List<? extends Number> list, Number num) {
        list.add(num);
    }

    public static void main(String> args) {
        List<Integer> integers = new ArrayList<>();
        addNumber(integers, 42);
    }
}
```

**Bug:** You cannot add elements to a collection with an upper-bounded wildcard (`? extends Number`). The compiler doesn't know the exact type, so it prevents adding to maintain type safety. Also, there's a syntax error: `List<Integer>` should be `List<Integer>`.
**Fix:** Change the method signature to use a lower bound for writing:
```java
public static void addNumber(List<? super Integer> list, Integer num) {
    list.add(num);
}
```

---

## Question 9 (Scenario-based)
You're building a data processing framework that accepts lists of any Comparable type and sorts them. Which generic approach is best?

- A) Use `List<Object>` and cast elements to Comparable
- B) Use `List<? extends Comparable>` to accept any list of Comparable types
- C) Create separate methods for each type
- D) Use raw types without generics

**Answer: B**
**Explanation:** `List<? extends Comparable>` (or more precisely `List<? extends Comparable<?>>`) accepts any list of Comparable types while maintaining type safety. This uses generics for reusability without losing compile-time checks.

---

## Question 10 (Architecture Decision)
You need to design a generic repository pattern for a data access layer. The repository must support CRUD operations for any entity type and should be type-safe. How should you design it?

- A) Create a separate repository class for each entity type
- B) Define `interface Repository<T, ID>` with methods like `findById(ID id)`, `save(T entity)`, `delete(T entity)`
- C) Use Object for all entity types and cast as needed
- D) Use a single Repository class with string-based entity names

**Answer: B**
**Explanation:** A generic Repository interface provides type-safe CRUD operations for any entity. Each entity gets its own implementation or uses a single implementation with type parameters. This reduces code duplication while maintaining compile-time type safety.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;

public class Main {
    static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static void main(String[] args) {
        List<Object> dest = new ArrayList<>();
        List<Integer> src = Arrays.asList(1, 2, 3);
        copy(dest, src);
        System.out.println(dest);
    }
}
```

A) [1, 2, 3]
B) Compilation error
C) ClassCastException
D) [Object, Object, Object]

**Answer: A**
**Explanation:** This demonstrates wildcard capture. `copy(dest, src)` infers T as Integer (the most specific type that satisfies both bounds). `List<Object>` accepts `? super Integer`, and `List<Integer>` satisfies `? extends Integer`. Elements are copied successfully. Output: `[1, 2, 3]`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.0, 2.0, 3.0);

        System.out.println(integers.getClass() == doubles.getClass());
        System.out.println(integers.getClass().getName());
    }
}
```

A) true java.util.Arrays$ArrayList
B) false java.util.Arrays$ArrayList
C) true java.util.ArrayList
D) false java.util.List

**Answer: A**
**Explanation:** Due to type erasure, `List<Integer>` and `List<Double>` have the same runtime type (`java.util.Arrays$ArrayList`). Generic type parameters are erased at compile time, so both lists are the same class at runtime. `getClass()` returns the actual runtime class.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
public class Main {
    static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static void main(String[] args) {
        String result = max("Apple", "Banana");
        System.out.println(result);
    }
}
```

A) Apple
B) Banana
C) Compilation error
D) null

**Answer: B**
**Explanation:** Type inference deduces T as String (since both arguments are Strings, and String implements Comparable). `"Apple".compareTo("Banana")` returns a negative value (A < B), so the ternary returns "Banana" (the second argument). Output: `Banana`.

