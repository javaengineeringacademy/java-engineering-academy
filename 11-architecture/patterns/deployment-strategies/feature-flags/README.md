# Feature Flags

## Overview

Toggle features at runtime without deployment.

## Types

- **Release flags**: Control feature rollout
- **Experiment flags**: A/B testing
- **Ops flags**: Operational toggles
- **Permission flags**: User access control

## Implementation

```python
class FeatureFlags:
    def __init__(self):
        self.flags = {}
    
    def is_enabled(self, flag_name, user_id=None):
        flag = self.flags.get(flag_name)
        if not flag:
            return False
        
        if flag['type'] == 'percentage':
            return hash(user_id) % 100 < flag['percentage']
        elif flag['type'] == 'whitelist':
            return user_id in flag['users']
        return flag['enabled']

# Usage
if feature_flags.is_enabled('new_checkout', user.id):
    return new_checkout_flow()
else:
    return legacy_checkout_flow()
```
