#!/bin/sh
# Maven Wrapper script

set -e

MAVEN_OPTS="${MAVEN_OPTS:--Xmx512m}"
export MAVEN_OPTS

WRAPPER_JAR=".mvn/wrapper/maven-wrapper.jar"
WRAPPER_PROPERTIES=".mvn/wrapper/maven-wrapper.properties"

# Resolve project directory (required by Maven Wrapper 3.2+)
if [ -z "$MAVEN_PROJECTBASEDIR" ]; then
    MAVEN_PROJECTBASEDIR=$(cd "$(dirname "$0")" && pwd)
    export MAVEN_PROJECTBASEDIR
fi

if [ -r "$WRAPPER_JAR" ]; then
    JAVACMD="java"
else
    JAVACMD="java"
    if [ ! -x "$(command -v java)" ]; then
        echo "Error: Java is not installed" >&2
        exit 1
    fi
fi

if [ -r "$WRAPPER_PROPERTIES" ]; then
    while IFS="=" read -r key value; do
        case "$key" in (distributionUrl) DISTRIBUTION_URL="$value"; break ;;
        esac
    done < "$WRAPPER_PROPERTIES"
fi

if [ ! -r "$WRAPPER_JAR" ]; then
    echo "Downloading Maven Wrapper..."
    if command -v curl > /dev/null; then
        curl -sL -o "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    elif command -v wget > /dev/null; then
        wget -q -O "$WRAPPER_JAR" "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    fi
fi

exec "$JAVACMD" $MAVEN_OPTS \
    -Dmaven.multiModuleProjectDirectory="$MAVEN_PROJECTBASEDIR" \
    -cp "$WRAPPER_JAR" org.apache.maven.wrapper.MavenWrapperMain "$@"
