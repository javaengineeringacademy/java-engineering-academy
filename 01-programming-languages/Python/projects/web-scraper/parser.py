"""HTML parsing and data extraction."""

from bs4 import BeautifulSoup
from typing import List, Dict, Optional


class Parser:
    """Parses HTML content and extracts data."""
    
    def __init__(self, html: str):
        """Initialize parser with HTML content."""
        self.soup = BeautifulSoup(html, "html.parser")
    
    def find_by_selector(self, selector: str) -> List[Dict[str, str]]:
        """
        Find elements using CSS selector.
        
        Returns list of dictionaries with element data.
        """
        elements = self.soup.select(selector)
        results = []
        
        for elem in elements:
            data = {
                "tag": elem.name,
                "text": elem.get_text(strip=True),
                "html": str(elem),
                "attributes": dict(elem.attrs)
            }
            
            # Extract links if present
            if elem.name == "a" and elem.get("href"):
                data["href"] = elem["href"]
            
            # Extract images if present
            if elem.name == "img":
                data["src"] = elem.get("src", "")
                data["alt"] = elem.get("alt", "")
            
            results.append(data)
        
        return results
    
    def extract_text(self, selector: str) -> List[str]:
        """Extract text content from matching elements."""
        elements = self.soup.select(selector)
        return [elem.get_text(strip=True) for elem in elements]
    
    def extract_links(self, selector: str = "a") -> List[Dict[str, str]]:
        """Extract all links matching selector."""
        links = []
        for elem in self.soup.select(selector):
            if elem.name == "a" and elem.get("href"):
                links.append({
                    "text": elem.get_text(strip=True),
                    "href": elem["href"],
                    "title": elem.get("title", "")
                })
        return links
    
    def extract_table(self, table_selector: str = "table") -> List[List[str]]:
        """Extract table data as list of rows."""
        tables = self.soup.select(table_selector)
        if not tables:
            return []
        
        table = tables[0]
        rows = []
        
        for tr in table.select("tr"):
            cells = [td.get_text(strip=True) for td in tr.select("td, th")]
            if cells:
                rows.append(cells)
        
        return rows
    
    def extract_metadata(self) -> Dict[str, str]:
        """Extract page metadata from meta tags."""
        metadata = {}
        
        # Title
        title_tag = self.soup.find("title")
        if title_tag:
            metadata["title"] = title_tag.get_text(strip=True)
        
        # Meta tags
        for meta in self.soup.select("meta"):
            name = meta.get("name") or meta.get("property", "")
            content = meta.get("content", "")
            if name and content:
                metadata[name] = content
        
        return metadata
    
    def get_element_count(self, selector: str) -> int:
        """Count elements matching selector."""
        return len(self.soup.select(selector))
