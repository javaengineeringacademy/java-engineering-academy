# Travis CI

## Overview

Travis CI is a hosted CI/CD service used to build and test software projects. It uses `.travis.yml` for configuration.

## Basic Configuration

```yaml
language: node_js

node_js:
  - '18'
  - '20'

cache:
  directories:
    - node_modules

branches:
  only:
    - main
    - develop

install:
  - npm ci

script:
  - npm run lint
  - npm test
  - npm run build

after_success:
  - npm run coverage

deploy:
  provider: pages
  skip_cleanup: true
  github_token: $GITHUB_TOKEN
  local_dir: dist
  on:
    branch: main
```

## Build Stages

```yaml
language: python

stages:
  - lint
  - test
  - name: deploy
    if: branch = main

jobs:
  include:
    - stage: lint
      script: flake8 .
    
    - stage: test
      python: '3.9'
      script: pytest tests/
    
    - stage: test
      python: '3.10'
      script: pytest tests/
    
    - stage: deploy
      script: ./deploy.sh
```

## Matrix Builds

```yaml
language: node_js

node_js:
  - '16'
  - '18'
  - '20'

os:
  - linux
  - osx

env:
  - NODE_ENV=test
  - NODE_ENV=production

matrix:
  exclude:
    - node_js: '16'
      os: osx
  allow_failures:
    - node_js: '20'
```

## Services

```yaml
language: python

services:
  - postgresql
  - redis
  - docker

before_script:
  - psql -c 'create database test_db;' -U postgres
  - redis-server --daemonize yes

script:
  - pytest tests/

after_success:
  - docker build -t my-app .
  - docker push my-app
```

## Caching

```yaml
language: ruby

cache:
  bundler: true
  directories:
    - node_modules
    - vendor/bundle

before_install:
  - npm install

script:
  - bundle exec rspec
```

## Environment Variables

```yaml
language: node_js

env:
  global:
    - APP_ENV=test
    - secure: "encrypted_key_here"

script:
  - echo "Running in $APP_ENV"
  - npm test
```

## Best Practices

1. **Use build stages** - Organize builds into logical stages
2. **Implement caching** - Speed up builds with proper caching
3. **Use matrix builds** - Test across multiple environments
4. **Secure secrets** - Use encrypted environment variables
5. **Use branch filters** - Control when builds run
6. **Monitor build performance** - Track build times and failures
7. **Use deployment providers** - Leverage built-in deployment support
8. **Implement notifications** - Get notified of build status
9. **Use Travis CI CLI** - Manage builds from command line
10. **Document configuration** - Add comments for complex logic
