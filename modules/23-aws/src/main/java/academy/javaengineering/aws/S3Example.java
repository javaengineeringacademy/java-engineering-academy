package academy.javaengineering.aws;

import java.util.Map;

public class S3Example {

    public static void main(String[] args) {
        System.out.println("=== S3 Examples ===\n");
        
        demonstrateStorageClasses();
        demonstrateOperations();
        demonstrateSecurity();
    }

    public static void demonstrateStorageClasses() {
        System.out.println("--- S3 Storage Classes ---");
        
        Map<String, String> storageClasses = Map.of(
            "Standard", "Frequent access, instant retrieval",
            "Standard-IA", "Infrequent access, instant retrieval",
            "One Zone-IA", "Non-critical, infrequent",
            "Glacier Instant", "Archive, millisecond retrieval",
            "Glacier Flexible", "Archive, minutes-hours retrieval",
            "Intelligent-Tiering", "Automatic tiering"
        );
        
        storageClasses.forEach((className, description) ->
            System.out.printf("  %-20s - %s%n", className, description)
        );
        System.out.println();
    }

    public static void demonstrateOperations() {
        System.out.println("--- S3 Operations ---");
        
        String operations = """
                Operations:
                - PUT: Upload object
                - GET: Download object
                - DELETE: Remove object
                - LIST: List objects
                - COPY: Copy object
                - HEAD: Get metadata
                """;
        
        System.out.println(operations);
    }

    public static void demonstrateSecurity() {
        System.out.println("--- S3 Security ---");
        
        Map<String, String> securityFeatures = Map.of(
            "Bucket Policy", "Resource-based policy",
            "IAM Policy", "Identity-based policy",
            "ACLs", "Legacy access control",
            "Encryption", "At-rest encryption",
            "VPC Endpoint", "Private network access"
        );
        
        securityFeatures.forEach((feature, description) ->
            System.out.printf("  %-18s - %s%n", feature, description)
        );
        System.out.println();
    }
}
