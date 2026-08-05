package academy.javaengineering.patterns.structural.flyweight;

public class ConcreteFlyweight implements Flyweight {
    private final String type;
    private final String intrinsicState;

    public ConcreteFlyweight(String type, String intrinsicState) {
        this.type = type;
        this.intrinsicState = intrinsicState;
    }

    @Override
    public void operation(String extrinsicState) {
        System.out.println("ConcreteFlyweight: type=" + type +
            ", intrinsic=" + intrinsicState +
            ", extrinsic=" + extrinsicState);
    }

    @Override
    public String getType() {
        return type;
    }

    public String getIntrinsicState() {
        return intrinsicState;
    }
}
