package performance.jfr;

import jdk.jfr.*;
import jdk.jfr.consumer.RecordingFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class JfrExamples {

    @Name("com.example.RequestEvent")
    @Label("HTTP Request")
    @Category("Application")
    @StackTrace(true)
    public static class RequestEvent extends Event {
        @Label("URL")
        String url;

        @Label("Duration (ms)")
        long durationMs;

        @Label("Status Code")
        int statusCode;
    }

    @Name("com.example.DatabaseQueryEvent")
    @Label("Database Query")
    @Category("Application.Database")
    @StackTrace(true)
    public static class DatabaseQueryEvent extends Event {
        @Label("SQL")
        String sql;

        @Label("Duration (ms)")
        long durationMs;

        @Label("Rows Returned")
        int rowsReturned;
    }

    @Name("com.example.CacheEvent")
    @Label("Cache Operation")
    @Category("Application.Cache")
    @StackTrace(false)
    public static class CacheEvent extends Event {
        @Label("Operation")
        String operation;

        @Label("Hit")
        boolean hit;

        @Label("Key")
        String key;
    }

    public static void programmaticRecording() throws IOException {
        Recording recording = new Recording();
        recording.enable(CPULoad.class, Duration.ofSeconds(1));
        recording.enable(GarbageCollection.class, Duration.ofSeconds(1));
        recording.enable(MethodProfiling.class, Duration.ofSeconds(5));
        recording.enable(ThreadLock.class, Duration.ofSeconds(1));

        recording.start();

        simulateApplicationWork();

        recording.dump(Paths.get("programmatic-recording.jfr"));
        recording.stop();

        System.out.println("Recording saved to programmatic-recording.jfr");
    }

    public static void customEventExample() throws IOException {
        Recording recording = new Recording();
        recording.start();

        for (int i = 0; i < 100; i++) {
            RequestEvent event = new RequestEvent();
            event.begin();
            event.url = "/api/users/" + i;
            event.statusCode = 200;

            simulateHttpCall();

            event.durationMs = (long) (Math.random() * 100);
            event.commit();
        }

        for (int i = 0; i < 50; i++) {
            DatabaseQueryEvent dbEvent = new DatabaseQueryEvent();
            dbEvent.begin();
            dbEvent.sql = "SELECT * FROM users WHERE id = " + i;

            simulateDbQuery();

            dbEvent.durationMs = (long) (Math.random() * 50);
            dbEvent.rowsReturned = (int) (Math.random() * 10);
            dbEvent.commit();
        }

        recording.dump(Paths.get("custom-events.jfr"));
        recording.stop();

        System.out.println("Custom events recorded to custom-events.jfr");
    }

    public static void eventStreamingExample() throws IOException {
        Recording recording = new Recording();
        recording.enable(CPULoad.class);
        recording.enable(GarbageCollection.class);
        recording.start();

        Thread processingThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        processingThread.start();

        try (jdk.jfr.consumer.RecordingStream stream = new jdk.jfr.consumer.RecordingStream()) {
            stream.onEvent("jdk.CPULoad", event -> {
                double jvmUser = event.getDouble("jvmUser");
                double jvmMachine = event.getDouble("jvmMachine");
                double machineTotal = event.getDouble("machineTotal");
                System.out.printf("CPU: JVM User=%.2f%%, JVM Machine=%.2f%%, Total=%.2f%%%n",
                    jvmUser * 100, jvmMachine * 100, machineTotal * 100);
            });

            stream.onEvent("jdk.GarbageCollection", event -> {
                long gcId = event.getLong("gcId");
                String name = event.getString("name");
                long duration = event.getLong("duration");
                System.out.printf("GC #%d (%s): %d ms%n", gcId, name, duration / 1_000_000);
            });

            stream.onEvent(event -> {
                if (event.getEventType().getName().startsWith("com.example")) {
                    System.out.printf("Custom Event: %s%n", event.getEventType().getName());
                }
            });

            stream.startAsync();

            processingThread.join();
            Thread.sleep(2000);
        }

        recording.dump(Paths.get("streaming-recording.jfr"));
        recording.stop();
    }

    public static void productionRecordingExample() throws IOException, InterruptedException {
        Recording recording = new Recording();
        recording.enable(CPULoad.class, Duration.ofSeconds(1));
        recording.enable(GarbageCollection.class, Duration.ofSeconds(1));
        recording.enable(MethodProfiling.class, Duration.ofSeconds(10));
        recording.enable(ThreadLock.class, Duration.ofSeconds(1));
        recording.enable(FileRead.class, Duration.ofSeconds(1));
        recording.enable(FileWrite.class, Duration.ofSeconds(1));
        recording.enable(SocketRead.class, Duration.ofSeconds(1));
        recording.enable(SocketWrite.class, Duration.ofSeconds(1));
        recording.setDestination(Paths.get("production-recording.jfr"));
        recording.setMaxAge(Duration.ofMinutes(5));
        recording.setMaxSize(100 * 1024 * 1024); // 100 MB

        recording.start();

        System.out.println("Production recording started. Press Ctrl+C to dump.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                recording.dump(Paths.get("production-dump.jfr"));
                recording.stop();
                System.out.println("Recording dumped and stopped.");
            } catch (IOException e) {
                System.err.println("Failed to dump recording: " + e.getMessage());
            }
        }));

        Thread.currentThread().join();
    }

    private static void simulateApplicationWork() {
        for (int i = 0; i < 10000; i++) {
            double result = 0;
            for (int j = 0; j < 1000; j++) {
                result += Math.sin(j) * Math.cos(j);
            }
            if (i % 1000 == 0) {
                System.out.println("Progress: " + i);
            }
        }
    }

    private static void simulateHttpCall() {
        try {
            Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void simulateDbQuery() {
        try {
            Thread.sleep((long) (Math.random() * 20));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java JfrExamples <mode>");
            System.out.println("Modes: programmatic, custom, streaming, production");
            return;
        }

        switch (args[0]) {
            case "programmatic" -> programmaticRecording();
            case "custom" -> customEventExample();
            case "streaming" -> eventStreamingExample();
            case "production" -> productionRecordingExample();
            default -> System.out.println("Unknown mode: " + args[0]);
        }
    }
}
