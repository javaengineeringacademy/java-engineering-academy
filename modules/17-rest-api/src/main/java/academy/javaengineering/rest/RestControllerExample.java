package academy.javaengineering.rest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RestControllerExample {

    private final Map<Long, Map<String, Object>> users = new ConcurrentHashMap<>();
    private long idCounter = 1;

    public static class RestResponse {
        private final int status;
        private final Object body;
        private final String location;

        public RestResponse(int status, Object body, String location) {
            this.status = status;
            this.body = body;
            this.location = location;
        }

        public int getStatus() { return status; }
        public Object getBody() { return body; }
        public String getLocation() { return location; }
    }

    public RestResponse getAllUsers() {
        List<Map<String, Object>> userList = new ArrayList<>(users.values());
        return new RestResponse(200, userList, null);
    }

    public RestResponse getUser(Long id) {
        Map<String, Object> user = users.get(id);
        if (user == null) {
            return new RestResponse(404, Map.of("error", "User not found"), null);
        }
        return new RestResponse(200, user, null);
    }

    public RestResponse createUser(Map<String, Object> userData) {
        if (!userData.containsKey("name") || !userData.containsKey("email")) {
            return new RestResponse(400, Map.of("error", "Name and email are required"), null);
        }

        Long id = idCounter++;
        Map<String, Object> user = new HashMap<>(userData);
        user.put("id", id);
        user.put("createdAt", System.currentTimeMillis());
        users.put(id, user);

        return new RestResponse(201, user, "/api/users/" + id);
    }

    public RestResponse updateUser(Long id, Map<String, Object> userData) {
        if (!users.containsKey(id)) {
            return new RestResponse(404, Map.of("error", "User not found"), null);
        }

        Map<String, Object> user = new HashMap<>(users.get(id));
        user.putAll(userData);
        user.put("id", id);
        user.put("updatedAt", System.currentTimeMillis());
        users.put(id, user);

        return new RestResponse(200, user, null);
    }

    public RestResponse deleteUser(Long id) {
        if (!users.containsKey(id)) {
            return new RestResponse(404, Map.of("error", "User not found"), null);
        }

        users.remove(id);
        return new RestResponse(204, null, null);
    }

    public RestResponse searchUsers(String name, String email) {
        List<Map<String, Object>> results = users.values().stream()
                .filter(user -> {
                    boolean matches = true;
                    if (name != null) {
                        matches = matches && user.get("name").toString().contains(name);
                    }
                    if (email != null) {
                        matches = matches && user.get("email").toString().contains(email);
                    }
                    return matches;
                })
                .toList();

        return new RestResponse(200, results, null);
    }

    public static void main(String[] args) {
        RestControllerExample controller = new RestControllerExample();

        System.out.println("=== REST Controller Demo ===\n");

        System.out.println("--- POST /api/users ---");
        RestResponse response = controller.createUser(
                Map.of("name", "John Doe", "email", "john@example.com"));
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());
        System.out.println("Location: " + response.getLocation());

        System.out.println("\n--- POST /api/users (invalid) ---");
        response = controller.createUser(Map.of("name", "Jane"));
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- GET /api/users ---");
        response = controller.getAllUsers();
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- GET /api/users/1 ---");
        response = controller.getUser(1L);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- PUT /api/users/1 ---");
        response = controller.updateUser(1L, Map.of("name", "John Updated"));
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());

        System.out.println("\n--- DELETE /api/users/1 ---");
        response = controller.deleteUser(1L);
        System.out.println("Status: " + response.getStatus());

        System.out.println("\n--- GET /api/users/999 ---");
        response = controller.getUser(999L);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Body: " + response.getBody());
    }
}
