package academy.javaengineering.exercises;

import java.util.*;
import java.util.stream.*;

/**
 * Exercises: Structural Design Patterns (Adapter, Decorator, Proxy)
 *
 * Complete the TODO sections below.
 */
public class StructuralExercises {

    // TODO 1: Implement an Adapter pattern
    // Adapt a legacy CelsiusTemperature interface to a new KelvinTemperature interface
    public interface CelsiusTemperature {
        double getCelsius();
        void setCelsius(double celsius);
    }

    public interface KelvinTemperature {
        double getKelvin();
        void setKelvin(double kelvin);
    }

    public static class SimpleCelsius implements CelsiusTemperature {
        private double celsius;

        public SimpleCelsius(double celsius) {
            this.celsius = celsius;
        }

        @Override public double getCelsius() { return celsius; }
        @Override public void setCelsius(double celsius) { this.celsius = celsius; }
    }

    public static class CelsiusToKelvinAdapter implements KelvinTemperature {
        private final CelsiusTemperature celsius;

        public CelsiusToKelvinAdapter(CelsiusTemperature celsius) {
            this.celsius = celsius;
        }

        @Override
        public double getKelvin() {
            // TODO: implement conversion
            return 0;
        }

        @Override
        public void setKelvin(double kelvin) {
            // TODO: implement conversion
        }
    }

    // TODO 2: Implement a Decorator pattern for a TextProcessor
    public interface TextProcessor {
        String process(String text);
    }

    public static class SimpleTextProcessor implements TextProcessor {
        @Override
        public String process(String text) {
            return text;
        }
    }

    public static class UpperCaseDecorator implements TextProcessor {
        private final TextProcessor wrapped;

        public UpperCaseDecorator(TextProcessor wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public String process(String text) {
            // TODO: implement - wrap and uppercase
            return "";
        }
    }

    public static class TrimDecorator implements TextProcessor {
        private final TextProcessor wrapped;

        public TrimDecorator(TextProcessor wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public String process(String text) {
            // TODO: implement - wrap and trim
            return "";
        }
    }

    public static class ReplaceDecorator implements TextProcessor {
        private final TextProcessor wrapped;
        private final String target;
        private final String replacement;

        public ReplaceDecorator(TextProcessor wrapped, String target, String replacement) {
            this.wrapped = wrapped;
            this.target = target;
            this.replacement = replacement;
        }

        @Override
        public String process(String text) {
            // TODO: implement - wrap and replace
            return "";
        }
    }

    // TODO 3: Implement a simple Proxy pattern for access control
    public interface Document {
        String getContent(String user);
        void setContent(String user, String content);
    }

    public static class RealDocument implements Document {
        private String content;

        public RealDocument(String content) {
            this.content = content;
        }

        @Override
        public String getContent(String user) { return content; }

        @Override
        public void setContent(String user, String content) { this.content = content; }
    }

    public static class AccessControlProxy implements Document {
        private final RealDocument document;
        private final Set<String> allowedReaders;
        private final Set<String> allowedWriters;

        public AccessControlProxy(RealDocument document, Set<String> readers, Set<String> writers) {
            this.document = document;
            this.allowedReaders = readers;
            this.allowedWriters = writers;
        }

        @Override
        public String getContent(String user) {
            // TODO: implement access check
            return "";
        }

        @Override
        public void setContent(String user, String content) {
            // TODO: implement access check
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        StructuralExercises exercises = new StructuralExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== StructuralExercises Tests ===\n");

        // Test 1 - Adapter
        total++;
        CelsiusToKelvinAdapter adapter = new CelsiusToKelvinAdapter(new SimpleCelsius(0));
        double kelvin = adapter.getKelvin();
        if (Math.abs(kelvin - 273.15) < 0.01) {
            System.out.println("Test 1a PASSED: CelsiusToKelvinAdapter getKelvin");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: expected 273.15, got " + kelvin);
        }

        total++;
        adapter.setKelvin(373.15);
        CelsiusToKelvinAdapter adapter2 = new CelsiusToKelvinAdapter(new SimpleCelsius(100));
        if (Math.abs(adapter2.getKelvin() - 373.15) < 0.01) {
            System.out.println("Test 1b PASSED: CelsiusToKelvinAdapter setKelvin");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: setKelvin");
        }

        // Test 2 - Decorator
        total++;
        TextProcessor processor = new ReplaceDecorator(
            new UpperCaseDecorator(
                new TrimDecorator(new SimpleTextProcessor())
            ), "WORLD", "JAVA"
        );
        String result = processor.process("  hello world  ");
        if ("HELLO JAVA".equals(result)) {
            System.out.println("Test 2 PASSED: Decorator chain");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: Decorator chain - '" + result + "'");
        }

        // Test 3 - Proxy
        total++;
        RealDocument doc = new RealDocument("secret content");
        Set<String> readers = Set.of("alice", "bob");
        Set<String> writers = Set.of("alice");
        AccessControlProxy proxy = new AccessControlProxy(doc, readers, writers);
        String content = proxy.getContent("alice");
        if ("secret content".equals(content)) {
            System.out.println("Test 3a PASSED: Proxy allowed read");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: Proxy allowed read - '" + content + "'");
        }

        total++;
        proxy.setContent("alice", "new content");
        String newContent = proxy.getContent("bob");
        if ("new content".equals(newContent)) {
            System.out.println("Test 3b PASSED: Proxy allowed write");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: Proxy allowed write");
        }

        total++;
        String denied = proxy.getContent("charlie");
        if (denied == null || denied.isEmpty() || denied.contains("denied") || denied.contains("Access")) {
            System.out.println("Test 3c PASSED: Proxy denied access");
            passed++;
        } else {
            System.out.println("Test 3c FAILED: Proxy should deny access");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
