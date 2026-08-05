# CentOS Linux

## Overview

CentOS (Community Enterprise Operating System) was a free Linux distribution based on Red Hat Enterprise Linux. Note: CentOS Linux reached end of life in 2021, with CentOS Stream becoming the upstream development platform.

## History and Status

| Version | Status |
|---------|--------|
| CentOS 7 | EOL June 2024 |
| CentOS 8 | EOL December 2021 |
| CentOS Stream 9 | Rolling release (active) |

### CentOS Stream

CentOS Stream is a rolling-release distribution that serves as the upstream for RHEL development. It receives updates before RHEL releases.

## Package Management

### YUM/DNF
```bash
yum update                    # Update packages
yum install package-name      # Install package
yum remove package-name       # Remove package
yum repolist                  # List repositories
yum clean all                 # Clean cache
```

### YUM History
```bash
yum history                   # View history
yum history undo transaction  # Undo transaction
yum history info transaction  # Transaction details
```

## System Administration

### Service Management
```bash
systemctl start firewalld     # Start firewall
systemctl enable firewalld    # Enable at boot
systemctl status sshd         # Check SSH status
systemctl list-unit-files     # List services
```

### Network Configuration
```bash
nmtui                        # Network Manager TUI
nmcli con show               # List connections
nmcli con mod "name" ipv4.addresses 192.168.1.100/24
```

## Differences from RHEL

- CentOS: Community-supported, free
- RHEL: Commercial support, subscription required
- CentOS Stream: Upstream for RHEL development

## Migration Options

For organizations using CentOS Linux:

1. **CentOS Stream**: Continue with rolling updates
2. **Rocky Linux**: Community RHEL rebuild
3. **AlmaLinux**: Community RHEL rebuild
4. **RHEL**: Commercial support option
5. **RHEL Developer Subscription**: Free for development

## Best Practices

1. Plan migration before EOL dates
2. Test applications on CentOS Stream
3. Consider alternatives for production
4. Implement proper backup strategies
5. Monitor security advisories

## References

- CentOS Project Documentation
- CentOS Stream Documentation
- Migration guides for alternatives
