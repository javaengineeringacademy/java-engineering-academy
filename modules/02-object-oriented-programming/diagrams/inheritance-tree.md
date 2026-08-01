# Class Hierarchy and Inheritance Tree

Inheritance allows classes to inherit properties and methods from parent classes, forming hierarchical relationships.

## Basic Inheritance Structure

```mermaid
classDiagram
    class Animal {
        +String name
        +int age
        +void eat()
        +void sleep()
    }
    
    class Dog {
        +String breed
        +void bark()
        +void fetch()
    }
    
    class Cat {
        +boolean isIndoor
        +void meow()
        +void purr()
    }
    
    class Bird {
        +boolean canFly
        +void fly()
        +void sing()
    }
    
    Animal <|-- Dog
    Animal <|-- Cat
    Animal <|-- Bird
```

## Multi-level Inheritance

```mermaid
classDiagram
    class LivingThing {
        +boolean isAlive
        +void breathe()
    }
    
    class Animal {
        +String species
        +void move()
    }
    
    class Mammal {
        +boolean hasFur
        +void nurse()
    }
    
    class Dog {
        +String breed
        +void bark()
    }
    
    class GoldenRetriever {
        +String coatColor
        +void retrieve()
    }
    
    LivingThing <|-- Animal
    Animal <|-- Mammal
    Mammal <|-- Dog
    Dog <|-- GoldenRetriever
```

## Java Class Hierarchy (Core)

```mermaid
classDiagram
    class Object {
        +toString()
        +equals()
        +hashCode()
        +getClass()
    }
    
    class String {
        +length()
        +charAt()
        +substring()
    }
    
    class Number {
        +intValue()
        +doubleValue()
    }
    
    class Integer {
        +parseInt()
    }
    
    class Double {
        +parseDouble()
    }
    
    class Collection {
        +add()
        +remove()
        +contains()
    }
    
    class List {
        +get()
        +set()
        +indexOf()
    }
    
    class ArrayList {
        +ensureCapacity()
    }
    
    class LinkedList {
        +addFirst()
        +addLast()
    }
    
    Object <|-- String
    Object <|-- Number
    Number <|-- Integer
    Number <|-- Double
    Object <|-- Collection
    Collection <|-- List
    List <|-- ArrayList
    List <|-- LinkedList
```

## Interface Implementation

```mermaid
classDiagram
    class Comparable {
        <<interface>>
        +compareTo()
    }
    
    class Serializable {
        <<interface>>
    }
    
    class Cloneable {
        <<interface>>
    }
    
    class Shape {
        <<abstract>>
        +double area()
        +void draw()
    }
    
    class Circle {
        +double radius
        +double area()
        +void draw()
    }
    
    class Rectangle {
        +double width
        +double height
        +double area()
        +void draw()
    }
    
    Comparable <|.. Circle
    Comparable <|.. Rectangle
    Serializable <|.. Circle
    Cloneable <|.. Rectangle
    
    Shape <|-- Circle
    Shape <|-- Rectangle
```

## Key Takeaways

- **Single Inheritance**: Java supports single class inheritance only
- **Interfaces**: Multiple interface implementation is allowed
- **Polymorphism**: Subclasses can override parent methods
- **`extends`**: Keyword for class inheritance
- **`implements`**: Keyword for interface implementation