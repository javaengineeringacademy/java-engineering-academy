"""Pipeline orchestration module."""

from typing import List, Dict, Callable, Optional
from extract import DataExtractor
from transform import DataTransformer
from load import DataLoader


class Pipeline:
    """Orchestrates ETL pipeline execution."""
    
    def __init__(self, name: str = "default"):
        """Initialize pipeline with name."""
        self.name = name
        self.extractor = DataExtractor()
        self.transformer = DataTransformer()
        self.loader = DataLoader()
        self.stages_completed = []
        self.data = []
        self.errors = []
    
    def extract_from_csv(self, filepath: str) -> "Pipeline":
        """Extract data from CSV file."""
        try:
            self.data = self.extractor.from_csv(filepath)
            self.stages_completed.append("extract")
            print(f"[Extract] Loaded {len(self.data)} rows from {filepath}")
        except Exception as e:
            self.errors.append(f"Extract error: {e}")
        return self
    
    def extract_from_json(self, filepath: str) -> "Pipeline":
        """Extract data from JSON file."""
        try:
            self.data = self.extractor.from_json(filepath)
            self.stages_completed.append("extract")
            print(f"[Extract] Loaded {len(self.data)} records from {filepath}")
        except Exception as e:
            self.errors.append(f"Extract error: {e}")
        return self
    
    def extract_sample(self) -> "Pipeline":
        """Extract sample data for testing."""
        self.data = self.extractor.generate_sample_data()
        self.stages_completed.append("extract")
        print(f"[Extract] Generated {len(self.data)} sample records")
        return self
    
    def transform(self, *transformations: Callable) -> "Pipeline":
        """Apply transformations to data."""
        try:
            for func in transformations:
                self.transformer.add_transformation(func)
            
            self.data = self.transformer.apply(self.data)
            self.stages_completed.append("transform")
            print(f"[Transform] Applied {len(transformations)} transformations")
        except Exception as e:
            self.errors.append(f"Transform error: {e}")
        return self
    
    def load_to_csv(self, filename: str) -> "Pipeline":
        """Load data to CSV file."""
        try:
            filepath = self.loader.to_csv(self.data, filename)
            self.stages_completed.append("load")
            print(f"[Load] Saved to {filepath}")
        except Exception as e:
            self.errors.append(f"Load error: {e}")
        return self
    
    def load_to_json(self, filename: str) -> "Pipeline":
        """Load data to JSON file."""
        try:
            filepath = self.loader.to_json(self.data, filename)
            self.stages_completed.append("load")
            print(f"[Load] Saved to {filepath}")
        except Exception as e:
            self.errors.append(f"Load error: {e}")
        return self
    
    def load_to_console(self, limit: int = 10) -> "Pipeline":
        """Load data to console output."""
        self.loader.to_console(self.data, limit)
        self.stages_completed.append("load")
        return self
    
    def get_data(self) -> List[Dict]:
        """Get current pipeline data."""
        return self.data
    
    def get_report(self) -> str:
        """Generate pipeline execution report."""
        metadata = {
            "pipeline_name": self.name,
            "stages_completed": self.stages_completed,
            "record_count": len(self.data),
            "errors": self.errors
        }
        return self.loader.generate_report(self.data, metadata)
    
    def has_errors(self) -> bool:
        """Check if pipeline has errors."""
        return len(self.errors) > 0
    
    def reset(self) -> "Pipeline":
        """Reset pipeline state."""
        self.data = []
        self.stages_completed = []
        self.errors = []
        self.transformer = DataTransformer()
        return self
