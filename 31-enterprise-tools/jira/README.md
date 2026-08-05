# Jira - Project Tracking and Agile Boards

## Overview

Jira is Atlassian's project management tool designed for agile software development. It supports Scrum and Kanban workflows, enabling teams to plan, track, and ship software efficiently. Jira provides visibility into work items, sprint progress, and release readiness.

## Why It Matters

- Provides a single source of truth for all work items and their status
- Enables agile ceremonies through boards, backlogs, and sprint planning
- Integrates with development tools for traceability from commit to deployment
- Offers reporting and metrics to track team velocity and cycle time
- Enforces workflows and approval processes for governance

## Key Concepts

- **Issue Types**: Stories, tasks, bugs, epics, and subtasks represent different work items
- **Board**: Visual representation of work flowing through statuses (Kanban or Scrum)
- **Sprint**: Time-boxed iteration for completing planned work
- **Backlog**: Prioritized list of work items not yet assigned to a sprint
- **Workflow**: State machine defining how issues transition between statuses
- **Epic**: Large body of work broken down into smaller stories

## Core Topics

### Agile Boards
- Kanban boards for continuous flow
- Scrum boards for sprint-based development
- Swimlanes for organizing work by team, priority, or component

### Workflows and Automation
- Custom workflows with conditions, validators, and post functions
- Automation rules for transitions, notifications, and field updates
- Workflow schemes for mapping workflows to projects

### Reporting and Metrics
- Velocity charts for sprint planning
- Burndown and burnup charts for tracking progress
- Cumulative flow diagrams for identifying bottlenecks

### Integration and Extensibility
- REST API for programmatic access
- Webhooks for event-driven integrations
- Marketplace apps for extended functionality

## Best Practices

1. Keep issue types simple; use epics and subtasks for hierarchy
2. Define clear workflow states with explicit entry and exit criteria
3. Use automation to reduce manual status updates
4. Limit work in progress on Kanban boards to improve flow
5. Regularly groom the backlog and remove stale items
6. Use labels and components for cross-project categorization

## Hands-on Labs

1. **Scrum Board Setup**: Create a project, configure a scrum board, and run a sprint
2. **Custom Workflow**: Design a workflow with approval stages and automated transitions
3. **Automation Rules**: Build rules that auto-assign issues and send notifications
4. **JQL Queries**: Write advanced queries to filter and analyze work items
5. **Dashboard Creation**: Build a custom dashboard with gadgets for team metrics

## Interview Questions

1. What is the difference between a Kanban board and a Scrum board in Jira?
2. How do you handle dependencies between issues in Jira?
3. Explain the purpose of workflow validators and conditions
4. How can Jira be integrated with a CI/CD pipeline?
5. What metrics would you track to measure team performance in Jira?
6. Describe a use case for Jira automation rules

## References

- Jira Software Documentation: https://support.atlassian.com/jira-software/
- Jira Administration Guide: https://support.atlassian.com/jira-software/
- Jira REST API: https://developer.atlassian.com/cloud/jira/platform/rest/v3/
- Agile Practice Guide: https://www.atlassian.com/agile
