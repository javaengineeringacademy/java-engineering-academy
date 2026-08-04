# Ansible Roles

## Overview

Ansible roles provide a structured way to organize playbooks, making them reusable and maintainable.

## Role Structure

```
roles/
└── nginx/
    ├── tasks/
    │   ├── main.yml
    │   ├── install.yml
    │   └── configure.yml
    ├── handlers/
    │   └── main.yml
    ├── templates/
    │   └── nginx.conf.j2
    ├── files/
    │   └── index.html
    ├── vars/
    │   └── main.yml
    ├── defaults/
    │   └── main.yml
    ├── meta/
    │   └── main.yml
    └── README.md
```

## Creating Roles

### Tasks
```yaml
# roles/nginx/tasks/main.yml
---
- name: Include install tasks
  include_tasks: install.yml

- name: Include configure tasks
  include_tasks: configure.yml
```

```yaml
# roles/nginx/tasks/install.yml
---
- name: Install nginx
  apt:
    name: nginx
    state: present
    update_cache: yes
```

### Handlers
```yaml
# roles/nginx/handlers/main.yml
---
- name: Restart nginx
  service:
    name: nginx
    state: restarted

- name: Reload nginx
  service:
    name: nginx
    state: reloaded
```

### Defaults
```yaml
# roles/nginx/defaults/main.yml
---
nginx_port: 80
nginx_worker_processes: auto
nginx_worker_connections: 1024
```

### Templates
```jinja2
# roles/nginx/templates/nginx.conf.j2
worker_processes {{ nginx_worker_processes }};

events {
    worker_connections {{ nginx_worker_connections }};
}

http {
    server {
        listen {{ nginx_port }};
        server_name localhost;
        
        location / {
            root /var/www/html;
            index index.html;
        }
    }
}
```

## Using Roles

```yaml
# playbook.yml
---
- name: Configure web servers
  hosts: webservers
  become: yes
  
  roles:
    - common
    - role: nginx
      vars:
        nginx_port: 8080
    - role: redis
      when: enable_redis | default(false)
```

## Galaxy

```bash
# Install role
ansible-galaxy install geerlingguy.nginx

# Install from requirements
ansible-galaxy install -r requirements.yml

# List installed roles
ansible-galaxy list
```

### Requirements File
```yaml
# requirements.yml
---
roles:
  - name: geerlingguy.nginx
    version: "3.1.0"
  - name: geerlingguy.postgresql
    version: "3.0.0"
```

## Best Practices

1. **Keep roles focused** - Single responsibility principle
2. **Use defaults** - Provide sensible defaults
3. **Document roles** - Add README and variable descriptions
4. **Use tags** - Allow selective execution
5. **Implement idempotency** - Ensure roles can run multiple times
6. **Use ansible-lint** - Lint roles for best practices
7. **Test roles** - Use molecule for testing
8. **Use Galaxy** - Share and reuse roles
9. **Version roles** - Use semantic versioning
10. **Implement dependencies** - Use meta for role dependencies
