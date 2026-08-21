package academy.javaengineering.reflection.constructor.practices;

import java.lang.reflect.Constructor;
import java.util.*;

public class Exercise3_ConstructorSignatureFormatter {

    public static List<String> getSortedSignatures(Class<?> clazz) {
        List<String> sigs = new ArrayList<>();
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            StringBuilder sb = new StringBuilder();
            sb.append(clazz.getSimpleName()).append("(");
            Class<?>[] params = ctor.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(params[i].getSimpleName());
            }
            sb.append(")");
            sigs.add(sb.toString());
        }
        sigs.sort(Comparator.comparingInt(s -> s.split(",").length));
        return sigs;
    }

    public static void main(String[] args) {
        getSortedSignatures(StringBuilder.class).forEach(System.out::println);
    }
}
