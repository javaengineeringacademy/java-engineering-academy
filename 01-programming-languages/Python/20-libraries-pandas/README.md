# Pandas — Data Analysis Made Simple

> **Data is messy. Pandas makes it clean, queryable, and ready for insight.**

## What

Pandas provides two core data structures — `DataFrame` (2D labeled table) and `Series` (1D labeled array) — that make loading, cleaning, transforming, and analyzing structured data intuitive and efficient. Built on NumPy, it adds SQL-like operations, time series support, and flexible indexing.

## Why

- **Data ingestion:** Read CSV, Excel, JSON, SQL, Parquet, and more in one line.
- **Cleaning power:** Handle missing values, duplicates, type conversions, and outliers effortlessly.
- **Transformation:** Groupby, merge, pivot, melt, and reshape data with a expressive API.
- **Analysis:** Statistical summaries, correlation, time series resampling — all built-in.
- **Integration:** Works seamlessly with NumPy, scikit-learn, Matplotlib, and databases.

## When

| Scenario | Pandas Approach | Why |
|----------|----------------|-----|
| Load CSV data | `pd.read_csv()` | Automatic type inference, missing value detection |
| Filter rows | `df[df['age'] > 30]` or `df.query()` | Boolean indexing, readable syntax |
| Aggregate by group | `df.groupby('category').sum()` | SQL GROUP BY in one line |
| Merge datasets | `pd.merge(df1, df2, on='key')` | SQL-like joins with control |
| Time series | `df.resample('M').mean()` | Resample, shift, rolling windows |
| Pivot tables | `df.pivot_table(values, index, columns)` | Cross-tabulation made easy |
| Handle missing data | `df.dropna()`, `df.fillna()` | Flexible NaN handling |
| Export results | `df.to_csv()`, `df.to_excel()` | Write to multiple formats |

## How

### DataFrame Basics

```python
import pandas as pd

# Create from dict
df = pd.DataFrame({
    'name': ['Alice', 'Bob', 'Charlie', 'Diana'],
    'age': [28, 35, 42, 31],
    'city': ['NYC', 'LA', 'NYC', 'Chicago'],
    'salary': [75000, 82000, 90000, 68000]
})

# From CSV
df = pd.read_csv('data.csv')

# Inspect
print(df.head())          # First 5 rows
print(df.info())          # Column types, non-null counts
print(df.describe())      # Statistical summary
print(df.shape)           # (rows, columns)
print(df.dtypes)          # Column data types
```

### Selection and Filtering

```python
# Column selection
names = df['name']               # Series
subset = df[['name', 'age']]     # DataFrame

# Row selection
first_row = df.iloc[0]           # By integer position
named_row = df.loc[0]           # By index label

# Boolean filtering
seniors = df[df['age'] > 35]
nyc_salary = df[(df['city'] == 'NYC') & (df['salary'] > 70000)]

# Query syntax (readable for complex conditions)
result = df.query("age > 30 and city == 'NYC'")

# Conditional assignment
df['senior'] = df['age'] > 35
```

### Data Cleaning

```python
# Handle missing values
df.dropna()                           # Drop rows with any NaN
df.dropna(subset=['age'])             # Drop rows where 'age' is NaN
df['age'].fillna(df['age'].mean())    # Fill NaN with mean
df['city'].fillna('Unknown')          # Fill NaN with constant

# Remove duplicates
df.drop_duplicates()
df.drop_duplicates(subset=['name'], keep='last')

# Type conversion
df['age'] = df['age'].astype(int)
df['date'] = pd.to_datetime(df['date'])

# String operations
df['name'] = df['name'].str.lower()
df['email'] = df['email'].str.contains('@')
```

### Groupby and Aggregation

```python
# Basic groupby
city_stats = df.groupby('city').agg({
    'salary': ['mean', 'max'],
    'age': ['mean', 'count']
})

# Multiple aggregations
summary = df.groupby('city').agg(
    avg_salary=('salary', 'mean'),
    headcount=('name', 'count'),
    salary_range=('salary', lambda x: x.max() - x.min())
).reset_index()

# Transform (preserves original shape)
df['city_avg_salary'] = df.groupby('city')['salary'].transform('mean')
df['salary_rank'] = df['salary'].rank(ascending=False)
```

### Merge and Join

```python
# Inner merge (only matching keys)
merged = pd.merge(employees, departments, on='dept_id')

# Left merge (keep all from left)
merged = pd.merge(employees, departments, on='dept_id', how='left')

# Join on index
df_joined = df.set_index('dept_id').join(dept_df)

# Concatenate
vertical = pd.concat([df1, df2], ignore_index=True)  # Stack rows
horizontal = pd.concat([df1, df2], axis=1)            # Stack columns
```

