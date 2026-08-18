# Artificial & Synthetic Monitoring

> **Package:** `academy.observability.synthetic`

## Table of Contents

1. [Synthetic Monitoring](#synthetic-monitoring)
2. [Artificial Monitoring](#artificial-monitoring)
3. [Tools](#tools)
4. [Implementation](#implementation)
5. [Architecture](#architecture)
6. [Getting Started](#getting-started)

---

## Synthetic Monitoring

### What is Synthetic Monitoring?

Synthetic monitoring is a proactive observability technique where automated scripts and tools simulate user interactions with a system to detect failures, measure performance, and verify availability before real users are affected. Unlike real user monitoring (RUM), synthetic monitoring operates independently of actual user traffic, providing consistent, repeatable measurements.

**Core Principles:**
- **Proactive Detection:** Identify outages and degradation before users notice
- **Baseline Establishment:** Create performance baselines under controlled conditions
- **SLA Verification:** Continuously validate service level agreements
- **Consistent Measurement:** Eliminate variables inherent in real user traffic
- **External Perspective:** Monitor from the user's vantage point, not just internal health

**Synthetic vs. Real User Monitoring:**

| Aspect | Synthetic | Real User |
|--------|-----------|-----------|
| Traffic Source | Simulated | Actual users |
| Coverage | Critical paths only | Full user behavior |
| Detection | Proactive | Reactive |
| Consistency | High | Variable |
| Cost | Fixed | Scales with traffic |
| Environment | Controlled | Production chaos |

### Simulated User Transactions

Simulated user transactions replicate critical user journeys through an application. These scripts execute predefined sequences of actions at regular intervals to verify functionality and measure response times.

**Key Transaction Types:**

1. **Login Flow:** Authentication, session creation, token validation
2. **Search & Browse:** Query execution, result rendering, pagination
3. **Checkout/E2E:** Cart management, payment processing, order confirmation
4. **CRUD Operations:** Create, read, update, delete on critical resources
5. **File Operations:** Upload, download, processing pipelines

**Implementation Pattern:**

```python
# Python synthetic transaction using requests
import requests
import time
from prometheus_client import Histogram, Counter

REQUEST_DURATION = Histogram(
    'synthetic_request_duration_seconds',
    'Duration of synthetic requests',
    ['endpoint', 'method', 'status'],
    buckets=[0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0]
)
REQUEST_TOTAL = Counter(
    'synthetic_requests_total',
    'Total synthetic requests',
    ['endpoint', 'status']
)

def execute_transaction(name, url, method='GET', payload=None, headers=None):
    """Execute a synthetic transaction and record metrics."""
    start = time.time()
    try:
        if method == 'GET':
            response = requests.get(url, headers=headers, timeout=30)
        elif method == 'POST':
            response = requests.post(url, json=payload, headers=headers, timeout=30)
        
        duration = time.time() - start
        status = str(response.status_code)
        
        REQUEST_DURATION.labels(
            endpoint=name, method=method, status=status
        ).observe(duration)
        REQUEST_TOTAL.labels(
            endpoint=name, status=status
        ).inc()
        
        return {
            'success': response.status_code < 400,
            'status': response.status_code,
            'duration': duration,
            'error': None
        }
    except Exception as e:
        duration = time.time() - start
        REQUEST_DURATION.labels(
            endpoint=name, method=method, status='error'
        ).observe(duration)
        REQUEST_TOTAL.labels(
            endpoint=name, status='error'
        ).inc()
        return {
            'success': False,
            'status': 0,
            'duration': duration,
            'error': str(e)
        }

# Define critical user journeys
transactions = [
    {'name': 'homepage', 'url': 'https://app.example.com/', 'method': 'GET'},
    {'name': 'login', 'url': 'https://app.example.com/api/auth/login', 'method': 'POST',
     'payload': {'username': 'synthetic', 'password': 'test'}},
    {'name': 'search', 'url': 'https://app.example.com/api/search?q=test', 'method': 'GET'},
    {'name': 'product_detail', 'url': 'https://app.example.com/api/products/123', 'method': 'GET'},
    {'name': 'add_to_cart', 'url': 'https://app.example.com/api/cart', 'method': 'POST',
     'payload': {'product_id': 123, 'quantity': 1}},
]

for txn in transactions:
    result = execute_transaction(**txn)
    if not result['success']:
        send_alert(f"Synthetic transaction {txn['name']} failed: {result['error']}")
```

### API Monitoring (REST, GraphQL, gRPC)

API monitoring validates that endpoints respond correctly, within expected latency bounds, and with proper data structures.

**REST API Monitoring:**

```yaml
# Synthetic monitor configuration for REST APIs
api_monitors:
  - name: "User API Health"
    endpoint: https://api.example.com/v1/users
    method: GET
    headers:
      Authorization: "Bearer ${SYNTHETIC_TOKEN}"
      Accept: "application/json"
    expected:
      status_code: 200
      response_time_ms: 500
      json_schema:
        type: object
        required: ["data", "meta"]
        properties:
          data:
            type: array
          meta:
            type: object
            required: ["total", "page"]
    assertions:
      - field: "data.length"
        operator: "gte"
        value: 1
      - field: "meta.total"
        operator: "gte"
        value: 0
    schedule: "*/5 * * * *"
    
  - name: "Order Creation"
    endpoint: https://api.example.com/v1/orders
    method: POST
    headers:
      Content-Type: "application/json"
    body:
      product_id: "synthetic-test"
      quantity: 1
    expected:
      status_code: 201
      response_time_ms: 1000
    schedule: "*/10 * * * *"
```

**GraphQL API Monitoring:**

```javascript
// GraphQL synthetic monitor
const graphqlQuery = {
  query: `
    query HealthCheck {
      systemHealth {
        status
        uptime
        version
        dependencies {
          name
          status
          latencyMs
        }
      }
    }
  `,
  variables: {}
};

const checkGraphQLHealth = async () => {
  const start = Date.now();
  const response = await fetch('https://api.example.com/graphql', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${process.env.SYNTHETIC_TOKEN}`
    },
    body: JSON.stringify(graphqlQuery)
  });
  
  const data = await response.json();
  const duration = Date.now() - start;
  
  // Validate response structure
  if (data.errors && data.errors.length > 0) {
    throw new Error(`GraphQL errors: ${JSON.stringify(data.errors)}`);
  }
  
  const health = data.data.systemHealth;
  if (health.status !== 'healthy') {
    throw new Error(`System unhealthy: ${health.status}`);
  }
  
  // Check dependency health
  for (const dep of health.dependencies) {
    if (dep.status !== 'healthy') {
      throw new Error(`Dependency ${dep.name} unhealthy: ${dep.status}`);
    }
    if (dep.latencyMs > 200) {
      console.warn(`Dependency ${dep.name} slow: ${dep.latencyMs}ms`);
    }
  }
  
  return { duration, health };
};
```

**gRPC Monitoring:**

```protobuf
// synthetic_health.proto
syntax = "proto3";
package synthetic;

service HealthService {
  rpc Check(HealthCheckRequest) returns (HealthCheckResponse);
  rpc StreamMetrics(MetricsRequest) returns (stream MetricsResponse);
}

message HealthCheckRequest {
  string service = 1;
}

message HealthCheckResponse {
  string status = 1;
  int64 timestamp = 2;
  map<string, string> details = 3;
}
```

```python
# gRPC synthetic monitoring
import grpc
import time
from synthetic_health_pb2 import HealthCheckRequest
from synthetic_health_pb2_grpc import HealthServiceStub

class GRPCSyntheticMonitor:
    def __init__(self, target):
        self.channel = grpc.insecure_channel(target)
        self.stub = HealthServiceStub(self.channel)
    
    def check_health(self, service='all'):
        start = time.time()
        try:
            request = HealthCheckRequest(service=service)
            response = self.stub.Check(request, timeout=10)
            duration = time.time() - start
            return {
                'success': response.status == 'SERVING',
                'duration': duration,
                'status': response.status,
                'details': dict(response.details)
            }
        except grpc.RpcError as e:
            duration = time.time() - start
            return {
                'success': False,
                'duration': duration,
                'status': 'UNAVAILABLE',
                'error': str(e.code())
            }
```

### Browser Synthetic Monitoring

Browser synthetic monitoring uses real browser instances to render pages, execute JavaScript, and validate the complete user experience including visual elements.

**Playwright-based Browser Monitoring:**

```javascript
const { chromium } = require('playwright');

class BrowserSyntheticMonitor {
  constructor(config) {
    this.config = config;
    this.browser = null;
  }

  async init() {
    this.browser = await chromium.launch({ headless: true });
  }

  async monitorPage(pageConfig) {
    const context = await this.browser.newContext({
      viewport: { width: 1920, height: 1080 },
      userAgent: 'SyntheticMonitor/1.0'
    });
    const page = await context.newPage();
    
    const metrics = {
      url: pageConfig.url,
      timestamps: {},
      errors: [],
      resources: []
    };

    // Capture performance metrics
    page.on('requestfinished', request => {
      metrics.resources.push({
        url: request.url(),
        type: request.resourceType(),
        duration: request.response()?.headers()['server-timing'] || 'unknown'
      });
    });

    page.on('pageerror', error => {
      metrics.errors.push(error.message);
    });

    try {
      // Navigate and measure
      const navStart = Date.now();
      await page.goto(pageConfig.url, { 
        waitUntil: pageConfig.waitUntil || 'networkidle',
        timeout: pageConfig.timeout || 30000 
      });
      metrics.timestamps.load = Date.now() - navStart;

      // Wait for specific elements
      if (pageConfig.waitForSelector) {
        const selStart = Date.now();
        await page.waitForSelector(pageConfig.waitForSelector, { timeout: 10000 });
        metrics.timestamps.elementVisible = Date.now() - selStart;
      }

      // Validate content
      if (pageConfig.expectedText) {
        const content = await page.textContent('body');
        metrics.contentValid = content.includes(pageConfig.expectedText);
      }

      // Screenshot for visual validation
      if (pageConfig.screenshot) {
        await page.screenshot({ 
          path: `/tmp/synthetic-${Date.now()}.png`,
          fullPage: pageConfig.fullPage || false 
        });
      }

      // Collect Core Web Vitals
      const vitals = await page.evaluate(() => {
        return new Promise(resolve => {
          new PerformanceObserver(list => {
            const entries = list.getEntries();
            resolve({
              LCP: entries[entries.length - 1].startTime,
              FID: entries[0].processingStart - entries[0].startTime,
              CLS: entries.reduce((sum, e) => sum + e.value, 0)
            });
          }).observe({ type: 'largest-contentful-paint', buffered: true });
          
          setTimeout(() => resolve({ LCP: 0, FID: 0, CLS: 0 }), 3000);
        });
      });
      metrics.webVitals = vitals;

      metrics.success = metrics.errors.length === 0;
    } catch (error) {
      metrics.success = false;
      metrics.error = error.message;
    } finally {
      await context.close();
    }

    return metrics;
  }

  async close() {
    if (this.browser) await this.browser.close();
  }
}

// Usage
const monitor = new BrowserSyntheticMonitor();
await monitor.init();

const results = await monitor.monitorPage({
  url: 'https://app.example.com/dashboard',
  waitUntil: 'networkidle',
  waitForSelector: '[data-testid="dashboard-loaded"]',
  expectedText: 'Welcome',
  screenshot: true
});
```

### Ping/ICMP Monitoring

Ping monitoring verifies basic network reachability and measures round-trip time (RTT) to target hosts.

```python
import subprocess
import time
import re
from dataclasses import dataclass
from typing import Optional

@dataclass
class PingResult:
    host: str
    success: bool
    rtt_ms: float
    packet_loss: float
    min_rtt: Optional[float]
    max_rtt: Optional[float]
    std_dev: Optional[float]
    timestamp: float

class PingMonitor:
    def __init__(self, count=10, timeout=5, interval=60):
        self.count = count
        self.timeout = timeout
        self.interval = interval
    
    def ping(self, host: str) -> PingResult:
        """Execute ping and parse results."""
        start = time.time()
        try:
            result = subprocess.run(
                ['ping', '-c', str(self.count), '-W', str(self.timeout), host],
                capture_output=True, text=True, timeout=self.count * self.timeout + 5
            )
            
            output = result.stdout
            
            # Parse packet loss
            loss_match = re.search(r'(\d+)% packet loss', output)
            packet_loss = float(loss_match.group(1)) / 100 if loss_match else 1.0
            
            # Parse RTT stats
            rtt_match = re.search(
                r'rtt min/avg/max/mdev = ([\d.]+)/([\d.]+)/([\d.]+)/([\d.]+)',
                output
            )
            
            if rtt_match and packet_loss < 1.0:
                return PingResult(
                    host=host,
                    success=True,
                    rtt_ms=float(rtt_match.group(2)),
                    packet_loss=packet_loss,
                    min_rtt=float(rtt_match.group(1)),
                    max_rtt=float(rtt_match.group(3)),
                    std_dev=float(rtt_match.group(4)),
                    timestamp=start
                )
            else:
                return PingResult(
                    host=host, success=False, rtt_ms=0,
                    packet_loss=1.0, min_rtt=None, max_rtt=None,
                    std_dev=None, timestamp=start
                )
        except Exception as e:
            return PingResult(
                host=host, success=False, rtt_ms=0,
                packet_loss=1.0, min_rtt=None, max_rtt=None,
                std_dev=None, timestamp=start
            )
    
    def continuous_monitor(self, hosts: list):
        """Run continuous monitoring loop."""
        import time
        while True:
            for host in hosts:
                result = self.ping(host)
                self.record_metrics(result)
                if result.packet_loss > 0.1:
                    self.send_alert(f"High packet loss to {host}: {result.packet_loss*100}%")
                if result.rtt_ms > 100:
                    self.send_alert(f"High latency to {host}: {result.rtt_ms}ms")
            time.sleep(self.interval)
```

### DNS Monitoring

DNS monitoring validates that domain names resolve correctly, measure resolution time, and detect DNS hijacking or misconfigurations.

```python
import dns.resolver
import dns.rdatatype
import time
from dataclasses import dataclass
from typing import List, Optional

@dataclass
class DNSResult:
    domain: str
    record_type: str
    resolved: List[str]
    expected: List[str]
    match: bool
    resolution_time_ms: float
    ttl: Optional[int]
    nameserver: str
    timestamp: float

class DNSMonitor:
    def __init__(self, nameservers=None):
        self.nameservers = nameservers or ['8.8.8.8', '1.1.1.1']
    
    def check(self, domain: str, record_type: str, expected: List[str]) -> DNSResult:
        """Perform DNS resolution check."""
        resolver = dns.resolver.Resolver()
        resolver.nameservers = self.nameservers
        resolver.timeout = 5
        resolver.lifetime = 10
        
        start = time.time()
        try:
            answers = resolver.resolve(domain, record_type)
            resolution_time = (time.time() - start) * 1000
            
            resolved = [str(rdata) for rdata in answers]
            ttl = answers.rrset.ttl
            
            return DNSResult(
                domain=domain,
                record_type=record_type,
                resolved=sorted(resolved),
                expected=sorted(expected),
                match=sorted(resolved) == sorted(expected),
                resolution_time_ms=resolution_time,
                ttl=ttl,
                nameserver=','.join(self.nameservers),
                timestamp=start
            )
        except Exception as e:
            resolution_time = (time.time() - start) * 1000
            return DNSResult(
                domain=domain,
                record_type=record_type,
                resolved=[],
                expected=expected,
                match=False,
                resolution_time_ms=resolution_time,
                ttl=None,
                nameserver=','.join(self.nameservers),
                timestamp=start
            )
    
    def check_all_records(self, domain: str) -> dict:
        """Check all record types for a domain."""
        checks = {
            'A': self.check(domain, 'A', ['expected_ip']),
            'AAAA': self.check(domain, 'AAAA', ['expected_ipv6']),
            'MX': self.check(domain, 'MX', ['mail.example.com']),
            'TXT': self.check(domain, 'TXT', ['v=spf1 include:_spf.google.com ~all']),
            'CNAME': self.check(domain, 'CNAME', ['canonical.example.com']),
            'NS': self.check(domain, 'NS', ['ns1.example.com', 'ns2.example.com']),
        }
        return checks
```

### SSL Certificate Monitoring

SSL certificate monitoring tracks certificate expiration, chain validity, protocol support, and cipher suite strength.

```python
import ssl
import socket
import datetime
from dataclasses import dataclass
from typing import List, Optional

@dataclass
class SSLResult:
    hostname: str
    port: int
    valid: bool
    issuer: str
    subject: str
    not_before: datetime.datetime
    not_after: datetime.datetime
    days_until_expiry: int
    serial_number: str
    san_list: List[str]
    protocol_version: str
    cipher_suite: str
    chain_valid: bool
    warnings: List[str]

class SSLMonitor:
    def __init__(self, warning_days=30, critical_days=7):
        self.warning_days = warning_days
        self.critical_days = critical_days
    
    def check(self, hostname: str, port: int = 443) -> SSLResult:
        """Perform SSL certificate check."""
        context = ssl.create_default_context()
        warnings = []
        
        try:
            with socket.create_connection((hostname, port), timeout=10) as sock:
                with context.wrap_socket(sock, server_hostname=hostname) as ssock:
                    cert = ssock.getpeercert()
                    protocol = ssock.version()
                    cipher = ssock.cipher()[0]
                    
                    # Parse certificate details
                    not_after = datetime.datetime.strptime(
                        cert['notAfter'], '%b %d %H:%M:%S %Y %Z'
                    )
                    not_before = datetime.datetime.strptime(
                        cert['notBefore'], '%b %d %H:%M:%S %Y %Z'
                    )
                    days_until_expiry = (not_after - datetime.datetime.utcnow()).days
                    
                    # Extract SAN list
                    san_list = []
                    for entry in cert.get('subjectAltName', ()):
                        san_list.append(entry[1])
                    
                    # Extract issuer and subject
                    issuer = dict(x[0] for x in cert['issuer']).get('organizationName', 'Unknown')
                    subject = dict(x[0] for x in cert['subject']).get('commonName', 'Unknown')
                    
                    # Validate chain
                    chain_valid = True
                    try:
                        # Additional chain validation can be added here
                        pass
                    except Exception:
                        chain_valid = False
                        warnings.append("Certificate chain validation failed")
                    
                    # Check expiration warnings
                    if days_until_expiry <= self.critical_days:
                        warnings.append(f"CRITICAL: Certificate expires in {days_until_expiry} days")
                    elif days_until_expiry <= self.warning_days:
                        warnings.append(f"WARNING: Certificate expires in {days_until_expiry} days")
                    
                    # Check protocol version
                    if protocol in ('TLSv1', 'TLSv1.1'):
                        warnings.append(f"Deprecated protocol: {protocol}")
                    
                    return SSLResult(
                        hostname=hostname,
                        port=port,
                        valid=True,
                        issuer=issuer,
                        subject=subject,
                        not_before=not_before,
                        not_after=not_after,
                        days_until_expiry=days_until_expiry,
                        serial_number=cert.get('serialNumber', ''),
                        san_list=san_list,
                        protocol_version=protocol,
                        cipher_suite=cipher,
                        chain_valid=chain_valid,
                        warnings=warnings
                    )
        except Exception as e:
            return SSLResult(
                hostname=hostname, port=port, valid=False,
                issuer='', subject='', not_before=None,
                not_after=None, days_until_expiry=0,
                serial_number='', san_list=[], protocol_version='',
                cipher_suite='', chain_valid=False,
                warnings=[f"Connection failed: {str(e)}"]
            )
```

### SLA/SLO Verification

Synthetic monitoring is the primary mechanism for verifying SLA/SLO compliance. By continuously probing critical paths, organizations can detect violations before they impact users.

**SLA Verification Framework:**

```python
from dataclasses import dataclass
from typing import Callable, List
import time

@dataclass
class SLACheck:
    name: str
    metric: str
    target: float  # e.g., 0.999 for 99.9% availability
    window_minutes: int
    check_fn: Callable

class SLAVerifier:
    def __init__(self, checks: List[SLACheck]):
        self.checks = checks
        self.results = {c.name: [] for c in checks}
    
    def record(self, check_name: str, success: bool, duration: float):
        """Record a check result."""
        self.results[check_name].append({
            'success': success,
            'duration': duration,
            'timestamp': time.time()
        })
    
    def verify(self, check_name: str) -> dict:
        """Verify SLA compliance for a specific check."""
        check = next(c for c in self.checks if c.name == check_name)
        now = time.time()
        window_start = now - (check.window_minutes * 60)
        
        relevant = [
            r for r in self.results[check_name]
            if r['timestamp'] >= window_start
        ]
        
        if not relevant:
            return {'compliant': None, 'actual': 0, 'target': check.target}
        
        success_count = sum(1 for r in relevant if r['success'])
        actual = success_count / len(relevant)
        
        return {
            'compliant': actual >= check.target,
            'actual': actual,
            'target': check.target,
            'total_checks': len(relevant),
            'successful': success_count,
            'failed': len(relevant) - success_count,
            'avg_duration_ms': sum(r['duration'] for r in relevant) / len(relevant) * 1000,
            'error_budget_remaining': max(0, (check.target - actual) / (1 - check.target)) if check.target < 1 else 1.0
        }
    
    def generate_report(self) -> dict:
        """Generate comprehensive SLA report."""
        report = {}
        for check in self.checks:
            report[check.name] = self.verify(check.name)
        return report
```

---

## Artificial Monitoring

### What is Artificial Monitoring?

Artificial monitoring generates synthetic traffic patterns to test system behavior under controlled conditions. Unlike synthetic monitoring (which simulates individual user actions), artificial monitoring creates artificial load to stress-test capacity, validate auto-scaling, and measure system limits.

**Key Differences from Synthetic Monitoring:**

| Aspect | Synthetic Monitoring | Artificial Monitoring |
|--------|---------------------|----------------------|
| Purpose | Verify availability | Test capacity |
| Traffic Pattern | Mimics users | Stresses systems |
| Volume | Low, consistent | Variable, high |
| Duration | Continuous | Periodic experiments |
| Risk | Minimal | Can impact production |
| Complexity | Simple scripts | Load generation tools |

### Synthetic Traffic Generation

Synthetic traffic generation creates realistic but artificial request patterns to fill gaps in monitoring coverage or test specific scenarios.

```python
import asyncio
import aiohttp
import random
import time
from dataclasses import dataclass, field
from typing import List, Dict
import numpy as np

@dataclass
class TrafficPattern:
    name: str
    rps: float  # requests per second
    duration_seconds: int
    endpoints: List[Dict]
    ramp_up_seconds: int = 0
    headers: Dict = field(default_factory=dict)

class SyntheticTrafficGenerator:
    def __init__(self, base_url: str):
        self.base_url = base_url
        self.results = []
    
    async def generate(self, pattern: TrafficPattern, session: aiohttp.ClientSession):
        """Generate synthetic traffic according to pattern."""
        start_time = time.time()
        interval = 1.0 / pattern.rps
        
        # Ramp up phase
        if pattern.ramp_up_seconds > 0:
            ramp_end = start_time + pattern.ramp_up_seconds
            current_rps = 0
        else:
            ramp_end = start_time
            current_rps = pattern.rps
        
        tasks = []
        while time.time() - start_time < pattern.duration_seconds:
            elapsed = time.time() - start_time
            
            # Handle ramp up
            if elapsed < pattern.ramp_up_seconds:
                progress = elapsed / pattern.ramp_up_seconds
                current_rps = pattern.rps * progress
                actual_interval = 1.0 / max(current_rps, 0.1)
            else:
                actual_interval = interval
            
            # Select endpoint based on weights
            endpoint = random.choices(
                pattern.endpoints,
                weights=[e.get('weight', 1) for e in pattern.endpoints]
            )[0]
            
            task = asyncio.create_task(
                self._make_request(session, endpoint, pattern.headers)
            )
            tasks.append(task)
            
            await asyncio.sleep(actual_interval)
        
        # Wait for all requests to complete
        await asyncio.gather(*tasks, return_exceptions=True)
        return self.results
    
    async def _make_request(self, session, endpoint, default_headers):
        """Execute a single request and record metrics."""
        url = f"{self.base_url}{endpoint['path']}"
        method = endpoint.get('method', 'GET')
        headers = {**default_headers, **endpoint.get('headers', {})}
        payload = endpoint.get('payload')
        
        start = time.time()
        try:
            if method == 'GET':
                async with session.get(url, headers=headers) as resp:
                    await resp.text()
                    status = resp.status
            elif method == 'POST':
                async with session.post(url, headers=headers, json=payload) as resp:
                    await resp.text()
                    status = resp.status
            
            duration = time.time() - start
            self.results.append({
                'url': url,
                'status': status,
                'duration': duration,
                'success': status < 400,
                'timestamp': start
            })
        except Exception as e:
            duration = time.time() - start
            self.results.append({
                'url': url,
                'status': 0,
                'duration': duration,
                'success': False,
                'error': str(e),
                'timestamp': start
            })
```

### Load Testing as Monitoring

Load testing can be integrated into continuous monitoring to detect performance regressions before they reach production.

```yaml
# k6 load test as monitoring
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend } from 'k6/metrics';

const errorRate = new Rate('errors');
const latencyP99 = new Trend('latency_p99');

export const options = {
  stages: [
    { duration: '2m', target: 100 },   // Ramp up
    { duration: '5m', target: 100 },   // Sustained load
    { duration: '2m', target: 200 },   // Peak load
    { duration: '5m', target: 200 },   // Sustained peak
    { duration: '2m', target: 0 },     // Ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    errors: ['rate<0.01'],
  },
};

export default function () {
  const res = http.get('https://api.example.com/v1/products');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
    'has correct content-type': (r) => r.headers['Content-Type'] === 'application/json',
  });
  
  errorRate.add(res.status >= 400);
  latencyP99.add(res.timings.duration);
  
  sleep(1);
}
```

### Chaos Engineering Integration

Artificial monitoring integrates with chaos engineering to validate that systems degrade gracefully under failure conditions.

```python
import time
import random
from enum import Enum

