# Examples

## Complete Examples

This folder contains comprehensive, working examples of synthetic and artificial monitoring implementations.

---

## Example 1: Full HTTP Synthetic Monitor

```python
#!/usr/bin/env python3
"""
Complete HTTP Synthetic Monitor with Prometheus metrics export.
"""

import time
import requests
import threading
from prometheus_client import start_http_server, Histogram, Counter, Gauge
from dataclasses import dataclass
from typing import List, Dict, Optional
import json

# Prometheus metrics
CHECK_DURATION = Histogram(
    'synthetic_http_check_duration_seconds',
    'Duration of HTTP checks',
    ['endpoint', 'method'],
    buckets=[0.1, 0.25, 0.5, 1.0, 2.5, 5.0, 10.0]
)

CHECK_RESULT = Counter(
    'synthetic_http_check_result_total',
    'Result of HTTP checks',
    ['endpoint', 'method', 'status']
)

CHECK_SUCCESS = Gauge(
    'synthetic_http_check_success',
    'Whether the last check succeeded',
    ['endpoint', 'method']
)

@dataclass
class MonitorConfig:
    name: str
    url: str
    method: str = 'GET'
    headers: Dict = None
    payload: Optional[Dict] = None
    expected_status: int = 200
    timeout: int = 30
    interval: int = 60
    validate_json: bool = False
    json_schema: Optional[Dict] = None

class HTTPSyntheticMonitor:
    def __init__(self, configs: List[MonitorConfig]):
        self.configs = configs
        self.running = False
    
    def run_check(self, config: MonitorConfig) -> Dict:
        """Execute a single check."""
        start = time.time()
        
        try:
            response = requests.request(
                config.method,
                config.url,
                headers=config.headers or {},
                json=config.payload,
                timeout=config.timeout
            )
            
            duration = time.time() - start
            success = response.status_code == config.expected_status
            
            # Validate JSON if required
            if config.validate_json and success:
                try:
                    data = response.json()
                    if config.json_schema:
                        # Schema validation would go here
                        pass
                except json.JSONDecodeError:
                    success = False
            
            return {
                'success': success,
                'status': response.status_code,
                'duration': duration,
                'size': len(response.content)
            }
            
        except Exception as e:
            duration = time.time() - start
            return {
                'success': False,
                'status': 0,
                'duration': duration,
                'error': str(e)
            }
    
    def run_monitor(self, config: MonitorConfig):
        """Run continuous monitoring for a single endpoint."""
        while self.running:
            result = self.run_check(config)
            
            # Record metrics
            CHECK_DURATION.labels(
                endpoint=config.name,
                method=config.method
            ).observe(result['duration'])
            
            CHECK_RESULT.labels(
                endpoint=config.name,
                method=config.method,
                status=str(result['status'])
            ).inc()
            
            CHECK_SUCCESS.labels(
                endpoint=config.name,
                method=config.method
            ).set(1 if result['success'] else 0)
            
            if not result['success']:
                print(f"[FAIL] {config.name}: {result.get('error', f'Status {result[\"status\"]}')}")
            
            time.sleep(config.interval)
    
    def start(self):
        """Start monitoring all endpoints."""
        self.running = True
        
        threads = []
        for config in self.configs:
            t = threading.Thread(target=self.run_monitor, args=(config,))
            t.daemon = True
            t.start()
            threads.append(t)
        
        print(f"Started {len(self.configs)} monitors")
        
        # Keep main thread alive
        try:
            while self.running:
                time.sleep(1)
        except KeyboardInterrupt:
            self.running = False
            print("Stopping monitors...")

# Usage
if __name__ == '__main__':
    # Start Prometheus metrics server
    start_http_server(9090)
    
    # Define monitors
    monitors = [
        MonitorConfig(
            name='API Health',
            url='https://api.example.com/health',
            expected_status=200,
            interval=30
        ),
        MonitorConfig(
            name='User API',
            url='https://api.example.com/v1/users',
            method='GET',
            headers={'Authorization': 'Bearer ${TOKEN}'},
            expected_status=200,
            interval=60
        ),
        MonitorConfig(
            name='Product Search',
            url='https://api.example.com/v1/products?q=test',
            expected_status=200,
            validate_json=True,
            interval=120
        ),
    ]
    
    # Start monitor
    monitor = HTTPSyntheticMonitor(monitors)
    monitor.start()
```

---

## Example 2: Browser Synthetic Monitor

