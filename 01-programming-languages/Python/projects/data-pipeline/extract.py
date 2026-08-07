"""Data extraction module for loading data from various sources."""

import csv
import json
import os
from typing import List, Dict, Optional


class DataExtractor:
    """Handles data extraction from different sources."""
    
    def from_csv(self, filepath: str) -> List[Dict]:
        """Extract data from CSV file."""
        if not os.path.exists(filepath):
            raise FileNotFoundError(f"File not found: {filepath}")
        
        data = []
        with open(filepath, "r", encoding="utf-8") as f:
            reader = csv.DictReader(f)
            for row in reader:
                data.append(dict(row))
        
        return data
    
    def from_json(self, filepath: str) -> List[Dict]:
        """Extract data from JSON file."""
        if not os.path.exists(filepath):
            raise FileNotFoundError(f"File not found: {filepath}")
        
        with open(filepath, "r", encoding="utf-8") as f:
            data = json.load(f)
        
        if isinstance(data, dict):
            data = [data]
        
        return data
    
    def from_dict(self, data: dict) -> List[Dict]:
        """Convert dictionary to list format."""
        if isinstance(data, list):
            return data
        return [data]
    
    def from_list(self, data: list) -> List[Dict]:
        """Validate and return list data."""
        return [item for item in data if isinstance(item, dict)]
    
    def generate_sample_data(self) -> List[Dict]:
        """Generate sample sales data for testing."""
        return [
            {"id": "1", "product": "Laptop", "quantity": "5", "price": "999.99", "date": "2024-01-15"},
            {"id": "2", "product": "Mouse", "quantity": "20", "price": "29.99", "date": "2024-01-16"},
            {"id": "3", "product": "Keyboard", "quantity": "15", "price": "79.99", "date": "2024-01-16"},
            {"id": "4", "product": "Monitor", "quantity": "8", "price": "449.99", "date": "2024-01-17"},
            {"id": "5", "product": "Headphones", "quantity": "30", "price": "149.99", "date": "2024-01-18"},
            {"id": "6", "product": "Webcam", "quantity": "12", "price": "89.99", "date": "2024-01-19"},
            {"id": "7", "product": "USB Hub", "quantity": "25", "price": "39.99", "date": "2024-01-20"},
            {"id": "8", "product": "Desk Lamp", "quantity": "10", "price": "59.99", "date": "2024-01-21"},
        ]
    
    def create_csv_sample(self, filepath: str) -> str:
        """Create a sample CSV file for testing."""
        sample_data = self.generate_sample_data()
        
        with open(filepath, "w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=sample_data[0].keys())
            writer.writeheader()
            writer.writerows(sample_data)
        
        return filepath
