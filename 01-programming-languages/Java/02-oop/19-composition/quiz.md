# Quiz: Composition

## Multiple Choice Questions

1. What is composition in Java?
   - A) A "has-a" relationship where the child cannot exist without the parent
   - B) A "is-a" relationship between classes
   - C) Inheriting from multiple classes
   - D) Defining methods in a class

2. In composition, what happens when the parent object is destroyed?
   - A) Child objects are destroyed too
   - B) Child objects continue to exist
   - C) Child objects become null
   - D) Nothing happens

3. Which is an example of composition?
   - A) Car has-a Engine
   - B) Dog is-a Animal
   - C) Circle is-a Shape
   - D) Student is-a Person

4. What is the main advantage of composition over inheritance?
   - A) Faster execution
   - B) Greater flexibility and loose coupling
   - C) Less memory usage
   - D) Simpler code

5. How is composition typically implemented?
   - A) Using the extends keyword
   - B) Using instance variables of other classes
   - C) Using the implements keyword
   - D) Using static methods

## True/False Questions

6. In composition, the lifecycle of the contained object is managed by the container.
   - True / False

7. Composition creates a tighter coupling than inheritance.
   - True / False

8. A car composed of an engine, wheels, and seats is a good example of composition.
   - True / False

## Code Output Questions

9. What will this code print?
```java
class Engine {
    void start() { System.out.println("Engine started"); }
}
class Car {
    private Engine engine = new Engine();
    void start() {
        engine.start();
        System.out.println("Car ready");
    }
}
class Test {
    public static void main(String[] args) {
        Car car = new Car();
        car.start();
    }
}
```

10. What will this code print?
```java
class Room {
    private String name;
    Room(String name) { this.name = name; }
    String getName() { return name; }
}
class House {
    private Room[] rooms;
    House() {
        rooms = new Room[]{ new Room("Bedroom"), new Room("Kitchen") };
    }
    void listRooms() {
        for (Room r : rooms)
            System.out.println(r.getName());
    }
}
class Demo {
    public static void main(String[] args) {
        new House().listRooms();
    }
}
```

## Answers

1. A
2. A - In true composition, contained objects are destroyed with the container
3. A
4. B
5. B
6. True
7. False - Composition creates looser coupling than inheritance
8. True
9. Output:
```
Engine started
Car ready
```
10. Output:
```
Bedroom
Kitchen
```
