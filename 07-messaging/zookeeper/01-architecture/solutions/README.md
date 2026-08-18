# Architecture Solutions

## Solution 1: 3-Node Cluster Deployment

### docker-compose.yml

```yaml
version: '3.8'

services:
  zk1:
    image: zookeeper:3.8
    hostname: zk1
    container_name: zk1
    ports:
      - "2181:2181"
      - "2888:2888"
      - "3888:3888"
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

  zk2:
    image: zookeeper:3.8
    hostname: zk2
    container_name: zk2
    ports:
      - "2182:2181"
      - "2889:2888"
      - "3889:3888"
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

  zk3:
    image: zookeeper:3.8
    hostname: zk3
    container_name: zk3
    ports:
      - "2183:2181"
      - "2890:2888"
      - "3890:3888"
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

networks:
  zknet:
    driver: bridge
```

### Deployment Commands

```bash
# Start cluster
docker-compose up -d

# Check status
docker-compose exec zk1 zkServer.sh status
docker-compose exec zk2 zkServer.sh status
docker-compose exec zk3 zkServer.sh status

# Test connectivity
docker-compose exec zk1 zkCli.sh -server zk1:2181,zk2:2181,zk3:2181

# View logs
docker-compose logs -f
```

### Verification Script

```bash
#!/bin/bash
# verify-cluster.sh

echo "=== Cluster Status ==="
for i in 1 2 3; do
    echo "Node zk$i:"
    docker-compose exec zk$i zkServer.sh status
    echo ""
done

echo "=== Health Check ==="
for i in 1 2 3; do
    echo -n "zk$i: "
    docker-compose exec zk$i echo ruok | nc localhost 2181
    echo ""
done

echo "=== Connection Test ==="
docker-compose exec zk1 zkCli.sh -server zk1:2181,zk2:2181,zk3:2181 create /test "hello"
docker-compose exec zk1 zkCli.sh -server zk1:2181,zk2:2181,zk3:2181 get /test
```

---

## Solution 2: Observer Node Configuration

### docker-compose.yml (with observer)

```yaml
version: '3.8'

services:
  zk1:
    image: zookeeper:3.8
    hostname: zk1
    container_name: zk1
    ports:
      - "2181:2181"
      - "2888:2888"
      - "3888:3888"
    environment:
      ZOO_MY_ID: 1
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181 server.4=zk4:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

  zk2:
    image: zookeeper:3.8
    hostname: zk2
    container_name: zk2
    ports:
      - "2182:2181"
      - "2889:2888"
      - "3889:3888"
    environment:
      ZOO_MY_ID: 2
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181 server.4=zk4:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

  zk3:
    image: zookeeper:3.8
    hostname: zk3
    container_name: zk3
    ports:
      - "2183:2181"
      - "2890:2888"
      - "3890:3888"
    environment:
      ZOO_MY_ID: 3
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181 server.4=zk4:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
    networks:
      - zknet

  zk4:
    image: zookeeper:3.8
    hostname: zk4
    container_name: zk4
    ports:
      - "2184:2181"
      - "2891:2888"
      - "3891:3888"
    environment:
      ZOO_MY_ID: 4
      ZOO_SERVERS: server.1=zk1:2888:3888;0.0.0.0:2181 server.2=zk2:2888:3888;0.0.0.0:2181 server.3=zk3:2888:3888;0.0.0.0:2181 server.4=zk4:2888:3888;0.0.0.0:2181
      ZOO_TICK_TIME: 2000
      ZOO_INIT_LIMIT: 10
      ZOO_SYNC_LIMIT: 5
      ZOO_PEER_TYPE: observer
    networks:
      - zknet

networks:
  zknet:
    driver: bridge
```

### Verification

```bash
# Start cluster
docker-compose up -d

# Check observer status
docker-compose exec zk4 zkServer.sh status
# Should show: Mode: observer

# Test write (should fail on observer)
docker-compose exec zk4 zkCli.sh -server zk4:2181 create /test "data"
# Should fail with: KeeperException: NotLeader

# Test read (should succeed)
docker-compose exec zk4 zkCli.sh -server zk4:2181 get /zookeeper/config
# Should succeed
```

