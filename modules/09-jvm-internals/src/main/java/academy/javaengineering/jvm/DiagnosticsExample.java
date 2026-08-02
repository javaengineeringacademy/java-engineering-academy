package academy.javaengineering.jvm;

/**
 * JVM Diagnostics - Thread Dumps, Heap Dumps, Flight Recordings.
 */
public class DiagnosticsExample {

    public static class ThreadDumpExample {
        public void demonstrateThreads() {
            Thread mainThread = Thread.currentThread();
            System.out.println("Main Thread: " + mainThread.getName());
            System.out.println("Thread ID: " + mainThread.getId());
            System.out.println("Thread State: " + mainThread.getState());
            System.out.println("Priority: " + mainThread.getPriority());
        }
    }

    public static class HeapDumpExample {
        private java.util.Map<String, Object> data = new java.util.HashMap<>();

        public void addData(String key, Object value) {
            data.put(key, value);
        }

        public void printHeapInfo() {
            Runtime runtime = Runtime.getRuntime();
            System.out.println("Heap Size: " + runtime.totalMemory() / 1024 + " KB");
            System.out.println("Used Heap: " + (runtime.totalMemory() - runtime.freeMemory()) / 1024 + " KB");
            System.out.println("Free Heap: " + runtime.freeMemory() / 1024 + " KB");
        }
    }

    public static class FlightRecordingExample {
        public void startRecording() {
            System.out.println("Starting JFR recording...");
            System.out.println("Configuration: default");
            System.out.println("Duration: 60 seconds");
        }

        public void analyzeRecording() {
            System.out.println("Analyzing JFR recording...");
            System.out.println("Events captured: CPU, Memory, I/O, Threads");
        }
    }

    public static void main(String[] args) {
        System.out.println("=== JVM Diagnostics Demo ===");

        ThreadDumpExample threadDump = new ThreadDumpExample();
        threadDump.demonstrateThreads();

        HeapDumpExample heapDump = new HeapDumpExample();
        heapDump.addData("key1", "value1");
        heapDump.addData("key2", 42);
        heapDump.printHeapInfo();

        FlightRecordingExample flightRecording = new FlightRecordingExample();
        flightRecording.startRecording();
        flightRecording.analyzeRecording();

        System.out.println("\n=== Diagnostic Commands ===");
        System.out.println("jstack <pid>        - Thread dump");
        System.out.println("jmap -heap <pid>    - Heap info");
        System.out.println("jmap -dump <pid>    - Heap dump");
        System.out.println("jcmd <pid> JFR.start - Start JFR");
    }
}
