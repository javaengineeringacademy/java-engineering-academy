package academy.javaengineering.oop.solid;

/**
 * SimpleScanner - Implements only Scanner interface (ISP).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SimpleScanner implements Scanner {

    @Override
    public void scan(String document) {
        System.out.println("  [SCANNER] Scanning: " + document);
    }
}