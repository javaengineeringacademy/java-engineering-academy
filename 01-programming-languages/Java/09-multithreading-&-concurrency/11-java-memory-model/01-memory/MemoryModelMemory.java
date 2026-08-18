package academy.javaengineering.concurrency.memorymodel.memory;

public class MemoryModelMemory {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Java Memory Model - Memory Visibility");
        System.out.println("======================================");

        // Demonstrate visibility issue
        class SharedState {
            int count = 0;
        }
        SharedState state = new SharedState();

        Thread writer = new Thread(() -> {
            for (int i = 0; i < 1000; i++) state.count++;
        });
        Thread reader = new Thread(() -> {
            Thread.yield();
            System.out.println("Without volatile: " + state.count + " (may not be 1000)");
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();

        // With volatile
        class VolatileState {
            volatile int count = 0;
        }
        VolatileState vstate = new VolatileState();

        Thread vw = new Thread(() -> {
            for (int i = 0; i < 1000; i++) vstate.count++;
        });
        Thread vr = new Thread(() -> {
            Thread.yield();
            System.out.println("With volatile: " + vstate.count + " (may not be 1000, volatile doesn't help here)");
        });

        vw.start();
        vr.start();
        vw.join();
        vr.join();

        System.out.println("\nNote: volatile ensures visibility but NOT atomicity.");
        System.out.println("Use AtomicInteger for atomic count++ operations.");
    }
}
