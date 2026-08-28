package jvm;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * FlameGraphExample - Flame graph generation and analysis
 *
 * Demonstrates:
 * - Flame graph concepts and structure
 * - Generating flame graphs from profiler output
 * - Collapsed stack format
 * - Differential flame graphs (before vs after optimization)
 * - Interpreting flame graph patterns
 * - Common optimization patterns visible in flame graphs
 *
 * Flame graphs are visualizations of call stack data where:
 *   - X-axis: proportion of samples (wider = more time spent)
 *   - Y-axis: stack depth (bottom = entry point, top = leaf methods)
 *   - Color: typically arbitrary or by package (warm = more samples)
 */
public class FlameGraphExample {

    // Simulated call stack samples for flame graph construction
    private static final String[][] SAMPLED_STACKS = {
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "checkInventory"},
        {"main", "processOrders", "validateOrder", "formatResult"},
        {"main", "processOrders", "validateOrder", "formatResult"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "executeSQL"},
        {"main", "processOrders", "persistOrder", "getConnection"},
        {"main", "processOrders", "sendNotification", "formatEmail"},
        {"main", "processOrders", "sendNotification", "formatEmail"},
        {"main", "processOrders", "sendNotification", "formatEmail"},
        {"main", "processOrders", "sendNotification", "smtpSend"},
        {"main", "processOrders", "sendNotification", "smtpSend"},
        {"main", "processOrders", "sendNotification", "smtpSend"},
        {"main", "processOrders", "sendNotification", "smtpSend"},
        {"main", "processOrders", "sendNotification", "smtpSend"},
        {"main", "processOrders", "logResult", "serializeJson"},
        {"main", "processOrders", "logResult", "serializeJson"},
        {"main", "processOrders", "logResult", "serializeJson"},
        {"main", "processOrders", "logResult", "writeToFile"},
        {"main", "handleErrors", "sendAlert", "httpPost"},
        {"main", "handleErrors", "sendAlert", "httpPost"},
        {"main", "handleErrors", "sendAlert", "httpPost"},
    };

    public static void main(String[] args) throws Exception {
        System.out.println("=== Flame Graph Example ===\n");

        printFlameGraphConcepts();
        generateCollapsedStackFormat();
        generateAsciiFlameGraph();
        printOptimizationPatterns();

        System.out.println("\n=== Generating Real Flame Graphs ===");
        printRealFlameGraphCommands();
    }

    static void printFlameGraphConcepts() {
        System.out.println("Flame Graph Structure:");
        System.out.println();
        System.out.println("  Each rectangle represents a function in the call stack.");
        System.out.println("  Width = proportion of samples where this function appears.");
        System.out.println("  Height = stack depth (y-axis is call depth, not time).");
        System.out.println();
        System.out.println("  main [100%]");
        System.out.println("  └── processOrders [90%]");
        System.out.println("      ├── validateOrder [25%]");
        System.out.println("      │   ├── checkInventory [22%]  <-- hotspot");
        System.out.println("      │   └── formatResult [3%]");
        System.out.println("      ├── persistOrder [21%]");
        {"      │   ├── executeSQL [18%]  <-- hotspot"};  // unused, for display only
        System.out.println("      │   └── getConnection [3%]");
        System.out.println("      ├── sendNotification [25%]");
        System.out.println("      │   ├── formatEmail [9%]");
        {"      │   └── smtpSend [16%]  <-- hotspot"};  // unused, for display only
        System.out.println("      └── logResult [19%]");
        System.out.println("          ├── serializeJson [12%]");
        System.out.println("          └── writeToFile [7%]");
        System.out.println();
    }

    static void generateCollapsedStackFormat() {
        System.out.println("=== Collapsed Stack Format ===\n");
        System.out.println("The collapsed format is the standard input for flame graph tools:");
        System.out.println("  <semicolon-separated stack> <sample count>");
        System.out.println();

        // Count occurrences of each unique stack
        Map<String, Integer> stackCounts = new LinkedHashMap<>();
        for (String[] stack : SAMPLED_STACKS) {
            String key = String.join(";", stack);
            stackCounts.merge(key, 1, Integer::sum);
        }

        System.out.println("Collapsed output (from our simulated samples):");
        System.out.println("---");
        for (Map.Entry<String, Integer> entry : stackCounts.entrySet()) {
            System.out.printf("%s %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("---");
        System.out.println();
        System.out.println("Total samples: " + SAMPLED_STACKS.length);
        System.out.println("Unique stacks: " + stackCounts.size());
        System.out.println();
        System.out.println("Generate flame graph with:");
        System.out.println("  cat collapsed.txt | ./flamegraph.pl > flamegraph.svg");
        System.out.println("  # Or with Brendan Gregg's FlameGraph tool:");
        System.out.println("  stackcollapse-perf.pl perf.data > collapsed.txt");
        System.out.println("  flamegraph.pl collapsed.txt > flamegraph.svg");
    }

    static void generateAsciiFlameGraph() {
        System.out.println("\n=== ASCII Flame Graph Representation ===\n");

        // Calculate proportions
        Map<String, Double> methodProportions = new LinkedHashMap<>();
        int total = SAMPLED_STACKS.length;

        // Count leaf methods (top of stack = most time spent)
        Map<String, Integer> leafCounts = new TreeMap<>();
        for (String[] stack : SAMPLED_STACKS) {
            String leaf = stack[stack.length - 1];
            leafCounts.merge(leaf, 1, Integer::sum);
        }

        System.out.println("Leaf method distribution (wider = more time):");
        System.out.println();
        for (Map.Entry<String, Integer> entry : leafCounts.entrySet()) {
            double pct = (entry.getValue() * 100.0) / total;
            int barWidth = (int) (pct / 2);
            String bar = "█".repeat(Math.max(1, barWidth));
            System.out.printf("  %-25s %s %.1f%% (%d samples)%n",
                    entry.getKey(), bar, pct, entry.getValue());
        }
        System.out.println();

        // Show stack depth distribution
        Map<String, Integer> depthCounts = new TreeMap<>();
        for (String[] stack : SAMPLED_STACKS) {
            String depthKey = "depth=" + stack.length;
            depthCounts.merge(depthKey, 1, Integer::sum);
        }

        System.out.println("Stack depth distribution:");
        for (Map.Entry<String, Integer> entry : depthCounts.entrySet()) {
            double pct = (entry.getValue() * 100.0) / total;
            int barWidth = (int) (pct / 2);
            String bar = "█".repeat(Math.max(1, barWidth));
            System.out.printf("  %-15s %s %.1f%% (%d samples)%n",
                    entry.getKey(), bar, pct, entry.getValue());
        }
    }

    static void printOptimizationPatterns() {
        System.out.println("\n=== Common Flame Graph Patterns ===\n");

        System.out.println("Pattern: Wide rectangles at the top (leaf methods)");
        System.out.println("  Meaning: CPU time concentrated in specific leaf functions");
        System.out.println("  Action:  Optimize or cache those specific functions");
        System.out.println();

        System.out.println("Pattern: Wide rectangles in the middle (utility functions)");
        System.out.println("  Meaning: Common framework/library calls dominate");
        System.out.println("  Action:  Consider different algorithms or data structures");
        System.out.println();

        System.out.println("Pattern: Deep stacks (many nested calls)");
        System.out.println("  Meaning: Complex call chains, potential recursion");
        System.out.println("  Action:  Flatten call hierarchy, reduce abstraction layers");
        System.out.println();

        System.out.println("Pattern: Narrow top, wide bottom");
        System.out.println("  Meaning: Time spread across many leaf methods");
        System.out.println("  Action:  Look for broader optimizations, not single method");
        System.out.println();

        System.out.println("Pattern: GC or JIT compiler frames dominating");
        System.out.println("  Meaning: Memory pressure or compilation overhead");
        System.out.println("  Action:  Tune GC settings, reduce allocation rate");
    }

    static void printRealFlameGraphCommands() {
        System.out.println();
        System.out.println("From async-profiler:");
        System.out.println("  ./profiler.sh -d 30 -f flamegraph.html <pid>");
        System.out.println("  # Opens interactive SVG flame graph in browser");
        System.out.println();
        System.out.println("From JFR recording:");
        System.out.println("  # Open in JDK Mission Control (JMC)");
        System.out.println("  # Or convert with:");
        System.out.println("  jfr print --events jdk.ExecutionSample recording.jfr | \\" ;
        System.out.println("    stackcollapse-jfr.pl | flamegraph.pl > flamegraph.svg");
        System.out.println();
        System.out.println("Differential flame graphs (before/after optimization):");
        System.out.println("  # Capture before:");
        System.out.println("  ./profiler.sh -d 30 -o collapsed -f before.txt <pid>");
        System.out.println("  # Apply optimization");
        System.out.println("  # Capture after:");
        System.out.println("  ./profiler.sh -d 30 -o collapsed -f after.txt <pid>");
        System.out.println("  # Generate differential:");
        System.out.println("  difffolded.pl before.txt after.txt | flamegraph.pl > diff.svg");
        System.out.println("  # Red = regressions, Blue = improvements");
    }
}
