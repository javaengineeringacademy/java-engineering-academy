# Functional Programming Quiz

## Question 1 (MCQ)
What is a functional interface in Java?
- A) An interface with exactly one abstract method
- B) An interface with no methods
- C) An interface with multiple abstract methods
- D) An abstract class with one method

**Answer: A**
**Explanation:** A functional interface has exactly one abstract method (SAM - Single Abstract Method). It can have multiple default and static methods. The `@FunctionalInterface` annotation ensures this contract at compile time.

---

## Question 2 (MCQ)
What is the difference between `map()` and `flatMap()` in Stream API?
- A) They are identical
- B) `map()` transforms each element one-to-one; `flatMap()` transforms each element to a stream and flattens results
- C) `map()` is faster than `flatMap()`
- D) `flatMap()` is for primitive types only

**Answer: B**
**Explanation:** `map()` applies a function to each element producing one output element. `flatMap()` applies a function that returns a stream for each element, then flattens all streams into a single stream.

---

## Question 3 (MCQ)
What is lazy evaluation in Streams?
- A) Operations are not executed until a terminal operation is invoked
- B) Operations are executed immediately
- C) Operations are executed in parallel
- D) Operations are executed in reverse order

**Answer: A**
- **Explanation:** Intermediate operations (filter, map, etc.) are deferred. Processing only begins when a terminal operation (collect, forEach, etc.) is called, enabling optimizations like short-circuiting.

---

## Question 4 (MCQ)
What is the difference between `reduce()` and `collect()`?
- A) They are identical
- B) `reduce()` combines elements into a single value; `collect()` accumulates into a mutable container
- C) `reduce()` is for parallel streams only
- D) `collect()` is slower than `reduce()`

**Answer: B**
**Explanation:** `reduce()` uses a BinaryOperator to combine elements into one value (e.g., sum). `collect()` uses a Collector to accumulate elements into a mutable container like a List, Set, or Map.

---

## Question 5 (Code Output)
What does this code print?

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int sum = numbers.stream()
            .filter(n -> n % 2 == 0)
            .mapToInt(Integer::intValue)
            .sum();

        System.out.println(sum);
    }
}
```

**Answer:** 30
**Explanation:** The stream filters even numbers (2, 4, 6, 8, 10), maps them to int values, and sums them: 2 + 4 + 6 + 8 + 10 = 30.

---

## Question 6 (Code Output)
What does this code print?

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Map<String, Integer> wordLengths = Stream.of("apple", "banana", "cherry")
            .collect(Collectors.toMap(
                s -> s,
                String::length
            ));

        System.out.println(wordLengths);
    }
}
```

**Answer:** {banana=6, apple=5, cherry=6}
**Explanation:** The stream collects into a Map where keys are the strings and values are their lengths. The order may vary since HashMap is unordered.

---

## Question 7 (Bug Finding)
Find the bug:

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        Optional<String> result = names.stream()
            .filter(n -> n.length() > 3)
            .findFirst();

        result.ifPresent(n -> System.out.println(n.toUpperCase()));

        // Try to use the stream again
        long count = names.stream()
            .filter(n -> n.length() > 3)
            .count();

        System.out.println(count);
    }
}
```

**Bug:** There's no bug — the code correctly creates a new stream from the collection for each operation. However, a common mistake is trying to reuse a single stream instance. Streams are single-use and cannot be reused after a terminal operation.

---

## Question 8 (Bug Finding)
Find the bug:

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        int sum = numbers.parallelStream()
            .reduce(0, (a, b) -> a + b);

        System.out.println(sum);
    }
}
```

**Bug:** The code works correctly, but using `reduce(0, (a, b) -> a + b)` with parallel streams creates unnecessary boxing. A better approach uses `mapToInt().sum()` for primitive streams:
**Fix:**
```java
int sum = numbers.parallelStream()
    .mapToInt(Integer::intValue)
    .sum();
```

