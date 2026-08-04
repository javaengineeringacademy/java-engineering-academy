# Terraform Fundamentals

## Overview

Terraform is an Infrastructure as Code (IaC) tool by HashiCorp that enables you to define and provision infrastructure using a declarative configuration language.

## Providers

### AWS Provider
```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

provider "aws" {
  alias  = "west"
  region = "us-west-2"
}
```

## Resources

### EC2 Instance
```hcl
resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"

  tags = {
    Name = "web-server"
  }
}

resource "aws_security_group" "web" {
  name        = "web-sg"
  description = "Security group for web server"

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

### S3 Bucket
```hcl
resource "aws_s3_bucket" "data" {
  bucket = "my-data-bucket"

  tags = {
    Name = "Data bucket"
  }
}

resource "aws_s3_bucket_versioning" "data" {
  bucket = aws_s3_bucket.data.id

  versioning_configuration {
    status = "Enabled"
  }
}
```

## Variables

```hcl
variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t2.micro"

  validation {
    condition     = can(regex("^t2\\.", var.instance_type))
    error_message = "Instance type must start with t2."
  }
}

variable "enable_monitoring" {
  description = "Enable monitoring"
  type        = bool
  default     = false
}

locals {
  common_tags = {
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}
```

## Outputs

```hcl
output "instance_id" {
  description = "EC2 instance ID"
  value       = aws_instance.web.id
}

output "public_ip" {
  description = "Public IP address"
  value       = aws_instance.web.public_ip
}
```

## State Management

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

## Commands

```bash
# Initialize
terraform init

# Plan changes
terraform plan

# Apply changes
terraform apply

# Destroy infrastructure
terraform destroy

# Format code
terraform fmt

# Validate configuration
terraform validate
```

## Best Practices

1. **Use remote state** - Store state in S3 or Terraform Cloud
2. **Implement state locking** - Use DynamoDB for AWS
3. **Use modules** - Reuse infrastructure components
4. **Implement workspaces** - Manage multiple environments
5. **Use variables** - Parameterize configurations
6. **Implement tags** - Organize and track resources
7. **Use data sources** - Reference existing resources
8. **Implement lifecycle rules** - Control resource behavior
9. **Use provisioners sparingly** - Prefer declarative approaches
10. **Document configurations** - Add comments and README
