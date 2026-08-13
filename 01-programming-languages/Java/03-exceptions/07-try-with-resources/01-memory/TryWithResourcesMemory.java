package academy.javaengineering.exceptions.trywithresources.memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates memory implications of try-with-resources.
 */
public class TryWithResourcesMemory {

    // Resource that tracks allocation and deallocation
    static class TrackedResource implements AutoCloseable {
        private final String name;
        private static final List<String> openResources = new ArrayList<>();
        private boolean closed = false;

        TrackedResource(String name) {
            this.name = name;
            openResources.add(name);
            System.out.println("Allocated: " + name + " (open: " + openResources.size() + ")");
        }

        @Override
        public void close() {
            closed = true;
            openResources.remove(name);
            System.out.println("Released: " + name + " (open: " + openResources.size() + ")");
        }

        static List<String> getOpenResources() {
            return new ArrayList<>(openResources);
        }
    }

    // Simulated large resource
    static class LargeResource implements AutoCloseable {
        private final byte[] data;

        LargeResource(int sizeMb) {
            data = new byte[sizeMb * 1024 * 1024];
            System.out.println("Allocated " + sizeMb + "MB resource");
        }

        @Override
        public void close() {
            System.out.println("Released large resource");
        }
    }

    /**
     * Demonstrates deterministic cleanup with TWR.
     */
    static void deterministicCleanup() {
        System.out.println("=== Deterministic Cleanup ===");
        System.out.println("Before TWR: " + TrackedResource.getOpenResources());

        try (TrackedResource r1 = new TrackedResource("resource-1");
             TrackedResource r2 = new TrackedResource("resource-2")) {
            System.out.println("Inside TWR: " + TrackedResource.getOpenResources());
        }

        System.out.println("After TWR: " + TrackedResource.getOpenResources());
        System.out.println();
    }

    /**
     * Shows resource lifetime compared to manual management.
     */
    static void resourceLifetime() {
        System.out.println("=== Resource Lifetime ===");

        // TWR — resource released immediately
        WeakReference<TrackedResource> twrRef;
        try (TrackedResource r = new TrackedResource("twr-resource")) {
            twrRef = new WeakReference<>(r);
            System.out.println("TWR resource alive: " + (twrRef.get() != null));
        }
        System.out.println("TWR resource after close: " + (twrRef.get() != null));

        // Manual — resource reference retained
        WeakReference<TrackedResource> manualRef;
        TrackedResource manual = new TrackedResource("manual-resource");
        manualRef = new WeakReference<>(manual);
        manual.close();
        System.out.println("Manual resource after close: " + (manualRef.get() != null));
        // Note: reference still exists — GC cannot collect

        System.out.println();
    }

    /**
     * Demonstrates scope optimization with nested TWR.
     */
    static void scopeOptimization() {
        System.out.println("=== Scope Optimization ===");
        System.out.println("Open before: " + TrackedResource.getOpenResources());

        try (TrackedResource outer = new TrackedResource("outer")) {
            System.out.println("Open after outer: " + TrackedResource.getOpenResources());

            try (TrackedResource inner = new TrackedResource("inner")) {
                System.out.println("Open after inner: " + TrackedResource.getOpenResources());
            }
            System.out.println("Open after inner close: " + TrackedResource.getOpenResources());
        }
        System.out.println("Open after outer close: " + TrackedResource.getOpenResources());
        System.out.println();
    }

    /**
     * Demonstrates memory efficiency with short-lived resources.
     */
    static void shortLivedResources() {
        System.out.println("=== Short-lived Resources ===");

        for (int i = 0; i < 5; i++) {
            try (TrackedResource r = new TrackedResource("iteration-" + i)) {
                System.out.println("Processing iteration " + i);
            }
            // Resource released immediately — minimal memory footprint
        }
        System.out.println();
    }

    /**
     * Shows connection pool pattern with TWR.
     */
    static void connectionPoolPattern() {
        System.out.println("=== Connection Pool Pattern ===");

        // Simulated connection pool
        List<String> pool = new ArrayList<>(List.of("conn-1", "conn-2", "conn-3"));
        System.out.println("Pool before: " + pool);

        // Simulate TWR with connection return
        String conn = pool.remove(0);
        try {
            System.out.println("Using connection: " + conn);
            // Simulate work
        } finally {
            pool.add(conn);  // Return to pool
            System.out.println("Pool after: " + pool);
        }
        System.out.println();
    }

    /**
     * Demonstrates buffer size impact.
     */
    static void bufferImpact() {
        System.out.println("=== Buffer Size Impact ===");
        String text = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";

        // Small buffer
        try (BufferedReader reader = new BufferedReader(new StringReader(text), 16)) {
            System.out.println("Small buffer (16 bytes):");
            reader.lines().forEach(line -> System.out.println("  " + line));
        }

        // Large buffer
        try (BufferedReader reader = new BufferedReader(new StringReader(text), 1024)) {
            System.out.println("Large buffer (1024 bytes):");
            reader.lines().forEach(line -> System.out.println("  " + line));
        }
        System.out.println();
    }

    /**
     * Shows GC-friendly pattern with weak references.
     */
    static void gcFriendlyPattern() {
        System.out.println("=== GC-Friendly Pattern ===");
        WeakReference<TrackedResource> ref;

        try (TrackedResource r = new TrackedResource("gc-test")) {
            ref = new WeakReference<>(r);
            System.out.println("Resource alive: " + (ref.get() != null));
        }

        System.out.println("Resource after TWR: " + (ref.get() != null));
        System.out.println("Note: Resource is eligible for GC immediately");
        System.out.println();
    }

    /**
     * Demonstrates memory leak prevention.
     */
    static void memoryLeakPrevention() {
        System.out.println("=== Memory Leak Prevention ===");
        List<TrackedResource> leaked = new ArrayList<>();

        // Bad: resource leaked to list
        try (TrackedResource r = new TrackedResource("leaked")) {
            leaked.add(r);
            System.out.println("Resource added to list — will leak");
        }
        System.out.println("Leaked resources: " + leaked.size());

        // Good: no reference retained
        try (TrackedResource r = new TrackedResource("not-leaked")) {
            System.out.println("Resource used but not retained");
        }
        System.out.println("No leak — resource released");

        System.out.println();
    }

    public static void main(String[] args) {
        deterministicCleanup();
        resourceLifetime();
        scopeOptimization();
        shortLivedResources();
        connectionPoolPattern();
        bufferImpact();
        gcFriendlyPattern();
        memoryLeakPrevention();
    }
}
