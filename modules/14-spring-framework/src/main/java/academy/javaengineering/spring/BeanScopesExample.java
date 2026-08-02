package academy.javaengineering.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean Scopes - Singleton, Prototype, Request, Session.
 */
public class BeanScopesExample {

    public interface Scope {
        Object create();
    }

    public static class SingletonScope implements Scope {
        private static final SingletonScope INSTANCE = new SingletonScope();
        private SingletonScope() {}
        public static SingletonScope getInstance() { return INSTANCE; }
        @Override
        public Object create() { return this; }
    }

    public static class PrototypeScope implements Scope {
        private int counter = 0;
        @Override
        public Object create() { return new Object(); }
        public int getCounter() { return ++counter; }
    }

    public static void main(String[] args) {
        SingletonScope s1 = SingletonScope.getInstance();
        SingletonScope s2 = SingletonScope.getInstance();
        System.out.println("Singleton same: " + (s1 == s2));

        PrototypeScope prototype = new PrototypeScope();
        Object p1 = prototype.create();
        Object p2 = prototype.create();
        System.out.println("Prototype same: " + (p1 == p2));
    }
}
