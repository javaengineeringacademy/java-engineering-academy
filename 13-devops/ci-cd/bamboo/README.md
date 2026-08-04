# Bamboo CI

## Overview

Bamboo is a CI/CD server by Atlassian that automates builds, tests, and releases. It integrates with Jira and Bitbucket for a complete DevOps solution.

## Plans

### Basic Plan Configuration
```yaml
# bamboo-specs/plan.yml
plan:
  name: My Application Build
  project:
    key: MYAPP
  key: PLAN1
  stages:
    - jobs:
        - name: Build
          key: BUILD
          tasks:
            - script:
                inline:
                  scriptBody: |
                    mvn clean package
                  interpreter: bash
          requirements:
            - system.builder.Maven 3.8
        
        - name: Test
          key: TEST
          tasks:
            - script:
                inline:
                  scriptBody: |
                    mvn test
                  interpreter: bash
          artifacts:
            - name: test-results
              pattern: '**/target/surefire-reports/*.xml'
              required: true
```

### Deployment Projects
```yaml
# bamboo-specs/deployment.yml
deployment:
  name: Production Deployment
  sourcePlan: MYAPP-PLAN1
  environments:
    - name: Staging
      tasks:
        - script:
            inline:
              scriptBody: |
                kubectl apply -f k8s/staging/
      deploymentトリガー:
        - triggered: after successful build
    
    - name: Production
      tasks:
        - script:
            inline:
              scriptBody: |
                kubectl apply -f k8s/production/
      requirements:
        - system.agent.type == production
      finalTasks: []
```

## Variables and Repositories

```yaml
plan:
  variables:
    - name: APP_VERSION
      value: '1.0.0'
    - name: BUILD_ENV
      value: 'production'
  
  repositories:
    - name: Application Source
      url: https://github.com/org/app.git
      branch: main
  
  branches:
    createStrategy:
      pattern: 'feature/*'
```

## Best Practices

1. **Use plan dependencies** - Chain related plans together
2. **Implement deployment projects** - Manage environments systematically
3. **Use artifact sharing** - Pass build artifacts between jobs
4. **Secure credentials** - Use Bamboo's credential manager
5. **Implement branch detection** - Auto-create plans for branches
6. **Use task types** - Leverage built-in task types
7. **Monitor plan performance** - Track build times and failures
8. **Use Jira integration** - Link builds to issues
9. **Implement notifications** - Get notified of build status
10. **Document plans** - Add descriptions for complex pipelines
