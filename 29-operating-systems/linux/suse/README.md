# SUSE Linux Enterprise

## Overview

SUSE Linux Enterprise Server (SLES) is a commercial Linux distribution developed by SUSE. It provides enterprise-grade reliability, security, and support for mission-critical workloads.

## Products

| Product | Description |
|---------|-------------|
| SLES | Enterprise server distribution |
| openSUSE Leap | Community distribution |
| openSUSE Tumbleweed | Rolling release |
| SUSE Liberty Linux | Multi-distribution support |

## Package Management

### ZYpp Package Manager
```bash
zypper refresh               # Refresh repositories
zypper update                # Update packages
zypper install package-name  # Install package
zypper remove package-name   # Remove package
zypper search keyword        # Search packages
zypper info package-name     # Show package info
```

### Repository Management
```bash
zypper repos                 # List repositories
zypper addrepo URL name      # Add repository
zypper removerepo name       # Remove repository
zypper modifyrepo --enable name  # Enable repository
```

## System Administration

### YaST (Yet another Setup Tool)
```bash
yast                         # GUI/NCurses interface
yast2 network                 # Network configuration
yast2 firewall               # Firewall setup
yast2 users                  # User management
```

### Systemd Services
```bash
systemctl start apache2      # Start Apache
systemctl enable apache2     # Enable at boot
systemctl status apache2     # Check status
systemctl list-units         # List all units
```

### Network Management
```bash
wicked ifstatus              # Network status
wicked up eth0               # Bring interface up
wicked down eth0             # Bring interface down
```

## Enterprise Features

- Long-term support (13 years)
- High availability clustering
- Live patching support
- Virtualization support
- Mainframe and ARM support

## Security Features

- AppArmor integration
- Firewall configuration
- Security auditing
- FIPS 140-2 compliance
- Common Criteria certification

## High Availability

```bash
crm status                   # Cluster status
crm configure                # Configure resources
crm mon                      # Monitor cluster
crm node standby             # Put node in standby
```

## Best Practices

1. Use SLES for enterprise production
2. Enable automatic updates
3. Configure proper monitoring
4. Implement backup strategies
5. Use YaST for initial configuration

## References

- SLES Documentation
- SUSE Customer Center
- openSUSE Wiki
