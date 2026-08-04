# Jenkins Pipeline

## Overview

Jenkins is an open-source automation server used for building, testing, and deploying software. It supports CI/CD through pipelines defined as code.

## Pipeline Concepts

### Declarative Pipeline
```groovy
pipeline {
    agent any
    
    environment {
        APP_NAME = 'my-app'
        VERSION = '1.0.0'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
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
        always {
            cleanWs()
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
```

### Scripted Pipeline
```groovy
node {
    try {
        stage('Checkout') {
            checkout scm
        }
        
        stage('Build') {
            sh 'mvn clean package'
        }
        
        stage('Test') {
            sh 'mvn test'
        }
        
        stage('Deploy') {
            if (env.BRANCH_NAME == 'main') {
                sh './deploy.sh'
            }
        }
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        cleanWs()
    }
}
```

## Agent Types

### Distributed Agents
```groovy
// Docker agent
pipeline {
    agent {
        docker {
            image 'maven:3.8-openjdk-11'
            args '-v $HOME/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
    }
}

// Kubernetes agent
pipeline {
    agent {
        kubernetes {
            yaml """
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: maven
                    image: maven:3.8-openjdk-11
                    command: ['cat']
                    tty: true
            """
        }
    }
    stages {
        stage('Build') {
            steps {
                container('maven') {
                    sh 'mvn clean package'
                }
            }
        }
    }
}
```

## Shared Libraries

### Library Structure
```
vars/
  buildAndTest.groovy
  deploy.groovy
  notifySlack.groovy
src/
  com/
    company/
      utils/
        DockerUtils.groovy
resources/
  templates/
    deployment.yaml
```

### Using Shared Libraries
```groovy
@Library('my-shared-library') _

pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                script {
                    buildAndTest(
                        tool: 'maven',
                        args: '-DskipTests=false'
                    )
                }
            }
        }
        
        stage('Deploy') {
            steps {
                script {
                    deploy(
                        environment: 'staging',
                        replicas: 3
                    )
                }
            }
        }
    }
    
    post {
        success {
            notifySlack(channel: '#deployments')
        }
    }
}
```

### Shared Library Implementation
```groovy
// vars/buildAndTest.groovy
def call(Map config) {
    def tool = config.tool ?: 'maven'
    def args = config.args ?: ''
    
    stage("Build with ${tool}") {
        if (tool == 'maven') {
            sh "mvn clean package ${args}"
        } else if (tool == 'gradle') {
            sh "./gradlew build ${args}"
        }
    }
    
    stage('Test') {
        if (tool == 'maven') {
            sh 'mvn test'
        } else if (tool == 'gradle') {
            sh './gradlew test'
        }
    }
}
```

## Credentials Management

### Using Credentials
```groovy
pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'aws-credentials',
                        usernameVariable: 'AWS_ACCESS_KEY_ID',
                        passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                    ),
                    file(
                        credentialsId: 'kubeconfig',
                        variable: 'KUBECONFIG'
                    )
                ]) {
                    sh '''
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        kubectl apply -f deployment.yaml
                    '''
                }
            }
        }
    }
}
```

## Parallel Execution

```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('Parallel Tests') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        sh 'mvn test -Dtest=UnitTests'
                    }
                }
                stage('Integration Tests') {
                    steps {
                        sh 'mvn test -Dtest=IntegrationTests'
                    }
                }
                stage('Performance Tests') {
                    steps {
                        sh 'mvn test -Dtest=PerformanceTests'
                    }
                }
            }
        }
        
        stage('Deploy') {
            steps {
                sh './deploy.sh'
            }
        }
    }
}
```

## Best Practices

1. **Store Jenkinsfile in version control** - Keep pipeline as code
2. **Use shared libraries** - Reuse common pipeline logic
3. **Minimize credentials exposure** - Use Jenkins credentials store
4. **Use declarative pipelines** - More readable and maintainable
5. **Implement proper error handling** - Use try-catch-finally blocks
6. **Cache dependencies** - Speed up builds with proper caching
7. **Use parallel stages** - Reduce build time
8. **Monitor pipeline performance** - Track build times and failures
9. **Keep agents updated** - Regular updates and patches
10. **Document pipelines** - Add comments for complex logic
