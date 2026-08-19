package academy.javaengineering.jvm.profiling;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: Memory Allocation Profiling
 *
 * Task: Profile memory allocations and identify high-allocation code paths.
 *
 * Run with: ./profiler.sh -d 30 -e alloc -f alloc_profile.html <pid>
 */
public class Exercise2 {

    private static final List<byte[]> cache = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Memory Allocation Profiling ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());

        // TODO: Create workloads with different allocation patterns
        // TODO: Identify which methods allocate the most memory
        for (int i = 0; i < 50000; i++) {
            allocateObjects(i);
        }
    }

    static void allocateObjects(int id) {
        // TODO: Different allocation patterns to profile
        byte[] data = new byte[1024]; // High allocation
        String s = "item-" + id; // String allocation
        if (id % 100 == 0) {
            cache.add(data); // Long-lived allocation
        }
    }
}
