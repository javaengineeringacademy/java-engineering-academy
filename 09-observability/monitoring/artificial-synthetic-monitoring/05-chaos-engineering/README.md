# Chaos Engineering

## Overview

Chaos engineering is the discipline of experimenting on systems to build confidence in their ability to withstand turbulent conditions in production. When integrated with synthetic monitoring, it validates that systems degrade gracefully under failure conditions.

---

## Principles of Chaos Engineering

### 1. Build a Hypothesis
Define steady state behavior that indicates normal operation.

### 2. Vary Real-World Events
Introduce realistic failure scenarios.

### 3. Run Experiments in Production
Test in the environment where real failures occur.

### 4. Automate Experiments
Run experiments continuously to catch regressions.

### 5. Minimize Blast Radius
Control the scope of experiments to limit impact.

---

## Fault Injection Types

### Network Faults

```python
import subprocess
import time
from enum import Enum

class NetworkFault(Enum):
    LATENCY = "latency"
    PACKET_LOSS = "packet_loss"
    CORRUPTION = "corruption"
    DUPLICATION = "duplication"
    PARTITION = "partition"

class NetworkInjector:
    def __init__(self, interface='eth0'):
        self.interface = interface
        self.active_rules = []
    
    def inject_latency(self, delay_ms=100, jitter_ms=50, duration=60):
        """Add latency to network traffic."""
        cmd = [
            'tc', 'qdisc', 'add', 'dev', self.interface,
            'root', 'netem', 'delay',
            f'{delay_ms}ms', f'{jitter_ms}ms'
        ]
        subprocess.run(cmd, check=True)
        self.active_rules.append(('latency', time.time()))
        
        if duration:
            time.sleep(duration)
            self.remove_all()
    
    def inject_packet_loss(self, percentage=10, duration=60):
        """Simulate packet loss."""
        cmd = [
            'tc', 'qdisc', 'add', 'dev', self.interface,
            'root', 'netem', 'loss', f'{percentage}%'
        ]
        subprocess.run(cmd, check=True)
        self.active_rules.append(('packet_loss', time.time()))
        
        if duration:
            time.sleep(duration)
            self.remove_all()
    
    def inject_partition(self, target_ip):
        """Create network partition to target."""
        cmd = [
            'iptables', '-A', 'OUTPUT', '-d', target_ip,
            '-j', 'DROP'
        ]
        subprocess.run(cmd, check=True)
        self.active_rules.append(('partition', time.time()))
    
    def remove_all(self):
        """Remove all fault injection rules."""
        subprocess.run(['tc', 'qdisc', 'del', 'dev', self.interface, 'root'],
                      capture_output=True)
        subprocess.run(['iptables', '-F', 'OUTPUT'],
                      capture_output=True)
        self.active_rules.clear()
```

### CPU/Memory Faults

```python
import multiprocessing
import time
import os

class ResourceInjector:
    @staticmethod
    def inject_cpu_load(cores=2, duration_seconds=60, load_percent=80):
        """Generate CPU load on specified cores."""
        def cpu_stress(stop_event):
            while not stop_event.is_set():
                # Busy loop to consume CPU
                _ = sum(i * i for i in range(10000))
        
        stop_event = multiprocessing.Event()
        processes = []
        
        for _ in range(cores):
            p = multiprocessing.Process(target=cpu_stress, args=(stop_event,))
            p.start()
            processes.append(p)
        
        time.sleep(duration_seconds)
        stop_event.set()
        
        for p in processes:
            p.join(timeout=5)
            if p.is_alive():
                p.terminate()
    
    @staticmethod
    def inject_memory_pressure(target_mb=512, duration_seconds=60):
        """Consume memory to simulate pressure."""
        def memory_stress(stop_event, target_mb):
            chunks = []
            chunk_size = 1024 * 1024  # 1MB
            target_chunks = target_mb
            
            while not stop_event.is_set() and len(chunks) < target_chunks:
                chunks.append(bytearray(chunk_size))
                time.sleep(0.01)
            
            # Hold memory
            while not stop_event.is_set():
                time.sleep(1)
            
            del chunks
        
        stop_event = multiprocessing.Event()
        p = multiprocessing.Process(
            target=memory_stress, 
            args=(stop_event, target_mb)
        )
        p.start()
        
        time.sleep(duration_seconds)
        stop_event.set()
        p.join(timeout=5)
        if p.is_alive():
            p.terminate()
```

### Service Faults

