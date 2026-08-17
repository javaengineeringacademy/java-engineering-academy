# Maven - Quiz

## Questions

### Q1: What is Apache Maven?
- A) A Java compiler
- B) A build automation and dependency management tool
- C) An IDE
- D) A testing framework

### Q2: What file does Maven use?
- A) build.gradle
- B) pom.xml
- C) Makefile
- D) package.json

### Q3: What does `mvn compile` do?
- A) Runs tests
- B) Compiles Java source code
- C) Packages the project
- D) Installs to local repo

### Q4: What is a Maven repository?
- A) A source code folder
- B) A storage location for dependencies
- C) A test folder
- D) A documentation site

### Q5: What is the local repository?
- A) `~/.m2/repository`
- B) The project directory
- C) The Maven installation
- D) A remote server

### Q6: What is `groupId` in Maven?
- A) The project name
- B) The organization that created the artifact
- C) The module name
- D) The version number

### Q7: What is `artifactId`?
- A) The organization name
- B) The unique name of the project/module
- C) The version number
- D) The group name

### Q8: What does `mvn test` do?
- A) Compiles code
- B) Runs test classes
- C) Packages code
- D) Deploys code

### Q9: What is a Maven plugin?
- A) A Java library
- B) A component that adds build lifecycle functionality
- C) A test framework
- D) A dependency

### Q10: What is `mvn clean install`?
- A) Deletes all files
- B) Cleans previous build and installs to local repository
- C) Installs Maven
- D) Creates a new project

## Answers

1. **B** - Maven automates builds and manages dependencies
2. **B** - `pom.xml` (Project Object Model) is Maven's configuration file
3. **B** - `mvn compile` compiles Java source files
4. **B** - Repositories store JAR files and dependencies
5. **A** - Local repository is at `~/.m2/repository`
6. **B** - `groupId` identifies the organization (e.g., `org.apache`)
7. **B** - `artifactId` is the project's unique name
8. **B** - `mvn test` executes test classes (Surefire plugin)
9. **B** - Plugins provide build lifecycle functionality
10. **B** - Clean removes old artifacts; install copies to local repo
