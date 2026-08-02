package academy.javaengineering.graphql;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class GraphQlExample {

    public static class GraphQLField {
        private final String name;
        private final String type;
        private final boolean nullable;
        private final List<String> arguments;

        public GraphQLField(String name, String type, boolean nullable, List<String> arguments) {
            this.name = name;
            this.type = type;
            this.nullable = nullable;
            this.arguments = arguments;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public boolean isNullable() { return nullable; }
        public List<String> getArguments() { return arguments; }
    }

    public static class GraphQLType {
        private final String name;
        private final Map<String, GraphQLField> fields;
        private final String kind;

        public GraphQLType(String name, String kind) {
            this.name = name;
            this.fields = new HashMap<>();
            this.kind = kind;
        }

        public void addField(GraphQLField field) {
            fields.put(field.getName(), field);
        }

        public GraphQLField getField(String name) {
            return fields.get(name);
        }

        public String getName() { return name; }
        public Map<String, GraphQLField> getFields() { return fields; }
        public String getKind() { return kind; }
    }

    public static class GraphQLSchema {
        private final Map<String, GraphQLType> types;
        private GraphQLType queryType;
        private GraphQLType mutationType;
        private GraphQLType subscriptionType;

        public GraphQLSchema() {
            this.types = new HashMap<>();
        }

        public void addType(GraphQLType type) {
            types.put(type.getName(), type);
        }

        public void setQueryType(GraphQLType type) {
            this.queryType = type;
        }

        public void setMutationType(GraphQLType type) {
            this.mutationType = type;
        }

        public GraphQLType getType(String name) {
            return types.get(name);
        }

        public boolean validate() {
            return queryType != null;
        }

        public Map<String, GraphQLType> getTypes() { return types; }
        public GraphQLType getQueryType() { return queryType; }
        public GraphQLType getMutationType() { return mutationType; }
    }

    public static class GraphQLQuery {
        private final String operation;
        private final Map<String, Object> variables;
        private final List<String> fields;

        public GraphQLQuery(String operation, Map<String, Object> variables, List<String> fields) {
            this.operation = operation;
            this.variables = variables;
            this.fields = fields;
        }

        public String getOperation() { return operation; }
        public Map<String, Object> getVariables() { return variables; }
        public List<String> getFields() { return fields; }
    }

    public static class DataLoader<K, V> {
        private final java.util.function.Function<List<K>, List<V>> batchLoader;
        private final Map<K, V> cache;
        private final List<K> pendingKeys;

        public DataLoader(java.util.function.Function<List<K>, List<V>> batchLoader) {
            this.batchLoader = batchLoader;
            this.cache = new HashMap<>();
            this.pendingKeys = new java.util.concurrent.CopyOnWriteArrayList<>();
        }

        public V load(K key) {
            if (cache.containsKey(key)) {
                return cache.get(key);
            }
            pendingKeys.add(key);
            return null;
        }

        public void dispatch() {
            if (pendingKeys.isEmpty()) return;
            List<K> keysToLoad = new java.util.ArrayList<>(pendingKeys);
            pendingKeys.clear();

            List<V> results = batchLoader.apply(keysToLoad);
            for (int i = 0; i < keysToLoad.size() && i < results.size(); i++) {
                cache.put(keysToLoad.get(i), results.get(i));
            }
        }

        public V get(K key) {
            return cache.get(key);
        }

        public void clear() {
            cache.clear();
            pendingKeys.clear();
        }
    }

    public static class ResolverResult {
        private final Object data;
        private final List<String> errors;

        public ResolverResult(Object data, List<String> errors) {
            this.data = data;
            this.errors = errors;
        }

        public Object getData() { return data; }
        public List<String> getErrors() { return errors; }
        public boolean hasErrors() { return errors != null && !errors.isEmpty(); }
    }

    public static ResolverResult executeQuery(GraphQLSchema schema, GraphQLQuery query) {
        if (!schema.validate()) {
            return new ResolverResult(null, List.of("Schema must define a Query type"));
        }

        if (query.getFields().isEmpty()) {
            return new ResolverResult(null, List.of("Query must select at least one field"));
        }

        Map<String, Object> result = new HashMap<>();
        List<String> errors = new java.util.ArrayList<>();

        for (String field : query.getFields()) {
            GraphQLType queryType = schema.getQueryType();
            if (queryType.getField(field) == null) {
                errors.add("Field '" + field + "' not found on Query type");
            } else {
                result.put(field, "mock_data_" + field);
            }
        }

        return new ResolverResult(result, errors);
    }

    public static void main(String[] args) {
        GraphQLSchema schema = new GraphQLSchema();

        GraphQLType userType = new GraphQLType("User", "OBJECT");
        userType.addField(new GraphQLField("id", "ID", false, List.of()));
        userType.addField(new GraphQLField("name", "String", false, List.of()));
        userType.addField(new GraphQLField("email", "String", false, List.of()));
        schema.addType(userType);

        GraphQLType queryType = new GraphQLType("Query", "OBJECT");
        queryType.addField(new GraphQLField("user", "User", true, List.of("id")));
        queryType.addField(new GraphQLField("users", "[User]", false, List.of()));
        schema.setQueryType(queryType);

        GraphQLType mutationType = new GraphQLType("Mutation", "OBJECT");
        mutationType.addField(new GraphQLField("createUser", "User", false, List.of("name", "email")));
        schema.setMutationType(mutationType);

        System.out.println("Schema valid: " + schema.validate());

        DataLoader<String, Map<String, Object>> userLoader = new DataLoader<>(
            ids -> {
                System.out.println("Batch loading users: " + ids);
                return ids.stream()
                    .map(id -> Map.<String, Object>of("id", id, "name", "User " + id))
                    .toList();
            }
        );

        userLoader.load("1");
        userLoader.load("2");
        userLoader.load("3");
        userLoader.dispatch();

        System.out.println("User 1: " + userLoader.get("1"));
        System.out.println("User 2: " + userLoader.get("2"));

        GraphQLQuery query = new GraphQLQuery("query", Map.of(), List.of("user", "users"));
        ResolverResult result = executeQuery(schema, query);
        System.out.println("Query result: " + result.getData());
        System.out.println("Errors: " + result.getErrors());

        System.out.println("GraphQL Example Complete");
    }
}
