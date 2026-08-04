# Packer Fundamentals

## Overview

Packer is a tool by HashiCorp for creating identical machine images for multiple platforms from a single source configuration.

## Basic Template

```json
{
  "variables": {
    "aws_access_key": "{{env `AWS_ACCESS_KEY_ID`}}",
    "aws_secret_key": "{{env `AWS_SECRET_ACCESS_KEY`}}",
    "ami_name": "my-app-{{timestamp}}"
  },
  "builders": [
    {
      "type": "amazon-ebs",
      "access_key": "{{user `aws_access_key`}}",
      "secret_key": "{{user `aws_secret_key`}}",
      "region": "us-east-1",
      "source_ami": "ami-0c55b159cbfafe1f0",
      "instance_type": "t2.micro",
      "ssh_username": "ubuntu",
      "ami_name": "{{user `ami_name`}}"
    }
  ],
  "provisioners": [
    {
      "type": "shell",
      "inline": [
        "sudo apt-get update",
        "sudo apt-get install -y nginx"
      ]
    }
  ]
}
```

## HCL2 Template

```hcl
variable "aws_access_key" {
  type    = string
  default = env("AWS_ACCESS_KEY_ID")
}

variable "aws_secret_key" {
  type    = string
  default = env("AWS_SECRET_ACCESS_KEY")
}

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
}

build {
  sources = ["source.amazon-ebs.ubuntu"]
  
  provisioner "shell" {
    inline = [
      "sudo apt-get update",
      "sudo apt-get install -y nginx"
    ]
  }
}
```

## Commands

```bash
# Build image
packer build template.pkr.hcl

# Validate template
packer validate template.pkr.hcl

# Format template
packer fmt template.pkr.hcl

# Inspect template
packer inspect template.pkr.hcl
```

## Best Practices

1. **Use HCL2** - Use modern template format
2. **Use variables** - Parameterize templates
3. **Use provisioners** - Install and configure software
4. **Implement debugging** - Use debug mode
5. **Test images** - Verify images work correctly
6. **Document templates** - Add comments and README
7. **Use version control** - Store templates in Git
8. **Implement CI/CD** - Automate image building
9. **Use builders** - Use appropriate builder for platform
10. **Use post-processors** - Process images after build
