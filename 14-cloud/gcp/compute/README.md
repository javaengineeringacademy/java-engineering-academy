# Google Compute Engine

## Overview

Google Compute Engine (GCE) provides scalable, high-performance virtual machines running in Google Cloud.

## Machine Types

| Type            | Use Case                    | Examples              |
|-----------------|-----------------------------|-----------------------|
| General Purpose | Balanced compute/memory     | E2, N2, N2D           |
| Compute Optimized| CPU-intensive workloads    | C2, C2D               |
| Memory Optimized| In-memory databases         | M1, M2               |
| Accelerator     | ML/AI workloads             | A2, G2               |
| Compute-optimized| High-performance computing | H3                    |

## Creating Instances

### gcloud CLI
```bash
# Create instance
gcloud compute instances create my-instance \
  --zone=us-central1-a \
  --machine-type=e2-medium \
  --image-family=debian-12 \
  --image-project=debian-cloud

# Create with multiple options
gcloud compute instances create my-instance \
  --zone=us-central1-a \
  --machine-type=n2-standard-4 \
  --network=default \
  --subnet=default \
  --address=external-ip \
  --metadata=startup-script='#!/bin/bash
apt update && apt install -y nginx' \
  --tags=http-server,https-server \
  --scopes=cloud-platform
```

### Terraform
```hcl
resource "google_compute_instance" "default" {
  name         = "my-instance"
  machine_type = "e2-medium"
  zone         = "us-central1-a"

  boot_disk {
    initialize_params {
      image = "debian-cloud/debian-12"
    }
  }

  network_interface {
    network = "default"
    access_config {}
  }
}
```

## Instance Templates

```bash
# Create instance template
gcloud compute instance-templates create my-template \
  --machine-type=e2-medium \
  --image-family=debian-12 \
  --image-project=debian-cloud

# Use template in instance group
gcloud compute instance-groups managed create my-group \
  --template=my-template \
  --size=3 \
  --zone=us-central1-a
```

## Instance Groups

### Managed Instance Groups (MIG)
```bash
# Create MIG
gcloud compute instance-groups managed create my-mig \
  --template=my-template \
  --size=3 \
  --zone=us-central1-a

# Set auto-scaling
gcloud compute instance-groups managed set-autoscaling my-mig \
  --zone=us-central1-a \
  --min-num-replicas=1 \
  --max-num-replicas=10 \
  --target-cpu-utilization=0.7

# Create named port
gcloud compute instance-groups managed set-named-ports my-mig \
  --named-ports=http:80 \
  --zone=us-central1-a
```

### Unmanaged Instance Groups
```bash
# Create unmanaged group
gcloud compute instance-groups unmanaged create my-unmanaged-group \
  --zone=us-central1-a

# Add instances
gcloud compute instance-groups unmanaged add-instances my-unmanaged-group \
  --instances=instance-1,instance-2 \
  --zone=us-central1-a
```

## Disk Options

| Type     | Use Case              | Performance |
|----------|-----------------------|-------------|
| pd-balanced | General purpose    | Medium      |
| pd-ssd   | High performance      | High        |
| pd-extreme | Mission-critical    | Highest     |
| local-ssd | Temporary storage   | Highest     |

```bash
# Create disk
gcloud compute disks create my-disk \
  --size=100GB \
  --type=pd-ssd \
  --zone=us-central1-a

# Attach disk
gcloud compute instances attach-disk my-instance \
  --disk=my-disk \
  --zone=us-central1-a
```

## Snapshots

```bash
# Create snapshot
gcloud compute snapshots create my-snapshot \
  --source-disk=my-disk \
  --zone=us-central1-a

# Create instance from snapshot
gcloud compute instances create my-instance \
  --zone=us-central1-a \
  --source-snapshot=my-snapshot
```

## Preemptible/Spot VMs

```bash
# Create spot VM
gcloud compute instances create my-spot-vm \
  --zone=us-central1-a \
  --machine-type=e2-medium \
  --provisioning-model=SPOT \
  --instance-termination-action=STOP

# Benefits:
# - Up to 91% discount
# - Up to 24 hours max runtime
# - Can be preempted at any time
```

## Shielded VMs

```bash
# Create shielded VM
gcloud compute instances create my-shielded-vm \
  --zone=us-central1-a \
  --shielded-secure-boot \
  --shielded-vtpm \
  --shielded-integrity-monitoring
```

## Serial Port Access

```bash
# Enable serial port
gcloud compute instances add-metadata my-instance \
  --zone=us-central1-a \
  --metadata=serial-port-enable=TRUE

# Connect via serial port
gcloud compute connect-tserial-port my-instance \
  --zone=us-central1-a
```

## Pricing

| VM Type     | Preemptible Discount | Committed Use Discount |
|-------------|----------------------|------------------------|
| Standard    | Up to 91%            | Up to 57%              |
| Preemptible | Up to 91%            | N/A                    |

## Best Practices

1. **Right-size instances** based on workload
2. **Use committed use discounts** for steady workloads
3. **Use spot VMs** for fault-tolerant workloads
4. **Implement auto-scaling** with MIGs
5. **Use shielded VMs** for security
6. **Enable deletion protection** for production
7. **Use instance templates** for consistency
8. **Monitor with Cloud Monitoring**
9. **Use startup scripts** for configuration
10. **Implement proper networking**
