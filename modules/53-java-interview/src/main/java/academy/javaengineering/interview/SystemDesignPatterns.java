package academy.javaengineering.interview;

import java.util.*;

/**
 * Demonstrates system design patterns for interviews.
 */
public class SystemDesignPatterns {

    public interface LoadBalancer {
        String getServer();
    }

    public static class RoundRobinLoadBalancer implements LoadBalancer {
        private final List<String> servers;
        private int currentIndex = 0;

        public RoundRobinLoadBalancer(List<String> servers) {
            this.servers = servers;
        }

        @Override
        public synchronized String getServer() {
            String server = servers.get(currentIndex);
            currentIndex = (currentIndex + 1) % servers.size();
            return server;
        }
    }

    public static class RateLimiter {
        private final int maxRequests;
        private final long windowMs;
        private final Map<String, List<Long>> requestCounts = new HashMap<>();

        public RateLimiter(int maxRequests, long windowMs) {
            this.maxRequests = maxRequests;
            this.windowMs = windowMs;
        }

        public synchronized boolean allowRequest(String clientId) {
            long now = System.currentTimeMillis();
            List<Long> timestamps = requestCounts.computeIfAbsent(clientId, k -> new ArrayList<>());
            
            timestamps.removeIf(time -> now - time > windowMs);
            
            if (timestamps.size() >= maxRequests) {
                return false;
            }
            
            timestamps.add(now);
            return true;
        }
    }
}
