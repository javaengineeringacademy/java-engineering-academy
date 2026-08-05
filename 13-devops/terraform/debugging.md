# Terraform Debugging

## terraform plan

Preview changes before applying:

```bash
# Basic plan
terraform plan

# Save plan to file
terraform plan -out=tfplan

# Target specific resources
terraform plan -target=aws_instance.web

# Refresh state only
terraform plan -refresh=false

# Detailed exit code
terraform plan -detailed-exitcode
```

## terraform console

Interactive console for testing expressions:

```bash
# Start console
terraform console

# Test expressions
> var.instance_type
> aws_instance.web.id
> length(var.subnets)
> file("config.json")

# Exit
> exit
```

## terraform state

Inspect and manipulate state:

```bash
# List all resources
terraform state list

# Show resource details
terraform state show aws_instance.web

# Move resources
terraform state mv aws_instance.old aws_instance.new

# Remove from state
terraform state rm aws_instance.web

# Pull state
terraform state pull | jq .

# Push state
terraform state push state.json
```

## terraform import

Import existing resources:

```bash
# Import single resource
terraform import aws_instance.web i-1234567890abcdef0

# Import with module
terraform import module.vpc.aws_vpc.main vpc-12345678

# Import with workspace
terraform workspace select prod
terraform import aws_instance.web i-1234567890abcdef0
```

## terraform validate

Check configuration syntax:

```bash
# Validate configuration
terraform validate

# Check formatting
terraform fmt -check

# Auto-format
terraform fmt -recursive
```

## Logging

```bash
# Enable debug logging
export TF_LOG=DEBUG

# Log levels: TRACE, DEBUG, INFO, WARN, ERROR

# Log to file
export TF_LOG=DEBUG
export TF_LOG_PATH=./terraform.log

# Trace logging
export TF_LOG=TRACE
```

## Common Issues

**State drift:**

```bash
terraform plan -refresh=true
terraform apply
```

**Provider version conflicts:**

```bash
terraform init -upgrade
```

**Missing dependencies:**

```bash
terraform graph | dot -Tpng > graph.png
```

**Resource already exists:**

```bash
terraform import aws_instance.existing i-1234567890abcdef0
```

**Configuration drift:**

```bash
terraform refresh
terraform plan
```
