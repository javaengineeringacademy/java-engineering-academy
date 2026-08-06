package academy.javaengineering.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstrates real-world reflection use cases including:
 * - Dependency injection container
 * - Object-relational mapping (ORM)
 * - Serialization framework
 * - Property mapping
 * - Builder pattern via reflection
 * - Test discovery
 */
public class RealWorldReflection {

    // === Annotations for DI and ORM ===

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Inject {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Service {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Repository {}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Column {
        String name() default "";
        int length() default 255;
        boolean nullable() default true;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Table {
        String name() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface JsonProperty {
        String name() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Test {
        String description() default "";
    }

    // === Domain classes ===

    @Table(name = "users")
    static class User {
        @Column(name = "user_id")
        @JsonProperty
        private Long id;

        @Column(name = "user_name", length = 100)
        @JsonProperty
        private String name;

        @Column(name = "user_email", nullable = false)
        @JsonProperty
        private String email;

        @Column(name = "user_age")
        @JsonProperty
        private int age;

        public User() {}

        public User(Long id, String name, String email, int age) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.age = age;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public int getAge() { return age; }

        @Override
        public String toString() {
            return "User{id=" + id + ", name='" + name + "', email='" + email + "', age=" + age + "}";
        }
    }

    @Table(name = "orders")
    static class Order {
        @Column(name = "order_id")
        @JsonProperty
        private Long id;

        @Column(name = "user_id")
        @JsonProperty
        private Long userId;

        @Column(name = "product")
        @JsonProperty
        private String product;

        @Column(name = "amount")
        @JsonProperty
        private double amount;

        public Order() {}

        public Order(Long id, Long userId, String product, double amount) {
            this.id = id;
            this.userId = userId;
            this.product = product;
            this.amount = amount;
        }

        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public String getProduct() { return product; }
        public double getAmount() { return amount; }
    }

    // === Dependency Injection Container ===

    static class DIContainer {
        private final Map<Class<?>, Object> singletons = new LinkedHashMap<>();
        private final Map<Class<?>, Class<?>> implementations = new LinkedHashMap<>();

        public <T> void register(Class<T> interfaceType, Class<? extends T> implType) {
            implementations.put(interfaceType, implType);
        }

        public <T> void registerSingleton(Class<T> type, T instance) {
            singletons.put(type, instance);
        }

        @SuppressWarnings("unchecked")
        public <T> T resolve(Class<T> type) throws ReflectiveOperationException {
            // Check singletons first
            if (singletons.containsKey(type)) {
                return (T) singletons.get(type);
            }

            Class<?> implType = implementations.getOrDefault(type, type);
            Constructor<?> constructor = findConstructor(implType);
            Object instance = constructor.newInstance();

            // Inject dependencies
            injectFields(instance);

            if (Modifier.isAbstract(implType.getModifiers()) || Modifier.isInterface(implType.getModifiers())) {
                throw new IllegalArgumentException("Cannot instantiate: " + implType);
            }

            singletons.put(type, instance);
            return (T) instance;
        }

        private Constructor<?> findConstructor(Class<?> type) throws ReflectiveOperationException {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            // Prefer default constructor
            for (Constructor<?> ctor : constructors) {
                if (ctor.getParameterCount() == 0) {
                    ctor.setAccessible(true);
                    return ctor;
                }
            }
            // Use first constructor
            if (constructors.length > 0) {
                constructors[0].setAccessible(true);
                return constructors[0];
            }
            throw new ReflectiveOperationException("No constructors found for " + type);
        }

        private void injectFields(Object instance) throws ReflectiveOperationException {
            Class<?> clazz = instance.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    Object dependency = resolve(field.getType());
                    field.set(instance, dependency);
                }
            }
        }
    }

    // === ORM Framework ===

    static class ORMFramework {
        public static String generateCreateTableSQL(Class<?> entityClass) {
            Table table = entityClass.getAnnotation(Table.class);
            String tableName = table != null && !table.name().isEmpty()
                    ? table.name()
                    : entityClass.getSimpleName().toLowerCase();

            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE ").append(tableName).append(" (\n");

            List<String> columns = new ArrayList<>();
            for (Field field : entityClass.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String colName = column.name().isEmpty()
                            ? field.getName()
                            : column.name();
                    String colType = mapJavaTypeToSQL(field.getType());
                    StringBuilder colDef = new StringBuilder("  ").append(colName).append(" ").append(colType);
                    if (!column.nullable()) {
                        colDef.append(" NOT NULL");
                    }
                    columns.add(colDef.toString());
                }
            }

            sql.append(String.join(",\n", columns));
            sql.append("\n);");
            return sql.toString();
        }

        public static Map<String, Object> entityToMap(Object entity) throws ReflectiveOperationException {
            Map<String, Object> map = new LinkedHashMap<>();
            Class<?> clazz = entity.getClass();

            Table table = clazz.getAnnotation(Table.class);
            if (table != null) {
                map.put("_table", table.name().isEmpty() ? clazz.getSimpleName().toLowerCase() : table.name());
            }

            for (Field field : clazz.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    field.setAccessible(true);
                    String colName = column.name().isEmpty() ? field.getName() : column.name();
                    map.put(colName, field.get(entity));
                }
            }
            return map;
        }

        public static <T> T mapToEntity(Map<String, Object> data, Class<T> entityClass) throws ReflectiveOperationException {
            Constructor<T> ctor = entityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            T entity = ctor.newInstance();

            for (Field field : entityClass.getDeclaredFields()) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    String colName = column.name().isEmpty() ? field.getName() : column.name();
                    if (data.containsKey(colName)) {
                        field.setAccessible(true);
                        field.set(entity, data.get(colName));
                    }
                }
            }
            return entity;
        }

        private static String mapJavaTypeToSQL(Class<?> type) {
            if (type == Long.class || type == long.class) return "BIGINT";
            if (type == Integer.class || type == int.class) return "INTEGER";
            if (type == Double.class || type == double.class) return "DOUBLE";
            if (type == Float.class || type == float.class) return "REAL";
            if (type == Boolean.class || type == boolean.class) return "BOOLEAN";
            if (type == String.class) return "VARCHAR(255)";
            if (type == java.util.Date.class) return "TIMESTAMP";
            return "TEXT";
        }
    }

