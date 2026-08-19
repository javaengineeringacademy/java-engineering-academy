package academy.javaengineering.jvm.tuning;

/**
 * Exercise 3: Container JVM Optimization
 *
 * Task: Determine optimal JVM flags for a containerized application
 * with 2GB memory limit and 2 CPU cores.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== Container JVM Optimization ===\n");
        System.out.println("Container: 2GB memory, 2 CPU cores\n");

        // TODO: Calculate optimal JVM flags
        // TODO: Account for non-heap memory usage
        // TODO: Set appropriate GC algorithm

        System.out.println("Recommended configuration:");
        System.out.println("  -XX:MaxRAMPercentage=75.0    # 1.5GB heap");
        System.out.println("  -XX:InitialRAMPercentage=75.0");
        System.out.println("  -XX:+UseContainerSupport");
        System.out.println("  -XX:ActiveProcessorCount=2");
        System.out.println("  -XX:+UseG1GC");
        System.out.println("  -XX:MaxGCPauseMillis=200");
        System.out.println("  -XX:MetaspaceSize=128m");
        System.out.println("  -XX:MaxMetaspaceSize=256m");
        System.out.println("  -Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags");
    }
}
