# Vagrant Networking

## Overview

Vagrant provides several networking options to connect VMs to the host, other VMs, and external networks.

## Port Forwarding

```ruby
Vagrant.configure("2") do |config|
  config.vm.network "forwarded_port", guest: 80, host: 8080
  config.vm.network "forwarded_port", guest: 443, host: 8443
  config.vm.network "forwarded_port", guest: 22, host: 2222, id: "ssh"
end
```

## Private Network

```ruby
Vagrant.configure("2") do |config|
  config.vm.network "private_network", ip: "192.168.33.10"
end
```

## Public Network

```ruby
Vagrant.configure("2") do |config|
  config.vm.network "public_network"
end
```

## Static IP

```ruby
Vagrant.configure("2") do |config|
  config.vm.network "private_network", ip: "192.168.33.10"
  config.vm.network "private_network", ip: "192.168.33.11", netmask: "255.255.255.0"
end
```

## UDP Forwarding

```ruby
Vagrant.configure("2") do |config|
  config.vm.network "forwarded_port", guest: 53, host: 5353, protocol: "udp"
end
```

## Best Practices

1. **Use port forwarding** - Forward necessary ports
2. **Use private networks** - Isolate VMs
3. **Use static IPs** - For consistent addressing
4. **Document networking** - Add comments and README
5. **Test networking** - Verify network connectivity
6. **Use networking triggers** - Automate network setup
7. **Implement security** - Secure network access
8. **Use multiple adapters** - Configure multiple network interfaces
9. **Monitor networking** - Track network usage
10. **Use networking provisioners** - Automate network configuration
