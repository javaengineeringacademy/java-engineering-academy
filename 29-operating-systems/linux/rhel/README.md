# Red Hat Enterprise Linux (RHEL)

## Overview

Red Hat Enterprise Linux is a commercial Linux distribution designed for enterprise environments. It provides stability, security, and long-term support with a focus on mission-critical workloads.

## Versions

| Version | Release Date | End of Life |
|---------|--------------|-------------|
| RHEL 9 | 2022 | 2032 |
| RHEL 8 | 2019 | 2029 |
| RHEL 7 | 2014 | 2024 |

## Subscription Model

- Requires paid subscription for access
- Includes support and certification
- Access to Red Hat Customer Portal
- Security errata and patches

## Package Management

### YUM/DNF
```bash
yum update                    # Update packages
yum install package-name      # Install package
yum remove package-name       # Remove package
yum search keyword            # Search packages
yum info package-name         # Show package info
yum history                   # Transaction history
```

### RPM
```bash
rpm -qa                       # List all packages
rpm -qi package-name          # Package info
rpm -ql package-name          # List files
rpm -Uvh package.rpm          # Upgrade package
```

## System Administration

### Systemd
```bash
systemctl start httpd         # Start Apache
systemctl enable httpd        # Enable at boot
systemctl status httpd        # Check status
systemctl mask service        # Disable completely
```

### SELinux
```bash
getenforce                    # Check status
setenforce 0                  # Permissive mode
semanage port -l              # List ports
restorecon -Rv /path          # Restore contexts
```

### Firewall
```bash
firewall-cmd --state                      # Check status
firewall-cmd --add-port=8080/tcp --permanent  # Add rule
firewall-cmd --reload                     # Reload rules
firewall-cmd --list-all                   # List rules
```

## Enterprise Features

- Extended lifecycle support (ELS)
- Certified hardware and software ecosystem
- Real-time kernel for low-latency workloads
- FIPS 140-2 compliance
- Common Criteria certification

## Security Features

- Security-Enhanced Linux (SELinux)
- OpenSCAP for compliance scanning
- System-wide cryptographic policies
- Secure Boot support
- FIPS 140-2 validated cryptographic modules

## Performance Tuning

```bash
tuned-adm active              # Check active profile
tuned-adm profile throughput-performance  # Set profile
```

## Red Hat Ecosystem

- **Red Hat OpenShift**: Container platform
- **Red Hat Ansible**: Automation platform
- **Red Hat Satellite**: Systems management
- **Red Hat JBoss**: Middleware platform

## References

- Red Hat Enterprise Linux Documentation
- Red Hat Customer Portal
- Red Hat System Administration guides
