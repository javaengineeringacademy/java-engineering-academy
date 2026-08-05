# SVN to Git/BitBucket Migration

## Overview

Subversion (SVN) served as a standard version control system for years, but Git's distributed model, branching capabilities, and ecosystem have made it the modern standard. This playbook covers migrating from SVN to Git with BitBucket as the hosting platform.

## Migration Strategy

### Repository Assessment

Inventory all SVN repositories, their structure, and usage patterns. Identify trunk, branches, and tags. Assess the size of history, number of contributors, and any SVN-specific features in use.

### Tool Selection

Use svn2git or similar tools to migrate SVN repositories to Git. These tools preserve history, branches, and tags while converting the repository structure.

### Migration Execution

Migrate repositories in order of complexity and dependency. Start with simple repositories to validate the process before migrating more complex ones.

## Implementation Patterns

### History Conversion

svn2git preserves commit history, authors, and timestamps. Map SVN usernames to Git author names and email addresses using an authors file.

Preserve commit messages exactly to maintain traceability. SVN revision numbers can be referenced in Git commit messages for cross-reference.

### Branch and Tag Conversion

SVN's branches directory maps to Git branches. SVN's tags directory maps to Git tags. Ensure the mapping correctly handles the different conventions.

SVN allows commits to tags, which Git does not support. Handle any tag modifications by recreating them as branches or ignoring the modifications if they are not significant.

### Repository Structure

SVN repositories may have non-standard structures. Flatten the structure to Git conventions where possible. Preserve the directory layout within the repository root.

### Large Files

SVN handles large files differently than Git. Identify large files and consider Git LFS (Large File Storage) for binary assets that should not be stored in Git history.

## BitBucket Setup

### Repository Creation

Create BitBucket repositories with appropriate settings:

- Access controls and branch permissions
- Webhook configurations for CI/CD
- Pull request settings and review requirements
- Integration with issue tracking

### Access Control

Map SVN access controls to BitBucket permissions. SVN path-based access becomes BitBucket repository and branch permissions.

### CI/CD Integration

Update CI/CD pipelines from SVN to Git triggers. Configure BitBucket Pipelines or external CI systems to build on Git commits and pull requests.

## Key Differences

### Workflow

SVN uses centralized checkout-commit-update workflow. Git uses local commit-branch-merge workflow. Train teams on Git branching, merging, and rebasing.

### Branching

SVN branches are directory copies. Git branches are lightweight pointers. Encourage feature branching and pull request workflows.

### Merging

SVN merging is manual and error-prone. Git merging is automatic for most cases. Train teams on conflict resolution and merge strategies.

### Offline Work

SVN requires network access for most operations. Git operates entirely locally. This enables faster development cycles and offline work.

## Lessons Learned

### Train Teams

Git has a different workflow than SVN. Provide training on Git concepts, branching strategies, and BitBucket features before migration.

### Migrate Incrementally

Migrate one repository at a time, allowing teams to adapt to Git before migrating additional repositories. Prioritize active repositories for early migration.

### Preserve History

History provides context and audit trails. Ensure the migration preserves complete history, including branches, tags, and commit messages.

### Update Tooling

SVN-specific tooling (hooks, scripts, CI integrations) must be updated for Git. Review and update all tooling that interacts with version control.
