package academy.javaengineering.aws;

import java.util.Map;

public class RDSExample {

    public static void main(String[] args) {
        System.out.println("=== RDS Examples ===\n");
        
        demonstrateEngines();
        demonstrateFeatures();
        demonstrateBestPractices();
    }

    public static void demonstrateEngines() {
        System.out.println("--- RDS Engines ---");
        
        Map<String, String> engines = Map.of(
            "MySQL", "General purpose relational database",
            "PostgreSQL", "Advanced features, extensions",
            "Aurora", "AWS cloud-native, high performance",
            "MariaDB", "MySQL fork, community-driven",
            "Oracle", "Enterprise applications",
            "SQL Server", "Microsoft ecosystem"
        );
        
        engines.forEach((engine, description) ->
            System.out.printf("  %-15s - %s%n", engine, description)
        );
        System.out.println();
    }

    public static void demonstrateFeatures() {
        System.out.println("--- RDS Features ---");
        
        Map<String, String> features = Map.of(
            "Multi-AZ", "High availability with standby",
            "Read Replicas", "Read scaling with async replication",
            "Automated Backups", "Daily backups with PITR",
            "Encryption", "At-rest and in-transit",
            "Monitoring", "CloudWatch metrics"
        );
        
        features.forEach((feature, description) ->
            System.out.printf("  %-20s - %s%n", feature, description)
        );
        System.out.println();
    }

    public static void demonstrateBestPractices() {
        System.out.println("--- RDS Best Practices ---");
        
        String[] practices = {
            "Enable Multi-AZ for production",
            "Use read replicas for read-heavy workloads",
            "Enable automated backups",
            "Use IAM authentication",
            "Monitor with CloudWatch",
            "Implement encryption at rest"
        };
        
        for (String practice : practices) {
            System.out.println("  " + practice);
        }
        System.out.println();
    }
}
