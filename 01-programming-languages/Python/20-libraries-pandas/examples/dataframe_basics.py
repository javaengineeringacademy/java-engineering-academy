"""
Pandas DataFrame Basics
Demonstrates core Pandas concepts: creation, selection, cleaning, and aggregation
"""

import pandas as pd
import numpy as np

# ============================================
# DataFrame Creation
# ============================================

def dataframe_creation():
    print("=== DataFrame Creation ===")

    # From dictionary
    df = pd.DataFrame({
        'name': ['Alice', 'Bob', 'Charlie', 'Diana', 'Eve'],
        'age': [28, 35, 42, 31, 26],
        'city': ['NYC', 'LA', 'NYC', 'Chicago', 'LA'],
        'salary': [75000, 82000, 90000, 68000, 71000]
    })
    print(f"DataFrame:\n{df}\n")

    # Inspect
    print(f"Shape: {df.shape}")
    print(f"Columns: {list(df.columns)}")
    print(f"dtypes:\n{df.dtypes}\n")

    # Statistical summary
    print(f"Describe:\n{df.describe()}\n")

    return df

# ============================================
# Selection and Filtering
# ============================================

def selection_examples(df):
    print("\n=== Selection and Filtering ===")

    # Column selection
    print(f"Name column:\n{df['name']}\n")

    # Multiple columns
    print(f"Name and age:\n{df[['name', 'age']]}\n")

    # Row selection by position
    print(f"First row (iloc[0]):\n{df.iloc[0]}\n")

    # Boolean filtering
    seniors = df[df['age'] > 35]
    print(f"Seniors (age > 35):\n{seniors}\n")

    # Multiple conditions
    nyc_high = df[(df['city'] == 'NYC') & (df['salary'] > 70000)]
    print(f"NYC with salary > 70k:\n{nyc_high}\n")

    # Query syntax
    result = df.query("age > 30 and city == 'NYC'")
    print(f"Query result:\n{result}\n")

# ============================================
# Data Cleaning
# ============================================

def cleaning_examples():
    print("\n=== Data Cleaning ===")

    # Create DataFrame with missing values
    df = pd.DataFrame({
        'name': ['Alice', 'Bob', None, 'Diana', 'Eve'],
        'age': [28, np.nan, 42, 31, 26],
        'city': ['NYC', 'LA', 'NYC', None, 'LA'],
        'salary': [75000, 82000, 90000, 68000, np.nan]
    })
    print(f"Original:\n{df}\n")

    # Check missing values
    print(f"Missing values:\n{df.isnull().sum()}\n")

    # Drop rows with any NaN
    dropped = df.dropna()
    print(f"After dropna:\n{dropped}\n")

    # Fill missing values
    filled = df.copy()
    filled['age'] = filled['age'].fillna(filled['age'].mean())
    filled['city'] = filled['city'].fillna('Unknown')
    print(f"After fillna:\n{filled}\n")

    # Drop duplicates
    df2 = pd.DataFrame({'name': ['Alice', 'Bob', 'Alice'], 'age': [28, 35, 28]})
    print(f"Before dedup:\n{df2}")
    deduped = df2.drop_duplicates()
    print(f"After dedup:\n{deduped}\n")

# ============================================
# Groupby and Aggregation
# ============================================

def groupby_examples(df):
    print("\n=== Groupby and Aggregation ===")

    # Basic groupby
    city_stats = df.groupby('city')['salary'].mean()
    print(f"Average salary by city:\n{city_stats}\n")

    # Multiple aggregations
    detailed = df.groupby('city').agg(
        avg_salary=('salary', 'mean'),
        count=('name', 'count'),
        salary_range=('salary', lambda x: x.max() - x.min())
    ).reset_index()
    print(f"Detailed city stats:\n{detailed}\n")

    # Transform (preserves shape)
    df_copy = df.copy()
    df_copy['city_avg'] = df_copy.groupby('city')['salary'].transform('mean')
    df_copy['salary_rank'] = df_copy['salary'].rank(ascending=False)
    print(f"With transforms:\n{df_copy}\n")

# ============================================
# Merge and Join
# ============================================

def merge_examples():
    print("\n=== Merge and Join ===")

    employees = pd.DataFrame({
        'emp_id': [1, 2, 3, 4],
        'name': ['Alice', 'Bob', 'Charlie', 'Diana'],
        'dept_id': [101, 102, 101, 103]
    })

    departments = pd.DataFrame({
        'dept_id': [101, 102, 104],
        'dept_name': ['Engineering', 'Marketing', 'Sales']
    })

    # Inner merge
    merged = pd.merge(employees, departments, on='dept_id', how='inner')
    print(f"Inner merge:\n{merged}\n")

    # Left merge
    left_merge = pd.merge(employees, departments, on='dept_id', how='left')
    print(f"Left merge:\n{left_merge}\n")

# ============================================
# Pivot Table
# ============================================

def pivot_examples(df):
    print("\n=== Pivot Table ===")

    # Add department for pivot demo
    df_copy = df.copy()
    df_copy['department'] = ['Engineering', 'Marketing', 'Engineering', 'Sales', 'Marketing']

    # Pivot table
    pivot = df_copy.pivot_table(
        values='salary',
        index='city',
        columns='department',
        aggfunc='mean',
        fill_value=0
    )
    print(f"Pivot table:\n{pivot}\n")

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    df = dataframe_creation()
    selection_examples(df)
    cleaning_examples()
    groupby_examples(df)
    merge_examples()
    pivot_examples(df)

    print("All Pandas examples completed!")
