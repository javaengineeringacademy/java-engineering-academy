# Terraform Monitoring

## Terraform Cloud Audit

```hcl
# Enable audit logging
terraform {
    cloud {
        organization = "my-org"

        workspaces {
            name = "production"
        }
    }
}
```

Audit events:

- State file access
- Configuration changes
- Run triggers
- Variable updates
- Workspace management

## Drift Detection

```bash
# Check for drift
terraform plan -detailed-exitcode

# Exit codes:
# 0 = No changes
# 1 = Error
# 2 = Changes present
```

Scheduled drift detection:

```yaml
# GitHub Actions
name: Drift Detection
on:
    schedule:
        - cron: '0 8 * * *'

jobs:
    drift:
        runs-on: ubuntu-latest
        steps:
            - uses: hashicorp/setup-terraform@v2
            - run: terraform init
            - run: terraform plan -detailed-exitcode
```

## Cost Estimation

```bash
# Install infracost
brew install infracost

# Generate cost estimate
infracost breakdown --path .

# Compare plans
infracost diff --path .
```

Cost reporting:

```hcl
# Add cost estimates to PR comments
resource "aws_instance" "web" {
    instance_type = "t3.medium"  # ~$30/month

    tags = {
        CostCenter = "engineering"
    }
}
```

## Monitoring Integration

```hcl
# CloudWatch alarms
resource "aws_cloudwatch_metric_alarm" "cpu" {
    alarm_name          = "high-cpu"
    comparison_operator = "GreaterThanThreshold"
    evaluation_periods  = 2
    metric_name         = "CPUUtilization"
    namespace           = "AWS/EC2"
    period              = 300
    statistic           = "Average"
    threshold           = 80
    alarm_description   = "CPU utilization exceeds 80%"
}

# SNS notifications
resource "aws_sns_topic" "alerts" {
    name = "terraform-alerts"
}

resource "aws_sns_topic_subscription" "email" {
    topic_arn = aws_sns_topic.alerts.arn
    protocol  = "email"
    endpoint  = "ops@example.com"
}
```

## State Monitoring

```bash
# State file size
terraform state pull | wc -c

# Resource count
terraform state list | wc -l

# State history
aws s3api head-object \
    --bucket terraform-state \
    --key prod/terraform.tfstate
```

## Compliance Reporting

```bash
# Run compliance checks
terraform validate
terraform fmt -check
tfsec .
checkov -d .
```
