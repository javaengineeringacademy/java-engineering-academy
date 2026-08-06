public class MethodOverridingAnimal {

    protected String name;

    public MethodOverridingAnimal(String name) {
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