```javascript
/**
 * Complete Browser Synthetic Monitor using Playwright
 */

const { chromium, firefox, webkit } = require('playwright');
const prometheus = require('prom-client');

// Prometheus metrics
const checkDuration = new prometheus.Histogram({
  name: 'synthetic_browser_check_duration_seconds',
  help: 'Duration of browser checks',
  labelNames: ['page', 'browser'],
  buckets: [1, 2, 5, 10, 15, 20, 30]
});

const checkSuccess = new prometheus.Gauge({
  name: 'synthetic_browser_check_success',
  help: 'Whether the last check succeeded',
  labelNames: ['page', 'browser']
});

const webVitals = new prometheus.Gauge({
  name: 'synthetic_web_vitals',
  help: 'Web Vitals metrics',
  labelNames: ['page', 'metric']
});

class BrowserSyntheticMonitor {
  constructor() {
    this.browsers = { chromium, firefox, webkit };
    this.results = [];
  }

  async monitorPage(pageConfig) {
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
        webVitals: {},
        errors: []
      };

      try {
        // Navigate and measure
        const start = Date.now();
        await page.goto(pageConfig.url, {
          waitUntil: pageConfig.waitUntil || 'networkidle',
          timeout: 30000
        });
        metrics.loadTime = Date.now() - start;

        // Collect Web Vitals
        metrics.webVitals = await this.collectWebVitals(page);

        // Validate content
        if (pageConfig.expectedText) {
          const content = await page.textContent('body');
          metrics.contentValid = content.includes(pageConfig.expectedText);
        }

        // Take screenshot
        await page.screenshot({
          path: `/tmp/monitor-${name}-${Date.now()}.png`
        });

      } catch (error) {
        metrics.errors.push(error.message);
      } finally {
        await browser.close();
      }

      // Record Prometheus metrics
      checkDuration.labels({
        page: pageConfig.name,
        browser: name
      }).observe(metrics.loadTime / 1000);

      checkSuccess.labels({
        page: pageConfig.name,
        browser: name
      }).set(metrics.errors.length === 0 ? 1 : 0);

      // Record Web Vitals
      for (const [metric, value] of Object.entries(metrics.webVitals)) {
        webVitals.labels({
          page: pageConfig.name,
          metric: metric
        }).set(value);
      }

      results[name] = metrics;
    }

    return results;
  }

  async collectWebVitals(page) {
    return await page.evaluate(() => {
      return new Promise(resolve => {
        const vitals = {};

        // LCP
        new PerformanceObserver(list => {
          const entries = list.getEntries();
          vitals.LCP = entries[entries.length - 1].startTime;
        }).observe({ type: 'largest-contentful-paint', buffered: true });

        // CLS
        new PerformanceObserver(list => {
          let cls = 0;
          for (const entry of list.getEntries()) {
            if (!entry.hadRecentInput) cls += entry.value;
          }
          vitals.CLS = cls;
        }).observe({ type: 'layout-shift', buffered: true });

        // FCP
        new PerformanceObserver(list => {
          const entries = list.getEntries();
          vitals.FCP = entries[entries.length - 1].startTime;
        }).observe({ type: 'paint', buffered: true });

        setTimeout(() => resolve(vitals), 3000);
      });
    });
  }
}

// Usage
async function main() {
  const monitor = new BrowserSyntheticMonitor();
  
  const pages = [
    {
      name: 'homepage',
      url: 'https://example.com',
      waitUntil: 'networkidle',
      expectedText: 'Welcome'
    },
    {
      name: 'login',
      url: 'https://example.com/login',
      waitUntil: 'networkidle',
      expectedText: 'Sign In'
    }
  ];

  for (const page of pages) {
    console.log(`Monitoring ${page.name}...`);
    const results = await monitor.monitorPage(page);
    console.log(JSON.stringify(results, null, 2));
  }
}

main().catch(console.error);
```

---

## Example 3: Chaos Engineering Integration