class ChaosExperiment:
    class FaultType(Enum):
        LATENCY_INJECTION = "latency_injection"
        PACKET_LOSS = "packet_loss"
        CPU_STRESS = "cpu_stress"
        MEMORY_STRESS = "memory_stress"
        NETWORK_PARTITION = "network_partition"
        SERVICE_KILL = "service_kill"
    
    def __init__(self, name, fault_type, target, parameters):
        self.name = name
        self.fault_type = fault_type
        self.target = target
        self.parameters = parameters
        self.start_time = None
        self.end_time = None
    
    def inject(self):
        """Inject the fault into the target system."""
        self.start_time = time.time()
        
        if self.fault_type == self.FaultType.LATENCY_INJECTION:
            self._inject_latency()
        elif self.fault_type == self.FaultType.CPU_STRESS:
            self._stress_cpu()
        elif self.fault_type == self.FaultType.PACKET_LOSS:
            self._simulate_packet_loss()
    
    def _inject_latency(self):
        """Add latency to target service."""
        latency_ms = self.parameters.get('latency_ms', 500)
        jitter_ms = self.parameters.get('jitter_ms', 100)
        
        # Implementation depends on infrastructure
        # Example: Using tc (traffic control) on Linux
        import subprocess
        subprocess.run([
            'tc', 'qdisc', 'add', 'dev', self.target,
            'root', 'netem', 'delay',
            f'{latency_ms}ms', f'{jitter_ms}ms'
        ])
    
    def _stress_cpu(self):
        """Generate CPU load on target."""
        duration = self.parameters.get('duration_seconds', 60)
        load_percent = self.parameters.get('load_percent', 80)
        
        # Use stress-ng or similar tool
        import subprocess
        subprocess.Popen([
            'stress-ng', '--cpu', '4',
            '--cpu-load', str(load_percent),
            '--timeout', f'{duration}s'
        ])
    
    def revert(self):
        """Revert the fault injection."""
        self.end_time = time.time()
        
        if self.fault_type == self.FaultType.LATENCY_INJECTION:
            import subprocess
            subprocess.run([
                'tc', 'qdisc', 'del', 'dev', self.target, 'root'
            ])
    
    @property
    def duration_seconds(self):
        if self.end_time:
            return self.end_time - self.start_time
        return time.time() - self.start_time

