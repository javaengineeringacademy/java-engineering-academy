# Architecture Examples

## Example 1: Basic Cluster Setup

### Configuration Files

```properties
# zoo.cfg for 3-node cluster

tickTime=2000
initLimit=10
syncLimit=5
dataDir=/var/lib/zookeeper
clientPort=2181

server.1=zk1.example.com:2888:3888
server.2=zk2.example.com:2888:3888
server.3=zk3.example.com:2888:3888
```

```bash
# Node 1 (zk1)
echo "1" > /var/lib/zookeeper/myid
zkServer.sh start

# Node 2 (zk2)
echo "2" > /var/lib/zookeeper/myid
zkServer.sh start

# Node 3 (zk3)
echo "3" > /var/lib/zookeeper/myid
zkServer.sh start

# Check status
zkServer.sh status
```

---

## Example 2: Docker Compose Cluster

```yaml
# docker-compose.yml
version: '3.8'

services:
  zk1:
    image: zookeeper:3.8
    hostname: zk1
    ports:
      - "2181:2181"
      - "2888:2888"
      - "3888:3888"
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5

  zk2:
    image: zookeeper:3.8
    hostname: zk2
    ports:
      - "2182:2181"
      - "2889:2888"
      - "3889:3888"
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5

  zk3:
    image: zookeeper:3.8
    hostname: zk3
    ports:
      - "2183:2181"
      - "2890:2888"
      - "3890:3888"
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zk1:2888:3888;2181 server.2=zk2:2888:3888;2181 server.3=zk3:2888:3888;2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
```

---

## Example 3: ZAB Protocol Simulation

```java
package academy.messaging.zookeeper.architecture.examples;

import java.util.*;
import java.util.concurrent.*;

public class ZABSimulation {
    
    private final int serverId;
    private final List<Integer> peers;
    private long currentEpoch = 0;
    private long lastZxid = 0;
    private ServerState state;
    private volatile boolean running = true;
    
    enum ServerState {
        LOOKING,
        FOLLOWING,
        LEADING
    }
    
    public ZABSimulation(int serverId, List<Integer> peers) {
        this.serverId = serverId;
        this.peers = peers;
        this.state = ServerState.LOOKING;
    }
    
    // Phase 1: Leader Election
    public synchronized int conductElection() {
        System.out.println("Server " + serverId + " starting leader election");
        
        // Fast Leader Election
        Vote myVote = new Vote(serverId, lastZxid);
        Map<Integer, Vote> votes = new HashMap<>();
        votes.put(serverId, myVote);
        
        // Simulate voting
        for (int peer : peers) {
            if (peer != serverId) {
                Vote peerVote = new Vote(peer, lastZxid);
                votes.put(peer, peerVote);
            }
        }
        
        // Determine winner (highest zxid, then highest id)
        Vote winner = votes.values().stream()
            .max(Comparator.comparingLong(Vote::getZxid)
                .thenComparingInt(Vote::getServerId))
            .orElse(myVote);
        
        System.out.println("Server " + serverId + " elected " + winner.getServerId());
        return winner.getServerId();
    }
    
    // Phase 2: Discovery
    public synchronized void discovery(int leaderId) {
        System.out.println("Server " + serverId + " discovering from leader " + leaderId);
        currentEpoch++;
        System.out.println("Server " + serverId + " now at epoch " + currentEpoch);
    }
    
    // Phase 3: Synchronization
    public synchronized void synchronization(byte[] snapshot, List<Long> transactionLog) {
        System.out.println("Server " + serverId + " synchronizing...");
        // Apply snapshot
        // Replay transaction log
        lastZxid = transactionLog.isEmpty() ? 0 : 
                   transactionLog.get(transactionLog.size() - 1);
        System.out.println("Server " + serverId + " synchronized to zxid " + lastZxid);
    }
    
    // Phase 4: Broadcast
    public synchronized void broadcast(String operation) {
        long proposalZxid = (currentEpoch << 32) | (lastZxid + 1);
        System.out.println("Server " + serverId + " proposing zxid " + 
                           proposalZxid + ": " + operation);
        
        // Wait for quorum
        if (acknowledgeProposal(proposalZxid)) {
            commit(proposalZxid, operation);
            lastZxid++;
        }
    }
    
    private boolean acknowledgeProposal(long zxid) {
        // Simulate waiting for quorum
        System.out.println("Server " + serverId + " received quorum for zxid " + zxid);
        return true;
    }
    
    private void commit(long zxid, String operation) {
        System.out.println("Server " + serverId + " committing zxid " + zxid);
    }
    
    public static void main(String[] args) {
        List<Integer> cluster = Arrays.asList(1, 2, 3);
        
        List<ZABSimulation> servers = new ArrayList<>();
        for (int id : cluster) {
            servers.add(new ZABSimulation(id, cluster));
        }
        
        // Simulate ZAB protocol
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // Phase 1: Election
        Future<Integer> electionResult = executor.submit(() -> {
            return servers.get(0).conductElection();
        });
        
        try {
            int leaderId = electionResult.get();
            System.out.println("\n=== Leader elected: " + leaderId + " ===\n");
            
            // Phase 2: Discovery
            for (ZABSimulation server : servers) {
                server.discovery(leaderId);
            }
            
            // Phase 3: Synchronization
            byte[] snapshot = "snapshot".getBytes();
            List<Long> log = Arrays.asList(1L, 2L, 3L);
            for (ZABSimulation server : servers) {
                server.synchronization(snapshot, log);
            }
            
            // Phase 4: Broadcast
            System.out.println("\n=== Starting broadcast ===\n");
            for (ZABSimulation server : servers) {
                server.broadcast("CREATE /test znode");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        executor.shutdown();
    }
    
    static class Vote {
        private final int serverId;
        private final long zxid;
        
        Vote(int serverId, long zxid) {
            this.serverId = serverId;
            this.zxid = zxid;
        }
        
        int getServerId() { return serverId; }
        long getZxid() { return zxid; }
    }
}
```

