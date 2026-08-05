# Python Installation

## pyenv

Manage multiple Python versions.

```bash
# Install pyenv
curl https://pyenv.run | bash

# Add to shell
echo 'export PYENV_ROOT="$HOME/.pyenv"' >> ~/.bashrc
echo 'command -v pyenv >/dev/null || export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.bashrc
echo 'eval "$(pyenv init -)"' >> ~/.bashrc

# Install Python
pyenv install 3.11.0
pyenv global 3.11.0

# List versions
pyenv versions
```

## Conda

### Miniconda
```bash
# Download installer
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh

# Install
bash Miniconda3-latest-Linux-x86_64.sh

# Initialize
conda init bash
```

### Create Environment
```bash
conda create -n myproject python=3.11
conda activate myproject
conda install numpy pandas
```

## pip

### Install pip
```bash
# Ensure pip is installed
python -m ensurepip --upgrade

# Upgrade pip
pip install --upgrade pip
```

### Common Commands
```bash
# Install package
pip install requests

# Install from requirements.txt
pip install -r requirements.txt

# Install in development mode
pip install -e .

# Show installed packages
pip list

# Show package info
pip show requests

# Uninstall
pip uninstall requests
```

## venv (Built-in)

```bash
# Create virtual environment
python -m venv venv

# Activate (macOS/Linux)
source venv/bin/activate

# Activate (Windows)
venv\Scripts\activate

# Install packages
pip install requests

# Deactivate
deactivate

# Remove
rm -rf venv
```

## Docker

### Basic Setup
```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["python", "main.py"]
```

### Build and Run
```bash
docker build -t myapp .
docker run -it --rm myapp
```

### Development Container
```bash
docker run -it -v $(pwd):/app python:3.11 bash
```

## System Package Manager

### Ubuntu/Debian
```bash
sudo apt update
sudo apt install python3 python3-pip python3-venv
```

### macOS (Homebrew)
```bash
brew install python@3.11
```

### Windows
```powershell
# Download from python.org
# Or use Chocolatey
choco install python --version=3.11
```

## Verification

```bash
# Check version
python --version
python3 --version

# Check pip
pip --version

# Test import
python -c "import sys; print(sys.version)"
```