```python
import docker
import signal
import os

class ServiceInjector:
    def __init__(self):
        self.client = docker.from_env()
    
    def stop_container(self, container_name, duration_seconds=30):
        """Stop a Docker container temporarily."""
        try:
            container = self.client.containers.get(container_name)
            container.stop()
            
            time.sleep(duration_seconds)
            
            container.start()
            return {'success': True, 'container': container_name}
        except Exception as e:
            return {'success': False, 'error': str(e)}
    
    def kill_process(self, process_name, signal_type=signal.SIGKILL):
        """Kill a process by name."""
        import subprocess
        result = subprocess.run(
            ['pkill', '-f', process_name],
            capture_output=True, text=True
        )
        return {'success': result.returncode == 0}
    
    def corrupt_data(self, file_path, corruption_type='random'):
        """Corrupt a file to simulate data issues."""
        try:
            with open(file_path, 'r+b') as f:
                content = bytearray(f.read())
                
                if corruption_type == 'random':
                    import random
                    idx = random.randint(0, len(content) - 1)
                    content[idx] = 0xFF
                elif corruption_type == 'zero':
                    content[:100] = b'\x00' * 100
                
                f.seek(0)
                f.write(content)
            
            return {'success': True}
        except Exception as e:
            return {'success': False, 'error': str(e)}
```

---

## Chaos Experiments

### Experiment Framework

```python
from dataclasses import dataclass, field
from typing import Any, Callable, Dict, List
from enum import Enum
import time

class ExperimentStatus(Enum):
    PENDING = "pending"
    RUNNING = "running"
    COMPLETED = "completed"
    FAILED = "failed"
    ABORTED = "aborted"

@dataclass
class ChaosExperiment:
    name: str
    description: str
    hypothesis: str
    fault_type: str
    parameters: Dict[str, Any]
    duration_seconds: int
    blast_radius: str  # 'low', 'medium', 'high'
    rollback_fn: Callable = None
    status: ExperimentStatus = ExperimentStatus.PENDING
    start_time: float = None
    end_time: float = None
    results: Dict = field(default_factory=dict)
    
    def run(self, monitor_fn: Callable, inject_fn: Callable):
        """Execute the chaos experiment."""
        self.status = ExperimentStatus.RUNNING
        self.start_time = time.time()
        
        try:
            # Capture baseline
            baseline = monitor_fn()
            self.results['baseline'] = baseline
            
            # Inject fault
            inject_fn(self.parameters)
            
            # Monitor during experiment
            during = []
            while time.time() - self.start_time < self.duration_seconds:
                metrics = monitor_fn()
                during.append(metrics)
                time.sleep(10)
            
            self.results['during'] = during
            
            # Rollback
            if self.rollback_fn:
                self.rollback_fn()
            
            # Capture recovery
            time.sleep(60)  # Allow recovery
            recovery = monitor_fn()
            self.results['recovery'] = recovery
            
            # Evaluate results
            self.results['evaluation'] = self.evaluate()
            self.status = ExperimentStatus.COMPLETED
            
        except Exception as e:
            self.status = ExperimentStatus.FAILED
            self.results['error'] = str(e)
            
            # Ensure rollback
            if self.rollback_fn:
                self.rollback_fn()
        
        self.end_time = time.time()
        return self.results
    
    def evaluate(self) -> Dict:
        """Evaluate experiment results against hypothesis."""
        baseline = self.results.get('baseline', {})
        recovery = self.results.get('recovery', {})
        
        # Check recovery metrics
        recovered = True
        issues = []
        
        for metric in ['error_rate', 'latency_p99', 'throughput']:
            base_val = baseline.get(metric, 0)
            rec_val = recovery.get(metric, 0)
            
            if base_val > 0:
                deviation = abs(rec_val - base_val) / base_val
                if deviation > 0.1:  # 10% tolerance
                    recovered = False
                    issues.append({
                        'metric': metric,
                        'baseline': base_val,
                        'recovery': rec_val,
                        'deviation': deviation
                    })
        
        return {
            'hypothesis_valid': recovered,
            'recovered': recovered,
            'issues': issues,
            'duration': self.end_time - self.start_time
        }
```

### Predefined Experiments

```python
class ExperimentLibrary:
    @staticmethod
    def network_latency(region='us-east-1', delay_ms=200, duration=300):
        return ChaosExperiment(
            name=f"Network Latency - {region}",
            description=f"Inject {delay_ms}ms latency in {region}",
            hypothesis="System should maintain performance within 10% degradation",
            fault_type='network_latency',
            parameters={
                'region': region,
                'delay_ms': delay_ms,
                'jitter_ms': 50
            },
            duration_seconds=duration,
            blast_radius='medium'
        )
    
    @staticmethod
    def database_failover(duration=300):
        return ChaosExperiment(
            name="Database Failover",
            description="Simulate primary database failure",
            hypothesis="Application should failover to replica within 30 seconds",
            fault_type='service_kill',
            parameters={
                'target': 'primary-db',
                'container': 'postgres-primary'
            },
            duration_seconds=duration,
            blast_radius='high'
        )
    
    @staticmethod
    def cache_eviction(duration=180):
        return ChaosExperiment(
            name="Cache Eviction",
            description="Clear Redis cache to test cold start",
            hypothesis="Application should recover within 60 seconds",
            fault_type='cache_clear',
            parameters={
                'cache_type': 'redis',
                'pattern': '*'
            },
            duration_seconds=duration,
            blast_radius='medium'
        )
    
    @staticmethod
    def disk_full(duration=120):
        return ChaosExperiment(
            name="Disk Space Exhaustion",
            description="Fill disk to 95% capacity",
            hypothesis="Application should handle disk pressure gracefully",
            fault_type='resource',
            parameters={
                'resource': 'disk',
                'target_percent': 95
            },
            duration_seconds=duration,
            blast_radius='high'
        )
```

