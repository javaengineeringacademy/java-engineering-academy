// C# Classes

using System;

namespace ClassesDemo
{
    // Basic class
    public class Person
    {
        // Properties
        public string Name { get; set; }
        public int Age { get; set; }
        
        // Constructor
        public Person(string name, int age)
        {
            Name = name;
            Age = age;
        }
        
        // Method
        public string GetDetails()
        {
            return $"{Name}, Age: {Age}";
        }
        
        // Override ToString
        public override string ToString()
        {
            return $"Person: {Name}";
        }
    }
    
    // Class with encapsulation
    public class BankAccount
    {
        // Private field
        private decimal balance;
        
        // Public property with validation
        public decimal Balance
        {
            get { return balance; }
        }
        
        // Constructor
        public BankAccount(decimal initialBalance)
        {
            if (initialBalance < 0)
                throw new ArgumentException("Initial balance cannot be negative");
            balance = initialBalance;
        }
        
        // Methods
        public void Deposit(decimal amount)
        {
            if (amount <= 0)
                throw new ArgumentException("Deposit amount must be positive");
            balance += amount;
        }
        
        public void Withdraw(decimal amount)
        {
            if (amount <= 0)
                throw new ArgumentException("Withdrawal amount must be positive");
            if (amount > balance)
                throw new InvalidOperationException("Insufficient funds");
            balance -= amount;
        }
    }
    
    // Static class
    public static class MathHelper
    {
        public static double PI = 3.14159;
        
        public static double Add(double a, double b)
        {
            return a + b;
        }
        
        public static double Multiply(double a, double b)
        {
            return a * b;
        }
    }
    
    // Sealed class
    public sealed class Logger
    {
        private static Logger instance;
        
        private Logger() { }
        
        public static Logger Instance
        {
            get
            {
                if (instance == null)
                    instance = new Logger();
                return instance;
            }
        }
        
        public void Log(string message)
        {
            Console.WriteLine($"[LOG] {message}");
        }
    }
    
    // Abstract class
    public abstract class Shape
    {
        public string Name { get; set; }
        
        protected Shape(string name)
        {
            Name = name;
        }
        
        public abstract double Area();
        public abstract double Perimeter();
        
        public string Describe()
        {
            return $"{Name}: Area = {Area():F2}, Perimeter = {Perimeter():F2}";
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            // Basic class
            Person person = new Person("Alice", 30);
            Console.WriteLine(person.GetDetails());
            Console.WriteLine(person.ToString());
            
            // Encapsulation
            BankAccount account = new BankAccount(1000);
            account.Deposit(500);
            Console.WriteLine($"Balance: {account.Balance}");
            
            // Static class
            Console.WriteLine($"PI: {MathHelper.PI}");
            Console.WriteLine($"Add: {MathHelper.Add(2, 3)}");
            
            // Sealed class (singleton)
            Logger logger = Logger.Instance;
            logger.Log("Application started");
            
            Console.WriteLine("Classes example running");
        }
    }
}