# ActiveMQ Clustering

## ActiveMQ Clustering, Failover, and High Availability

---

## Table of Contents

- [Overview](#overview)
- [Clustering Architecture](#clustering-architecture)
- [Failover](#failover)
- [High Availability](#high-availability)
- [Network of Brokers](#network-of-brokers)
- [Best Practices](#best-practices)

---

## Overview

ActiveMQ provides clustering for high availability, fault tolerance, and horizontal scaling. This guide covers clustering architectures, failover strategies, and best practices.

### Clustering Goals

- **High Availability**: Continue operating during failures
- **Fault Tolerance**: Recover from node failures
- **Load Balancing**: Distribute load across nodes
- **Scalability**: Add nodes for more capacity

---

## Clustering Architecture

### Master-Slave Architecture

```
Master-Slave:
┌─────────────────────────────────────────────────────────────┐
│                    Master-Slave Cluster                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Master     │  │   Slave 1    │  │   Slave 2    │      │
│  │   (Active)   │  │   (Standby)  │  │   (Standby)  │      │
│  │              │  │              │  │              │      │
│  │ ┌──────────┐ │  │ ┌──────────┐ │  │ ┌──────────┐ │      │
│  │ │ Queue    │ │  │ │ Queue    │ │  │ │ Queue    │ │      │
│  │ │ (Active) │ │  │ │ (Copy)   │ │  │ │ (Copy)   │ │      │
│  │ └──────────┘ │  │ └──────────┘ │  │ └──────────┘ │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                                                              │
│  Master handles all operations                               │
│  Slaves replicate from master                               │
│  On master failure: slave promoted                          │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

### Shared Storage Architecture

```
Shared Storage:
┌─────────────────────────────────────────────────────────────┐
│                    Shared Storage Cluster                     │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Broker 1   │  │   Broker 2   │  │   Broker 3   │      │
│  │   (Active)   │  │   (Standby)  │  │   (Standby)  │      │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘      │
│         │                 │                 │                │
│         └─────────────────┼─────────────────┘                │
│                           │                                  │
│                           ▼                                  │
│                    ┌──────────────┐                         │
│                    │ Shared       │                         │
│                    │ Storage      │                         │
│                    │ (Database)   │                         │
│                    └──────────────┘                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

---

## Failover

### Failover Transport

```java
// Failover URL
String url = "failover:(tcp://broker1:61616,tcp://broker2:61616)";

// With options
String url = "failover:(tcp://broker1:61616,tcp://broker2:61616)" +
    "?maxReconnectAttempts=10" +
    "&startupMaxReconnectAttempts=3" +
    "&useExponentialBackOff=true" +
    "&maxReconnectDelay=10000";

ConnectionFactory factory = new ActiveMQConnectionFactory(url);
```

### Failover Options

| Option | Description |
|--------|-------------|
| `maxReconnectAttempts` | Max reconnect attempts (-1 = infinite) |
| `startupMaxReconnectAttempts` | Max attempts on startup |
| `initialReconnectDelay` | Initial reconnect delay (ms) |
| `maxReconnectDelay` | Max reconnect delay (ms) |
| `useExponentialBackOff` | Exponential backoff |
| `backOffMultiplier` | Backoff multiplier |
| `randomize` | Randomize broker selection |

### Failover Behavior

```
Normal Operation:
Broker 1 (Active) ←── Clients connect here

Broker 1 fails:
Broker 2 (Active) ←── Clients failover to here

Broker 1 recovers:
Broker 1 (Active) ←── Clients can failback
Broker 2 (Standby)
```

---

## High Availability

### JDBC Master-Slave

```xml
<!-- activemq.xml -->
<broker brokerName="broker" useJmx="true" persistenceAdapter="true">
    <persistenceAdapter>
        <jdbcPersistenceAdapter dataSource="#ds" adapter="#jdbcAdapter"/>
    </persistenceAdapter>
</broker>

<bean id="ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/activemq"/>
    <property name="username" value="activemq"/>
    <property name="password" value="activemq"/>
</bean>
```

### KahaDB Master-Slave

```xml
<!-- activemq.xml -->
<broker brokerName="broker" useJmx="true" persistenceAdapter="true">
    <persistenceAdapter>
        <kahaDB directory="${activemq.data}/kahadb" journalMaxFileLength="32mb"/>
    </persistenceAdapter>
</broker>
```

### LevelDB Master-Slave

```xml
<!-- activemq.xml -->
<broker brokerName="broker" useJmx="true" persistenceAdapter="true">
    <persistenceAdapter>
        <levelDB directory="${activemq.data}/leveldb"/>
    </persistenceAdapter>
</broker>
```

---

## Network of Brokers

### Static Network

```xml
<!-- activemq.xml -->
<networkConnector name="bridge" uri="static:(tcp://broker2:61616,tcp://broker3:61616)"/>
```

### Dynamic Network

```xml
<!-- activemq.xml -->
<networkConnector name="bridge" uri="multicast://default"/>
```

### Network Configuration

```xml
<networkConnector name="bridge" uri="static:(tcp://broker2:61616)">
    <dynamicallyIncludedDestinations>
        <queue physicalName="orders"/>
        <topic physicalName="notifications"/>
    </dynamicallyIncludedDestinations>
</networkConnector>
```

---

## Best Practices

### Cluster Design

1. **Use odd number of brokers** - For quorum
2. **Dedicate brokers** - Don't share with other services
3. **Use shared storage** - For simplicity
4. **Plan capacity** - Size cluster appropriately

### Failover

1. **Use failover transport** - Automatic reconnection
2. **Tune reconnect settings** - Balance speed vs load
3. **Test failover** - Verify recovery
4. **Monitor failover events** - Track failures

### High Availability

1. **Use master-slave** - For simplicity
2. **Use shared storage** - For scalability
3. **Monitor health** - Track broker status
4. **Test recovery** - Verify data persistence

### Operations

1. **Use management console** - Monitor cluster
2. **Track metrics** - Performance and health
3. **Plan capacity** - Scale as needed
4. **Document procedures** - Maintain runbooks

---

## Further Reading

- [ActiveMQ Clustering](https://activemq.apache.org/clustering.html)
- [Failover Transport](https://activemq.apache.org/failover-transport-reference.html)
- [Network of Brokers](https://activemq.apache.org/networks-of-brokers.html)
