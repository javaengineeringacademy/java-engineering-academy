// C# Inheritance

using System;

namespace InheritanceDemo
{
    // Base class
    public class Animal
    {
        public string Name { get; set; }
        public int Age { get; set; }
        
        public Animal(string name, int age)
        {
            Name = name;
            Age = age;
        }
        
        public virtual string Speak()
        {
            return $"{Name} makes a sound";
        }
        
        public override string ToString()
        {
            return $"Animal: {Name}, Age: {Age}";
        }
    }
    
    // Derived class
    public class Dog : Animal
    {
        public string Breed { get; set; }
        
        public Dog(string name, int age, string breed) 
            : base(name, age)
        {
            Breed = breed;
        }
        
        public override string Speak()
        {
            return $"{Name} barks";
        }
        
        public void Fetch(string item)
        {
            Console.WriteLine($"{Name} fetches the {item}");
        }
    }
    
    // Another derived class
    public class Cat : Animal
    {
        public bool IsIndoor { get; set; }
        
        public Cat(string name, int age, bool isIndoor) 
            : base(name, age)
        {
            IsIndoor = isIndoor;
        }
        
        public override string Speak()
        {
            return $"{Name} meows";
        }
        
        public void Purr()
        {
            Console.WriteLine($"{Name} purrs...");
        }
    }
    
    // Multi-level inheritance
    public class Puppy : Dog
    {
        public bool IsTrained { get; set; }
        
        public Puppy(string name, int age, string breed, bool isTrained) 
            : base(name, age, breed)
        {
            IsTrained = isTrained;
        }
        
        public override string Speak()
        {
            return $"{Name} yips";
        }
    }
    
    // Abstract base class
    public abstract class Vehicle
    {
        public string Make { get; set; }
        public string Model { get; set; }
        public int Year { get; set; }
        
        protected Vehicle(string make, string model, int year)
        {
            Make = make;
            Model = model;
            Year = year;
        }
        
        public abstract double CalculateFuelEfficiency();
        
        public virtual string GetDescription()
        {
            return $"{Year} {Make} {Model}";
        }
    }
    
    public class Car : Vehicle
    {
        public int NumDoors { get; set; }
        
        public Car(string make, string model, int year, int numDoors) 
            : base(make, model, year)
        {
            NumDoors = numDoors;
        }
        
        public override double CalculateFuelEfficiency()
        {
            return 30.5; // MPG
        }
        
        public override string GetDescription()
        {
            return $"{base.GetDescription()} with {NumDoors} doors";
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            // Create objects
            Dog dog = new Dog("Rex", 5, "German Shepherd");
            Cat cat = new Cat("Whiskers", 3, true);
            Puppy puppy = new Puppy("Buddy", 1, "Labrador", false);
            
            // Method calls
            Console.WriteLine(dog.Speak());
            Console.WriteLine(cat.Speak());
            Console.WriteLine(puppy.Speak());
            
            // Polymorphism
            Animal[] animals = { dog, cat, puppy };
            foreach (Animal animal in animals)
            {
                Console.WriteLine(animal.Speak());
            }
            
            // Type checking
            if (dog is Dog d)
            {
                d.Fetch("ball");
            }
            
            // Base class reference
            Animal animalRef = dog;
            Console.WriteLine(animalRef.Speak());
            
            // Virtual method
            Console.WriteLine(dog.ToString());
            
            // Vehicle example
            Car car = new Car("Toyota", "Camry", 2023, 4);
            Console.WriteLine(car.GetDescription());
            Console.WriteLine($"Fuel efficiency: {car.CalculateFuelEfficiency():F1} MPG");
            
            Console.WriteLine("Inheritance example running");
        }
    }
}