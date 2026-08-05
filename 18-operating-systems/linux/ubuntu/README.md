# Ubuntu Linux

## Overview

Ubuntu is a popular Linux distribution based on Debian, known for its user-friendly approach and strong community support. It comes in several editions: Desktop, Server, and Core (for IoT devices).

## Editions

| Edition | Description |
|---------|-------------|
| Ubuntu Desktop | Full desktop environment with GUI |
| Ubuntu Server | Headless server installation |
| Ubuntu Core | Minimal IoT and embedded systems |
| Ubuntu LTS | Long-Term Support (5 years) |
| Ubuntu Interim | Latest features (9 months support) |

## Package Management

### APT (Advanced Package Tool)
```bash
apt update                    # Update package index
apt upgrade                   # Upgrade installed packages
apt install package-name      # Install package
apt remove package-name       # Remove package
apt search keyword            # Search packages
apt show package-name         # Show package info
```

### Snap Packages
```bash
snap install package-name     # Install snap
snap remove package-name      # Remove snap
snap list                     # List installed snaps
snap refresh                  # Update all snaps
```

## Server Administration

### Service Management
```bash
systemctl start nginx         # Start service
systemctl enable nginx        # Enable at boot
systemctl restart nginx       # Restart service
journalctl -u nginx           # View service logs
```

### Firewall (UFW)
```bash
ufw enable                    # Enable firewall
ufw allow 22/tcp              # Allow SSH
ufw allow 80/tcp              # Allow HTTP
ufw status                    # Check rules
```

### Network Configuration
```bash
ip addr show                  # View interfaces
netplan apply                 # Apply network config
resolvectl status             # DNS information
```

## System Configuration

### Update Manager
- Automatic security updates available
- Unattended-upgrades for automated patching
- Release upgrades for version transitions

### AppArmor
- Mandatory access control system
- Profiles for application confinement
- Management with `aa-status`, `aa-enforce`

## Security Hardening

1. Enable UnattendedUpgrades for security patches
2. Configure UFW firewall rules
3. Use SSH key authentication only
4. Install and configure fail2ban
5. Regular system audits with Lynis

## Performance Tuning

```bash
sysctl -w net.core.somaxconn=65535   # Increase connections
ulimit -n 65535                       # Increase file descriptors
systemctl set-property nginx CPUQuota=80%  # Resource limits
```

## Common Use Cases

- Web servers and application hosting
- Development environments
- Cloud and container deployments
- Desktop development workstations
- IoT and edge devices

## References

- Official Ubuntu Documentation: help.ubuntu.com
- Ubuntu Server Guide
- Ubuntu Security Guides
