# Windows Server

## Overview

Windows Server is Microsoft's server operating system designed for enterprise environments. It provides a comprehensive platform for running applications, managing networks, and storing data with extensive integration with Microsoft's ecosystem.

## Versions

| Version | Release Date | Support End |
|---------|--------------|-------------|
| Windows Server 2025 | 2024 | 2029 |
| Windows Server 2022 | 2021 | 2026 |
| Windows Server 2019 | 2018 | 2024 |

## Editions

| Edition | Description |
|---------|-------------|
| Standard | Virtualization rights limited |
| Datacenter | Unlimited virtualization |
| Essentials | Small business (25 users) |
| Azure Edition | Optimized for cloud |

## Installation and Configuration

### Server Core vs Desktop Experience
- **Server Core**: Minimal GUI, reduced attack surface
- **Desktop Experience**: Full GUI, traditional management

### Initial Configuration
```powershell
# Set computer name
Rename-Computer -NewName "Server01" -Restart

# Configure network
New-NetIPAddress -InterfaceAlias "Ethernet" -IPAddress 192.168.1.10 -PrefixLength 24
Set-DnsClientServerAddress -InterfaceAlias "Ethernet" -ServerAddresses 192.168.1.1

# Enable Remote Desktop
Set-ItemProperty -Path 'HKLM:\System\CurrentControlSet\Control\Terminal Server' -Name "fDenyTSConnections" -Value 0
Enable-NetFirewallRule -DisplayGroup "Remote Desktop"
```

## PowerShell Administration

### Service Management
```powershell
Get-Service                     # List services
Start-Service -Name "ServiceName"
Stop-Service -Name "ServiceName"
Restart-Service -Name "ServiceName"
Set-Service -Name "ServiceName" -StartupType Automatic
```

### User Management
```powershell
New-LocalUser -Name "username" -Password $securePassword
Add-LocalGroupMember -Group "Administrators" -Member "username"
Get-LocalUser                   # List users
Get-LocalGroup                  # List groups
```

### IIS Management
```powershell
Install-WindowsFeature -Name Web-Server -IncludeManagementTools
New-Website -Name "MySite" -PhysicalPath "C:\inetpub\wwwroot" -Port 80
Start-Website -Name "MySite"
```

## Active Directory

### Domain Controller
```powershell
Install-WindowsFeature -Name AD-Domain-Services -IncludeManagementTools
Import-Module ADDSDeployment
Install-ADDSForest -DomainName "corp.example.com"
```

### Group Policy
- Group Policy Management Console (GPMC)
- Group Policy Objects (GPOs)
- Software deployment
- Security policies

## Security Features

- Windows Defender Advanced Threat Protection
- BitLocker Drive Encryption
- Just Enough Administration (JEA)
- Credential Guard
- Shielded Virtual Machines

## Container Support

```powershell
# Install containers feature
Install-WindowsFeature -Name Containers

# Install Docker
Install-Module -Name DockerMsftProvider -Repository PSGallery -Force
Install-Package -Name docker -ProviderName DockerMsftProvider -Force

# Run container
docker run -d -p 80:80 --name web nginx
```

## Best Practices

1. Use Server Core for production
2. Enable Windows Update for Business
3. Implement proper backup strategies
4. Use PowerShell for automation
5. Monitor with Windows Admin Center

## References

- Microsoft Docs: Windows Server
- PowerShell Documentation
- Windows Server Blog
