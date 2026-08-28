package academy.javaengineering.jvm.profiling;

import jdk.jfr.Configuration;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Description;
import jdk.jfr.Category;
import jdk.jfr.Recording;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * Exercise 2: JFR Recording Exercise
 *
 * Task: Create custom JFR events and build a programmatic JFR recording
 * that captures application behavior. Since we can't write JFR files
 * without proper permissions, focus on the event definitions and
 * recording API usage.
 *
 * Requirements:
 * 1. Define 3 custom JFR event classes with appropriate annotations
 * 2. Create a recording that enables specific JDK events
 * 3. Use different recording configurations
 * 4. Demonstrate proper event lifecycle (begin/end/commit)
 * 5. Show how to filter and configure events
 *
 * Run this class and observe the output.
 */
public class ProfilingExercise2 {

    // TODO: Define a custom event for tracking API requests
    // Include: endpoint (String), method (String), statusCode (int), durationMs (long)

    // TODO: Define a custom event for tracking cache operations
    // Include: cacheName (String), key (String), hit (boolean), durationNanos (long)

    // TODO: Define a custom event for tracking file I/O
    // Include: filePath (String), operation (String), bytesTransferred (long), durationMs (long)

    public static void main(String[] args) throws Exception {
        System.out.println("=== JFR Recording Exercise ===\n");

        // TODO: Create a recording with default configuration
        // Show what events are available

        // TODO: Create a recording with profile configuration
        // Show the difference in enabled events

        // TODO: Demonstrate enabling/disabling specific events

        // TODO: Show how to use the recording API programmatically
        // - recording.enable(EventClass.class)
        // - recording.setDuration(Duration.ofSeconds(5))
        // - recording.setDestination(path)

        // TODO: Simulate work and emit custom events
        // - Create event instances
        // - Set event fields
        // - begin() -> work -> end() -> commit()

        System.out.println("Implement the custom events and recording logic above.");
        System.out.println("Consult the JfrRecordingExample for reference.");
    }

    static void printAvailableConfigurations() throws Exception {
        System.out.println("Available JFR configurations:");
        for (String configName : new String[]{"default", "profile", "minimal"}) {
            Configuration config = Configuration.getConfiguration(configName);
            System.out.printf("  %-12s - %s%n", configName,
                    config.getDescription() != null ? config.getDescription() : "No description");
        }
        System.out.println();
    }

    static void printRecordingOptions() {
        System.out.println("Recording options:");
        System.out.println("  setDuration(Duration)       - Auto-stop after duration");
        System.out.println("  setMaxAge(Duration)         - Keep only recent events");
        System.out.println("  setMaxSize(long)            - Limit recording file size");
        System.out.println("  setDestination(Path)        - Output file path");
        System.out.println("  setDumpOnExit(boolean)      - Dump on JVM shutdown");
        System.out.println("  setCircularMemory(int)      - In-memory circular buffer (KB)");
        System.out.println();
        System.out.println("Event configuration:");
        System.out.println("  enable(String)              - Enable event by name");
        System.out.println("  enable(Class)               - Enable event by class");
        System.out.println("  enable(String, String)      - Enable with stack trace setting");
        System.out.println("  disable(String)             - Disable an event");
        System.out.println();
    }
}
