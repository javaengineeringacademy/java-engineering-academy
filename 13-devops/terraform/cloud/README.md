# Terraform Cloud

## Overview

Terraform Cloud is a HashiCorp product that provides a managed platform for Terraform with collaboration, state management, and policy enforcement features.

## Workspaces

### Creating Workspaces
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

### Workspace Settings
- **Execution Mode**: Local or Remote
- **Terraform Version**: Specify Terraform version
- **Variable Sets**: Shared variables across workspaces
- **Sentinel Policies**: Policy as code

## Variables

### Environment Variables
```hcl
variable "AWS_ACCESS_KEY_ID" {
  type      = string
  sensitive = true
}

variable "AWS_SECRET_ACCESS_KEY" {
  type      = string
  sensitive = true
}
```

### Workspace Variables
- **HCL**: Variable definitions
- **Environment Variables**: Shell environment
- **Sensitive Variables**: Encrypted at rest

## Run Triggers

```hcl
# Trigger on workspace changes
resource "terraform_cloud_run_trigger" "example" {
  workspace_id = "ws-..."
  sourceable_id = "ws-..."
}
```

## Sentinel Policies

```python
# policy.sentinel
import "tfplan/v2" as tfplan

# Enforce resource tagging
enforce_tags = rule {
    all tfplan.resource_changes as _, rc {
        rc.mode is "managed" implies
            "Tags" in rc.change.after
    }
}

# Enforce instance types
enforce_instance_type = rule {
    all tfplan.resource_changes as _, rc {
        rc.type is "aws_instance" implies
            rc.change.after.instance_type in ["t2.micro", "t2.small", "t2.medium"]
    }
}
```

## Best Practices

1. **Use remote execution** - Run Terraform in the cloud
2. **Implement Sentinel policies** - Enforce organizational standards
3. **Use variable sets** - Share variables across workspaces
4. **Enable cost estimation** - Track infrastructure costs
5. **Implement VCS integration** - Trigger runs on code changes
6. **Use run triggers** - Automate workspace dependencies
7. **Monitor run history** - Track Terraform changes
8. **Implement RBAC** - Control workspace access
9. **Use private modules** - Share modules securely
10. **Document workspaces** - Add descriptions for complex configs
