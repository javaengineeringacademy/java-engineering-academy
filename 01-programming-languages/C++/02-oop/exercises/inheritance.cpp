/*
 * Exercise: Inheritance in C++
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Understand single and multiple inheritance
 *   - Practice virtual functions and polymorphism
 *   - Learn about abstract classes and interfaces
 *   - Understand the diamond problem and virtual inheritance
 */

#include <iostream>
#include <string>
using namespace std;

/*
 * TODO 1: Create a base class Animal with:
 * - Protected members: name, age
 * - Virtual methods: speak(), eat()
 * - Virtual destructor
 */

/*
 * TODO 2: Create derived classes Dog and Cat that inherit from Animal
 * - Override speak() to make appropriate sounds
 * - Override eat() to show different eating behaviors
 * - Add unique methods for each (fetch, purr)
 */

/*
 * TODO 3: Create an abstract class Shape with:
 * - Pure virtual methods: area(), perimeter()
 * - Virtual destructor
 */

/*
 * TODO 4: Create concrete classes Circle, Rectangle, Triangle that inherit from Shape
 * - Implement area() and perimeter() for each
 * - Add appropriate member variables
 */

/*
 * TODO 5: Demonstrate polymorphism
 * - Create an array of Shape pointers
 * - Call area() and perimeter() on each
 * - Show dynamic dispatch in action
 */

/*
 * TODO 6: Implement multiple inheritance
 * - Create classes Flyable and Swimmable
 * - Create Duck class that inherits from both
 */

/*
 * TODO 7: Demonstrate the diamond problem and virtual inheritance
 * - Create a base class Animal
 * - Create Mammal and Bird inheriting from Animal
 * - Create Bat inheriting from both
 * - Show why virtual inheritance is needed
 */

/*
 * TODO 8: Implement a virtual function table concept
 * - Show how vtable works conceptually
 */

int main() {
    /* Test cases */
    /*
    cout << "=== Inheritance Test Cases ===" << endl;

    // Test Animal hierarchy
    Dog dog("Rex", 5);
    Cat cat("Whiskers", 3);
    cout << dog.name << " says: ";
    dog.speak();
    cout << cat.name << " says: ";
    cat.speak();

    // Test polymorphism with Shape
    Circle circle(5.0);
    Rectangle rect(4.0, 6.0);
    Triangle tri(3.0, 4.0, 5.0);

    Shape* shapes[] = {&circle, &rect, &tri};
    for (int i = 0; i < 3; i++) {
        cout << "Shape " << i + 1 << ": area=" << shapes[i]->area()
             << ", perimeter=" << shapes[i]->perimeter() << endl;
    }

    // Test multiple inheritance
    Duck duck("Donald");
    duck.fly();
    duck.swim();
    duck.speak();

    // Test virtual inheritance (diamond problem)
    Bat bat("Bruce");
    bat.eat();
    bat.fly();
    */

    return 0;
}
