# Architecture Practices

## Practice 1: Deploy a 3-Node Cluster

### Objective
Deploy a working 3-node Zookeeper cluster on separate machines.

### Requirements
- 3 virtual machines or containers
- Network connectivity between all nodes
- Java 8+ installed

### Steps

1. **Configure each node**
   ```bash
   # On each node, create myid file
   echo "1" > /var/lib/zookeeper/myid  # Node 1
   echo "2" > /var/lib/zookeeper/myid  # Node 2
   echo "3" > /var/lib/zookeeper/myid  # Node 3
   ```

2. **Create zoo.cfg**
   ```properties
   tickTime=2000
   initLimit=10
   syncLimit=5
   dataDir=/var/lib/zookeeper
   clientPort=2181
   server.1=node1:2888:3888
   server.2=node2:2888:3888
   server.3=node3:2888:3888
   ```

3. **Start all nodes**
   ```bash
   zkServer.sh start
   ```

4. **Verify cluster**
   ```bash
   zkServer.sh status  # On each node
   ```

### Expected Output
- One node shows "Leader"
- Two nodes show "Follower"
- All nodes can be connected via zkCli.sh

---

## Practice 2: Configure an Observer Node

### Objective
Add an observer node to the cluster for read scaling.

### Steps

1. **Update zoo.cfg**
   ```properties
   server.4=node4:2888:3888:observer
   peerType=observer
   ```

2. **Configure observer node**
   ```bash
   echo "4" > /var/lib/zookeeper/myid
   zkServer.sh start
   ```

3. **Verify observer mode**
   ```bash
   echo stat | nc localhost 2181 | grep Mode
   # Should output: Mode: observer
   ```

### Questions
- What happens if you try to write to the observer?
- How does the observer affect cluster performance?

---

## Practice 3: Simulate Leader Failure

### Objective
Test cluster behavior when the leader fails.

### Steps

1. **Identify the leader**
   ```bash
   zkServer.sh status
   ```

2. **Stop the leader**
   ```bash
   zkServer.sh stop  # On leader node
   ```

3. **Observe election**
   ```bash
   # On other nodes
   zkServer.sh status
   # Watch logs for election
   tail -f zookeeper.out
   ```

4. **Restart old leader**
   ```bash
   zkServer.sh start
   ```

### Expected Results
- New leader elected within 1-2 seconds
- All operations continue after election
- Old leader becomes follower when restarted

---

## Practice 4: Monitor Cluster Metrics

### Objective
Set up monitoring for Zookeeper cluster.

### Steps

1. **Enable 4-letter words**
   ```properties
   # In zoo.cfg
   4lw.commands.whitelist=*
   ```

2. **Collect metrics**
   ```bash
   # Health check
   echo ruok | nc localhost 2181
   
   # Statistics
   echo stat | nc localhost 2181
   
   # Metrics
   echo mntr | nc localhost 2181
   ```

3. **Parse metrics**
   ```bash
   # Parse mntr output
   echo mntr | nc localhost 2181 | grep "zk_" | \
   awk '{print $1": "$2}'
   ```

### Metrics to Track
- `zk_outstanding_requests`
- `zk_num_alive_connections`
- `zk_avg_latency`
- `zk_followers`

---

## Practice 5: Test Network Partition

### Objective
Test cluster behavior during network partition.

### Steps

1. **Setup firewall rules**
   ```bash
   # Block traffic to leader from one follower
   iptables -A INPUT -s <follower_ip> -d <leader_ip> -j DROP
   ```

2. **Observe behavior**
   - Follower should detect partition
   - Leader election may occur
   - Check cluster status

3. **Restore network**
   ```bash
   iptables -D INPUT -s <follower_ip> -d <leader_ip> -j DROP
   ```

### Expected Results
- Cluster maintains quorum if majority available
- Partitioned node rejoins after network restore
- No data loss during partition

---

## Practice 6: Benchmark Cluster Performance

### Objective
Measure cluster throughput and latency.

### Steps

1. **Create benchmark script**
   ```bash
   #!/bin/bash
   # benchmark.sh
   
   ITERATIONS=1000
   ZK_HOST="localhost:2181"
   
   echo "Starting benchmark..."
   start_time=$(date +%s%N)
   
   for i in $(seq 1 $ITERATIONS); do
       echo "create /bench/test$i data" | nc $ZK_HOST 2181
   done
   
   end_time=$(date +%s%N)
   duration=$(( (end_time - start_time) / 1000000 ))
   
   echo "Completed $ITERATIONS operations in ${duration}ms"
   echo "Throughput: $(( ITERATIONS * 1000 / duration )) ops/sec"
   ```

2. **Run benchmark**
   ```bash
   chmod +x benchmark.sh
   ./benchmark.sh
   ```

3. **Measure latency**
   ```bash
   # Measure single operation latency
   start=$(date +%s%N)
   echo "create /bench/latency test" | nc localhost 2181
   end=$(date +%s%N)
   echo "Latency: $(( (end - start) / 1000000 ))ms"
   ```

---

## Practice 7: Backup and Recovery

### Objective
Implement backup and recovery procedures.

### Steps

1. **Backup snapshot**
   ```bash
   # Stop server
   zkServer.sh stop
   
   # Copy data directory
   tar -czf zk-backup-$(date +%Y%m%d).tar.gz /var/lib/zookeeper
   
   # Restart server
   zkServer.sh start
   ```

2. **Restore from backup**
   ```bash
   # Stop server
   zkServer.sh stop
   
   # Restore data
   rm -rf /var/lib/zookeeper/*
   tar -xzf zk-backup-20240101.tar.gz -C /
   
   # Restart server
   zkServer.sh start
   ```

3. **Automate backups**
   ```bash
   # Add to crontab
   0 2 * * * /path/to/backup-script.sh
   ```

---

## Practice 8: Upgrade Cluster

### Objective
Perform rolling upgrade of cluster.

### Steps

1. **Upgrade follower 1**
   ```bash
   zkServer.sh stop  # On follower 1
   # Install new version
   zkServer.sh start
   # Verify
   zkServer.sh status
   ```

2. **Upgrade follower 2**
   ```bash
   zkServer.sh stop  # On follower 2
   # Install new version
   zkServer.sh start
   # Verify
   zkServer.sh status
   ```

3. **Upgrade leader (during low traffic)**
   ```bash
   zkServer.sh stop  # On leader
   # Install new version
   zkServer.sh start
   # Verify new leader elected
   zkServer.sh status
   ```

### Expected Results
- No downtime during upgrade
- Cluster remains operational
- All nodes upgraded to new version

---

## Discussion Questions

1. **Why use odd number of nodes?**
   - What happens with 4 nodes vs 3 nodes?
   - How does this affect fault tolerance?

2. **When to use observers?**
   - What are the tradeoffs?
   - How do they affect consistency?

3. **How does ZAB compare to Raft?**
   - What are the differences?
   - When would you choose one over the other?

4. **What are the failure modes?**
   - How does ZK handle split-brain?
   - What happens during network partitions?

5. **How to size a cluster?**
   - What factors affect sizing?
   - How do you determine the right number of nodes?
