# Salt Formulas

## Overview

Salt formulas are reusable packages of Salt states that can be shared and reused across multiple environments.

## Formula Structure

```
nginx/
├── init.sls
├── install.sls
├── config.sls
├── service.sls
├── pillar.example
└── README.md
```

## Creating Formulas

### Init SLS
```yaml
# nginx/init.sls
include:
  - nginx.install
  - nginx.config
  - nginx.service
```

### Install SLS
```yaml
# nginx/install.sls
nginx:
  pkg.installed:
    - name: nginx
```

### Config SLS
```yaml
# nginx/config.sls
/etc/nginx/nginx.conf:
  file.managed:
    - source: salt://nginx/nginx.conf
    - user: root
    - group: root
    - mode: 644
    - template: jinja
```

### Service SLS
```yaml
# nginx/service.sls
nginx_service:
  service.running:
    - name: nginx
    - enable: True
    - require:
      - pkg: nginx
```

## Using Formulas

```yaml
# states/webserver/init.sls
include:
  - nginx
  - php
  - mysql
```

## Best Practices

1. **Keep formulas small** - Single responsibility principle
2. **Use parameters** - Parameterize formulas
3. **Document formulas** - Add README and pillar examples
4. **Test formulas** - Use salt-minion test mode
5. **Use Salt Forge** - Share and reuse formulas
6. **Version formulas** - Use semantic versioning
7. **Implement dependencies** - Use include for dependencies
8. **Use templates** - Template configuration files
9. **Implement idempotency** - Ensure formulas can run multiple times
10. **Use Salt Lint** - Lint formulas for best practices
