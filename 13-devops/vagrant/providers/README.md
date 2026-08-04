# Vagrant Providers

## Overview

Vagrant providers are the backend platforms that run Vagrant environments, such as VirtualBox, VMware, and Hyper-V.

## VirtualBox

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  
  config.vm.provider "virtualbox" do |vb|
    vb.name = "my-vm"
    vb.memory = "2048"
    vb.cpus = 2
    vb.gui = false
    
    vb.customize ["modifyvm", :id, "--natdnshostresolver1", "on"]
    vb.customize ["modifyvm", :id, "--natdnsproxy1", "on"]
  end
end
```

## VMware

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  
  config.vm.provider "vmware_desktop" do |vmware|
    vmware.vmx["memsize"] = "2048"
    vmware.vmx["numvcpus"] = "2"
    vmware.vmx["displayname"] = "my-vm"
  end
end
```

## Hyper-V

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/jammy64"
  
  config.vm.provider "hyperv" do |hyperv|
    hyperv.memory = "2048"
    hyperv.cpus = 2
    hyperv.vmname = "my-vm"
    hyperv.switchname = "Default Switch"
  end
end
```

## Docker

```ruby
Vagrant.configure("2") do |config|
  config.vm.provider "docker" do |docker|
    docker.image = "ubuntu:22.04"
    docker.remains_running = true
    docker.has_ssh = true
    
    docker.ports = [
      { guest: 80, host: 8080, protocol: "tcp" }
    ]
    
    docker.volumes = [
      { host_path: "./data", guest_path: "/data" }
    ]
  end
end
```

## Best Practices

1. **Choose appropriate provider** - Match provider to use case
2. **Configure resources** - Set memory and CPUs appropriately
3. **Use networking** - Configure networking properly
4. **Use synced folders** - Share files between host and VM
5. **Document providers** - Add comments and README
6. **Test providers** - Verify provider compatibility
7. **Use provider-specific features** - Leverage provider capabilities
8. **Implement CI/CD** - Automate VM creation
9. **Monitor resources** - Track VM resource usage
10. **Use multiple providers** - Test across providers
