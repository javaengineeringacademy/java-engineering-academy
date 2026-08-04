# Delta Lake Operations

## CRUD Operations
```python
# Insert
df.write.format("delta").mode("append").save("/delta/events")

# Overwrite
df.write.format("delta").mode("overwrite").save("/delta/events")

# Delete
deltaTable.delete("event_type = 'old'")

# Update
deltaTable.update(
    condition="event_type = 'click'",
    set={"event_type": "'page_view'"}
)

# Merge (upsert)
deltaTable.alias("target").merge(
    source.alias("source"),
    "target.id = source.id"
).whenMatchedUpdateAll()  .whenNotMatchedInsertAll()  .execute()
```

## Time Travel
```python
# Read specific version
df = spark.read.format("delta").option("versionAsOf", 5).load("/delta/events")

# Read by timestamp
df = spark.read.format("delta").option("timestampAsOf", "2024-01-15").load("/delta/events")

# Get history
history = deltaTable.history()
history.select("version", "timestamp", "operation").show()
```

## Maintenance
```python
# Optimize (compaction + Z-order)
deltaTable.optimize().executeCompaction()
deltaTable.optimize().executeZOrderBy("user_id", "event_type")

# Vacuum (delete old files)
deltaTable.vacuum(retentionHours=168)  # 7 days

# Restore to version
deltaTable.restoreToVersion(5)
```

## Best Practices
1. Run optimize regularly for performance
2. Set appropriate vacuum retention
3. Monitor transaction log size
4. Use Z-ordering for common filters
5. Enable change data feed for downstream
