# Quiz: Method References

## Multiple Choice Questions

1. What is a method reference?
   - A) Pointer to a method
   - B) Method name
   - C) Method call
   - D) Method definition

2. Which symbol is used for method references?
   - A) `->`
   - B) `::`
   - C) `=>`
   - D) `##`

3. What are the types of method references?
   - A) Static, instance, constructor
   - B) Public, private, protected
   - C) Abstract, concrete, default
   - D) Final, non-final, static

4. When should you use method references?
   - A) Always
   - B) When lambda is simple
   - C) Never
   - D) Only for strings

5. What is the equivalent of `x -> System.out.println(x)`?
   - A) `System.out::println`
   - B) `println::System.out`
   - C) `System.out.println::`
   - D) `::System.out.println`

## True/False Questions

6. Method references are more readable than lambdas.
   - True / False

7. Method references can only reference static methods.
   - True / False

8. `String::new` is a constructor reference.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<String> list = List.of("a", "b", "c");
list.forEach(System.out::println);
```

10. What will this code print?
```java
Function<String, Integer> length = String::length;
System.out.println(length.apply("Java"));
```

## Answers

1. A - Method reference is a pointer to a method
2. B - Method references use ::
3. A - Static, instance, and constructor references
4. B - Use when lambda is simple
5. A - System.out::println is equivalent
6. True - Method references are more concise
7. False - Method references can reference instance methods
8. True - String::new is a constructor reference
9. Output:
```
a
b
c
```
10. Output:
```
4
```
