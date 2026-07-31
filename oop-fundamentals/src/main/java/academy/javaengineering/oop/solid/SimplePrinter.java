package academy.javaengineering.oop.solid;

/**
 * SimplePrinter - Implements only Printer interface (ISP).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SimplePrinter implements Printer {

    @Override
    public void print(String document) {
        System.out.println("  [PRINTER] Printing: " + document);
    }
}