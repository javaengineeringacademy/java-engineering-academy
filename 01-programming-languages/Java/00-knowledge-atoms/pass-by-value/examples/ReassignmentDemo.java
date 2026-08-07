public class ReassignmentDemo {
    public static void main(String[] args) {
        Person person = new Person("Alice");
        System.out.println("Before method call: " + person.getName());
        
        reassign(person);
        
        System.out.println("After method call: " + person.getName());
        System.out.println("Expected: Alice (reassignment doesn't affect original)");
    }

    public static void reassign(Person p) {
        System.out.println("Inside method, before reassignment: " + p.getName());
        p = new Person("Charlie");
        System.out.println("Inside method, after reassignment: " + p.getName());
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