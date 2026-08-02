package academy.javaengineering.casestudies;

/**
 * Demonstrates real-world case study patterns.
 */
public class CaseStudyPatterns {

    public record CaseStudy(
        String company,
        String challenge,
        String solution,
        String outcome
    ) {}

    public static java.util.List<CaseStudy> getCaseStudies() {
        return java.util.List.of(
            new CaseStudy(
                "Netflix",
                "Handle millions of concurrent streams",
                "Microservices architecture with Cassandra",
                "99.99% uptime, global scalability"
            ),
            new CaseStudy(
                "Uber",
                "Real-time ride matching",
                "Event-driven architecture with Kafka",
                "Sub-second matching, millions of rides daily"
            ),
            new CaseStudy(
                "Amazon",
                "E-commerce scalability",
                "SOA with thousands of microservices",
                "Handles billions in sales"
            )
        );
    }
}
