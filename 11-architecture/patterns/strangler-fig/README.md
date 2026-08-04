# Strangler Fig Pattern

## Overview

The Strangler Fig pattern is an incremental migration strategy where you gradually replace parts of a legacy system with a new implementation. Named after the strangler fig tree that grows around an existing tree and eventually replaces it.

## Table of Contents

- [Core Concepts](#core-concepts)
- [Migration Strategy](#migration-strategy)
- [Implementation](#implementation)
- [Traffic Routing](#traffic-routing)
- [Benefits](#benefits)
- [Best Practices](#best-practices)

## Core Concepts

```
+--------------------------------------------------+
|            STRANGLER FIG PATTERN                  |
+--------------------------------------------------+
|                                                  |
|  Legacy System      New System                   |
|  +----------+       +----------+                |
|  | Module A |       | Module A |  <-- Migrated  |
|  | Module B |       +----------+                |
|  | Module C |  ----> Module B |  <-- Migrating  |
|  +----------+       | Module C |  <-- Pending   |
|                     +----------+                |
+--------------------------------------------------+
```

### Key Principles

1. **Incremental**: Migrate one piece at a time
2. **Parallel Running**: Both systems run simultaneously
3. **Traffic Shifting**: Gradually move traffic to new system
4. **Safe Rollback**: Can revert to legacy if issues arise

## Migration Strategy

### Phase 1: Identify Boundaries

```python
legacy_modules = {
    'user_management': {'complexity': 'low', 'dependencies': []},
    'order_processing': {'complexity': 'high', 'dependencies': ['user_management']},
    'payment': {'complexity': 'medium', 'dependencies': ['order_processing']},
    'inventory': {'complexity': 'medium', 'dependencies': ['order_processing']},
    'reporting': {'complexity': 'low', 'dependencies': ['order_processing', 'payment']}
}

migration_order = ['user_management', 'payment', 'inventory', 'order_processing', 'reporting']
```

### Phase 2: Build Facade

```python
class SystemFacade:
    def __init__(self, legacy_system, new_system, routing_rules):
        self.legacy = legacy_system
        self.new = new_system
        self.rules = routing_rules

    def handle_request(self, request):
        module = self._identify_module(request)
        if self.rules.should_use_new_system(module, request):
            return self.new.handle(request)
        else:
            return self.legacy.handle(request)
```

### Phase 3: Migrate Incrementally

```python
class MigrationManager:
    def __init__(self):
        self.migration_status = {}
        self.traffic_percentages = {}

    def start_migration(self, module, percentage=0):
        self.migration_status[module] = 'in_progress'
        self.traffic_percentages[module] = percentage

    def increase_traffic(self, module, increment=10):
        current = self.traffic_percentages.get(module, 0)
        self.traffic_percentages[module] = min(100, current + increment)

    def complete_migration(self, module):
        self.migration_status[module] = 'completed'
        self.traffic_percentages[module] = 100
```

## Implementation

### Traffic Router

```python
import random

class TrafficRouter:
    def __init__(self):
        self.routes = {}
        self.health_checker = HealthChecker()

    def register_route(self, module, legacy_handler, new_handler):
        self.routes[module] = {
            'legacy': legacy_handler,
            'new': new_handler,
            'percentage': 0
        }

    def set_traffic_percentage(self, module, percentage):
        if module in self.routes:
            self.routes[module]['percentage'] = percentage

    def route(self, module, request):
        route = self.routes.get(module)
        if not route:
            raise ValueError(f'No route for module: {module}')

        if not self.health_checker.is_healthy(module):
            return route['legacy'](request)

        if random.randint(1, 100) <= route['percentage']:
            try:
                return route['new'](request)
            except Exception:
                return route['legacy'](request)
        else:
            return route['legacy'](request)

class HealthChecker:
    def __init__(self):
        self.health_status = {}

    def is_healthy(self, module):
        return self.health_status.get(module, True)
```

### Gradual Traffic Shifting

```python
class GradualMigration:
    STAGES = [
        {'percentage': 5, 'duration_days': 7, 'name': 'canary'},
        {'percentage': 25, 'duration_days': 7, 'name': 'early_adopters'},
        {'percentage': 50, 'duration_days': 7, 'name': 'half'},
        {'percentage': 75, 'duration_days': 7, 'name': 'majority'},
        {'percentage': 100, 'duration_days': 0, 'name': 'complete'}
    ]

    def __init__(self, module, router):
        self.module = module
        self.router = router
        self.current_stage = 0

    def advance_stage(self):
        if self.current_stage < len(self.STAGES):
            stage = self.STAGES[self.current_stage]
            self.router.set_traffic_percentage(self.module, stage['percentage'])
            self.current_stage += 1
            return stage
        return None
```

### Monitoring During Migration

```python
class MigrationMonitor:
    def compare_systems(self, module, time_range):
        legacy_metrics = self._get_legacy_metrics(module, time_range)
        new_metrics = self._get_new_metrics(module, time_range)
        return {
            'module': module,
            'legacy': legacy_metrics,
            'new': new_metrics,
            'comparison': {
                'latency_diff': new_metrics['p95'] - legacy_metrics['p95'],
                'error_diff': new_metrics['error_rate'] - legacy_metrics['error_rate']
            }
        }

    def should_rollback(self, module, threshold=0.05):
        metrics = self._get_new_metrics(module, '1h')
        return metrics['error_rate'] > threshold
```

## Benefits

1. **Zero Downtime**: Legacy system continues during migration
2. **Reduced Risk**: Incremental changes are easier to debug
3. **Rollback Capability**: Can revert traffic at any time
4. **Parallel Development**: Teams work independently
5. **Testing in Production**: Gradual rollout allows real-world testing
6. **Cost Control**: Migrate at your own pace

## Best Practices

### 1. Start with Simple Modules

```python
priority_modules = [
    ('user_management', 'Low risk, few dependencies'),
    ('notifications', 'Independent, easy to isolate'),
    ('reporting', 'Read-only, no side effects'),
]
```

### 2. Implement Comprehensive Monitoring

```python
class MigrationDashboard:
    def get_status(self, module):
        return {
            'traffic_percentage': self.router.get_percentage(module),
            'error_rate_new': self.metrics.get_error_rate(module, 'new'),
            'error_rate_legacy': self.metrics.get_error_rate(module, 'legacy'),
        }
```

### 3. Automate Rollback

```python
class AutoRollback:
    def check_and_rollback(self, module):
        status = self.monitor.get_health_status(module)
        if status['error_rate'] > 0.05:
            self.router.set_traffic_percentage(module, 0)
            alert(f'Auto-rolled back {module}')
            return True
        return False
```

### 4. Clean Up Legacy Code

```python
class LegacyCleanup:
    def cleanup_after_migration(self, module):
        if self.router.get_percentage(module) == 100:
            self.archive_code(module)
            self.remove_legacy_infrastructure(module)
```

### 5. Data Synchronization

```python
class DataSync:
    def dual_write(self, data):
        legacy_result = self.legacy.save(data)
        new_result = self.new.save(data)
        return legacy_result

    def smart_read(self, id, module):
        if self.router.get_percentage(module) > 50:
            return self.new.find(id)
        return self.legacy.find(id)
```

## Further Reading

- [Strangler Fig Application - Martin Fowler](https://martinfowler.com/bliki/StranglerFigApplication.html)
- [Building Microservices - Sam Newman](https://samnewman.io/books/building_microservices_2nd_edition/)
