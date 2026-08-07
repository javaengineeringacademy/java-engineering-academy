"""Data transformation module for cleaning and processing data."""

from typing import List, Dict, Callable
from datetime import datetime


class DataTransformer:
    """Handles data transformation and cleaning operations."""
    
    def __init__(self):
        """Initialize transformer with empty pipeline."""
        self.transformations: List[Callable] = []
        self.validation_errors: List[str] = []
    
    def add_transformation(self, func: Callable) -> "DataTransformer":
        """Add a transformation function to the pipeline."""
        self.transformations.append(func)
        return self
    
    def apply(self, data: List[Dict]) -> List[Dict]:
        """Apply all transformations to the data."""
        self.validation_errors = []
        result = data.copy()
        
        for transformation in self.transformations:
            try:
                result = transformation(result)
            except Exception as e:
                self.validation_errors.append(f"Transformation error: {e}")
        
        return result
    
    @staticmethod
    def remove_empty_rows(data: List[Dict]) -> List[Dict]:
        """Remove rows where all values are empty."""
        return [row for row in data if any(str(v).strip() for v in row.values())]
    
    @staticmethod
    def convert_types(data: List[Dict], type_map: Dict[str, type]) -> List[Dict]:
        """Convert column types based on type map."""
        result = []
        for row in data:
            new_row = {}
            for key, value in row.items():
                if key in type_map:
                    try:
                        new_row[key] = type_map[key](value)
                    except (ValueError, TypeError):
                        new_row[key] = value
                else:
                    new_row[key] = value
            result.append(new_row)
        return result
    
    @staticmethod
    def calculate_total(data: List[Dict]) -> List[Dict]:
        """Calculate total price (quantity * price) for each row."""
        for row in data:
            try:
                quantity = float(row.get("quantity", 0))
                price = float(row.get("price", 0))
                row["total"] = round(quantity * price, 2)
            except (ValueError, TypeError):
                row["total"] = 0.0
        return data
    
    @staticmethod
    def normalize_strings(data: List[Dict], columns: List[str]) -> List[Dict]:
        """Normalize string columns to lowercase and strip whitespace."""
        for row in data:
            for col in columns:
                if col in row and isinstance(row[col], str):
                    row[col] = row[col].lower().strip()
        return data
    
    @staticmethod
    def filter_by_condition(
        data: List[Dict],
        column: str,
        condition: Callable[[any], bool]
    ) -> List[Dict]:
        """Filter rows based on a condition."""
        return [row for row in data if condition(row.get(column))]
    
    @staticmethod
    def add_timestamp(data: List[Dict]) -> List[Dict]:
        """Add processing timestamp to each row."""
        timestamp = datetime.now().isoformat()
        for row in data:
            row["processed_at"] = timestamp
        return data
    
    @staticmethod
    def deduplicate(data: List[Dict], key: str = "id") -> List[Dict]:
        """Remove duplicate rows based on key."""
        seen = set()
        result = []
        for row in data:
            value = row.get(key)
            if value not in seen:
                seen.add(value)
                result.append(row)
        return result
    
    def get_errors(self) -> List[str]:
        """Get validation errors from last transformation."""
        return self.validation_errors
