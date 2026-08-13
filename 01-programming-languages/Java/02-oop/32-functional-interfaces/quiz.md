# Quiz: Functional Interfaces

## Multiple Choice Questions

1. What is a functional interface?
   - A) An interface with exactly one abstract method
   - B) An interface with no methods
   - C) An interface with only default methods
   - D) An interface with static methods

2. Which annotation is used to mark a functional interface?
   - A) @Override
   - B) @FunctionalInterface
   - C) @Interface
   - D) @Abstract

3. Which of these is a built-in functional interface?
   - A) Runnable
   - B) Comparable
   - C) Iterable
   - D) All of the above (that have single abstract method)

4. What does a Function<T, R> functional interface represent?
   - A) Takes no arguments, returns a value
   - B) Takes one argument T, returns a value R
   - C) Takes two arguments
   - D) Takes an argument, returns void

5. What is the main benefit of functional interfaces?
   - A) Better memory usage
   - B) Enable lambda expressions and method references
   - C) Improved compilation speed
   - D) Stronger typing for collections

## True/False Questions

6. A functional interface can have multiple default methods.
   - True / False

7. @FunctionalInterface annotation is mandatory for a functional interface.
   - True / False

8. Lambda expressions can only be used with functional interfaces.
   - True / False

## Code Output Questions

9. What will this code print?
```java
@FunctionalInterface
interface Converter {
    String convert(int n);
}
class Test {
    public static void main(String[] args) {
        Converter c = n -> "Number: " + n;
        System.out.println(c.convert(42));
        Converter c2 = n -> String.valueOf(n * 2);
        System.out.println(c2.convert(21));
    }
}
```

10. What will this code print?
```java
import java.util.function.*;
class Test {
    public static void main(String[] args) {
        Function<String, Integer> len = String::length;
        Predicate<String> empty = String::isEmpty;
        Consumer<String> print = System.out::println;

        String word = "Hello";
        System.out.println(len.apply(word));
        System.out.println(empty.test(word));
        System.out.println(empty.test(""));
    }
}
```

## Answers

1. A
2. B
3. D - All have exactly one abstract method
4. B
5. B
6. True
7. False - It's optional but recommended as a compile-time check
8. True - They require a target type that is a functional interface
9. Output:
```
Number: 42
Number: 42
```
10. Output:
```
5
false
true
```
