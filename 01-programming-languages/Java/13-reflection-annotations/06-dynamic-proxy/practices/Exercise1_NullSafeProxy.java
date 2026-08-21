package academy.javaengineering.reflection.proxy.practices;

import java.lang.reflect.*;
import java.util.Arrays;

public class Exercise1_NullSafeProxy {

    public static <T> T createNullSafeProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                if (args != null) {
                    for (Object arg : args) {
                        if (arg == null) throw new NullPointerException(
                            "Null argument in " + method.getName());
                    }
                }
                return method.invoke(target, args);
            }
        );
    }

    interface Greeter { String greet(String name); }
    static class GreeterImpl implements Greeter {
        public String greet(String name) { return "Hello, " + name; }
    }

    public static void main(String[] args) throws Exception {
        Greeter safe = createNullSafeProxy(new GreeterImpl());
        System.out.println(safe.greet("World"));
        try { safe.greet(null); } catch (NullPointerException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}
