package academy.javaengineering.generics.examples;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Java 26 - Lazy Constants (JEP 482)
 *
 * <p>Complexity: O(1) for access after initialization</p>
 * <p>Thread-safety: Thread-safe with double-checked locking</p>
 * <p>Key characteristics: Lazy initialization of constants with thread-safe, on-demand computation. Provides a way to defer expensive constant initialization until first access.</p>
 */
public class LazyConstantsDemo {

    // Basic lazy constant
    private static final LazyConstant<String> LAZY_STRING = LazyConstant.of(() -> {
        System.out.println("Computing lazy string value...");
        return "computed-at-" + System.currentTimeMillis();
    });

    // Lazy constant with expensive computation
    private static final LazyConstant<String> EXPENSIVE_VALUE = LazyConstant.of(() -> {
        System.out.println("Performing expensive computation...");
        // Simulate expensive operation
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "expensive-computation-result";
    });

    // Lazy constant for configuration
    private static final LazyConstant<Config> CONFIG = LazyConstant.of(() -> {
        System.out.println("Loading configuration...");
        return new Config();
    });

    public static void main(String[] args) {
        System.out.println("Lazy Constants Demo");
        System.out.println("==================");

        // Demonstrate basic lazy constant
        demonstrateBasicLazyConstant();

        // Demonstrate supplier-based lazy constant
        demonstrateSupplierLazyConstant();

        // Demonstrate lazy constants in collections
        demonstrateLazyCollections();

        // Demonstrate thread safety
        demonstrateThreadSafety();
    }

    /**
     * Basic lazy constant usage.
     */
    private static void demonstrateBasicLazyConstant() {
        System.out.println("\n1. Basic Lazy Constant");
        System.out.println("----------------------");

        // First access triggers computation
        System.out.println("Lazy value computed: " + LAZY_STRING.get());

        // Subsequent accesses return cached value
        System.out.println("Subsequent access: " + LAZY_STRING.get() + " (same value, no recomputation)");
        System.out.println("Is initialized: " + LAZY_STRING.isInitialized());
    }

    /**
     * Lazy constant with expensive computation.
     */
    private static void demonstrateSupplierLazyConstant() {
        System.out.println("\n2. Lazy Constant with Supplier");
        System.out.println("------------------------------");

        // Value not computed yet
        System.out.println("Before first access...");

        // First access computes value
        System.out.println("Value: " + EXPENSIVE_VALUE.get());

        // Second access returns cached value
        System.out.println("Accessing again (cached): " + EXPENSIVE_VALUE.get());
    }

    /**
     * Lazy constants for configuration loading.
     */
    private static void demonstrateLazyCollections() {
        System.out.println("\n3. Lazy Constants in Collections");
        System.out.println("--------------------------------");

        // Configuration loaded on first access
        System.out.println("Config loaded: " + CONFIG.get());

        // Same config returned on subsequent access
        System.out.println("Config access (cached): " + CONFIG.get());
    }

    /**
     * Thread safety demonstration.
     */
    private static void demonstrateThreadSafety() {
        System.out.println("\n4. Thread-safe Lazy Initialization");
        System.out.println("----------------------------------");

        LazyConstant<String> threadSafeValue = LazyConstant.of(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Computed by: " + threadName);
            return threadName + "-computation";
        });

        // Start multiple threads
        Thread[] threads = new Thread[5];
        String[] results = new String[5];

        for (int i = 0; i < threads.length; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = threadSafeValue.get();
            }, "Thread-" + (i + 1));
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for completion
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // All threads get same value
        System.out.println("Thread 1 value: " + results[0]);
        System.out.println("Thread 2 value: " + results[1] + " (same instance, computed once)");
        System.out.println("All values equal: " + (results[0].equals(results[1]) &&
                results[1].equals(results[2])));
    }

    /**
     * Thread-safe lazy constant implementation.
     * Uses double-checked locking for thread safety.
     */
    static class LazyConstant<T> {
        private final Supplier<T> supplier;
        private final AtomicReference<T> value = new AtomicReference<>();

        private LazyConstant(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        /**
         * Create a lazy constant with a supplier.
         */
        public static <T> LazyConstant<T> of(Supplier<T> supplier) {
            return new LazyConstant<>(supplier);
        }

        /**
         * Get the value, computing it if necessary.
         */
        public T get() {
            T currentValue = value.get();
            if (currentValue == null) {
                synchronized (this) {
                    currentValue = value.get();
                    if (currentValue == null) {
                        currentValue = supplier.get();
                        value.set(currentValue);
                    }
                }
            }
            return currentValue;
        }

        /**
         * Check if value has been computed.
         */
        public boolean isInitialized() {
            return value.get() != null;
        }

        /**
         * Reset the lazy constant (for testing).
         */
        public void reset() {
            value.set(null);
        }
    }

    /**
     * Sample configuration class.
     */
    static class Config {
        private final String host;
        private final int port;

        public Config() {
            System.out.println("  Loading config from file...");
            this.host = "localhost";
            this.port = 5432;
        }

        @Override
        public String toString() {
            return "{db.host=" + host + ", db.port=" + port + "}";
        }
    }
}
