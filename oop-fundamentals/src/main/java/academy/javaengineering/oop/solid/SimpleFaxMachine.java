package academy.javaengineering.oop.solid;

/**
 * SimpleFaxMachine - Implements only FaxMachine interface (ISP).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class SimpleFaxMachine implements FaxMachine {

    @Override
    public void fax(String document) {
        System.out.println("  [FAX] Faxing: " + document);
    }
}