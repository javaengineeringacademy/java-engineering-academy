public class ObjectReferenceDemo {
    public static void main(String[] args) {
        Person person = new Person("Alice");
        System.out.println("Before method call: " + person.getName());
        
        modifyObject(person);
        
        System.out.println("After method call: " + person.getName());
        System.out.println("Expected: Bob (object modified through reference)");
    }

    public static void modifyObject(Person p) {
        System.out.println("Inside method, before modification: " + p.getName());
        p.setName("Bob");
        System.out.println("Inside method, after modification: " + p.getName());
    }
}

class Person {
    private String name;
    
    public Person(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}