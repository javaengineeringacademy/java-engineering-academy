package academy.javaengineering.graphql;

import academy.javaengineering.graphql.GraphQlExample.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GraphQlTest {

    private GraphQLSchema schema;

    @BeforeEach
    void setUp() {
        schema = new GraphQLSchema();

        GraphQLType userType = new GraphQLType("User", "OBJECT");
        userType.addField(new GraphQLField("id", "ID", false, List.of()));
        userType.addField(new GraphQLField("name", "String", false, List.of()));
        userType.addField(new GraphQLField("email", "String", false, List.of()));
        schema.addType(userType);

        GraphQLType queryType = new GraphQLType("Query", "OBJECT");
        queryType.addField(new GraphQLField("user", "User", true, List.of("id")));
        queryType.addField(new GraphQLField("users", "[User]", false, List.of()));
        schema.setQueryType(queryType);
    }

    @Test
    void testSchemaCreation() {
        assertNotNull(schema);
        assertTrue(schema.validate());
        assertEquals(1, schema.getTypes().size());
    }

    @Test
    void testTypeWithFields() {
        GraphQLType type = schema.getType("User");
        assertNotNull(type);
        assertEquals(3, type.getFields().size());
        assertNotNull(type.getField("id"));
        assertNotNull(type.getField("name"));
        assertNull(type.getField("nonexistent"));
    }

    @Test
    void testSchemaValidation() {
        GraphQLSchema invalidSchema = new GraphQLSchema();
        assertFalse(invalidSchema.validate());
    }

    @Test
    void testQueryExecution() {
        GraphQLQuery query = new GraphQLQuery("query", Map.of(), List.of("user", "users"));
        ResolverResult result = GraphQlExample.executeQuery(schema, query);
        assertFalse(result.hasErrors());
        assertNotNull(result.getData());
    }

    @Test
    void testQueryWithInvalidField() {
        GraphQLQuery query = new GraphQLQuery("query", Map.of(), List.of("invalidField"));
        ResolverResult result = GraphQlExample.executeQuery(schema, query);
        assertTrue(result.hasErrors());
    }

    @Test
    void testQueryEmptyFields() {
        GraphQLQuery query = new GraphQLQuery("query", Map.of(), List.of());
        ResolverResult result = GraphQlExample.executeQuery(schema, query);
        assertTrue(result.hasErrors());
    }

    @Test
    void testDataLoader() {
        DataLoader<String, String> loader = new DataLoader<>(
            keys -> keys.stream().map(k -> "value_" + k).toList()
        );

        loader.load("a");
        loader.load("b");
        loader.dispatch();

        assertEquals("value_a", loader.get("a"));
        assertEquals("value_b", loader.get("b"));
    }

    @Test
    void testDataLoaderCaching() {
        DataLoader<String, String> loader = new DataLoader<>(
            keys -> keys.stream().map(k -> "loaded_" + k).toList()
        );

        loader.load("x");
        loader.dispatch();
        String first = loader.get("x");

        loader.load("x");
        loader.dispatch();
        String second = loader.get("x");

        assertEquals(first, second);
    }

    @Test
    void testDataLoaderClear() {
        DataLoader<String, String> loader = new DataLoader<>(
            keys -> keys.stream().map(k -> "val").toList()
        );

        loader.load("1");
        loader.dispatch();
        assertNotNull(loader.get("1"));

        loader.clear();
        assertNull(loader.get("1"));
    }

    @Test
    void testMutationType() {
        GraphQLType mutationType = new GraphQLType("Mutation", "OBJECT");
        mutationType.addField(new GraphQLField("createUser", "User", false, List.of("name")));
        schema.setMutationType(mutationType);

        assertNotNull(schema.getMutationType());
        assertNotNull(schema.getMutationType().getField("createUser"));
    }

    @Test
    void testResolverResult() {
        ResolverResult success = new ResolverResult(Map.of("key", "value"), List.of());
        assertFalse(success.hasErrors());
        assertEquals("value", ((Map<?, ?>) success.getData()).get("key"));

        ResolverResult error = new ResolverResult(null, List.of("Something went wrong"));
        assertTrue(error.hasErrors());
    }

    @Test
    void testGraphQLFieldProperties() {
        GraphQLField field = new GraphQLField("test", "String", true, List.of("arg1"));
        assertEquals("test", field.getName());
        assertEquals("String", field.getType());
        assertTrue(field.isNullable());
        assertEquals(1, field.getArguments().size());
    }

    @Test
    void testBatchLoading() {
        DataLoader<Integer, Integer> loader = new DataLoader<>(
            keys -> keys.stream().map(k -> k * 2).toList()
        );

        for (int i = 0; i < 5; i++) {
            loader.load(i);
        }
        loader.dispatch();

        for (int i = 0; i < 5; i++) {
            assertEquals(i * 2, loader.get(i));
        }
    }
}
