package academy.javaengineering.kubernetes;

import java.util.Map;

public class DeploymentExample {

    public static void main(String[] args) {
        System.out.println("=== Kubernetes Deployment Examples ===\n");
        
        demonstrateDeploymentSpec();
        demonstrateRollingUpdate();
        demonstrateScaling();
        demonstrateRollback();
    }

    public static void demonstrateDeploymentSpec() {
        System.out.println("--- Deployment Specification ---");
        
        String deploymentSpec = """
                apiVersion: apps/v1
                kind: Deployment
                metadata:
                  name: myapp
                spec:
                  replicas: 3
                  selector:
                    matchLabels:
                      app: myapp
                  template:
                    metadata:
                      labels:
                        app: myapp
                    spec:
                      containers:
                      - name: myapp
                        image: myapp:2.0.0
                        ports:
                        - containerPort: 8080
                """;
        
        System.out.println(deploymentSpec);
    }

    public static void demonstrateRollingUpdate() {
        System.out.println("--- Rolling Update Strategy ---");
        
        String rollingUpdateSpec = """
                spec:
                  strategy:
                    type: RollingUpdate
                    rollingUpdate:
                      maxSurge: 1
                      maxUnavailable: 0
                """;
        
        System.out.println(rollingUpdateSpec);
        
        Map<String, String> rollingUpdateParams = Map.of(
            "maxSurge", "Maximum extra pods during update",
            "maxUnavailable", "Maximum unavailable pods during update"
        );
        
        rollingUpdateParams.forEach((param, description) ->
            System.out.printf("  %-20s - %s%n", param, description)
        );
        System.out.println();
    }

    public static void demonstrateScaling() {
        System.out.println("--- Scaling Commands ---");
        
        String[] commands = {
            "kubectl scale deployment myapp --replicas=5",
            "kubectl autoscale deployment myapp --min=3 --max=10 --cpu-percent=70",
            "kubectl get hpa"
        };
        
        for (String command : commands) {
            System.out.println("  $ " + command);
        }
        System.out.println();
    }

    public static void demonstrateRollback() {
        System.out.println("--- Rollback Commands ---");
        
        String[] commands = {
            "kubectl rollout status deployment myapp",
            "kubectl rollout history deployment myapp",
            "kubectl rollout undo deployment myapp",
            "kubectl rollout undo deployment myapp --to-revision=2"
        };
        
        for (String command : commands) {
            System.out.println("  $ " + command);
        }
        System.out.println();
    }
}
