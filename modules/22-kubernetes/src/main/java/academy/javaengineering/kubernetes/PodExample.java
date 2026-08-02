package academy.javaengineering.kubernetes;

import java.util.Map;

public class PodExample {

    public static void main(String[] args) {
        System.out.println("=== Pod Examples ===\n");
        
        demonstratePodSpec();
        demonstrateInitContainers();
        demonstrateSidecarPattern();
        demonstrateResourceLimits();
    }

    public static void demonstratePodSpec() {
        System.out.println("--- Pod Specification ---");
        
        String podSpec = """
                apiVersion: v1
                kind: Pod
                metadata:
                  name: myapp
                  labels:
                    app: myapp
                spec:
                  containers:
                  - name: myapp
                    image: myapp:latest
                    ports:
                    - containerPort: 8080
                """;
        
        System.out.println(podSpec);
    }

    public static void demonstrateInitContainers() {
        System.out.println("--- Init Containers ---");
        
        String initContainerSpec = """
                apiVersion: v1
                kind: Pod
                metadata:
                  name: myapp
                spec:
                  initContainers:
                  - name: wait-for-db
                    image: busybox:1.36
                    command: ['sh', '-c', 'until nslookup db; do sleep 2; done']
                  
                  containers:
                  - name: myapp
                    image: myapp:latest
                """;
        
        System.out.println(initContainerSpec);
    }

    public static void demonstrateSidecarPattern() {
        System.out.println("--- Sidecar Pattern ---");
        
        String sidecarSpec = """
                containers:
                - name: myapp
                  image: myapp:latest
                  ports:
                  - containerPort: 8080
                
                - name: log-shipper
                  image: fluent/fluent-bit:latest
                  volumeMounts:
                  - name: logs
                    mountPath: /var/log
                
                volumes:
                - name: logs
                  emptyDir: {}
                """;
        
        System.out.println(sidecarSpec);
    }

    public static void demonstrateResourceLimits() {
        System.out.println("--- Resource Limits ---");
        
        String resourceSpec = """
                resources:
                  requests:
                    memory: "256Mi"
                    cpu: "250m"
                  limits:
                    memory: "512Mi"
                    cpu: "500m"
                """;
        
        System.out.println(resourceSpec);
        
        Map<String, String> resourceMeaning = Map.of(
            "requests", "Minimum resources guaranteed",
            "limits", "Maximum resources allowed",
            "memory", "RAM allocation",
            "cpu", "CPU allocation (1000m = 1 core)"
        );
        
        resourceMeaning.forEach((key, value) ->
            System.out.printf("  %-12s - %s%n", key, value)
        );
        System.out.println();
    }
}
