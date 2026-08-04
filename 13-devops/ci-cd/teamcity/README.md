# TeamCity

## Overview

TeamCity is a CI/CD server by JetBrains that provides powerful build automation and continuous integration features.

## Build Configuration

### Basic Configuration (Kotlin DSL)
```kotlin
// .teamcity/settings.kts
project {
    vcsRoot(MyApp)
    
    buildType(MyApp_Build)
    buildType(MyApp_Test)
    buildType(MyApp_Deploy)
    
    params {
        param("env.NODE_ENV", "production")
    }
}

vcsRoot {
    vcsRoot(MyApp) {
        url = "https://github.com/org/app.git"
        branch = "refs/heads/main"
    }
}

buildType(MyApp_Build) {
    name = "Build"
    
    steps {
        script {
            scriptContent = """
                npm ci
                npm run build
            """.trimIndent()
        }
    }
    
    artifacts {
        artifactRules = "dist/** => .teamcity/artifacts"
    }
    
    triggers {
        vcs {
            branchFilter = "+:*"
        }
    }
}

buildType(MyApp_Test) {
    name = "Test"
    
    dependencies {
        snapshot(MyApp_Build) {}
    }
    
    steps {
        script {
            scriptContent = "npm test"
        }
    }
}

buildType(MyApp_Deploy) {
    name = "Deploy"
    
    dependencies {
        snapshot(MyApp_Test) {}
    }
    
    steps {
        script {
            scriptContent = "./deploy.sh"
        }
    }
    
    triggers {
        vcs {
            branchFilter = "+:main"
        }
    }
}
```

## Build Templates

```kotlin
// .teamcity/buildTypes/BuildTemplate.kts
template {
    name = "Node.js Build"
    
    params {
        param("node.version", "18")
    }
    
    steps {
        script {
            scriptContent = """
                node --version
                npm ci
                npm run build
            """.trimIndent()
        }
    }
    
    features {
        commitStatusPublisher {
            publisher = github {
                githubUrl = "https://api.github.com"
                authType = personalToken {
                    token = "credentialsJSON:github-token"
                }
            }
        }
    }
}

// Using template
buildType(MyApp_Build) {
    extends(template("Node.js Build"))
}
```

## Meta-Runners

```kotlin
// .teamcity/metaRunners/DeployRunner.xml
<meta-runner name="Deploy">
  <parameters>
    <param name="environment" defaultValue="staging" />
  </parameters>
  <runner name="Deploy" scriptType="bash">
    <script>
      <![CDATA[
        echo "Deploying to ${environment}"
        ./deploy.sh ${environment}
      ]]>
    </script>
  </runner>
</meta-runner>
```

## Best Practices

1. **Use Kotlin DSL** - Version control build configurations
2. **Implement templates** - Reuse common build steps
3. **Use meta-runners** - Share reusable build steps
4. **Secure credentials** - Use TeamCity's credential manager
5. **Implement build chains** - Chain related build configurations
6. **Use artifact dependencies** - Pass artifacts between builds
7. **Monitor build performance** - Track build times and failures
8. **Use VCS triggers** - Trigger builds on code changes
9. **Implement notifications** - Get notified of build status
10. **Document configurations** - Add descriptions for complex builds
