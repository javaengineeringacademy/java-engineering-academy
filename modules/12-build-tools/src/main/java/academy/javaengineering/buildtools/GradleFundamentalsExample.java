package academy.javaengineering.buildtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Gradle Fundamentals - Build Scripts, Tasks, Dependencies.
 */
public class GradleFundamentalsExample {

    public static class GradleBuild {
        private final String project;
        private final List<String> plugins = new ArrayList<>();
        private final List<String> dependencies = new ArrayList<>();

        public GradleBuild(String project) {
            this.project = project;
        }

        public void applyPlugin(String plugin) { plugins.add(plugin); }

        public void addDependency(String configuration, String dependency) {
            dependencies.add(configuration + " '" + dependency + "'");
        }

        public String getBuildScript() {
            StringBuilder script = new StringBuilder();
            script.append("plugins {\n");
            plugins.forEach(p -> script.append("    id '").append(p).append("'\n"));
            script.append("}\n\n");
            script.append("dependencies {\n");
            dependencies.forEach(d -> script.append("    ").append(d).append("\n"));
            script.append("}\n");
            return script.toString();
        }
    }

    public static void main(String[] args) {
        GradleBuild build = new GradleBuild("my-app");
        build.applyPlugin("java");
        build.applyPlugin("org.springframework.boot");
        build.addDependency("implementation", "org.springframework.boot:spring-boot-starter-web");
        build.addDependency("testImplementation", "org.junit.jupiter:junit-jupiter:5.10.0");
        System.out.println(build.getBuildScript());
    }
}
