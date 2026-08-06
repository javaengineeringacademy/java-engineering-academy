// TypeScript Classes - Class, access modifiers, abstract

// Basic class
class Animal {
  name: string;
  
  constructor(name: string) {
    this.name = name;
  }
  
  speak(): string {
    return `${this.name} makes a sound`;
  }
}

let dog = new Animal("Dog");
console.log(dog.speak());

// Access modifiers: public, private, protected
class BankAccount {
  public owner: string;
  private balance: number;
  protected accountType: string;
  
  constructor(owner: string, initialBalance: number) {
    this.owner = owner;
    this.balance = initialBalance;
    this.accountType = "Checking";
  }
  
  public deposit(amount: number): void {
    this.balance += amount;
  }
  
  public getBalance(): number {
    return this.balance;
  }
}

// Parameter properties
class Person {
  constructor(
    public name: string,
    public age: number,
    private ssn: string
  ) {}
  
  getDetails(): string {
    return `${this.name}, Age: ${this.age}`;
  }
}

// Abstract classes
abstract class Shape {
  abstract area(): number;
  abstract perimeter(): number;
  
  describe(): string {
    return `Area: ${this.area()}, Perimeter: ${this.perimeter()}`;
  }
}

class Circle extends Shape {
  constructor(public radius: number) {
    super();
  }
  
  area(): number {
    return Math.PI * this.radius ** 2;
  }
  
  perimeter(): number {
    return 2 * Math.PI * this.radius;
  }
}

class Rectangle extends Shape {
  constructor(public width: number, public height: number) {
    super();
  }
  
  area(): number {
    return this.width * this.height;
  }
  
  perimeter(): number {
    return 2 * (this.width + this.height);
  }
}

// Static members
class MathUtils {
  static readonly PI = 3.14159;
  
  static add(a: number, b: number): number {
    return a + b;
  }
  
  static multiply(a: number, b: number): number {
    return a * b;
  }
}

// Getters and setters
class Temperature {
  private _celsius: number;
  
  constructor(celsius: number) {
    this._celsius = celsius;
  }
  
  get fahrenheit(): number {
    return this._celsius * 9/5 + 32;
  }
  
  set celsius(value: number) {
    this._celsius = value;
  }
}

// Usage examples
let circle = new Circle(5);
console.log(circle.describe());

let rect = new Rectangle(4, 6);
console.log(rect.describe());

console.log(MathUtils.PI);
console.log(MathUtils.add(2, 3));
