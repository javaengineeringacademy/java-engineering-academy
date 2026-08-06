package academy.javaengineering.senior.java21;

public class StringTemplatesDemo {

    public static void main(String[] args) {
        strProcessor();
        fmtProcessor();
        customProcessor();
    }

    // ==================== STR Template Processor ====================

    static void strProcessor() {
        System.out.println("=== STR Template Processor ===\n");

        String name = "Java";
        int version = 21;
        String greeting = STR."Hello, \{name} \{version}!";

        System.out.println(greeting);

        String lang = "Java";
        int year = 2023;
        String info = STR."""
            Language: \{lang}
            Version:  \{year}
            Status:   Active
            """;
        System.out.println(info);

        record Person(String name, int age) {}
        Person p = new Person("Alice", 30);
        String personStr = STR."Person: \{p.name()}, age \{p.age()}";
        System.out.println(personStr);

        String[] fruits = {"apple", "banana", "cherry"};
        String list = STR."Fruits: \{String.join(", ", fruits)}";
        System.out.println(list);

        int x = 5, y = 3;
        String math = STR."\{x} + \{y} = \{x + y}";
        System.out.println(math);
    }

    // ==================== FMT Template Processor ====================

    static void fmtProcessor() {
        System.out.println("\n=== FMT Template Processor ===\n");

        double pi = 3.14159265358979;
        System.out.println(FMT."Pi = %.4f".formatted(pi));

        int num = 42;
        System.out.println(FMT."Number: %5d".formatted(num));

        String name = "Engineering";
        System.out.println(FMT."|%-20s|".formatted(name));

        System.out.println(FMT."Hex: 0x%08X".formatted(255));

        System.out.println(FMT."Rate: %.2f%%".formatted(85.7));

        String[] items = {"first", "second", "third"};
        for (int i = 0; i < items.length; i++) {
            System.out.println(FMT."%d. %-10s".formatted(i + 1, items[i]));
        }
    }

    // ==================== Custom Template Processors ====================

    static void customProcessor() {
        System.out.println("\n=== Custom Template Processors ===\n");

        String html = HTML."<!DOCTYPE html><html><body><h1>\{\"Welcome\"}</h1></body></html>";
        System.out.println("HTML: " + html);

        String sql = SQL."SELECT * FROM users WHERE id = \{1}";
        System.out.println("SQL: " + sql);

        String yaml = YAML."name: \{\"my-app\"}\nversion: \{1}";
        System.out.println("YAML:\n" + yaml);

        String csv = CSV."name,age,city\n\{\"Alice\"},\{30},\{\"NYC\"}";
        System.out.println("CSV: " + csv);

        String upper = UPPER."hello world";
        System.out.println("Upper: " + upper);

        String reversed = REVERSE."abcdef";
        System.out.println("Reversed: " + reversed);
    }

    // ==================== Custom Processor Definitions ====================

    static final java.lang.StringTemplate.Processor<String, RuntimeException> HTML =
        template -> "<html>" + escapeHtml(template.interpolate()) + "</html>";

    static final java.lang.StringTemplate.Processor<String, RuntimeException> SQL =
        template -> template.interpolate().replaceAll("'", "''");

    static final java.lang.StringTemplate.Processor<String, RuntimeException> YAML =
        java.lang.StringTemplate.Processor.of(StringTemplate::interpolate);

    static final java.lang.StringTemplate.Processor<String, RuntimeException> CSV =
        java.lang.StringTemplate.Processor.of(StringTemplate::interpolate);

    static final java.lang.StringTemplate.Processor<String, RuntimeException> UPPER =
        template -> template.interpolate().toUpperCase();

    static final java.lang.StringTemplate.Processor<String, RuntimeException> REVERSE =
        template -> new StringBuilder(template.interpolate()).reverse().toString();

    private static String escapeHtml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
