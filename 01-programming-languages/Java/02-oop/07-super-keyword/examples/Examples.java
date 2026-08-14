package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== 'super' Keyword Patterns ===\n");

        // WHY: 'super' accesses hidden/overridden members from parent class
        // INTERNAL: Compiler generates invokespecial for super calls (not virtual dispatch)
        // ENGINEERING: Always call super() as first line in constructor

        Animal cat = new Cat("Whiskers", 9);
        System.out.println(cat);
        cat.speak();
        System.out.println("Has 9 lives? " + ((Cat) cat).hasNineLives());

        // TRADE-OFF: super.method() vs wrapper pattern
        // super: direct, simple, tight coupling to parent
        // wrapper: more flexible, can conditionally call parent
    }
}

class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void speak() {
        System.out.println(name + " makes a sound");
    }

    @Override
    public String toString() { return "Animal: " + name; }
}

class Cat extends Animal {
    private int lives;

    public Cat(String name, int lives) {
        super(name);  // Must be first statement
        this.lives = lives;
    }

    @Override
    public void speak() {
        super.speak();  // Call parent implementation
        System.out.println(name + " says: Meow!");
    }

    public boolean hasNineLives() { return lives == 9; }

    @Override
    public String toString() {
        return super.toString() + " (Cat, " + lives + " lives)";
    }
}
