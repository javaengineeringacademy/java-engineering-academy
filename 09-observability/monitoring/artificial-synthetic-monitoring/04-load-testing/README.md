# Load Testing

## Overview

Load testing as monitoring involves running periodic load tests to detect performance regressions, validate capacity, and ensure systems can handle expected traffic. Unlike traditional load testing (pre-deployment), load testing as monitoring is continuous and integrated into observability.

---

## Load Testing as Monitoring

### Continuous Load Testing Pattern

```yaml
# k6 load test as continuous monitoring
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate, Trend, Counter } from 'k6/metrics';

const errorRate = new Rate('errors');
const latencyP99 = new Trend('latency_p99');
const requestCount = new Counter('requests');

export const options = {
  scenarios: {
    // Baseline load - always running
    baseline: {
      executor: 'constant-vus',
      vus: 50,
      duration: '30m',
      exec: 'baselineScenario',
    },
    // Peak load - periodic spikes
    peak: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '5m', target: 200 },
        { duration: '10m', target: 200 },
        { duration: '5m', target: 0 },
      ],
      exec: 'peakScenario',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<500', 'p(99)<1000'],
    errors: ['rate<0.01'],
    latency_p99: ['value<1000'],
  },
};

export function baselineScenario() {
  const res = http.get('https://api.example.com/v1/products');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });
  
  errorRate.add(res.status >= 400);
  latencyP99.add(res.timings.duration);
  requestCount.add(1);
  
  sleep(1);
}

export function peakScenario() {
  const res = http.get('https://api.example.com/v1/products');
  
  check(res, {
    'status is 200': (r) => r.status === 200,
    'response time < 1000ms': (r) => r.timings.duration < 1000,
  });
  
  errorRate.add(res.status >= 400);
  requestCount.add(1);
  
  sleep(0.5);
}
```

---

## Load Test Types

### 1. Constant Load

Maintains steady traffic to detect gradual degradation.

```javascript
export const options = {
  scenarios: {
    constant: {
      executor: 'constant-vus',
      vus: 100,
      duration: '1h',
    },
  },
};
```

### 2. Ramp-Up Load

Gradually increases load to identify breaking points.

```javascript
export const options = {
  scenarios: {
    ramping: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '5m', target: 100 },  // Ramp up
        { duration: '10m', target: 100 }, // Sustain
        { duration: '5m', target: 0 },    // Ramp down
      ],
    },
  },
};
```

### 3. Stress Testing

Pushes beyond normal capacity to test resilience.

```javascript
export const options = {
  scenarios: {
    stress: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '2m', target: 200 },   // Normal
        { duration: '2m', target: 500 },   // High
        { duration: '2m', target: 1000 },  // Stress
        { duration: '2m', target: 500 },   // Recovery
        { duration: '2m', target: 0 },     // Back to normal
      ],
    },
  },
};
```

### 4. Spike Testing

Sudden traffic spikes to test auto-scaling.

```javascript
export const options = {
  scenarios: {
    spike: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '1m', target: 50 },    // Normal
        { duration: '10s', target: 1000 }, // Spike
        { duration: '1m', target: 50 },    // Recovery
      ],
    },
  },
};
```

---

## Custom Metrics

### Defining Metrics

```javascript
import { Counter, Gauge, Rate, Trend } from 'k6/metrics';

// Custom counters
const successfulLogins = new Counter('successful_logins');
const failedLogins = new Counter('failed_logins');

// Custom gauges
const activeUsers = new Gauge('active_users');

// Custom rates
const loginSuccessRate = new Rate('login_success_rate');

// Custom trends
const orderProcessingTime = new Trend('order_processing_time');
```

### Recording Metrics

```javascript
export default function () {
  // Login scenario
  const loginRes = http.post('https://api.example.com/auth/login', 
    JSON.stringify({
      username: `user${__VU}`,
      password: 'test123'
    }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  
  if (loginRes.status === 200) {
    successfulLogins.add(1);
    loginSuccessRate.add(true);
  } else {
    failedLogins.add(1);
    loginSuccessRate.add(false);
  }
  
  // Order processing
  const orderStart = Date.now();
  const orderRes = http.post('https://api.example.com/orders',
    JSON.stringify({
      product_id: 'test-product',
      quantity: 1
    }),
    { headers: { 'Content-Type': 'application/json' } }
  );
  orderProcessingTime.add(Date.now() - orderStart);
  
  sleep(1);
}
```

---

## Performance Baselines

### Establishing Baselines

