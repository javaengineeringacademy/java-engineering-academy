# Packer Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Packer templates.

## Template Organization

```
packer/
├── templates/
│   ├── ubuntu/
│   │   ├── ubuntu.pkr.hcl
│   │   └── variables.pkr.hcl
│   └── centos/
│       ├── centos.pkr.hcl
│       └── variables.pkr.hcl
├── scripts/
│   ├── install.sh
│   └── configure.sh
├── files/
│   └── app.conf
├── ansible/
│   └── playbook.yml
└── Makefile
```

## Template Best Practices

### Use Variables
```hcl
variable "region" {
  type    = string
  default = "us-east-1"
}

source "amazon-ebs" "ubuntu" {
  region = var.region
  # ...
}
```

### Use Locals
```hcl
locals {
  timestamp = formatdate("YYYYMMDDhhmm", timestamp())
  
  common_tags = {
    ManagedBy = "Packer"
    Timestamp = local.timestamp
  }
}
```

### Use Validation
```hcl
variable "instance_type" {
  type    = string
  default = "t2.micro"
  
  validation {
    condition     = can(regex("^t2\\.", var.instance_type))
    error_message = "Instance type must start with t2."
  }
}
```

## Build Process

```bash
# Validate template
packer validate template.pkr.hcl

# Format template
packer fmt template.pkr.hcl

# Build image
packer build template.pkr.hcl

# Build with variables
packer build -var "region=us-west-2" template.pkr.hcl
```

## Best Practices Summary

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
