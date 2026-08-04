# Blue-Green Deployment

## Overview

Maintain two identical environments, switch traffic between them.

## Process

1. Deploy to inactive environment
2. Test in inactive
3. Switch load balancer
4. Keep old environment for rollback

## Implementation

```python
class BlueGreenDeployer:
    def __init__(self, load_balancer):
        self.lb = load_balancer
        self.active = 'blue'
    
    def deploy(self, new_version):
        inactive = 'green' if self.active == 'blue' else 'blue'
        self.deploy_to(inactive, new_version)
        self.test(inactive)
        self.lb.switch_target(inactive)
        self.active = inactive
```

## Benefits

- Zero downtime
- Instant rollback
- Full testing before switch
