"""Data storage and export functionality."""

import csv
import json
import os
from typing import List, Dict


class Storage:
    """Handles data export to CSV and JSON formats."""
    
    def __init__(self, output_dir: str = "output"):
        """Initialize storage with output directory."""
        self.output_dir = output_dir
        self._ensure_directory()
    
    def _ensure_directory(self) -> None:
        """Create output directory if it doesn't exist."""
        if not os.path.exists(self.output_dir):
            os.makedirs(self.output_dir)
    
    def to_json(self, data: List[Dict], filename: str) -> str:
        """Export data to JSON file."""
        filepath = os.path.join(self.output_dir, filename)
        
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
        
        return filepath
    
    def to_csv(self, data: List[Dict], filename: str) -> str:
        """Export data to CSV file."""
        if not data:
            return ""
        
        filepath = os.path.join(self.output_dir, filename)
        
        # Get all unique keys from data
        fieldnames = list(set().union(*(d.keys() for d in data)))
        
        with open(filepath, "w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(data)
        
        return filepath
    
    def append_json(self, data: List[Dict], filename: str) -> str:
        """Append data to existing JSON file or create new."""
        filepath = os.path.join(self.output_dir, filename)
        
        existing = []
        if os.path.exists(filepath):
            with open(filepath, "r", encoding="utf-8") as f:
                existing = json.load(f)
        
        existing.extend(data)
        
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(existing, f, indent=2, ensure_ascii=False)
        
        return filepath
    
    def list_files(self) -> List[str]:
        """List all files in output directory."""
        return os.listdir(self.output_dir)
    
    def read_json(self, filename: str) -> List[Dict]:
        """Read data from JSON file."""
        filepath = os.path.join(self.output_dir, filename)
        
        if not os.path.exists(filepath):
            return []
        
        with open(filepath, "r", encoding="utf-8") as f:
            return json.load(f)
