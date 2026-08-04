# Vagrant Provisioning

## Overview

Vagrant provisioners are responsible for installing and configuring software on Vagrant machines.

## Shell Provisioner

```ruby
Vagrant.configure("2") do |config|
  config.vm.provision "shell", inline: <<-SHELL
    apt-get update
    apt-get install -y nginx
  SHELL
  
  config.vm.provision "shell", path: "scripts/install.sh"
  
  config.vm.provision "shell", run: "always", inline: <<-SHELL
    systemctl restart nginx
  SHELL
end
```

## Ansible Provisioner

```ruby
Vagrant.configure("2") do |config|
  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "ansible/playbook.yml"
    ansible.extra_vars = {
      app_version: "1.0.0"
    }
    ansible.groups = {
      "webservers" => ["default"]
    }
  end
end
```

## Chef Provisioner

```ruby
Vagrant.configure("2") do |config|
  config.vm.provision "chef_client" do |chef|
    chef.chef_server_url = "https://chef.example.com"
    chef.validation_key_path = "chef-validator.pem"
    chef.run_list = [
      "recipe[myapp::default]",
      "role[webserver]"
    ]
  end
end
```

## Docker Provisioner

```ruby
Vagrant.configure("2") do |config|
  config.vm.provision "docker" do |docker|
    docker.images = [
      "ubuntu:22.04",
      "nginx:latest"
    ]
    
    docker.run "web" do |config|
      config.image = "nginx:latest"
      config.ports = ["80:80"]
    end
  end
end
```

## Provisioner Triggers

```ruby
Vagrant.configure("2") do |config|
  config.trigger.before :up do |trigger|
    trigger.info = "Running before up trigger"
    trigger.run = { inline: "echo 'Before up'" }
  end
  
  config.trigger.after :up do |trigger|
    trigger.info = "Running after up trigger"
    trigger.run = { inline: "echo 'After up'" }
  end
end
```

## Best Practices

1. **Use appropriate provisioners** - Match provisioner to use case
2. **Use triggers** - Automate pre/post actions
3. **Use variables** - Parameterize provisioners
4. **Document provisioners** - Add comments and README
5. **Test provisioners** - Verify provisioner compatibility
6. **Use idempotent provisioners** - Ensure provisioners can run multiple times
7. **Implement debugging** - Use debug mode
8. **Use version control** - Store provisioners in Git
9. **Implement CI/CD** - Automate provisioner execution
10. **Use provisioner ordering** - Order provisioners correctly
