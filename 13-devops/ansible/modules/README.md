# Ansible Modules

## Overview

Ansible modules are reusable units of code that perform specific tasks. They are the building blocks of Ansible playbooks.

## Common Modules

### Package Management
```yaml
- name: Install nginx
  apt:
    name: nginx
    state: present
    update_cache: yes

- name: Install python packages
  pip:
    name:
      - flask
      - gunicorn
    state: present

- name: Install Docker
  yum:
    name: docker
    state: present
```

### File Management
```yaml
- name: Create directory
  file:
    path: /opt/myapp
    state: directory
    owner: deploy
    group: deploy
    mode: '0755'

- name: Copy configuration file
  copy:
    src: files/nginx.conf
    dest: /etc/nginx/nginx.conf
    owner: root
    group: root
    mode: '0644'
  notify: Restart nginx

- name: Create symlink
  file:
    src: /opt/myapp/current
    dest: /opt/myapp/active
    state: link
```

### Service Management
```yaml
- name: Start and enable nginx
  service:
    name: nginx
    state: started
    enabled: yes

- name: Restart nginx
  service:
    name: nginx
    state: restarted
```

### User Management
```yaml
- name: Create deploy user
  user:
    name: deploy
    groups: sudo
    shell: /bin/bash
    create_home: yes
    state: present
```

### Template Module
```yaml
- name: Deploy nginx config
  template:
    src: templates/nginx.conf.j2
    dest: /etc/nginx/nginx.conf
    owner: root
    group: root
    mode: '0644'
  notify: Restart nginx
```

## Custom Modules

### Python Module
```python
#!/usr/bin/python
from ansible.module_utils.basic import AnsibleModule

def main():
    module = AnsibleModule(
        argument_spec=dict(
            name=dict(required=True, type='str'),
            state=dict(default='present', choices=['present', 'absent']),
        ),
        supports_check_mode=True
    )

    name = module.params['name']
    state = module.params['state']

    # Implementation here
    changed = False

    module.exit_json(changed=changed, name=name)

if __name__ == '__main__':
    main()
```

## Best Practices

1. **Use built-in modules** - Prefer built-in modules over shell commands
2. **Implement idempotency** - Ensure modules can run multiple times
3. **Use parameters properly** - Validate and document parameters
4. **Implement error handling** - Handle failures gracefully
5. **Use check mode** - Support check mode when possible
6. **Document modules** - Add descriptions for complex modules
7. **Test modules** - Use ansible-test for testing
8. **Use module utilities** - Leverage module_utils for common functions
9. **Implement logging** - Log module operations
10. **Use callbacks** - Implement custom callbacks for monitoring
