"""
Module 19 - Libraries: Pandas Basics Solutions
Complete solutions with explanations
"""

import pandas as pd
import numpy as np


# =============================================================================
# Exercise 1: DataFrame Creation - SOLUTION
# =============================================================================

def exercise_1_dataframe_creation():
    """
    Create DataFrames in different ways.
    """
    # 1. Create from dictionary
    df_dict = pd.DataFrame({
        'Name': ['Alice', 'Bob', 'Charlie'],
        'Age': [25, 30, 35],
        'City': ['New York', 'London', 'Paris']
    })
    
    # 2. Create from list of dicts
    df_list = pd.DataFrame([
        {'Name': 'David', 'Age': 28, 'City': 'Tokyo'},
        {'Name': 'Eve', 'Age': 32, 'City': 'Sydney'}
    ])
    
    # 3. Create with specific index
    df_indexed = pd.DataFrame(
        [[1, 2, 3], [4, 5, 6]],
        index=['row1', 'row2'],
        columns=['col1', 'col2', 'col3']
    )
    
    # 4. Create with date range
    dates = pd.date_range('2024-01-01', periods=5)
    df_dates = pd.DataFrame({
        'Date': dates,
        'Value': [10, 20, 30, 40, 50]
    })
    
    return {
        'from_dict_shape': df_dict.shape,
        'from_list_shape': df_list.shape,
        'indexed_columns': list(df_indexed.columns),
        'dates_shape': df_dates.shape,
    }


# =============================================================================
# Exercise 2: Data Selection - SOLUTION
# =============================================================================

def exercise_2_data_selection():
    """
    Select data from DataFrames.
    """
    df = pd.DataFrame({
        'Name': ['Alice', 'Bob', 'Charlie', 'David'],
        'Age': [25, 30, 35, 28],
        'Salary': [50000, 60000, 70000, 55000]
    })
    
    # 1. Select columns
    names = df['Name']
    subset = df[['Name', 'Age']]
    
    # 2. Select rows by label
    first_row = df.iloc[0]
    first_two = df.iloc[:2]
    
    # 3. Select rows by condition
    high_salary = df[df['Salary'] > 55000]
    young = df[df['Age'] < 30]
    
    # 4. loc and iloc
    specific = df.loc[0:2, ['Name', 'Salary']]
    
    return {
        'names': names.tolist(),
        'high_salary': high_salary['Name'].tolist(),
        'young': young['Name'].tolist(),
        'specific': specific.to_dict(),
    }


# =============================================================================
# Exercise 3: Data Transformation - SOLUTION
# =============================================================================

def exercise_3_data_transformation():
    """
    Transform DataFrame data.
    """
    df = pd.DataFrame({
        'Name': ['Alice', 'Bob', 'Charlie'],
        'Age': [25, 30, 35],
        'Salary': [50000, 60000, 70000]
    })
    
    # 1. Apply functions
    df['Age_doubled'] = df['Age'].apply(lambda x: x * 2)
    
    # 2. Add new columns
    df['Salary_k'] = df['Salary'] / 1000
    
    # 3. Rename columns
    df_renamed = df.rename(columns={'Name': 'Employee', 'Age': 'Years'})
    
    # 4. Apply to entire DataFrame
    df['Name_upper'] = df['Name'].str.upper()
    
    # 5. Map values
    df['Age_group'] = df['Age'].map(
        lambda x: 'Young' if x < 30 else 'Senior'
    )
    
    return {
        'columns': list(df.columns),
        'age_doubled': df['Age_doubled'].tolist(),
        'renamed_columns': list(df_renamed.columns),
    }


# =============================================================================
# Exercise 4: Grouping and Aggregation - SOLUTION
# =============================================================================

def exercise_4_grouping():
    """
    Group and aggregate data.
    """
    df = pd.DataFrame({
        'Department': ['Sales', 'Sales', 'HR', 'HR', 'IT', 'IT'],
        'Employee': ['Alice', 'Bob', 'Charlie', 'David', 'Eve', 'Frank'],
        'Salary': [50000, 60000, 45000, 55000, 70000, 80000],
        'Age': [25, 30, 35, 28, 32, 40]
    })
    
    # 1. Group by column
    grouped = df.groupby('Department')
    
    # 2. Aggregate functions
    salary_stats = grouped['Salary'].agg(['mean', 'min', 'max'])
    
    # 3. Multiple aggregations
    dept_stats = grouped.agg({
        'Salary': ['mean', 'sum'],
        'Age': 'mean'
    })
    
    # 4. Transform
    df['Dept_avg_salary'] = grouped['Salary'].transform('mean')
    
    return {
        'departments': list(grouped.groups.keys()),
        'salary_stats': salary_stats.to_dict(),
        'dept_stats_shape': dept_stats.shape,
    }


# =============================================================================
# Exercise 5: Data Merging - SOLUTION
# =============================================================================

def exercise_5_merging():
    """
    Merge multiple DataFrames.
    """
    df1 = pd.DataFrame({
        'id': [1, 2, 3],
        'name': ['Alice', 'Bob', 'Charlie']
    })
    
    df2 = pd.DataFrame({
        'id': [2, 3, 4],
        'salary': [60000, 70000, 80000]
    })
    
    # 1. Merge on common column
    merged = pd.merge(df1, df2, on='id', how='inner')
    
    # 2. Left join
    left_merged = pd.merge(df1, df2, on='id', how='left')
    
    # 3. Join DataFrames
    df3 = df1.set_index('id')
    df4 = df2.set_index('id')
    joined = df3.join(df4, how='outer')
    
    # 4. Concatenate DataFrames
    df5 = pd.DataFrame({'A': [1, 2], 'B': [3, 4]})
    df6 = pd.DataFrame({'A': [5, 6], 'B': [7, 8]})
    concatenated = pd.concat([df5, df6], ignore_index=True)
    
    return {
        'merged': merged.to_dict(),
        'left_merged': left_merged.to_dict(),
        'concatenated': concatenated.to_dict(),
    }


# =============================================================================
# Test Cases (Uncommented)
# =============================================================================

def test_exercises():
    print("Testing Module 19 - Pandas Basics Solutions\n")
    
    # Test Exercise 1
    print("Exercise 1: DataFrame Creation")
    result = exercise_1_dataframe_creation()
    assert result['from_dict_shape'] == (3, 3)
    assert result['from_list_shape'] == (2, 3)
    print(f"  DataFrames created successfully")
    print("  ✓ Passed\n")
    
    # Test Exercise 2
    print("Exercise 2: Data Selection")
    result = exercise_2_data_selection()
    assert len(result['names']) == 4
    assert 'Charlie' in result['high_salary']
    assert 'Alice' in result['young']
    print(f"  Selection works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 3
    print("Exercise 3: Data Transformation")
    result = exercise_3_data_transformation()
    assert 'Age_doubled' in result['columns']
    assert result['age_doubled'] == [50, 60, 70]
    print(f"  Transformation works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 4
    print("Exercise 4: Grouping and Aggregation")
    result = exercise_4_grouping()
    assert len(result['departments']) == 3
    print(f"  Grouping works correctly")
    print("  ✓ Passed\n")
    
    # Test Exercise 5
    print("Exercise 5: Data Merging")
    result = exercise_5_merging()
    assert len(result['merged']['id']) == 2
    assert len(result['concatenated']['A']) == 4
    print(f"  Merging works correctly")
    print("  ✓ Passed\n")


if __name__ == "__main__":
    test_exercises()
