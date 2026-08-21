package academy.javaengineering.reflection.realworld.practices;

import java.lang.annotation.*;
import java.lang.reflect.*;

public class Exercise1_MiniBeanContainer {

    @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
    public @interface Inject {}

    public static <T> T createAndWire(Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object dep = field.getType().getDeclaredConstructor().newInstance();
                field.setAccessible(true);
                field.set(instance, dep);
            }
        }
        return instance;
    }

    static class Service { public String serve() { return "served"; } }
    static class Controller { @Inject private Service service; }

    public static void main(String[] args) throws Exception {
        Controller c = createAndWire(Controller.class);
        System.out.println(c.service.serve());
    }
}
