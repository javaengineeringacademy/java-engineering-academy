# Terraform Interview Questions

## Fundamentals

**1. What is Infrastructure as Code (IaC)?**
IaC manages infrastructure through machine-readable configuration files instead of manual processes. Benefits include version control, reproducibility, and automation. Terraform uses HCL for declarative configuration.

**2. Explain Terraform's plan/apply cycle.**
Terraform plan reads configuration and state, compares to remote infrastructure, and generates execution plan. Plan shows what will be created, modified, or destroyed. Apply executes the plan and updates state.

**3. What is Terraform state and why is it important?**
State tracks infrastructure resources managed by Terraform. It maps configuration to real-world resources, stores metadata, and enables drift detection. State must be stored remotely for teams.

**4. What is HCL and how does it differ from JSON?**
HCL (HashiCorp Configuration Language) is human-readable and supports comments, expressions, and first-class functions. JSON is data-only format. HCL supports variable interpolation and conditional expressions.

**5. Explain the difference between `terraform plan` and `terraform apply`.**
Plan shows what changes will be made without executing them. Apply executes the changes and updates state. Plan is for review, apply is for execution.

## State Management

**6. How do you handle state in a team environment?**
Use remote state backends (S3, GCS, Terraform Cloud) with state locking (DynamoDB). Separate state per environment. Never commit state to version control.

**7. What is state locking and why is it needed?**
State locking prevents concurrent modifications to state file. When one person runs apply, others cannot modify state simultaneously. Prevents conflicts and data loss.

**8. How do you migrate state between backends?**
Use `terraform state pull` and `terraform state push` commands. Or change backend configuration and run `terraform init`. Terraform will prompt for state migration.

**9. What is drift detection and how does it work?**
Drift detection identifies changes made outside Terraform. `terraform plan` compares configuration to state and remote infrastructure. Differences indicate drift that needs reconciliation.

**10. How do you import existing resources into Terraform?**
Use `terraform import` command with resource address and resource ID. Import adds resource to state but not configuration. You must write configuration to match imported resource.

## Modules and Reusability

**11. What are Terraform modules and when should you use them?**
Modules are reusable packages of Terraform configuration. Use them for common patterns, multi-environment deployments, and team standardization. Keep modules small and focused.

**12. How do you version Terraform modules?**
Use semantic versioning with Git tags. Reference modules by version in configuration. Test module changes before releasing new versions. Document breaking changes.

**13. What is the difference between a module and a workspace?**
Modules are reusable configuration packages. Workspaces are separate state files for same configuration. Use modules for sharing code, workspaces for environment separation.

**14. How do you handle secrets in Terraform?**
Mark sensitive variables with `sensitive = true`. Store secrets in Vault or cloud secrets managers. Use data sources to reference secrets. Never commit secrets to version control.

## Advanced Topics

**15. What are provisioners and when should you use them?**
Provisioners execute actions on resources during creation or destruction. Use sparingly for bootstrapping, configuration management, or cleanup. Prefer cloud-init or configuration management tools.

**16. How do you test Terraform code?**
Use Terratest for integration testing. Validate configuration with `terraform validate`. Run plan to preview changes. Use checkov and tfsec for security testing.

**17. What is the Terraform Cloud and when should you use it?**
Terraform Cloud provides remote state, collaboration features, and automation. Use for team workflows, policy enforcement, and cost estimation. Alternative to self-managed backends.

**18. How do you implement CI/CD for Terraform?**
Use GitHub Actions, GitLab CI, or Jenkins. Run validate, plan, and apply in stages. Require approval for production changes. Store state remotely and use lock files.

**19. What is the purpose of `terraform destroy`?**
Destroy removes all infrastructure managed by Terraform. Use for cleaning up test environments or when decommissioning. Be cautious in production. Review plan before destroying.

**20. How do you handle multi-cloud deployments?**
Configure multiple providers with different regions or accounts. Use modules to abstract cloud-specific details. Reference resources across providers. Plan carefully to avoid conflicts.
