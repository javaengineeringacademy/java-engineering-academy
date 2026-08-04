# Vagrant Fundamentals

## Overview

Vagrant is a tool for building and managing virtual machine environments using a simple and consistent workflow.

## Basic Vagrantfile

```ruby
# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  
  config.vm.hostname = "my-vm"
  
  config.vm.network "forwarded_port", guest: 80, host: 8080
  config.vm.network "private_network", ip: "192.168.33.10"
  
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
    vb.cpus = 2
  end
  
  config.vm.provision "shell", inline: <<-SHELL
    apt-get update
    apt-get install -y nginx
  SHELL
end
```

## Boxes

```bash
# Add box
vagrant box add ubuntu/jammy64

# List boxes
vagrant box list

# Remove box
vagrant box remove ubuntu/jammy64

# Update box
vagrant box update
```

## Vagrant Commands

```bash
# Start VM
vagrant up

# SSH into VM
vagrant ssh

# Halt VM
vagrant halt

# Destroy VM
vagrant destroy

# Reload VM
vagrant reload

# Provision VM
vagrant provision

# Status
vagrant status
```

## Synced Folders

```ruby
Vagrant.configure("2") do |config|
  # Default synced folder
  config.vm.synced_folder "./data", "/vagrant/data"
  
  # NFS synced folder
  config.vm.synced_folder "./src", "/src", type: "nfs"
  
  # Disable default synced folder
  config.vm.synced_folder ".", "/vagrant", disabled: true
end
```

## Best Practices

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
