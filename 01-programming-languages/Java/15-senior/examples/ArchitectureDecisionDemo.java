package academy.javaengineering.senior.examples;

import java.time.Instant;
import java.util.*;

public class ArchitectureDecisionDemo {

    enum Status { PROPOSED, ACCEPTED, DEPRECATED, SUPERSEDED }

    record Decision(
        String id,
        String title,
        Status status,
        String context,
        String decision,
        String consequences,
        Instant date
    ) {}

    static class ArchitectureDecisionLog {
        private final List<Decision> decisions = new ArrayList<>();

        void record(Decision d) {
            decisions.add(d);
            System.out.printf("  [ADR-%s] %s (%s)%n", d.id(), d.title(), d.status());
        }

        Optional<Decision> findById(String id) {
            return decisions.stream().filter(d -> d.id().equals(id)).findFirst();
        }

        List<Decision> findByStatus(Status status) {
            return decisions.stream().filter(d -> d.status() == status).toList();
        }

        void printTimeline() {
            decisions.stream()
                .sorted(Comparator.comparing(Decision::date))
                .forEach(d -> System.out.printf("    %s [ADR-%s] %s%n",
                    d.date(), d.id(), d.title()));
        }
    }

    // Trade-off analysis framework
    record TradeOff(
        String option,
        Map<String, Integer> scores,  // criteria -> score (1-10)
        String rationale
    ) {
        int totalScore() {
            return scores.values().stream().mapToInt(Integer::intValue).sum();
        }
    }

    static class TradeOffMatrix {
        private final String decisionTitle;
        private final List<String> criteria;
        private final List<TradeOff> options = new ArrayList<>();

        TradeOffMatrix(String decisionTitle, List<String> criteria) {
            this.decisionTitle = decisionTitle;
            this.criteria = criteria;
        }

        void addOption(TradeOff option) {
            options.add(option);
        }

