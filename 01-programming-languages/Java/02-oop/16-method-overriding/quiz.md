# Quiz: Method Overriding

## Multiple Choice Questions

1. What is method overriding?
   - A) Redefining a parent class method in a subclass with the same signature
   - B) Defining a new method with the same name in a subclass
   - C) Calling a parent class method from a subclass
   - D) Defining a method with a different name in a subclass

2. Which annotation is used to indicate method overriding?
   - A) @Overload
   - B) @Override
   - C) @Super
   - D) @Parent

3. Which of the following CAN be overridden?
   - A) Private methods
   - B) Static methods
   - C) Protected or public non-static methods
   - D) Final methods

4. What happens if a subclass overrides a method and the original throws a checked exception?
   - A) Subclass must throw the same exception or a subclass of it
   - B) Subclass can throw any exception
   - C) Subclass cannot throw any exception
   - D) Subclass must throw a superclass of the original exception

5. Can a subclass override a method and narrow the access modifier?
   - A) Yes, any modifier is allowed
   - B) No, access cannot be restricted
   - C) Yes, but only from private to public
   - D) Only if the parent method is public

## True/False Questions

6. Overriding is resolved at compile time.
   - True / False

7. A subclass can have a method with the same name as a parent method but different parameters — this is overriding.
   - True / False

8. The @Override annotation causes a compilation error if the method doesn't actually override anything.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Animal {
    void speak() { System.out.println("Animal speaks"); }
}
class Dog extends Animal {
    @Override
    void speak() { System.out.println("Dog barks"); }
}
class Main {
    public static void main(String[] args) {
        Animal a = new Dog();
        a.speak();
        Dog d = new Dog();
        d.speak();
    }
}
```

10. What will this code print?
```java
class Parent {
    String greet() { return "Hello from Parent"; }
}
class Child extends Parent {
    @Override
    String greet() { return "Hello from Child"; }
}
class Test {
    public static void main(String[] args) {
        Parent p = new Child();
        System.out.println(p.greet());
        System.out.println(new Parent().greet());
    }
}
```

## Answers

1. A
2. B
3. C - Private, static, and final methods cannot be overridden
4. A - Cannot throw broader checked exceptions
5. B - Access modifier must be same or more permissive
6. False - Overriding is resolved at runtime via dynamic binding
7. False - That is overloading, not overriding
8. True - Acts as a compile-time check
9. Output:
```
Dog barks
Dog barks
```
10. Output:
```
Hello from Child
Hello from Parent
```
