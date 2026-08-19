package academy.javaengineering.jvm.diagnostics;

/**
 * Exercise 3: Automated Diagnostic Collection
 *
 * Task: Set up JVM flags for automatic diagnostic collection
 * on OutOfMemoryError and periodic monitoring.
 */
public class Exercise3 {

    public static void main(String[] args) {
        System.out.println("=== Automated Diagnostics ===\n");

        // TODO: Show JVM flags for automatic diagnostics:
        // -XX:+HeapDumpOnOutOfMemoryError
        // -XX:HeapDumpPath=/path/to/dumps
        // -Xlog:gc*:file=gc.log
        // -XX:StartFlightRecording=duration=60s,filename=recording.jfr

        System.out.println("Recommended JVM flags for production:");
        System.out.println("  -XX:+HeapDumpOnOutOfMemoryError");
        System.out.println("  -XX:HeapDumpPath=/var/log/app/heapdump.hprof");
        System.out.println("  -Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags");
        System.out.println("  -XX:StartFlightRecording=duration=60s,filename=/var/log/app/recording.jfr");
        System.out.println("  -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints");
    }
}
