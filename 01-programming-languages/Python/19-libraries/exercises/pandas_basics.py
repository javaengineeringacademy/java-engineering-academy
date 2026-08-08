"""
Module 19 - Libraries: Pandas Basics Exercises
Difficulty: ⭐⭐⭐ (Intermediate)
Topic: Pandas DataFrame operations
"""

import pandas as pd
import numpy as np


# =============================================================================
# Exercise 1: DataFrame Creation (⭐⭐⭐)
# =============================================================================

def exercise_1_dataframe_creation():
    """
    Create DataFrames in different ways.
    
    TODO:
    1. Create from dictionary
    2. Create from list of dicts
    3. Create with specific index
    """
    # TODO: Create DataFrames
    pass


# =============================================================================
# Exercise 2: Data Selection (⭐⭐⭐)
# =============================================================================

def exercise_2_data_selection():
    """
    Select data from DataFrames.
    
    TODO:
    1. Select columns
    2. Select rows by label
    3. Select rows by condition
    """
    # TODO: Select data
    pass


# =============================================================================
# Exercise 3: Data Transformation (⭐⭐⭐⭐)
# =============================================================================

def exercise_3_data_transformation():
    """
    Transform DataFrame data.
    
    TODO:
    1. Apply functions
    2. Add new columns
    3. Rename columns
    """
    # TODO: Transform data
    pass


# =============================================================================
# Exercise 4: Grouping and Aggregation (⭐⭐⭐⭐)
# =============================================================================

def exercise_4_grouping():
    """
    Group and aggregate data.
    
    TODO:
    1. Group by column
    2. Aggregate functions
    3. Multiple aggregations
    """
    # TODO: Group and aggregate
    pass


# =============================================================================
# Exercise 5: Data Merging (⭐⭐⭐⭐⭐)
# =============================================================================

def exercise_5_merging():
    """
    Merge multiple DataFrames.
    
    TODO:
    1. Merge on common column
    2. Join DataFrames
    3. Concatenate DataFrames
    """
    # TODO: Merge DataFrames
    pass


# =============================================================================
# Test Cases
# =============================================================================

def test_exercises():
    print("Testing Module 19 - Pandas Basics Exercises\n")
    
    # Test Exercise 1
    print("Exercise 1: DataFrame Creation")
    try:
        result = exercise_1_dataframe_creation()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 2
    print("Exercise 2: Data Selection")
    try:
        result = exercise_2_data_selection()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 3
    print("Exercise 3: Data Transformation")
    try:
        result = exercise_3_data_transformation()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 4
    print("Exercise 4: Grouping and Aggregation")
    try:
        result = exercise_4_grouping()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")
    
    # Test Exercise 5
    print("Exercise 5: Data Merging")
    try:
        result = exercise_5_merging()
        print(f"  Result: {result}")
        print("  ✓ Passed\n")
    except Exception as e:
        print(f"  ✗ Failed: {e}\n")


if __name__ == "__main__":
    test_exercises()
