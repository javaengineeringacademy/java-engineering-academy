package war.stories;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * WAR STORY: High Latency - GC Pause Investigation
 * 
 * Scenario: A trading platform experienced intermittent 500ms latency spikes.
 * The spikes occurred every 30 seconds and coincided with Full GC cycles.
 * The application used G1GC with default settings on a 16GB heap.
 * 
 * Investigation Process:
 * 1. Enable JFR recording: jcmd <pid> JFR.start duration=60s filename=recording.jfr
 * 2. Analyze GC events in JDK Mission Control
 * 3. Check GC pause times and frequency
 * 4. Identify allocation hotspots
 * 
 * Root Cause: G1GC was triggering Full GC every 30 seconds due to:
 * - Large heap (16GB) with high allocation rate
 * - Default IHOP (45%) causing late concurrent marking
 * - Mixed collections couldn't keep up with old gen growth
 */
public class HighLatencyStory {

    // Simulated trading data
    static class Trade {
        private final long timestamp;
        private final String symbol;
        private final double price;
        private final int quantity;
        private final byte[] marketData; // Simulated market data payload

        public Trade(String symbol, double price, int quantity) {
            this.timestamp = System.currentTimeMillis();
            this.symbol = symbol;
            this.price = price;
            this.quantity = quantity;
            this.marketData = new byte[1024]; // 1KB per trade
        }
    }

    // BUGGY VERSION: High allocation rate causing GC pressure
    static class TradingEngineBuggy {
        private final Random random = new Random();
        private int tradesProcessed = 0;

        public Trade processTrade(String symbol) {
            // BUG: Creating new Trade object for every tick
            // With 100K ticks/second, this creates 100K objects/sec
            double price = 100.0 + random.nextGaussian() * 5;
            int quantity = random.nextInt(1000) + 1;
            
            Trade trade = new Trade(symbol, price, quantity);
            tradesProcessed++;
            
            // BUG: Also creating temporary strings and collections
            String logMessage = "Processed trade: " + symbol + " @ " + price + 
                " qty=" + quantity + " total=" + (price * quantity);
            System.out.println(logMessage);
            
            return trade;
        }
    }

    // FIXED VERSION: Object pooling and reduced allocations
    static class TradingEngineFixed {
        private final Random random = new Random();
        private int tradesProcessed = 0;
        
        // Reuse objects instead of creating new ones
        private final Trade[] tradePool = new Trade[1000];
        private int poolIndex = 0;

        public Trade processTrade(String symbol) {
            // Reuse trade object from pool
            Trade trade = tradePool[poolIndex % tradePool.length];
            if (trade == null) {
                trade = new Trade(symbol, 0, 0);
                tradePool[poolIndex % tradePool.length] = trade;
            }
            
            // Update existing object instead of creating new one
            // Note: In real code, you'd use mutable fields or builder pattern
            double price = 100.0 + random.nextGaussian() * 5;
            int quantity = random.nextInt(1000) + 1;
            
            poolIndex++;
            tradesProcessed++;
            
            // Use StringBuilder for string concatenation in hot paths
            StringBuilder sb = new StringBuilder(100);
            sb.append("Processed trade: ").append(symbol)
              .append(" @ ").append(price)
              .append(" qty=").append(quantity);
            System.out.println(sb.toString());
            
            return trade;
        }
    }

    // BETTER VERSION: Use primitive collections and avoid boxing
    // import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMap;
    // import org.eclipse.collections.impl.list.mutable.FastList;
    
    // Production version would use:
    // - Eclipse Collections for primitive-heavy workloads
    // - Object pooling for frequently created objects
    // - Off-heap storage for large market data
    // - Ring buffers for streaming data

    public static void main(String[] args) {
        System.out.println("=== High Latency War Story ===\n");
        
        // Demonstrate the allocation problem
        System.out.println("--- Demonstrating GC Pressure (Buggy Version) ---");
        TradingEngineBuggy buggy = new TradingEngineBuggy();
        
        long startTime = System.currentTimeMillis();
        int tradesToProcess = 100_000;
        
        for (int i = 0; i < tradesToProcess; i++) {
            buggy.processTrade("AAPL");
            
            if (i % 10_000 == 0) {
                System.out.println("Processed " + i + " trades...");
            }
        }
        
        long buggyTime = System.currentTimeMillis() - startTime;
        System.out.println("Buggy version completed in: " + buggyTime + "ms\n");
        
        // Show the fix
        System.out.println("--- Demonstrating Optimized Version ---");
        TradingEngineFixed fixed = new TradingEngineFixed();
        
        startTime = System.currentTimeMillis();
        
        for (int i = 0; i < tradesToProcess; i++) {
            fixed.processTrade("AAPL");
            
            if (i % 10_000 == 0) {
                System.out.println("Processed " + i + " trades...");
            }
        }
        
        long fixedTime = System.currentTimeMillis() - startTime;
        System.out.println("Fixed version completed in: " + fixedTime + "ms\n");
        
        // Print JVM tuning recommendations
        printJVMTuningGuide();
    }

    private static void printJVMTuningGuide() {
        System.out.println("=== GC Tuning Guide ===");
        System.out.println("\n1. JFR Analysis Commands:");
        System.out.println("   # Start JFR recording");
        System.out.println("   jcmd <pid> JFR.start name=profile duration=60s filename=profile.jfr");
        System.out.println("   # Or use continuous recording");
        System.out.println("   jcmd <pid> JFR.start name=continuous settings=profile");
        System.out.println("\n2. GC Logging (Java 11+):");
        System.out.println("   -Xlog:gc*:file=gc.log:time,uptime,level,tags");
        System.out.println("   -Xlog:gc+heap=debug");
        System.out.println("   -Xlog:gc+phases=debug");
        System.out.println("\n3. G1GC Tuning:");
        System.out.println("   -XX:+UseG1GC");
        System.out.println("   -XX:MaxGCPauseMillis=200          # Target max pause");
        System.out.println("   -XX:InitiatingHeapOccupancyPercent=45  # Start concurrent GC earlier");
        System.out.println("   -XX:G1HeapRegionSize=16m          # Larger regions for large objects");
        System.out.println("   -XX:G1MixedGCCountTarget=8        # Spread mixed GC over more cycles");
        System.out.println("\n4. ZGC (Java 15+):");
        System.out.println("   -XX:+UseZGC");
        System.out.println("   -XX:+ZGenerational               # Enable generational ZGC (Java 21+)");
        System.out.println("   -XX:SoftMaxHeapSize=8g           # Soft limit for ZGC");
        System.out.println("   ZGC provides sub-millisecond pauses regardless of heap size");
        System.out.println("\n5. Allocation Rate Reduction:");
        System.out.println("   - Use object pooling for hot paths");
        System.out.println("   - Avoid autoboxing in tight loops");
        System.out.println("   - Use StringBuilder for string concatenation");
        System.out.println("   - Prefer primitive collections (Eclipse Collections)");
        System.out.println("   - Consider off-heap storage for large objects");
        System.out.println("\n6. Monitoring:");
        System.out.println("   - jstat -gc <pid> 1000           # GC stats every second");
        System.out.println("   - jcmd <pid> GC.heap_info       # Heap information");
        System.out.println("   - VisualVM or JMC for real-time monitoring");
        System.out.println("   - Alert on GC pause > 500ms");
    }
}
