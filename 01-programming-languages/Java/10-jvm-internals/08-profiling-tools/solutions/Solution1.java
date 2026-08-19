package academy.javaengineering.jvm.profiling;

/**
 * Solution 1: CPU Hotspot Profiling
 */
public class Solution1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CPU Hotspot Profiling ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Run: ./profiler.sh -d 30 -f cpu_profile.html " + ProcessHandle.current().pid() + "\n");

        // Simulate different workloads
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            cpuIntensiveWork(i);
        }
        System.out.printf("CPU work completed in %d ms%n", System.currentTimeMillis() - start);
        System.out.println("Check the flame graph for hot methods.");
    }

    static double cpuIntensiveWork(int n) {
        double result = 0;
        for (int i = 0; i < 1000; i++) {
            result += Math.sin(n + i) * Math.cos(n - i);
        }
        return result;
    }
}
