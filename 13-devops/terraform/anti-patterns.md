# Terraform Anti-Patterns

## 1. Hardcoding Values
**Description:** Hardcoding sensitive or environment-specific values in code.

**Why it's bad:** Security risk, not portable across environments.

**Example (bad code):**
```hcl
resource "aws_instance" "web" {
  ami           = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"
  
  tags = {
    Name = "production-web-server"
  }
}
```

**Better approach:** Use variables:
```hcl
variable "ami_id" {
  type = string
}

variable "instance_type" {
  type = string
}

resource "aws_instance" "web" {
  ami           = var.ami_id
  instance_type = var.instance_type
  
  tags = {
    Name = "${var.environment}-web-server"
  }
}
```

**Impact:** Portable, secure, reusable.

---

## 2. Not Using Remote State
**Description:** Using local state files.

**Why it's bad:** State file loss, team collaboration issues, no state locking.

**Example (bad code):**
```bash
terraform apply
# State stored locally in terraform.tfstate
```

**Better approach:** Use remote state:
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

**Impact:** State protection, team collaboration, locking.

---

## 3. No Module Usage
**Description:** Writing all infrastructure in a single configuration.

**Why it's bad:** Code duplication, hard to maintain, not reusable.

**Example (bad code):**
```hcl
# Same configuration repeated for each environment
resource "aws_instance" "dev_web" { ... }
resource "aws_instance" "staging_web" { ... }
resource "aws_instance" "prod_web" { ... }
```

**Better approach:** Use modules:
```hcl
module "dev_web" {
  source = "./modules/web-server"
  environment = "dev"
  instance_type = "t2.micro"
}

module "prod_web" {
  source = "./modules/web-server"
  environment = "prod"
  instance_type = "t3.medium"
}
```

**Impact:** Reusable, DRY, maintainable.

---

## 4. Ignoring State Locking
**description:** Not using state locking.

**Why it's bad:** State corruption when multiple people apply simultaneously.

**Example (bad code):**
```bash
# No locking configured
terraform apply
# Another person applies at same time
# State corruption
```

**Better approach:** Use state locking:
```hcl
terraform {
  backend "s3" {
    bucket = "my-terraform-state"
    key    = "terraform.tfstate"
    region = "us-east-1"
    dynamodb_table = "terraform-locks"
  }
}
```

**Impact:** Prevents state corruption, team safety.

---

## 5. Not Using Workspaces
**Description:** Using same state for multiple environments.

**Why it's bad:** Environment mixing, accidental resource modification.

**Example (bad code):**
```bash
# Same state file for dev and prod
terraform apply
```

**Better approach:** Use workspaces:
```bash
terraform workspace new dev
terraform workspace select dev
terraform apply

terraform workspace new prod
terraform workspace select prod
terraform apply
```

**Impact:** Environment isolation, safe management.

---

## 6. Ignoring Plan Before Apply
**Description:** Running terraform apply without reviewing the plan.

**Why it's bad:** Unintended changes, accidental resource deletion.

**Example (bad code):**
```bash
terraform apply -auto-approve
```

**Better approach:** Review plan first:
```bash
terraform plan -out=tfplan
# Review changes
terraform apply tfplan
```

**Impact:** Controlled changes, fewer accidents.

---

## 7. Not Using Variables for CIDR Blocks
**Description:** Hardcoding IP addresses and CIDR blocks.

**Why it's bad:** Not portable, conflicts across environments.

**Example (bad code):**
```hcl
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
}
```

**Better approach:** Use variables:
```hcl
variable "vpc_cidr" {
  type = string
  default = "10.0.0.0/16"
}

resource "aws_vpc" "main" {
  cidr_block = var.vpc_cidr
}
```

**Impact:** Flexible networking, environment-specific.

---

## 8. Not Using Lifecycle Rules
**Description:** Not protecting critical resources from deletion.

**Why it's bad:** Accidental deletion of important resources.

**Example (bad code):**
```hcl
resource "aws_s3_bucket" "data" {
  bucket = "my-data-bucket"
}
```

**Better approach:** Use lifecycle rules:
```hcl
resource "aws_s3_bucket" "data" {
  bucket = "my-data-bucket"
  
  lifecycle {
    prevent_destroy = true
  }
}
```

**Impact:** Resource protection, prevents accidents.

---

## 9. Ignoring Provider Versioning
**Description:** Not pinning provider versions.

**Why it's bad:** Breaking changes when providers update.

**Example (bad code):**
```hcl
provider "aws" {
  region = "us-east-1"
}
```

**Better approach:** Pin versions:
```hcl
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
  required_version = ">= 1.0"
}
```

**Impact:** Reproducible, stable.

---

## 10. Not Using Data Sources
**Description:** Hardcoding resource IDs or attributes.

**Why it's bad:** Not dynamic, breaks when resources change.

**Example (bad code):**
```hcl
resource "aws_instance" "web" {
  ami = "ami-0c55b159cbfafe1f0"
  subnet_id = "subnet-12345678"
}
```

**Better approach:** Use data sources:
```hcl
data "aws_ami" "web" {
  most_recent = true
  owners      = ["amazon"]
}

data "aws_subnets" "available" {
  filter {
    name   = "vpc-id"
    values = [var.vpc_id]
  }
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.web.id
  subnet_id     = data.aws_subnets.available.ids[0]
}
```

**Impact:** Dynamic, portable.

---

## 11. Not Using terraform fmt
**Description:** Not formatting code consistently.

**Why it's bad:** Inconsistent style, harder to review.

**Example (bad code):**
```hcl
resource "aws_instance" "web" {
ami="ami-123"
instance_type="t2.micro"
}
```

**Better approach:** Format code:
```bash
terraform fmt
```

**Impact:** Consistent style, easier reviews.

---

## 12. Ignoring Security Scanning
**description:** Not scanning for security issues.

**Why it's bad:** Security vulnerabilities go undetected.

**Example (bad code):**
```bash
terraform apply
# No security scanning
```

**Better approach:** Use security tools:
```bash
tfsec .
checkov -d .
```

**Impact:** Security compliance, vulnerability detection.