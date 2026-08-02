package academy.javaengineering.linux;

import java.util.Map;
import java.util.HashMap;

/**
 * Demonstrates Linux environment variables.
 */
public class LinuxEnvironment {

    public Map<String, String> getEnvironmentVariables() {
        return new HashMap<>(System.getenv());
    }

    public String getEnvironmentVariable(String name) {
        return System.getenv(name);
    }

    public String getPath() {
        return System.getenv("PATH");
    }

    public String getHomeDirectory() {
        return System.getenv("HOME");
    }

    public String getUserName() {
        return System.getenv("USER");
    }

    public String getJavaHome() {
        return System.getenv("JAVA_HOME");
    }
}
