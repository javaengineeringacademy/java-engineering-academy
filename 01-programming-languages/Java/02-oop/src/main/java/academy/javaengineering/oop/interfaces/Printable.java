package academy.javaengineering.oop.interfaces;

public interface Printable {

    String format();

    default String printWithHeader() {
        return "=== DOCUMENT ===\n" + format();
    }

    static String version() {
        return "1.0";
    }
}
