# Practices

## Exercises

Work through these exercises to build hands-on skills with synthetic and artificial monitoring.

---

## Exercise 1: Basic HTTP Monitor

### Objective
Build an HTTP synthetic monitor that checks multiple endpoints and records metrics.

### Requirements
1. Monitor at least 3 different endpoints
2. Record response time and status code
3. Export metrics to Prometheus format
4. Handle timeouts and errors gracefully

### Starter Code

```python
import requests
import time
from prometheus_client import Histogram, Counter, Gauge

# Define metrics
CHECK_DURATION = Histogram(
    'http_check_duration_seconds',
    'Duration of HTTP checks',
    ['endpoint'],
    buckets=[0.1, 0.25, 0.5, 1.0, 2.5, 5.0]
)

CHECK_TOTAL = Counter(
    'http_checks_total',
    'Total HTTP checks',
    ['endpoint', 'status']
)

CHECK_SUCCESS = Gauge(
    'http_check_success',
    'Check success status',
    ['endpoint']
)

# TODO: Implement your monitor here

def check_endpoint(name, url, expected_status=200, timeout=10):
    """Check a single endpoint."""
    # TODO: Implement this function
    pass

def run_monitor(endpoints, interval=60):
    """Run continuous monitoring."""
    # TODO: Implement this function
    pass
```

### Expected Output
```
[SUCCESS] api-health: 200 (150ms)
[SUCCESS] user-list: 200 (230ms)
[FAIL] product-detail: 500 (50ms)
```

### Solution Hints
- Use `requests.get()` with timeout parameter
- Measure time using `time.time()` before and after request
- Handle `requests.exceptions.RequestException` for errors

---

## Exercise 2: Browser Synthetic Monitor

### Objective
Create a Playwright-based browser monitor that validates page content and captures Web Vitals.

### Requirements
1. Navigate to a URL and wait for full load
2. Validate that specific text exists on the page
3. Capture Largest Contentful Paint (LCP)
4. Take a screenshot on failure

### Starter Code

```javascript
const { chromium } = require('playwright');

async function monitorPage(url, expectedText) {
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext();
  const page = await context.newPage();
  
  const result = {
    url,
    success: false,
    loadTime: 0,
    webVitals: {},
    error: null
  };
  
  try {
    // TODO: Navigate to URL and measure load time
    
    // TODO: Validate expected text exists
    
    // TODO: Capture Web Vitals
    
    result.success = true;
    
  } catch (error) {
    result.error = error.message;
    // TODO: Take screenshot on failure
    
  } finally {
    await browser.close();
  }
  
  return result;
}

// TODO: Run monitor for multiple pages
```

### Solution Hints
- Use `page.goto()` with `waitUntil: 'networkidle'`
- Use `page.textContent('body')` to get page text
- Use `page.evaluate()` with PerformanceObserver for Web Vitals
- Use `page.screenshot()` for screenshots

---

## Exercise 3: DNS Monitor

### Objective
Build a DNS monitoring tool that checks resolution and detects issues.

### Requirements
1. Check A, AAAA, MX, and TXT records
2. Compare against expected values
3. Measure resolution time
4. Detect potential DNS hijacking

### Starter Code

```python
import dns.resolver
import time

class DNSMonitor:
    def __init__(self, nameservers=None):
        # TODO: Initialize DNS resolver
        pass
    
    def check_record(self, domain, record_type, expected):
        """Check a single DNS record."""
        # TODO: Implement DNS check
        pass
    
    def check_domain(self, domain, checks):
        """Check all records for a domain."""
        # TODO: Implement full domain check
        pass
    
    def monitor_continuous(self, domains, interval=300):
        """Run continuous monitoring."""
        # TODO: Implement continuous monitoring
        pass

# Example usage
monitor = DNSMonitor(nameservers=['8.8.8.8', '1.1.1.1'])
checks = {
    'A': ['192.0.2.1'],
    'MX': ['mail.example.com'],
    'TXT': ['v=spf1 include:_spf.google.com ~all']
}
```

### Solution Hints
- Use `dns.resolver.resolve(domain, record_type)`
- Measure time with `time.time()` before and after
- Compare sorted lists of results
- Handle `dns.resolver.NoAnswer` and `dns.resolver.NXDOMAIN`

---

## Exercise 4: SSL Certificate Monitor

### Objective
Create an SSL certificate monitor that tracks expiration and validates certificates.

### Requirements
1. Check certificate expiration
2. Validate certificate chain
3. Detect protocol version
4. Alert on issues

### Starter Code

