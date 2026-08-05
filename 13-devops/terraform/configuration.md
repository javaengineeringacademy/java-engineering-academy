# Terraform Configuration

## main.tf

Primary configuration file:

```hcl
terraform {
    required_version = ">= 1.5.0"

    required_providers {
        aws = {
            source  = "hashicorp/aws"
            version = "~> 5.0"
        }
    }
}

provider "aws" {
    region = var.aws_region

    default_tags {
        tags = {
            Environment = var.environment
            ManagedBy   = "terraform"
        }
    }
}

resource "aws_instance" "web" {
    ami           = data.aws_ami.ubuntu.id
    instance_type = var.instance_type

    vpc_security_group_ids = [aws_security_group.web.id]
    subnet_id              = aws_subnet.public.id

    tags = {
        Name = "web-server"
    }
}
```

## variables.tf

Input variable definitions:

```hcl
variable "aws_region" {
    type        = string
    default     = "us-west-2"
    description = "AWS region"
}

variable "environment" {
    type        = string
    description = "Environment name"
    validation {
        condition     = contains(["dev", "staging", "prod"], var.environment)
        error_message = "Environment must be dev, staging, or prod."
    }
}

variable "instance_type" {
    type        = string
    default     = "t2.micro"
    description = "EC2 instance type"
}

variable "vpc_cidr" {
    type        = string
    default     = "10.0.0.0/16"
    description = "VPC CIDR block"
}

variable "enable_monitoring" {
    type        = bool
    default     = true
    description = "Enable CloudWatch monitoring"
}
```

## outputs.tf

Output value definitions:

```hcl
output "instance_id" {
    value       = aws_instance.web.id
    description = "EC2 instance ID"
}

output "public_ip" {
    value       = aws_instance.web.public_ip
    description = "Public IP address"
}

output "vpc_id" {
    value       = aws_vpc.main.id
    description = "VPC ID"
}

output "subnet_ids" {
    value       = aws_subnet.private[*].id
    description = "Private subnet IDs"
}

output "database_endpoint" {
    value       = aws_db_instance.main.endpoint
    sensitive   = true
    description = "Database endpoint"
}
```

## backend.tf

State storage configuration:

```hcl
terraform {
    backend "s3" {
        bucket         = "my-terraform-state"
        key            = "prod/terraform.tfstate"
        region         = "us-west-2"
        encrypt        = true
        dynamodb_table = "terraform-locks"
    }
}
```

Alternative backends:

```hcl
# GCS
terraform {
    backend "gcs" {
        bucket = "my-terraform-state"
        prefix = "prod"
    }
}

# Azure
terraform {
    backend "azurerm" {
        resource_group_name  = "terraform-state"
        storage_account_name = "terraformstate"
        container_name       = "tfstate"
        key                  = "prod.terraform.tfstate"
    }
}
```

## providers.tf

Provider configuration:

```hcl
provider "aws" {
    region = var.aws_region

    assume_role {
        role_arn = "arn:aws:iam::ACCOUNT_ID:role/terraform"
    }

    default_tags {
        tags = {
            Project     = "my-project"
            Environment = var.environment
            ManagedBy   = "terraform"
        }
    }
}

provider "aws" {
    alias  = "us_east_1"
    region = "us-east-1"
}

data "aws_caller_identity" "current" {}
data "aws_region" "current" {}
```

## Version Constraints

```hcl
terraform {
    required_version = ">= 1.5.0, < 2.0.0"
}

provider "aws" {
    version = "~> 5.0"    # >= 5.0, < 6.0
    region  = "us-west-2"
}

# Exact version
provider "aws" {
    version = "= 5.1.0"
}
```
