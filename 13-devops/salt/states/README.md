# Salt States

## Overview

Salt states are SLS (Salt State) files that define the desired state of system resources.

## SLS Files

### Basic SLS
```yaml
# states/nginx/init.sls
nginx:
  pkg.installed

/etc/nginx/nginx.conf:
  file.managed:
    - source: salt://nginx/nginx.conf
    - user: root
    - group: root
    - mode: 644

nginx_service:
  service.running:
    - name: nginx
    - enable: True
```

### Include States
```yaml
# states/webserver/init.sls
include:
  - nginx
  - php
  - mysql
```

### Template States
```yaml
# states/nginx/init.sls
nginx_config:
  file.managed:
    - name: /etc/nginx/nginx.conf
    - source: salt://nginx/nginx.conf.jinja
    - template: jinja
    - context:
        port: {{ pillar['nginx']['port'] }}
        workers: {{ pillar['nginx']['workers'] }}
```

## State Functions

```yaml
# Package management
nginx:
  pkg.installed:
    - name: nginx

# Service management
nginx_service:
  service.running:
    - name: nginx
    - enable: True

# File management
/etc/nginx/nginx.conf:
  file.managed:
    - source: salt://nginx/nginx.conf
    - user: root
    - group: root
    - mode: 644

# User management
deploy_user:
  user.present:
    - name: deploy
    - groups:
      - sudo
    - shell: /bin/bash
```

## Best Practices

1. **Use SLS files** - Organize states into SLS files
2. **Use includes** - Include related states
3. **Use templates** - Template configuration files
4. **Use pillars** - Separate data from code
5. **Implement idempotency** - Ensure states can run multiple times
6. **Test states** - Use test mode
7. **Document states** - Add comments and README
8. **Use Salt formulas** - Reuse state components
9. **Implement code review** - Review states before deployment
10. **Use version control** - Store states in Git
