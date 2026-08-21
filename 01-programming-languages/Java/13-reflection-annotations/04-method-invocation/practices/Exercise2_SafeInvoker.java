package academy.javaengineering.reflection.methodinvocation.practices;

import java.lang.reflect.*;

public class Exercise2_SafeInvoker {

    public static Object safeInvoke(Object obj, String methodName, Object... args) throws Exception {
        Class<?>[] paramTypes = args == null ? new Class<?>[0] : new Class<?>[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getClass();
            if (paramTypes[i] == Integer.class) paramTypes[i] = int.class;
            if (paramTypes[i] == Long.class) paramTypes[i] = long.class;
            if (paramTypes[i] == Double.class) paramTypes[i] = double.class;
            if (paramTypes[i] == Boolean.class) paramTypes[i] = boolean.class;
        }

        Method method = obj.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);

        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (cause instanceof Exception) throw (Exception) cause;
            throw new RuntimeException(cause);
        }
    }

    static class Greeter {
        private String greet(String name) { return "Hello, " + name; }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(safeInvoke(new Greeter(), "greet", "World"));
    }
}
