# Salt Fundamentals

## Overview

Salt (SaltStack) is a Python-based configuration management and remote execution tool.

## States

### Basic State
```yaml
# states/nginx/init.sls
nginx:
  pkg.installed:
    - name: nginx

nginx_service:
  service.running:
    - name: nginx
    - enable: True
    - require:
      - pkg: nginx

nginx_config:
  file.managed:
    - name: /etc/nginx/nginx.conf
    - source: salt://nginx/nginx.conf
    - user: root
    - group: root
    - mode: 644
    - watch_in:
      - service: nginx
```

## Pillars

```yaml
# pillars/nginx.sls
nginx:
  port: 80
  worker_processes: auto
  worker_connections: 1024
```

## Grains

```bash
# List grains
salt '*' grains.ls

# Get specific grain
salt '*' grains.item os
```

## Commands

```bash
# Apply state
salt '*' state.apply nginx

# Test state
salt '*' state.apply nginx test=True

# Run command
salt '*' cmd.run 'uptime'

# Target minions
salt -G 'os:Ubuntu' state.apply nginx
```

## Best Practices

1. **Use states** - Define system state declaratively
2. **Use pillars** - Separate data from code
3. **Use grains** - Target minions based on system properties
4. **Implement idempotency** - Ensure states can run multiple times
5. **Use templates** - Template configuration files
6. **Test states** - Use test mode
7. **Document states** - Add comments and README
8. **Use Salt formulas** - Reuse state components
9. **Implement code review** - Review states before deployment
10. **Use version control** - Store states in Git
