# Azure Interview Questions

## Overview

This guide covers common Azure interview questions organized by topic, with concise answers to demonstrate foundational and intermediate knowledge.

## 1. What is the difference between IaaS, PaaS, and SaaS?

IaaS provides virtualized computing resources (VMs, networks, storage). PaaS provides a platform for developing and deploying applications without managing infrastructure. SaaS delivers complete applications over the internet. Examples: VMs (IaaS), App Service (PaaS), Microsoft 365 (SaaS).

## 2. What is Azure Resource Manager (ARM)?

ARM is the deployment and management service for Azure. It provides a consistent API layer for creating, updating, and deleting resources. ARM handles authentication, resource locking, policy enforcement, and dependency resolution.

## 3. Explain Azure Availability Sets vs Availability Zones.

Availability Sets protect against hardware failures within a single data center using fault and update domains. Availability Zones are physically separate data centers within a region, protecting against entire data center failures. Availability Zones offer higher availability.

## 4. What is the difference between Azure Blob Storage tiers?

Hot tier is for frequently accessed data with higher storage costs and lower access costs. Cool tier is for data accessed infrequently with lower storage costs and higher access costs. Archive tier is for long-term retention with the lowest storage costs but highest retrieval latency.

## 5. How does Azure Cosmos DB achieve global distribution?

Cosmos DB replicates data across Azure regions with configurable consistency levels. It supports multi-master writes, automatic failover, and conflict resolution. Data is partitioned and replicated at the infrastructure level.

## 6. What is the difference between Azure Functions and Azure Logic Apps?

Azure Functions is code-based, event-driven serverless compute. Logic Apps is a no-code/low-code workflow automation platform. Functions is better for custom logic and code flexibility. Logic Apps is better for integrating services with visual designers.

## 7. Explain Azure Virtual Network peering.

VNet peering connects two virtual networks through the Azure backbone. Peered networks can communicate using private IP addresses. Global peering connects VNets across regions. Peering is non-transitive by default.

## 8. What is Azure Active Directory Entra ID?

Azure AD is Microsoft's cloud-based identity and access management service. It provides authentication, SSO, conditional access, and RBAC across Azure, Microsoft 365, and thousands of SaaS applications.

## 9. How does Azure Load Balancer differ from Application Gateway?

Load Balancer operates at Layer 4 (TCP/UDP) for high-performance, low-latency load balancing. Application Gateway operates at Layer 7 (HTTP/HTTPS) with features like SSL offloading, URL routing, and WAF integration.

## 10. What are Azure Management Groups?

Management Groups provide governance scope above subscriptions. They organize subscriptions into containers for applying Azure Policy and RBAC. Up to six levels of depth are supported, enabling enterprise-wide governance.

## 11. Explain Azure DevOps vs GitHub Actions for CI/CD.

Azure DevOps provides a full DevOps platform with Repos, Pipelines, Boards, Artifacts, and Test Plans. GitHub Actions is a CI/CD platform integrated with GitHub repositories. Azure DevOps supports more enterprise features out of the box.

## 12. What is Azure Policy?

Azure Policy enforces organizational standards and compliance rules on Azure resources. Policies can deny non-compliant resources, audit compliance, or automatically deploy required configurations. Policy initiatives group related policies.

## 13. How does Azure handle data encryption?

Azure encrypts data at rest with AES-256 by default. Data in transit is encrypted with TLS 1.2+. Customers can use customer-managed keys stored in Key Vault. Double encryption is available for compliance requirements.

## 14. What is the difference between Azure SQL Database and Azure SQL Managed Instance?

SQL Database is a fully managed single database service. Managed Instance provides near 100% SQL Server compatibility with instance-level features like SQL Agent, Service Broker, and linked servers. Managed Instance supports cross-database queries.

## 15. Explain Azure Site Recovery.

Azure Site Recovery replicates VMs from a primary region to a secondary region for disaster recovery. It supports automatic failover, planned failovers, and test failovers. RPO is based on replication frequency (30 seconds to 15 minutes).
