public class Employee implements Cloneable {
    private String name;
    private int id;
    private double salary;
    private Address address;
    private transient String password;

    public Employee(String name, int id, double salary, Address address, String password) {
        this.name = name;
        this.id = id;
        this.salary = salary;
        this.address = address;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getSalary() {
        return salary;
    }

    public Address getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Employee shallowClone() {
        try {
            return (Employee) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Employee deepClone() {
        try {
            Employee cloned = (Employee) super.clone();
            cloned.address = this.address.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + "', id=" + id + ", salary=" + salary + ", address=" + address + ", password='" + password + "'}";
    }
}