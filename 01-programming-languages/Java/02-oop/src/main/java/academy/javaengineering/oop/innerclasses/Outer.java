package academy.javaengineering.oop.innerclasses;

public class Outer {

    private String outerField;

    public Outer(String outerField) {
        this.outerField = outerField;
    }

    // Non-static inner class
    public class Inner {

        public String getInnerInfo() {
            return "Inner sees: " + outerField;
        }

        public String getOuterClass() {
            return Outer.this.getClass().getSimpleName();
        }
    }

    // Static nested class
    public static class StaticNested {

        private final String nestedField;

        public StaticNested(String nestedField) {
            this.nestedField = nestedField;
        }

        public String getInfo() {
            return "StaticNested: " + nestedField;
        }
    }

    public String getOuterField() { return outerField; }
}
