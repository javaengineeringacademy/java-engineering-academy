"""Data loading module for exporting processed data."""

import csv
import json
import os
from typing import List, Dict
from datetime import datetime


class DataLoader:
    """Handles data loading to various destinations."""
    
    def __init__(self, output_dir: str = "output"):
        """Initialize loader with output directory."""
        self.output_dir = output_dir
        self._ensure_directory()
        self.loaded_count = 0
    
    def _ensure_directory(self) -> None:
        """Create output directory if it doesn't exist."""
        if not os.path.exists(self.output_dir):
            os.makedirs(self.output_dir)
    
    def to_csv(self, data: List[Dict], filename: str) -> str:
        """Load data to CSV file."""
        if not data:
            raise ValueError("No data to export")
        
        filepath = os.path.join(self.output_dir, filename)
        fieldnames = list(data[0].keys())
        
        with open(filepath, "w", newline="", encoding="utf-8") as f:
            writer = csv.DictWriter(f, fieldnames=fieldnames)
            writer.writeheader()
            writer.writerows(data)
        
        self.loaded_count += len(data)
        return filepath
    
    def to_json(self, data: List[Dict], filename: str) -> str:
        """Load data to JSON file."""
        filepath = os.path.join(self.output_dir, filename)
        
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
        
        self.loaded_count += len(data)
        return filepath
    
    def to_console(self, data: List[Dict], limit: int = 10) -> None:
        """Print data to console."""
        print(f"\nLoaded {len(data)} records:")
        print("-" * 60)
        
        for i, row in enumerate(data[:limit]):
            print(f"Row {i + 1}: {row}")
        
        if len(data) > limit:
            print(f"... and {len(data) - limit} more rows")
        
        self.loaded_count += len(data)
    
    def generate_report(self, data: List[Dict], metadata: Dict = None) -> str:
        """Generate a processing report."""
        report = {
            "report_generated": datetime.now().isoformat(),
            "total_records": len(data),
            "columns": list(data[0].keys()) if data else [],
            "loaded_count": self.loaded_count,
            "metadata": metadata or {}
        }
        
        # Add summary statistics
        if data and "total" in data[0]:
            totals = [row.get("total", 0) for row in data]
            report["statistics"] = {
                "total_revenue": sum(totals),
                "average_order": sum(totals) / len(totals) if totals else 0,
                "max_order": max(totals) if totals else 0,
                "min_order": min(totals) if totals else 0
            }
        
        return json.dumps(report, indent=2)
    
    def get_stats(self) -> Dict:
        """Get loader statistics."""
        return {
            "output_directory": self.output_dir,
            "total_loaded": self.loaded_count,
            "files_created": os.listdir(self.output_dir) if os.path.exists(self.output_dir) else []
        }
