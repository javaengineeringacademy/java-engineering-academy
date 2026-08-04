@REM Maven Wrapper script for Windows
@echo off
setlocal

set MAVEN_OPTS=-Xmx512m

set WRAPPER_JAR=.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=.mvn\wrapper\maven-wrapper.properties

if exist "%WRAPPER_JAR%" goto runWrapper

echo Downloading Maven Wrapper...
curl -sL -o "%WRAPPER_JAR%" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

:runWrapper
java %MAVEN_OPTS% -jar "%WRAPPER_JAR%" %*
