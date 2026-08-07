# Data Pipeline

A modular data processing pipeline demonstrating ETL (Extract, Transform, Load) patterns in Python.

## Features

- Modular pipeline architecture
- Pluggable extractors and loaders
- Data validation and cleaning
- Transformation functions
- Pipeline orchestration
- Error handling and logging

## Pipeline Stages

1. **Extract**: Load data from various sources (CSV, JSON, API)
2. **Transform**: Clean, validate, and transform data
3. **Load**: Export processed data to destinations

## Architecture

```
data-pipeline/
├── extract.py    # Data extraction from sources
├── transform.py  # Data transformation logic
├── load.py       # Data loading/exports
├── pipeline.py   # Pipeline orchestration
├── main.py       # CLI interface
└── README.md     # This file
```

## Learning Objectives

- ETL pipeline design patterns
- Data validation and cleaning
- Functional composition
- Error handling in pipelines
- Logging and monitoring

## How to Run

```bash
# Run with sample data
python main.py

# Run with custom input
python main.py --input data.csv --output result.json

# Run specific pipeline
python main.py --pipeline sales --format csv
```
