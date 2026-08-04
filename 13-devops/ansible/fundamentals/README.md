# Ansible Fundamentals

## Overview

Ansible is an open-source automation tool for configuration management, application deployment, and task automation.

## Inventory

### Static Inventory
```ini
# inventory/hosts
[webservers]
web1.example.com
web2.example.com

[dbservers]
db1.example.com
db2.example.com

[all:vars]
ansible_user=deploy
ansible_ssh_private_key_file=~/.ssh/id_rsa
```

### Dynamic Inventory
```python
#!/usr/bin/env python3
import json
import argparse

def get_inventory():
    inventory = {
        "webservers": {
            "hosts": ["web1.example.com", "web2.example.com"],
            "vars": {
                "ansible_user": "deploy"
            }
        },
        "dbservers": {
            "hosts": ["db1.example.com", "db2.example.com"],
            "vars": {
                "ansible_user": "deploy"
            }
        }
    }
    return inventory

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--list", action="store_true")
    args = parser.parse_args()
    
    if args.list:
        print(json.dumps(get_inventory(), indent=2))
```

## Playbooks

### Basic Playbook
```yaml
# playbook.yml
---
- name: Configure web servers
  hosts: webservers
  become: yes
  
  vars:
    http_port: 80
    max_clients: 200
  
  tasks:
    - name: Install nginx
      apt:
        name: nginx
        state: present
        update_cache: yes
    
    - name: Start nginx
      service:
        name: nginx
        state: started
        enabled: yes
    
    - name: Copy nginx config
      template:
        src: templates/nginx.conf.j2
        dest: /etc/nginx/nginx.conf
      notify: Restart nginx
  
  handlers:
    - name: Restart nginx
      service:
        name: nginx
        state: restarted
```

### Multi-Play Playbook
```yaml
---
- name: Configure web servers
  hosts: webservers
  become: yes
  roles:
    - common
    - nginx

- name: Configure database servers
  hosts: dbservers
  become: yes
  roles:
    - common
    - postgresql
```

## Ansible Commands

```bash
# Run playbook
ansible-playbook -i inventory/hosts playbook.yml

# Run specific task
ansible-playbook playbook.yml --tags "install"

# Check mode
ansible-playbook playbook.yml --check

# Dry run
ansible-playbook playbook.yml --diff

# Limit to specific hosts
ansible-playbook playbook.yml --limit web1.example.com

# Run ad-hoc command
ansible webservers -m ping
ansible webservers -m shell -a "uptime"
```

## Best Practices

1. **Use roles** - Organize playbooks into roles
2. **Use variables** - Parameterize configurations
3. **Implement handlers** - Use handlers for service restarts
4. **Use templates** - Jinja2 templates for configuration files
5. **Implement tags** - Use tags for selective execution
6. **Use vault** - Encrypt sensitive data
7. **Implement idempotency** - Ensure playbooks can run multiple times
8. **Use ansible-lint** - Lint playbooks for best practices
9. **Document playbooks** - Add comments and README
10. **Test playbooks** - Use molecule for testing
