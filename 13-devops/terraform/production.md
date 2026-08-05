# Terraform Production

## Remote State

```hcl
terraform {
    backend "s3" {
        bucket         = "company-terraform-state"
        key            = "production/infrastructure.tfstate"
        region         = "us-west-2"
        encrypt        = true
        dynamodb_table = "terraform-locks"
    }
}
```

## State Locking

```hcl
# DynamoDB table for locking
resource "aws_dynamodb_table" "terraform_locks" {
    name         = "terraform-locks"
    billing_mode = "PAY_PER_REQUEST"
    hash_key     = "LockID"

    attribute {
        name = "LockID"
        type = "S"
    }
}
```

## Workspaces

```bash
# Create workspaces
terraform workspace new dev
terraform workspace new staging
terraform workspace new prod

# List workspaces
terraform workspace list

# Switch workspace
terraform workspace select prod

# Show current workspace
terraform workspace show
```

## Promotion Workflow

1. **Development**: Apply changes, test functionality
2. **Staging**: Verify behavior, run integration tests
3. **Production**: Apply with caution, monitor closely

```bash
# Promote from dev to staging
terraform workspace select staging
terraform plan -out=staging.tfplan
terraform apply staging.tfplan

# Promote from staging to production
terraform workspace select prod
terraform plan -out=prod.tfplan
terraform apply prod.tfplan
```

## Safety Measures

```hcl
# Prevent accidental destruction
resource "aws_db_instance" "main" {
    lifecycle {
        prevent_destroy = true
    }
}

# Use move to rename resources
moved {
    from = aws_instance.web
    to   = aws_instance.application
}

# Import existing resources
import {
    to = aws_instance.existing
    id = "i-1234567890abcdef0"
}
```

## Rollback Strategy

```bash
# View state history
terraform state pull | jq '.serial'

# Restore previous state
aws s3api get-object \
    --bucket terraform-state \
    --key prod/terraform.tfstate \
    prod/terraform.tfstate.backup

# Apply previous state
terraform apply -auto-approve
```

## Production Checklist

- [ ] State stored remotely with encryption
- [ ] State locking enabled
- [ ] Sensitive variables marked as sensitive
- [ ] Secrets stored in Vault or Secrets Manager
- [ ] CloudTrail enabled for auditing
- [ ] VPC Flow Logs enabled
- [ ] Security groups follow least privilege
- [ ] Database backups enabled
- [ ] Monitoring and alerting configured
- [ ] Cost estimates reviewed
