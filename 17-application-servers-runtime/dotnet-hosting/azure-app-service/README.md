# Azure App Service

## Overview

Azure App Service is a fully managed platform for building, deploying, and scaling web apps, RESTful APIs, and mobile backends. It provides infrastructure management, auto-scaling, and integrated DevOps capabilities.

## App Service Plans

App Service plans define the compute resources for applications. Options include Free/Shared for development, and Basic/Standard/Premium/Isolated for production workloads with dedicated resources.

## Deployment Options

Azure App Service supports multiple deployment methods: Azure DevOps pipelines, GitHub Actions, ZIP deploy, FTP, and Docker containers. Deployment slots enable zero-downtime deployments.

## Auto-Scaling

Auto-scaling adjusts instance count based on CPU, memory, or custom metrics. Rules can scale out during peak hours and scale in during low-traffic periods to optimize costs.

## Networking

App Service provides VNet integration, private endpoints, and hybrid connections for secure network access. Custom domains and managed SSL certificates are configured through the portal.

## Configuration

Application settings configure through the Azure Portal or ARM templates. Connection strings, environment variables, and feature flags are managed separately from application code.

## Monitoring

Application Insights integration provides performance monitoring, error tracking, and availability testing. Logs stream in real-time and archive to storage accounts for analysis.

## Security

Managed identities eliminate credential management for Azure resource access. Authentication and authorization integrate with Azure AD, social providers, and custom identity providers.

## Migration

Migrating to App Service involves evaluating dependencies, configuring application settings, and testing in deployment slots. The Migration Assistant tool helps assess application compatibility.
