# Salt Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Salt states and formulas.

## Code Organization

```
salt/
├── states/
│   ├── nginx/
│   ├── php/
│   └── mysql/
├── pillars/
│   ├── nginx.sls
│   └── common.sls
├── minions/
│   ├── webserver/
│   └── database/
└── top.sls
```

## State Best Practices

### Use States
```yaml
# Good
nginx:
  pkg.installed

# Bad
execute 'install nginx':
  cmd.run:
    - name: apt-get install -y nginx
```

### Use Pillars
```yaml
# Good
nginx_port: {{ pillar['nginx']['port'] }}

# Bad
nginx_port: 80
```

### Use Grains
```yaml
# Good
{% if grains['os'] == 'Ubuntu' %}
nginx_package: nginx
{% elif grains['os'] == 'CentOS' %}
nginx_package: nginx
{% endif %}
```

## Testing

```bash
# Test state
salt '*' state.apply nginx test=True

# Apply state
salt '*' state.apply nginx

# Check state
salt '*' state.show_sls nginx
```

## Best Practices Summary

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
