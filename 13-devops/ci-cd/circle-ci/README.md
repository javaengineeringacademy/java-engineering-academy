# CircleCI

## Overview

CircleCI is a cloud-based CI/CD platform that automates software build, test, and deployment. It uses YAML configuration and supports orbs for reusable packages.

## Basic Configuration

```yaml
version: 2.1

orbs:
  node: circleci/node@5.1
  docker: circleci/docker@2.4
  aws-cli: circleci/aws-cli@4.1

workflows:
  build-and-deploy:
    jobs:
      - build
      - test:
          requires:
            - build
      - deploy:
          requires:
            - test
          filters:
            branches:
              only: main

jobs:
  build:
    docker:
      - image: cimg/node:18.17
    steps:
      - checkout
      - restore_cache:
          keys:
            - v1-dependencies-{{ checksum "package-lock.json" }}
            - v1-dependencies-
      - run:
          name: Install dependencies
          command: npm ci
      - save_cache:
          paths:
            - node_modules
          key: v1-dependencies-{{ checksum "package-lock.json" }}
      - run:
          name: Build
          command: npm run build
      - persist_to_workspace:
          root: .
          paths:
            - dist/
            - node_modules/

  test:
    docker:
      - image: cimg/node:18.17
      - image: cimg/postgres:15.0
        environment:
          POSTGRES_DB: test_db
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
    steps:
      - checkout
      - attach_workspace:
          at: .
      - run:
          name: Run tests
          command: npm test

  deploy:
    docker:
      - image: cimg/aws:2023.11
    steps:
      - checkout
      - attach_workspace:
          at: .
      - aws-cli/setup
      - run:
          name: Deploy to AWS
          command: |
            aws s3 sync dist/ s3://my-bucket/
            aws cloudfront create-invalidation \
              --distribution-id $CLOUDFRONT_ID \
              --paths "/*"
```

## Orbs

### Using Orbs
```yaml
version: 2.1

orbs:
  node: circleci/node@5.1
  docker: circleci/docker@2.4
  kubernetes: circleci/kubernetes@1.3
  helm: circleci/helm@2.0

workflows:
  deploy:
    jobs:
      - node/test
      - docker/publish:
          requires:
            - node/test
          image: my-org/my-app
          tag: $CIRCLE_SHA1
      - kubernetes/install
      - helm/install:
          requires:
            - docker/publish
          command: upgrade --install my-app ./chart
```

### Creating Custom Orbs
```yaml
version: 2.1

description: Custom orb for deployment

commands:
  deploy:
    description: Deploy to environment
    parameters:
      environment:
        type: enum
        enum: [staging, production]
      tag:
        type: string
        default: latest
    steps:
      - run:
          name: Deploy to << parameters.environment >>
          command: |
            echo "Deploying << parameters.tag >> to << parameters.environment >>"
            ./deploy.sh << parameters.environment >> << parameters.tag >>

jobs:
  deploy:
    parameters:
      environment:
        type: enum
        enum: [staging, production]
    docker:
      - image: cimg/base:current
    steps:
      - checkout
      - deploy:
          environment: << parameters.environment >>

workflows:
  deploy:
    jobs:
      - deploy:
          environment: staging
```

## Workflows

### Conditional Workflows
```yaml
workflows:
  version: 2
  
  build-test-deploy:
    jobs:
      - build:
          filters:
            tags:
              only: /.*/
      
      - test:
          requires:
            - build
      
      - deploy_staging:
          requires:
            - test
          filters:
            branches:
              only: develop
      
      - deploy_production:
          requires:
            - test
          filters:
            branches:
              only: main
            tags:
              only: /^v.*/
```

### Matrix Jobs
```yaml
workflows:
  matrix:
    jobs:
      - test:
          matrix:
            parameters:
              node-version: ["16", "18", "20"]
              os: ["linux", "macos"]
            exclude:
              - node-version: "16"
                os: "macos"
```

## Caching and Workspaces

```yaml
jobs:
  build:
    steps:
      - restore_cache:
          keys:
            - v1-deps-{{ checksum "package-lock.json" }}
            - v1-deps-
      - run: npm ci
      - save_cache:
          paths:
            - node_modules
          key: v1-deps-{{ checksum "package-lock.json" }}
      - persist_to_workspace:
          root: .
          paths:
            - node_modules
            - dist/

  test:
    steps:
      - attach_workspace:
          at: .
      - run: npm test
```

## Environment Variables

```yaml
jobs:
  deploy:
    environment:
      APP_ENV: production
    steps:
      - run:
          name: Deploy
          command: |
            echo "Deploying to $APP_ENV"
            ./deploy.sh
```

## Best Practices

1. **Use orbs** - Leverage community and custom orbs
2. **Implement caching** - Speed up builds with dependency caching
3. **Use workspaces** - Pass data between jobs efficiently
4. **Use filters wisely** - Control when jobs run
5. **Implement parallelism** - Use matrix builds for multiple environments
6. **Secure secrets** - Use environment variables and contexts
7. **Monitor pipelines** - Track performance and failures
8. **Use config splitting** - Keep config files manageable
9. **Test configuration** - Use circleci config validate
10. **Document pipelines** - Add comments for complex logic
