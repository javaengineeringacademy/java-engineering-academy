## Azure SQL Database

Fully managed relational database service based on SQL Server with built-in intelligence and security.

## Overview

Azure SQL Database is a fully managed PaaS database that handles patching, backups, and maintenance. It includes features like intelligent performance, advanced security, and business continuity.

## Why It Matters

- Fully managed database service
- Built-in high availability and disaster recovery
- Automatic tuning and optimization
- Advanced security features
- Elastic pools for cost optimization

## Key Concepts

- **Single Database**: Independent database
- **Elastic Pool**: Shared resources across databases
- **Serverless**: Auto-pause and auto-resume
- **Hyperscale**: Massive scale storage tier
- **Geo-Replication**: Cross-region redundancy
- **DTU vs vCore**: Purchasing models

## Core Topics

- Database provisioning and configuration
- Connection management with ADO.NET and EF Core
- Performance tuning and monitoring
- Security features (TDE, auditing, threat detection)
- Backup and restore
- Geo-replication and failover groups
- Elastic pools for multi-tenant apps

## Best Practices

- Use connection pooling for performance
- Enable Transparent Data Encryption
- Configure auditing for compliance
- Use elastic pools for multi-tenant scenarios
- Implement geo-replication for DR

## Hands-on Labs

- Create an Azure SQL Database
- Connect from .NET using EF Core
- Configure geo-replication
- Implement elastic pools

## Interview Questions

1. What is the difference between DTU and vCore models?
2. How does geo-replication work?
3. When should you use elastic pools?

## References

- https://learn.microsoft.com/azure/azure-sql/
- https://learn.microsoft.com/azure/azure-sql/database/
- https://learn.microsoft.com/azure/azure-sql/database/connect-dotnet-core
