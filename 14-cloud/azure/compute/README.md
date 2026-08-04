# Azure Virtual Machines

## Overview

Azure Virtual Machines provides scalable computing capacity in Azure.

## VM Sizes

| Type            | Use Case                    | Examples              |
|-----------------|-----------------------------|-----------------------|
| General Purpose | Balanced compute/memory     | B, D, Dv2             |
| Compute Optimized| CPU-intensive workloads    | F, Fs                 |
| Memory Optimized| In-memory databases         | E, Ev2, M             |
| Storage Optimized| High I/O workloads         | L, Ls                 |
| GPU             | ML/AI workloads             | N, NC, ND             |
| High Performance| HPC workloads               | H, HB                 |

## Creating VMs

### Azure CLI
```bash
# Create VM
az vm create \
  --resource-group myResourceGroup \
  --name myVM \
  --image Ubuntu2204 \
  --size Standard_DS1_v2 \
  --admin-username azureuser \
  --ssh-key-values ~/.ssh/id_rsa.pub

# Create with options
az vm create \
  --resource-group myResourceGroup \
  --name myVM \
  --image Ubuntu2204 \
  --size Standard_DS2_v2 \
  --vnet-name myVNet \
  --subnet mySubnet \
  --nsg myNSG \
  --public-ip-sku Standard \
  --admin-username azureuser \
  --generate-ssh-keys
```

### ARM Template
```json
{
  "type": "Microsoft.Compute/virtualMachines",
  "apiVersion": "2023-03-01",
  "name": "myVM",
  "location": "eastus",
  "properties": {
    "hardwareProfile": {
      "vmSize": "Standard_DS1_v2"
    },
    "osProfile": {
      "computerName": "myVM",
      "adminUsername": "azureuser"
    },
    "storageProfile": {
      "imageReference": {
        "publisher": "Canonical",
        "offer": "UbuntuServer",
        "sku": "22_04-lts",
        "version": "latest"
      }
    },
    "networkProfile": {
      "networkInterfaces": [
        {
          "id": "[resourceId('Microsoft.Network/networkInterfaces', 'myNic')]"
        }
      ]
    }
  }
}
```

## Availability Sets

```bash
# Create availability set
az vm availability-set create \
  --resource-group myResourceGroup \
  --name myAvailSet \
  --platform-fault-domain-count 2 \
  --platform-update-domain-count 5

# Create VM in availability set
az vm create \
  --resource-group myResourceGroup \
  --name myVM \
  --availability-set myAvailSet \
  --image Ubuntu2204 \
  --size Standard_DS1_v2
```

## Virtual Machine Scale Sets (VMSS)

```bash
# Create VMSS
az vmss create \
  --resource-group myResourceGroup \
  --name myVMSS \
  --image Ubuntu2204 \
  --instance-count 3 \
  --admin-username azureuser \
  --generate-ssh-keys

# Update VMSS
az vmss update \
  --resource-group myResourceGroup \
  --name myVMSS \
  --instance-count 5
```

### VMSS Features
- **Auto-scaling**: Scale based on metrics
- **Rolling upgrades**: Zero-downtime updates
- **Health monitoring**: Automatic repair
- **Uniform or flexible** orchestration

## Disks

### Managed Disks
```bash
# Create disk
az disk create \
  --resource-group myResourceGroup \
  --name myDisk \
  --size-gb 128 \
  --sku Premium_LRS

# Attach disk
az vm disk attach \
  --resource-group myResourceGroup \
  --vm-name myVM \
  --name myDisk
```

### Disk Types
| Type        | Use Case              | IOPS      |
|-------------|-----------------------|-----------|
| Ultra       | SAP, Oracle           | 160,000   |
| Premium SSD | Production workloads  | 20,000    |
| Standard SSD| Web servers           | 6,000     |
| Standard HDD| Backup, dev/test      | 500       |

## Snapshots

```bash
# Create snapshot
az snapshot create \
  --resource-group myResourceGroup \
  --name mySnapshot \
  --source myVM

# Create VM from snapshot
az vm create \
  --resource-group myResourceGroup \
  --name myRestoredVM \
  --image mySnapshot
```

## Images

```bash
# Create image from VM
az image create \
  --resource-group myResourceGroup \
  --name myImage \
  --source myVM

# Create VM from image
az vm create \
  --resource-group myResourceGroup \
  --name myVM \
  --image myImage
```

## Networking

### Network Security Groups
```bash
# Create NSG
az network nsg create \
  --resource-group myResourceGroup \
  --name myNSG

# Add rule
az network nsg rule create \
  --resource-group myResourceGroup \
  --nsg-name myNSG \
  --name AllowHTTP \
  --priority 100 \
  --destination-port-ranges 80 \
  --access Allow \
  --protocol Tcp \
  --direction Inbound
```

### Public IP
```bash
# Create public IP
az network public-ip create \
  --resource-group myResourceGroup \
  --name myPublicIP \
  --sku Standard
```

## Extensions

```bash
# Install custom script extension
az vm extension set \
  --resource-group myResourceGroup \
  --vm-name myVM \
  --name CustomScript \
  --publisher Microsoft.Azure.Extensions \
  --settings '{"commandToExecute": "apt-get update && apt-get install -y nginx"}'
```

## Monitoring

```bash
# Enable boot diagnostics
az vm boot-diagnostics enable \
  --resource-group myResourceGroup \
  --name myVM

# Get VM metrics
az monitor metrics list \
  --resource myVM \
  --resource-group myResourceGroup \
  --metric "Percentage CPU"
```

## Cost Optimization

- **Use Reserved Instances** for steady workloads
- **Use Spot VMs** for fault-tolerant workloads
- **Right-size VMs** based on usage
- **Use auto-scaling** with VMSS
- **Delete unused resources**

## Best Practices

1. **Use availability sets** for HA
2. **Use VMSS** for scaling
3. **Implement proper networking** with NSGs
4. **Use managed disks**
5. **Enable boot diagnostics**
6. **Implement proper monitoring**
7. **Use extensions** for configuration
8. **Regular backups**
9. **Implement proper security**
10. **Monitor costs**
