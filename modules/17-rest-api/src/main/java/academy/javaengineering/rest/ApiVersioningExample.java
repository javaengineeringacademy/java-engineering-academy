package academy.javaengineering.rest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ApiVersioningExample {

    private final Map<String, Map<Long, Map<String, Object>>> versionedResources = new ConcurrentHashMap<>();

    public static class VersionedResponse {
        private final String version;
        private final Object data;
        private final Map<String, String> headers;

        public VersionedResponse(String version, Object data, Map<String, String> headers) {
            this.version = version;
            this.data = data;
            this.headers = headers;
        }

        public String getVersion() { return version; }
        public Object getData() { return data; }
        public Map<String, String> getHeaders() { return headers; }
    }

    public ApiVersioningExample() {
        versionedResources.put("v1", new ConcurrentHashMap<>());
        versionedResources.put("v2", new ConcurrentHashMap<>());
    }

    public String resolveVersion(String uri, String headerVersion, String acceptHeader) {
        if (uri.contains("/v1/")) return "v1";
        if (uri.contains("/v2/")) return "v2";

        if (headerVersion != null) return headerVersion;

        if (acceptHeader != null) {
            if (acceptHeader.contains("v1")) return "v1";
            if (acceptHeader.contains("v2")) return "v2";
        }

        return "v2";
    }

    public VersionedResponse getResource(String version, Long id) {
        Map<Long, Map<String, Object>> resources = versionedResources.get(version);
        if (resources == null) {
            return new VersionedResponse(version, Map.of("error", "Unsupported version"), null);
        }

        Map<String, Object> resource = resources.get(id);
        if (resource == null) {
            return new VersionedResponse(version, Map.of("error", "Resource not found"), null);
        }

        return new VersionedResponse(version, resource, Map.of("X-API-Version", version));
    }

    public VersionedResponse createResource(String version, Map<String, Object> data) {
        Map<Long, Map<String, Object>> resources = versionedResources.get(version);
        if (resources == null) {
            return new VersionedResponse(version, Map.of("error", "Unsupported version"), null);
        }

        Long id = (long) (resources.size() + 1);
        Map<String, Object> resource = new HashMap<>(data);
        resource.put("id", id);

        if ("v2".equals(version)) {
            resource.put("createdAt", System.currentTimeMillis());
            resource.put("version", "v2");
        }

        resources.put(id, resource);

        return new VersionedResponse(version, resource, Map.of("X-API-Version", version, "Location", "/api/" + version + "/resources/" + id));
    }

    public static void main(String[] args) {
        ApiVersioningExample api = new ApiVersioningExample();

        System.out.println("=== API Versioning Demo ===\n");

        System.out.println("--- URI Versioning ---");
        String version = api.resolveVersion("/api/v1/users", null, null);
        System.out.println("URI /api/v1/users -> Version: " + version);

        version = api.resolveVersion("/api/v2/users", null, null);
        System.out.println("URI /api/v2/users -> Version: " + version);

        System.out.println("\n--- Header Versioning ---");
        version = api.resolveVersion("/api/users", "1", null);
        System.out.println("Header X-API-Version: 1 -> Version: " + version);

        System.out.println("\n--- Accept Header Versioning ---");
        version = api.resolveVersion("/api/users", null, "application/vnd.api.v1+json");
        System.out.println("Accept: application/vnd.api.v1+json -> Version: " + version);

        System.out.println("\n--- Create Resource v1 ---");
        VersionedResponse response = api.createResource("v1", Map.of("name", "Resource 1"));
        System.out.println("Created: " + response.getData());
        System.out.println("Headers: " + response.getHeaders());

        System.out.println("\n--- Create Resource v2 ---");
        response = api.createResource("v2", Map.of("name", "Resource 2"));
        System.out.println("Created: " + response.getData());
        System.out.println("Headers: " + response.getHeaders());

        System.out.println("\n--- Get Resource v1 ---");
        response = api.getResource("v1", 1L);
        System.out.println("Version: " + response.getVersion());
        System.out.println("Data: " + response.getData());

        System.out.println("\n--- Get Resource v2 ---");
        response = api.getResource("v2", 1L);
        System.out.println("Version: " + response.getVersion());
        System.out.println("Data: " + response.getData());

        System.out.println("\n--- Unsupported Version ---");
        response = api.getResource("v3", 1L);
        System.out.println("Response: " + response.getData());
    }
}
