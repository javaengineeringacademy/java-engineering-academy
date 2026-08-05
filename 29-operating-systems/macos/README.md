# macOS

## Overview

macOS is Apple's desktop and server operating system for Mac computers. While primarily designed for personal use, macOS is increasingly used in development environments and enterprise settings, particularly for iOS/macOS application development.

## Versions

| Version | Name | Release Date |
|---------|------|--------------|
| macOS 15 | Sequoia | 2024 |
| macOS 14 | Sonoma | 2023 |
| macOS 13 | Ventura | 2022 |
| macOS 12 | Monterey | 2021 |

## Package Management

### Homebrew
```bash
brew update                    # Update Homebrew
brew upgrade                   # Upgrade packages
brew install package-name      # Install package
brew uninstall package-name    # Remove package
brew search keyword            # Search packages
brew list                      # List installed packages
```

### Mac App Store
```bash
mas search "App Name"          # Search App Store
mas install app-id             # Install app
mas upgrade                    # Update apps
mas list                       # List installed apps
```

## System Configuration

### System Preferences (GUI)
- General settings
- Network configuration
- Security & Privacy
- Users & Groups
- Sharing settings

### Command Line Configuration
```bash
# Network
networksetup -setairportpower en0 on
networksetup -setdnsservers Wi-Fi 8.8.8.8 8.8.4.4

# System
systemsetup -setcomputersleep Never
systemsetup -getcomputersleep
```

### Launch Services
```bash
launchctl list                 # List services
launchctl load ~/Library/LaunchAgents/com.example.plist
launchctl unload ~/Library/LaunchAgents/com.example.plist
```

## Development Environment

### Xcode
```bash
xcode-select --install         # Install command line tools
xcodebuild -version            # Check Xcode version
swift --version                # Check Swift version
```

### Homebrew Packages for Development
```bash
brew install git node python3 java maven docker
brew install --cask visual-studio-code intellij-idea
```

## Security Features

- FileVault full disk encryption
- Gatekeeper application verification
- System Integrity Protection (SIP)
- XProtect malware detection
- Privacy controls and permissions

## System Monitoring

```bash
top                            # Process monitor
Activity Monitor               # GUI monitor
diskutil list                  # List disks
system_profiler SPHardwareDataType  # Hardware info
```

## Automation

### AppleScript
```bash
osascript -e 'tell application "Finder" to make new folder at desktop'
```

### Automator
- Workflow automation
- Quick Actions
- Folder actions
- Application scripting

## Best Practices

1. Keep macOS updated for security
2. Use Homebrew for package management
3. Enable FileVault encryption
4. Configure proper backups with Time Machine
5. Use SIP and Gatekeeper

## References

- Apple Developer Documentation
- macOS User Guide
- Homebrew Documentation