```python
import ssl
import socket
from datetime import datetime

class SSLMonitor:
    def __init__(self, warning_days=30, critical_days=7):
        self.warning_days = warning_days
        self.critical_days = critical_days
    
    def check_certificate(self, hostname, port=443):
        """Check SSL certificate for a hostname."""
        # TODO: Implement SSL check
        pass
    
    def check_multiple(self, hostnames):
        """Check multiple hostnames."""
        # TODO: Implement batch checking
        pass
```

### Solution Hints
- Use `ssl.create_default_context()` for secure connections
- Use `context.wrap_socket()` to get certificate
- Parse `notAfter` from cert dict
- Calculate days remaining with `datetime.strptime`

---

## Exercise 5: Alert Rules

### Objective
Write Prometheus alert rules for synthetic monitoring.

### Requirements
1. Alert when availability drops below 99%
2. Alert when P99 latency exceeds 2 seconds
3. Alert when SSL certificate expires in 30 days
4. Implement multi-window alerting

### Starter Code

```yaml
# alert-rules.yaml
groups:
  - name: synthetic_monitoring
    rules:
      # TODO: Add availability alert
      
      # TODO: Add latency alert
      
      # TODO: Add SSL certificate alert
      
      # TODO: Add multi-window alert
```

### Solution Hints
- Use `rate()` for error rate calculations
- Use `histogram_quantile()` for latency percentiles
- Use `for` parameter for sustained conditions
- Use `and` operator for multi-window alerts

---

## Exercise 6: Chaos Engineering Experiment

### Objective
Design and run a chaos engineering experiment with monitoring.

### Requirements
1. Define a hypothesis
2. Inject a fault (latency, packet loss, or service kill)
3. Monitor system behavior during experiment
4. Verify recovery after experiment

### Starter Code

```python
class ChaosExperiment:
    def __init__(self, name, hypothesis, fault_type, duration):
        self.name = name
        self.hypothesis = hypothesis
        self.fault_type = fault_type
        self.duration = duration
        self.results = {}
    
    def inject_fault(self):
        """Inject the fault."""
        # TODO: Implement fault injection
        pass
    
    def revert_fault(self):
        """Revert the fault."""
        # TODO: Implement fault revert
        pass
    
    def run(self, monitor_fn):
        """Run the experiment."""
        # TODO: Implement experiment runner
        pass
    
    def evaluate(self):
        """Evaluate results against hypothesis."""
        # TODO: Implement evaluation
        pass
```

### Solution Hints
- Use `subprocess.run()` for system commands
- Capture baseline metrics before experiment
- Monitor continuously during experiment
- Compare recovery metrics to baseline

---

## Exercise 7: Dashboard Creation

### Objective
Create a Grafana dashboard for synthetic monitoring.

### Requirements
1. Executive overview panel (availability, SLA)
2. Service health table
3. Response time timeseries
4. SSL certificate status

### Starter Code

```json
{
  "dashboard": {
    "title": "Synthetic Monitoring Dashboard",
    "panels": [
      {
        "title": "TODO: Add panels here",
        "type": "TODO"
      }
    ]
  }
}
```

### Solution Hints
- Use `stat` panel for single values
- Use `timeseries` panel for trends
- Use `table` panel for detailed data
- Use `gauge` panel for thresholds

---

## Exercise 8: Report Generator

### Objective
Build a monitoring report generator.

### Requirements
1. Calculate availability over time window
2. Identify incidents and their duration
3. Generate recommendations
4. Output as JSON

### Starter Code

```python
from datetime import datetime, timedelta
from typing import List, Dict

class ReportGenerator:
    def __init__(self, metrics_data: List[Dict]):
        self.metrics = metrics_data
    
    def calculate_availability(self, window_hours=24):
        """Calculate availability over time window."""
        # TODO: Implement availability calculation
        pass
    
    def identify_incidents(self):
        """Identify incidents from metrics."""
        # TODO: Implement incident detection
        pass
    
    def generate_recommendations(self):
        """Generate recommendations."""
        # TODO: Implement recommendation engine
        pass
    
    def generate_report(self):
        """Generate complete report."""
        # TODO: Implement report generation
        pass
```

### Solution Hints
- Filter metrics by timestamp for time window
- Count consecutive failures as incidents
- Check against thresholds for recommendations
- Structure output as nested dictionary

---

## Evaluation Criteria

For each exercise, evaluate:
1. **Functionality:** Does it work as expected?
2. **Error Handling:** Does it handle edge cases?
3. **Performance:** Is it efficient?
4. **Code Quality:** Is it readable and maintainable?
5. **Documentation:** Are there clear comments?

---

## Next Steps

After completing these exercises:
1. Review [Solutions](../solutions/README.md) for reference implementations
2. Try combining multiple exercises into a complete monitoring system
3. Deploy your monitor and set up alerts
4. Share your implementation with others
