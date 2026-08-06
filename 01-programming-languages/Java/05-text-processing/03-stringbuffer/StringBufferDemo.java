/**
 * StringBufferDemo.java
 *
 * Demonstrates StringBuffer usage and thread safety.
 */
public class StringBufferDemo {

    public static void main(String[] args) {
        System.out.println("=== StringBuffer Demo ===\n");

        // 1. Creating StringBuffer
        createStringBuffer();

        // 2. Common methods
        commonMethods();

        // 3. Thread safety demonstration
        threadSafetyDemo();

        // 4. Performance comparison
        performanceComparison();
    }

    private static void createStringBuffer() {
        System.out.println("--- 1. Creating StringBuffer ---");

        // Default constructor
        StringBuffer sb1 = new StringBuffer();
        System.out.println("Default: \"" + sb1 + "\" (capacity: " + sb1.capacity() + ")");

        // With initial string
        StringBuffer sb2 = new StringBuffer("Hello");
        System.out.println("With string: \"" + sb2 + "\" (capacity: " + sb2.capacity() + ")");

        // With initial capacity
        StringBuffer sb3 = new StringBuffer(100);
        System.out.println("With capacity: \"" + sb3 + "\" (capacity: " + sb3.capacity() + ")");

        System.out.println();
    }

    private static void commonMethods() {
        System.out.println("--- 2. Common Methods ---");

        StringBuffer sb = new StringBuffer("Hello");

        // append()
        sb.append(" World");
        System.out.println("After append: " + sb);

        // insert()
        sb.insert(5, ",");
        System.out.println("After insert: " + sb);

        // delete()
        sb.delete(5, 6);
        System.out.println("After delete: " + sb);

        // deleteCharAt()
        sb.deleteCharAt(5);
        System.out.println("After deleteCharAt: " + sb);

        // replace()
        sb.replace(6, 11, "Java");
        System.out.println("After replace: " + sb);

        // reverse()
        StringBuffer reversed = new StringBuffer("Hello");
        reversed.reverse();
        System.out.println("Reversed: " + reversed);

        // charAt()
        System.out.println("charAt(0): " + sb.charAt(0));

        // indexOf()
        System.out.println("indexOf('J'): " + sb.indexOf("J"));

        // substring()
        System.out.println("substring(6): " + sb.substring(6));

        System.out.println();
    }

    private static void threadSafetyDemo() {
        System.out.println("--- 3. Thread Safety Demo ---");

        // StringBuffer is thread-safe (synchronized)
        StringBuffer sharedBuffer = new StringBuffer("Initial");

        // Multiple threads accessing StringBuffer
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedBuffer.append("-T1");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedBuffer.append("-T2");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Shared StringBuffer result: " + sharedBuffer);
        System.out.println("Length: " + sharedBuffer.length());

        System.out.println();
    }

    private static void performanceComparison() {
        System.out.println("--- 4. Performance Comparison ---");

        int iterations = 100000;

        // StringBuilder performance
        long start = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        String builderResult = sb.toString();
        long builderTime = System.nanoTime() - start;

        // StringBuffer performance
        start = System.nanoTime();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            buffer.append("a");
        }
        String bufferResult = buffer.toString();
        long bufferTime = System.nanoTime() - start;

        System.out.println("StringBuilder time: " + builderTime / 1_000_000 + " ms");
        System.out.println("StringBuffer time: " + bufferTime / 1_000_000 + " ms");
        System.out.println("Speed ratio: " +
            String.format("%.2f", (double) bufferTime / builderTime) + "x slower");
        System.out.println("Both produce same length: " +
            (builderResult.length() == bufferResult.length()));

        System.out.println();
    }
}
