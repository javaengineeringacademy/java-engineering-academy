# AWS Developer Tools

## Overview

AWS Developer Tools provide a comprehensive set of services for developing, building, testing, and deploying applications on AWS.

## Services Overview

```
┌─────────────────────────────────────────────────────────┐
│                 AWS Dev Tools                            │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐             │
│  │CodeCommit│  │CodeBuild │  │CodeDeploy│             │
│  │  (Git)   │  │  (Build) │  │  (Deploy)│             │
│  └────┬─────┘  └────┬─────┘  └────┬─────┘             │
│       │              │              │                    │
│       └──────────────┴──────────────┘                    │
│                      │                                  │
│              ┌───────┴───────┐                          │
│              │ CodePipeline  │                          │
│              │  (CI/CD)      │                          │
│              └───────────────┘                          │
└─────────────────────────────────────────────────────────┘
```

## CodeCommit

### Create Repository
```bash
aws codecommit create-repository \
  --repository-name my-repo \
  --repository-description "My application repository"
```

### Git Operations
```bash
# Clone repository
git clone https://git-codecommit.us-east-1.amazonaws.com/v1/repos/my-repo

# Push code
git add .
git commit -m "Initial commit"
git push origin main
```

### Triggers
```bash
# Create trigger
aws codecommit create-trigger \
  --repository-name my-repo \
  --trigger-name my-trigger \
  --events all \
  --destination-arn arn:aws:sns:us-east-1:123456789012:my-topic
```

## CodeBuild

### Build Specification
```yaml
version: 0.2

env:
  variables:
    NODE_ENV: production

phases:
  install:
    runtime-versions:
      nodejs: 18
    commands:
      - npm install
  pre_build:
    commands:
      - npm test
  build:
    commands:
      - npm run build
  post_build:
    commands:
      - echo Build completed
artifacts:
  files:
    - '**/*'
```

### Create Build Project
```bash
aws codebuild create-project \
  --name my-build \
  --service-role arn:aws:iam::123456789012:role/CodeBuildRole \
  --artifacts type=NO_ARTIFACTS \
  --environment '{
    "type": "LINUX_CONTAINER",
    "image": "aws/codebuild/standard:7.0",
    "computeType": "BUILD_GENERAL1_SMALL"
  }' \
  --source '{
    "type": "CODECOMMIT",
    "location": "https://git-codecommit.us-east-1.amazonaws.com/v1/repos/my-repo"
  }'
```

### Start Build
```bash
# Start build
aws codebuild start-build --project-name my-build

# Get build status
aws codebuild batch-get-builds --ids build-id-12345678
```

## CodeDeploy

### Application Configuration
```yaml
# appspec.yml
version: 0.0
os: linux
files:
  - source: /
    destination: /var/www/html
hooks:
  BeforeInstall:
    - location: scripts/before_install.sh
      timeout: 300
  AfterInstall:
    - location: scripts/after_install.sh
      timeout: 300
  ApplicationStart:
    - location: scripts/start_server.sh
      timeout: 300
```

### Deployment Strategies
| Strategy           | Description                          |
|--------------------|--------------------------------------|
| AllAtOnce          | Deploy to all instances              |
| OneAtATime         | Deploy to one instance at a time     |
| HalfAtATime        | Deploy to 50% at a time             |
| Linear10PercentEvery3Minutes | 10% every 3 minutes     |

### Create Deployment Group
```bash
aws deploy create-deployment-group \
  --application-name my-app \
  --deployment-group-name my-deployment-group \
  --ec2-tag-filters Key=Environment,Value=production,Type=KEY_AND_VALUE \
  --auto-rollback-configuration '{
    "enabled": true,
    "events": ["DEPLOYMENT_FAILURE"]
  }' \
  --deployment-config-name CodeDeployDefault.OneAtATime
```

### Start Deployment
```bash
aws deploy create-deployment \
  --application-name my-app \
  --deployment-group-name my-deployment-group \
  --s3-location bucket=my-bucket,key=deployment.zip,bundleType=zip
```

## CodePipeline

### Pipeline Definition
```yaml
version: 0.2
stages:
  - name: Source
    actions:
      - name: SourceAction
        actionTypeId:
          category: Source
          owner: AWS
          provider: CodeCommit
          version: "1"
        configuration:
          RepositoryName: my-repo
          BranchName: main

  - name: Build
    actions:
      - name: BuildAction
        actionTypeId:
          category: Build
          owner: AWS
          provider: CodeBuild
          version: "1"
        configuration:
          ProjectName: my-build

  - name: Deploy
    actions:
      - name: DeployAction
        actionTypeId:
          category: Deploy
          owner: AWS
          provider: CodeDeploy
          version: "1"
        configuration:
          ApplicationName: my-app
          DeploymentGroupName: my-deployment-group
```

### Create Pipeline
```bash
aws codepipeline create-pipeline \
  --pipeline '{
    "name": "my-pipeline",
    "roleArn": "arn:aws:iam::123456789012:role/CodePipelineRole",
    "stages": [
      {
        "name": "Source",
        "actions": [
          {
            "name": "Source",
            "actionTypeId": {
              "category": "Source",
              "owner": "AWS",
              "provider": "CodeCommit",
              "version": "1"
            },
            "configuration": {
              "RepositoryName": "my-repo",
              "BranchName": "main"
            }
          }
        ]
      }
    ],
    "artifactStore": {
      "type": "S3",
      "location": "my-artifacts-bucket"
    }
  }'
```

## CodeStar

```bash
# Create project
aws codestar create-project \
  --name my-project \
  --id my-project \
  --source '{
    "code": {
      "codetoS3": {
        "s3Location": {
          "bucketName": "my-bucket",
          "objectKey": "project-template.zip"
        }
      }
    }
  }'
```

## CodeGuru

### CodeGuru Reviewer
```bash
# Associate repository
aws codeguru-reviewer associate-repository \
  --name my-review \
  --repository '{
    "codecommit": {
      "name": "my-repo"
    }
  }'
```

### CodeGuru Profiler
```bash
# Create profiling group
aws codeguru-profiler create-profiling-group \
  --profiling-group-name my-profiler
```

## Cloud9

```bash
# Create environment
aws cloud9 create-environment-ec2 \
  --name my-env \
  --instance-type t3.micro \
  --image-id amazonlinux-2023-x86_64
```

## CodeArtifact

```bash
# Create domain
aws codeartifact create-domain --domain my-domain

# Create repository
aws codeartifact create-repository \
  --domain my-domain \
  --repository my-repo

# Publish package
npm publish --registry https://my-domain-123456789012.d.codeartifact.us-east-1.amazonaws.com/npm/my-repo/
```

## Monitoring

```bash
# Get pipeline execution
aws codepipeline get-pipeline-execution \
  --pipeline-name my-pipeline \
  --pipeline-execution-id exec-id-12345678

# Get build reports
aws codebuild batch-get-reports \
  --report-arns arn:aws:codebuild:us-east-1:123456789012:report/my-report
```

## Best Practices

1. **Use CodeCommit** for source control
2. **Implement CodeBuild** for builds
3. **Use CodeDeploy** for deployments
4. **Automate with CodePipeline**
5. **Use CodeGuru** for code reviews
6. **Implement proper IAM roles**
7. **Enable CloudTrail** for auditing
8. **Use artifacts** for build outputs
9. **Implement rollback** strategies
10. **Monitor pipeline** executions
