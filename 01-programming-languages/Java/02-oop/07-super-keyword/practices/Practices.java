package academy.javaengineering.oop.practices;

/**
 * Practice: 'super' Keyword
 * Complete the TODO items below.
 */
public class Practices {

    public static void main(String[] args) {
        // TODO 1: Create a Cat named "Whiskers" with 9 lives
        // Cat cat = ???
        
        // TODO 2: Make it speak
        // cat.speak();
        
        // TODO 3: Print cat info using super.toString()
        // System.out.println(cat);
    }
}

// TODO: Create an Animal class with:
// - Protected field: name (String)
// - Constructor Animal(String name)
// - void speak() - prints "name makes a sound"
// - toString() returning "Animal: name"

// TODO: Create a Cat class extending Animal with:
// - Private field: lives (int)
// - Constructor Cat(String name, int lives) - calls super(name)
// - Override speak() - calls super.speak() then prints "name says: Meow!"
// - Override toString() - calls super.toString() and adds lives info
class Cat {
    // YOUR CODE HERE
}
