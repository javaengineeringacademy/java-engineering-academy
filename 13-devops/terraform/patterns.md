# Terraform Patterns

## 1. Module Composition

**Problem:** Infrastructure code is duplicated across environments with no reuse.

**Solution:** Encapsulate reusable infrastructure components into modules with clean interfaces.

**Implementation:**
```hcl
# modules/vpc/main.tf
variable "cidr_block" { type = string }
variable "azs" { type = list(string) }

resource "aws_vpc" "this" {
  cidr_block = var.cidr_block
}

resource "aws_subnet" "public" {
  for_each          = { for az in var.azs : az => az }
  vpc_id            = aws_vpc.this.id
  cidr_block        = cidrsubnet(var.cidr_block, 4, index(var.azs, each.key))
  availability_zone = each.value
}

output "vpc_id" { value = aws_vpc.this.id }
output "subnet_ids" { value = [for s in aws_subnet.public : s.id] }

# environments/prod/main.tf
module "vpc" {
  source     = "../../modules/vpc"
  cidr_block = "10.0.0.0/16"
  azs        = ["us-east-1a", "us-east-1b", "us-east-1c"]
}
```

**When to Use:** When the same infrastructure pattern is used across multiple environments or projects.

**When NOT to Use:** When infrastructure is unique and unlikely to be reused. Premature abstraction adds complexity.

---

## 2. Environment Separation with Workspaces

**Problem:** Staging and production share state, risking accidental cross-environment changes.

**Solution:** Use separate state files per environment with identical module code.

**Implementation:**
```hcl
# environments/prod/main.tf
terraform {
  backend "s3" {
    bucket = "mycompany-terraform-state"
    key    = "prod/infrastructure.tfstate"
    region = "us-east-1"
  }
}

module "vpc" {
  source     = "../../modules/vpc"
  cidr_block = var.cidr_block
}

# environments/staging/main.tf
terraform {
  backend "s3" {
    bucket = "mycompany-terraform-state"
    key    = "staging/infrastructure.tfstate"
    region = "us-east-1"
  }
}

module "vpc" {
  source     = "../../modules/vpc"
  cidr_block = var.cidr_block
}
```

**When to Use:** Every Terraform project with more than one environment.

**When NOT to Use:** Single-environment projects where workspace overhead is unnecessary.

---

## 3. Remote State with State Locking

**Problem:** Local state files are not shared across team members and offer no concurrency safety.

**Solution:** Use remote backends (S3, Terraform Cloud) with state locking.

**Implementation:**
```hcl
terraform {
  backend "s3" {
    bucket         = "mycompany-terraform-state"
    key            = "infrastructure.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}
```

```bash
# Read state from another project
data "terraform_remote_state" "vpc" {
  backend = "s3"
  config = {
    bucket = "mycompany-terraform-state"
    key    = "prod/vpc.tfstate"
    region = "us-east-1"
  }
}

output "vpc_id" { value = data.terraform_remote_state.vpc.outputs.vpc_id }
```

**When to Use:** Every Terraform project used by more than one person.

**When NOT to Use:** Personal learning projects where local state is sufficient.

---

## 4. State Splitting

**Problem:** One monolithic state file means a single resource change locks all infrastructure.

**Solution:** Split state by domain (networking, compute, data) so changes are isolated.

**Implementation:**
```
infra/
  networking/
    main.tf          # VPC, subnets, routing
  compute/
    main.tf          # ECS, Lambda, ASGs
    variables.tf
  data/
    main.tf          # RDS, ElastiCache, S3
  dns/
    main.tf          # Route53, ACM
```

Each directory has its own backend and state file. Cross-stack references use `terraform_remote_state` or SSM parameters.

**When to Use:** When infrastructure has distinct domains owned by different teams or when blast radius must be minimized.

**When NOT to Use:** Small projects where the overhead of managing multiple states exceeds the benefit.

---

## 5. Blue-Green Infrastructure

**Problem:** Infrastructure changes require downtime or risky in-place modifications.

**Solution:** Deploy new infrastructure alongside old, then switch traffic atomically.

**Implementation:**
```hcl
resource "aws_lb_target_group" "blue" {
  name     = "app-blue"
  port     = 80
  protocol = "HTTP"
}

resource "aws_lb_target_group" "green" {
  name     = "app-green"
  port     = 80
  protocol = "HTTP"
}

resource "aws_lb_listener_rule" "active" {
  listener_arn = aws_lb_listener.https.arn
  action {
    type             = "forward"
    target_group_arn = var.active_color == "blue" ? aws_lb_target_group.blue.arn : aws_lb_target_group.green.arn
  }
}
```

