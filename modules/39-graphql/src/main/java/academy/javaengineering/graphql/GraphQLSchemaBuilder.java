package academy.javaengineering.graphql;

import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.GraphQL;

/**
 * Demonstrates GraphQL schema building.
 */
public class GraphQLSchemaBuilder {

    public GraphQL buildGraphQL() {
        String schema = """
            type Query {
                products: [Product]
                product(id: ID!): Product
            }
            
            type Product {
                id: ID!
                name: String!
                description: String
                price: Float!
                stock: Int!
            }
            """;

        SchemaParser schemaParser = new SchemaParser();
        var typeDefinitionRegistry = schemaParser.parse(schema);

        GraphQLDataFetchers dataFetchers = new GraphQLDataFetchers();

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
            .type("Query", builder -> builder
                .dataFetcher("products", dataFetchers.getAllProducts())
                .dataFetcher("product", dataFetchers.getProductById())
            )
            .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        var executableSchema = schemaGenerator.makeExecutableSchema(
            typeDefinitionRegistry, runtimeWiring);

        return GraphQL.newGraphQL(executableSchema).build();
    }
}