---

## Example 4: Observer Node Setup

```properties
# zoo.cfg with observers

tickTime=2000
initLimit=10
syncLimit=5
dataDir=/var/lib/zookeeper
clientPort=2181

# Regular servers (participants)
server.1=zk1:2888:3888:participant
server.2=zk2:2888:3888:participant
server.3=zk3:2888:3888:participant

# Observer nodes (read-only)
server.4=zk4:2888:3888:observer
server.5=zk5:2888:3888:observer

# Observer mode flag
peerType=observer
```

```bash
# Setup observer node
echo "4" > /var/lib/zookeeper/myid

# Start observer
zkServer.sh start

# Verify it's an observer
echo stat | nc localhost 2181 | grep Mode
# Output: Mode: observer
```

---

## Example 5: Cluster Monitoring Script

```bash
#!/bin/bash
# zookeeper-monitor.sh

ZK_HOSTS="zk1 zk2 zk3"
ZK_PORT=2181

check_health() {
    local host=$1
    local response=$(echo ruok | nc -w 5 $host $ZK_PORT 2>/dev/null)
    
    if [ "$response" = "imok" ]; then
        echo "✓ $host: Healthy"
        return 0
    else
        echo "✗ $host: Unhealthy"
        return 1
    fi
}

get_stats() {
    local host=$1
    echo "=== Stats for $host ==="
    echo stat | nc -w 5 $host $ZK_PORT
    echo ""
}

get_metrics() {
    local host=$1
    echo "=== Metrics for $host ==="
    echo mntr | nc -w 5 $host $ZK_PORT
    echo ""
}

# Check all nodes
echo "Health Check:"
echo "============="
for host in $ZK_HOSTS; do
    check_health $host
done

echo ""
echo "Cluster Stats:"
echo "=============="
for host in $ZK_HOSTS; do
    get_stats $host
done

# Check leader
echo "Leader Information:"
echo "==================="
for host in $ZK_HOSTS; do
    mode=$(echo stat | nc -w 5 $host $ZK_PORT | grep "Mode:" | awk '{print $2}')
    echo "$host: $mode"
done
```

---

## Example 6: Failover Testing

```java
package academy.messaging.zookeeper.architecture.examples;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;

public class FailoverTest {
    
    private static final String CONNECT_STRING = "zk1:2181,zk2:2181,zk3:2181";
    private static final String TEST_PATH = "/failover-test";
    
    public static void main(String[] args) throws Exception {
        // Create client
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(CONNECT_STRING)
            .sessionTimeoutMs(10000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        
        client.start();
        
        // Create test znode
        if (client.checkExists().forPath(TEST_PATH) == null) {
            client.create().forPath(TEST_PATH, "initial".getBytes());
        }
        
        System.out.println("Initial value: " + 
            new String(client.getData().forPath(TEST_PATH)));
        
        // Simulate updates
        for (int i = 0; i < 10; i++) {
            Thread.sleep(2000);
            
            try {
                client.setData().forPath(TEST_PATH, 
                    ("update-" + i).getBytes());
                System.out.println("Update " + i + " successful");
            } catch (Exception e) {
                System.out.println("Update " + i + " failed: " + e.getMessage());
            }
        }
        
        client.close();
    }
}
```
