# Unix Operating Systems

## Overview

Unix is a family of multitasking, multiuser computer operating systems that derive from the original AT&T Unix, developed in the 1970s at Bell Labs. Unix systems are known for their stability, security, and portability.

## Major Unix Systems

| System | Vendor | Description |
|--------|--------|-------------|
| AIX | IBM | Enterprise Unix for Power Systems |
| HP-UX | Hewlett Packard Enterprise | Unix for Itanium servers |
| Solaris | Oracle | Enterprise Unix platform |
| FreeBSD | Community | Open-source Unix-like OS |
| OpenBSD | Community | Security-focused Unix-like OS |
| NetBSD | Community | Portable Unix-like OS |

## System Administration

### User Management
```bash
useradd username              # Add user
passwd username               # Set password
usermod -G group username     # Add to group
userdel username              # Delete user
```

### Process Management
```bash
ps aux                        # List all processes
top                           # Process monitor
kill PID                      # Kill process
killall process-name          # Kill by name
nice -n 10 command            # Run with priority
```

### File System Management
```bash
df -h                         # Disk usage
du -sh /path                  # Directory size
mount /dev/sdX1 /mnt          # Mount filesystem
umount /mnt                   # Unmount filesystem
```

## Shell and Command Line

### Common Shells
- **sh**: Bourne shell (original)
- **bash**: Bourne Again Shell
- **csh**: C Shell
- **ksh**: Korn Shell
- **zsh**: Z Shell

### Shell Scripting
```bash
#!/bin/bash
# Variables
NAME="World"
echo "Hello, $NAME"

# Loops
for i in 1 2 3; do
    echo "Number: $i"
done

# Conditionals
if [ -f /path/to/file ]; then
    echo "File exists"
fi
```

## System Monitoring

### Performance Tools
```bash
vmstat 1                      # Virtual memory stats
iostat 1                      # I/O statistics
sar -u 1                      # CPU usage
netstat -i                    # Network interfaces
```

### Log Files
```bash
tail -f /var/log/syslog       # Real-time logs
grep error /var/log/messages  # Search logs
last                          # Login history
who                           # Current users
```

## Security Features

- File permissions (rwx, chmod, chown)
- Access control lists
- Cryptographic authentication
- Secure shell (SSH)
- Audit logging

## High Availability

### Clustering
- Failover clustering
- Load balancing
- Shared storage
- Resource management

### Backup and Recovery
```bash
tar -czf backup.tar.gz /path/to/backup
rsync -av /source/ /destination/
dump 0f /dev/st0 /dev/sda1
```

## Standards and Portability

- POSIX compliance
- Single UNIX Specification
- X/Open standards
- System V Interface Definition

## References

- The Unix Programming Environment
- Unix and Linux System Administration Handbook
- Man pages and documentation
