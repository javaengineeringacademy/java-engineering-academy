# Jenkins to GitHub Actions Migration

## Overview

Jenkins has been the standard for CI/CD automation, but its maintenance burden, plugin ecosystem complexity, and infrastructure requirements have driven organizations to migrate to GitHub Actions. This playbook covers migrating from Jenkins to GitHub Actions.

## Migration Strategy

### Pipeline Assessment

Inventory all Jenkins jobs, their configurations, triggers, and dependencies. Identify shared libraries, custom plugins, and credential usage. The assessment produces a complete picture of the migration scope.

### Pipeline Design

Design GitHub Actions workflows that replicate Jenkins pipeline functionality. Map Jenkins stages to GitHub Actions jobs, steps, and actions. Identify opportunities to simplify or improve the pipeline during migration.

### Incremental Migration

Migrate one pipeline at a time, starting with simple, low-risk pipelines. Validate functionality and build team capability before migrating complex pipelines.

## Implementation Patterns

### Jenkinsfile to Workflow YAML

Jenkins declarative pipelines map to GitHub Actions workflow YAML files. Key mappings include:

- stages become jobs
- steps become actions or run commands
- environment variables map to env context
- credentials map to secrets
- post actions map to always, success, or failure conditions

### Plugin Replacement

Jenkins plugins provide functionality that GitHub Actions handles differently:

- Credentials plugin becomes GitHub Secrets
- Parameterized builds become workflow_dispatch inputs
- Shared libraries become reusable workflows or composite actions
- Build triggers become event-based triggers (push, pull_request, schedule)

### Agent to Runner

Jenkins agents (nodes) become GitHub-hosted or self-hosted runners. GitHub-hosted runners provide pre-built environments for common platforms. Self-hosted runners provide custom environments.

Map Jenkins agent labels to runner labels for workflow routing. Ensure self-hosted runners have the necessary tools and dependencies.

### Workspace Management

Jenkins uses workspaces on agents. GitHub Actions uses the runner's filesystem. Each job gets a fresh environment by default. Use actions/cache or artifacts to persist data between jobs.

## Key Differences

### Execution Model

Jenkins maintains persistent master and agent infrastructure. GitHub Actions is serverless, with runners provisioned on demand. This eliminates infrastructure management but may affect execution time for frequent builds.

### Marketplace

Jenkins uses plugins for extensibility. GitHub Actions uses the Marketplace for pre-built actions. The Marketplace provides simpler integration but may have fewer options for specialized use cases.

### Secrets Management

Jenkins stores credentials in the master configuration. GitHub Actions stores secrets at the repository, organization, or environment level. Secrets are encrypted and masked in logs.

### Concurrency

Jenkins manages build queues and agent assignment. GitHub Actions provides concurrency controls at the workflow level, enabling cancellation of in-progress runs.

## Lessons Learned

### Start with Simple Pipelines

Begin with straightforward build-and-test pipelines to learn GitHub Actions patterns. Complex pipelines with many dependencies should be migrated after establishing patterns.

### Use Reusable Workflows

GitHub Actions reusable workflows reduce duplication across similar pipelines. Extract common patterns into reusable workflows that can be called from multiple pipelines.

### Leverage Marketplace Actions

The GitHub Actions Marketplace provides pre-built actions for common tasks. Use existing actions rather than writing custom shell scripts when possible.

### Implement Environments

GitHub Actions environments provide deployment protection, secrets scoping, and environment-specific configuration. Use environments to manage deployments to different stages.

### Monitor Costs

GitHub Actions provides limited free minutes for public and private repositories. Monitor usage and consider self-hosted runners for high-volume pipelines to control costs.
