package academy.javaengineering.senior.practices;

import java.time.Instant;
import java.util.*;

/**
 * Architecture Exercises
 *
 * Complete each exercise by implementing the TODO sections.
 * Focus on ADR patterns, trade-off analysis, and cost modeling.
 */
public class ArchitectureExercises {

    record DecisionRecord(
        String id,
        String title,
        String status,
        String context,
        String decision,
        String consequences,
        Instant date
    ) {}

    // ============================================================
    // Exercise 1: ADR Repository
    // ============================================================
    // Implement an ADR repository that:
    // 1. Stores decision records with unique IDs
    // 2. Supports finding by status (accepted, deprecated, proposed)
    // 3. Supports finding by date range
    // 4. Can detect superseded decisions (same title, different ID)
    static class AdrRepository {
        // TODO: Implement the repository
        void record(DecisionRecord adr) {
            throw new UnsupportedOperationException("Exercise 1 not implemented");
        }

        List<DecisionRecord> findByStatus(String status) {
            throw new UnsupportedOperationException("Exercise 1 not implemented");
        }

        List<DecisionRecord> findSuperseded() {
            throw new UnsupportedOperationException("Exercise 1 not implemented");
        }

        Optional<DecisionRecord> findById(String id) {
            throw new UnsupportedOperationException("Exercise 1 not implemented");
        }
    }

    // ============================================================
    // Exercise 2: Trade-off Scoring
    // ============================================================
    // Implement a trade-off scoring system that:
    // 1. Takes multiple options with weighted criteria scores
    // 2. Normalizes scores to 0-1 range
    // 3. Applies weights to criteria
    // 4. Returns ranked options with scores and rationale
    record Criterion(String name, double weight) {}
    record ScoredOption(String name, Map<String, Double> scores, String rationale) {}

    static class TradeOffScorer {
        private final List<Criterion> criteria;

        TradeOffScorer(List<Criterion> criteria) {
            this.criteria = criteria;
        }

        List<ScoredOption> rank(List<ScoredOption> options) {
            // TODO: Implement weighted scoring and ranking
            throw new UnsupportedOperationException("Exercise 2 not implemented");
        }
    }

    // ============================================================
    // Exercise 3: Cost Estimation Model
    // ============================================================
    // Implement a cost estimation model that:
    // 1. Takes hourly usage patterns (requests/sec, data volume)
    // 2. Calculates compute, storage, and network costs
    // 3. Supports multiple cloud providers (AWS, GCP, Azure)
    // 4. Returns a cost breakdown and recommendation
    record UsageProfile(
        String name,
        double requestsPerSecond,
        double dataGBPerDay,
        double computeHoursPerDay
    ) {}

    record CostBreakdown(
        double computeCost,
        double storageCost,
        double networkCost,
        double totalMonthlyCost
    ) {}

    static class CostEstimator {
        CostBreakdown estimate(UsageProfile profile, String provider) {
            // TODO: Implement cost estimation per provider
            throw new UnsupportedOperationException("Exercise 3 not implemented");
        }

        String recommendBest(List<UsageProfile> profiles) {
            // TODO: Compare providers and recommend cheapest
            throw new UnsupportedOperationException("Exercise 3 not implemented");
        }
    }

    // ============================================================
    // Exercise 4: Migration Risk Assessment
    // ============================================================
    // Implement a migration risk assessment that:
    // 1. Takes source and target architecture descriptions
    // 2. Identifies risk categories (data, performance, team, timeline)
    // 3. Scores each risk 1-10
    // 4. Suggests mitigations
    // 5. Returns an overall risk score
    record RiskCategory(String name, int score, String mitigation) {}

    static class MigrationRiskAssessor {
        List<RiskCategory> assess(String sourceArch, String targetArch) {
            // TODO: Implement risk assessment logic
            throw new UnsupportedOperationException("Exercise 4 not implemented");
        }

        double overallRisk(List<RiskCategory> risks) {
            // TODO: Calculate weighted overall risk score
            throw new UnsupportedOperationException("Exercise 4 not implemented");
        }
    }

