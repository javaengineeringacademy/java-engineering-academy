# Packer Provisioners

## Overview

Packer provisioners are components that install and configure software on machine images during the build process.

## Shell Provisioner

```hcl
provisioner "shell" {
  inline = [
    "sudo apt-get update",
    "sudo apt-get install -y nginx",
    "sudo systemctl enable nginx"
  ]
  
  scripts = [
    "scripts/install.sh",
    "scripts/configure.sh"
  ]
  
  environment_vars = [
    "APP_ENV=production",
    "APP_VERSION=1.0.0"
  ]
}
```

## Ansible Provisioner

```hcl
provisioner "ansible" {
  playbook_file = "ansible/playbook.yml"
  
  extra_arguments = [
    "--extra-vars", "app_version=1.0.0"
  ]
  
  ansible_env_vars = [
    "ANSIBLE_HOST_KEY_CHECKING=False"
  ]
}
```

## Chef Provisioner

```hcl
provisioner "chef" {
  chef_license      = "accept"
  server_url        = "https://chef.example.com"
  validation_client_name = "myorg-validator"
  validation_key    = "chef-validator.pem"
  
  run_list = [
    "recipe[myapp::default]",
    "role[webserver]"
  ]
  
  attributes = {
    "myapp" => {
      "version" => "1.0.0"
    }
  }
}
```

## Puppet Provisioner

```hcl
provisioner "puppet" {
  puppet_server = "puppet.example.com"
  
  module_paths = [
    "/etc/puppet/modules",
    "/opt/puppetlabs/puppet/modules"
  ]
  
  manifest_file = "site.pp"
  
  extra_arguments = [
    "--environment", "production"
  ]
}
```

## File Provisioner

```hcl
provisioner "file" {
  source      = "files/app.conf"
  destination = "/tmp/app.conf"
}

provisioner "file" {
  source      = "files/"
  destination = "/tmp/files/"
}

provisioner "file" {
  content     = "Hello World"
  destination = "/tmp/hello.txt"
}
```

## Best Practices

1. **Use shell provisioners** - For simple tasks
2. **Use Ansible provisioners** - For complex configurations
3. **Use file provisioners** - For file transfers
4. **Use variables** - Parameterize provisioners
5. **Implement debugging** - Use debug mode
6. **Test provisioners** - Verify provisioners work correctly
7. **Document provisioners** - Add comments and README
8. **Use version control** - Store provisioners in Git
9. **Implement CI/CD** - Automate provisioner execution
10. **Use provisioner ordering** - Order provisioners correctly
