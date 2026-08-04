# Azure DevOps Pipelines

## Overview

Azure DevOps Pipelines is a cloud-based CI/CD service that automates building, testing, and deploying code to any cloud or on-premises environment.

## Basic YAML Pipeline

```yaml
trigger:
  branches:
    include:
      - main
      - develop
  paths:
    include:
      - src/*

pool:
  vmImage: 'ubuntu-latest'

variables:
  buildConfiguration: 'Release'
  dotnetVersion: '8.0.x'

stages:
- stage: Build
  displayName: 'Build stage'
  jobs:
  - job: BuildJob
    displayName: 'Build job'
    steps:
    - task: UseDotNet@2
      displayName: 'Use .NET SDK'
      inputs:
        packageType: 'sdk'
        version: '$(dotnetVersion)'
    
    - script: dotnet restore
      displayName: 'Restore dependencies'
    
    - script: dotnet build --configuration $(buildConfiguration)
      displayName: 'Build project'
    
    - task: DotNetCoreCLI@2
      displayName: 'Run tests'
      inputs:
        command: 'test'
        projects: '**/*Tests.csproj'
        arguments: '--configuration $(buildConfiguration) --collect:"XPlat Code Coverage"'
    
    - task: PublishCodeCoverageResults@1
      displayName: 'Publish code coverage'
      inputs:
        codeCoverageTool: 'Cobertura'
        summaryFileLocation: '$(Agent.TempDirectory)/**/coverage.cobertura.xml'

- stage: Deploy
  displayName: 'Deploy stage'
  dependsOn: Build
  condition: and(succeeded(), eq(variables['Build.SourceBranch'], 'refs/heads/main'))
  jobs:
  - deployment: DeployStaging
    displayName: 'Deploy to staging'
    environment: 'staging'
    strategy:
      runOnce:
        deploy:
          steps:
          - task: AzureWebApp@1
            displayName: 'Deploy to Azure Web App'
            inputs:
              azureSubscription: 'Azure-Connection'
              appName: 'my-app-staging'
              package: '$(Build.ArtifactStagingDirectory)/**/*.zip'
```

## Multi-Stage Pipelines

```yaml
stages:
- stage: Build
  jobs:
  - job: Build
    steps:
    - task: DotNetCoreCLI@2
      inputs:
        command: 'publish'
        publishWebProjects: true
        arguments: '--configuration $(buildConfiguration) --output $(Build.ArtifactStagingDirectory)'
    - task: PublishBuildArtifacts@1
      inputs:
        pathToPublish: '$(Build.ArtifactStagingDirectory)'

- stage: Test
  dependsOn: Build
  jobs:
  - job: UnitTest
    steps:
    - task: DotNetCoreCLI@2
      inputs:
        command: 'test'
        projects: '**/*Tests.csproj'
  - job: IntegrationTest
    steps:
    - script: npm run test:integration

- stage: DeployDev
  dependsOn: Test
  jobs:
  - deployment: Dev
    environment: 'development'
    strategy:
      runOnce:
        deploy:
          steps:
          - task: AzureRmWebAppDeployment@4
            inputs:
              azureSubscription: 'Azure-Connection'
              WebAppName: 'my-app-dev'

- stage: DeployStaging
  dependsOn: DeployDev
  condition: succeeded()
  jobs:
  - deployment: Staging
    environment: 'staging'
    strategy:
      runOnce:
        deploy:
          steps:
          - task: AzureRmWebAppDeployment@4
            inputs:
              azureSubscription: 'Azure-Connection'
              WebAppName: 'my-app-staging'

- stage: DeployProduction
  dependsOn: DeployStaging
  condition: succeeded()
  jobs:
  - deployment: Production
    environment: 'production'
    strategy:
      runOnce:
        deploy:
          steps:
          - task: AzureRmWebAppDeployment@4
            inputs:
              azureSubscription: 'Azure-Connection'
              WebAppName: 'my-app-prod'
```

## Templates

### Task Templates
```yaml
# templates/build-template.yml
parameters:
  - name: buildConfiguration
    type: string
    default: 'Release'
  - name: dotnetVersion
    type: string
    default: '8.0.x'

steps:
- task: UseDotNet@2
  inputs:
    version: '${{ parameters.dotnetVersion }}'

- script: dotnet build --configuration ${{ parameters.buildConfiguration }}
  displayName: 'Build'

- task: DotNetCoreCLI@2
  inputs:
    command: 'test'
    arguments: '--configuration ${{ parameters.buildConfiguration }}'
```

### Job Templates
```yaml
# templates/test-job.yml
parameters:
  - name: testProjects
    type: object

jobs:
- ${{ each(project in parameters.testProjects) }}:
  - job: Test_${{ replace(project, '.', '_') }}
    displayName: 'Test ${{ project }}'
    steps:
    - task: DotNetCoreCLI@2
      inputs:
        command: 'test'
        projects: '${{ project }}'
```

## Deployment Strategies

```yaml
- deployment: Production
  environment: 'production'
  strategy:
    runOnce:
      deploy:
        steps:
        - script: echo "Deploying..."
    rolling:
      maxParallel: 2
      deploy:
        steps:
        - script: echo "Rolling deploy..."
  healthMonitor:
    enabled: true
    timeout: 10
```

## Best Practices

1. **Use multi-stage pipelines** - Separate build, test, and deploy stages
2. **Leverage templates** - Reuse pipeline components
3. **Implement approval gates** - Control production deployments
4. **Use environments** - Manage deployment targets
5. **Secure secrets** - Use variable groups and Azure Key Vault
6. **Implement parallel jobs** - Speed up pipeline execution
7. **Use branch policies** - Protect main branch
8. **Monitor pipelines** - Track performance and failures
9. **Use matrix builds** - Test across multiple configurations
10. **Document pipelines** - Add comments for complex logic
