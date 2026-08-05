# AlmaLinux

## Overview

AlmaLinux is a community-owned and governed, CentOS-compatible Linux distribution. It is an enterprise-grade server operating system that is binary-compatible with RHEL, maintained by the AlmaLinux OS Foundation.

## Versions

| Version | RHEL Based | Status |
|---------|------------|--------|
| AlmaLinux 9 | RHEL 9 | Active |
| AlmaLinux 8 | RHEL 8 | Active |

## Key Features

- 1:1 binary compatibility with RHEL
- Community-driven governance
- Free and open source
- Enterprise-grade stability
- Long-term support lifecycle

## Package Management

### DNF/YUM
```bash
dnf check-update               # Check for updates
dnf install package-name       # Install package
dnf remove package-name        # Remove package
dnf list installed             # List installed packages
dnf history                    # View history
```

### EPEL Repository
```bash
dnf install epel-release       # Enable EPEL
dnf install package-name       # Install EPEL package
```

## System Administration

### Security Policies
```bash
authselect list                # List profiles
authselect select sssd         # Select profile
authselect enable-feature with-mkhomedir
```

### System Monitoring
```bash
uptime                        # System uptime
vmstat 1                      # Virtual memory stats
mpstat -P ALL 1               # CPU statistics
sar -n DEV 1                  # Network statistics
```

### Storage
```bash
lsblk                        # List block devices
blkid                        # Block device IDs
df -hT                       # Disk usage with types
mount /dev/sdb1 /mnt          # Mount filesystem
```

## Security Features

- SELinux enforcing by default
- System-wide cryptographic policies
- FIPS 140-2 support
- OpenSCAP integration
- Secure Boot compliance

## Cloud and Container Support

- Official cloud images available
- Container-optimized builds
- AWS, Azure, GCP marketplace images
- Docker and Podman support

## Migration from CentOS

```bash
# Download migration script
curl -O https://raw.githubusercontent.com/AlmaLinux/almalinux-deploy/master/almalinux-deploy.sh

# Run migration
sudo bash almalinux-deploy.sh

# Reboot system
sudo reboot
```

## Community Resources

- AlmaLinux Documentation Center
- Community forums and IRC
- Bug tracker
- Wiki with how-to guides
- Steering Council governance

## References

- AlmaLinux Official Documentation
- AlmaLinux Wiki
- GitHub repositories
