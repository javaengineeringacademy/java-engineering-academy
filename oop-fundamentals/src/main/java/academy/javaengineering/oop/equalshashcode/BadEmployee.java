package academy.javaengineering.oop.equalshashcode;

/**
 * BadEmployee - Example of BROKEN equals/hashCode contract (DO NOT DO THIS).
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class BadEmployee {

    private final int id;
    private final String name;

    public BadEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BadEmployee other = (BadEmployee) obj;
        return id == other.id && java.util.Objects.equals(name, other.name);
    }

    // BAD! equals() uses id and name, but hashCode() only uses name
    // This BREAKS the contract!
    @Override
    public int hashCode() {
        return java.util.Objects.hash(name); // Missing id!
    }

    @Override
    public String toString() {
        return "BadEmployee{id=" + id + ", name='" + name + "'}";
    }
}