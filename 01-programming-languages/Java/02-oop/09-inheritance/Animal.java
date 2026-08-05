/**
 * Base class demonstrating inheritance concepts.
 *
 * <p>Inheritance allows a class to extend another class, inheriting its
 * fields and methods while adding new functionality.</p>
 */
public class Animal {

    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + " is eating");
    }

    public void sleep() {
        System.out.println(name + " is sleeping");
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    public String describe() {
        return name + " (age: " + age + ")";
    }

    @Override
    public String toString() {
        return "Animal{name='%s', age=%d}".formatted(name, age);
    }
}