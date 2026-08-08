package reflection.solutions;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

public class ProxySolutions {

    // === LOGGING PROXY ===

    public static <T> T createLoggingProxy(T target) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                System.out.println("Calling: " + method.getName() +
                    " with args: " + Arrays.toString(args));
                Object result = method.invoke(target, args);
                System.out.println("Returned: " + result);
                return result;
            }
        );
    }

    // === CACHING PROXY ===

    public static <T> T createCachingProxy(T target) {
        Map<String, Object> cache = new ConcurrentHashMap<>();
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                String key = method.getName() + ":" + Arrays.toString(args);
                if (cache.containsKey(key)) return cache.get(key);
                Object result = method.invoke(target, args);
                cache.put(key, result);
                return result;
            }
        );
    }

    // === TIMING PROXY ===

    public static <T> Map.Entry<T, Map<String, Long>> createTimingProxy(T target) {
        Map<String, Long> timings = new ConcurrentHashMap<>();
        T proxy = (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxyObj, method, args) -> {
                long start = System.nanoTime();
                Object result = method.invoke(target, args);
                long elapsed = (System.nanoTime() - start) / 1_000_000;
                timings.merge(method.getName(), elapsed, Long::sum);
                return result;
            }
        );
        return new AbstractMap.SimpleEntry<>(proxy, timings);
    }

    // === NULL-CHECK PROXY ===

    public static <T> T createNullCheckProxy(T target) {
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

    // === ANNOTATION GATE PROXY ===

    public static <T> T createAnnotationGateProxy(T target,
            Class<? extends java.lang.annotation.Annotation> requiredAnnotation) {
        return (T) Proxy.newProxyInstance(
            target.getClass().getClassLoader(),
            target.getClass().getInterfaces(),
            (proxy, method, args) -> {
                if (!method.isAnnotationPresent(requiredAnnotation)) {
                    throw new SecurityException(
                        "Method " + method.getName() + " lacks @" +
                        requiredAnnotation.getSimpleName());
                }
                return method.invoke(target, args);
            }
        );
    }
}
