# Quiz: Sealed Hierarchy

## Multiple Choice Questions

1. What is a sealed class hierarchy?
   - A) A class hierarchy where a sealed class restricts which classes can extend it
   - B) A hierarchy with only abstract classes
   - C) A hierarchy using only interfaces
   - D) A hierarchy with no inheritance

2. What are the permitted modifiers for subclasses of a sealed class?
   - A) final, sealed, non-sealed
   - B) public, private, protected
   - C) abstract, concrete, static
   - D) Any modifier

3. What does a `non-sealed` subclass allow?
   - A) Anyone can extend it further
   - B) No further extension
   - C) Only sealed extension
   - D) Only final extension

4. Can a sealed class hierarchy cross module boundaries?
   - A) Yes, always
   - B) Only with explicit permissions
   - C) Only if in the same package
   - D) No, never

5. What pattern does sealed hierarchy enable in switch expressions?
   - A) Pattern matching with exhaustive checks
   - B) Dynamic dispatch
   - C) Static binding
   - D) Late binding

## True/False Questions

6. A sealed class can permit interfaces as subtypes.
   - True / False

7. A non-sealed class can be extended by any class without restriction.
   - True / False

8. Sealed hierarchies make the compiler able to check exhaustiveness in switch.
   - True / False

## Code Output Questions

9. What will this code print?
```java
sealed interface Result permits Success, Failure {}
record Success(String data) implements Result {}
record Failure(String error) implements Result {}

class Test {
    static String process(Result r) {
        return switch (r) {
            case Success s -> "OK: " + s.data();
            case Failure f -> "Error: " + f.error();
        };
    }
    public static void main(String[] args) {
        System.out.println(process(new Success("data123")));
        System.out.println(process(new Failure("timeout")));
    }
}
```

10. What will this code print?
```java
sealed class Vehicle permits Car, Truck {}
final class Car extends Vehicle { String type() { return "Car"; } }
non-sealed class Truck extends Vehicle { String type() { return "Truck"; } }

class Test {
    static String describe(Vehicle v) {
        return switch (v) {
            case Car c -> "I am a " + c.type();
            case Truck t -> "I am a " + t.type();
        };
    }
    public static void main(String[] args) {
        System.out.println(describe(new Car()));
        System.out.println(describe(new Truck()));
    }
}
```

## Answers

1. A
2. A
3. A
4. B - Permitted subtypes must be in the same module (or same package in unnamed module)
5. A
6. True
7. True - non-sealed breaks the restriction
8. True
9. Output:
```
OK: data123
Error: timeout
```
10. Output:
```
I am a Car
I am a Truck
```
