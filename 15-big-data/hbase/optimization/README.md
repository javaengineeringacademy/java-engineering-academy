# HBase Performance Optimization

## Row Key Design
```java
// BAD: Sequential keys create hotspots
"0001", "0002", "0003"

// GOOD: Salted/hashed keys
byte[] salt = Bytes.toBytes(hash(rowKey) % NUM_REGIONS);
byte[] saltedKey = Bytes.add(salt, Bytes.toBytes(rowKey));

// GOOD: Reverse domain as row key
"com.example.www" -> "www.example.com"
```

## Column Family Tuning
```java
// Table descriptor tuning
ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("info"))
    .setMaxVersions(3)           // Keep 3 versions
    ..setInMemory(true)          // Cache in memory
    ..setBlockCacheEnabled(true) // Enable block cache
    ..setBloomFilterType(BloomType.ROW)  // Row-level bloom filter
    .setDataBlockEncoding(DataBlockEncoding.PREFIX) // Compression
    .setMaxFileSize(1073741824L) // 1GB region size
    .build();
```

## Compaction
```java
// Major compaction
admin.majorCompact(TableName.valueOf("users"));

// Compaction filter
public class TTLCompactionFilter extends FilterBase {
    @Override
    public boolean filterKeyValue(Cell v) {
        if (System.currentTimeMillis() - v.getTimestamp() > TTL_MS) {
            return Filter.ReturnCode.SKIP;
        }
        return Filter.ReturnCode.INCLUDE;
    }
}
```

## Region Management
```java
// Pre-split regions
byte[][] splits = {Bytes.toBytes("A"), Bytes.toBytes("M"), Bytes.toBytes("Z")};
admin.createTable(descriptor, splits);

// Merge regions
admin.mergeRegionsAsync(
    Bytes.toBytesBytes(region1), Bytes.toBytesBytes(region2), false);
```

## Caching
```java
// Block cache configuration
Configuration conf = HBaseConfiguration.create();
conf.set("hfile.block.cache.size", "0.4");  // 40% of heap
conf.set("hbase.mob.file.cache.size", "1000");

// Client-side scan caching
scan.setCaching(1000);  // Fetch 1000 rows per RPC
scan.setBatch(100);     // Fetch 100 columns per RPC
```

## Best Practices
1. Monitor region server metrics
2. Tune block cache and memstore size
3. Use appropriate compression (Snappy, LZ4)
4. Implement bucket cache for off-heap caching
5. Regular major compaction during off-peak
6. Monitor store file count and size
