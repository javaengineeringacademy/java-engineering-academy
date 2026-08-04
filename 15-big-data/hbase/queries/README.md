# HBase Queries

## Scan Operations
```java
// Basic scan
Scan scan = new Scan();
scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));
ResultScanner scanner = table.getScanner(scan);

// Filtered scan
FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ALL);
filters.add(new PrefixFilter(Bytes.toBytes("user_")));
filters.add(new ColumnPrefixFilter(Bytes.toBytes("email")));
filters.add(new SingleColumnValueFilter(
    Bytes.toBytes("info"), Bytes.toBytes("age"),
    CompareOperator.GREATER_OR_EQUAL, Bytes.toBytes(18)));
scan.setFilter(filters);
```

## Filters
```java
// Row filter
scan.setFilter(new RowFilter(CompareOperator.EQUAL,
    new RegexStringComparator("user_.*")));

// Value filter
scan.setFilter(new ValueFilter(CompareOperator.EQUAL,
    new SubstringComparator("gmail")));

// Column count limit
scan.setFilter(new ColumnCountGetFilter(10));

// Page filter
scan.setFilter(new PageFilter(100));

// First key only
scan.setFilter(new FirstKeyOnlyFilter());
```

## Aggregation
```java
// Use Coprocessor for aggregation
public class SumCoprocessor extends BaseRegionObserver {
    @Override
    public void preScannerOp(ObserverContext<RegionCoprocessorEnvironment> e,
                             Scan scan) throws IOException {
        // Add aggregation logic
    }
}
```

## Secondary Indexes
```java
// Manual index table
public class IndexBuilder {
    public Put createIndexEntry(String rowKey, String indexField, String value) {
        Put indexPut = new Put(Bytes.toBytes(value + "_" + rowKey));
        indexPut.addColumn(Bytes.toBytes("idx"), Bytes.toBytes("pk"), Bytes.toBytes(rowKey));
        return indexPut;
    }
}
```

## Best Practices
1. Limit scan ranges with start/stop rows
2. Use filters to reduce data transfer
3. Implement pagination for large result sets
4. Use coprocessors for server-side aggregation
5. Monitor scan performance with metrics
