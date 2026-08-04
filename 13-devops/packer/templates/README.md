# Packer Templates

## Overview

Packer templates define the machine image build process, including builders, provisioners, and post-processors.

## Variables

```hcl
variable "aws_access_key" {
  type        = string
  description = "AWS access key"
  default     = env("AWS_ACCESS_KEY_ID")
}

variable "aws_secret_key" {
  type        = string
  description = "AWS secret key"
  sensitive   = true
  default     = env("AWS_SECRET_ACCESS_KEY")
}

variable "app_version" {
  type        = string
  description = "Application version"
  default     = "1.0.0"
}
```

## Locals

```hcl
locals {
  timestamp = formatdate("YYYYMMDDhhmm", timestamp())
  
  common_tags = {
    Environment = "production"
    ManagedBy   = "Packer"
    AppVersion  = var.app_version
  }
}
```

## Functions

```hcl
# Timestamp
ami_name = "my-app-${local.timestamp}"

# Environment variables
access_key = env("AWS_ACCESS_KEY_ID")

# String functions
name = lower("MyApp")

# Conditional
instance_type = var.environment == "prod" ? "t3.large" : "t2.micro"

# Lists
regions = ["us-east-1", "us-west-2"]
```

## Best Practices

1. **Use HCL2** - Use modern template format
2. **Use variables** - Parameterize templates
3. **Use locals** - Define computed values
4. **Use functions** - Use built-in functions
5. **Document templates** - Add comments and README
6. **Use version control** - Store templates in Git
7. **Implement debugging** - Use debug mode
8. **Test templates** - Verify templates work correctly
9. **Use validation** - Validate templates before build
10. **Use formatting** - Format templates consistently