class ChaosMonitor:
    def __init__(self):
        self.experiments = []
        self.baseline_metrics = {}
    
    def capture_baseline(self, duration_seconds=300):
        """Capture baseline metrics before chaos experiment."""
        print(f"Capturing baseline for {duration_seconds} seconds...")
        # Collect metrics during baseline period
        time.sleep(duration_seconds)
        # Store baseline metrics
        self.baseline_metrics = {
            'error_rate': 0.001,
            'p99_latency_ms': 150,
            'throughput_rps': 1000,
            'cpu_usage_percent': 45,
            'memory_usage_percent': 60
        }
    
    def run_experiment(self, experiment: ChaosExperiment, monitoring_fn):
        """Run chaos experiment while monitoring system health."""
        print(f"Starting chaos experiment: {experiment.name}")
        
        # Pre-experiment validation
        pre_metrics = monitoring_fn()
        
        # Inject fault
        experiment.inject()
        
        # Monitor during experiment
        during_metrics = []
        while experiment.duration_seconds < experiment.parameters.get('max_duration', 300):
            metrics = monitoring_fn()
            during_metrics.append(metrics)
            time.sleep(10)
        
        # Revert fault
        experiment.revert()
        
        # Post-experiment recovery check
        time.sleep(60)  # Allow recovery
        post_metrics = monitoring_fn()
        
        return {
            'experiment': experiment.name,
            'pre': pre_metrics,
            'during': during_metrics,
            'post': post_metrics,
            'recovered': self._check_recovery(pre_metrics, post_metrics)
        }
    
    def _check_recovery(self, pre, post, tolerance=0.1):
        """Check if system recovered to baseline levels."""
        for key in pre:
            if abs(pre[key] - post[key]) / pre[key] > tolerance:
                return False
        return True
