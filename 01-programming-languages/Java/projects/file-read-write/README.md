# File Read/Write Project

## Overview
A comprehensive file processing utility that reads and writes Excel, CSV, and JSON files using Apache POI and other libraries. This project demonstrates real-world file I/O operations.

## Features
- Read/write Excel files (XLS, XLSX) using Apache POI
- Read/write CSV files
- Read/write JSON files using Jackson
- File validation and error handling
- Batch processing
- Progress reporting

## Architecture
- FileProcessor: Main orchestrator
- ExcelReader: Excel file operations using POI
- CsvProcessor: CSV file operations
- JsonProcessor: JSON file operations using Jackson
- FileUtils: Common file utilities

## Learning Objectives
- Apache POI for Excel operations
- CSV parsing with OpenCSV
- JSON processing with Jackson
- File I/O best practices
- Error handling and validation

## Dependencies
```xml
<dependencies>
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.2.5</version>
    </dependency>
    <dependency>
        <groupId>com.opencsv</groupId>
        <artifactId>opencsv</artifactId>
        <version>5.9</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.16.1</version>
    </dependency>
</dependencies>
```

## How to Run
```bash
mvn compile exec:java -Dexec.mainClass="FileProcessor"
```

## Production Notes
- Use streaming for large files
- Add progress callbacks for UI
- Implement retry logic for network files
