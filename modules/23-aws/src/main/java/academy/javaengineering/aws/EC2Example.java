package academy.javaengineering.aws;

import java.util.Map;

public class EC2Example {

    public static void main(String[] args) {
        System.out.println("=== EC2 Examples ===\n");
        
        demonstrateInstanceTypes();
        demonstrateSecurityGroups();
        demonstrateLaunch();
    }

    public static void demonstrateInstanceTypes() {
        System.out.println("--- EC2 Instance Types ---");
        
        Map<String, String> instanceTypes = Map.of(
            "t3.micro", "General purpose, burstable",
            "m5.large", "General purpose, balanced",
            "c5.xlarge", "Compute optimized",
            "r5.2xlarge", "Memory optimized",
            "i3.large", "Storage optimized"
        );
        
        instanceTypes.forEach((type, description) ->
            System.out.printf("  %-12s - %s%n", type, description)
        );
        System.out.println();
    }

    public static void demonstrateSecurityGroups() {
        System.out.println("--- Security Group Rules ---");
        
        String rules = """
                Security Group Rules:
                - Inbound: Allow incoming traffic
                - Outbound: Allow outgoing traffic
                - Protocols: TCP, UDP, ICMP
                - Sources: CIDR, security groups
                """;
        
        System.out.println(rules);
    }

    public static void demonstrateLaunch() {
        System.out.println("--- EC2 Launch Configuration ---");
        
        String config = """
                Launch Parameters:
                - AMI ID: ami-0c55b159cbfafe1f0
                - Instance Type: t3.micro
                - Key Pair: my-key-pair
                - Security Group: sg-12345678
                - Subnet: subnet-12345678
                """;
        
        System.out.println(config);
    }
}
