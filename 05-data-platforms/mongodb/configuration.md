# MongoDB Configuration

## mongod.conf

### Basic Configuration

```yaml
systemLog:
  destination: file
  path: /var/log/mongodb/mongod.log
  logAppend: true

storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true

net:
  port: 27017
  bindIp: 0.0.0.0

security:
  authorization: enabled
```

### Storage Engine

```yaml
storage:
  dbPath: /var/lib/mongodb
  engine: wiredTiger
  wiredTiger:
    engineConfig:
      cacheSizeGB: 1
      journalCompressor: snappy
    collectionConfig:
      blockCompressor: snappy
```

### Replication

```yaml
replication:
  replSetName: "rs0"
  oplogSizeMB: 1024
```

### Sharding

```yaml
sharding:
  clusterRole: shardsvr
  chunkSize: 128
```

## Replica Set Configuration

### Initialize Replica Set

```javascript
rs.initiate({
  _id: "rs0",
  members: [
    { _id: 0, host: "mongo1:27017" },
    { _id: 1, host: "mongo2:27017" },
    { _id: 2, host: "mongo3:27017" }
  ]
})
```

### Add Members

```javascript
rs.add("mongo4:27017")
rs.addArb("mongo5:27017")
```

### Configure Priority

```javascript
rs.reconfig({
  members: [
    { _id: 0, host: "mongo1:27017", priority: 2 },
    { _id: 1, host: "mongo2:27017", priority: 1 },
    { _id: 2, host: "mongo3:27017", priority: 1 }
  ]
})
```

## Sharding Configuration

### Config Server

```yaml
sharding:
  clusterRole: configsvr
replication:
  replSetName: "configRS"
```

### Shard Server

```yaml
sharding:
  clusterRole: shardsvr
replication:
  replSetName: "shardRS"
```

### Mongos Router

```yaml
sharding:
  configDB: configRS/config1:27017,config2:27017,config3:27017
net:
  bindIp: 0.0.0.0
  port: 27017
```

## Security Configuration

### Authentication

```yaml
security:
  authorization: enabled
  keyFile: /etc/mongodb/keyfile
```

### TLS/SSL

```yaml
net:
  tls:
    mode: requireTLS
    certificateKeyFile: /etc/ssl/mongodb.pem
    CAFile: /etc/ssl/ca.pem
```

### Audit Logging

```yaml
auditLog:
  destination: file
  format: JSON
  path: /var/log/mongodb/audit.json
```

## Performance Configuration

### WiredTiger Cache

```yaml
storage:
  wiredTiger:
    engineConfig:
      cacheSizeGB: 2
```

### Operation Profiling

```yaml
operationProfiling:
  mode: slowOp
  slowOpThresholdMs: 100
```

## Connection Settings

```yaml
net:
  maxIncomingConnections: 65536
  compression:
    compressors: snappy,zstd,zlib
```

## Log Rotation

```yaml
systemLog:
  logRotate: reopen
```

## Command Line Options

```bash
# Start with config file
mongod --config /etc/mongod.conf

# Override options
mongod --config /etc/mongod.conf --port 27018
```

## Best Practices

1. Use config files for production
2. Enable authentication
3. Use TLS for encrypted connections
4. Configure appropriate cache size
5. Enable audit logging