```python
#!/usr/bin/env python3
"""
Chaos Engineering Experiment Runner with Monitoring
"""

import time
import subprocess
import threading
from dataclasses import dataclass
from typing import Callable, Dict, List
from enum import Enum

class FaultType(Enum):
    LATENCY = "latency"
    PACKET_LOSS = "packet_loss"
    CPU_STRESS = "cpu_stress"
    MEMORY_STRESS = "memory_stress"
    SERVICE_KILL = "service_kill"

@dataclass
class Experiment:
    name: str
    fault_type: FaultType
    parameters: Dict
    duration_seconds: int
    target: str = "localhost"

class ChaosExperimentRunner:
    def __init__(self):
        self.active_faults = []
    
    def inject_fault(self, experiment: Experiment):
        """Inject fault based on experiment type."""
        if experiment.fault_type == FaultType.LATENCY:
            self._inject_latency(experiment)
        elif experiment.fault_type == FaultType.PACKET_LOSS:
            self._inject_packet_loss(experiment)
        elif experiment.fault_type == FaultType.CPU_STRESS:
            self._stress_cpu(experiment)
        elif experiment.fault_type == FaultType.MEMORY_STRESS:
            self._stress_memory(experiment)
        elif experiment.fault_type == FaultType.SERVICE_KILL:
            self._kill_service(experiment)
    
    def _inject_latency(self, experiment: Experiment):
        """Inject network latency."""
        delay = experiment.parameters.get('delay_ms', 100)
        cmd = [
            'tc', 'qdisc', 'add', 'dev', 'eth0',
            'root', 'netem', 'delay', f'{delay}ms'
        ]
        subprocess.run(cmd, check=True)
        self.active_faults.append(('latency', 'eth0'))
    
    def _inject_packet_loss(self, experiment: Experiment):
        """Inject packet loss."""
        loss = experiment.parameters.get('loss_percent', 10)
        cmd = [
            'tc', 'qdisc', 'add', 'dev', 'eth0',
            'root', 'netem', 'loss', f'{loss}%'
        ]
        subprocess.run(cmd, check=True)
        self.active_faults.append(('packet_loss', 'eth0'))
    
    def _stress_cpu(self, experiment: Experiment):
        """Generate CPU load."""
        cores = experiment.parameters.get('cores', 2)
        duration = experiment.duration_seconds
        cmd = [
            'stress-ng', '--cpu', str(cores),
            '--timeout', f'{duration}s'
        ]
        subprocess.Popen(cmd)
    
    def _stress_memory(self, experiment: Experiment):
        """Generate memory pressure."""
        size_mb = experiment.parameters.get('size_mb', 256)
        cmd = [
            'stress-ng', '--vm', '1',
            '--vm-bytes', f'{size_mb}M',
            '--timeout', f'{experiment.duration_seconds}s'
        ]
        subprocess.Popen(cmd)
    
    def _kill_service(self, experiment: Experiment):
        """Kill a service."""
        service = experiment.parameters.get('service_name', 'nginx')
        cmd = ['systemctl', 'stop', service]
        subprocess.run(cmd, check=True)
        self.active_faults.append(('service', service))
    
    def revert_all(self):
        """Revert all active faults."""
        for fault_type, target in self.active_faults:
            if fault_type in ('latency', 'packet_loss'):
                subprocess.run([
                    'tc', 'qdisc', 'del', 'dev', target, 'root'
                ], capture_output=True)
            elif fault_type == 'service':
                subprocess.run([
                    'systemctl', 'start', target
                ], capture_output=True)
        
        self.active_faults.clear()
    
    def run_experiment(self, experiment: Experiment, monitor_fn: Callable):
        """Run chaos experiment with monitoring."""
        print(f"Starting experiment: {experiment.name}")
        
        # Capture baseline
        baseline = monitor_fn()
        print(f"Baseline: {baseline}")
        
        # Inject fault
        self.inject_fault(experiment)
        print(f"Fault injected: {experiment.fault_type.value}")
        
        # Monitor during experiment
        during = []
        start = time.time()
        while time.time() - start < experiment.duration_seconds:
            metrics = monitor_fn()
            during.append(metrics)
            print(f"During: {metrics}")
            time.sleep(10)
        
        # Revert
        self.revert_all()
        print("Faults reverted")
        
        # Wait for recovery
        time.sleep(30)
        recovery = monitor_fn()
        print(f"Recovery: {recovery}")
        
        return {
            'baseline': baseline,
            'during': during,
            'recovery': recovery,
            'recovered': self._check_recovery(baseline, recovery)
        }
    
    def _check_recovery(self, baseline, recovery, threshold=0.1):
        """Check if system recovered."""
        for key in baseline:
            if baseline[key] > 0:
                deviation = abs(recovery[key] - baseline[key]) / baseline[key]
                if deviation > threshold:
                    return False
        return True

# Example usage
def monitor_system():
    """Simple system monitor."""
    import psutil
    return {
        'cpu_percent': psutil.cpu_percent(),
        'memory_percent': psutil.virtual_memory().percent
    }

if __name__ == '__main__':
    runner = ChaosExperimentRunner()
    
    # Define experiment
    experiment = Experiment(
        name="CPU Stress Test",
        fault_type=FaultType.CPU_STRESS,
        parameters={'cores': 2},
        duration_seconds=60
    )
    
    # Run experiment
    results = runner.run_experiment(experiment, monitor_system)
    print(f"\nResults: {results}")
```

