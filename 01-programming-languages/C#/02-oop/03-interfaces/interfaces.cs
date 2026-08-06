// C# Interfaces

using System;

namespace InterfacesDemo
{
    // Interface
    public interface IMovable
    {
        int X { get; set; }
        int Y { get; set; }
        void Move(int x, int y);
    }
    
    // Interface with default implementation (C# 8.0+)
    public interface ILogger
    {
        void Log(string message);
        
        // Default implementation
        void LogError(string error)
        {
            Log($"ERROR: {error}");
        }
    }
    
    // Multiple interfaces
    public interface IDrawable
    {
        void Draw();
    }
    
    public interface IResizable
    {
        void Resize(double factor);
    }
    
    // Class implementing multiple interfaces
    public class Circle : IDrawable, IResizable
    {
        public double Radius { get; set; }
        
        public Circle(double radius)
        {
            Radius = radius;
        }
        
        public void Draw()
        {
            Console.WriteLine($"Drawing circle with radius {Radius}");
        }
        
        public void Resize(double factor)
        {
            Radius *= factor;
        }
    }
    
    // Interface inheritance
    public interface IShape
    {
        double Area();
    }
    
    public interface IColorShape : IShape
    {
        string Color { get; set; }
    }
    
    public class ColoredRectangle : IColorShape
    {
        public double Width { get; set; }
        public double Height { get; set; }
        public string Color { get; set; }
        
        public ColoredRectangle(double width, double height, string color)
        {
            Width = width;
            Height = height;
            Color = color;
        }
        
        public double Area()
        {
            return Width * Height;
        }
    }
    
    // Explicit interface implementation
    public interface IFlyable
    {
        void Fly();
    }
    
    public interface ISwimmable
    {
        void Swim();
    }
    
    public class Duck : IFlyable, ISwimmable
    {
        // Explicit implementation
        void IFlyable.Fly()
        {
            Console.WriteLine("Duck flies");
        }
        
        void ISwimmable.Swim()
        {
            Console.WriteLine("Duck swims");
        }
        
        // Regular method
        public void Quack()
        {
            Console.WriteLine("Duck quacks");
        }
    }
    
    // Interface as parameter type
    public static class ShapeHelper
    {
        public static void DrawShape(IDrawable shape)
        {
            shape.Draw();
        }
        
        public static void ResizeShape(IResizable shape, double factor)
        {
            shape.Resize(factor);
        }
    }
    
    class Program
    {
        static void Main(string[] args)
        {
            // Interface implementation
            Circle circle = new Circle(5);
            circle.Draw();
            circle.Resize(2);
            
            // Interface as type
            IDrawable drawable = circle;
            drawable.Draw();
            
            // Multiple interfaces
            Duck duck = new Duck();
            ((IFlyable)duck).Fly();
            ((ISwimmable)duck).Swim();
            duck.Quack();
            
            // Interface inheritance
            ColoredRectangle rect = new ColoredRectangle(4, 6, "Red");
            Console.WriteLine($"Area: {rect.Area()}");
            Console.WriteLine($"Color: {rect.Color}");
            
            // Interface as parameter
            ShapeHelper.DrawShape(circle);
            ShapeHelper.ResizeShape(circle, 0.5);
            
            // Default interface method (C# 8.0+)
            ILogger logger = new ConsoleLogger();
            logger.Log("Hello");
            logger.LogError("Something went wrong");
            
            Console.WriteLine("Interfaces example running");
        }
    }
    
    // Helper class implementing ILogger
    public class ConsoleLogger : ILogger
    {
        public void Log(string message)
        {
            Console.WriteLine($"[LOG] {message}");
        }
    }
}