---

## Solution 3: Failover Testing

### failover-test.sh

```bash
#!/bin/bash
# failover-test.sh

set -e

ZK_HOSTS=("zk1" "zk2" "zk3")
ZK_PORT=2181

echo "=== Finding Leader ==="
LEADER=""
for host in "${ZK_HOSTS[@]}"; do
    mode=$(echo stat | nc -w 5 $host $ZK_PORT | grep "Mode:" | awk '{print $2}')
    if [ "$mode" = "leader" ]; then
        LEADER=$host
        echo "Leader found: $host"
        break
    fi
done

if [ -z "$LEADER" ]; then
    echo "No leader found!"
    exit 1
fi

echo ""
echo "=== Creating Test Data ==="
docker-compose exec $LEADER zkCli.sh -server ${ZK_HOSTS[0]}:2181,${ZK_HOSTS[1]}:2181,${ZK_HOSTS[2]}:2181 create /failover-test "initial-value"

echo ""
echo "=== Stopping Leader ($LEADER) ==="
docker-compose stop $LEADER

echo ""
echo "=== Waiting for Election ==="
sleep 5

echo ""
echo "=== Checking New Leader ==="
NEW_LEADER=""
for host in "${ZK_HOSTS[@]}"; do
    if [ "$host" != "$LEADER" ]; then
        mode=$(echo stat | nc -w 5 $host $ZK_PORT | grep "Mode:" | awk '{print $2}')
        if [ "$mode" = "leader" ]; then
            NEW_LEADER=$host
            echo "New leader: $host"
            break
        fi
    fi
done

echo ""
echo "=== Verifying Data ==="
docker-compose exec $NEW_LEADER zkCli.sh -server ${ZK_HOSTS[0]}:2181,${ZK_HOSTS[1]}:2181 get /failover-test

echo ""
echo "=== Restarting Old Leader ==="
docker-compose start $LEADER
sleep 5

echo ""
echo "=== Final Cluster Status ==="
for host in "${ZK_HOSTS[@]}"; do
    mode=$(echo stat | nc -w 5 $host $ZK_PORT | grep "Mode:" | awk '{print $2}')
    echo "$host: $mode"
done

echo ""
echo "=== Failover Test Complete ==="
```

---

## Solution 4: Monitoring Setup

### prometheus-zk.yml

```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'zookeeper'
    static_configs:
      - targets:
          - 'zk1:7000'
          - 'zk2:7000'
          - 'zk3:7000'
    metrics_path: '/metrics'
    scrape_interval: 10s
```

### grafana-dashboard.json

```json
{
  "dashboard": {
    "title": "Zookeeper Cluster",
    "panels": [
      {
        "title": "Outstanding Requests",
        "type": "graph",
        "targets": [
          {
            "expr": "zk_outstanding_requests",
            "legendFormat": "{{instance}}"
          }
        ]
      },
      {
        "title": "Alive Connections",
        "type": "graph",
        "targets": [
          {
            "expr": "zk_num_alive_connections",
            "legendFormat": "{{instance}}"
          }
        ]
      },
      {
        "title": "Average Latency",
        "type": "graph",
        "targets": [
          {
            "expr": "zk_avg_latency",
            "legendFormat": "{{instance}}"
          }
        ]
      }
    ]
  }
}
```

### monitoring-script.sh

