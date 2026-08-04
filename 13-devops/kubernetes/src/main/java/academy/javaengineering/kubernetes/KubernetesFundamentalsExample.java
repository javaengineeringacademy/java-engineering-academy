package academy.javaengineering.kubernetes;

import java.util.Map;

public class KubernetesFundamentalsExample {

    public static void main(String[] args) {
        System.out.println("=== Kubernetes Fundamentals Examples ===\n");
        
        demonstrateClusterArchitecture();
        demonstrateKubectlCommands();
        demonstrateResourceTypes();
        demonstrateLabelsAndSelectors();
    }

    public static void demonstrateClusterArchitecture() {
        System.out.println("--- Kubernetes Architecture ---");
        
        Map<String, String> masterComponents = Map.of(
            "API Server", "Frontend for control plane",
            "Scheduler", "Assigns pods to nodes",
            "Controller Manager", "Maintains desired state",
            "etcd", "Distributed key-value store"
        );
        
        Map<String, String> workerComponents = Map.of(
            "kubelet", "Node agent",
            "kube-proxy", "Network proxy",
            "Container Runtime", "Runs containers"
        );
        
        System.out.println("Master Node Components:");
        masterComponents.forEach((component, description) ->
            System.out.printf("  %-20s - %s%n", component, description)
        );
        
        System.out.println("\nWorker Node Components:");
        workerComponents.forEach((component, description) ->
            System.out.printf("  %-20s - %s%n", component, description)
        );
        System.out.println();
    }

    public static void demonstrateKubectlCommands() {
        System.out.println("--- kubectl Commands ---");
        
        String[] commands = {
            "kubectl cluster-info",
            "kubectl get nodes",
            "kubectl get pods --all-namespaces",
            "kubectl create deployment myapp --image=myapp:latest",
            "kubectl expose deployment myapp --port=8080",
            "kubectl scale deployment myapp --replicas=3",
            "kubectl apply -f deployment.yaml",
            "kubectl delete -f deployment.yaml",
            "kubectl get pods -o wide",
            "kubectl describe pod <pod-name>",
            "kubectl logs <pod-name>",
            "kubectl exec -it <pod-name> -- /bin/bash"
        };
        
        for (String command : commands) {
            System.out.println("  $ " + command);
        }
        System.out.println();
    }

    public static void demonstrateResourceTypes() {
        System.out.println("--- Kubernetes Resource Types ---");
        
        Map<String, String> resources = Map.of(
            "Pod", "Smallest deployable unit",
            "Service", "Stable network endpoint",
            "Deployment", "Declarative updates for pods",
            "ConfigMap", "Non-sensitive configuration",
            "Secret", "Sensitive configuration",
            "Ingress", "HTTP routing",
            "StatefulSet", "Stateful applications",
            "DaemonSet", "Pods on all nodes",
            "Job", "One-time tasks",
            "CronJob", "Scheduled tasks"
        );
        
        resources.forEach((resource, description) ->
            System.out.printf("  %-15s - %s%n", resource, description)
        );
        System.out.println();
    }

    public static void demonstrateLabelsAndSelectors() {
        System.out.println("--- Labels and Selectors ---");
        
        String labelYaml = """
                metadata:
                  labels:
                    app: myapp
                    version: 2.0.0
                    environment: production
                
                spec:
                  selector:
                    matchLabels:
                      app: myapp
                """;
        
        System.out.println(labelYaml);
    }
}
