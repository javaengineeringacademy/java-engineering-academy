# Terraform Performance

## State Optimization

```hcl
# Use data sources to read existing resources
data "aws_vpc" "existing" {
    filter {
        name   = "tag:Name"
        values = ["production-vpc"]
    }
}

# Avoid importing unnecessary resources
# Use targeted apply for large configurations
terraform apply -target=aws_instance.web
```

## Parallelism

```bash
# Increase parallel operations
terraform apply -parallelism=20

# Default is 10
# Maximum depends on resource dependencies
```

Control parallel execution:

```hcl
resource "aws_instance" "web" {
    # ...

    lifecycle {
        # This resource must be created before others
        create_before_destroy = true
    }
}
```

## Large Infrastructure

For large configurations:

- Split into multiple workspaces
- Use modules to organize resources
- Implement remote state with locking
- Use data sources instead of managing all resources
- Consider Terraform Cloud for team management

## Caching

```hcl
# Provider caching
provider "aws" {
    region = "us-west-2"

    # Cache provider plugins
    skip_metadata_api_check = true
}

# Module caching
module "vpc" {
    source  = "terraform-aws-modules/vpc/aws"
    version = "5.0.0"
}
```

## Plan Optimization

```bash
# Refresh state only
terraform plan -refresh=false

# Target specific resources
terraform plan -target=aws_instance.web

# Save plan for later apply
terraform plan -out=planfile
terraform apply planfile
```

## State Locking

```hcl
# Enable state locking
terraform {
    backend "s3" {
        bucket         = "terraform-state"
        key            = "prod/terraform.tfstate"
        region         = "us-west-2"
        dynamodb_table = "terraform-locks"
        encrypt        = true
    }
}
```

## Performance Monitoring

```bash
# Trace execution
TF_LOG=TRACE terraform apply

# Performance profiling
TF_PROFILE=cpu terraform plan
```
