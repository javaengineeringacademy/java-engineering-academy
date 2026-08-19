package academy.javaengineering.jvm.profiling;

/**
 * Exercise 1: CPU Hotspot Profiling
 *
 * Task: Profile this application with async-profiler and identify
 * the top CPU-consuming methods.
 *
 * Run with: ./profiler.sh -d 30 -f cpu_profile.html <pid>
 */
public class Exercise1 {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== CPU Hotspot Profiling ===\n");
        System.out.println("PID: " + ProcessHandle.current().pid());
        System.out.println("Run async-profiler while this runs.\n");

        // TODO: Create workloads with different CPU profiles
        // TODO: Identify which methods are hotspots
        for (int i = 0; i < 100000; i++) {
            cpuIntensiveWork(i);
        }
    }

    static double cpuIntensiveWork(int n) {
        double result = 0;
        for (int i = 0; i < 1000; i++) {
            result += Math.sin(n + i) * Math.cos(n - i);
        }
        return result;
    }
}
