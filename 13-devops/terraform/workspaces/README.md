# Terraform Workspaces

## Overview

Terraform workspaces allow you to manage multiple environments (dev, staging, prod) using the same configuration with different state files.

## Workspace Commands

```bash
# List workspaces
terraform workspace list

# Create workspace
terraform workspace new dev

# Select workspace
terraform workspace select dev

# Show current workspace
terraform workspace show

# Delete workspace
terraform workspace delete dev
```

## Using Workspaces

```hcl
# Reference current workspace
locals {
  environment = terraform.workspace
  
  instance_type = terraform.workspace == "prod" ? "t3.large" : "t2.micro"
  
  common_tags = {
    Environment = terraform.workspace
    ManagedBy   = "Terraform"
  }
}

resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = local.instance_type
  
  tags = merge(local.common_tags, {
    Name = "web-${terraform.workspace}"
  })
}
```

## Workspace Isolation

```hcl
# Different state per workspace
terraform {
  backend "s3" {
    bucket = "my-terraform-state"
    key    = "env:/dev/terraform.tfstate"
    region = "us-east-1"
  }
}
```

## Best Practices

1. **Use workspaces for environments** - Isolate dev, staging, prod
2. **Implement workspace naming conventions** - Consistent naming
3. **Use workspace-specific variables** - Different configs per env
4. **Implement workspace-specific tags** - Track resources
5. **Use workspace-specific backends** - Separate state files
6. **Document workspaces** - Add comments for complex configs
7. **Test workspace operations** - Verify workspace isolation
8. **Implement workspace cleanup** - Remove unused workspaces
9. **Monitor workspace usage** - Track workspace activity
10. **Use workspace-specific CI/CD** - Pipeline per workspace
