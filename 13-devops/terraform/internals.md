# Terraform Internals

## State Management

Terraform state maps real infrastructure to configuration. The state file (terraform.tfstate) stores resource attributes, metadata, and dependencies. State is essential for planning: Terraform compares the desired configuration with the current state to determine changes. Without state, Terraform cannot identify which resources to create, update, or destroy.

State backends provide remote storage and locking. Supported backends include S3, GCS, Azure Blob, Consul, and Terraform Cloud. Remote backends enable team collaboration by storing state centrally. State locking (via DynamoDB for S3) prevents concurrent modifications that could cause conflicts. The state file contains sensitive data and should be encrypted at rest.

State operations include: state mv (move resources), state rm (remove resources), state show (display resource attributes), and state pull/push (remote state manipulation). The `terraform import` command brings existing resources under management. The `terraform refresh` command updates state to match actual infrastructure. State backup files (`.tfstate.backup`) are created before each modification.

The state file uses JSON format. Resource attributes are stored as key-value pairs. Dependencies are tracked as references to other resources. The `terraform state list` command shows all resources in state. The `terraform state show` command displays resource attributes. The `terraform state rm` command removes resources from state without destroying them.

## Provider Protocol

Providers are plugins that interact with external APIs. The provider protocol defines the interface between Terraform core and providers. Providers declare their schema (resource types, data sources, attributes) and implement CRUD operations. The gRPC-based protocol supports bidirectional streaming.

Provider authentication uses credentials configured via environment variables, config files, or instance profiles. The provider validates resource configurations and communicates with external APIs. Provider version constraints ensure compatibility. The provider cache in `.terraform/providers/` stores downloaded plugins to avoid repeated downloads.

Providers implement the Resource CRUD interface: Create, Read, Update, and Delete. The `terraform providers` command lists configured providers. Provider requirements specify version constraints. The `required_providers` block in configuration defines provider sources and versions. Provider configuration blocks supply credentials and settings.

Providers support import operations to bring existing resources under management. The `terraform import` command imports resources by ID. The `import` block in configuration defines import mappings. Providers implement the Importer interface for custom import logic. The `terraform plan -generate-config-out` flag generates configuration for imported resources.

## Plan/Apply Cycle

The plan phase computes the execution strategy. Terraform refreshes state (reads current infrastructure), compares with configuration, and generates a change set. The plan shows creates, updates, destroys, and in-place modifications. Plans are saved and can be applied later (terraform apply planfile).

The apply phase executes the plan. Resources are modified in dependency order. Terraform uses a directed acyclic graph (DAG) to determine execution order. Parallel execution is possible for independent resources. The -parallelism flag controls concurrency. Failed operations trigger rollback for resources that support it; otherwise, manual intervention is required.

The plan output includes: resource changes (create/update/destroy), attribute changes, and dependency ordering. The `terraform plan -out=planfile` saves the plan for later application. The `terraform apply -auto-approve` skips confirmation. The `terraform destroy` removes all managed resources. The `terraform validate` command checks configuration syntax.

The plan phase uses the refresh flag to update state. The `-refresh=false` flag skips refresh for faster planning. The `-target=resource` flag limits planning to specific resources. The `-replace=resource` flag forces replacement of specific resources. The `-compact-warnings` flag reduces plan output verbosity.

## Dependency Graph

Terraform builds a dependency graph from resource configurations. Explicit dependencies use the depends_on attribute. Implicit dependencies are inferred from resource references (e.g., referencing aws_instance.web.id in a security group rule). The graph is traversed topologically to determine execution order.

The graph is represented as an adjacency list. Cycle detection prevents circular dependencies. The terraform graph command outputs the graph in DOT format for visualization. Data sources are evaluated before resources. Modules introduce sub-graphs that are merged into the main graph. Providers are initialized before any resource operations.

The `terraform graph -type=plan` shows the planning graph. The `terraform graph -type=apply` shows the execution graph. Graph cycles cause errors and must be resolved. The `terraform providers schema` command shows provider resource dependencies. The `depends_on` attribute should be used sparingly, as it forces sequential execution.

The dependency graph supports resource targeting. The `-target=resource` flag limits operations to specific resources and their dependencies. The `-dependson=resource` flag adds explicit dependencies. The graph visualization helps identify dependency bottlenecks. The `terraform graph -type=plan` output can be rendered with Graphviz.

## HCL and Terraform Core

HashiCorp Configuration Language (HCL) is the configuration syntax. HCL supports variables, expressions, functions, and blocks. Terraform parses HCL into an abstract syntax tree (AST). The core engine evaluates expressions and resolves references. Functions (e.g., length, merge, lookup) transform data.

