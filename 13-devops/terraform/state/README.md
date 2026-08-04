# Terraform State Management

## Overview

Terraform state is a mapping of your infrastructure resources to their configuration. State management is critical for collaborative Terraform usage.

## Remote State

### S3 Backend
```hcl
terraform {
  backend "s3" {
    bucket         = "my-terraform-state"
    key            = "prod/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}
```

### Terraform Cloud
```hcl
terraform {
  cloud {
    organization = "my-org"
    workspaces {
      name = "my-workspace"
    }
  }
}
```

## State Commands

```bash
# Show state
terraform show

# List resources
terraform state list

# Show specific resource
terraform state show aws_instance.web

# Move resource
terraform state mv aws_instance.web aws_instance.web_server

# Remove resource from state
terraform state rm aws_instance.web

# Import existing resource
terraform import aws_instance.web i-1234567890abcdef0

# Force unlock state
terraform force-unlock LOCK_ID
```

## State Locking

```bash
# Lock state (automatic with apply/plan)
# Force unlock if needed
terraform force-unlock <lock-id>
```

## Best Practices

1. **Use remote state** - Never store state locally in production
2. **Enable state locking** - Use DynamoDB for AWS
3. **Encrypt state** - Protect sensitive data
4. **Use workspaces** - Manage multiple environments
5. **Implement state backup** - Enable state versioning
6. **Use state files sparingly** - Minimize state complexity
7. **Implement access controls** - Restrict state access
8. **Monitor state changes** - Track state modifications
9. **Document state** - Add comments for complex state
10. **Test state operations** - Verify state commands
