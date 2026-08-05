# Terraform Security

## State Encryption

```hcl
terraform {
    backend "s3" {
        bucket         = "terraform-state"
        key            = "prod/terraform.tfstate"
        region         = "us-west-2"
        encrypt        = true
        dynamodb_table = "terraform-locks"
    }
}
```

## Secrets Management

```hcl
# Use variables for sensitive data
variable "db_password" {
    type      = string
    sensitive = true
}

# Store in Vault
data "vault_generic_secret" "db" {
    path = "secret/data/database"
}

resource "aws_db_instance" "main" {
    password = data.vault_generic_secret.db.data["password"]
}

# Use AWS Secrets Manager
data "aws_secretsmanager_secret_version" "db" {
    secret_id = "database/password"
}
```

## tfsec - Security Scanner

```bash
# Install
go install github.com/aquasecurity/tfsec/cmd/tfsec@latest

# Scan project
tfsec .

# Scan with specific rules
tfsec . --include-low-risk

# Output format
tfsec . --format json
```

Common findings:

- Unencrypted storage
- Public access to databases
- Overly permissive security groups
- Missing logging
- Hardcoded secrets

## checkov - Policy Checker

```bash
# Install
pip install checkov

# Scan Terraform
checkov -d .

# Scan specific file
checkov -f main.tf

# Run specific checks
checkov -d . --check CKV_AWS_18
```

## IAM Best Practices

```hcl
# Least privilege access
resource "aws_iam_policy" "s3_read" {
    name = "s3-read-only"

    policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
            {
                Effect = "Allow"
                Action = [
                    "s3:GetObject",
                    "s3:ListBucket"
                ]
                Resource = [
                    aws_s3_bucket.data.arn,
                    "${aws_s3_bucket.data.arn}/*"
                ]
            }
        ]
    })
}

# Use IAM roles instead of access keys
resource "aws_iam_role" "ec2" {
    name = "ec2-role"

    assume_role_policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
            {
                Effect = "Allow"
                Principal = {
                    Service = "ec2.amazonaws.com"
                }
                Action = "sts:AssumeRole"
            }
        ]
    })
}
```

## Network Security

```hcl
resource "aws_security_group" "web" {
    name = "web-sg"

    # Restrict ingress
    ingress {
        from_port   = 443
        to_port     = 443
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    # No unrestricted egress
    egress {
        from_port   = 443
        to_port     = 443
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
}

# Use VPC endpoints for AWS services
resource "aws_vpc_endpoint" "s3" {
    vpc_id       = aws_vpc.main.id
    service_name = "com.amazonaws.us-west-2.s3"
}
```

## Logging and Auditing

```hcl
# Enable CloudTrail
resource "aws_cloudtrail" "main" {
    name                          = "main-trail"
    s3_bucket_name                = aws_s3_bucket.logs.id
    enable_logging                = true
    include_global_service_events = true
}

# Enable VPC Flow Logs
resource "aws_flow_log" "main" {
    vpc_id          = aws_vpc.main.id
    traffic_type    = "ALL"
    iam_role_arn    = aws_iam_role.flow_log.arn
    log_destination = aws_s3_bucket.flow_logs.arn
}
```