---

## Monitoring During Chaos

### Metrics to Track

```python
CHAOS_METRICS = {
    'availability': {
        'error_rate': 'synthetic_check_success_rate',
        'uptime': 'up_metric'
    },
    'performance': {
        'latency_p50': 'http_request_duration_seconds{quantile="0.5"}',
        'latency_p95': 'http_request_duration_seconds{quantile="0.95"}',
        'latency_p99': 'http_request_duration_seconds{quantile="0.99"}',
        'throughput': 'rate(http_requests_total[5m])'
    },
    'resources': {
        'cpu_usage': 'process_cpu_seconds_total',
        'memory_usage': 'process_resident_memory_bytes',
        'disk_usage': 'node_filesystem_avail_bytes'
    },
    'dependencies': {
        'database_latency': 'db_query_duration_seconds',
        'cache_hit_rate': 'redis_cache_hit_rate',
        'queue_depth': 'queue_messages_total'
    }
}
```

### Evaluation Dashboard

```json
{
  "dashboard": {
    "title": "Chaos Engineering Dashboard",
    "panels": [
      {
        "title": "Experiment Status",
        "type": "stat",
        "targets": [{
          "expr": "chaex_experiment_status",
          "legendFormat": "{{ experiment }}"
        }]
      },
      {
        "title": "System Health During Experiment",
        "type": "timeseries",
        "targets": [
          {
            "expr": "rate(http_requests_total{status=~'5..'}[1m])",
            "legendFormat": "Error Rate"
          },
          {
            "expr": "http_request_duration_seconds{quantile='0.99'}",
            "legendFormat": "P99 Latency"
          }
        ]
      },
      {
        "title": "Recovery Time",
        "type": "histogram",
        "targets": [{
          "expr": "chaex_recovery_duration_seconds",
          "legendFormat": "{{ experiment }}"
        }]
      }
    ]
  }
}
```

---

## Integration with Synthetic Monitoring

### Combined Monitoring

```python
class ChaosAwareMonitor:
    def __init__(self):
        self.synthetic_monitors = []
        self.chaos_experiments = []
        self.is_chaos_running = False
    
    def register_monitor(self, monitor):
        self.synthetic_monitors.append(monitor)
    
    def run_experiment(self, experiment):
        """Run chaos experiment while monitoring."""
        self.is_chaos_running = True
        
        # Capture baseline
        baseline = self.collect_all_metrics()
        
        # Run experiment
        results = experiment.run(
            monitor_fn=self.collect_all_metrics,
            inject_fn=self.inject_fault
        )
        
        self.is_chaos_running = False
        
        # Compare with baseline
        comparison = self.compare_results(baseline, results['recovery'])
        
        return {
            'experiment': experiment.name,
            'results': results,
            'comparison': comparison,
            'passed': results['evaluation']['recovered']
        }
    
    def collect_all_metrics(self):
        """Collect metrics from all monitors."""
        metrics = {}
        for monitor in self.synthetic_monitors:
            result = monitor.execute()
            metrics[monitor.name] = {
                'success': result.success,
                'duration_ms': result.duration_ms
            }
        return metrics
```

---

## Best Practices

1. **Start Small:** Begin with low-blast-radius experiments
2. **Hypothesis First:** Define expected outcomes before experiments
3. **Monitor Everything:** Track metrics during and after experiments
4. **Automate Rollback:** Ensure experiments can be safely reverted
5. **Learn from Failures:** Document and share lessons learned
6. **Progressive Difficulty:** Gradually increase experiment complexity
7. **Game Days:** Practice chaos experiments as team exercises
8. **Document Results:** Maintain experiment history for trend analysis

---

## Next Steps

- [Alerting Thresholds](../06-alerting-thresholds/README.md) - Setting up alerts
- [Dashboard Integration](../07-dashboard-integration/README.md) - Creating dashboards
- [Examples](../examples/README.md) - Complete examples
