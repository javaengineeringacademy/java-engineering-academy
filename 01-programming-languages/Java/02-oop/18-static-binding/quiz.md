# Quiz: Static Binding

## Multiple Choice Questions

1. What is static binding?
   - A) Method resolution at runtime
   - B) Method resolution at compile time based on reference type
   - C) Binding of static variables
   - D) Binding of constructor calls

2. Which of the following uses static binding?
   - A) Overridden methods
   - B) Overloaded methods
   - C) Abstract methods
   - D) Interface methods

3. Static binding is also known as:
   - A) Dynamic dispatch
   - B) Early binding
   - C) Late binding
   - D) Runtime binding

4. Which methods are resolved using static binding?
   - A) Private, static, and final methods
   - B) Only static methods
   - C) Only final methods
   - D) All public methods

5. What is the main advantage of static binding?
   - A) Better flexibility
   - B) Better performance
   - C) More abstraction
   - D) Runtime polymorphism

## True/False Questions

6. Static binding is faster than dynamic binding because it's resolved at compile time.
   - True / False

7. Overloaded methods use dynamic binding at runtime.
   - True / False

8. Static binding eliminates the need for method lookup at runtime.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Parent {
    static void show() { System.out.println("Parent static"); }
    void display() { System.out.println("Parent instance"); }
}
class Child extends Parent {
    static void show() { System.out.println("Child static"); }
    void display() { System.out.println("Child instance"); }
}
class Test {
    public static void main(String[] args) {
        Parent p = new Child();
        p.show();      // static binding
        p.display();   // dynamic binding
    }
}
```

10. What will this code print?
```java
class Tool {
    static String name() { return "Tool"; }
    String type() { return "Base"; }
}
class Hammer extends Tool {
    static String name() { return "Hammer"; }
    String type() { return "Hammer"; }
}
class Demo {
    public static void main(String[] args) {
        Tool t = new Hammer();
        System.out.println(t.name());
        System.out.println(t.type());
    }
}
```

## Answers

1. B
2. B - Overloaded methods are resolved at compile time
3. B - Also called early binding
4. A
5. B - No runtime method lookup needed
6. True
7. False - Overloaded methods use static binding
8. True
9. Output:
```
Parent static
Child instance
```
10. Output:
```
Tool
Hammer
```