```bash
#!/bin/bash
# zookeeper-monitor.sh

LOG_FILE="/var/log/zookeeper-monitor.log"
ALERT_EMAIL="admin@example.com"

log() {
    echo "$(date '+%Y-%m-%d %H:%M:%S') $1" >> $LOG_FILE
}

check_health() {
    local host=$1
    local response=$(echo ruok | nc -w 5 $host 2181 2>/dev/null)
    
    if [ "$response" != "imok" ]; then
        log "ALERT: $host is unhealthy"
        echo "Zookeeper $host is unhealthy" | mail -s "ZK Alert" $ALERT_EMAIL
        return 1
    fi
    return 0
}

check_leader_exists() {
    local leader_found=false
    
    for host in zk1 zk2 zk3; do
        mode=$(echo stat | nc -w 5 $host 2181 2>/dev/null | grep "Mode:" | awk '{print $2}')
        if [ "$mode" = "leader" ]; then
            leader_found=true
            log "Leader found on $host"
            break
        fi
    done
    
    if [ "$leader_found" = false ]; then
        log "ALERT: No leader found in cluster"
        echo "No Zookeeper leader found" | mail -s "ZK Alert" $ALERT_EMAIL
        return 1
    fi
    return 0
}

check_latency() {
    local host=$1
    local latency=$(echo mntr | nc -w 5 $host 2181 2>/dev/null | grep zk_avg_latency | awk '{print $2}')
    
    if [ -n "$latency" ] && [ "$latency" -gt 100 ]; then
        log "WARNING: High latency on $host: ${latency}ms"
    fi
}

# Main monitoring loop
while true; do
    log "=== Starting health check ==="
    
    for host in zk1 zk2 zk3; do
        check_health $host
    done
    
    check_leader_exists
    
    for host in zk1 zk2 zk3; do
        check_latency $host
    done
    
    log "=== Health check complete ==="
    sleep 60
done
```

---

## Solution 5: Performance Benchmark

### benchmark.sh

```bash
#!/bin/bash
# zookeeper-benchmark.sh

set -e

ZK_HOST="localhost:2181"
ITERATIONS=${1:-1000}
CONCURRENT=${2:-10}

echo "=== Zookeeper Benchmark ==="
echo "Iterations: $ITERATIONS"
echo "Concurrent: $CONCURRENT"
echo ""

# Clean up
echo "clean" | nc $ZK_HOST 2181 > /dev/null 2>&1 || true

# Create test directory
echo "create /benchmark data" | nc $ZK_HOST 2181 > /dev/null 2>&1 || true

# Sequential writes
echo "=== Sequential Write Test ==="
start_time=$(date +%s%N)

for i in $(seq 1 $ITERATIONS); do
    echo "create /benchmark/seq_$i data" | nc $ZK_HOST 2181 > /dev/null
done

end_time=$(date +%s%N)
duration=$(( (end_time - start_time) / 1000000 ))
throughput=$(( ITERATIONS * 1000 / duration ))

echo "Completed in ${duration}ms"
echo "Throughput: ${throughput} ops/sec"
echo ""

# Sequential reads
echo "=== Sequential Read Test ==="
start_time=$(date +%s%N)

for i in $(seq 1 $ITERATIONS); do
    echo "get /benchmark/seq_1" | nc $ZK_HOST 2181 > /dev/null
done

end_time=$(date +%s%N)
duration=$(( (end_time - start_time) / 1000000 ))
throughput=$(( ITERATIONS * 1000 / duration ))

echo "Completed in ${duration}ms"
echo "Throughput: ${throughput} ops/sec"
echo ""

# Concurrent writes
echo "=== Concurrent Write Test ==="
start_time=$(date +%s%N)

for i in $(seq 1 $CONCURRENT); do
    (
        for j in $(seq 1 $((ITERATIONS / CONCURRENT))); do
            echo "create /benchmark/conc_${i}_${j} data" | nc $ZK_HOST 2181 > /dev/null
        done
    ) &
done

wait

end_time=$(date +%s%N)
duration=$(( (end_time - start_time) / 1000000 ))
throughput=$(( ITERATIONS * 1000 / duration ))

echo "Completed in ${duration}ms"
echo "Throughput: ${throughput} ops/sec"
echo ""

# Cleanup
echo "delete /benchmark" | nc $ZK_HOST 2181 > /dev/null 2>&1 || true

echo "=== Benchmark Complete ==="
```

### Java Benchmark

