# Terraform Cheat Sheet

## Initialization

```bash
terraform init                    # Initialize project
terraform init -upgrade           # Upgrade providers
terraform init -migrate-state     # Migrate state backend
terraform init -reconfigure       # Reconfigure backend
```

## Planning

```bash
terraform plan                    # Preview changes
terraform plan -out=tfplan        # Save plan
terraform plan -target=res        # Target resource
terraform plan -destroy           # Plan destruction
terraform plan -detailed-exitcode # Exit code on changes
```

## Apply

```bash
terraform apply                   # Apply changes
terraform apply tfplan            # Apply saved plan
terraform apply -auto-approve     # Skip confirmation
terraform apply -target=res       # Target resource
terraform apply -parallelism=10   # Set parallelism
```

## Destroy

```bash
terraform destroy                 # Destroy all resources
terraform destroy -target=res     # Target resource
terraform destroy -auto-approve   # Skip confirmation
```

## State

```bash
terraform state list              # List resources
terraform state show res          # Show resource
terraform state mv old new        # Move resource
terraform state rm res            # Remove resource
terraform state pull              # Pull state
terraform state push state.json   # Push state
```

## Import

```bash
terraform import res id           # Import resource
terraform import module.res id    # Import to module
```

## Validate

```bash
terraform validate                # Validate configuration
terraform fmt                     # Format files
terraform fmt -check             # Check formatting
terraform fmt -recursive         # Format recursively
```

## Console

```bash
terraform console                 # Start console
terraform console -var="x=1"     # With variables
```

## Graph

```bash
terraform graph                   # Generate graph
terraform graph | dot -Tpng > graph.png  # Visualize
```

## Providers

```bash
terraform providers               # List providers
terraform providers lock          # Lock provider versions
terraform providers mirror DIR    # Mirror providers
```

## Workspace

```bash
terraform workspace list          # List workspaces
terraform workspace new name      # Create workspace
terraform workspace select name   # Switch workspace
terraform workspace show          # Show current
terraform workspace delete name   # Delete workspace
```

## Output

```bash
terraform output                  # List outputs
terraform output -json            # JSON format
terraform output name             # Specific output
```

## Variables

```bash
terraform console -var="x=1"     # Set variable
terraform console -var-file="tfvars"  # From file
terraform console -var="x=${1+2}"    # Expression
```

## Logs

```bash
export TF_LOG=TRACE              # Enable logging
export TF_LOG_PATH=debug.log     # Log to file
export TF_LOG=DEBUG              # Debug level
```
