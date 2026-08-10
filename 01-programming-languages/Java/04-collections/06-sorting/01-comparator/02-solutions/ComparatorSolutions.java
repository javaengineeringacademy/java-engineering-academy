package academy.javaengineering.collections.sorting.comparator.solutions;

import java.util.*;

public class ComparatorSolutions {
    public static List<String> sortByLengthThenAlpha(List<String> strings) {
        List<String> sorted = new ArrayList<>(strings);
        sorted.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        return sorted;
    }
    
    public static List<Employee> sortByDepartmentThenSalary(List<Employee> employees) {
        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort(Comparator.comparing(Employee::getDepartment)
                .thenComparing(Employee::getSalary, Comparator.reverseOrder()));
        return sorted;
    }
    
    public static Comparator<Person> createPersonComparator() {
        return Comparator.comparing(Person::getLastName)
                .thenComparing(Person::getFirstName)
                .thenComparingInt(Person::getAge);
    }
    
    public static List<City> sortByDistance(List<City> cities, double targetLat, double targetLon) {
        List<City> sorted = new ArrayList<>(cities);
        sorted.sort(Comparator.comparingDouble(city -> 
            Math.sqrt(Math.pow(city.getLatitude() - targetLat, 2) + 
                     Math.pow(city.getLongitude() - targetLon, 2))));
        return sorted;
    }
    
    public static List<Product> sortByPriceThenName(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparingDouble(Product::getPrice)
                .thenComparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
        return sorted;
    }
    
    static class Employee {
        String name;
        String department;
        double salary;
        Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
        public String getDepartment() { return department; }
        public double getSalary() { return salary; }
    }
    
    static class Person {
        String firstName;
        String lastName;
        int age;
        Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public int getAge() { return age; }
    }
    
    static class City {
        String name;
        double latitude;
        double longitude;
        City(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }
    
    static class Product {
        String name;
        double price;
        Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
        public String getName() { return name; }
        public double getPrice() { return price; }
    }
}
