# Snyk

## Overview
Snyk is a developer-first security platform that finds and fixes vulnerabilities in code, dependencies, containers, and infrastructure as code.

## Setup
```bash
npm install -g snyk
snyk auth
```

## Scanning Commands
```bash
snyk test                          # Test for vulnerabilities
snyk monitor                       # Monitor project
snyk container test myapp:latest   # Scan container
snyk iac test terraform/           # Scan IaC
```

## CI/CD Integration
```yaml
security:
  runs-on: ubuntu-latest
  steps:
    - uses: actions/checkout@v3
    - name: Run Snyk
      uses: snyk/actions/maven@master
      env:
        SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
      with:
        args: --severity-threshold=high
```

## Best Practices
1. Integrate with PR checks
2. Set severity thresholds per pipeline
3. Use Snyk Code for SAST
4. Monitor dependencies continuously
5. Use fix PRs for automated remediation
