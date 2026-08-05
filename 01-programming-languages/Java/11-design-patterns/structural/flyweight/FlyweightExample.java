package academy.javaengineering.patterns.structural.flyweight;

public class FlyweightExample {
    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();

        Flyweight fw1 = factory.getFlyweight("type1");
        fw1.operation("context1");

        Flyweight fw2 = factory.getFlyweight("type2");
        fw2.operation("context2");

        Flyweight fw3 = factory.getFlyweight("type1");
        fw3.operation("context3");

        System.out.println("Total flyweights: " + factory.getFlyweightCount());
    }
}