**When to Use:** When zero-downtime deployments are required and infrastructure cannot tolerate in-place updates.

**When NOT to Use:** When the cost of maintaining duplicate infrastructure is prohibitive.

---

## 6. Plan/Apply Gates with CI/CD

**Problem:** Manual terraform apply can introduce unreviewed changes to production.

**Solution:** Require plan output review and approval before apply in CI/CD pipelines.

**Implementation:**
```yaml
# .github/workflows/terraform.yml
name: Terraform
on: [pull_request]
jobs:
  plan:
    steps:
    - run: terraform init
    - run: terraform plan -out=tfplan
    - run: terraform show -json tfplan > plan.json

  apply:
    if: github.event_name == 'push' && github.ref == 'refs/heads/main'
    needs: review
    steps:
    - run: terraform init
    - run: terraform apply -auto-approve tfplan
```

**When to Use:** Every production Terraform deployment. Unreviewed applies are a common source of outages.

**When NOT to Use:** Development sandboxes where rapid iteration is more important than change control.

---

## 7. DRY with for_each and locals

**Problem:** Repeating similar resources with slight variations creates maintenance burden.

**Solution:** Use `for_each` and `locals` to generate resources from a data structure.

**Implementation:**
```hcl
locals {
  services = {
    api = { port = 8080, cpu = 512, memory = 1024 }
    worker = { port = 9090, cpu = 1024, memory = 2048 }
    cache = { port = 6379, cpu = 256, memory = 512 }
  }
}

resource "aws_ecs_service" "this" {
  for_each = local.services

  name            = each.key
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.this[each.key].arn
  desired_count   = 2

  container_definitions = jsonencode([{
    name  = each.key
    image = "${var.ecr_url}/${each.key}:${var.image_tag}"
    portMappings = [{
      containerPort = each.value.port
    }]
    cpu    = each.value.cpu
    memory = each.value.memory
  }])
}
```

**When to Use:** When you have N similar resources that differ only in parameterized values.

**When NOT to Use:** When resources are fundamentally different or when `for_each` would obscure important details.

---

## 8. Conditional Creation

**Problem:** Resources must be created or skipped based on environment or feature flags.

**Solution:** Use `count` or `for_each` with conditionals, or use `dynamic` blocks.

**Implementation:**
```hcl
variable "enable_monitoring" { type = bool, default = false }
variable "environments" { type = list(string) }

# Conditional resource
resource "aws_cloudwatch_metric_alarm" "cpu" {
  count = var.enable_monitoring ? 1 : 0
  alarm_name = "cpu-high"
  # ...
}

# Dynamic blocks
resource "aws_security_group" "this" {
  name = "app"

  dynamic "ingress" {
    for_each = var.allowed_ports
    content {
      from_port   = ingress.value
      to_port     = ingress.value
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
  }
}
```

**When to Use:** When resources are conditionally needed based on environment, feature flags, or user input.

**When NOT to Use:** When conditions create complex logic that is hard to reason about. Consider splitting into modules instead.

---

## 9. Testing with Terratest

**Problem:** Terraform configurations can have subtle errors not caught by `terraform validate`.

**Solution:** Write integration tests that deploy real infrastructure and verify outputs.

**Implementation:**
```go
package test

import (
    "testing"
    "github.com/gruntwork-io/terratest/modules/terraform"
    "github.com/stretchr/testify/assert"
)

func TestVpcModule(t *testing.T) {
    terraformOptions := terraform.WithDefaultRetryableErrors(t, &terraform.Options{
        TerraformDir: "../modules/vpc",
        Vars: map[string]interface{}{
            "cidr_block": "10.0.0.0/16",
            "azs":        []string{"us-east-1a", "us-east-1b"},
        },
    })

    defer terraform.Destroy(t, terraformOptions)
    terraform.InitAndApply(t, terraformOptions)

    vpcId := terraform.Output(t, terraformOptions, "vpc_id")
    assert.Regexp(t, "^vpc-", vpcId)
}
```

**When to Use:** When infrastructure modules have critical correctness requirements (VPC, IAM, networking).

**When NOT to Use:** When test infrastructure costs are prohibitive or when simple validation suffices.

---

## Best Practices

- Pin provider and module versions to avoid unexpected changes.
- Use `terraform plan` as a mandatory step before every apply.
- Store state remotely with encryption and locking enabled.
- Use variables for all configurable values, never hardcode.
- Run `tflint` and `tfsec` in CI for static analysis.
- Tag all resources consistently for cost allocation and ownership.
- Use `moved` blocks instead of `terraform state mv` for refactoring.
