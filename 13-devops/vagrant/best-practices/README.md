# Vagrant Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Vagrant environments.

## Project Structure

```
project/
├── Vagrantfile
├── scripts/
│   ├── install.sh
│   └── configure.sh
├── ansible/
│   ├── playbook.yml
│   └── inventory/
├── data/
│   └── shared/
└── README.md
```

## Vagrantfile Best Practices

```ruby
# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  # Use consistent box
  config.vm.box = "ubuntu/jammy64"
  
  # Set hostname
  config.vm.hostname = "my-vm"
  
  # Configure networking
  config.vm.network "forwarded_port", guest: 80, host: 8080
  config.vm.network "private_network", ip: "192.168.33.10"
  
  # Configure provider
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
    vb.cpus = 2
  end
  
  # Use provisioners
  config.vm.provision "shell", path: "scripts/install.sh"
end
```

## Provisioning Best Practices

### Use Shell Scripts
```bash
#!/bin/bash
# scripts/install.sh

apt-get update
apt-get install -y nginx
systemctl enable nginx
```

### Use Ansible
```yaml
# ansible/playbook.yml
---
- name: Configure web server
  hosts: all
  become: yes
  
  tasks:
    - name: Install nginx
      apt:
        name: nginx
        state: present
    
    - name: Start nginx
      service:
        name: nginx
        state: started
        enabled: yes
```

## Best Practices Summary

1. **Use version control** - Store Vagrantfiles in Git
2. **Use boxes wisely** - Choose appropriate base boxes
3. **Implement provisioning** - Use provisioners for setup
4. **Use synced folders** - Share files between host and VM
5. **Use networking** - Configure networking properly
6. **Use multiple VMs** - Test multi-machine environments
7. **Document Vagrantfiles** - Add comments and README
8. **Use Vagrant Cloud** - Share and reuse boxes
9. **Implement CI/CD** - Automate VM creation
10. **Use providers** - Choose appropriate provider
