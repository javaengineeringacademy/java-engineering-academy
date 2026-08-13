package academy.javaengineering.text.memory;

public class StringBufferMemory {

    public static void main(String[] args) {
        System.out.println("=== StringBuffer Memory Analysis ===\n");

        Runtime rt = Runtime.getRuntime();

        // 1. StringBuffer vs StringBuilder
        System.out.println("--- StringBuffer vs StringBuilder ---");
        rt.gc();
        long before = rt.totalMemory() - rt.freeMemory();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 1000; i++) sb.append("a");
        long after = rt.totalMemory() - rt.freeMemory();
        System.out.println("StringBuffer: " + (after - before) + " bytes");

        rt.gc();
        before = rt.totalMemory() - rt.freeMemory();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 1000; i++) sb2.append("a");
        after = rt.totalMemory() - rt.freeMemory();
        System.out.println("StringBuilder: " + (after - before) + " bytes");

        // 2. Synchronization Overhead
        System.out.println("\n--- Synchronization Overhead ---");
        System.out.println("StringBuffer: ~2-3x slower");
        System.out.println("StringBuilder: fastest");
        System.out.println("Use StringBuilder unless multi-threaded");
    }
}