        void evaluate() {
            System.out.println("\n  Trade-off: " + decisionTitle);
            System.out.println("  " + "-".repeat(60));

            // Header
            StringBuilder header = new StringBuilder(String.format("  %-20s", "Option"));
            criteria.forEach(c -> header.append(String.format(" | %-12s", c)));
            header.append(" | Total");
            System.out.println(header);
            System.out.println("  " + "-".repeat(60));

            // Rows
            options.stream()
                .sorted(Comparator.comparingInt(TradeOff::totalScore).reversed())
                .forEach(opt -> {
                    StringBuilder row = new StringBuilder(String.format("  %-20s", opt.option()));
                    criteria.forEach(c -> row.append(String.format(" | %-12d", opt.scores().getOrDefault(c, 0))));
                    row.append(String.format(" | %-5d", opt.totalScore()));
                    System.out.println(row);
                });

            System.out.println("  " + "-".repeat(60));
            TradeOff best = options.stream()
                .max(Comparator.comparingInt(TradeOff::totalScore))
                .orElseThrow();
            System.out.println("  Recommended: " + best.option());
            System.out.println("  Rationale: " + best.rationale());
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Architecture Decision Demo ===\n");

        // 1. ADR Pattern
        System.out.println("--- Architecture Decision Records ---");
        ArchitectureDecisionLog adrLog = new ArchitectureDecisionLog();

        adrLog.record(new Decision(
            "001", "Use event sourcing for order service",
            Status.ACCEPTED,
            "Order service needs full audit trail and temporal queries",
            "Implement event sourcing with Kafka as event store",
            "Adds complexity but provides complete auditability and replay capability",
            Instant.parse("2024-01-15T10:00:00Z")
        ));

        adrLog.record(new Decision(
            "002", "Migrate from REST to gRPC for internal services",
            Status.PROPOSED,
            "Internal service-to-service calls have high latency over REST",
            "Adopt gRPC with Protocol Buffers for internal communication",
            "Requires proto file management but gives 3-5x latency improvement",
            Instant.parse("2024-02-20T14:30:00Z")
        ));

        adrLog.record(new Decision(
            "003", "Use Cassandra instead of PostgreSQL for time-series data",
            Status.ACCEPTED,
            "IoT sensor data grows to 1TB/day, PostgreSQL struggles with writes",
            "Migrate time-series workload to Cassandra with compaction tuning",
            "Loses SQL flexibility but handles write throughput at scale",
            Instant.parse("2024-03-10T09:15:00Z")
        ));

        adrLog.record(new Decision(
            "001", "Use event sourcing for order service",
            Status.SUPERSEDED,
            "Replaced by ADR-004 due to team scaling concerns",
            "Switch to CQRS with separate read/write stores",
            "Reduces operational complexity while maintaining audit needs",
            Instant.parse("2024-06-01T11:00:00Z")
        ));

        System.out.println("\n  Timeline:");
        adrLog.printTimeline();

        System.out.println("\n  Accepted decisions:");
        adrLog.findByStatus(Status.ACCEPTED)
            .forEach(d -> System.out.println("    - " + d.title()));

        // 2. Trade-off Analysis
        System.out.println("\n--- Trade-off Analysis ---");

        TradeOffMatrix databaseSelection = new TradeOffMatrix(
            "Primary Database Selection",
            List.of("Performance", "Scalability", "Consistency", "Ops Complexity", "Cost")
        );

        databaseSelection.addOption(new TradeOff(
            "PostgreSQL",
            Map.of("Performance", 7, "Scalability", 5, "Consistency", 9, "Ops Complexity", 8, "Cost", 8),
            "Best for ACID compliance and complex queries"
        ));

        databaseSelection.addOption(new TradeOff(
            "Cassandra",
            Map.of("Performance", 9, "Scalability", 9, "Consistency", 5, "Ops Complexity", 5, "Cost", 6),
            "Best for write-heavy, horizontally scaled workloads"
        ));

        databaseSelection.addOption(new TradeOff(
            "MongoDB",
            Map.of("Performance", 7, "Scalability", 8, "Consistency", 6, "Ops Complexity", 7, "Cost", 7),
            "Best for flexible schemas and rapid development"
        ));

        databaseSelection.evaluate();

        // 3. Cost-benefit analysis
        System.out.println("\n--- Cost-Benefit Analysis ---");
        System.out.println("  Migration: Monolith to Microservices");
        System.out.println("  " + "-".repeat(50));

        Map<String, double[]> costBenefit = new LinkedHashMap<>();
        costBenefit.put("Development time", new double[]{200_000, 0});      // cost, benefit
        costBenefit.put("Infrastructure", new double[]{50_000, 0});
        costBenefit.put("Training", new double[]{30_000, 0});
        costBenefit.put("Independent deploy", new double[]{0, 150_000});
        costBenefit.put("Team autonomy", new double[]{0, 200_000});
        costBenefit.put("Scalability", new double[]{0, 300_000});

        double totalCost = 0, totalBenefit = 0;
        for (var entry : costBenefit.entrySet()) {
            double cost = entry.getValue()[0];
            double benefit = entry.getValue()[1];
            totalCost += cost;
            totalBenefit += benefit;
            String label = cost > 0 ? String.format("$%.0f cost", cost) : String.format("$%.0f benefit", benefit);
            System.out.printf("    %-25s %s%n", entry.getKey(), label);
        }

        System.out.println("  " + "-".repeat(50));
        System.out.printf("    Total cost:     $%.0f%n", totalCost);
        System.out.printf("    Total benefit:  $%.0f%n", totalBenefit);
        System.out.printf("    ROI:            %.1fx%n", totalBenefit / totalCost);
        System.out.printf("    Payback:        ~%d months%n", (long)(totalCost / (totalBenefit / 12)));

        System.out.println("\n=== Demo Complete ===");
    }
}