---

## Question 9 (Scenario-based)
You need to process a list of orders and group them by status (COMPLETED, PENDING, CANCELLED), then find the total revenue for COMPLETED orders. Which stream approach is most appropriate?

- A) Iterate with a for loop, use if-else for grouping
- B) Use `Collectors.groupingBy()` to group by status, then `filter` and `mapToDouble` for revenue
- C) Sort the list, then manually group
- D) Use parallel streams for all operations

**Answer: B**
**Explanation:** `Collectors.groupingBy()` provides a clean, declarative way to group elements. Combined with filtering and aggregation operations, this approach is concise, readable, and efficient.

---

## Question 10 (Architecture Decision)
You are designing a data transformation pipeline that needs to: (1) read CSV data, (2) validate records, (3) transform to domain objects, (4) aggregate statistics, and (5) write results. How should you design this?

- A) Process each step entirely before moving to the next
- B) Use a Stream pipeline with intermediate operations for validation and transformation, and terminal operations for aggregation
- C) Load all data into memory, process, then output
- D) Use separate threads for each step without coordination

**Answer: B**
**Explanation:** A Stream pipeline provides lazy evaluation, so each record flows through all steps before the next record is processed. This minimizes memory usage and allows the JVM to optimize the pipeline. Intermediate operations are fused for efficiency.

---

## Question 11 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        int[] counter = {0};
        List<String> names = List.of("Alice", "Bob", "Charlie");

        names.stream()
            .filter(n -> n.length() > 3)
            .peek(n -> counter[0]++)
            .collect(Collectors.toList());

        System.out.println(counter[0]);

        counter[0] = 0;
        List<String> result = names.stream()
            .filter(n -> n.length() > 3)
            .peek(n -> counter[0]++)
            .collect(Collectors.toList());

        System.out.println(counter[0]);
    }
}
```

A) 2 then 2
B) 0 then 2
C) 2 then 0
D) 0 then 0

**Answer: A**
**Explanation:** First pipeline: filter keeps "Alice" and "Charlie" (length > 3), peek increments counter twice, output 2. Second pipeline: same filter, peek increments counter from 0 to 2, output 2. Streams are re-created from the list each time. Note: the first pipeline's peek side-effect fires during collect. Output: `2` then `2`.

---

## Question 12 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Optional<String> empty = Optional.empty();
        Optional<String> present = Optional.of("Hello");

        String result1 = empty.orElse("Default");
        String result2 = present.orElseGet(() -> {
            System.out.print("Supplier called ");
            return "Computed";
        });

        System.out.println(result1);
        System.out.println(result2);
    }
}
```

A) Default Hello
B) Supplier called Default Hello
C) Default Computed
D) Hello Default

**Answer: A**
**Explanation:** `empty.orElse("Default")` returns "Default" since Optional is empty. `present.orElseGet(...)` returns "Hello" directly — the supplier is never invoked because the Optional already has a value. The key difference: `orElse` always evaluates its argument, while `orElseGet` only calls the supplier when empty. Output: `Default` then `Hello`.

---

## Question 13 (Code Snippet MCQ)
What is the output of this code?

```java
import java.util.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(Collectors.partitioningBy(n -> n % 2 == 0));

        System.out.println(partitioned.get(true));
        System.out.println(partitioned.get(false));
    }
}
```

A) [2, 4, 6] [1, 3, 5]
B) [1, 3, 5] [2, 4, 6]
C) {false=[1,3,5], true=[2,4,6]}
D) [1, 2, 3, 4, 5, 6]

**Answer: A**
**Explanation:** `partitioningBy` splits elements into two groups: `true` (even numbers) and `false` (odd numbers). `partitioned.get(true)` returns `[2, 4, 6]` and `partitioned.get(false)` returns `[1, 3, 5]`. The output is printed line by line. Output: `[2, 4, 6]` then `[1, 3, 5]`.

