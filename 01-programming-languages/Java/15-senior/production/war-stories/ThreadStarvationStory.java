package war.stories;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WAR STORY: Thread Starvation - The Silent Queue Overflow
 * 
 * Scenario: An API gateway started timing out requests after 30 seconds.
 * The gateway had 200 threads, but only 20 were available for processing.
 * Users experienced timeouts on 80% of requests during peak traffic.
 * 
 * Investigation Process:
 * 1. Check thread pool metrics: active threads, queue size, completed tasks
 * 2. Analyze thread dumps for blocked/waiting threads
 * 3. Identify slow downstream services consuming threads
 * 4. Measure actual latency distribution per endpoint
 * 
 * Root Cause: The gateway used a single fixed thread pool for all operations.
 * A downstream recommendation service started responding slowly (5-10 seconds).
 * The 200 threads were quickly consumed by slow requests, leaving no threads
 * for fast operations (health checks, authentication, cached responses).
 */
public class ThreadStarvationStory {

    // Simulated slow downstream service
    static class RecommendationService {
        private final AtomicInteger requestCount = new AtomicInteger(0);
        
        public String getRecommendations(String userId) throws InterruptedException {
            requestCount.incrementAndGet();
            // Simulate slow response (5-10 seconds)
            long sleepTime = 5000 + (long) (Math.random() * 5000);
            Thread.sleep(sleepTime);
            return "Recommendations for " + userId;
        }
    }

    // Simulated fast service
    static class AuthService {
        public boolean authenticate(String token) throws InterruptedException {
            // Fast response (10ms)
            Thread.sleep(10);
            return token != null && !token.isEmpty();
        }
    }

    // BUGGY VERSION: Single thread pool for everything
    static class GatewayBuggy {
        private final ExecutorService executor = Executors.newFixedThreadPool(200);
        private final RecommendationService recommendationService = new RecommendationService();
        private final AuthService authService = new AuthService();
        
        public String handleRequest(String userId, String token) throws Exception {
            // All requests compete for same 200 threads
            Future<String> future = executor.submit(() -> {
                // Slow operation consumes thread for 5-10 seconds
                if (userId != null) {
                    return recommendationService.getRecommendations(userId);
                }
                return "No recommendations";
            });
            
            // This will timeout if all threads are busy
            try {
                return future.get(30, TimeUnit.SECONDS);
            } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                throw new RuntimeException("Request timed out", e);
            }
        }
        
