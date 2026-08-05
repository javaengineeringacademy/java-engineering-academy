package academy.javaengineering.patterns.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

public class FlyweightFactory {
    private final Map<String, Flyweight> flyweights = new HashMap<>();

    public Flyweight getFlyweight(String type) {
        Flyweight flyweight = flyweights.get(type);
        if (flyweight == null) {
            flyweight = new ConcreteFlyweight(type, "state_" + type);
            flyweights.put(type, flyweight);
            System.out.println("FlyweightFactory: Created new flyweight for " + type);
        } else {
            System.out.println("FlyweightFactory: Reusing existing flyweight for " + type);
        }
        return flyweight;
    }

    public int getFlyweightCount() {
        return flyweights.size();
    }

    public void clear() {
        flyweights.clear();
    }
}
