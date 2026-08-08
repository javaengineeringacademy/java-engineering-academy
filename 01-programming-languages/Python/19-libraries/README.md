# Python Libraries

> A detailed guide to essential Python libraries for web development, data science, and system administration.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Web development, data science, system administration | Standard library for simple cases |
| When NOT to use | Don't add dependencies without need; evaluate maintenance status | Use built-ins when possible |
| Alternatives | stdlib for simple cases; lightweight alternatives | requests over urllib, flask over django |
| Production Examples | Web APIs, data pipelines, CLI tools | Simple scripts, prototypes |
| Common Mistakes | Not pinning versions, ignoring security updates | Use `pip-audit`; pin in requirements |

## Libraries

| # | Library | Category | Description |
|---|---------|----------|-------------|
| 01 | [NumPy](01-numpy/) | Data Science | Numerical computing with arrays |
| 02 | [Pandas](02-pandas/) | Data Science | Data manipulation and analysis |
| 03 | [Requests](03-requests/) | HTTP | Elegant HTTP library |
| 04 | [Flask](04-flask/) | Web | Lightweight web framework |
| 05 | [Django](05-django/) | Web | Full-featured web framework |
| 06 | [SQLAlchemy](06-sqlalchemy/) | Database | SQL toolkit and ORM |
| 07 | [pytest](07-pytest/) | Testing | Testing framework |
| 08 | [Click](08-click/) | CLI | Creating command line interfaces |
| 09 | [FastAPI](09-fastapi/) | Web | Modern async web framework |
| 10 | [Redis](10-redis/) | Database | In-memory data store |
| 11 | [Celery](11-celery/) | Async | Distributed task queue |
| 12 | [Pillow](12-pillow/) | Image | Image processing library |
| 13 | [BeautifulSoup](13-beautifulsoup/) | Web Scraping | HTML/XML parsing |
| 14 | [Matplotlib](14-matplotlib/) | Visualization | Plotting library |
| 15 | [Pydantic](15-pydantic/) | Data | Data validation |
| 16 | [Typer](16-typer/) | CLI | Modern CLI builder |
| 17 | [SQLite3](17-sqlite/) | Database | Built-in SQL database |

## Installation

Most libraries can be installed via pip:

```bash
pip install numpy pandas requests flask django sqlalchemy pytest click \
    fastapi redis celery pillow beautifulsoup4 matplotlib pydantic typer
```

SQLite3 is included with Python's standard library.

## Learning Path

1. **Data Science**: NumPy → Pandas → Matplotlib
2. **Web Development**: Flask/Django → SQLAlchemy → Redis
3. **API Development**: FastAPI → Pydantic → Redis
4. **DevOps/CLI**: Click/Typer → Celery → Redis
5. **Web Scraping**: Requests → BeautifulSoup

## Resources

Each library folder contains:
- `README.md` - Overview and concepts
- `examples/` - Practical code examples

## Production Incidents

### Incident 1: Requests Library Timeout Causing Cascading Failure

**Problem:** External API call hung indefinitely; thread pool exhausted
**Cause:** `requests.get()` without timeout; default is infinite
**Impact:** All threads blocked; service unresponsive for 10 minutes
**Detection:** Health check timeouts; thread count spike
**Solution:**
```python
# BAD: No timeout
response = requests.get(url)

# GOOD: Explicit timeout
response = requests.get(url, timeout=(5, 30))  # (connect, read)
```
**Prevention:** Always set timeout; use circuit breakers; implement retry with backoff

### Incident 2: Pandas Memory Explosion

**Problem:** Data processing used 10GB RAM for 1GB CSV file
**Cause:** `pd.read_csv()` loaded entire file; dtypes not optimized
**Impact:** Service OOM; processing failed
**Detection:** Memory monitoring showed spike
**Solution:**
```python
# BAD: Load entire file
df = pd.read_csv("large_file.csv")

# GOOD: Optimize dtypes and use chunks
dtypes = {
    'id': 'int32',  # Instead of int64
    'category': 'category',  # Instead of object
}
chunks = pd.read_csv("large_file.csv", dtype=dtypes, chunksize=10000)
for chunk in chunks:
    process(chunk)
```
**Prevention:** Optimize dtypes; use chunking for large files; profile memory usage

### Incident 3: SQLAlchemy N+1 Query Problem

**Problem:** API response time increased linearly with data size
**Cause:** Accessing relationships triggered additional queries per row
**Impact:** 1000 records caused 1001 queries; 5-second response time
**Detection:** Query logging showed N+1 pattern
**Solution:**
```python
# BAD: N+1 queries
users = session.query(User).all()
for user in users:
    print(user.orders)  # Query per user!

# GOOD: Eager loading
from sqlalchemy.orm import joinedload

users = session.query(User).options(joinedload(User.orders)).all()
for user in users:
    print(user.orders)  # No additional query
```
**Prevention:** Use eager loading (`joinedload`, `subqueryload`); monitor query count; use `selectinload` for collections

## Related Topics

- [05-testing](../05-testing/) - Testing libraries (pytest)
- [16-best-practices](../16-best-practices/) - Library selection best practices
- [18-senior](../18-senior/) - Production library usage patterns

## Interview Questions

### Q1: When would you use NumPy over Python lists?
**Answer:** NumPy for: numerical computation, large arrays, vectorized operations, memory efficiency. Lists for: mixed types, small datasets, simple operations.

### Q2: What is the difference between pandas DataFrame and SQL?
**Answer:** DataFrame is in-memory, flexible, Python-native. SQL is persistent, optimized for queries. Use pandas for analysis, SQL for storage.

### Q3: When would you use Flask over Django?
**Answer:** Flask: microservices, APIs, simple apps, learning. Django: complex apps, admin panels, batteries-included. Flask is flexible, Django is productive.

### Q4: What is the difference between requests and httpx?
**Answer:** requests: synchronous HTTP. httpx: sync and async HTTP, HTTP/2 support. Use httpx for async, requests for simple scripts.

### Q5: What is the difference between SQLAlchemy Core and ORM?
**Answer:** Core: SQL expressions, faster, more control. ORM: object-oriented, easier for complex domains. Use Core for performance, ORM for productivity.
