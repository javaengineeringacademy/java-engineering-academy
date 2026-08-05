# Terraform Project Structure

## Standard Layout

```
project/
  environments/
    dev/
      main.tf
      variables.tf
      outputs.tf
      terraform.tfvars
    staging/
      main.tf
      variables.tf
      outputs.tf
      terraform.tfvars
    prod/
      main.tf
      variables.tf
      outputs.tf
      terraform.tfvars
  modules/
    vpc/
      main.tf
      variables.tf
      outputs.tf
    ec2/
      main.tf
      variables.tf
      outputs.tf
    rds/
      main.tf
      variables.tf
      outputs.tf
  global/
    iam/
      main.tf
      variables.tf
      outputs.tf
  .terraform/
  terraform.tfstate
  terraform.tfstate.backup
  .gitignore
  README.md
```

## Environment Separation

Separate state files per environment:

```hcl
# dev/main.tf
terraform {
    backend "s3" {
        bucket = "terraform-state"
        key    = "dev/terraform.tfstate"
        region = "us-west-2"
    }
}
```

## Module Structure

```
modules/vpc/
  main.tf         # Resource definitions
  variables.tf    # Input variables
  outputs.tf      # Output values
  versions.tf     # Provider constraints
  README.md       # Module documentation
```

## Naming Conventions

- Files: lowercase with underscores
- Resources: snake_case
- Variables: snake_case
- Outputs: snake_case
- Modules: snake_case

## Configuration Files

```
project/
  main.tf           # Primary resources
  variables.tf      # Input variables
  outputs.tf        # Output values
  providers.tf      # Provider configuration
  versions.tf       # Version constraints
  backend.tf        # State backend
  data.tf           # Data sources
  locals.tf         # Local values
  terraform.tfvars  # Variable values
```

## Best Practices

- Separate state per environment
- Use modules for reusable components
- Keep modules small and focused
- Document module interfaces
- Use consistent naming conventions
- Version pin all providers and modules
- Store state remotely with locking
- Use workspaces for temporary environments