```

### Canary Deployments Monitoring

Canary deployments require monitoring to automatically detect issues and roll back if the canary shows degraded metrics compared to the baseline.

```python
from dataclasses import dataclass
from typing import Dict, List
import time

@dataclass
class CanaryConfig:
    name: str
    canary_percentage: float  # 0.0 - 1.0
    metric_thresholds: Dict[str, float]
    evaluation_window_seconds: int
    min_sample_size: int
    auto_rollback: bool

class CanaryMonitor:
    def __init__(self, config: CanaryConfig):
        self.config = config
        self.baseline_metrics = {}
        self.canary_metrics = {}
    
    def collect_metrics(self, duration_seconds: int):
        """Collect metrics from baseline and canary populations."""
        start = time.time()
        while time.time() - start < duration_seconds:
            # In production, these would query your metrics system
            self.baseline_metrics = self._query_metrics('baseline')
            self.canary_metrics = self._query_metrics('canary')
            time.sleep(10)
    
    def evaluate(self) -> dict:
        """Evaluate canary against baseline."""
        results = {
            'metrics': {},
            'overall': 'healthy',
            'violations': []
        }
        
        for metric, threshold in self.config.metric_thresholds.items():
            baseline_val = self.baseline_metrics.get(metric, 0)
            canary_val = self.canary_metrics.get(metric, 0)
            
            if baseline_val > 0:
                deviation = (canary_val - baseline_val) / baseline_val
            else:
                deviation = canary_val
            
            is_violation = abs(deviation) > threshold
            
            results['metrics'][metric] = {
                'baseline': baseline_val,
                'canary': canary_val,
                'deviation': deviation,
                'threshold': threshold,
                'violation': is_violation
            }
            
            if is_violation:
                results['violations'].append({
                    'metric': metric,
                    'deviation': deviation,
                    'threshold': threshold
                })
                results['overall'] = 'unhealthy'
        
        return results
    
    def auto_rollback_if_needed(self, evaluation: dict):
        """Automatically rollback canary if unhealthy."""
        if evaluation['overall'] == 'unhealthy' and self.config.auto_rollback:
            print(f"CRITICAL: Canary {self.config.name} showing degradation")
            print(f"Violations: {evaluation['violations']}")
            self._execute_rollback()
            return True
        return False
    
    def _execute_rollback(self):
        """Execute canary rollback."""
        # Implementation depends on deployment system
        print("Executing automatic rollback...")
    
    def _query_metrics(self, population: str) -> Dict[str, float]:
        """Query metrics for a population."""
        # Placeholder - would query Prometheus/Datadog/etc.
        return {}
