# Azure Performance Optimization

## Overview

Optimizing Azure performance involves selecting appropriate VM sizes, configuring App Service plans, tuning Cosmos DB throughput, and leveraging caching and CDN.

## VM Size Selection

### Series Overview

| Series | Use Case | Max vCPUs | Max Memory |
|---|---|---|---|
| B | Dev/test, burstable | 32 | 128 GB |
| D | General purpose | 128 | 504 GB |
| E | Memory optimized | 128 | 672 GB |
| F | Compute optimized | 128 | 326 GB |
| M | Storage optimized | 128 | 3.8 TB |
| N | GPU enabled | 96 | 1.9 TB |

### Selection Criteria

- Measure CPU utilization and select a size with 20-30% headroom
- Monitor memory usage for at least 7 days before sizing
- Consider burstable B-series for dev/test workloads
- Use reserved instances for predictable, steady-state workloads

## App Service Plans

### Tier Comparison

| Tier | Features | Use Case |
|---|---|---|
| Free | Shared infrastructure, 60 min/day compute | Prototyping |
| Basic | Dedicated VMs, manual scale | Low-traffic apps |
| Standard | Auto-scale, deployment slots | Production apps |
| Premium | Enhanced performance, more slots | High-traffic apps |
| Isolated | Dedicated environment, network isolation | Enterprise apps |

### Optimization Tips

- Use deployment slots for zero-downtime deployments
- Enable HTTP/2 and WebSocket support
- Configure appropriate worker counts based on load
- Enable autoscale rules based on CPU or HTTP queue length

## Cosmos DB Performance

### RU Calculation

Request Units (RU) measure the cost of operations:

| Operation | Approximate Cost |
|---|---|
| Read (1 KB) | 1 RU |
| Write (1 KB) | 5 RU |
| Query (1 KB) | Variable based on index |

### Optimization Strategies

- Create composite indexes for common query patterns
- Use point reads for single-item lookups
- Avoid cross-partition queries when possible
- Monitor and adjust provisioned RU/s based on usage patterns
- Use autoscale for variable workloads

## Storage Performance

### Blob Storage

- Use block blobs for large files (up to 190.7 TB)
- Use page blobs for random read/write (VM disks)
- Enable blob caching for frequently accessed data
- Choose hot tier for active data, cool for archival

### Managed Disks

| Type | IOPS | Throughput | Use Case |
|---|---|---|---|
| Ultra | 160,000 | 4,000 MB/s | SAP HANA, databases |
| Premium SSD v2 | 80,000 | 1,200 MB/s | Enterprise workloads |
| Premium SSD | 20,000 | 900 MB/s | Production servers |
| Standard SSD | 6,000 | 750 MB/s | Web servers |
| Standard HDD | 2,000 | 500 MB/s | Backups, archives |

## Caching

### Azure Cache for Redis

- In-memory data store for sub-millisecond response times
- Supports data caching, session management, and message brokering
- Available tiers: Basic, Standard, Premium, Enterprise
- Use for frequently accessed data to reduce database load

### CDN Caching

- Cache static content at edge locations
- Configure cache rules for custom paths and file types
- Use origin shield to reduce backend load
- Set appropriate TTLs based on content freshness needs

## Network Performance

- Use Azure Accelerated Networking for low-latency VM connections
- Deploy resources in the same region and availability zone
- Use Private Link for secure, low-latency connectivity
- Enable ExpressRoute for hybrid connectivity scenarios

## Monitoring Performance

- Enable VM Insights for CPU, memory, and disk metrics
- Use Application Insights for application performance monitoring
- Set up alerts for key performance indicators
- Review performance recommendations in Azure Advisor
