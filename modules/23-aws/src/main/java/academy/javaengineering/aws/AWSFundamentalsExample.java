package academy.javaengineering.aws;

import java.util.Map;

public class AWSFundamentalsExample {

    public static void main(String[] args) {
        System.out.println("=== AWS Fundamentals Examples ===\n");
        
        demonstrateGlobalInfrastructure();
        demonstrateIAM();
        demonstrateCoreServices();
    }

    public static void demonstrateGlobalInfrastructure() {
        System.out.println("--- AWS Global Infrastructure ---");
        
        Map<String, String> infrastructure = Map.of(
            "Regions", "Geographic areas (us-east-1, eu-west-1)",
            "Availability Zones", "Isolated data centers within regions",
            "Edge Locations", "CDN points of presence",
            "Local Zones", "Extend regions closer to users"
        );
        
        infrastructure.forEach((component, description) ->
            System.out.printf("  %-20s - %s%n", component, description)
        );
        System.out.println();
    }

    public static void demonstrateIAM() {
        System.out.println("--- IAM Concepts ---");
        
        Map<String, String> iamConcepts = Map.of(
            "Users", "Long-term credentials for individuals",
            "Roles", "Temporary credentials for AWS services",
            "Policies", "JSON documents defining permissions",
            "Groups", "Collection of users with same permissions",
            "MFA", "Multi-factor authentication"
        );
        
        iamConcepts.forEach((concept, description) ->
            System.out.printf("  %-15s - %s%n", concept, description)
        );
        System.out.println();
    }

    public static void demonstrateCoreServices() {
        System.out.println("--- Core AWS Services ---");
        
        Map<String, String> services = Map.of(
            "EC2", "Virtual servers",
            "S3", "Object storage",
            "RDS", "Managed databases",
            "Lambda", "Serverless functions",
            "SQS", "Message queuing",
            "VPC", "Virtual private cloud",
            "CloudFront", "CDN",
            "IAM", "Identity and access"
        );
        
        services.forEach((service, description) ->
            System.out.printf("  %-12s - %s%n", service, description)
        );
        System.out.println();
    }
}