Variables are validated using type constraints and custom validation rules. Output values expose resource attributes. Locals define computed values within a module. The terraform console provides an interactive REPL for testing expressions. The terraform fmt command formats configuration files to a canonical style.

HCL supports conditional expressions (condition ? true_val : false_val), for expressions (for_each), and dynamic blocks. The `jsonencode()` and `yamlencode()` functions generate formatted strings. The `templatefile()` function renders templates with variable substitution. The `file()` function reads file contents. The `filebase64()` function reads files as base64.

HCL supports object types with `object({})` syntax. The `try()` function handles errors gracefully. The `can()` function checks if an expression evaluates successfully. The `type()` function returns the type of a value. The `merge()` function merges maps. The `lookup()` function retrieves map values with defaults.

## Module System

Modules encapsulate reusable configurations. A module is a directory with .tf files. Child modules are called from parent configurations. Module sources include local paths, Git repositories, and the Terraform Registry. Module versioning uses semantic versioning tags.

Module inputs are variables; outputs expose computed values. Module composition promotes reuse across teams. Private registries host internal modules with versioning and access control. The terraform init command downloads module dependencies. Module testing is supported by the terraform test framework using HCL-based test files.

Module sources support: local paths, GitHub repositories, generic Git repositories, Mercurial repositories, HTTP URLs, and S3 buckets. The `terraform init` command downloads and caches module sources. Module versioning follows semantic versioning. The `terraform get` command updates modules to the latest version.

Modules support variables and outputs for configuration and data exchange. The `variables.tf` file defines input variables. The `outputs.tf` file defines output values. The `versions.tf` file specifies provider and Terraform version constraints. The `terraform console` command evaluates module expressions.

## Provisioners and Lifecycle

Provisioners execute actions on resources during creation or destruction. Types include remote-exec (SSH commands), local-exec (local commands), and file (file transfer). Provisioners are a last resort; declarative alternatives (cloud-init, configuration management) are preferred.

Lifecycle rules control resource behavior. create_before_destroy ensures new resources exist before old ones are removed. prevent_destroy blocks accidental deletion. ignore_changes exempts attributes from plan comparisons. replace_triggered_by forces recreation based on other resource changes.

Provisioners run in order: creation provisioners before the resource is marked as created, destruction provisioners before the resource is destroyed. The `when = create` and `when = destroy` attributes control timing. Failed provisioners can set `on_failure = continue` to ignore errors. The `connection` block configures SSH or WinRM access.

The `lifecycle` block supports: create_before_destroy, prevent_destroy, ignore_changes, replace_triggered_by, and precondition/postcondition. Precondition blocks validate conditions before resource operations. Postcondition blocks validate conditions after resource operations. The `terraform validate` command checks lifecycle rules.

## Workspace Management

Workspaces isolate state files within the same configuration. The default workspace is always present. Additional workspaces provide environment separation (dev, staging, prod). Workspace variables can customize configuration per workspace.

Workspaces are useful for multi-tenant deployments and environment promotion. The terraform workspace select command switches context. State is isolated per workspace; resources in one workspace are invisible to others. For complex environments, separate directories or Terragrunt provide stronger isolation than workspaces.

The `terraform workspace list` command shows available workspaces. The `terraform workspace new` command creates a workspace. The `terraform workspace delete` command removes a workspace. Workspace-specific variables can be defined using `terraform.workspace` interpolation. The `terraform workspace select` command switches the active workspace.

Workspaces share the same configuration directory. State is isolated per workspace. Workspace variables can be used in configuration. The `terraform.workspace` variable returns the current workspace name. The `terraform workspace show` command displays the current workspace. Workspaces support remote state backends for team collaboration.

## Testing and Validation

Terraform supports testing through the `terraform test` command. Test files use HCL syntax and define test cases. Test cases include: plan assertions, apply assertions, and destroy assertions. The `terraform test` command executes tests and reports results.

Test files use `run` blocks to define test cases. The `plan` command within test cases generates plans. The `apply` command executes configurations. The `assert` block validates conditions. The `expect_failures` block validates expected failures. Test fixtures provide test data.

The `terraform validate` command checks configuration syntax and internal consistency. The `terraform fmt -check` command verifies formatting. The `terraform graph` command visualizes dependencies. The `terraform console` evaluates expressions interactively. The `terraform output` command shows output values.

Testing best practices include: writing tests for modules, using test fixtures for test data, validating resource attributes after apply, and testing error conditions. The `terraform test` command supports parallel execution. Test results are reported in human-readable format.
