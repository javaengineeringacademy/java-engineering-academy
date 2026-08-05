# Debian Linux

## Overview

Debian is a free and open-source Linux distribution known for its stability, reliability, and extensive package repository. It serves as the foundation for many other distributions, including Ubuntu.

## Versions

| Version | Codename | Status |
|---------|----------|--------|
| Debian 12 | Bookworm | Current Stable |
| Debian 11 | Bullseye | Old Stable |
| Debian Testing | Trixie | Development |
| Debian Unstable | Sid | Rolling release |

## Package Management

### APT (Advanced Package Tool)
```bash
apt update                    # Update package index
apt upgrade                   # Upgrade packages
apt full-upgrade              # Full system upgrade
apt install package-name      # Install package
apt remove package-name       # Remove package
apt purge package-name        # Remove with config
apt autoremove                # Remove unused deps
```

### DPKG
```bash
dpkg -l                       # List packages
dpkg -L package-name          # List files in package
dpkg -s package-name          # Show package status
dpkg -i package.deb           # Install local package
```

## System Administration

### User Management
```bash
adduser username              # Add user (interactive)
useradd -m -s /bin/bash user  # Add user (non-interactive)
passwd username               # Set password
deluser username              # Remove user
```

### Service Management
```bash
systemctl start service       # Start service
systemctl stop service        # Stop service
systemctl restart service     # Restart service
systemctl status service      # Check status
```

### Network Configuration
```bash
ip addr show                  # View interfaces
/etc/network/interfaces       # Network config file
ifup eth0                     # Bring interface up
ifdown eth0                   # Bring interface down
```

## Debian Releases Policy

- Stable: Production-ready, security updates
- Testing: Next release candidate
- Unstable: Rolling development
- Backports: Newer packages for stable

## Security

### Security Updates
```bash
apt install debian-security-key   # Install security key
apt update && apt upgrade          # Apply updates
unattended-upgrades               # Automatic updates
```

### AppArmor
```bash
aa-status                    # Check AppArmor status
aa-enforce /etc/apparmor.d/path  # Enforce profile
aa-complain /etc/apparmor.d/path # Set complain mode
```

## Best Practices

1. Use stable for production systems
2. Enable security updates repository
3. Regular system backups
4. Minimize installed packages
5. Use unattended-upgrades for security

## References

- Debian Documentation
- Debian Administrator's Handbook
- Debian Wiki
