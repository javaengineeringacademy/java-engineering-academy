# Azure Troubleshooting Guide

## Overview

This guide addresses common Azure issues and their resolutions, organized by service category.

## Virtual Machine Issues

### VM Not Starting

**Symptoms:** VM stuck in "Creating" or "Updating" state.

**Resolutions:**
- Check Activity Log for errors
- Verify quota limits in the subscription
- Ensure the VM size is available in the target region
- Validate disk and network configuration
- Try deallocating and starting the VM again

### SSH/RDP Connection Failures

**Symptoms:** Cannot connect to VM via SSH or RDP.

**Resolutions:**
- Verify NSG rules allow inbound traffic on port 22 (SSH) or 3389 (RDP)
- Use Serial Console to access the VM
- Check boot diagnostics for startup errors
- Verify the public IP address is assigned
- Reset the SSH key or VM password

### High CPU/Memory Usage

**Symptoms:** VM performance degraded, applications unresponsive.

**Resolutions:**
- Enable VM Insights for detailed metrics
- Check for runaway processes via Serial Console
- Right-size the VM based on actual utilization
- Consider upgrading to a larger VM size
- Implement autoscale with VMSS

## Storage Issues

### Blob Storage Access Denied

**Symptoms:** 403 Forbidden when accessing blobs.

**Resolutions:**
- Verify the access key or SAS token is valid
- Check storage account firewall settings
- Ensure the managed identity has proper RBAC assignment
- Verify the container ACL allows the operation

### Storage Account Throttling

**Symptoms:** High latency, 503 errors.

**Resolutions:**
- Check transaction rate against account limits
- Distribute access across multiple storage accounts
- Use Premium storage for high-performance needs
- Implement exponential backoff for retries

## Networking Issues

### DNS Resolution Failures

**Symptoms:** Cannot resolve private DNS zones.

**Resolutions:**
- Verify DNS server settings on the VM
- Check private DNS zone links to virtual networks
- Ensure DNS records are correctly configured
- Verify NSG rules allow DNS traffic (port 53)

### VPN Gateway Connection Drops

**Symptoms:** Intermittent connectivity to on-premises.

**Resolutions:**
- Check VPN gateway resource health
- Verify IPsec/IKE policy configuration
- Monitor gateway metrics for packet loss
- Review on-premises firewall logs
- Consider upgrading gateway SKU

## Azure Kubernetes Service Issues

### Pods Stuck in Pending State

**Symptoms:** Pods not scheduling to nodes.

**Resolutions:**
- Check node resource availability (CPU, memory)
- Verify pod resource requests are within node capacity
- Check for node taints and pod tolerations
- Ensure images are pulling successfully
- Review cluster autoscaler status

### Service Unreachable

**Symptoms:** Cannot access service via ClusterIP or LoadBalancer.

**Resolutions:**
- Verify the service selector matches pod labels
- Check endpoint availability: `kubectl get endpoints`
- Validate network policies
- Check load balancer provisioning status
- Review kube-proxy logs

## Cosmos DB Issues

### High Request Unit Consumption

**Symptoms:** 429 Too Many Requests errors.

**Resolutions:**
- Monitor RU consumption in Azure Portal
- Optimize queries to use fewer RU
- Create appropriate indexes for query patterns
- Consider switching to autoscale mode
- Increase provisioned throughput if needed

### Cross-Partition Query Performance

**Symptoms:** Slow queries across partitions.

**Resolutions:**
- Design partition keys for query locality
- Avoid cross-partition queries where possible
- Use composite indexes for common query patterns
- Implement Change Feed for materialized views

## App Service Issues

### Application Startup Failures

**Symptoms:** HTTP 500 errors on application start.

**Resolutions:**
- Check Application Logs in the App Service
- Verify connection strings and app settings
- Review the startup command configuration
- Check platform version compatibility
- Enable detailed error messages

### Deployment Slot Swap Failures

**Symptoms:** Swap operation fails or causes downtime.

**Resolutions:**
- Verify all application settings are slot-sticky where needed
- Check health endpoint configuration
- Review swap operation in Activity Log
- Ensure deployment slot warm-up is sufficient
- Test with auto-swap disabled first

## Key Vault Issues

### Access Denied to Key Vault

**Symptoms:** 403 Forbidden when accessing secrets.

**Resolutions:**
- Verify the access policy or RBAC assignment
- Check Key Vault firewall rules
- Ensure the managed identity or service principal has access
- Verify the secret name and version
- Review Key Vault diagnostic logs

### Secret Not Found

**Symptoms:** Secret retrieval returns 404.

**Resolutions:**
- Check the exact secret name (case-sensitive)
- Verify the secret has not been soft-deleted
- Check if the secret has expired
- Ensure the caller has Get permission

## General Troubleshooting Steps

1. Check Azure Service Health for platform issues
2. Review Activity Log for the resource or resource group
3. Enable and check diagnostic logs
4. Verify resource quotas and limits
5. Test connectivity and DNS resolution
6. Check application logs and error messages
7. Review resource health status
8. Consult Azure Advisor recommendations
9. Open a support ticket if the issue persists
