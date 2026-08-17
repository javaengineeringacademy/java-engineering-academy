package academy.javaengineering.collections.sorting.comparator.exercises;

import java.util.*;

public class ComparatorExercises {
    // TODO: Implement a method that sorts a list of strings by length, then alphabetically
    public static List<String> sortByLengthThenAlpha(List<String> strings) { return null; }
    
    // TODO: Implement a method that sorts a list of Employee objects by department, then by salary descending
    public static List<Employee> sortByDepartmentThenSalary(List<Employee> employees) { return null; }
    
    // TODO: Implement a method that creates a custom Comparator for comparing Person objects by multiple fields
    public static Comparator<Person> createPersonComparator() { return null; }
    
    // TODO: Implement a method that sorts a list of cities by distance from a given point
    public static List<City> sortByDistance(List<City> cities, double targetLat, double targetLon) { return null; }
    
    // TODO: Implement a method that sorts a list of Product objects by price, ignoring case for names
    public static List<Product> sortByPriceThenName(List<Product> products) { return null; }
    
    // Helper classes for exercises
    static class Employee {
        String name;
        String department;
        double salary;
        Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
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
    }
    
    static class Product {
        String name;
        double price;
        Product(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}
