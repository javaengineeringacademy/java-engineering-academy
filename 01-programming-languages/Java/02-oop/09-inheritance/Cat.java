/**
 * Cat class extending Animal - demonstrates another subclass.
 */
public class Cat extends Animal {

    private boolean isIndoor;

    public Cat(String name, int age, boolean isIndoor) {
        super(name, age);
        this.isIndoor = isIndoor;
    }

    public boolean isIndoor() { return isIndoor; }

    public void purr() {
        System.out.println(name + " is purring");
    }

    public void meow() {
        System.out.println(name + " says: Meow!");
    }

    @Override
    public String describe() {
        return super.describe() + " [" + (isIndoor ? "Indoor" : "Outdoor") + "]";
    }
}