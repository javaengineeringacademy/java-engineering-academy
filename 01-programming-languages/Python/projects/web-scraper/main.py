"""CLI interface for the Web Scraper."""

import argparse
import sys
from scraper import Scraper
from parser import Parser
from storage import Storage


def parse_arguments():
    """Parse command line arguments."""
    parser = argparse.ArgumentParser(
        description="Web Scraper - Extract data from websites"
    )
    
    parser.add_argument(
        "--url", "-u",
        required=True,
        help="URL to scrape"
    )
    
    parser.add_argument(
        "--selector", "-s",
        default="body",
        help="CSS selector for elements (default: body)"
    )
    
    parser.add_argument(
        "--format", "-f",
        choices=["json", "csv"],
        default="json",
        help="Output format (default: json)"
    )
    
    parser.add_argument(
        "--output", "-o",
        help="Output filename"
    )
    
    parser.add_argument(
        "--text-only",
        action="store_true",
        help="Extract text content only"
    )
    
    parser.add_argument(
        "--links-only",
        action="store_true",
        help="Extract links only"
    )
    
    parser.add_argument(
        "--metadata",
        action="store_true",
        help="Extract page metadata"
    )
    
    parser.add_argument(
        "--rate-limit",
        type=float,
        default=1.0,
        help="Seconds between requests (default: 1.0)"
    )
    
    return parser.parse_args()


def main():
    """Main entry point."""
    args = parse_arguments()
    
    print(f"Scraping: {args.url}")
    print(f"Selector: {args.selector}")
    print("-" * 50)
    
    # Initialize components
    scraper = Scraper(rate_limit=args.rate_limit)
    storage = Storage()
    
    # Fetch the page
    html = scraper.fetch(args.url)
    if not html:
        print("Failed to fetch the URL")
        sys.exit(1)
    
    print(f"Page fetched successfully ({len(html)} bytes)")
    
    # Parse the content
    parser = Parser(html)
    
    # Extract data based on options
    if args.metadata:
        data = [parser.extract_metadata()]
        print(f"Extracted metadata")
    elif args.links_only:
        data = parser.extract_links(args.selector)
        print(f"Found {len(data)} links")
    elif args.text_only:
        texts = parser.extract_text(args.selector)
        data = [{"text": t} for t in texts]
        print(f"Extracted {len(data)} text elements")
    else:
        data = parser.find_by_selector(args.selector)
        print(f"Found {len(data)} elements")
    
    if not data:
        print("No data found with given selector")
        sys.exit(1)
    
    # Export data
    if args.output:
        filename = args.output
    else:
        filename = f"scraped_data.{args.format}"
    
    if args.format == "json":
        filepath = storage.to_json(data, filename)
    else:
        filepath = storage.to_csv(data, filename)
    
    print(f"Data exported to: {filepath}")
    print(f"Total requests made: {scraper.get_stats()['total_requests']}")


if __name__ == "__main__":
    main()
