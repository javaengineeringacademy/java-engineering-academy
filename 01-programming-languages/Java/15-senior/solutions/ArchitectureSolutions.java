package academy.javaengineering.senior.solutions;

import java.time.Instant;
import java.util.*;
import java.util.stream.*;

public class ArchitectureSolutions {

    record DecisionRecord(
        String id,
        String title,
        String status,
        String context,
        String decision,
        String consequences,
        Instant date
    ) {}

    // Exercise 1: ADR Repository
    static class AdrRepository {
        private final Map<String, DecisionRecord> store = new LinkedHashMap<>();

        void record(DecisionRecord adr) {
            store.put(adr.id(), adr);
        }

        List<DecisionRecord> findByStatus(String status) {
            return store.values().stream()
                .filter(d -> d.status().equals(status))
                .collect(Collectors.toList());
        }

        List<DecisionRecord> findSuperseded() {
            Map<String, List<DecisionRecord>> byTitle = store.values().stream()
                .collect(Collectors.groupingBy(DecisionRecord::title));

            return byTitle.values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .filter(d -> d.status().equals("superseded"))
                .collect(Collectors.toList());
        }

        Optional<DecisionRecord> findById(String id) {
            return Optional.ofNullable(store.get(id));
        }
    }

    // Exercise 2: Trade-off Scoring
    record Criterion(String name, double weight) {}
    record ScoredOption(String name, Map<String, Double> scores, String rationale) {
        double weightedScore(List<Criterion> criteria) {
            return criteria.stream()
                .mapToDouble(c -> scores.getOrDefault(c.name(), 0.0) * c.weight())
                .sum();
        }
    }

    static class TradeOffScorer {
        private final List<Criterion> criteria;

        TradeOffScorer(List<Criterion> criteria) {
            this.criteria = criteria;
        }

        List<ScoredOption> rank(List<ScoredOption> options) {
            return options.stream()
                .sorted(Comparator.comparingDouble(o -> -o.weightedScore(criteria)))
                .collect(Collectors.toList());
        }
    }

    // Exercise 3: Cost Estimation
    record UsageProfile(String name, double requestsPerSecond, double dataGBPerDay, double computeHoursPerDay) {}
    record CostBreakdown(double computeCost, double storageCost, double networkCost, double totalMonthlyCost) {}

    static class CostEstimator {
        private static final Map<String, double[]> PRICING = Map.of(
            "aws",   new double[]{0.032, 0.023, 0.09},
            "gcp",   new double[]{0.030, 0.020, 0.08},
            "azure", new double[]{0.034, 0.024, 0.087}
        );

        CostBreakdown estimate(UsageProfile profile, String provider) {
            double[] prices = PRICING.getOrDefault(provider, PRICING.get("aws"));
            double compute = profile.computeHoursPerDay() * 30 * prices[0];
            double storage = profile.dataGBPerDay() * 30 * prices[1];
            double network = profile.requestsPerSecond() * 86400 * 30 * 0.000001 * prices[2];
            return new CostBreakdown(compute, storage, network, compute + storage + network);
        }

        String recommendBest(List<UsageProfile> profiles) {
            return Arrays.asList("aws", "gcp", "azure").stream()
                .min(Comparator.comparingDouble(provider ->
                    profiles.stream()
                        .mapToDouble(p -> estimate(p, provider).totalMonthlyCost())
                        .sum()
                ))
                .orElse("aws");
        }
    }

    // Exercise 4: Migration Risk Assessment
    record RiskCategory(String name, int score, String mitigation) {}

    static class MigrationRiskAssessor {
        List<RiskCategory> assess(String sourceArch, String targetArch) {
            List<RiskCategory> risks = new ArrayList<>();

            if (targetArch.contains("microservice")) {
                risks.add(new RiskCategory("Data consistency", 8,
                    "Implement saga pattern and eventual consistency"));
                risks.add(new RiskCategory("Network latency", 7,
                    "Use gRPC and circuit breakers"));
                risks.add(new RiskCategory("Team knowledge", 6,
                    "Conduct training sessions and pair programming"));
                risks.add(new RiskCategory("Deployment complexity", 7,
                    "Invest in CI/CD and Kubernetes"));
                risks.add(new RiskCategory("Testing complexity", 6,
                    "Implement contract testing and integration tests"));
            }

            if (targetArch.contains("event")) {
                risks.add(new RiskCategory("Event ordering", 7,
                    "Use partitioned topics and idempotent consumers"));
            }

            return risks;
        }

