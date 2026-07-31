package academy.javaengineering.oop.solid;

/**
 * FaxMachine - Interface Segregation: Only faxing functionality.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface FaxMachine {

    void fax(String document);
}