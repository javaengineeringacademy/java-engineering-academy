# Docker Installation

## Docker Desktop

### macOS
```bash
# Download Docker Desktop from docker.com
# Or use Homebrew
brew install --cask docker

# After installation, open Docker Desktop
```

### Windows
```bash
# Download Docker Desktop installer
# Run installer with:
# - WSL 2 backend (recommended)
# - Hyper-V backend (alternative)

# After installation, restart computer
```

### Linux (GUI)
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io

# Start and enable
sudo systemctl start docker
sudo systemctl enable docker
```

## Docker Engine (Linux)

### Ubuntu/Debian
```bash
# Remove old versions
sudo apt remove docker docker-engine docker.io containerd runc

# Install prerequisites
sudo apt update
sudo apt install ca-certificates curl gnupg

# Add Docker GPG key
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Add repository
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker
```

### CentOS/RHEL/Fedora
```bash
# Remove old versions
sudo yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-engine

# Install yum-utils
sudo yum install -y yum-utils

# Add repository
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# Install Docker
sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Start and enable
sudo systemctl start docker
sudo systemctl enable docker
```

## Rootless Mode

### Installation
```bash
# Install prerequisites
sudo apt install uidmap

# Create user namespace
echo "$USER" | sudo tee /etc/subuid
echo "$USER" | sudo tee /etc/subgid

# Install Docker in rootless mode
dockerd-rootless-setuptool.sh install

# Start Docker
systemctl --user start docker
systemctl --user enable docker

# Verify
docker info | grep -i root
```

### Rootless Benefits
- No root access required
- Better security isolation
- Reduced attack surface
- User namespace isolation

## Post-Installation

### Verify Installation
```bash
docker --version
docker compose version
docker run hello-world
```

### Configure Docker
```bash
# Create daemon.json
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<EOF
{
  "data-root": "/var/lib/docker",
  "storage-driver": "overlay2",
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  }
}
EOF

# Restart Docker
sudo systemctl restart docker
```

## Docker Compose

### Install Compose Plugin
```bash
# Ubuntu/Debian
sudo apt install docker-compose-plugin

# CentOS/RHEL
sudo yum install docker-compose-plugin

# Verify
docker compose version
```

### Standalone Compose
```bash
# Download binary
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Make executable
sudo chmod +x /usr/local/bin/docker-compose

# Verify
docker-compose --version
```

## Troubleshooting

### Permission Denied
```bash
# Add user to docker group
sudo usermod -aG docker $USER
newgrp docker

# Or use sudo
sudo docker run hello-world
```

### Docker Not Starting
```bash
# Check status
sudo systemctl status docker

# View logs
sudo journalctl -u docker.service

# Restart
sudo systemctl restart docker
```

### Network Issues
```bash
# Check Docker network
docker network ls
docker network inspect bridge

# Restart networking
sudo systemctl restart docker
```

## Best Practices

1. Use Docker Desktop for development
2. Use Docker Engine for servers
3. Consider rootless mode for security
4. Keep Docker updated
5. Configure logging and storage
6. Add user to docker group
7. Test installation with hello-world