    // ============================================================
    // Exercise 5: Dependency Impact Analysis
    // ============================================================
    // Implement a dependency impact analyzer that:
    // 1. Models a directed graph of service dependencies
    // 2. Given a failing service, identifies all affected downstream services
    // 3. Calculates blast radius (percentage of system affected)
    // 4. Suggests circuit breaker placement
    static class DependencyGraph {
        // TODO: Implement using adjacency list
        void addEdge(String from, String to) {
            throw new UnsupportedOperationException("Exercise 5 not implemented");
        }

        Set<String> findAffectedServices(String failedService) {
            throw new UnsupportedOperationException("Exercise 5 not implemented");
        }

        double blastRadius(String failedService, int totalServices) {
            throw new UnsupportedOperationException("Exercise 5 not implemented");
        }

        List<String> suggestCircuitBreakers() {
            throw new UnsupportedOperationException("Exercise 5 not implemented");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Architecture Exercises ===\n");

        // Test Exercise 1
        System.out.println("--- Exercise 1: ADR Repository ---");
        try {
            AdrRepository repo = new AdrRepository();
            repo.record(new DecisionRecord("001", "Use Kafka", "accepted", "Need event streaming",
                "Adopt Kafka", "Adds operational complexity", Instant.parse("2024-01-01T00:00:00Z")));
            repo.record(new DecisionRecord("002", "Use Kafka", "superseded", "Replaced by ADR-003",
                "Switch to Pulsar", "Newer technology", Instant.parse("2024-06-01T00:00:00Z")));
            System.out.println("  By status: " + repo.findByStatus("accepted").size());
            System.out.println("  Superseded: " + repo.findSuperseded().size());
            System.out.println("  PASS: true");
        } catch (UnsupportedOperationException e) {
            System.out.println("  NOT IMPLEMENTED");
        }

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Trade-off Scoring ---");
        try {
            TradeOffScorer scorer = new TradeOffScorer(List.of(
                new Criterion("performance", 0.4),
                new Criterion("cost", 0.3),
                new Criterion("complexity", 0.3)
            ));
            List<ScoredOption> options = List.of(
                new ScoredOption("Option A", Map.of("performance", 0.8, "cost", 0.6, "complexity", 0.7), "Fast but costly"),
                new ScoredOption("Option B", Map.of("performance", 0.6, "cost", 0.9, "complexity", 0.8), "Cheap and simple")
            );
            List<ScoredOption> ranked = scorer.rank(options);
            System.out.println("  Ranked: " + ranked.get(0).name());
            System.out.println("  PASS: true");
        } catch (UnsupportedOperationException e) {
            System.out.println("  NOT IMPLEMENTED");
        }

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Cost Estimation ---");
        try {
            CostEstimator estimator = new CostEstimator();
            UsageProfile profile = new UsageProfile("api", 100, 50, 24);
            CostBreakdown cost = estimator.estimate(profile, "aws");
            System.out.println("  Monthly cost: $" + cost.totalMonthlyCost());
            System.out.println("  PASS: " + (cost.totalMonthlyCost() > 0));
        } catch (UnsupportedOperationException e) {
            System.out.println("  NOT IMPLEMENTED");
        }

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Migration Risk ---");
        try {
            MigrationRiskAssessor assessor = new MigrationRiskAssessor();
            List<RiskCategory> risks = assessor.assess("monolith", "microservices");
            System.out.println("  Risks identified: " + risks.size());
            double overall = assessor.overallRisk(risks);
            System.out.println("  Overall risk: " + overall);
            System.out.println("  PASS: " + (overall > 0 && overall <= 10));
        } catch (UnsupportedOperationException e) {
            System.out.println("  NOT IMPLEMENTED");
        }

        // Test Exercise 5
        System.out.println("\n--- Exercise 5: Dependency Impact ---");
        try {
            DependencyGraph graph = new DependencyGraph();
            graph.addEdge("gateway", "auth");
            graph.addEdge("gateway", "orders");
            graph.addEdge("orders", "inventory");
            graph.addEdge("orders", "payment");
            graph.addEdge("payment", "notifications");
            Set<String> affected = graph.findAffectedServices("auth");
            double radius = graph.blastRadius("auth", 5);
            System.out.println("  Affected by auth failure: " + affected);
            System.out.println("  Blast radius: " + (radius * 100) + "%");
            System.out.println("  PASS: " + (radius > 0));
        } catch (UnsupportedOperationException e) {
            System.out.println("  NOT IMPLEMENTED");
        }
    }
}
