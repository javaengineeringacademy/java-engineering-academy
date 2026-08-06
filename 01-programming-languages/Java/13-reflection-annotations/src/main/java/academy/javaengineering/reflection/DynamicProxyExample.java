package academy.javaengineering.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Demonstrates dynamic proxy patterns including:
 * - java.lang.reflect.Proxy fundamentals
 * - InvocationHandler implementation
 * - Logging proxy
 * - Transaction proxy
 * - Caching proxy
 * - Performance monitoring proxy
 * - Proxy composition
 */
public class DynamicProxyExample {

    private static final Logger logger = Logger.getLogger(DynamicProxyExample.class.getName());

    // === Annotations for proxy configuration ===

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface LogExecution {
        boolean logArgs() default true;
        boolean logResult() default true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Cacheable {
        int ttlSeconds() default 300;
        String key() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Transactional {
        boolean readOnly() default false;
    }

    // === Service interfaces ===

    interface UserRepository {
        String findById(long id);
        List<String> findAll();
        void save(String entity);
        void delete(long id);
        boolean exists(long id);
    }

    interface OrderService {
        @Transactional
        String createOrder(String item, int quantity);

        @Transactional(readOnly = true)
        String getOrderStatus(long orderId);

        void cancelOrder(long orderId);
    }

    interface Calculator {
        int add(int a, int b);
        int multiply(int a, int b);
        @Cacheable(ttlSeconds = 60)
        int expensiveComputation(int n);
    }

    // === Implementation classes ===

    static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, String> store = new LinkedHashMap<>();

        @Override
        public String findById(long id) {
            return store.getOrDefault(id, "User not found");
        }

        @Override
        public List<String> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public void save(String entity) {
            long id = store.size() + 1;
            store.put(id, entity);
        }

        @Override
        public void delete(long id) {
            store.remove(id);
        }

        @Override
        public boolean exists(long id) {
            return store.containsKey(id);
        }
    }

    static class SimpleOrderService implements OrderService {
        @Override
        public String createOrder(String item, int quantity) {
            return "Order created: " + item + " x" + quantity;
        }

        @Override
        public String getOrderStatus(long orderId) {
            return "Order " + orderId + " status: shipped";
        }

        @Override
        public void cancelOrder(long orderId) {
            System.out.println("Order " + orderId + " cancelled");
        }
    }

    static class SimpleCalculator implements Calculator {
        @Override
        public int add(int a, int b) {
            return a + b;
        }

        @Override
        public int multiply(int a, int b) {
            return a * b;
        }

        @Override
        public int expensiveComputation(int n) {
            // Simulate expensive work
            long result = 0;
            for (int i = 0; i < n; i++) {
                result += i;
            }
            return (int) result;
        }
    }

    // === InvocationHandler implementations ===

    /**
     * Logging proxy: logs method invocations with arguments and results.
     */
    static class LoggingHandler implements InvocationHandler {
        private final Object target;

        public LoggingHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            System.out.println("[LOG] Invoking: " + methodName);

            if (args != null && args.length > 0) {
                System.out.println("[LOG] Args: " + java.util.Arrays.toString(args));
            }

            long start = System.nanoTime();
            Object result = method.invoke(target, args);
            long elapsed = System.nanoTime() - start;

            System.out.println("[LOG] Result: " + result);
            System.out.println("[LOG] Time: " + elapsed + " ns");

            return result;
        }
    }

    /**
     * Transaction proxy: simulates transaction begin/commit/rollback.
     */
    static class TransactionHandler implements InvocationHandler {
        private final Object target;
        private boolean inTransaction = false;

        public TransactionHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Transactional txAnnotation = method.getAnnotation(Transactional.class);

            if (txAnnotation != null) {
                inTransaction = true;
                String txType = txAnnotation.readOnly() ? "READ-ONLY" : "READ-WRITE";
                System.out.println("[TX] Beginning " + txType + " transaction for " + method.getName());

                try {
                    Object result = method.invoke(target, args);

                    if (!txAnnotation.readOnly()) {
                        System.out.println("[TX] Committing transaction");
                    } else {
                        System.out.println("[TX] Read-only transaction completed");
                    }
                    return result;
                } catch (java.lang.reflect.InvocationTargetException e) {
                    System.out.println("[TX] Rolling back transaction due to: " + e.getMessage());
                    throw e;
                } finally {
                    inTransaction = false;
                }
            }

