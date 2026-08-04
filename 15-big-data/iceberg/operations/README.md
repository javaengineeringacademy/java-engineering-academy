# Iceberg Operations

## Snapshot Management
```java
// List snapshots
for (Snapshot snapshot : table.snapshots()) {
    System.out.println("Snapshot: " + snapshot.snapshotId());
    System.out.println("Timestamp: " + snapshot.timestampMillis());
    System.out.println("Operation: " + snapshot.operation());
}

// Time travel
Table scan = table.newScan().useSnapshotId(snapshotId);
// or
Table scan = table.newScan().useRef("tag-1.0");

// Rollback
table manageSnapshots().rollbackToSnapshotId(snapshotId).commit();
```

## Data Operations
```java
// Append data
DataFile file = Parquet.write(table, dataStream).create();
table.newFastAppend().appendFile(file).commit();

// Overwrite data
table.newOverwrite().overwriteByRowFilter(filter).commit();

// Delete data
table.newDelete().deleteFromRowFilter(filter).commit();

// Schema evolution
table.updateSchema().addColumn("new_col", Types.StringType.get()).commit();
```

## Compaction
```java
// Rewrite data files for better performance
RewriteFiles rewriteFiles = table.newRewrite();
for (DataFile oldFile : oldFiles) {
    rewriteFiles.removeFile(oldFile);
}
for (DataFile newFile : newFiles) {
    rewriteFiles.addFile(newFile);
}
rewriteFiles.commit();
```

## Best Practices
1. Monitor snapshot count and age
2. Expire old snapshots regularly
3. Compact small files during off-peak
4. Use partition transforms for time-series
5. Enable vectorized reads for performance
