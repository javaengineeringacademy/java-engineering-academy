package academy.javaengineering.systemdesign;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalancingExample {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final String[] servers = {"server1:8080", "server2:8080", "server3:8080"};

    public static void main(String[] args) {
        System.out.println("=== Load Balancing Examples ===\n");
        LoadBalancingExample lb = new LoadBalancingExample();
        lb.demonstrateRoundRobin();
    }

    public void demonstrateRoundRobin() {
        System.out.println("--- Round Robin ---");
        for (int i = 0; i < 9; i++) {
            System.out.printf("  Request %d → %s%n", i + 1, getNextServer());
        }
        System.out.println();
    }

    public String getNextServer() {
        return servers[counter.getAndIncrement() % servers.length];
    }
}
