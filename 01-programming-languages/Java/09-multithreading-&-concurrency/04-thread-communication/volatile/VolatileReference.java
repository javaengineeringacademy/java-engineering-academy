package academy.javaengineering.concurrency.communication.volatile;

/**
 * Demonstrates volatile with object references.
 *
 * A volatile reference guarantees visibility of the reference itself,
 * but does NOT make the object's fields thread-safe.
 */
public class VolatileReference {

    // ============================================
    // Problem: Volatile reference doesn't protect fields
    // ============================================
    static class UnsafeHolder {
        int x = 0;
        int y = 0;

        void setValues(int newX, int newY) {
            x = newX;
            y = newY;
        }

        @Override
        public String toString() {
            return "x=" + x + ", y=" + y;
        }
    }

    // ============================================
    // Solution 1: Immutable object + volatile reference
    // ============================================
    static class ImmutableData {
        final int x;
        final int y;

        ImmutableData(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "x=" + x + ", y=" + y;
        }
    }

    // ============================================
    // Solution 2: Synchronized access
    // ============================================
    static class SyncHolder {
        private int x = 0;
        private int y = 0;

        synchronized void setValues(int newX, int newY) {
            x = newX;
            y = newY;
        }

        synchronized int getX() { return x; }
        synchronized int getY() { return y; }

        @Override
        public synchronized String toString() {
            return "x=" + x + ", y=" + y;
        }
    }

    // ============================================
    // Main method
    // ============================================
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Volatile Reference Behavior ===");
        System.out.println();

        // Demonstrate the problem
        System.out.println("--- Problem: Volatile Reference with Mutable Fields ---");
        UnsafeHolder unsafeHolder = new UnsafeHolder();
        volatile UnsafeHolder volatileRef = unsafeHolder;

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                unsafeHolder.setValues(i, i * 2);
                Thread.yield();
            }
        });

        Thread reader = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                UnsafeHolder ref = volatileRef;
                // ref might see x=5 but y=8 (inconsistent!)
                System.out.println("[Reader] " + ref);
                Thread.yield();
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println();

        // Solution 1: Immutable objects
        System.out.println("--- Solution 1: Immutable Object + Volatile Reference ---");
        volatile ImmutableData immutableRef = null;

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                immutableRef = new ImmutableData(i, i * 10);
                Thread.yield();
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                ImmutableData data = immutableRef;
                if (data != null) {
                    // Both x and y are guaranteed consistent (same object)
                    System.out.println("[Consumer] " + data);
                }
                Thread.yield();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println();

        // Solution 2: Synchronized
        System.out.println("--- Solution 2: Synchronized Access ---");
        SyncHolder syncHolder = new SyncHolder();

        Thread syncWriter = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                syncHolder.setValues(i, i * 10);
                Thread.yield();
            }
        });

        Thread syncReader = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                // Guaranteed to see consistent values
                System.out.println("[Sync Reader] " + syncHolder);
                Thread.yield();
            }
        });

        syncWriter.start();
        syncReader.start();
        syncWriter.join();
        syncReader.join();
        System.out.println();

        // Summary
        System.out.println("--- Key Takeaways ---");
        System.out.println();
        System.out.println("1. volatile reference = visibility of the reference only");
        System.out.println("2. Object fields accessed through volatile reference are NOT safe");
        System.out.println("3. Solutions:");
        System.out.println("   a. Use immutable objects (best approach)");
        System.out.println("   b. Use synchronized access");
        System.out.println("   c. Make fields volatile too (if independent)");
        System.out.println("4. Example: volatile Ref<MutableObject> doesn't protect MutableObject");
    }
}
