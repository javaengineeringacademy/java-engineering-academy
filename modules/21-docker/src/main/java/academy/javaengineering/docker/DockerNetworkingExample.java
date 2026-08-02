package academy.javaengineering.docker;

import java.util.Map;

public class DockerNetworkingExample {

    public static void main(String[] args) {
        System.out.println("=== Docker Networking Examples ===\n");
        
        demonstrateNetworkTypes();
        demonstrateBridgeNetworking();
        demonstrateDnsResolution();
        demonstrateNetworkSecurity();
        demonstrateOverlayNetworks();
    }

    public static void demonstrateNetworkTypes() {
        System.out.println("--- Network Types ---");
        
        Map<String, String> networkTypes = Map.of(
            "bridge", "Default, single-host communication",
            "host", "Maximum performance, no isolation",
            "overlay", "Multi-host communication (Swarm)",
            "macvlan", "Direct L2 network access",
            "none", "Complete network isolation"
        );
        
        networkTypes.forEach((type, description) ->
            System.out.printf("  %-10s - %s%n", type, description)
        );
        System.out.println();
    }

    public static void demonstrateBridgeNetworking() {
        System.out.println("--- Bridge Network Architecture ---");
        
        String architecture = """
                Host Machine
                ├── docker0 (Bridge: 172.17.0.1)
                │   ├── Container A (172.17.0.2)
                │   │   └── eth0 → veth → docker0
                │   └── Container B (172.17.0.3)
                │       └── eth0 → veth → docker0
                └── iptables (NAT rules)
                """;
        
        System.out.println(architecture);
    }

    public static void demonstrateDnsResolution() {
        System.out.println("--- DNS Resolution ---");
        
        String dnsExample = """
                # Containers resolve each other by name
                docker run -d --name web --network mynet nginx
                docker run -d --name app --network mynet myapp
                
                # From app container, resolve web:
                $ nslookup web
                Name:      web
                Address:   172.20.0.2
                """;
        
        System.out.println(dnsExample);
    }

    public static void demonstrateNetworkSecurity() {
        System.out.println("--- Network Security ---");
        
        String[] securityPractices = {
            "1. Use internal networks for databases",
            "2. Implement network segmentation",
            "3. Use encrypted overlay networks",
            "4. Restrict port exposure",
            "5. Implement firewall rules"
        };
        
        for (String practice : securityPractices) {
            System.out.println("  " + practice);
        }
        System.out.println();
    }

    public static void demonstrateOverlayNetworks() {
        System.out.println("--- Overlay Network Configuration ---");
        
        String overlayConfig = """
                version: '3.8'
                
                services:
                  web:
                    image: nginx:alpine
                    networks:
                      - frontend
                
                  app:
                    build: .
                    networks:
                      - frontend
                      - backend
                
                  db:
                    image: postgres:15
                    networks:
                      - backend
                
                networks:
                  frontend:
                  backend:
                    driver: overlay
                    encrypted: true
                """;
        
        System.out.println(overlayConfig);
    }
}