        double overallRisk(List<RiskCategory> risks) {
            if (risks.isEmpty()) return 0.0;
            return risks.stream()
                .mapToInt(RiskCategory::score)
                .average()
                .orElse(0.0);
        }
    }

    // Exercise 5: Dependency Impact Analysis
    static class DependencyGraph {
        private final Map<String, Set<String>> adj = new HashMap<>();

        void addEdge(String from, String to) {
            adj.computeIfAbsent(from, k -> new HashSet<>()).add(to);
        }

        Set<String> findAffectedServices(String failedService) {
            Set<String> affected = new HashSet<>();
            Queue<String> queue = new LinkedList<>(adj.getOrDefault(failedService, Set.of()));
            while (!queue.isEmpty()) {
                String svc = queue.poll();
                if (affected.add(svc)) {
                    queue.addAll(adj.getOrDefault(svc, Set.of()));
                }
            }
            return affected;
        }

        double blastRadius(String failedService, int totalServices) {
            return (double) findAffectedServices(failedService).size() / totalServices;
        }

        List<String> suggestCircuitBreakers() {
            // Services with most outgoing edges are good candidates
            return adj.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .map(Map.Entry::getKey)
                .limit(3)
                .toList();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Architecture Solutions ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: ADR Repository ---");
        AdrRepository repo = new AdrRepository();
        repo.record(new DecisionRecord("001", "Use Kafka", "accepted", "Need event streaming",
            "Adopt Kafka", "Adds ops complexity", Instant.parse("2024-01-01T00:00:00Z")));
        repo.record(new DecisionRecord("002", "Use Kafka", "superseded", "Replaced",
            "Switch to Pulsar", "Newer", Instant.parse("2024-06-01T00:00:00Z")));
        System.out.println("  Accepted: " + repo.findByStatus("accepted").size());
        System.out.println("  Superseded: " + repo.findSuperseded().size());

        // Exercise 2
        System.out.println("\n--- Exercise 2: Trade-off Scoring ---");
        TradeOffScorer scorer = new TradeOffScorer(List.of(
            new Criterion("performance", 0.4),
            new Criterion("cost", 0.3),
            new Criterion("complexity", 0.3)
        ));
        List<ScoredOption> ranked = scorer.rank(List.of(
            new ScoredOption("Option A", Map.of("performance", 0.8, "cost", 0.6, "complexity", 0.7), "Fast but costly"),
            new ScoredOption("Option B", Map.of("performance", 0.6, "cost", 0.9, "complexity", 0.8), "Cheap")
        ));
        System.out.println("  Best: " + ranked.get(0).name());

        // Exercise 3
        System.out.println("\n--- Exercise 3: Cost Estimation ---");
        CostEstimator estimator = new CostEstimator();
        UsageProfile api = new UsageProfile("api", 100, 50, 24);
        CostBreakdown awsCost = estimator.estimate(api, "aws");
        CostBreakdown gcpCost = estimator.estimate(api, "gcp");
        System.out.printf("  AWS: $%.2f/mo, GCP: $%.2f/mo%n", awsCost.totalMonthlyCost(), gcpCost.totalMonthlyCost());
        System.out.println("  Best: " + estimator.recommendBest(List.of(api)));

        // Exercise 4
        System.out.println("\n--- Exercise 4: Migration Risk ---");
        MigrationRiskAssessor assessor = new MigrationRiskAssessor();
        List<RiskCategory> risks = assessor.assess("monolith", "microservices");
        System.out.println("  Risks: " + risks.size());
        System.out.printf("  Overall risk: %.1f/10%n", assessor.overallRisk(risks));
        risks.forEach(r -> System.out.printf("    [%d] %s: %s%n", r.score(), r.name(), r.mitigation()));

        // Exercise 5
        System.out.println("\n--- Exercise 5: Dependency Impact ---");
        DependencyGraph graph = new DependencyGraph();
        graph.addEdge("gateway", "auth");
        graph.addEdge("gateway", "orders");
        graph.addEdge("orders", "inventory");
        graph.addEdge("orders", "payment");
        graph.addEdge("payment", "notifications");
        System.out.println("  Affected by auth failure: " + graph.findAffectedServices("auth"));
        System.out.printf("  Blast radius: %.0f%%%n", graph.blastRadius("auth", 5) * 100);
        System.out.println("  Circuit breaker suggestions: " + graph.suggestCircuitBreakers());

        System.out.println("\n=== All Solutions Complete ===");
    }
}
