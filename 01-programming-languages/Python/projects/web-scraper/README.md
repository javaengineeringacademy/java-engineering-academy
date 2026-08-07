# Web Scraper

A flexible web scraping tool built with requests and BeautifulSoup for extracting and exporting data from websites.

## Features

- Fetch web pages with custom headers
- Parse HTML content with BeautifulSoup
- Extract data using CSS selectors
- Export to CSV and JSON formats
- Rate limiting and error handling
- Configurable retry logic

## Architecture

```
web-scraper/
├── scraper.py   # HTTP requests and page fetching
├── parser.py    # HTML parsing and data extraction
├── storage.py   # CSV/JSON export functionality
├── main.py      # CLI interface
└── README.md    # This file
```

## Learning Objectives

- HTTP requests with the requests library
- HTML parsing with BeautifulSoup
- CSS selector usage
- File I/O for data export
- CLI argument parsing

## How to Run

```bash
# Install dependencies
pip install requests beautifulsoup4

# Run the scraper
python main.py --url "https://example.com" --selector "h1" --format json

# Run with options
python main.py --url "https://quotes.toscrape.com" --selector ".quote" --output quotes.csv
```