```python
import json
import statistics
from dataclasses import dataclass
from typing import List, Dict

@dataclass
class PerformanceBaseline:
    name: str
    p50_ms: float
    p95_ms: float
    p99_ms: float
    error_rate: float
    throughput_rps: float
    created_at: str
    
    def compare(self, current: Dict) -> Dict:
        """Compare current metrics against baseline."""
        return {
            'latency_p50': {
                'baseline': self.p50_ms,
                'current': current.get('p50_ms', 0),
                'deviation': (current.get('p50_ms', 0) - self.p50_ms) / self.p50_ms
            },
            'latency_p95': {
                'baseline': self.p95_ms,
                'current': current.get('p95_ms', 0),
                'deviation': (current.get('p95_ms', 0) - self.p95_ms) / self.p95_ms
            },
            'error_rate': {
                'baseline': self.error_rate,
                'current': current.get('error_rate', 0),
                'deviation': current.get('error_rate', 0) - self.error_rate
            }
        }

class BaselineManager:
    def __init__(self, baseline_file='baselines.json'):
        self.baseline_file = baseline_file
        self.baselines = self.load_baselines()
    
    def load_baselines(self):
        try:
            with open(self.baseline_file, 'r') as f:
                data = json.load(f)
                return {k: PerformanceBaseline(**v) for k, v in data.items()}
        except FileNotFoundError:
            return {}
    
    def save_baselines(self):
        with open(self.baseline_file, 'w') as f:
            json.dump({k: vars(v) for k, v in self.baselines.items()}, f, indent=2)
    
    def update_baseline(self, name: str, metrics: Dict):
        """Update baseline with new metrics."""
        baseline = PerformanceBaseline(
            name=name,
            p50_ms=metrics['p50_ms'],
            p95_ms=metrics['p95_ms'],
            p99_ms=metrics['p99_ms'],
            error_rate=metrics['error_rate'],
            throughput_rps=metrics['throughput_rps'],
            created_at=metrics.get('timestamp', '')
        )
        self.baselines[name] = baseline
        self.save_baselines()
    
    def check_regression(self, name: str, current: Dict, threshold: float = 0.1) -> bool:
        """Check if current metrics show regression."""
        if name not in self.baselines:
            return False
        
        comparison = self.baselines[name].compare(current)
        
        # Check if any metric deviates beyond threshold
        for metric, values in comparison.items():
            if abs(values['deviation']) > threshold:
                return True
        
        return False
```

---

## Alert Configuration

### Load Test Alerts

```yaml
groups:
  - name: load_testing
    rules:
      # Performance regression
      - alert: PerformanceRegression
        expr: |
          load_test_latency_p99 > 
          load_test_baseline_p99 * 1.2
        for: 15m
        labels:
          severity: warning
        annotations:
          summary: "Performance regression detected"
          description: "P99 latency is {{ $value }}ms (20% above baseline)"
      
      # High error rate during load test
      - alert: LoadTestHighErrors
        expr: load_test_error_rate > 0.05
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "High error rate during load test"
          description: "Error rate is {{ $value | humanizePercentage }}"
      
      # Throughput drop
        - alert: ThroughputDrop
        expr: |
          rate(load_test_requests_total[5m]) < 
          load_test_baseline_throughput * 0.8
        for: 10m
        labels:
          severity: warning
        annotations:
          summary: "Throughput below baseline"
          description: "Current: {{ $value }} RPS, Baseline: {{ $labels.baseline }} RPS"
```

---

## Integration with CI/CD

### GitHub Actions Integration

```yaml
# .github/workflows/load-test.yml
name: Load Test

on:
  schedule:
    - cron: '0 */6 * * *'  # Every 6 hours
  workflow_dispatch:

jobs:
  load-test:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v3
      
      - name: Run k6 load test
        uses: grafana/k6-action@v0.3.0
        with:
          filename: tests/load-test.js
          
      - name: Compare with baseline
        run: |
          python scripts/compare_results.py \
            --current k6-results.json \
            --baseline baselines/performance.json \
            --threshold 0.1
            
      - name: Upload results
        uses: actions/upload-artifact@v3
        with:
          name: load-test-results
          path: k6-results.json
```

---

## Best Practices

1. **Start with Baselines:** Establish performance baselines before monitoring
2. **Run Regular Tests:** Schedule load tests to run continuously
3. **Monitor Key Metrics:** Track latency, throughput, and error rates
4. **Set Realistic Thresholds:** Base thresholds on actual performance data
5. **Test Critical Paths:** Focus on user journeys that impact revenue
6. **Include Think Time:** Simulate real user behavior with pauses
7. **Monitor Infrastructure:** Track CPU, memory, and network during tests
8. **Automate Regression Detection:** Use tools to automatically detect performance issues

---

## Next Steps

- [Chaos Engineering](../05-chaos-engineering/README.md) - Chaos engineering integration
- [Alerting Thresholds](../06-alerting-thresholds/README.md) - Setting up alerts
- [Dashboard Integration](../07-dashboard-integration/README.md) - Creating dashboards
