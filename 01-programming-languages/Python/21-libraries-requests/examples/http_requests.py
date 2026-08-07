"""
HTTP Requests with the Requests Library
Demonstrates GET, POST, sessions, and error handling
"""

import requests
import json

# ============================================
# Basic GET Request
# ============================================

def basic_get():
    print("=== Basic GET Request ===")

    response = requests.get('https://httpbin.org/get')

    print(f"Status: {response.status_code}")
    print(f"Content-Type: {response.headers['content-type']}")
    print(f"Response (first 200 chars): {response.text[:200]}")
    print(f"JSON parsed: {response.json()['url']}")
    print()

# ============================================
# POST Request with JSON
# ============================================

def post_json():
    print("=== POST with JSON ===")

    data = {
        'name': 'Alice',
        'age': 30,
        'skills': ['Python', 'Data Science']
    }

    response = requests.post(
        'https://httpbin.org/post',
        json=data
    )

    result = response.json()
    print(f"Status: {response.status_code}")
    print(f"Sent JSON: {result['json']}")
    print()

# ============================================
# Headers and Parameters
# ============================================

def headers_and_params():
    print("=== Headers and Parameters ===")

    headers = {
        'User-Agent': 'MyApp/1.0',
        'X-Custom-Header': 'custom-value'
    }

    params = {
        'page': 1,
        'limit': 20,
        'sort': 'name'
    }

    response = requests.get(
        'https://httpbin.org/get',
        headers=headers,
        params=params
    )

    result = response.json()
    print(f"URL: {result['url']}")
    print(f"Custom headers: {result['headers'].get('X-Custom-Header', 'N/A')}")
    print()

# ============================================
# Sessions
# ============================================

def session_example():
    print("=== Session Example ===")

    session = requests.Session()

    # Session persists cookies
    session.get('https://httpbin.org/cookies/set/session_id/abc123')
    response = session.get('https://httpbin.org/cookies')

    print(f"Session cookies: {response.json()['cookies']}")
    session.close()
    print()

# ============================================
# Error Handling
# ============================================

def error_handling():
    print("=== Error Handling ===")

    # Successful request
    response = requests.get('https://httpbin.org/status/200')
    if response.ok:
        print(f"200 OK: {response.status_code}")

    # Client error (404)
    response = requests.get('https://httpbin.org/status/404')
    print(f"404 Not Found: {response.status_code}")

    # Server error (500)
    response = requests.get('https://httpbin.org/status/500')
    print(f"500 Server Error: {response.status_code}")

    # Using raise_for_status
    try:
        response = requests.get('https://httpbin.org/status/404', timeout=5)
        response.raise_for_status()
    except requests.HTTPError as e:
        print(f"HTTPError caught: {e}")
    except requests.Timeout:
        print("Timeout caught")
    except requests.ConnectionError:
        print("Connection error caught")
    print()

# ============================================
# Timeouts
# ============================================

def timeout_example():
    print("=== Timeout Example ===")

    # This will timeout (delay endpoint waits 5 seconds)
    try:
        response = requests.get(
            'https://httpbin.org/delay/5',
            timeout=2  # 2 second timeout
        )
    except requests.Timeout:
        print("Request timed out as expected (2s timeout on 5s delay)")
    print()

# ============================================
# Main Execution
# ============================================

if __name__ == "__main__":
    print("NOTE: These examples require internet access.\n")

    try:
        basic_get()
        post_json()
        headers_and_params()
        session_example()
        error_handling()
        timeout_example()
        print("All HTTP request examples completed!")
    except requests.ConnectionError:
        print("ERROR: No internet connection. Please connect and try again.")
    except Exception as e:
        print(f"ERROR: {e}")
