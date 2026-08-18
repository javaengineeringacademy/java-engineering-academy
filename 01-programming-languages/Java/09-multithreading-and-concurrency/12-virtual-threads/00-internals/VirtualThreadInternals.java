package academy.javaengineering.concurrency.virtualthreads.internals;

public class VirtualThreadInternals {
    public static void main(String[] args) throws Exception {
        // Create virtual thread
        Thread vt = Thread.ofVirtual()
            .name("virtual-worker")
            .start(() -> {
                System.out.println("Virtual thread: " + Thread.currentThread().getName());
                System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
                System.out.println("Thread ID: " + Thread.currentThread().getId());
            });
        vt.join();

        // Virtual thread executor
        System.out.println("\n--- Virtual Thread Executor ---");
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 10; i++) {
                final int id = i;
                executor.submit(() -> {
                    System.out.println("Task " + id + " on " + Thread.currentThread().getName());
                });
            }
        }

        // Platform thread comparison
        System.out.println("\n--- Platform Thread ---");
        Thread pt = Thread.ofPlatform()
            .name("platform-worker")
            .start(() -> {
                System.out.println("Platform thread: " + Thread.currentThread().getName());
                System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            });
        pt.join();
    }
}