    // === JSON Serialization Framework ===

    static class JsonFramework {
        public static String toJson(Object obj) throws ReflectiveOperationException {
            Class<?> clazz = obj.getClass();
            StringBuilder json = new StringBuilder("{\n");

            List<String> entries = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                JsonProperty prop = field.getAnnotation(JsonProperty.class);
                if (prop != null) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    String propName = prop.name().isEmpty() ? field.getName() : prop.name();
                    entries.add("  \"" + propName + "\": " + formatValue(value));
                }
            }

            json.append(String.join(",\n", entries));
            json.append("\n}");
            return json.toString();
        }

        public static <T> T fromJson(String json, Class<T> clazz) throws ReflectiveOperationException {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            T obj = ctor.newInstance();

            // Simple JSON parser (handles flat objects)
            json = json.trim();
            if (json.startsWith("{") && json.endsWith("}")) {
                json = json.substring(1, json.length() - 1).trim();
            }

            String[] pairs = json.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replace("\"", "");
                    String value = keyValue[1].trim();

                    for (Field field : clazz.getDeclaredFields()) {
                        JsonProperty prop = field.getAnnotation(JsonProperty.class);
                        if (prop != null) {
                            String propName = prop.name().isEmpty() ? field.getName() : prop.name();
                            if (propName.equals(key)) {
                                field.setAccessible(true);
                                field.set(obj, parseValue(value, field.getType()));
                                break;
                            }
                        }
                    }
                }
            }
            return obj;
        }

        private static String formatValue(Object value) {
            if (value == null) return "null";
            if (value instanceof String) return "\"" + value + "\"";
            if (value instanceof Number || value instanceof Boolean) return value.toString();
            return "\"" + value + "\"";
        }

        private static Object parseValue(String value, Class<?> type) {
            value = value.trim();
            if (value.equals("null")) return null;
            if (type == String.class) return value.replace("\"", "");
            if (type == long.class || type == Long.class) return Long.parseLong(value);
            if (type == int.class || type == Integer.class) return Integer.parseInt(value);
            if (type == double.class || type == Double.class) return Double.parseDouble(value);
            if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);
            return value;
        }
    }

    // === Property Mapper ===

    static class PropertyMapper {
        public static <T> T map(Object source, Class<T> targetClass) throws ReflectiveOperationException {
            T target = targetClass.getDeclaredConstructor().newInstance();
            Class<?> sourceClass = source.getClass();

            for (Field targetField : targetClass.getDeclaredFields()) {
                for (Field sourceField : sourceClass.getDeclaredFields()) {
                    if (targetField.getName().equals(sourceField.getName())
                            && targetField.getType().equals(sourceField.getType())) {
                        sourceField.setAccessible(true);
                        targetField.setAccessible(true);
                        targetField.set(target, sourceField.get(source));
                        break;
                    }
                }
            }
            return target;
        }

        public static Map<String, Object> toMap(Object obj) throws ReflectiveOperationException {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        }
    }

    // === Test Discovery ===

    static class TestDiscovery {
        public static List<String> discoverTests(Class<?> testClass) {
            List<String> tests = new ArrayList<>();
            for (Method method : testClass.getDeclaredMethods()) {
                Test testAnnotation = method.getAnnotation(Test.class);
                if (testAnnotation != null) {
                    String desc = testAnnotation.description().isEmpty()
                            ? method.getName()
                            : testAnnotation.description();
                    tests.add(method.getName() + " - " + desc);
                }
            }
            return tests;
        }
    }

    // === Demo methods ===

    public static void demonstrateDIContainer() {
        System.out.println("=== Dependency Injection Container ===");
        DIContainer container = new DIContainer();
        container.registerSingleton(String.class, "SingletonService");

        try {
            User user = container.resolve(User.class);
            System.out.println("Created User: " + user);
        } catch (ReflectiveOperationException e) {
            System.out.println("DI error: " + e.getMessage());
        }
    }

    public static void demonstrateORM() {
        System.out.println("\n=== ORM Framework ===");

        // Generate CREATE TABLE SQL
        String userSQL = ORMFramework.generateCreateTableSQL(User.class);
        System.out.println("User table SQL:\n" + userSQL);

        String orderSQL = ORMFramework.generateCreateTableSQL(Order.class);
        System.out.println("\nOrder table SQL:\n" + orderSQL);

        // Entity to Map
        try {
            User user = new User(1L, "Alice", "alice@example.com", 30);
            Map<String, Object> map = ORMFramework.entityToMap(user);
            System.out.println("\nUser entity to map: " + map);

            // Map to entity
            User restored = ORMFramework.mapToEntity(map, User.class);
            System.out.println("Map to entity: " + restored);
        } catch (ReflectiveOperationException e) {
            System.err.println("ORM error: " + e.getMessage());
        }
    }

    public static void demonstrateJsonSerialization() {
        System.out.println("\n=== JSON Serialization ===");
        try {
            User user = new User(1L, "Bob", "bob@example.com", 25);
            String json = JsonFramework.toJson(user);
            System.out.println("Serialized:\n" + json);

            User deserialized = JsonFramework.fromJson(json, User.class);
            System.out.println("Deserialized: " + deserialized);
        } catch (ReflectiveOperationException e) {
            System.err.println("JSON error: " + e.getMessage());
        }
    }

    public static void demonstratePropertyMapping() {
        System.out.println("\n=== Property Mapping ===");
        try {
            User source = new User(1L, "Charlie", "charlie@example.com", 35);
            Order mapped = PropertyMapper.map(source, Order.class);
            System.out.println("Mapped User to Order: id=" + mapped.getId()
                    + ", userId=" + mapped.getUserId()
                    + ", product=" + mapped.getProduct());

            Map<String, Object> sourceMap = PropertyMapper.toMap(source);
            System.out.println("User to map: " + sourceMap);
        } catch (ReflectiveOperationException e) {
            System.err.println("Property mapping error: " + e.getMessage());
        }
    }

    public static void demonstrateAnnotationProcessing() {
        System.out.println("\n=== Annotation Processing ===");

        // Find all @Service classes
        System.out.println("Classes with @Column annotations in User:");
        for (Field field : User.class.getDeclaredFields()) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                System.out.println("  " + field.getName() + " -> column: " + col.name()
                        + ", length: " + col.length() + ", nullable: " + col.nullable());
            }
        }

        // Find fields with @JsonProperty
        System.out.println("\nJSON-serializable fields in User:");
        for (Field field : User.class.getDeclaredFields()) {
            JsonProperty prop = field.getAnnotation(JsonProperty.class);
            if (prop != null) {
                System.out.println("  " + field.getName() + " -> json: "
                        + (prop.name().isEmpty() ? field.getName() : prop.name()));
            }
        }
    }

    static class SampleTests {
        @Test(description = "User should have valid name")
        void testUserName() {}

        @Test(description = "User email should not be null")
        void testUserEmail() {}

        @Test
        void testOrderAmount() {}
    }

    public static void demonstrateTestDiscovery() {
        System.out.println("\n=== Test Discovery ===");
        List<String> tests = TestDiscovery.discoverTests(SampleTests.class);
        System.out.println("Found " + tests.size() + " tests:");
        tests.forEach(t -> System.out.println("  " + t));
    }

    public static void main(String[] args) {
        demonstrateDIContainer();
        demonstrateORM();
        demonstrateJsonSerialization();
        demonstratePropertyMapping();
        demonstrateAnnotationProcessing();
        demonstrateTestDiscovery();
    }
}