```java
package academy.messaging.zookeeper.architecture.solutions;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ZKBenchmark {
    
    private final CuratorFramework client;
    private final String testPath = "/benchmark";
    private final AtomicLong writeCount = new AtomicLong(0);
    private final AtomicLong readCount = new AtomicLong(0);
    
    public ZKBenchmark(String connectString) {
        this.client = CuratorFrameworkFactory.builder()
            .connectString(connectString)
            .sessionTimeoutMs(30000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        client.start();
    }
    
    public void setup() throws Exception {
        if (client.checkExists().forPath(testPath) != null) {
            client.delete().deletingChildrenIfNeeded().forPath(testPath);
        }
        client.create().forPath(testPath);
    }
    
    public void benchmarkWrites(int iterations, int concurrency) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch latch = new CountDownLatch(iterations);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    client.create().forPath(
                        testPath + "/write-" + index, 
                        ("data-" + index).getBytes()
                    );
                    writeCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("Writes: " + iterations + " in " + duration + "ms");
        System.out.println("Throughput: " + (iterations * 1000 / duration) + " ops/sec");
        
        executor.shutdown();
    }
    
    public void benchmarkReads(int iterations, int concurrency) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch latch = new CountDownLatch(iterations);
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < iterations; i++) {
            executor.submit(() -> {
                try {
                    client.getData().forPath(testPath + "/write-0");
                    readCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long duration = System.currentTimeMillis() - startTime;
        
        System.out.println("Reads: " + iterations + " in " + duration + "ms");
        System.out.println("Throughput: " + (iterations * 1000 / duration) + " ops/sec");
        
        executor.shutdown();
    }
    
    public void cleanup() throws Exception {
        if (client.checkExists().forPath(testPath) != null) {
            client.delete().deletingChildrenIfNeeded().forPath(testPath);
        }
        client.close();
    }
    
    public static void main(String[] args) throws Exception {
        ZKBenchmark benchmark = new ZKBenchmark("localhost:2181");
        
        benchmark.setup();
        
        System.out.println("=== Write Benchmark ===");
        benchmark.benchmarkWrites(1000, 10);
        
        System.out.println("\n=== Read Benchmark ===");
        benchmark.benchmarkReads(1000, 10);
        
        benchmark.cleanup();
    }
}
```

---

## Solution 6: Backup Script

### backup.sh

```bash
#!/bin/bash
# zookeeper-backup.sh

set -e

ZK_DATA_DIR="/var/lib/zookeeper"
BACKUP_DIR="/backup/zookeeper"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_NAME="zk-backup-$TIMESTAMP"

# Create backup directory
mkdir -p $BACKUP_DIR

echo "=== Starting Zookeeper Backup ==="

# Create snapshot
echo "Creating snapshot..."
echo "snap" | nc localhost 2181 > /dev/null

# Wait for snapshot
sleep 5

# Backup data directory
echo "Backing up data directory..."
tar -czf "$BACKUP_DIR/$BACKUP_NAME.tar.gz" -C / var/lib/zookeeper

# Verify backup
echo "Verifying backup..."
if [ -f "$BACKUP_DIR/$BACKUP_NAME.tar.gz" ]; then
    echo "Backup created: $BACKUP_DIR/$BACKUP_NAME.tar.gz"
    ls -lh "$BACKUP_DIR/$BACKUP_NAME.tar.gz"
else
    echo "ERROR: Backup failed!"
    exit 1
fi

# Cleanup old backups (keep last 7)
echo "Cleaning up old backups..."
ls -t $BACKUP_DIR/zk-backup-*.tar.gz | tail -n +8 | xargs -r rm

echo "=== Backup Complete ==="
```

### restore.sh

```bash
#!/bin/bash
# zookeeper-restore.sh

set -e

ZK_DATA_DIR="/var/lib/zookeeper"
BACKUP_FILE=$1

if [ -z "$BACKUP_FILE" ]; then
    echo "Usage: $0 <backup-file>"
    exit 1
fi

echo "=== Starting Zookeeper Restore ==="
echo "Backup file: $BACKUP_FILE"

# Stop zookeeper
echo "Stopping Zookeeper..."
zkServer.sh stop

# Backup current data
echo "Backing up current data..."
mv $ZK_DATA_DIR ${ZK_DATA_DIR}.bak.$(date +%s)

# Restore
echo "Restoring from backup..."
tar -xzf $BACKUP_FILE -C /

# Fix permissions
chown -R zookeeper:zookeeper $ZK_DATA_DIR

# Start zookeeper
echo "Starting Zookeeper..."
zkServer.sh start

# Verify
sleep 5
echo "Verifying restore..."
zkServer.sh status

echo "=== Restore Complete ==="
```
