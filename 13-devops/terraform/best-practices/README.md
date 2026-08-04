# Terraform Best Practices

## Overview

This guide covers best practices for writing, organizing, and maintaining Terraform configurations.

## File Structure

```
infrastructure/
├── modules/
│   ├── vpc/
│   ├── ec2/
│   └── rds/
├── environments/
│   ├── dev/
│   │   ├── main.tf
│   │   ├── variables.tf
│   │   ├── outputs.tf
│   │   └── terraform.tfvars
│   ├── staging/
│   └── prod/
├── README.md
└── .gitignore
```

## Configuration Best Practices

### Use Variables
```hcl
variable "environment" {
  description = "Environment name"
  type        = string
  
  validation {
    condition     = contains(["dev", "staging", "prod"], var.environment)
    error_message = "Environment must be dev, staging, or prod."
  }
}
```

### Use Locals
```hcl
locals {
  common_tags = {
    Environment = var.environment
    Project     = var.project_name
    ManagedBy   = "Terraform"
  }
  
  name_prefix = "${var.project_name}-${var.environment}"
}
```

### Use Data Sources
```hcl
data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }
}
```

### Implement Lifecycle Rules
```hcl
resource "aws_instance" "web" {
  # ...
  
  lifecycle {
    create_before_destroy = true
    prevent_destroy       = false
    ignore_changes        = [tags]
  }
}
```

## Code Organization

### Separate Concerns
```hcl
# main.tf - Provider configuration
provider "aws" {
  region = var.region
}

# network.tf - Networking resources
resource "aws_vpc" "main" {
  # ...
}

# compute.tf - Compute resources
resource "aws_instance" "web" {
  # ...
}
```

### Use Modules
```hcl
module "vpc" {
  source = "./modules/vpc"
  
  name       = "my-vpc"
  cidr_block = "10.0.0.0/16"
}
```

## Security Best Practices

1. **Never commit secrets** - Use environment variables or Vault
2. **Use remote state** - Store state in S3 with encryption
3. **Enable state locking** - Use DynamoDB for AWS
4. **Implement RBAC** - Control Terraform access
5. **Use least privilege** - Minimize IAM permissions
6. **Enable audit logging** - Track Terraform changes
7. **Use variables for secrets** - Don't hardcode sensitive data
8. **Implement encryption** - Encrypt data at rest and in transit
9. **Use security scanning** - Scan configurations for issues
10. **Document security** - Add comments for security considerations

## Testing

### Terratest
```go
package test

import (
	"testing"
	"github.com/gruntwork-io/terratest/modules/terraform"
	"github.com/stretchr/testify/assert"
)

func TestVpc(t *testing.T) {
	terraformOptions := terraform.WithDefaultRetryableErrors(t, &terraform.Options{
		TerraformDir: "../modules/vpc",
		Vars: map[string]interface{}{
			"name":       "test-vpc",
			"cidr_block": "10.0.0.0/16",
		},
	})

	defer terraform.Destroy(t, terraformOptions)
	terraform.InitAndApply(t, terraformOptions)

	vpcId := terraform.Output(t, terraformOptions, "vpc_id")
	assert.NotEmpty(t, vpcId)
}
```

## Best Practices Summary

1. **Use version constraints** - Pin provider and module versions
2. **Implement proper naming** - Consistent resource naming
3. **Use tags extensively** - Organize and track resources
4. **Implement proper error handling** - Handle failures gracefully
5. **Use remote state** - Never store state locally in production
6. **Implement state locking** - Prevent concurrent modifications
7. **Use workspaces** - Manage multiple environments
8. **Implement proper documentation** - Add README and comments
9. **Test configurations** - Use Terratest or similar tools
10. **Monitor Terraform usage** - Track changes and performance
