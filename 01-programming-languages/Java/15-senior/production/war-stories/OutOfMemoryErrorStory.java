package war.stories;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * WAR STORY: OutOfMemoryError - The Silent Memory Leak
 * 
 * Scenario: A caching service started crashing every 4 hours during peak traffic.
 * The heap dump showed 95% old generation usage, but no single object dominated.
 * 
 * Investigation Process:
 * 1. Enable heap dump on OOM: -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdumps
 * 2. Use Eclipse MAT or VisualVM to analyze heap dump
 * 3. Look for dominator tree - find objects consuming most memory
 * 4. Check for unusual object retention patterns
 * 
 * Root Cause: A custom cache stored user session objects but never evicted expired entries.
 * Each session was 50KB, and with 100K active users, the cache grew to 5GB over time.
 */
public class OutOfMemoryErrorStory {

    // BUGGY VERSION: This is what caused the production OOM
    static class LeakySessionCache {
        // ConcurrentHashMap doesn't automatically remove expired entries!
        private final ConcurrentMap<String, UserSession> sessions = new ConcurrentHashMap<>();
        
        public void putSession(String sessionId, UserSession session) {
            sessions.put(sessionId, session);
            // BUG: No TTL, no eviction, no cleanup!
        }
        
        public UserSession getSession(String sessionId) {
            return sessions.get(sessionId);
            // BUG: Never checks if session is expired!
        }
        
        public int getActiveSessions() {
            return sessions.size();
        }
    }

    static class UserSession {
        private final String sessionId;
        private final long createdAt;
        private final long expiresAt;
        private final byte[] userData; // 50KB per session
        private final List<String> permissions;
        
        public UserSession(String sessionId, long ttlMillis) {
            this.sessionId = sessionId;
            this.createdAt = System.currentTimeMillis();
            this.expiresAt = createdAt + ttlMillis;
            this.userData = new byte[50 * 1024]; // 50KB allocation
            this.permissions = new ArrayList<>();
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }

    // FIXED VERSION: Proper cache with TTL and eviction
    static class FixedSessionCache {
        private final ConcurrentMap<String, UserSession> sessions = new ConcurrentHashMap<>();
        private final ScheduledExecutorService cleanupExecutor = 
            Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "session-cleanup");
                t.setDaemon(true);
                return t;
            });
        
        public FixedSessionCache() {
            // Run cleanup every 60 seconds
            cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredSessions, 
                60, 60, TimeUnit.SECONDS);
        }
        
        public void putSession(String sessionId, UserSession session) {
            sessions.put(sessionId, session);
        }
        
        public UserSession getSession(String sessionId) {
            UserSession session = sessions.get(sessionId);
            if (session != null && session.isExpired()) {
                sessions.remove(sessionId);
                return null;
            }
            return session;
        }
        
        private void cleanupExpiredSessions() {
            int removed = 0;
            for (var entry : sessions.entrySet()) {
                if (entry.getValue().isExpired()) {
                    sessions.remove(entry.getKey());
                    removed++;
                }
            }
            if (removed > 0) {
                System.out.println("Cleaned up " + removed + " expired sessions");
            }
        }
        
        public int getActiveSessions() {
            return sessions.size();
        }
        
        public void shutdown() {
            cleanupExecutor.shutdown();
        }
    }

    // BETTER VERSION: Use Caffeine cache with built-in TTL and eviction
    // import com.github.benmanes.caffeine.cache.Cache;
    // import com.github.benmanes.caffeine.cache.Caffeine;
    //
    // Cache<String, UserSession> sessions = Caffeine.newBuilder()
    //     .maximumSize(100_000)
    //     .expireAfterAccess(Duration.ofMinutes(30))
    //     .recordStats()
    //     .build();

    public static void main(String[] args) {
        System.out.println("=== OutOfMemoryError War Story ===\n");
        
        // Simulate the memory leak scenario
        System.out.println("--- Simulating Memory Leak (Buggy Version) ---");
        LeakySessionCache leakyCache = new LeakySessionCache();
        
        // Simulate 100K sessions being created over time
        for (int i = 0; i < 100_000; i++) {
            String sessionId = "session-" + i;
            UserSession session = new UserSession(sessionId, 30 * 60 * 1000); // 30 min TTL
            leakyCache.putSession(sessionId, session);
            
            if (i % 10_000 == 0) {
                printMemoryUsage("After adding " + i + " sessions");
            }
        }
        
        System.out.println("\nBuggy cache size: " + leakyCache.getActiveSessions());
        System.out.println("PROBLEM: Sessions never evicted, memory grows unbounded!\n");
        
        // Show the fix
        System.out.println("--- Demonstrating Fixed Version ---");
        FixedSessionCache fixedCache = new FixedSessionCache();
        
        for (int i = 0; i < 10_000; i++) {
            String sessionId = "session-" + i;
            UserSession session = new UserSession(sessionId, 100); // 100ms TTL for demo
            fixedCache.putSession(sessionId, session);
        }
        
        System.out.println("Fixed cache size before cleanup: " + fixedCache.getActiveSessions());
        
        // Wait for cleanup to run
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Fixed cache size after cleanup: " + fixedCache.getActiveSessions());
        fixedCache.shutdown();
        
        // Print investigation checklist
        printInvestigationChecklist();
    }

    private static void printMemoryUsage(String label) {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        long usedMB = heapUsage.getUsed() / (1024 * 1024);
        long maxMB = heapUsage.getMax() / (1024 * 1024);
        System.out.println(label + ": Heap used = " + usedMB + "MB / " + maxMB + "MB");
    }

    private static void printInvestigationChecklist() {
        System.out.println("\n=== OOM Investigation Checklist ===");
        System.out.println("1. Enable heap dump on OOM:");
        System.out.println("   -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdumps");
        System.out.println("\n2. Analyze heap dump with Eclipse MAT:");
        System.out.println("   - Run Leak Suspects report");
        System.out.println("   - Check Dominator Tree for largest objects");
        System.out.println("   - Look for collections with unexpected sizes");
        System.out.println("\n3. Common memory leak patterns:");
        System.out.println("   - Collections that grow but never shrink");
        System.out.println("   - Static references to large objects");
        System.out.println("   - Unclosed resources (streams, connections)");
        System.out.println("   - ThreadLocal variables not cleaned up");
        System.out.println("   - Cache without eviction policy");
        System.out.println("\n4. Prevention strategies:");
        System.out.println("   - Use bounded caches with TTL");
        System.out.println("   - Enable leak detection in connection pools");
        System.out.println("   - Monitor heap usage trends");
        System.out.println("   - Set up alerts for old gen occupancy > 80%");
        System.out.println("   - Regular profiling in staging environment");
    }
}
