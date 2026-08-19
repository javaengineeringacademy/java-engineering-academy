package academy.javaengineering.jvm.diagnostics;

/**
 * Solution 3: Automated Diagnostic Collection
 */
public class Solution3 {

    public static void main(String[] args) {
        System.out.println("=== Automated Diagnostics Setup ===\n");

        System.out.println("Production JVM flags:");
        System.out.println("========================\n");
        System.out.println("# Heap dump on OOM");
        System.out.println("-XX:+HeapDumpOnOutOfMemoryError");
        System.out.println("-XX:HeapDumpPath=/var/log/app/heapdump.hprof\n");
        System.out.println("# GC logging");
        System.out.println("-Xlog:gc*:file=/var/log/app/gc.log:time,uptime,level,tags:filecount=5,filesize=50m\n");
        System.out.println("# JFR continuous recording");
        System.out.println("-XX:StartFlightRecording=duration=60s,filename=/var/log/app/recording.jfr");
        System.out.println("-XX:FlightRecorderOptions=settings=profile\n");
        System.out.println("# Diagnostic flags");
        System.out.println("-XX:+UnlockDiagnosticVMOptions");
        System.out.println("-XX:+DebugNonSafepoints\n");

        System.out.println("Monitoring commands:");
        System.out.println("========================");
        System.out.println("jstat -gc <pid> 1000          # GC stats every 1s");
        System.out.println("jcmd <pid> VM.flags           # JVM flags");
        System.out.println("jcmd <pid> GC.heap_info       # Heap info");
        System.out.println("jcmd <pid> Thread.print       # Thread dump");
        System.out.println("jcmd <pid> VM.classloader_stats  # ClassLoader stats");
    }
}
