public class Animal {

    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    public void eat() {
        System.out.println(name + " is eating");
    }

    public String speak() {
        return name + " makes a sound";
    }

    public String describe() {
        return "Animal: " + name;
    }
}