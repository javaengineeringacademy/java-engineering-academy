package reflection.solutions;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class solutions {

    // === INTRO & CLASS INTROSPECTION ===

    public static List<Class<?>> threeWaysToGetClass() {
        Class<?> c1 = String.class;
        Class<?> c2 = "hello".getClass();
        Class<?> c3 = null;
        try { c3 = Class.forName("java.lang.String"); }
        catch (ClassNotFoundException e) { throw new RuntimeException(e); }
        return Arrays.asList(c1, c2, c3);
    }

    public static Map<String, String> inspectClassMetadata(Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("name", clazz.getName());
        map.put("simpleName", clazz.getSimpleName());
        map.put("packageName", clazz.getPackage() != null ? clazz.getPackage().getName() : "");
        map.put("isInterface", String.valueOf(clazz.isInterface()));
        map.put("isAbstract", String.valueOf(Modifier.isAbstract(clazz.getModifiers())));
        return map;
    }

    public static List<String> findAllInterfaces(Class<?> clazz) {
        Set<String> result = new LinkedHashSet<>();
        Class<?> current = clazz;
        while (current != null) {
            for (Class<?> iface : current.getInterfaces()) {
                result.add(iface.getName());
                result.addAll(findAllInterfaces(iface));
            }
            current = current.getSuperclass();
        }
        return new ArrayList<>(result);
    }

    public static List<String> classHierarchy(Class<?> clazz) {
        List<String> hierarchy = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null) {
            hierarchy.add(current.getSimpleName());
            current = current.getSuperclass();
        }
        return hierarchy;
    }

    public static Object createInstanceByName(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) { return null; }
    }

    // === FIELD ACCESS ===

    public static Object readField(Object obj, String fieldName) throws Exception {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) { clazz = clazz.getSuperclass(); }
        }
        throw new NoSuchFieldException(fieldName);
    }

    public static void writeField(Object obj, String fieldName, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Class<?> type = field.getType();
        if (type == int.class && value instanceof Integer) field.setInt(obj, (Integer) value);
        else if (type == long.class && value instanceof Long) field.setLong(obj, (Long) value);
        else if (type == double.class && value instanceof Double) field.setDouble(obj, (Double) value);
        else if (type == boolean.class && value instanceof Boolean) field.setBoolean(obj, (Boolean) value);
        else field.set(obj, value);
    }

    public static Map<String, String> getFieldMap(Class<?> clazz) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields())
            map.put(field.getName(), field.getType().getSimpleName());
        return map;
    }

    public static void copyFields(Object source, Object target) throws Exception {
        for (Field sf : source.getClass().getDeclaredFields()) {
            if (Modifier.isStatic(sf.getModifiers())) continue;
            try {
                Field tf = target.getClass().getDeclaredField(sf.getName());
                if (!sf.getType().equals(tf.getType())) continue;
                sf.setAccessible(true); tf.setAccessible(true);
                tf.set(target, sf.get(source));
            } catch (NoSuchFieldException e) { /* skip */ }
        }
    }

    public static Map<String, Integer> countFieldsByModifier(Class<?> clazz) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            String mods = Modifier.toString(field.getModifiers());
            map.merge(mods, 1, Integer::sum);
        }
        return map;
    }

    // === METHOD INVOCATION & CONSTRUCTORS ===

    public static Object invokeByName(Object obj, String methodName, Object... args) throws Exception {
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (!m.getName().equals(methodName)) continue;
            Class<?>[] params = m.getParameterTypes();
            if (params.length != (args == null ? 0 : args.length)) continue;
            boolean match = true;
            for (int i = 0; i < params.length; i++) {
                if (args[i] != null && !params[i].isInstance(args[i])) { match = false; break; }
            }
            if (match) { m.setAccessible(true); return m.invoke(obj, args); }
        }
        throw new NoSuchMethodException(methodName);
    }

    public static List<String> getMethodSignatures(Class<?> clazz) {
        List<String> sigs = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            sigs.add(m.getReturnType().getSimpleName() + " " + m.getName() +
                "(" + Arrays.stream(m.getParameterTypes())
                    .map(Class::getSimpleName).collect(Collectors.joining(", ")) + ")");
        }
        Collections.sort(sigs);
        return sigs;
    }

    public static <T> T createInstance(Class<T> clazz, Object... args) throws Exception {
        for (Constructor<?> ctor : clazz.getDeclaredConstructors()) {
            Class<?>[] params = ctor.getParameterTypes();
            if (params.length != (args == null ? 0 : args.length)) continue;
            boolean match = true;
            for (int i = 0; i < params.length; i++) {
                if (args[i] != null && !params[i].isAssignableFrom(args[i].getClass())) {
                    if (params[i] == int.class && args[i] instanceof Integer) continue;
                    if (params[i] == long.class && args[i] instanceof Long) continue;
                    if (params[i] == double.class && args[i] instanceof Double) continue;
                    if (params[i] == boolean.class && args[i] instanceof Boolean) continue;
                    match = false; break;
                }
            }
            if (match) { ctor.setAccessible(true); return (T) ctor.newInstance(args); }
        }
        throw new NoSuchMethodException("No matching constructor");
    }

    public static List<String> findMethodsByReturnType(Class<?> clazz, Class<?> returnType) {
        List<String> names = new ArrayList<>();
        for (Method m : clazz.getDeclaredMethods()) {
            if (returnType.isAssignableFrom(m.getReturnType())) names.add(m.getName());
        }
        Collections.sort(names);
        return names;
    }

    public static Object safeInvoke(Object obj, Method method, Object... args) throws Exception {
        try {
            return method.invoke(obj, args);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            if (t instanceof Exception) throw (Exception) t;
            throw new RuntimeException(t);
        }
    }
}
