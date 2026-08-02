# Module 63: CI/CD

## Overview
Continuous Integration and Continuous Delivery/Deployment (CI/CD) automate building, testing, and deploying software. It enables frequent, reliable releases with quality gates.

## Learning Objectives
- Design CI/CD pipelines
- Implement automated testing
- Configure deployment strategies
- Monitor pipeline health
- Apply security scanning

## Prerequisites
- Version control (Git)
- Build tools (Maven/Gradle)
- Basic DevOps concepts

## Why This Concept Exists
Manual processes lead to:
- Slow releases
- Human errors
- Inconsistent environments
- Quality issues

CI/CD provides:
- Fast feedback
- Consistent builds
- Automated quality
- Reliable deployments

## Problem Statement
How do you automate software delivery while maintaining quality?

## Theory

### CI/CD Pipeline Stages

| Stage | Purpose |
|-------|---------|
| Build | Compile code |
| Test | Run tests |
| Security | Scan vulnerabilities |
| Package | Create artifacts |
| Deploy | Release to environments |
| Monitor | Track health |

### Deployment Strategies

| Strategy | Description |
|----------|-------------|
| Blue-Green | Two identical environments |
| Canary | Gradual rollout |
| Rolling | Update instances gradually |
| Recreate | Stop old, start new |

## Pipeline Examples

### GitHub Actions

```yaml
name: Java CI/CD Pipeline

on:
  push:
    branches: [main]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build
      run: mvn clean compile
    
    - name: Test
      run: mvn test
    
    - name: Package
      run: mvn package -DskipTests
    
    - name: Upload artifact
      uses: actions/upload-artifact@v3
      with:
        name: app
        path: target/*.jar

  deploy:
    needs: build
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
    - name: Deploy to production
      run: ./deploy.sh
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                sh './deploy.sh'
            }
        }
    }
    
    post {
        failure {
            mail to: 'team@example.com',
                 subject: "Build Failed: ${currentBuild.fullDisplayName}"
        }
    }
}
```

## Enterprise Example

```yaml
# Complete production pipeline
name: Production Pipeline

on:
  push:
    tags:
      - 'v*'

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Validate commit messages
      run: ./scripts/validate-commits.sh

  build:
    needs: validate
    runs-on: ubuntu-latest
    outputs:
      version: ${{ steps.version.outputs.version }}
    steps:
    - uses: actions/checkout@v3
    - id: version
      run: echo "version=${GITHUB_REF#refs/tags/v}" >> $GITHUB_OUTPUT
    - run: mvn clean package -Dversion=${{ steps.version.outputs.version }}

  security:
    needs: build
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    - name: Run Snyk
      uses: snyk/actions/maven@master
    - name: Run OWASP dependency check
      run: mvn verify -Psecurity-scan

  deploy-staging:
    needs: security
    runs-on: ubuntu-latest
    environment: staging
    steps:
    - run: ./deploy.sh staging ${{ needs.build.outputs.version }}

  test-staging:
    needs: deploy-staging
    runs-on: ubuntu-latest
    steps:
    - run: ./run-integration-tests.sh staging

  deploy-production:
    needs: test-staging
    runs-on: ubuntu-latest
    environment: production
    steps:
    - run: ./deploy.sh production ${{ needs.build.outputs.version }}

  notify:
    needs: deploy-production
    runs-on: ubuntu-latest
    steps:
    - name: Notify team
      run: |
        curl -X POST $SLACK_WEBHOOK \
          -d '{"text":"Deployed v${{ needs.build.outputs.version }} to production"}'
```

## Performance Considerations
- Cache dependencies
- Parallel test execution
- Optimize Docker layers
- Use artifact storage

## Best Practices
1. Fail fast
2. Automate everything
3. Use version control for pipelines
4. Monitor pipeline health
5. Keep pipelines fast

## Interview Questions

### Q1: What is CI/CD?
**Answer:** Continuous Integration (merge often) and Continuous Delivery/Deployment (deploy often).

### Q2: What is a pipeline?
**Answer:** Automated sequence of steps from code to production.

### Q3: What is the difference between continuous delivery and deployment?
**Answer:** Delivery requires manual approval, deployment is automatic.

### Q4: What is a quality gate?
**Answer:** Criteria that must pass before proceeding in pipeline.

### Q5: What is blue-green deployment?
**Answer:** Running two identical environments for zero-downtime deployment.

## Summary
CI/CD enables fast, reliable software delivery through automation and quality gates.

## References
- GitHub Actions Documentation
- Jenkins Documentation
- Continuous Delivery by Jez Humble
