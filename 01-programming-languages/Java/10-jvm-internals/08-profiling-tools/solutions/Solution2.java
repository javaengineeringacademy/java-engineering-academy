package academy.javaengineering.jvm.profiling;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution 2: Memory Allocation Profiling
 */
public class Solution2 {

    private static final List<byte[]> cache = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Memory Allocation Profiling ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Run: ./profiler.sh -d 30 -e alloc -f alloc_profile.html " + ProcessHandle.current().pid() + "\n");

        Runtime rt = Runtime.getRuntime();
        long startMem = rt.totalMemory() - rt.freeMemory();

        for (int i = 0; i < 50000; i++) {
            allocateObjects(i);
        }

        long endMem = rt.totalMemory() - rt.freeMemory();
        System.out.printf("Allocated: %d KB%n", (endMem - startMem) / 1024);
        System.out.println("Check the allocation profile for hot allocation sites.");
    }

    static void allocateObjects(int id) {
        byte[] data = new byte[1024];
        String s = "item-" + id;
        if (id % 100 == 0) {
            cache.add(data);
        }
    }
}
