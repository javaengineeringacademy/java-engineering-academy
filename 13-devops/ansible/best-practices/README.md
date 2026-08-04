# Ansible Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Ansible playbooks and roles.

## Directory Structure

```
ansible/
├── ansible.cfg
├── inventory/
│   ├── production/
│   │   ├── hosts
│   │   └── group_vars/
│   │       ├── all/
│   │       └── webservers/
│   └── staging/
│       ├── hosts
│       └── group_vars/
├── playbooks/
│   ├── site.yml
│   ├── webservers.yml
│   └── dbservers.yml
├── roles/
│   ├── common/
│   ├── nginx/
│   └── postgresql/
└── files/
    └── scripts/
```

## Configuration

```ini
# ansible.cfg
[defaults]
inventory = inventory/production/hosts
remote_user = deploy
private_key_file = ~/.ssh/id_rsa
host_key_checking = False
retry_files_enabled = False
gathering = smart
fact_caching = jsonfile
fact_caching_connection = /tmp/ansible_facts_cache
fact_caching_timeout = 3600

[privilege_escalation]
become = True
become_method = sudo
become_user = root
become_ask_pass = False

[ssh_connection]
ssh_args = -o ControlMaster=auto -o ControlPersist=60s
pipelining = True
```

## Playbook Best Practices

### Use Roles
```yaml
---
- name: Configure web servers
  hosts: webservers
  become: yes
  roles:
    - common
    - nginx
    - monitoring
```

### Use Tags
```yaml
---
- name: Install and configure nginx
  hosts: webservers
  become: yes
  
  tasks:
    - name: Install nginx
      apt:
        name: nginx
        state: present
      tags: [install, nginx]
    
    - name: Configure nginx
      template:
        src: nginx.conf.j2
        dest: /etc/nginx/nginx.conf
      tags: [configure, nginx]
      notify: Restart nginx
```

### Use Handlers
```yaml
---
- name: Restart nginx
  service:
    name: nginx
    state: restarted
  when: nginx_config_changed | default(false)
```

## Testing

### Molecule
```yaml
# molecule/default/molecule.yml
dependency:
  name: galaxy
driver:
  name: docker
platforms:
  - name: instance
    image: docker.io/pycontribs/ubuntu:22.04
provisioner:
  name: ansible
verifier:
  name: ansible
```

```bash
# Run molecule tests
molecule test

# Run specific scenario
molecule test -s default
```

## Best Practices Summary

1. **Use roles** - Organize playbooks into roles
2. **Use variables** - Parameterize configurations
3. **Implement idempotency** - Ensure playbooks can run multiple times
4. **Use tags** - Allow selective execution
5. **Implement handlers** - Use handlers for service restarts
6. **Use vault** - Encrypt sensitive data
7. **Test playbooks** - Use molecule for testing
8. **Document playbooks** - Add comments and README
9. **Use ansible-lint** - Lint playbooks for best practices
10. **Implement CI/CD** - Automate playbook execution
