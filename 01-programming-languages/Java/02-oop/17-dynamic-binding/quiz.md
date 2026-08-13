# Quiz: Dynamic Binding

## Multiple Choice Questions

1. What is dynamic binding in Java?
   - A) Method call is resolved at compile time
   - B) Method call is resolved at runtime based on actual object type
   - C) Variable type is determined at runtime
   - D) Classes are loaded dynamically

2. Which type of methods use dynamic binding?
   - A) Private methods
   - B) Static methods
   - C) Overridden instance methods
   - D) Final methods

3. What determines which method is called at runtime?
   - A) Reference type
   - B) Actual object type
   - C) Parameter types
   - D) Access modifier

4. What is the term for the mechanism that enables dynamic binding?
   - A) Static dispatch
   - B) Virtual method invocation
   - C) Early binding
   - D) Compile-time resolution

5. Which keyword prevents dynamic binding for a method?
   - A) static
   - B) final
   - C) Both A and B
   - D) Neither

## True/False Questions

6. Dynamic binding applies to overloaded methods.
   - True / False

7. All non-static, non-final, non-private methods in Java use dynamic binding.
   - True / False

8. Dynamic binding makes polymorphism possible in Java.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Base {
    void show() { System.out.println("Base"); }
}
class Derived extends Base {
    void show() { System.out.println("Derived"); }
}
class Test {
    public static void main(String[] args) {
        Base obj = new Derived();
        obj.show();
    }
}
```

10. What will this code print?
```java
class A {
    void display() { System.out.println("A"); }
}
class B extends A {
    void display() { System.out.println("B"); }
}
class C extends B {
    void display() { System.out.println("C"); }
}
class Demo {
    public static void main(String[] args) {
        A obj = new C();
        obj.display();
    }
}
```

## Answers

1. B
2. C
3. B
4. B - Virtual method invocation
5. C - Both static and final methods bypass dynamic binding
6. False - Dynamic binding applies to overridden methods, not overloaded ones
7. True
8. True
9. Output:
```
Derived
```
10. Output:
```
C
```
