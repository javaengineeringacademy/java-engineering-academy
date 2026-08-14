package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Method Design Patterns ===\n");

        // WHY: Methods define contracts. Clear method signatures reduce coupling.
        // INTERNAL: JVM uses invokespecial/virtual/invokeinterface based on call site
        // ENGINEERING: Return this for fluent APIs, return void for side effects

        Calculator calc = new Calculator();
        int result = calc.add(2, 3).multiply(4).getResult();
        System.out.println("((2+3)*4) = " + result);

        // TRADE-OFF: Fluent API vs explicit steps
        // Fluent: readable for configuration, harder to debug
        // Explicit: easier to step through, more verbose
        Query query = new Query()
            .select("name", "age")
            .from("users")
            .where("age > 18")
            .orderBy("name");
        System.out.println("Query: " + query);
    }
}

class Calculator {
    private int result = 0;

    public Calculator add(int a, int b) {
        this.result = a + b;
        return this;
    }

    public Calculator multiply(int factor) {
        this.result *= factor;
        return this;
    }

    public int getResult() { return result; }
}

class Query {
    private String select, from, where, orderBy;

    public Query select(String... cols) { this.select = String.join(",", cols); return this; }
    public Query from(String table) { this.from = table; return this; }
    public Query where(String cond) { this.where = cond; return this; }
    public Query orderBy(String col) { this.orderBy = col; return this; }

    @Override
    public String toString() {
        return "SELECT " + select + " FROM " + from + 
               (where != null ? " WHERE " + where : "") +
               (orderBy != null ? " ORDER BY " + orderBy : "");
    }
}
