"""HTTP request handling for web scraping."""

import time
import requests
from typing import Optional, Dict


class Scraper:
    """Handles HTTP requests with retry logic and rate limiting."""
    
    DEFAULT_HEADERS = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        "Accept-Language": "en-US,en;q=0.5",
    }
    
    def __init__(self, rate_limit: float = 1.0, max_retries: int = 3):
        """Initialize scraper with rate limiting and retry settings."""
        self.rate_limit = rate_limit
        self.max_retries = max_retries
        self.last_request_time = 0.0
        self.session = requests.Session()
        self.session.headers.update(self.DEFAULT_HEADERS)
        self.request_count = 0
    
    def _wait_for_rate_limit(self) -> None:
        """Wait if necessary to respect rate limiting."""
        elapsed = time.time() - self.last_request_time
        if elapsed < self.rate_limit:
            time.sleep(self.rate_limit - elapsed)
    
    def fetch(self, url: str, params: Optional[Dict] = None) -> Optional[str]:
        """
        Fetch a URL with retry logic.
        
        Returns HTML content as string or None on failure.
        """
        for attempt in range(self.max_retries):
            try:
                self._wait_for_rate_limit()
                
                response = self.session.get(url, params=params, timeout=30)
                self.last_request_time = time.time()
                self.request_count += 1
                
                response.raise_for_status()
                return response.text
                
            except requests.RequestException as e:
                print(f"Attempt {attempt + 1} failed: {e}")
                if attempt < self.max_retries - 1:
                    time.sleep(2 ** attempt)  # Exponential backoff
        
        return None
    
    def fetch_json(self, url: str) -> Optional[dict]:
        """Fetch URL and parse as JSON."""
        try:
            self._wait_for_rate_limit()
            response = self.session.get(url, timeout=30)
            self.last_request_time = time.time()
            self.request_count += 1
            
            response.raise_for_status()
            return response.json()
            
        except requests.RequestException as e:
            print(f"JSON fetch failed: {e}")
            return None
    
    def set_header(self, key: str, value: str) -> None:
        """Set a custom header."""
        self.session.headers[key] = value
    
    def get_stats(self) -> dict:
        """Get scraper statistics."""
        return {
            "total_requests": self.request_count,
            "session_headers": dict(self.session.headers)
        }
