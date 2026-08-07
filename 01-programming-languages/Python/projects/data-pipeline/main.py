"""CLI interface for the Data Pipeline."""

import argparse
import sys
import os
from pipeline import Pipeline
from transform import DataTransformer


def parse_arguments():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(
        description="Data Pipeline - ETL processing tool"
    )
    
    parser.add_argument(
        "--input", "-i",
        help="Input file path (CSV or JSON)"
    )
    
    parser.add_argument(
        "--output", "-o",
        default="output",
        help="Output directory (default: output)"
    )
    
    parser.add_argument(
        "--format", "-f",
        choices=["csv", "json"],
        default="csv",
        help="Output format (default: csv)"
    )
    
    parser.add_argument(
        "--pipeline", "-p",
        choices=["sales", "generic"],
        default="sales",
        help="Pipeline type (default: sales)"
    )
    
    parser.add_argument(
        "--sample",
        action="store_true",
        help="Use sample data"
    )
    
    parser.add_argument(
        "--report",
        action="store_true",
        help="Generate processing report"
    )
    
    return parser.parse_args()


def run_sales_pipeline(input_file: str = None, output_dir: str = "output", fmt: str = "csv"):
    """Run the sales data pipeline."""
    print("=" * 60)
    print("SALES DATA PIPELINE")
    print("=" * 60)
    
    # Create pipeline
    pipe = Pipeline(name="sales_pipeline")
    
    # Extract stage
    if input_file:
        if input_file.endswith(".csv"):
            pipe.extract_from_csv(input_file)
        elif input_file.endswith(".json"):
            pipe.extract_from_json(input_file)
        else:
            print(f"Unsupported file format: {input_file}")
            return
    else:
        pipe.extract_sample()
    
    # Transform stage
    transformer = DataTransformer()
    pipe.transform(
        DataTransformer.remove_empty_rows,
        lambda data: DataTransformer.convert_types(
            data, {"quantity": int, "price": float}
        ),
        DataTransformer.calculate_total,
        DataTransformer.deduplicate,
        DataTransformer.add_timestamp
    )
    
    # Load stage
    output_file = f"sales_data.{fmt}"
    if fmt == "csv":
        pipe.load_to_csv(output_file)
    else:
        pipe.load_to_json(output_file)
    
    pipe.load_to_console(limit=5)
    
    return pipe


def run_generic_pipeline(input_file: str, output_dir: str = "output", fmt: str = "csv"):
    """Run the generic data pipeline."""
    print("=" * 60)
    print("GENERIC DATA PIPELINE")
    print("=" * 60)
    
    pipe = Pipeline(name="generic_pipeline")
    
    if input_file.endswith(".csv"):
        pipe.extract_from_csv(input_file)
    elif input_file.endswith(".json"):
        pipe.extract_from_json(input_file)
    else:
        print(f"Unsupported file format: {input_file}")
        return
    
    pipe.transform(
        DataTransformer.remove_empty_rows,
        DataTransformer.add_timestamp
    )
    
    output_file = f"processed_data.{fmt}"
    if fmt == "csv":
        pipe.load_to_csv(output_file)
    else:
        pipe.load_to_json(output_file)
    
    return pipe


def main():
    """Main entry point."""
    args = parse_arguments()
    
    try:
        if args.sample or not args.input:
            pipe = run_sales_pipeline(args.input, args.output, args.format)
        else:
            pipe = run_generic_pipeline(args.input, args.output, args.format)
        
        if args.report and pipe:
            print("\n" + "=" * 60)
            print("PIPELINE REPORT")
            print("=" * 60)
            print(pipe.get_report())
        
        print("\nPipeline completed successfully!")
        
    except Exception as e:
        print(f"Pipeline failed: {e}")
        sys.exit(1)


if __name__ == "__main__":
    main()
