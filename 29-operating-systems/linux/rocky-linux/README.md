# Rocky Linux

## Overview

Rocky Linux is a community enterprise operating system designed to be 100% bug-for-bug compatible with Red Hat Enterprise Linux (RHEL). It was created by Gregory Kurtzer, CentOS co-founder, as a response to CentOS shifting to CentOS Stream.

## Versions

| Version | RHEL Based | Status |
|---------|------------|--------|
| Rocky Linux 9 | RHEL 9 | Active |
| Rocky Linux 8 | RHEL 8 | Active |

## Key Features

- Binary-compatible with RHEL
- Community-driven development
- Free and open source
- Long-term support lifecycle
- No subscription required

## Package Management

### DNF/YUM
```bash
dnf update                    # Update packages
dnf install package-name      # Install package
dnf remove package-name       # Remove package
dnf search keyword            # Search packages
dnf module list               # List modules
```

### Module System
```bash
dnf module enable module:stream  # Enable module
dnf module install module:stream # Install module
dnf module reset module          # Reset module
```

## System Administration

### SELinux Management
```bash
sestatus                      # Check status
setenforce 0                  # Permissive mode
setsebool -P httpd_can_network_connect on
semanage fcontext -a -t httpd_sys_content_t "/var/www(/.*)?"
```

### Firewall Configuration
```bash
firewall-cmd --list-all                    # List rules
firewall-cmd --add-service=http --permanent  # Allow HTTP
firewall-cmd --reload                     # Reload
```

### Storage Management
```bash
lvs                           # List logical volumes
pvs                           # List physical volumes
vgs                           # List volume groups
```

## Enterprise Features

- Secure Boot support
- FIPS 140-2 compliance
- CIS benchmarks
- OpenSCAP compliance profiles
- Full disk encryption

## Community and Support

- Active community forums
- Bug tracker and issue reporting
- Wiki documentation
- IRC and Matrix channels
- No paid support available

## Migration from CentOS

```bash
# Check current version
cat /etc/centos-release

# Run migration script
curl -O https://raw.githubusercontent.com/rocky-linux/rocky-tools/main/migrate2rocky/migrate2rocky.sh
sudo bash migrate2rocky.sh -r

# Reboot system
sudo reboot
```

## Best Practices

1. Regular system updates
2. Enable automatic security updates
3. Use SELinux in enforcing mode
4. Implement proper access controls
5. Monitor system performance

## References

- Rocky Linux Documentation
- Rocky Linux Wiki
- Community Forums