```

### A/B Testing Monitoring

A/B testing monitoring ensures that experiment variants are performing correctly and detects statistical significance in metrics.

```python
import math
from dataclasses import dataclass, field
from typing import Dict, List
import time

@dataclass
class ABTestConfig:
    name: str
    variants: Dict[str, float]  # variant_name -> traffic_percentage
    primary_metric: str
    secondary_metrics: List[str]
    significance_level: float = 0.05
    min_sample_size: int = 1000

class ABTestMonitor:
    def __init__(self, config: ABTestConfig):
        self.config = config
        self.variant_data = {v: {'samples': [], 'conversions': 0} 
                           for v in config.variants}
    
    def record(self, variant: str, metric_value: float, converted: bool = False):
        """Record a metric value for a variant."""
        if variant in self.variant_data:
            self.variant_data[variant]['samples'].append(metric_value)
            if converted:
                self.variant_data[variant]['conversions'] += 1
    
    def calculate_statistics(self, variant_a: str, variant_b: str) -> dict:
        """Calculate statistical comparison between two variants."""
        a_samples = self.variant_data[variant_a]['samples']
        b_samples = self.variant_data[variant_b]['samples']
        
        if len(a_samples) < self.config.min_sample_size or \
           len(b_samples) < self.config.min_sample_size:
            return {'insufficient_data': True, 'required': self.config.min_sample_size}
        
        a_mean = sum(a_samples) / len(a_samples)
        b_mean = sum(b_samples) / len(b_samples)
        
        a_var = sum((x - a_mean) ** 2 for x in a_samples) / (len(a_samples) - 1)
        b_var = sum((x - b_mean) ** 2 for x in b_samples) / (len(b_samples) - 1)
        
        # Two-sample t-test
        se = math.sqrt(a_var / len(a_samples) + b_var / len(b_samples))
        t_stat = (a_mean - b_mean) / se if se > 0 else 0
        
        # Approximate p-value (simplified)
        df = len(a_samples) + len(b_samples) - 2
        p_value = 2 * (1 - self._t_cdf(abs(t_stat), df))
        
        # Confidence interval
        ci_margin = 1.96 * se  # 95% CI
        
        return {
            'variant_a': {
                'name': variant_a,
                'mean': a_mean,
                'std': math.sqrt(a_var),
                'n': len(a_samples)
            },
            'variant_b': {
                'name': variant_b,
                'mean': b_mean,
                'std': math.sqrt(b_var),
                'n': len(b_samples)
            },
            'difference': b_mean - a_mean,
            'relative_difference': (b_mean - a_mean) / a_mean if a_mean > 0 else 0,
            't_statistic': t_stat,
            'p_value': p_value,
            'significant': p_value < self.config.significance_level,
            'confidence_interval': {
                'lower': (b_mean - a_mean) - ci_margin,
                'upper': (b_mean - a_mean) + ci_margin
            }
        }
    
    def _t_cdf(self, x, df):
        """Approximate t-distribution CDF."""
        # Simplified approximation
        return 0.5 * (1 + math.erf(x / math.sqrt(2)))
```

---

## Tools

### Pingdom

Pingdom is a synthetic monitoring service that provides uptime monitoring, page speed analysis, and real user monitoring.

**Configuration:**
```yaml
# pingdom-config.yaml
checks:
  - name: "API Health Check"
    type: http
    url: https://api.example.com/health
    interval: 60  # seconds
    regions:
      - us-east
      - eu-west
      - ap-south
    alerts:
      - email: ops@example.com
      - sms: "+1234567890"
      - webhook: https://hooks.example.com/alerts
    thresholds:
      response_time: 2000  # ms
      http_status: 200
      
  - name: "DNS Resolution"
    type: dns
    domain: example.com
    expected_ips:
      - 192.0.2.1
      - 192.0.2.2
    interval: 300
    
  - name: "SSL Certificate"
    type: ssl
    domain: example.com
    port: 443
    warning_days: 30
    critical_days: 7
    interval: 86400  # daily
