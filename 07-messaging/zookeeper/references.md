# Zookeeper References

## Official Documentation

### Apache Zookeeper
- [Zookeeper Documentation](https://zookeeper.apache.org/doc/current/)
- [Zookeeper Programmer's Guide](https://zookeeper.apache.org/doc/current/zookeeperProgrammers.html)
- [Zookeeper Administrator's Guide](https://zookeeper.apache.org/doc/current/zookeeperAdmin.html)
- [Zookeeper Javadoc](https://zookeeper.apache.org/doc/current/apidocs/)
- [Zookeeper Release Notes](https://zookeeper.apache.org/doc/r3.8.4/releasenotes.html)

### Curator Framework
- [Curator Documentation](https://curator.apache.org/)
- [Curator Recipes](https://curator.apache.org/recipes/)
- [Curator X Discovery](https://curator.apache.org/curator-x-discovery/)
- [Curator X Cache](https://curator.apache.org/curator-x-cache/)
- [Curator GitHub](https://github.com/apache/curator)

---

## Books

### Essential Reading
- **"Zookeeper: Distributed Process Coordination"** - Flavio Junqueira, Benjamin Reed
  - The definitive guide to Zookeeper
  - Covers theory and practice

- **"Building Microservices"** - Sam Newman
  - Chapter on service discovery
  - Practical patterns

- **"Microservices Patterns"** - Chris Richardson
  - Service registry patterns
  - Zookeeper in microservices

### Advanced Topics
- **"Designing Data-Intensive Applications"** - Martin Kleppmann
  - Consensus algorithms
  - Distributed systems fundamentals

- **"Distributed Systems"** - Maarten van Steen, Andrew Tanenbaum
  - Theoretical foundations
  - Consensus protocols

---

## Tutorials & Guides

### Getting Started
- [Zookeeper Quick Start](https://zookeeper.apache.org/doc/current/zookeeperStarted.html)
- [Curator Getting Started](https://curator.apache.org/docs/about/)
- [Zookeeper in 5 Minutes](https://dzone.com/articles/apache-zookeeper-quick-start)

### Advanced Tutorials
- [Building a Distributed System with Zookeeper](https://www.baeldung.com/zookeeper)
- [Service Discovery with Curator](https://www.baeldung.com/curator-discovery)
- [Leader Election with Zookeeper](https://www.baeldung.com/zookeeper-election)
- [Distributed Locks with Zookeeper](https://www.baeldung.com/java-distributed-locks)

### Video Tutorials
- [Zookeeper Fundamentals - Pluralsight](https://www.pluralsight.com/courses/zookeeper-fundamentals)
- [Apache Zookeeper Tutorial - YouTube](https://www.youtube.com/watch?v=Vz1fvRrpflw)
- [Curator Framework Tutorial - YouTube](https://www.youtube.com/watch?v=uQ8KLT7KxHQ)

---

## Code Examples

### Official Examples
- [Zookeeper Recipes](https://zookeeper.apache.org/doc/current/recipes.html)
- [Curator Examples](https://github.com/apache/curator/tree/master/examples)
- [Zookeeper Demo](https://github.com/apache/zookeeper/blob/trunk/zookeeper-demo/)

### Community Examples
- [Service Discovery Example](https://github.com/spring-petclinic/spring-petclinic-microservices)
- [Leader Election Example](https://github.com/akosdabas/zookeeper-leader-election)
- [Distributed Lock Example](https://github.com/chtison/zookeeper-lock)

---

## Configuration Reference

### zoo.cfg Parameters

```properties
# Core Settings
tickTime=2000                    # Basic time unit (ms)
initLimit=10                     # Ticks for initial sync
syncLimit=5                      # Ticks for sync
dataDir=/var/lib/zookeeper       # Data directory
clientPort=2181                  # Client connection port

# Cluster
server.1=zk1:2888:3888          # Server config
server.2=zk2:2888:3888
server.3=zk3:2888:3888

# Performance
maxClientCnxns=60                # Max connections per IP
autopurge.snapRetainCount=3      # Snapshots to retain
autopurge.purgeInterval=1        # Purge interval (hours)

# Security
authProvider.1=org.apache.zookeeper.server.auth.SASLAuthenticationProvider
ssl.keyStore.location=/path/to/keystore
ssl.trustStore.location=/path/to/truststore
```

---

## Curator API Reference

### Core Operations

```java
// CRUD Operations
client.create().forPath("/path");
client.create().creatingParentsIfNeeded().forPath("/path");
client.getData().forPath("/path");
client.getData().storingStatIn(stat).forPath("/path");
client.setData().forPath("/path", data);
client.setData().withVersion(version).forPath("/path");
client.delete().forPath("/path");
client.delete().deletingChildrenIfNeeded().forPath("/path");
client.checkExists().forPath("/path");
client.getChildren().forPath("/path");
```

### Watches

```java
// One-time Watch
client.getData().usingWatcher(watcher).forPath("/path");
client.getChildren().usingWatcher(watcher).forPath("/path");
client.checkExists().usingWatcher(watcher).forPath("/path");

// Persistent Watch (Curator)
PathChildrenCache cache = new PathChildrenCache(client, "/path", true);
NodeCache nodeCache = new NodeCache(client, "/path");
TreeCache treeCache = new TreeCache(client, "/path");
```

### Recipes

```java
// Leader Election
LeaderLatch latch = new LeaderLatch(client, "/leader");
LeaderSelector selector = new LeaderSelector(client, "/leader", (client, leader) -> {
    // Leadership acquired
});

// Distributed Lock
InterProcessMutex lock = new InterProcessMutex(client, "/locks/resource");
InterProcessReadWriteLock rwLock = new InterProcessReadWriteLock(client, "/locks/resource");

// Barrier
DistributedBarrier barrier = new DistributedBarrier(client, "/barriers/phase");
DistributedDoubleBarrier doubleBarrier = new DistributedDoubleBarrier(client, "/barriers/phase");

// Queue
InterProcessQueue queue = new InterProcessQueue(client, "/queues/my-queue");
```

---

## Monitoring & Operations

### JMX Metrics

```
# Key metrics to monitor
zk.outstanding_requests     # Pending requests
zk.num_alive_connections    # Active connections
zk.latency.avg              # Average latency
zk.packets.received         # Packets received
zk.packets.sent             # Packets sent
zk.followers                # Number of followers
zk.sync_time                # Time since last sync
```

### Command Line Tools

```bash
# Basic commands
zkCli.sh -server localhost:2181

# Four Letter Words
echo stat | nc localhost 2181
echo mntr | nc localhost 2181
echo conf | nc localhost 2181
echo ruok | nc localhost 2181
```

### Monitoring Setup

```yaml
# Prometheus configuration
- job_name: 'zookeeper'
  static_configs:
    - targets: ['zk1:7000', 'zk2:7000', 'zk3:7000']
  metrics_path: '/metrics'
```

---

## Common Patterns

### Circuit Breaker

```java
// Zookeeper-based circuit breaker
public class ZookeeperCircuitBreaker {
    private final CuratorFramework client;
    private final String path;
    
    public boolean isOpen() {
        byte[] data = client.getData().forPath(path);
        return Boolean.parseBoolean(new String(data));
    }
    
    public void setOpen(boolean open) {
        client.setData().forPath(path, 
            Boolean.toString(open).getBytes());
    }
}
```

### Service Registry

```java
// Register service
ServiceInstance instance = ServiceInstance.builder()
    .name("my-service")
    .address("10.0.0.1")
    .port(8080)
    .build();
serviceDiscovery.registerService(instance);

// Discover services
ServiceProvider provider = serviceDiscovery
    .serviceProviderBuilder()
    .serviceName("my-service")
    .build();
provider.start();
```

---

## Troubleshooting

### Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Connection refused | ZK not running | Start ZK server |
| Session expired | Long disconnection | Reduce timeout, handle reconnection |
| Leader election loop | Network instability | Check network, adjust timeouts |
| Watch lost | Session reset | Use Curator recipes |
| Performance slow | Large dataset | Split data, use observers |

### Debug Commands

```bash
# Check ZK status
zkServer.sh status

# View logs
tail -f zookeeper.out

# Check connections
echo stat | nc localhost 2181

# Test connectivity
echo ruok | nc localhost 2181
```

---

## Community Resources

### Stack Overflow Tags
- [apache-zookeeper](https://stackoverflow.com/questions/tagged/apache-zookeeper)
- [curator](https://stackoverflow.com/questions/tagged/curator)
- [distributed-systems](https://stackoverflow.com/questions/tagged/distributed-systems)

### Mailing Lists
- [zookeeper-users@lists.apache.org](mailto:zookeeper-users@lists.apache.org)
- [zookeeper-dev@lists.apache.org](mailto:zookeeper-dev@lists.apache.org)

### GitHub
- [Apache Zookeeper](https://github.com/apache/zookeeper)
- [Apache Curator](https://github.com/apache/curator)

---

## Related Technologies

| Technology | Use Case | Comparison |
|------------|----------|------------|
| **etcd** | Key-value store, leader election | Simpler, Raft consensus |
| **Consul** | Service discovery, config | More features, easier setup |
| **Eureka** | Service discovery | AP-focused, Netflix OSS |
| **Doozer** | Distributed coordination | Older, less maintained |
| **Toxiproxy** | Network fault injection | Testing, not coordination |
