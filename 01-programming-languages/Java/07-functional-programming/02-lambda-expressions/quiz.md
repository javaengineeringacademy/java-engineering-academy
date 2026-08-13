# Quiz: Lambda Expressions

## Multiple Choice Questions

1. What is the syntax of a lambda expression?
   - A) `(parameters) -> expression`
   - B) `function(parameters) { }`
   - C) `lambda parameters: expression`
   - D) `def lambda(parameters): expression`

2. Can a lambda have multiple statements?
   - A) Yes, always
   - B) No, never
   - C) Yes, with curly braces
   - D) Only with return statement

3. What is the type of a lambda expression?
   - A) Function
   - B) Functional interface
   - C) Abstract class
   - D) Interface

4. What does `->` mean in a lambda?
   - A) Arrow operator
   - B) Lambda operator
   - C) Flow operator
   - D) Assignment operator

5. Which functional interface is for no-arg functions?
   - A) Function<T,R>
   - B) Consumer<T>
   - C) Supplier<T>
   - D) Predicate<T>

## True/False Questions

6. Lambdas can access local variables if they are final.
   - True / False

7. Lambdas can have their own instance variables.
   - True / False

8. Lambdas can throw checked exceptions.
   - True / False

## Code Output Questions

9. What will this code print?
```java
List<String> list = List.of("a", "b", "c");
list.forEach(s -> System.out.print(s + " "));
```

10. What will this code print?
```java
Function<String, Integer> length = String::length;
System.out.println(length.apply("Hello"));
```

## Answers

1. A - Lambda syntax is (parameters) -> expression
2. C - Multiple statements require curly braces
3. B - Lambda type is a functional interface
4. B - -> is the lambda operator
5. C - Supplier<T> is for no-arg functions
6. True - Lambdas can access final local variables
7. False - Lambdas cannot have their own instance variables
8. False - Lambdas cannot throw checked exceptions (unless wrapped)
9. Output:
```
a b c
```
10. Output:
```
5
```
