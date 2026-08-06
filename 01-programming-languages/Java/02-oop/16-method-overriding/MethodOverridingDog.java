public class MethodOverridingDog extends MethodOverridingAnimal {

    private String breed;

    public MethodOverridingDog(String name, String breed) {
        super(name);
        this.breed = breed;
    }

    @Override
    public void eat() {
        System.out.println(name + " is eating dog food");
    }

    @Override
    public String speak() {
        return name + " says: Woof!";
    }

    @Override
    public String describe() {
        return "Dog: " + name + " [" + breed + "]";
    }

    public void fetch() {
        System.out.println(name + " fetches the ball");
    }
}
