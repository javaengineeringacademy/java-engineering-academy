package academy.javaengineering.buildtools;

import java.util.HashMap;
import java.util.Map;

/**
 * Maven Fundamentals - POM, Dependencies, Lifecycle.
 */
public class MavenFundamentalsExample {

    public static class MavenProject {
        private final String groupId;
        private final String artifactId;
        private final String version;
        private final Map<String, String> dependencies = new HashMap<>();

        public MavenProject(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public void addDependency(String groupId, String artifactId, String version, String scope) {
            dependencies.put(artifactId, groupId + ":" + artifactId + ":" + version + ":" + scope);
        }

        public String getPomXml() {
            StringBuilder pom = new StringBuilder();
            pom.append("<project>\n");
            pom.append("  <groupId>").append(groupId).append("</groupId>\n");
            pom.append("  <artifactId>").append(artifactId).append("</artifactId>\n");
            pom.append("  <version>").append(version).append("</version>\n");
            pom.append("  <dependencies>\n");
            dependencies.forEach((name, dep) -> {
                String[] parts = dep.split(":");
                pom.append("    <dependency>\n");
                pom.append("      <groupId>").append(parts[0]).append("</groupId>\n");
                pom.append("      <artifactId>").append(parts[1]).append("</artifactId>\n");
                pom.append("      <version>").append(parts[2]).append("</version>\n");
                pom.append("      <scope>").append(parts[3]).append("</scope>\n");
                pom.append("    </dependency>\n");
            });
            pom.append("  </dependencies>\n");
            pom.append("</project>");
            return pom.toString();
        }
    }

    public static void main(String[] args) {
        MavenProject project = new MavenProject("com.example", "my-app", "1.0.0");
        project.addDependency("org.springframework", "spring-core", "6.1.0", "compile");
        project.addDependency("junit", "junit", "4.13.2", "test");
        System.out.println(project.getPomXml());
    }
}
