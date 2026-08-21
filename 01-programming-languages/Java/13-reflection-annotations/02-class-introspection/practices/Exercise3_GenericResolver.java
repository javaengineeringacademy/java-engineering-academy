package academy.javaengineering.reflection.classintrospection.practices;

import java.lang.reflect.*;
import java.util.*;

public class Exercise3_GenericResolver {

    static class StringList extends ArrayList<String> {}
    static class IntMap extends HashMap<String, Integer> {}

    public static List<String> resolveGenericSuperclass(Class<?> clazz) {
        List<String> result = new ArrayList<>();
        Type genericSuper = clazz.getGenericSuperclass();

        if (genericSuper instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericSuper;
            for (Type typeArg : pt.getActualTypeArguments()) {
                if (typeArg instanceof Class) {
                    result.add(((Class<?>) typeArg).getSimpleName());
                } else if (typeArg instanceof TypeVariable) {
                    result.add(((TypeVariable<?>) typeArg).getName());
                } else {
                    result.add(typeArg.getTypeName());
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(resolveGenericSuperclass(StringList.class));
        System.out.println(resolveGenericSuperclass(IntMap.class));
        System.out.println(resolveGenericSuperclass(Object.class));
    }
}
