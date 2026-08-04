# GitLab CI

## Overview

GitLab CI/CD is a built-in tool in GitLab that automates the software development lifecycle. It uses `.gitlab-ci.yml` for pipeline configuration.

## Basic Configuration

```yaml
stages:
  - build
  - test
  - deploy

variables:
  DOCKER_IMAGE: my-app

build:
  stage: build
  image: node:18
  script:
    - npm ci
    - npm run build
  artifacts:
    paths:
      - dist/
    expire_in: 1 hour

test:
  stage: test
  image: node:18
  script:
    - npm ci
    - npm test
  coverage: '/Lines\s*:\s*(\d+\.?\d*)%/'

deploy_staging:
  stage: deploy
  script:
    - ./deploy.sh staging
  environment:
    name: staging
    url: https://staging.example.com
  only:
    - develop

deploy_production:
  stage: deploy
  script:
    - ./deploy.sh production
  environment:
    name: production
    url: https://example.com
  when: manual
  only:
    - main
```

## Runners

### Runner Types
```yaml
# Shell runner
build:
  tags:
    - shell
  script:
    - npm run build

# Docker runner
test:
  image: python:3.9
  services:
    - postgres:13
  variables:
    POSTGRES_DB: test_db
    POSTGRES_USER: test
    POSTGRES_PASSWORD: test
  script:
    - pip install -r requirements.txt
    - pytest

# Kubernetes runner
deploy:
  tags:
    - kubernetes
  script:
    - kubectl apply -f k8s/
```

### Runner Configuration
```toml
# config.toml for GitLab Runner
[[runners]]
  name = "my-runner"
  url = "https://gitlab.com/"
  token = "TOKEN"
  executor = "docker"
  
  [runners.docker]
    image = "node:18"
    privileged = false
    volumes = ["/cache"]
    
  [runners.cache]
    Type = "s3"
    Shared = true
    [runners.cache.s3]
      BucketName = "gitlab-runner-cache"
      BucketLocation = "us-east-1"
```

## Pipeline Features

### Multi-Project Pipelines
```yaml
trigger_project:
  trigger:
    project: other-group/other-project
    branch: main
    strategy: depend
```

### Parent-Child Pipelines
```yaml
# Parent pipeline
include:
  - local: 'ci/build.yml'
  - local: 'ci/test.yml'
  - local: 'ci/deploy.yml'

stages:
  - build
  - test
  - deploy
```

### DAG Pipelines
```yaml
stages:
  - build
  - test
  - deploy

build_app:
  stage: build
  script: npm run build

test_unit:
  stage: test
  script: npm run test:unit
  needs: [build_app]

test_integration:
  stage: test
  script: npm run test:integration
  needs: [build_app]

deploy:
  stage: deploy
  script: ./deploy.sh
  needs: [test_unit, test_integration]
```

## Cache and Artifacts

```yaml
# Cache for dependencies
build:
  cache:
    key:
      files:
        - package-lock.json
    paths:
      - node_modules/
    policy: pull-push
  
  script:
    - npm ci
    - npm run build

# Artifacts for build output
build:
  artifacts:
    paths:
      - dist/
      - build/
    expire_in: 1 week
    reports:
      junit: test-results.xml
      coverage_report:
        coverage_format: cobertura
        path: coverage/cobertura.xml
```

## Environment and Deployment

```yaml
deploy_staging:
  stage: deploy
  script:
    - helm upgrade --install my-app ./chart
  environment:
    name: staging
    url: https://staging.example.com
    on_stop: stop_staging
    auto_stop_in: 1 week
  
  rules:
    - if: $CI_COMMIT_BRANCH == "develop"

stop_staging:
  stage: deploy
  script:
    - helm uninstall my-app
  environment:
    name: staging
    action: stop
  
  rules:
    - if: $CI_COMMIT_BRANCH == "develop"
      when: manual
```

## Variables

```yaml
variables:
  # Job variables
  APP_ENV: production
  
  # File variables
  KUBECONFIG: /tmp/kubeconfig
  
  # Protected variables (set in UI)
  # AWS_ACCESS_KEY_ID
  # AWS_SECRET_ACCESS_KEY

deploy:
  script:
    - echo "Deploying to $APP_ENV"
    - aws s3 sync dist/ s3://my-bucket/
  variables:
    AWS_DEFAULT_REGION: us-east-1
```

## Best Practices

1. **Use YAML anchors** - Reduce repetition in pipeline files
2. **Leverage caching** - Speed up builds with proper caching
3. **Use rules instead of only/except** - More flexible pipeline control
4. **Implement proper environment management** - Use GitLab environments
5. **Secure secrets** - Use protected variables and CI/CD variables
6. **Use DAG pipelines** - Optimize pipeline execution order
7. **Implement merge request pipelines** - Test before merging
8. **Monitor pipeline performance** - Track job durations
9. **Use reusable includes** - Share pipeline components
10. **Document pipeline configuration** - Add comments for complex logic