```

### UptimeRobot

UptimeRobot provides free and paid monitoring with multiple check types.

**API Integration:**
```python
import requests

class UptimeRobotMonitor:
    BASE_URL = "https://api.uptimerobot.com/v2"
    
    def __init__(self, api_key):
        self.api_key = api_key
        self.headers = {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Cache-Control': 'no-cache'
        }
    
    def create_monitor(self, name, url, monitor_type=1, interval=300):
        """Create a new uptime monitor."""
        data = {
            'api_key': self.api_key,
            'format': 'json',
            'friendly_name': name,
            'url': url,
            'type': monitor_type,
            'interval': interval
        }
        response = requests.post(
            f"{self.BASE_URL}/newMonitor",
            headers=self.headers,
            data=data
        )
        return response.json()
    
    def get_monitor_status(self, monitor_id):
        """Get current status of a monitor."""
        data = {
            'api_key': self.api_key,
            'format': 'json',
            'monitors': monitor_id
        }
        response = requests.post(
            f"{self.BASE_URL}/getMonitors",
            headers=self.headers,
            data=data
        )
        return response.json()
```

### Grafana k6

k6 is an open-source load testing tool with built-in metrics export to Grafana.

```javascript
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const customErrors = new Counter('custom_errors');
const successRate = new Rate('success_rate');
const responseTime = new Trend('response_time');

export const options = {
  scenarios: {
    constant_load: {
      executor: 'constant-vus',
      vus: 50,
      duration: '10m',
    },
    ramping_load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '2m', target: 100 },
        { duration: '5m', target: 100 },
        { duration: '2m', target: 0 },
      ],
    },
    stress_test: {
      executor: 'ramping-arrival-rate',
      startRate: 100,
      timeUnit: '1s',
      preAllocatedVUs: 50,
      stages: [
        { duration: '2m', target: 100 },
        { duration: '2m', target: 500 },
        { duration: '2m', target: 1000 },
        { duration: '2m', target: 500 },
        { duration: '2m', target: 100 },
      ],
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    http_req_failed: ['rate<0.01'],
    success_rate: ['rate>0.99'],
  },
};

export default function () {
  const res = http.get('https://api.example.com/v1/products');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time OK': (r) => r.timings.duration < 500,
  });
  
  successRate.add(res.status < 400);
  responseTime.add(res.timings.duration);
  
  if (res.status >= 400) {
    customErrors.add(1);
  }
  
  sleep(1);
}

export function handleSummary(data) {
  return {
    '/tmp/summary.json': JSON.stringify(data, null, 2),
    stdout: textSummary(data, { indent: ' ', enableColors: true }),
  };
}
```

### Selenium/Playwright for Browser Monitoring

**Playwright Browser Monitor:**

```javascript
const { chromium, firefox, webkit } = require('playwright');

class MultiBrowserMonitor {
  constructor() {
    this.browsers = { chromium, firefox, webkit };
    this.results = {};
  }

  async monitorAcrossBrowsers(url, config) {
    const results = {};
    
    for (const [name, BrowserType] of Object.entries(this.browsers)) {
      const browser = await BrowserType.launch({ headless: true });
      const context = await browser.newContext({
        viewport: { width: 1920, height: 1080 }
      });
      const page = await context.newPage();
      
      const metrics = {
        browser: name,
        loadTime: 0,
        firstContentfulPaint: 0,
        largestContentfulPaint: 0,
        cumulativeLayoutShift: 0,
        errors: [],
        screenshots: []
      };

      try {
        const start = Date.now();
        
        // Capture FCP
        await page.goto(url, { waitUntil: 'commit' });
        const fcp = await page.evaluate(() => {
          return new Promise(resolve => {
            new PerformanceObserver(list => {
              const entries = list.getEntries();
              resolve(entries[entries.length - 1].startTime);
            }).observe({ type: 'paint', buffered: true });
            setTimeout(() => resolve(0), 5000);
          });
        });

        // Wait for full load
        await page.waitForLoadState('networkidle');
        metrics.loadTime = Date.now() - start;
        metrics.firstContentfulPaint = fcp;

        // Collect Core Web Vitals
        const vitals = await page.evaluate(() => {
          return new Promise(resolve => {
            const results = {};
            
            // LCP
            new PerformanceObserver(list => {
              const entries = list.getEntries();
              results.LCP = entries[entries.length - 1].startTime;
            }).observe({ type: 'largest-contentful-paint', buffered: true });
            
            // CLS
            new PerformanceObserver(list => {
              let cls = 0;
              for (const entry of list.getEntries()) {
                if (!entry.hadRecentInput) cls += entry.value;
              }
              results.CLS = cls;
            }).observe({ type: 'layout-shift', buffered: true });
            
            setTimeout(() => resolve(results), 3000);
          });
        });
        
        metrics.largestContentfulPaint = vitals.LCP || 0;
        metrics.cumulativeLayoutShift = vitals.CLS || 0;

        // Screenshot
        await page.screenshot({ path: `/tmp/monitor-${name}-${Date.now()}.png` });
        
      } catch (error) {
        metrics.errors.push(error.message);
      } finally {
        await browser.close();
      }
      
      results[name] = metrics;
    }
    
    return results;
  }
}
```

### Custom Synthetic Monitor Framework

```python
from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from typing import Any, Callable, Dict, List, Optional
import time
import json
import hashlib

@dataclass
class MonitorResult:
    monitor_name: str
    success: bool
    duration_ms: float
    timestamp: float
    metrics: Dict[str, Any] = field(default_factory=dict)
    errors: List[str] = field(default_factory=list)
    metadata: Dict[str, Any] = field(default_factory=dict)

class BaseMonitor(ABC):
    """Base class for all synthetic monitors."""
    
    def __init__(self, name: str, interval_seconds: int = 60):
        self.name = name
        self.interval_seconds = interval_seconds
        self.results: List[MonitorResult] = []
    
    @abstractmethod
    def execute(self) -> MonitorResult:
        """Execute the monitoring check."""
        pass
    
    def run_continuous(self, callback: Callable[[MonitorResult], None] = None):
        """Run monitor continuously."""
        while True:
            result = self.execute()
            self.results.append(result)
            
            if callback:
                callback(result)
            
            time.sleep(self.interval_seconds)
    
    def get_success_rate(self, window_minutes: int = 60) -> float:
        """Calculate success rate over a time window."""
        cutoff = time.time() - (window_minutes * 60)
        recent = [r for r in self.results if r.timestamp >= cutoff]
        
        if not recent:
            return 0.0
        
        return sum(1 for r in recent if r.success) / len(recent)
    
    def get_p99_latency(self, window_minutes: int = 60) -> float:
        """Calculate P99 latency over a time window."""
        cutoff = time.time() - (window_minutes * 60)
        durations = sorted([
            r.duration_ms for r in self.results if r.timestamp >= cutoff
        ])
        
        if not durations:
            return 0.0
        
        idx = int(len(durations) * 0.99)
        return durations[min(idx, len(durations) - 1)]

