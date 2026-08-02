package academy.javaengineering.kubernetes;

import java.util.Map;

public class ServiceExample {

    public static void main(String[] args) {
        System.out.println("=== Kubernetes Service Examples ===\n");
        
        demonstrateServiceTypes();
        demonstrateClusterIP();
        demonstrateNodePort();
        demonstrateLoadBalancer();
    }

    public static void demonstrateServiceTypes() {
        System.out.println("--- Service Types ---");
        
        Map<String, String> serviceTypes = Map.of(
            "ClusterIP", "Internal only (default)",
            "NodePort", "External via Node IP",
            "LoadBalancer", "External via cloud LB",
            "ExternalName", "CNAME redirect"
        );
        
        serviceTypes.forEach((type, description) ->
            System.out.printf("  %-15s - %s%n", type, description)
        );
        System.out.println();
    }

    public static void demonstrateClusterIP() {
        System.out.println("--- ClusterIP Service ---");
        
        String clusterIPSpec = """
                apiVersion: v1
                kind: Service
                metadata:
                  name: myapp
                spec:
                  type: ClusterIP
                  selector:
                    app: myapp
                  ports:
                  - port: 80
                    targetPort: 8080
                """;
        
        System.out.println(clusterIPSpec);
    }

    public static void demonstrateNodePort() {
        System.out.println("--- NodePort Service ---");
        
        String nodePortSpec = """
                apiVersion: v1
                kind: Service
                metadata:
                  name: myapp
                spec:
                  type: NodePort
                  selector:
                    app: myapp
                  ports:
                  - port: 80
                    targetPort: 8080
                    nodePort: 30080
                """;
        
        System.out.println(nodePortSpec);
    }

    public static void demonstrateLoadBalancer() {
        System.out.println("--- LoadBalancer Service ---");
        
        String loadBalancerSpec = """
                apiVersion: v1
                kind: Service
                metadata:
                  name: myapp
                spec:
                  type: LoadBalancer
                  selector:
                    app: myapp
                  ports:
                  - name: http
                    port: 80
                    targetPort: 8080
                  - name: https
                    port: 443
                    targetPort: 8443
                """;
        
        System.out.println(loadBalancerSpec);
    }
}
