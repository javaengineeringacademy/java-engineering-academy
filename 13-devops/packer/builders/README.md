# Packer Builders

## Overview

Packer builders are components that create machine images for various platforms.

## Amazon EBS

```hcl
source "amazon-ebs" "ubuntu" {
  access_key = var.aws_access_key
  secret_key = var.aws_secret_key
  region     = "us-east-1"
  
  source_ami_filter {
    filters = {
      name                = "ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"
      root-device-type    = "ebs"
      virtualization-type = "hvm"
    }
    most_recent = true
    owners      = ["099720109477"]
  }
  
  instance_type = "t2.micro"
  ssh_username  = "ubuntu"
  ami_name      = "my-app-{{timestamp}}"
  
  ami_regions = ["us-east-1", "us-west-2"]
  
  tags = {
    Name        = "my-app"
    Environment = "production"
  }
}
```

## Docker

```hcl
source "docker" "ubuntu" {
  image  = "ubuntu:22.04"
  commit = true
  
  changes = [
    "EXPOSE 80",
    "CMD [\"nginx\", \"-g\", \"daemon off;\"]"
  ]
}

build {
  sources = ["source.docker.ubuntu"]
  
  provisioner "shell" {
    inline = [
      "apt-get update",
      "apt-get install -y nginx"
    ]
  }
  
  post-processor "docker-tag" {
    repository = "my-nginx"
    tags       = ["1.0", "latest"]
  }
}
```

## VMware

```hcl
source "vmware-iso" "ubuntu" {
  vm_name       = "my-app"
  guest_os_type = "ubuntu-64"
  
  iso_url      = "https://releases.ubuntu.com/22.04/ubuntu-22.04-live-server-amd64.iso"
  iso_checksum = "sha256:..."
  
  cpus      = 2
  memory    = 2048
  disk_size = 40960
  
  ssh_username = "ubuntu"
  ssh_password = "ubuntu"
  
  shutdown_command = "shutdown -P now"
  
  output_directory = "output-vmware"
}
```

## VirtualBox

```hcl
source "virtualbox-iso" "ubuntu" {
  vm_name       = "my-app"
  guest_os_type = "Ubuntu_64"
  
  iso_url      = "https://releases.ubuntu.com/22.04/ubuntu-22.04-live-server-amd64.iso"
  iso_checksum = "sha256:..."
  
  cpus      = 2
  memory    = 2048
  disk_size = 40960
  
  ssh_username = "ubuntu"
  ssh_password = "ubuntu"
  
  shutdown_command = "shutdown -P now"
  
  output_directory = "output-virtualbox"
}
```

## Best Practices

1. **Use appropriate builders** - Choose right builder for platform
2. **Use variables** - Parameterize templates
3. **Use provisioners** - Install and configure software
4. **Implement debugging** - Use debug mode
5. **Test images** - Verify images work correctly
6. **Document templates** - Add comments and README
7. **Use version control** - Store templates in Git
8. **Implement CI/CD** - Automate image building
9. **Use post-processors** - Process images after build
10. **Use multiple regions** - Distribute images across regions
