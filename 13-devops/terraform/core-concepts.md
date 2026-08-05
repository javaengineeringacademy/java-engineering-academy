# Terraform Core Concepts

## Resources

Resources represent infrastructure components:

```hcl
resource "aws_instance" "web" {
    ami           = "ami-0c55b159cbfafe1f0"
    instance_type = "t2.micro"

    tags = {
        Name = "web-server"
    }
}
```

Resource attributes:

- **Computed**: Set by provider after creation
- **Required**: Must be specified
- **Optional**: Can be omitted
- **Sensitive**: Marked as secret in output

Resource lifecycle:

```hcl
resource "aws_instance" "web" {
    lifecycle {
        create_before_destroy = true
        prevent_destroy       = false
        ignore_changes        = [tags]
    }
}
```

## Data Sources

Data sources query existing infrastructure:

```hcl
data "aws_ami" "ubuntu" {
    most_recent = true
    owners      = ["099720109477"]

    filter {
        name   = "name"
        values = ["ubuntu/images/hvm-ssd/ubuntu-*-amd64-server-*"]
    }
}

resource "aws_instance" "web" {
    ami = data.aws_ami.ubuntu.id
}
```

## Variables

Input variables define configuration:

```hcl
variable "instance_type" {
    type        = string
    default     = "t2.micro"
    description = "EC2 instance type"

    validation {
        condition     = can(regex("^t2\\.", var.instance_type))
        error_message = "Instance type must start with t2."
    }
}
```

Variable types:

- `string`, `number`, `bool`
- `list(string)`, `map(string)`
- `object({ name = string, age = number })`
- `tuple([string, number])`

## Outputs

Outputs export values from configuration:

```hcl
output "instance_ip" {
    value       = aws_instance.web.public_ip
    description = "Public IP of web server"
    sensitive   = false
}
```

## Modules

Modules group related resources:

```hcl
module "vpc" {
    source  = "terraform-aws-modules/vpc/aws"
    version = "5.0.0"

    name = "my-vpc"
    cidr = "10.0.0.0/16"

    azs             = ["us-west-2a", "us-west-2b"]
    private_subnets = ["10.0.1.0/24", "10.0.2.0/24"]
    public_subnets  = ["10.0.101.0/24", "10.0.102.0/24"]
}

output "vpc_id" {
    value = module.vpc.vpc_id
}
```

## State

State tracks infrastructure managed by Terraform:

```bash
# List resources
terraform state list

# Show resource details
terraform state show aws_instance.web

# Move resources
terraform state mv aws_instance.old aws_instance.new

# Remove from state
terraform state rm aws_instance.web

# Import existing resource
terraform import aws_instance.web i-1234567890abcdef0
```

## Provisioners

Provisioners execute actions on resources:

```hcl
resource "aws_instance" "web" {
    provisioner "remote-exec" {
        inline = [
            "sudo apt-get update",
            "sudo apt-get install -y nginx",
        ]

        connection {
            type        = "ssh"
            user        = "ubuntu"
            private_key = file("~/.ssh/id_rsa")
            host        = self.public_ip
        }
    }
}
```

Provisioner types:

- `local-exec`: Run local commands
- `remote-exec`: Run remote commands
- `file`: Copy files
- `chef`: Run Chef recipes
- `puppet`: Run Puppet manifests
