# HBase Fundamentals

## Overview
Apache HBase is a distributed, scalable, NoSQL database built on Hadoop HDFS for random read/write access to big data.

## Architecture
- **HMaster**: Region server coordinator
- **RegionServer**: Stores and serves regions
- **ZooKeeper**: Cluster coordination
- **HDFS**: Underlying storage

## Data Model
- **Table**: Collection of rows
- **Row Key**: Unique identifier (sorted byte array)
- **Column Family**: Logical grouping of columns
- **Column Qualifier**: Specific column within a family
- **Timestamp**: Version for each cell
- **Cell**: Value at row+column+timestamp

## Schema Design
```java
// Create table
admin.createTable(TableDescriptorBuilder.newBuilder(TableName.valueOf("users"))
    .setColumnFamily(ColumnFamilyDescriptorBuilder.of("info"))
    .setColumnFamily(ColumnFamilyDescriptorBuilder.of("activity"))
    .build());
```

## Basic Operations
```java
// Put
Table table = connection.getTable(TableName.valueOf("users"));
Put put = new Put(Bytes.toBytes("row1"));
put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("John"));
table.put(put);

// Get
Get get = new Get(Bytes.toBytes("row1"));
Result result = table.get(get);
String name = Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name")));

// Scan
Scan scan = new Scan();
scan.withStartRow(Bytes.toBytes("row1"));
scan.withStopRow(Bytes.toBytes("row9"));
ResultScanner scanner = table.getScanner(scan);

// Delete
Delete delete = new Delete(Bytes.toBytes("row1"));
table.delete(delete);
```

## Best Practices
1. Keep row keys short and evenly distributed
2. Design column families based on access patterns
3. Use Bloom filters for random reads
4. Pre-split tables for even distribution
5. Monitor region server balance
