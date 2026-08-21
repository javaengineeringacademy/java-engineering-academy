package academy.javaengineering.reflection.methodinvocation.solutions;

import java.lang.reflect.Method;
import java.util.*;

public class Solution3_MethodSignaturesFormatter {

    public static List<String> getSortedSignatures(Class<?> clazz) {
        List<String> sigs = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            StringBuilder sb = new StringBuilder();
            sb.append(m.getReturnType().getSimpleName()).append(" ");
            sb.append(m.getName()).append("(");
            Class<?>[] params = m.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(params[i].getSimpleName());
            }
            sb.append(")");
            sigs.add(sb.toString());
        }
        Collections.sort(sigs);
        return sigs;
    }

    public static void main(String[] args) {
        getSortedSignatures(String.class).forEach(System.out::println);
    }
}
