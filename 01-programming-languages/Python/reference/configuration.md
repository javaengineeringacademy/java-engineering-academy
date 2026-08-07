# Python Configuration

## pyproject.toml

The modern standard for Python project configuration.

```toml
[build-system]
requires = ["setuptools>=68.0", "wheel"]
build-backend = "setuptools.backends._legacy:_Backend"

[project]
name = "myproject"
version = "0.1.0"
description = "A sample project"
readme = "README.md"
license = {text = "MIT"}
requires-python = ">=3.8"
dependencies = [
    "requests>=2.28.0",
    "click>=8.0",
]

[project.optional-dependencies]
dev = [
    "pytest>=7.0",
    "black>=23.0",
    "mypy>=1.0",
    "ruff>=0.1.0",
]

[tool.black]
line-length = 88
target-version = ['py38']

[tool.mypy]
python_version = "3.8"
strict = true

[tool.ruff]
line-length = 88
select = ["E", "F", "I", "N", "W"]
```

## setup.cfg

Legacy configuration file (still supported).

```ini
[metadata]
name = myproject
version = 0.1.0

[options]
packages = find:
install_requires =
    requests>=2.28.0
    click>=8.0

[options.extras_require]
dev =
    pytest>=7.0
    black>=23.0

[flake8]
max-line-length = 88
```

## .env Files

Environment variable management.

```bash
# .env
DATABASE_URL=postgresql://localhost/mydb
SECRET_KEY=my-secret-key
DEBUG=true
API_KEY=abc123
```

### Loading .env

```python
from dotenv import load_dotenv
import os

load_dotenv()  # Load from .env

database_url = os.getenv("DATABASE_URL")
secret_key = os.getenv("SECRET_KEY")
```

### python-dotenv

```bash
pip install python-dotenv
```

## Virtual Environments

### venv (Built-in)
```bash
# Create
python -m venv venv

# Activate (macOS/Linux)
source venv/bin/activate

# Activate (Windows)
venv\Scripts\activate

# Deactivate
deactivate
```

### Conda
```bash
# Create
conda create -n myenv python=3.11

# Activate
conda activate myenv

# Deactivate
conda deactivate
```

## pip Configuration

### pip.conf
```ini
[global]
index-url = https://pypi.org/simple
trusted-host = pypi.org

[install]
no-cache-dir = true
```

### requirements.txt
```txt
requests==2.28.0
click>=8.0,<9.0
```

### requirements-dev.txt
```txt
-r requirements.txt
pytest>=7.0
black>=23.0
mypy>=1.0
```

## Environment Variables

### Common Variables
```bash
# Python
PYTHONPATH=/custom/path
PYTHONDONTWRITEBYTECODE=1
PYTHONUNBUFFERED=1

# Virtual Environment
VIRTUAL_ENV=/path/to/venv
```

### Loading Configuration
```python
import os

# With defaults
debug = os.getenv("DEBUG", "false").lower() == "true"
port = int(os.getenv("PORT", "8000"))

# Required variables
api_key = os.environ["API_KEY"]  # Raises KeyError if missing
```

## Tox Configuration

```ini
[tox]
envlist = py38, py39, py310, py311

[testenv]
deps =
    pytest>=7.0
commands =
    pytest tests/

[testenv:lint]
deps =
    black
    ruff
commands =
    black --check .
    ruff check .
```

## Docker Integration

```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .
CMD ["python", "main.py"]
```
