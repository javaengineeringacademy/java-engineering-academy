# Linux Operating Systems

## Overview

Linux is an open-source Unix-like operating system kernel first released by Linus Torvalds in 1991. Today, Linux powers the majority of servers, cloud infrastructure, mobile devices (Android), and supercomputers worldwide.

## Distributions

| Distribution | Use Case | Package Manager |
|--------------|----------|-----------------|
| [Ubuntu](ubuntu/README.md) | Desktop, Server, Cloud | apt (deb) |
| [RHEL](rhel/README.md) | Enterprise Server | yum/dnf (rpm) |
| [CentOS](centos/README.md) | Enterprise (Community) | yum/dnf (rpm) |
| [Rocky Linux](rocky-linux/README.md) | RHEL Alternative | yum/dnf (rpm) |
| [AlmaLinux](almalinux/README.md) | RHEL Alternative | yum/dnf (rpm) |
| [Debian](debian/README.md) | Stability, Servers | apt (deb) |
| [SUSE](suse/README.md) | Enterprise, Desktop | zypper (rpm) |
| [Amazon Linux](amazon-linux/README.md) | AWS Cloud | yum/dnf (rpm) |

## Core Concepts

### Kernel and Shell
- **Kernel**: Core component managing hardware resources
- **Shell**: Command-line interface (bash, zsh, fish)
- **Init System**: systemd, SysVinit, OpenRC

### File System Hierarchy
```
/
├── bin/       # Essential binaries
├── etc/       # Configuration files
├── home/      # User home directories
├── var/       # Variable data (logs, caches)
├── usr/       # User programs and libraries
├── opt/       # Optional software
├── tmp/       # Temporary files
└── proc/      # Process information
```

### Package Management
- **DEB-based**: Ubuntu, Debian - use `apt` or `dpkg`
- **RPM-based**: RHEL, CentOS, Fedora - use `yum`, `dnf`, or `rpm`
- **SUSE**: Uses `zypper` and YaST

## Administration Essentials

### User Management
```bash
useradd username          # Create user
passwd username           # Set password
usermod -aG group user    # Add to group
userdel username          # Delete user
```

### Service Management
```bash
systemctl start service   # Start service
systemctl stop service    # Stop service
systemctl enable service  # Enable at boot
systemctl status service  # Check status
```

### System Monitoring
```bash
top                       # Process monitor
htop                      # Interactive process viewer
df -h                     # Disk usage
free -m                   # Memory usage
iostat                    # I/O statistics
```

## Security Fundamentals

- SELinux or AppArmor for mandatory access control
- Firewall configuration (iptables, nftables, firewalld)
- SSH key-based authentication
- Regular security updates and patching
- Audit logging with auditd

## Best Practices

1. Keep systems updated with latest security patches
2. Use least privilege principle for user accounts
3. Implement proper logging and monitoring
4. Regular backups and disaster recovery testing
5. Document system configurations

## References

- Linux Documentation Project (tldp.org)
- GNU/Linux Command-line Tools Summary
- Distribution-specific documentation
