# Terraform Installation

## Official Download

Download from https://www.terraform.io/downloads

```bash
# macOS
brew install hashicorp/tap/terraform

# Linux (Debian/Ubuntu)
wget -O- https://releases.hashicorp.com/terraform/ | sh

# Windows
choco install terraform
```

## tfenv

```bash
# Install tfenv
git clone https://github.com/tfutils/tfenv.git ~/.tfenv
echo 'export PATH="$HOME/.tfenv/bin:$PATH"' >> ~/.bashrc

# Install Terraform
tfenv install 1.5.0
tfenv use 1.5.0

# List available versions
tfenv list-remote

# Use specific version
tfenv use 1.4.0
```

## Docker

```bash
# Run Terraform in Docker
docker run --rm -it \
    -v $(pwd):/workspace \
    -w /workspace \
    hashicorp/terraform init

# Use specific version
docker run --rm -it \
    -v $(pwd):/workspace \
    -w /workspace \
    hashicorp/terraform:1.5.0 plan
```

## CI/CD Integration

GitHub Actions:

```yaml
- name: Setup Terraform
  uses: hashicorp/setup-terraform@v2
  with:
    terraform_version: 1.5.0

- name: Terraform Init
  run: terraform init

- name: Terraform Plan
  run: terraform plan
```

GitLab CI:

```yaml
image: hashicorp/terraform:1.5.0

stages:
  - plan
  - apply

plan:
  stage: plan
  script:
    - terraform init
    - terraform plan
```

## Version Management

```bash
# Check version
terraform version

# Upgrade to latest
brew upgrade terraform

# List installed
tfenv list

# Switch version
tfenv use 1.4.0
```

## Verification

```bash
terraform version
terraform --help
terraform providers
```
