# Pandas

## Why Pandas Exists

Every Python developer who works with data eventually needs to load, clean, and analyze structured data from CSVs, databases, or APIs. Writing raw Python to handle missing values, group rows, and merge datasets is tedious and error-prone. Pandas was created to solve this by providing DataFrame and Series objects that make data wrangling intuitive and efficient.

## What You'll Learn

By the end of this section, you'll be able to:

- Load and inspect data from CSV, Excel, JSON, and SQL sources
- Clean data by handling missing values, duplicates, and type conversions
- Transform data using groupby, merge, pivot, and time series operations

## When to Use Pandas

| Use Case | Why Pandas | Alternative |
|----------|-----------|-------------|
| Load CSV data | Automatic type inference, missing value detection | `csv` module |
| Filter rows | Boolean indexing, readable syntax | List comprehensions |
| Aggregate by group | SQL GROUP BY in one line | Manual dictionaries |
| Merge datasets | SQL-like joins with control | Dictionary lookups |
| Time series | Resample, shift, rolling windows | `datetime` module |
| Pivot tables | Cross-tabulation made easy | Manual iteration |

## How Pandas Works Internally

Pandas builds on NumPy arrays but adds axis labels (index and columns). A DataFrame is essentially a dictionary of Series objects, where each Series shares the same index. When you filter rows, Pandas creates a view or copy of the underlying NumPy arrays, not a new data structure.

Groupby operations use a split-apply-combine strategy. First, the data is split into groups based on the key. Then, a function is applied to each group independently. Finally, the results are combined back into a single DataFrame. This happens efficiently because Pandas operates on NumPy arrays under the hood.

```python
import pandas as pd

# Create DataFrame
df = pd.DataFrame({
    'name': ['Alice', 'Bob', 'Charlie'],
    'age': [28, 35, 42],
    'city': ['NYC', 'LA', 'NYC']
})

# Filtering
seniors = df[df['age'] > 30]

# Groupby
city_stats = df.groupby('city').agg({'age': ['mean', 'count']})

# Merge
merged = pd.merge(df1, df2, on='key', how='left')
```

## Production Checklist

### ✅ Before using Pandas in production:

☐ I know the time/space complexity
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Pandas is slow, use NumPy instead
**Reality:** Pandas uses NumPy under the hood. For tabular data with labels, groups, and joins, Pandas is faster than hand-rolled NumPy code. For pure numerical operations on homogeneous arrays, NumPy wins.

### ❌ Myth 2: Chained assignment is fine
**Reality:** `df[df['a'] > 1]['b'] = 0` triggers a `SettingWithCopyWarning` and may not modify the original DataFrame. Use `.loc[]` for assignment: `df.loc[df['a'] > 1, 'b'] = 0`.

### ❌ Myth 3: Pandas can't handle datasets larger than RAM
**Reality:** Modern Pandas supports chunked processing, and libraries like Dask and Polars extend Pandas-like operations to out-of-core and distributed datasets.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Data analysis and manipulation |
| Complexity | O(n) for most operations |
| Thread Safe | No |
| Best Alternative | Polars or Dask for large data |
| When to Use | Tabular data analysis |
| When to Avoid | Very large datasets exceeding RAM |

## Related Topics

- [01-numpy](../01-numpy/) - NumPy foundation for Pandas internals
- [06-sqlalchemy](../06-sqlalchemy/) - Database ↔ DataFrame pipeline
- [14-matplotlib](../14-matplotlib/) - Visualization from DataFrames

## References
- Pandas Documentation: https://pandas.pydata.org/docs/
- Pandas User Guide: https://pandas.pydata.org/docs/user_guide/index.html
- 10 Minutes to Pandas: https://pandas.pydata.org/docs/user_guide/10min.html

## Version Validation
- Verified against: Pandas 2.1+, Python 3.10+
