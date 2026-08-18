package academy.javaengineering.concurrency.threadlocal.memory;

public class ThreadLocalMemory {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("ThreadLocal Memory Analysis");
        System.out.println("===========================");

        Runtime rt = Runtime.getRuntime();
        long before = rt.freeMemory();

        ThreadLocal<byte[]> tl = new ThreadLocal<>();
        tl.set(new byte[1024]); // 1KB per thread

        long after = rt.freeMemory();
        System.out.println("ThreadLocal overhead: ~" + (before - after) + " bytes");

        System.out.println("\n⚠ Memory leak risk:");
        System.out.println("  - ThreadLocal values stored in Thread's ThreadLocalMap");
        System.out.println("  - In pools, thread is reused → old values persist");
        System.out.println("  - ALWAYS call remove() in finally blocks");
    }
}