            return method.invoke(target, args);
        }
    }

    /**
     * Caching proxy: caches method results based on parameters.
     */
    static class CachingHandler implements InvocationHandler {
        private final Object target;
        private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

        public CachingHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Cacheable cacheAnnotation = method.getAnnotation(Cacheable.class);

            if (cacheAnnotation != null) {
                String cacheKey = buildKey(method, args);
                CacheEntry entry = cache.get(cacheKey);

                if (entry != null && !entry.isExpired()) {
                    System.out.println("[CACHE] Cache HIT for " + method.getName() + " key=" + cacheKey);
                    return entry.value;
                }

                System.out.println("[CACHE] Cache MISS for " + method.getName() + " key=" + cacheKey);
                Object result = method.invoke(target, args);
                cache.put(cacheKey, new CacheEntry(result, cacheAnnotation.ttlSeconds()));
                return result;
            }

            return method.invoke(target, args);
        }

        private String buildKey(Method method, Object[] args) {
            StringBuilder sb = new StringBuilder(method.getName());
            if (args != null) {
                for (Object arg : args) {
                    sb.append(":").append(arg);
                }
            }
            return sb.toString();
        }

        static class CacheEntry {
            final Object value;
            final long expiryTime;

            CacheEntry(Object value, int ttlSeconds) {
                this.value = value;
                this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
            }

            boolean isExpired() {
                return System.currentTimeMillis() > expiryTime;
            }
        }
    }

    /**
     * Performance monitoring proxy: tracks invocation counts and timing.
     */
    static class PerformanceHandler implements InvocationHandler {
        private final Object target;
        private final AtomicInteger callCount = new AtomicInteger(0);
        private final Map<String, Long> totalTimeMap = new ConcurrentHashMap<>();

        public PerformanceHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            callCount.incrementAndGet();
            long start = System.nanoTime();

            try {
                return method.invoke(target, args);
            } finally {
                long elapsed = System.nanoTime() - start;
                totalTimeMap.merge(method.getName(), elapsed, Long::sum);
            }
        }

        public int getCallCount() {
            return callCount.get();
        }

        public long getTotalTime(String methodName) {
            return totalTimeMap.getOrDefault(methodName, 0L);
        }

        public void reset() {
            callCount.set(0);
            totalTimeMap.clear();
        }

        public void printStats() {
            System.out.println("[PERF] Total calls: " + callCount.get());
            totalTimeMap.forEach((method, time) ->
                    System.out.println("[PERF] " + method + ": " + time + " ns total"));
        }
    }

    /**
     * Validation proxy: validates arguments before invocation.
     */
    static class ValidationHandler implements InvocationHandler {
        private final Object target;

        public ValidationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        throw new IllegalArgumentException(
                                "Null argument at position " + i + " for method " + method.getName());
                    }
                }
            }
            return method.invoke(target, args);
        }
    }

    // === Proxy creation utilities ===

    @SuppressWarnings("unchecked")
    public static <T> T createLoggingProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new LoggingHandler(target)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T createTransactionProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new TransactionHandler(target)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T createCacheProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new CachingHandler(target)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T createPerformanceProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new PerformanceHandler(target)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T createValidationProxy(T target, Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ValidationHandler(target)
        );
    }

    /**
     * Creates a proxy with multiple handlers (middleware chain).
     */
    @SuppressWarnings("unchecked")
    public static <T> T createChainedProxy(T target, Class<T> interfaceClass, InvocationHandler... handlers) {
        Object current = target;

        // Wrap from last to first so the first handler in the array is the outermost
        for (int i = handlers.length - 1; i >= 0; i--) {
            final int index = i;
            current = Proxy.newProxyInstance(
                    interfaceClass.getClassLoader(),
                    new Class<?>[]{interfaceClass},
                    (proxy, method, args) -> handlers[index].invoke(proxy, method, args)
            );
        }

        return (T) current;
    }

    /**
     * Demonstrates the logging proxy.
     */
    public static void demonstrateLoggingProxy() {
        System.out.println("=== Logging Proxy ===");
        UserRepository realRepo = new InMemoryUserRepository();
        UserRepository proxy = createLoggingProxy(realRepo, UserRepository.class);

        proxy.save("Alice");
        proxy.save("Bob");
        proxy.findById(1);
        proxy.findAll();
    }

    /**
     * Demonstrates the transaction proxy.
     */
    public static void demonstrateTransactionProxy() {
        System.out.println("\n=== Transaction Proxy ===");
        OrderService realService = new SimpleOrderService();
        OrderService proxy = createTransactionProxy(realService, OrderService.class);

        proxy.createOrder("Laptop", 1);
        proxy.getOrderStatus(42);
    }

    /**
     * Demonstrates the caching proxy.
     */
    public static void demonstrateCachingProxy() {
        System.out.println("\n=== Caching Proxy ===");
        Calculator realCalc = new SimpleCalculator();
        Calculator proxy = createCacheProxy(realCalc, Calculator.class);

        System.out.println("First call (cache miss):");
        int result1 = proxy.expensiveComputation(1000);
        System.out.println("Result: " + result1);

        System.out.println("\nSecond call (cache hit):");
        int result2 = proxy.expensiveComputation(1000);
        System.out.println("Result: " + result2);
    }

    /**
     * Demonstrates the performance monitoring proxy.
     */
    public static void demonstratePerformanceProxy() {
        System.out.println("\n=== Performance Proxy ===");
        Calculator realCalc = new SimpleCalculator();
        PerformanceHandler perfHandler = new PerformanceHandler(realCalc);
        Calculator proxy = (Calculator) Proxy.newProxyInstance(
                Calculator.class.getClassLoader(),
                new Class<?>[]{Calculator.class},
                perfHandler
        );

        proxy.add(5, 3);
        proxy.multiply(4, 7);
        proxy.add(10, 20);

        perfHandler.printStats();
    }

    /**
     * Demonstrates the validation proxy.
     */
    public static void demonstrateValidationProxy() {
        System.out.println("\n=== Validation Proxy ===");
        UserRepository realRepo = new InMemoryUserRepository();
        UserRepository proxy = createValidationProxy(realRepo, UserRepository.class);

        try {
            proxy.save(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }

    /**
     * Demonstrates chaining multiple proxies.
     */
    public static void demonstrateChainedProxy() {
        System.out.println("\n=== Chained Proxy ===");
        Calculator realCalc = new SimpleCalculator();

        // Create a performance handler to capture stats
        PerformanceHandler perfHandler = new PerformanceHandler(realCalc);

        // Chain: validation -> performance -> cache -> real
        Calculator proxy = createChainedProxy(realCalc, Calculator.class,
                new ValidationHandler(realCalc),
                perfHandler,
                new CachingHandler(realCalc)
        );

        proxy.add(1, 2);
        proxy.multiply(3, 4);
        proxy.expensiveComputation(100);  // cache miss
        proxy.expensiveComputation(100);  // cache hit

        perfHandler.printStats();
    }

    /**
     * Demonstrates getting proxy information at runtime.
     */
    public static void demonstrateProxyIntrospection() {
        System.out.println("\n=== Proxy Introspection ===");
        UserRepository realRepo = new InMemoryUserRepository();
        UserRepository proxy = createLoggingProxy(realRepo, UserRepository.class);

        Class<?> proxyClass = proxy.getClass();
        System.out.println("Proxy class: " + proxyClass.getName());
        System.out.println("Is proxy: " + Proxy.isProxyClass(proxyClass));

        System.out.println("Interfaces implemented:");
        for (Class<?> iface : proxyClass.getInterfaces()) {
            System.out.println("  " + iface.getName());
        }
    }

    public static void main(String[] args) {
        demonstrateLoggingProxy();
        demonstrateTransactionProxy();
        demonstrateCachingProxy();
        demonstratePerformanceProxy();
        demonstrateValidationProxy();
        demonstrateChainedProxy();
        demonstrateProxyIntrospection();
    }
}