class HTTPMonitor(BaseMonitor):
    """HTTP endpoint monitor."""
    
    def __init__(self, name, url, method='GET', headers=None, 
                 expected_status=200, timeout=30, interval=60):
        super().__init__(name, interval)
        self.url = url
        self.method = method
        self.headers = headers or {}
        self.expected_status = expected_status
        self.timeout = timeout
    
    def execute(self) -> MonitorResult:
        import requests
        start = time.time()
        
        try:
            response = requests.request(
                self.method, self.url,
                headers=self.headers,
                timeout=self.timeout
            )
            duration = (time.time() - start) * 1000
            
            success = response.status_code == self.expected_status
            
            return MonitorResult(
                monitor_name=self.name,
                success=success,
                duration_ms=duration,
                timestamp=start,
                metrics={
                    'status_code': response.status_code,
                    'response_size': len(response.content),
                    'headers': dict(response.headers)
                },
                errors=[] if success else [f"Status {response.status_code} != {self.expected_status}"]
            )
        except Exception as e:
            duration = (time.time() - start) * 1000
            return MonitorResult(
                monitor_name=self.name,
                success=False,
                duration_ms=duration,
                timestamp=start,
                errors=[str(e)]
            )
```

---

## Implementation

### Setting Up Synthetic Monitors

**Docker-based Synthetic Monitor:**

```yaml
# docker-compose.yaml
version: '3.8'
services:
  synthetic-monitor:
    build: ./synthetic-monitor
    environment:
      - MONITOR_CONFIG=/config/monitors.yaml
      - PROMETHEUS_URL=http://prometheus:9090
      - ALERTMANAGER_URL=http://alertmanager:9093
      - GRAFANA_URL=http://grafana:3000
    volumes:
      - ./config:/config
      - ./results:/results
    deploy:
      replicas: 2
      resources:
        limits:
          cpus: '0.5'
          memory: 256M
    restart: unless-stopped

  playwright-monitor:
    image: mcr.microsoft.com/playwright:v1.40.0
    environment:
      - NODE_ENV=production
      - MONITOR_CONFIG=/config/browser-monitors.json
    volumes:
      - ./config:/config
      - ./screenshots:/screenshots
    deploy:
      replicas: 1
      resources:
        limits:
          cpus: '1.0'
          memory: 512M
```

### Alerting Thresholds

```yaml
# alerting-rules.yaml
groups:
  - name: synthetic_monitoring
    rules:
      # Availability alerts
      - alert: SyntheticMonitorDown
        expr: synthetic_check_success_rate < 0.95
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "Synthetic monitor {{ $labels.monitor }} is down"
          description: "Success rate is {{ $value | humanizePercentage }}"
      
      # Latency alerts
      - alert: SyntheticMonitorHighLatency
        expr: synthetic_check_duration_p99 > 2000
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "High latency detected for {{ $labels.monitor }}"
          description: "P99 latency is {{ $value }}ms"
      
      # SSL certificate alerts
      - alert: SSLCertificateExpiringSoon
        expr: ssl_certificate_days_remaining < 30
        for: 1h
        labels:
          severity: warning
        annotations:
          summary: "SSL certificate for {{ $labels.hostname }} expiring soon"
          description: "Certificate expires in {{ $value }} days"
      
      # SLA violation alerts
      - alert: SLAViolation
        expr: sla_compliance_ratio < 0.999
        for: 15m
        labels:
          severity: critical
        annotations:
          summary: "SLA violation detected"
          description: "Compliance ratio is {{ $value | humanizePercentage }}"
      
      # DNS resolution alerts
      - alert: DNSResolutionSlow
        expr: dns_resolution_duration_seconds > 2
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "DNS resolution slow for {{ $labels.domain }}"
          description: "Resolution time is {{ $value }}s"
```

### Integration with Observability Stack

```python
# Prometheus metrics exporter for synthetic monitoring
from prometheus_client import start_http_server, Gauge, Counter, Histogram
import time

# Define metrics
CHECK_TOTAL = Counter(
    'synthetic_checks_total',
    'Total number of synthetic checks',
    ['monitor', 'type', 'result']
)

CHECK_DURATION = Histogram(
    'synthetic_check_duration_seconds',
    'Duration of synthetic checks',
    ['monitor', 'type'],
    buckets=[0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0]
)

CHECK_SUCCESS = Gauge(
    'synthetic_check_success',
    'Whether the last synthetic check succeeded',
    ['monitor', 'type']
)

SSL_DAYS_REMAINING = Gauge(
    'ssl_certificate_days_remaining',
    'Days until SSL certificate expires',
    ['hostname']
)

DNS_RESOLUTION_DURATION = Gauge(
    'dns_resolution_duration_seconds',
    'DNS resolution duration',
    ['domain', 'record_type']
)

SLA_COMPLIANCE = Gauge(
    'sla_compliance_ratio',
    'SLA compliance ratio',
    ['service', 'slo']
)

class PrometheusExporter:
    def __init__(self, port=9090):
        self.port = port
    
    def start(self):
        start_http_server(self.port)
    
    def record_check(self, result: MonitorResult):
        """Export monitor result to Prometheus."""
        CHECK_TOTAL.labels(
            monitor=result.monitor_name,
            type='http',
            result='success' if result.success else 'failure'
        ).inc()
        
        CHECK_DURATION.labels(
            monitor=result.monitor_name,
            type='http'
        ).observe(result.duration_ms / 1000)
        
        CHECK_SUCCESS.labels(
            monitor=result.monitor_name,
            type='http'
        ).set(1 if result.success else 0)
    
    def record_ssl(self, hostname: str, days_remaining: int):
        SSL_DAYS_REMAINING.labels(hostname=hostname).set(days_remaining)
    
    def record_dns(self, domain: str, record_type: str, duration_seconds: float):
        DNS_RESOLUTION_DURATION.labels(
            domain=domain, record_type=record_type
        ).set(duration_seconds)
    
    def record_sla(self, service: str, slo: str, compliance: float):
        SLA_COMPLIANCE.labels(service=service, slo=slo).set(compliance)
```

### Dashboard Creation

```json
{
  "dashboard": {
    "title": "Synthetic Monitoring Overview",
    "panels": [
      {
        "title": "Monitor Success Rate",
        "type": "stat",
        "targets": [{
          "expr": "avg(synthetic_check_success) by (monitor)",
          "legendFormat": "{{ monitor }}"
        }],
        "fieldConfig": {
          "defaults": {
            "thresholds": {
              "steps": [
                {"value": 0, "color": "red"},
                {"value": 0.95, "color": "yellow"},
                {"value": 0.99, "color": "green"}
              ]
            }
          }
        }
      },
      {
        "title": "Response Time Distribution",
        "type": "heatmap",
        "targets": [{
          "expr": "rate(synthetic_check_duration_seconds_bucket[5m])",
          "legendFormat": "{{ monitor }} - {{ le }}"
        }]
      },
      {
        "title": "SSL Certificate Status",
        "type": "table",
        "targets": [{
          "expr": "ssl_certificate_days_remaining",
          "legendFormat": "{{ hostname }}"
        }]
      },
      {
        "title": "DNS Resolution Time",
        "type": "timeseries",
        "targets": [{
          "expr": "dns_resolution_duration_seconds",
          "legendFormat": "{{ domain }} - {{ record_type }}"
        }]
      },
      {
        "title": "SLA Compliance",
        "type": "gauge",
        "targets": [{
          "expr": "sla_compliance_ratio",
          "legendFormat": "{{ service }} - {{ slo }}"
        }],
        "fieldConfig": {
          "defaults": {
            "min": 0.9,
            "max": 1.0,
            "thresholds": {
              "steps": [
                {"value": 0.9, "color": "red"},
                {"value": 0.99, "color": "yellow"},
                {"value": 0.999, "color": "green"}
              ]
            }
          }
        }
      }
    ]
  }
}
```

### Report Generation

```python
from datetime import datetime, timedelta
from typing import Dict, List
import json

