# Quality Gates

## Overview

Automated checks that must pass before code can be merged/deployed.

## Common Gates

### Code Review
- At least one approval
- No unresolved comments

### Testing
- All tests pass
- Coverage threshold met

### Security
- No high/critical vulnerabilities
- SAST scan passed

### Build
- Build succeeds
- No compilation errors

## Implementation

```yaml
# GitHub Actions example
jobs:
  quality-gate:
    steps:
    - uses: actions/checkout@v3
    - run: npm test
    - run: npm run lint
    - run: npm run security-scan
```