### Pivot and Reshape

```python
# Pivot table
pivot = df.pivot_table(
    values='salary',
    index='city',
    columns='department',
    aggfunc='mean',
    fill_value=0
)

# Melt (wide to long)
melted = pd.melt(df, id_vars=['name'], value_vars=['math', 'science'])

# Explode (list to rows)
df['skills'] = df['skills'].str.split(',')
exploded = df.explode('skills')
```

### Time Series

```python
# Parse dates
df['date'] = pd.to_datetime(df['date'])
df.set_index('date', inplace=True)

# Resample to monthly averages
monthly = df.resample('M')['sales'].mean()

# Rolling window
df['rolling_7d'] = df['sales'].rolling(window=7).mean()

# Shift and diff
df['lag_1'] = df['sales'].shift(1)
df['pct_change'] = df['sales'].pct_change()

# Date components
df['month'] = df.index.month
df['day_of_week'] = df.index.day_name()
```

## Production Checklist

- [ ] **Specify dtypes on read** — prevents memory waste and type errors
- [ ] **Use `parse_dates` in `read_csv`** — avoids manual date conversion
- [ ] **Check for duplicates early** — `df.duplicated().sum()`
- [ ] **Use `.query()` or boolean indexing** — avoids chained assignment warnings
- [ ] **Chunk large files** — `pd.read_csv(chunksize=10000)` for memory efficiency
- [ ] **Use Parquet for storage** — columnar, compressed, fast reads
- [ ] **Set index for time series** — enables `.loc[]` slicing and resampling
- [ ] **Profile memory usage** — `df.info(memory_usage='deep')`

## Maturity Levels

| Level | Name | Characteristics |
|-------|------|----------------|
| 1 | **CSV Reader** | `pd.read_csv()` and `print(df)`. No transformation. |
| 2 | **Basic Cleaning** | Filtering, dropna, type conversion. Simple groupby. |
| 3 | **Data Wrangling** | Merges, pivots, time series, complex aggregations. |
| 4 | **Optimized** | Chunked processing, Parquet, memory-efficient dtypes, pipeline architecture. |
| 5 | **Expert** | Custom accessors, extension arrays, Dask/Polars for out-of-core processing. |

## Common Myths

### Myth 1: "Pandas is slow, use NumPy instead"
**Reality:** Pandas uses NumPy under the hood. For tabular data with labels, groups, and joins, Pandas is faster than hand-rolled NumPy code. For pure numerical operations on homogeneous arrays, NumPy wins. Use the right tool for the job.

### Myth 2: "Chained assignment is fine: `df[df['a'] > 1]['b'] = 0`"
**Reality:** This triggers a `SettingWithCopyWarning` and may not modify the original DataFrame. Use `.loc[]` for assignment: `df.loc[df['a'] > 1, 'b'] = 0`.

### Myth 3: "Pandas can't handle datasets larger than RAM"
**Reality:** Modern Pandas supports chunked processing, and libraries like Dask and Polars extend Pandas-like operations to out-of-core and distributed datasets. For truly massive data, these are production-ready.

## One-Minute Revision

| Operation | Syntax | Purpose |
|-----------|--------|---------|
| Read CSV | `pd.read_csv(path)` | Load tabular data |
| Inspect | `df.info()`, `df.describe()` | Understand structure and statistics |
| Filter | `df[df['col'] > val]` | Boolean row selection |
| Select | `df[['col1', 'col2']]` | Column subset |
| Group | `df.groupby('col').agg()` | Aggregate by category |
| Merge | `pd.merge(df1, df2, on='key')` | Combine datasets |
| Pivot | `df.pivot_table(vals, idx, cols)` | Reshape data |
| Clean | `dropna()`, `fillna()`, `drop_duplicates()` | Handle messy data |
| Time | `df.resample('M').mean()` | Time series aggregation |
| Export | `df.to_csv()`, `df.to_parquet()` | Save results |

## Related Topics

- [19-libraries-numpy](../19-libraries-numpy/) - NumPy foundation for Pandas internals
- [24-libraries-sqlalchemy](../24-libraries-sqlalchemy/) - Database ↔ DataFrame pipeline
- [15-performance](../15-performance/) - Memory optimization strategies
- [08-file-io](../08-file-io/) - File format handling

---

> **Remember:** Pandas is your data wrangling Swiss Army knife. Master groupby, merge, and clean — and you can handle any dataset.
