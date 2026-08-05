# Terraform Architecture

## HCL Parser

HashiCorp Configuration Language (HCL) is parsed into an Abstract Syntax Tree (AST).

Parsing stages:

1. **Lexing**: Converts source text into tokens
2. **Parsing**: Builds AST from tokens
3. **Evaluation**: Resolves expressions and variables
4. **Validation**: Checks syntax and semantic rules

HCL supports:

- Variable interpolation: `"${var.name}"`
- First-class expressions: `var.enabled ? 1 : 0`
- Function calls: `file("path.txt")`
- Conditional expressions: `condition ? true_val : false_val`
- For expressions: `[for s in var.list : upper(s)]`

## Provider Protocol

Providers are plugins that interact with external APIs.

```
Terraform Core <-> gRPC <-> Provider Plugin
```

Provider responsibilities:

- Resource CRUD operations
- Data source reads
- Schema definition and validation
- Authentication handling
- Import functionality
- State management

Provider lifecycle:

1. Discovery and installation
2. Schema negotiation
3. Configuration validation
4. Resource planning
5. Resource execution

## State Management

State tracks infrastructure resources managed by Terraform.

State file structure:

```json
{
    "version": 4,
    "terraform_version": "1.5.0",
    "resources": [
        {
            "mode": "managed",
            "type": "aws_instance",
            "name": "web",
            "provider": "provider[\"registry.terraform.io/hashicorp/aws\"]",
            "instances": [
                {
                    "attributes": { ... }
                }
            ]
        }
    ]
}
```

State backends:

- **Local**: Default, stored on filesystem
- **Remote**: S3, GCS, Azure Blob, Terraform Cloud
- **Locking**: Prevents concurrent modifications
- **Encryption**: Protects sensitive data

## Plan/Apply Engine

The plan/apply cycle is Terraform's core workflow:

**Plan Phase:**

1. Read current state
2. Read configuration files
3. Read remote infrastructure state
4. Compare configuration to state
5. Generate execution plan
6. Present changes for review

**Apply Phase:**

1. Read execution plan
2. Execute operations in dependency order
3. Handle errors and retries
4. Update state file
5. Output results

Operations:

- **Create**: Add new resource
- **Update**: Modify existing resource
- **Delete**: Remove resource
- **Destroy**: Remove all resources
- **Read**: Refresh state from remote

## Dependency Graph

Terraform builds a directed acyclic graph (DAG) of resource dependencies.

```hcl
resource "aws_instance" "web" {
    ami           = var.ami_id
    instance_type = "t2.micro"
}

resource "aws_eip" "web" {
    instance = aws_instance.web.id
}
```

Graph resolution:

- Explicit dependencies: `depends_on`
- Implicit dependencies: Resource references
- Data source dependencies
- Module dependencies
- Provider dependencies

## Module System

Modules encapsulate infrastructure components.

```
module
  ├── main.tf
  ├── variables.tf
  ├── outputs.tf
  └── providers.tf
```

Module features:

- Reusability across projects
- Version pinning
- Input/output abstraction
- Provider configuration
- Testing support