        public void shutdown() {
            executor.shutdown();
        }
    }

    // FIXED VERSION: Separate thread pools by operation type
    static class GatewayFixed {
        private final ExecutorService fastPool = Executors.newFixedThreadPool(50);   // Health, auth, cache
        private final ExecutorService slowPool = Executors.newFixedThreadPool(100);  // External calls
        private final ExecutorService criticalPool = Executors.newFixedThreadPool(20); // Payment, orders
        
        private final RecommendationService recommendationService = new RecommendationService();
        private final AuthService authService = new AuthService();
        
        public String handleRequest(String userId, String token) throws Exception {
            // Route to appropriate pool based on operation type
            if (token != null && userId == null) {
                // Fast operation: authentication
                Future<Boolean> authFuture = fastPool.submit(() -> 
                    authService.authenticate(token)
                );
                Boolean result = authFuture.get(1, TimeUnit.SECONDS);
                return result ? "Authenticated" : "Invalid token";
            } else if (userId != null) {
                // Slow operation: recommendation service
                Future<String> slowFuture = slowPool.submit(() -> 
                    recommendationService.getRecommendations(userId)
                );
                return slowFuture.get(10, TimeUnit.SECONDS);
            } else {
                // Critical operation
                Future<String> criticalFuture = criticalPool.submit(() -> 
                    "Critical operation result"
                );
                return criticalFuture.get(5, TimeUnit.SECONDS);
            }
        }
        
        public void shutdown() {
            fastPool.shutdown();
            slowPool.shutdown();
            criticalPool.shutdown();
        }
    }

    // BETTER VERSION: Virtual threads (Java 21+)
    static class GatewayBetter {
        // Virtual threads scale to millions, no thread pool sizing needed
        private final ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
        
        private final RecommendationService recommendationService = new RecommendationService();
        private final AuthService authService = new AuthService();
        
        public String handleRequest(String userId, String token) throws Exception {
            // Virtual threads handle I/O-bound work efficiently
            Future<String> future = virtualPool.submit(() -> {
                if (token != null && userId == null) {
                    return authService.authenticate(token) ? "Authenticated" : "Invalid token";
                } else if (userId != null) {
                    return recommendationService.getRecommendations(userId);
                } else {
                    return "Result";
                }
            });
            
            return future.get(30, TimeUnit.SECONDS);
        }
        
        public void shutdown() {
            virtualPool.shutdown();
        }
    }

    // PRODUCTION VERSION: With circuit breaker and timeout
    static class GatewayProduction {
        private final ExecutorService fastPool = Executors.newFixedThreadPool(50);
        private final ExecutorService slowPool = Executors.newFixedThreadPool(100);
        private final ExecutorService criticalPool = Executors.newFixedThreadPool(20);
        
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private volatile boolean circuitOpen = false;
        private volatile long circuitOpenedAt = 0;
        
        private static final int FAILURE_THRESHOLD = 10;
        private static final long CIRCUIT_RESET_TIMEOUT = 30000; // 30 seconds
        
        private final RecommendationService recommendationService = new RecommendationService();
        private final AuthService authService = new AuthService();
        
        public String handleRequest(String userId, String token) throws Exception {
            // Check circuit breaker
            if (circuitOpen) {
                if (System.currentTimeMillis() - circuitOpenedAt > CIRCUIT_RESET_TIMEOUT) {
                    circuitOpen = false;
                    failureCount.set(0);
                    System.out.println("Circuit breaker reset");
                } else {
                    throw new RuntimeException("Circuit breaker is open, failing fast");
                }
            }
            
            try {
                if (token != null && userId == null) {
                    Future<Boolean> authFuture = fastPool.submit(() -> 
                        authService.authenticate(token)
                    );
                    Boolean result = authFuture.get(1, TimeUnit.SECONDS);
                    return result ? "Authenticated" : "Invalid token";
                } else if (userId != null) {
                    Future<String> slowFuture = slowPool.submit(() -> 
                        recommendationService.getRecommendations(userId)
                    );
                    String result = slowFuture.get(10, TimeUnit.SECONDS);
                    failureCount.set(0); // Reset on success
                    return result;
                } else {
                    Future<String> criticalFuture = criticalPool.submit(() -> 
                        "Critical operation result"
                    );
                    return criticalFuture.get(5, TimeUnit.SECONDS);
                }
            } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                int failures = failureCount.incrementAndGet();
                if (failures >= FAILURE_THRESHOLD) {
                    circuitOpen = true;
                    circuitOpenedAt = System.currentTimeMillis();
                    System.out.println("Circuit breaker opened after " + failures + " failures");
                }
                throw e;
            }
        }
        
        public void shutdown() {
            fastPool.shutdown();
            slowPool.shutdown();
            criticalPool.shutdown();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Thread Starvation War Story ===\n");
        
        // Demonstrate the thread starvation problem
        System.out.println("--- Simulating Thread Starvation (Buggy Version) ---");
        GatewayBuggy buggyGateway = new GatewayBuggy();
        
        long startTime = System.currentTimeMillis();
        int totalRequests = 50;
        int successfulRequests = 0;
        int failedRequests = 0;
        
        // Simulate concurrent requests
        Thread[] threads = new Thread[totalRequests];
        for (int i = 0; i < totalRequests; i++) {
            final int requestId = i;
            threads[i] = new Thread(() -> {
                try {
                    String result = buggyGateway.handleRequest("user-" + requestId, null);
                    System.out.println("Request " + requestId + " succeeded: " + result);
                } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                    System.out.println("Request " + requestId + " failed: " + e.getMessage());
                }
            });
        }
        
        // Start all threads
        for (Thread t : threads) {
            t.start();
            try {
                Thread.sleep(100); // Small delay between requests
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Wait for completion
        for (Thread t : threads) {
            try {
                t.join(35000); // 35 second timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long buggyTime = System.currentTimeMillis() - startTime;
        System.out.println("\nBuggy gateway completed in: " + buggyTime + "ms");
        
        buggyGateway.shutdown();
        
        // Show the fix
        System.out.println("\n--- Demonstrating Fixed Version (Separate Thread Pools) ---");
        GatewayFixed fixedGateway = new GatewayFixed();
        
        startTime = System.currentTimeMillis();
        
        // Simulate mixed workload: 80% fast, 20% slow
        Thread[] fixedThreads = new Thread[totalRequests];
        for (int i = 0; i < totalRequests; i++) {
            final int requestId = i;
            fixedThreads[i] = new Thread(() -> {
                try {
                    // 80% fast requests, 20% slow requests
                    String userId = (requestId % 5 == 0) ? "user-" + requestId : null;
                    String token = "token-" + requestId;
                    
                    String result = fixedGateway.handleRequest(userId, token);
                    System.out.println("Request " + requestId + " succeeded: " + result);
                } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
                    System.out.println("Request " + requestId + " failed: " + e.getMessage());
                }
            });
        }
        
        for (Thread t : fixedThreads) {
            t.start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        for (Thread t : fixedThreads) {
            try {
                t.join(35000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        long fixedTime = System.currentTimeMillis() - startTime;
        System.out.println("\nFixed gateway completed in: " + fixedTime + "ms");
        
        fixedGateway.shutdown();
        
        // Print investigation checklist
        printInvestigationChecklist();
    }

    private static void printInvestigationChecklist() {
        System.out.println("\n=== Thread Starvation Investigation Checklist ===");
        System.out.println("1. Thread pool monitoring:");
        System.out.println("   - Active threads vs pool size");
        System.out.println("   - Queue size and rejection count");
        System.out.println("   - Task completion rate");
        System.out.println("\n2. Thread dump analysis:");
        System.out.println("   jstack <pid> | grep -A 5 'BLOCKED\\|WAITING'");
        System.out.println("   Look for threads waiting on thread pool submission");
        System.out.println("\n3. Identify slow consumers:");
        System.out.println("   - Profile downstream service latencies");
        System.out.println("   - Check for thread pool contention");
        System.out.println("   - Monitor queue depth over time");
        System.out.println("\n4. Thread pool sizing guidelines:");
        System.out.println("   CPU-bound: N CPU cores");
        System.out.println("   I/O-bound: N * (1 + wait_time/service_time)");
        System.out.println("   Mixed: Use separate pools per operation type");
        System.out.println("\n5. Prevention strategies:");
        System.out.println("   - Separate thread pools by latency class");
        System.out.println("   - Use virtual threads for I/O-bound work (Java 21+)");
        System.out.println("   - Implement circuit breakers for slow services");
        System.out.println("   - Set aggressive timeouts for non-critical operations");
        System.out.println("   - Add backpressure to prevent queue overflow");
        System.out.println("\n6. Monitoring:");
        System.out.println("   - Alert on thread pool utilization > 80%");
        System.out.println("   - Alert on queue depth > threshold");
        System.out.println("   - Track task rejection rate");
        System.out.println("   - Monitor average task execution time");
    }
}
