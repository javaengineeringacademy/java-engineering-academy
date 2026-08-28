package jvm;

import jdk.jfr.Configuration;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Label;
import jdk.jfr.Description;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Recording;
import jdk.jfr.RecordingFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * JfrRecordingExample - Java Flight Recorder recording example
 *
 * Demonstrates:
 * - Programmatic JFR recording via jdk.jfr API
 * - Custom JFR events with annotations
 * - Recording configurations (default, profile, minimal)
 * - Duration-based and continuous recordings
 * - Reading JFR recordings
 * - JFR event categories and labels
 *
 * Run with: java -XX:StartFlightRecording=duration=10s,filename=recording.jfr JfrRecordingExample
 * Or programmatically as shown below.
 */
public class JfrRecordingExample {

    @Label("Order Processing")
    @Description("Tracks order processing duration and outcome")
    @Category("Application")
    static class OrderProcessEvent extends Event {
        @Label("Order ID")
        String orderId;

        @Label("Item Count")
        int itemCount;

        @Label("Success")
        boolean success;

        @Label("Processing Time (ms)")
        long processingTimeMs;
    }

    @Label("Database Query")
    @Description("Tracks database query execution")
    @Category("Application.Database")
    static class DatabaseQueryEvent extends Event {
        @Label("Query")
        String query;

        @Label("Duration (ms)")
        long durationMs;

        @Label("Rows Returned")
        int rowsReturned;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Java Flight Recorder Example ===\n");

        // Show available configurations
        System.out.println("Available JFR configurations:");
        Configuration config = Configuration.getConfiguration("default");
        System.out.println("  Default config: " + config.getName());
        System.out.println("  Description: " + config.getDescription());
        System.out.println();

        // Demonstrate programmatic recording
        programmaticRecording();

        System.out.println("\n=== JFR Command-line Usage ===");
        System.out.println("Start recording:");
        System.out.println("  jcmd <pid> JFR.start name=profile duration=60s filename=profile.jfr");
        System.out.println();
        System.out.println("Dump running recording:");
        System.out.println("  jcmd <pid> JFR.dump name=profile filename=dump.jfr");
        System.out.println();
        System.out.println("Stop recording:");
        System.out.println("  jcmd <pid> JFR.stop name=profile");
        System.out.println();
        System.out.println("Start with specific configuration:");
        System.out.println("  jcmd <pid> JFR.start name=profile settings=profile duration=60s");
        System.out.println();
        System.out.println("Continuous recording (limited disk):");
        System.out.println("  jcmd <pid> JFR.start name=continuous settings=profile disk=true maxage=1h maxsize=500MB");
    }

    static void programmaticRecording() throws Exception {
        System.out.println("Programmatic JFR Recording:\n");

        Path outputPath = Paths.get("jfr_example_recording.jfr");

        // Create recording with default configuration
        try (Recording recording = new Recording()) {
            recording.setDestination(outputPath);
            recording.setDuration(Duration.ofSeconds(5));

            // Enable specific event types
            recording.enable("jdk.GarbageCollection");
            recording.enable("jdk.CPULoad");
            recording.enable("jdk.JavaMonitorEnter");
            recording.enable("jdk.ThreadStart");
            recording.enable("jdk.ThreadEnd");

            // Enable custom events
            recording.enable(OrderProcessEvent.class);
            recording.enable(DatabaseQueryEvent.class);

            System.out.println("Recording started...");
            recording.start();

            // Simulate application work that generates JFR events
            simulateApplicationWork();

            recording.stop();
            System.out.println("Recording stopped. Duration: " + recording.getDuration());

            // Check recording size
            File dest = outputPath.toFile();
            if (dest.exists()) {
                System.out.println("Recording file size: " + dest.length() + " bytes");
            }
        } catch (IOException e) {
            System.out.println("Note: JFR file I/O requires proper permissions: " + e.getMessage());
        }

        // Show how to read a JFR file
        System.out.println("\nReading JFR recordings:");
        System.out.println("  // Read with RecordingFile");
        System.out.println("  try (RecordingFile rfx = new RecordingFile(path)) {");
        System.out.println("      while (rfx.hasMoreEvents()) {");
        System.out.println("          RecordedEvent event = rfx.readEvent();");
        System.out.println("          System.out.println(event.getEventType().getName());");
        System.out.println("      }");
        System.out.println("  }");
    }

    static void simulateApplicationWork() {
        System.out.println("Simulating application work for JFR events...\n");

        // Generate custom events
        for (int i = 0; i < 5; i++) {
            OrderProcessEvent orderEvent = new OrderProcessEvent();
            orderEvent.orderId = "ORD-" + (1000 + i);
            orderEvent.itemCount = (int) (Math.random() * 10) + 1;
            orderEvent.processingTimeMs = (long) (Math.random() * 500) + 50;
            orderEvent.success = Math.random() > 0.1;
            orderEvent.begin();
            try {
                Thread.sleep(orderEvent.processingTimeMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            orderEvent.end();
            orderEvent.commit();

            System.out.printf("  Order %s: %d items, %d ms, success=%b%n",
                    orderEvent.orderId, orderEvent.itemCount,
                    orderEvent.processingTimeMs, orderEvent.success);
        }

        // Generate database query events
        String[] queries = {
            "SELECT * FROM orders WHERE status = 'PENDING'",
            "UPDATE inventory SET stock = stock - 1 WHERE product_id = 42",
            "INSERT INTO audit_log (action, timestamp) VALUES ('ORDER_CREATE', NOW())"
        };

        for (String query : queries) {
            DatabaseQueryEvent queryEvent = new DatabaseQueryEvent();
            queryEvent.query = query;
            queryEvent.durationMs = (long) (Math.random() * 100) + 5;
            queryEvent.rowsReturned = (int) (Math.random() * 1000);
            queryEvent.begin();
            try {
                Thread.sleep(queryEvent.durationMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            queryEvent.end();
            queryEvent.commit();

            System.out.printf("  Query: %.50s... (%d ms, %d rows)%n",
                    queryEvent.query, queryEvent.durationMs, queryEvent.rowsReturned);
        }

        // Trigger some GC activity for JFR GC events
        System.out.println("\n  Triggering GC activity...");
        for (int i = 0; i < 10; i++) {
            byte[] bytes = new byte[1024 * 1024]; // 1MB
            if (i % 3 == 0) {
                System.gc();
            }
        }
    }
}
