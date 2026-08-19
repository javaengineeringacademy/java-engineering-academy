package academy.javaengineering.jvm.tuning;

/**
 * Solution 3: Container JVM Optimization
 */
public class Solution3 {

    public static void main(String[] args) {
        System.out.println("=== Container JVM Optimization ===\n");

        Runtime rt = Runtime.getRuntime();
        System.out.println("Detected resources:");
        System.out.println("  Max heap: " + rt.maxMemory() / (1024 * 1024) + " MB");
        System.out.println("  Processors: " + rt.availableProcessors());
        System.out.println("  Container support: " + System.getProperty("java.vm.info", "unknown"));

        System.out.println("\nRecommended configuration for 2GB container:");
        System.out.println("=========================================\n");
        System.out.println("java \\");
        System.out.println("  -XX:MaxRAMPercentage=75.0 \\");
        System.out.println("  -XX:InitialRAMPercentage=75.0 \\");
        System.out.println("  -XX:+UseContainerSupport \\");
        System.out.println("  -XX:ActiveProcessorCount=2 \\");
        System.out.println("  -XX:+UseG1GC \\");
        System.out.println("  -XX:MaxGCPauseMillis=200 \\");
        System.out.println("  -XX:MetaspaceSize=128m \\");
        System.out.println("  -XX:MaxMetaspaceSize=256m \\");
        System.out.println("  -XX:ReservedCodeCacheSize=128m \\");
        System.out.println("  -Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags \\");
        System.out.println("  -jar app.jar\n");

        System.out.println("Memory breakdown:");
        System.out.println("  Heap (75%): 1536 MB");
        System.out.println("  Metaspace: 256 MB");
        System.out.println("  Code Cache: 128 MB");
        System.out.println("  Thread stacks: ~50 MB");
        System.out.println("  Native/other: ~30 MB");
        System.out.println("  Total: ~2000 MB");
    }
}
