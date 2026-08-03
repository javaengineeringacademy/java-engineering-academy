package academy.javaengineering.buildtools;

import java.util.HashMap;
import java.util.Map;

/**
 * Dependency Management - Versioning, Conflict Resolution, BOM.
 */
public class DependencyManagementExample {

    public static class Dependency {
        private final String groupId;
        private final String artifactId;
        private final String version;

        public Dependency(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        public String getCoordinates() { return groupId + ":" + artifactId + ":" + version; }
        public String getVersion() { return version; }
    }

    public static class DependencyResolver {
        private final Map<String, Dependency> resolved = new HashMap<>();

        public void resolve(Dependency dep) {
            String key = dep.groupId + ":" + dep.artifactId;
            Dependency existing = resolved.get(key);
            if (existing == null || compareVersions(dep.version, existing.version) > 0) {
                resolved.put(key, dep);
            }
        }

        private int compareVersions(String v1, String v2) {
            String[] parts1 = v1.split("\\.");
            String[] parts2 = v2.split("\\.");
            for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
                int p1 = Integer.parseInt(parts1[i]);
                int p2 = Integer.parseInt(parts2[i]);
                if (p1 != p2) return Integer.compare(p1, p2);
            }
            return Integer.compare(parts1.length, parts2.length);
        }

        public Map<String, Dependency> getResolved() { return resolved; }
    }

    public static void main(String[] args) {
        DependencyResolver resolver = new DependencyResolver();
        resolver.resolve(new Dependency("org.springframework", "spring-core", "6.0.0"));
        resolver.resolve(new Dependency("org.springframework", "spring-core", "6.1.0"));
        resolver.getResolved().forEach((k, v) ->
                System.out.println(k + " -> " + v.getVersion()));
    }
}