class SyntheticMonitoringReport:
    def __init__(self, results: List[MonitorResult]):
        self.results = results
    
    def generate(self, period_hours: int = 24) -> dict:
        """Generate comprehensive monitoring report."""
        cutoff = datetime.now() - timedelta(hours=period_hours)
        recent = [r for r in self.results if r.timestamp >= cutoff.timestamp()]
        
        return {
            'period': {
                'start': cutoff.isoformat(),
                'end': datetime.now().isoformat(),
                'hours': period_hours
            },
            'summary': self._calculate_summary(recent),
            'monitors': self._per_monitor_stats(recent),
            'incidents': self._identify_incidents(recent),
            'recommendations': self._generate_recommendations(recent)
        }
    
    def _calculate_summary(self, results: List[MonitorResult]) -> dict:
        total = len(results)
        successful = sum(1 for r in results if r.success)
        
        return {
            'total_checks': total,
            'successful': successful,
            'failed': total - successful,
            'success_rate': successful / total if total > 0 else 0,
            'avg_duration_ms': sum(r.duration_ms for r in results) / total if total > 0 else 0,
            'p95_duration_ms': self._percentile([r.duration_ms for r in results], 95),
            'p99_duration_ms': self._percentile([r.duration_ms for r in results], 99)
        }
    
    def _per_monitor_stats(self, results: List[MonitorResult]) -> dict:
        monitors = {}
        for r in results:
            if r.monitor_name not in monitors:
                monitors[r.monitor_name] = []
            monitors[r.monitor_name].append(r)
        
        stats = {}
        for name, monitor_results in monitors.items():
            total = len(monitor_results)
            successful = sum(1 for r in monitor_results if r.success)
            stats[name] = {
                'checks': total,
                'success_rate': successful / total if total > 0 else 0,
                'avg_duration_ms': sum(r.duration_ms for r in monitor_results) / total if total > 0 else 0
            }
        
        return stats
    
    def _identify_incidents(self, results: List[MonitorResult]) -> List[dict]:
        incidents = []
        # Group by monitor and find consecutive failures
        monitors = {}
        for r in sorted(results, key=lambda x: x.timestamp):
            if r.monitor_name not in monitors:
                monitors[r.monitor_name] = []
            monitors[r.monitor_name].append(r)
        
        for name, monitor_results in monitors.items():
            consecutive_failures = 0
            start_time = None
            
            for r in monitor_results:
                if not r.success:
                    if consecutive_failures == 0:
                        start_time = r.timestamp
                    consecutive_failures += 1
                else:
                    if consecutive_failures >= 3:  # 3+ failures = incident
                        incidents.append({
                            'monitor': name,
                            'start': datetime.fromtimestamp(start_time).isoformat(),
                            'end': datetime.fromtimestamp(r.timestamp).isoformat(),
                            'failures': consecutive_failures
                        })
                    consecutive_failures = 0
        
        return incidents
    
    def _generate_recommendations(self, results: List[MonitorResult]) -> List[str]:
        recommendations = []
        summary = self._calculate_summary(results)
        
        if summary['success_rate'] < 0.99:
            recommendations.append("Success rate below 99% - investigate failing checks")
        
        if summary['p99_duration_ms'] > 5000:
            recommendations.append("P99 latency above 5s - optimize critical paths")
        
        return recommendations
    
    def _percentile(self, data: List[float], percentile: int) -> float:
        if not data:
            return 0
        sorted_data = sorted(data)
        idx = int(len(sorted_data) * percentile / 100)
        return sorted_data[min(idx, len(sorted_data) - 1)]
```

---

## Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Synthetic Monitoring Platform                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   HTTP/API   │  │   Browser    │  │   Network    │          │
│  │   Monitors   │  │   Monitors   │  │   Monitors   │          │
│  │              │  │              │  │              │          │
│  │  - REST      │  │  - Playwright│  │  - Ping/ICMP │          │
│  │  - GraphQL   │  │  - Selenium  │  │  - DNS       │          │
│  │  - gRPC      │  │  - Puppeteer │  │  - SSL/TLS   │          │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘          │
│         │                 │                 │                    │
│         └─────────────────┼─────────────────┘                    │
│                           │                                      │
│                    ┌──────▼───────┐                              │
│                    │   Collector  │                              │
│                    │              │                              │
│                    │  - Metrics   │                              │
│                    │  - Events    │                              │
│                    │  - Logs      │                              │
│                    └──────┬───────┘                              │
│                           │                                      │
│              ┌────────────┼────────────┐                        │
│              │            │            │                         │
│        ┌─────▼─────┐ ┌───▼───┐ ┌─────▼─────┐                  │
│        │Prometheus │ │ Jaeger│ │  Loki     │                  │
│        │  Metrics  │ │Traces │ │   Logs    │                  │
│        └─────┬─────┘ └───┬───┘ └─────┬─────┘                  │
│              │            │            │                         │
│              └────────────┼────────────┘                        │
│                           │                                      │
│                    ┌──────▼───────┐                              │
│                    │   Grafana    │                              │
│                    │  Dashboards  │                              │
│                    └──────┬───────┘                              │
│                           │                                      │
│              ┌────────────┼────────────┐                        │
│              │            │            │                         │
│        ┌─────▼─────┐ ┌───▼───┐ ┌─────▼─────┐                  │
│        │  Alerts   │ │ Report│ │   SLA     │                  │
│        │           │ │       │ │  Tracking │                  │
│        └───────────┘ └───────┘ └───────────┘                  │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
```

### Data Flow

```
1. Monitor Execution
   ├─> Schedule triggers check
   ├─> Execute synthetic transaction
   ├─> Capture metrics & logs
   └─> Send to collector

2. Data Processing
   ├─> Validate responses
   ├─> Calculate metrics
   ├─> Store time series
   └─> Update dashboards

3. Alerting
   ├─> Evaluate alert rules
   ├─> Send notifications
   └─> Trigger incident response

4. Reporting
   ├─> Aggregate metrics
   ├─> Calculate SLAs
   ├─> Generate reports
   └─> Trend analysis
```

---

## Getting Started

### Quick Start

1. **Install dependencies:**
   ```bash
   pip install requests prometheus-client playwright dns-python
   npx playwright install chromium
   ```

2. **Configure monitors:**
   ```yaml
   # config/monitors.yaml
   monitors:
     - name: "API Health"
       type: http
       url: https://your-api.com/health
       interval: 60
   ```

3. **Run synthetic monitor:**
   ```bash
   python -m synthetic_monitor --config config/monitors.yaml
   ```

4. **View metrics:**
   ```bash
   # Access Prometheus metrics
   curl http://localhost:9090/metrics
   ```

### Best Practices

1. **Start with critical paths:** Monitor login, checkout, and core API endpoints first
2. **Use realistic data:** Synthetic transactions should mirror real user behavior
3. **Set appropriate intervals:** Balance between detection speed and resource usage
4. **Implement graceful degradation:** Monitors should not impact production systems
5. **Monitor from multiple locations:** Geographic diversity reveals regional issues
6. **Integrate with alerting:** Connect synthetic results to your incident response workflow
7. **Regular review:** Periodically update synthetic scripts as applications evolve
8. **Document everything:** Maintain runbooks for each monitored endpoint
