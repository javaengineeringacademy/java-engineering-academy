package academy.javaengineering.oop.abstractclasses;

/**
 * Developer - Concrete implementation of abstract Employee class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Developer extends Employee {

    private final String primaryLanguage;

    public Developer(String name, int id, String primaryLanguage) {
        super(name, id);
        this.primaryLanguage = primaryLanguage;
    }

    @Override
    public String getRole() {
        return "Software Developer (" + primaryLanguage + ")";
    }

    @Override
    public double calculatePay() {
        return 95000.00; // Annual salary
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }
}