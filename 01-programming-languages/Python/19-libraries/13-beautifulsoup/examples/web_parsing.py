from bs4 import BeautifulSoup
import requests

# Fetch a webpage
url = 'https://example.com'
response = requests.get(url)
soup = BeautifulSoup(response.text, 'html.parser')

# Find elements by tag
title = soup.find('title')
print(f"Title: {title.text}")

# Find all links
links = soup.find_all('a')
for link in links:
    print(f"Link: {link.get('href')}")

# CSS selectors
paragraphs = soup.select('p.content')
for p in paragraphs:
    print(f"Paragraph: {p.text}")

# Extract all text
text = soup.get_text(separator='\n', strip=True)