---

## Example 4: SLA Monitoring Dashboard

```python
#!/usr/bin/env python3
"""
SLA Monitoring with Prometheus and Grafana
"""

from prometheus_client import start_http_server, Gauge, Counter
import time
from dataclasses import dataclass
from typing import Dict, List

# Prometheus metrics
SLA_COMPLIANCE = Gauge(
    'sla_compliance_ratio',
    'SLA compliance ratio',
    ['service', 'slo']
)

ERROR_BUDGET = Gauge(
    'sla_error_budget_remaining',
    'Error budget remaining ratio',
    ['service']
)

SLA_ERRORS = Counter(
    'sla_errors_total',
    'Total SLA errors',
    ['service', 'type']
)

@dataclass
class SLODefinition:
    name: str
    target: float  # e.g., 0.999 for 99.9%
    window_days: int
    error_budget_days: float

class SLAMonitor:
    def __init__(self, slo_definitions: List[SLODefinition]):
        self.slo_definitions = {slo.name: slo for slo in slo_definitions}
        self.error_counts: Dict[str, int] = {slo.name: 0 for slo in slo_definitions}
        self.total_checks: Dict[str, int] = {slo.name: 0 for slo in slo_definitions}
    
    def record_check(self, service_name: str, success: bool):
        """Record a check result."""
        self.total_checks[service_name] = self.total_checks.get(service_name, 0) + 1
        if not success:
            self.error_counts[service_name] = self.error_counts.get(service_name, 0) + 1
        
        # Update metrics
        self._update_metrics(service_name)
    
    def _update_metrics(self, service_name: str):
        """Update Prometheus metrics."""
        if service_name not in self.slo_definitions:
            return
        
        slo = self.slo_definitions[service_name]
        total = self.total_checks[service_name]
        errors = self.error_counts[service_name]
        
        if total > 0:
            compliance = 1 - (errors / total)
            error_budget = max(0, (slo.target - compliance) / (1 - slo.target))
            
            SLA_COMPLIANCE.labels(
                service=service_name,
                slo=slo.name
            ).set(compliance)
            
            ERROR_BUDGET.labels(
                service=service_name
            ).set(error_budget)
    
    def get_report(self) -> Dict:
        """Generate SLA report."""
        report = {}
        
        for service_name, slo in self.slo_definitions.items():
            total = self.total_checks.get(service_name, 0)
            errors = self.error_counts.get(service_name, 0)
            
            if total > 0:
                compliance = 1 - (errors / total)
                error_budget = max(0, (slo.target - compliance) / (1 - slo.target))
            else:
                compliance = 0
                error_budget = 1
            
            report[service_name] = {
                'slo_target': slo.target,
                'current_compliance': compliance,
                'error_budget_remaining': error_budget,
                'total_checks': total,
                'errors': errors,
                'breached': compliance < slo.target
            }
        
        return report

# Usage
if __name__ == '__main__':
    start_http_server(9090)
    
    # Define SLOs
    slos = [
        SLODefinition(
            name='availability',
            target=0.999,  # 99.9%
            window_days=30,
            error_budget_days=0.03  # ~43 minutes per month
        ),
        SLODefinition(
            name='latency',
            target=0.99,  # 99% of requests under 500ms
            window_days=30,
            error_budget_days=0.3
        )
    ]
    
    # Create monitor
    monitor = SLAMonitor(slos)
    
    # Simulate checks
    import random
    while True:
        # Simulate check (99.9% success rate)
        success = random.random() > 0.001
        monitor.record_check('availability', success)
        
        # Generate report every minute
        report = monitor.get_report()
        print(f"\nSLA Report: {report}")
        
        time.sleep(60)
```

---

## Running the Examples

1. **HTTP Monitor:**
   ```bash
   pip install requests prometheus-client
   python http_monitor.py
   ```

2. **Browser Monitor:**
   ```bash
   npm install playwright prom-client
   node browser_monitor.js
   ```

3. **Chaos Engineering:**
   ```bash
   pip install psutil
   sudo python chaos_runner.py  # Needs root for tc/iptables
   ```

4. **SLA Monitor:**
   ```bash
   pip install prometheus-client
   python sla_monitor.py
   ```
