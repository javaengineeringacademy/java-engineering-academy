// TypeScript Type Guards - typeof, instanceof, custom guards

// typeof type guards
function processValue(value: string | number): string {
  if (typeof value === "string") {
    return value.toUpperCase();
  } else {
    return value.toFixed(2);
  }
}

// instanceof type guards
class Dog {
  bark(): string {
    return "Woof!";
  }
}

class Cat {
  meow(): string {
    return "Meow!";
  }
}

function makeSound(animal: Dog | Cat): string {
  if (animal instanceof Dog) {
    return animal.bark();
  } else {
    return animal.meow();
  }
}

// Custom type guard (type predicate)
interface Fish {
  swim: () => string;
}

interface Bird {
  fly: () => string;
}

function isFish(pet: Fish | Bird): pet is Fish {
  return (pet as Fish).swim !== undefined;
}

function move(pet: Fish | Bird): string {
  if (isFish(pet)) {
    return pet.swim();
  } else {
    return pet.fly();
  }
}

// Truthiness type guards
function processString(value: string | null): string {
  if (value) {
    return value.toUpperCase();
  }
  return "default";
}

// Equality type guards
function compare(a: string | number, b: string | boolean): string {
  if (a === b) {
    return "Equal";
  }
  return "Not equal";
}

// Discriminated unions
interface Circle {
  kind: "circle";
  radius: number;
}

interface Square {
  kind: "square";
  sideLength: number;
}

interface Triangle {
  kind: "triangle";
  base: number;
  height: number;
}

type Shape = Circle | Square | Triangle;

function getArea(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return Math.PI * shape.radius ** 2;
    case "square":
      return shape.sideLength ** 2;
    case "triangle":
      return (shape.base * shape.height) / 2;
  }
}

// Exhaustiveness checking
function getPerimeter(shape: Shape): number {
  switch (shape.kind) {
    case "circle":
      return 2 * Math.PI * shape.radius;
    case "square":
      return 4 * shape.sideLength;
    case "triangle":
      return shape.base + shape.height + Math.sqrt(shape.base ** 2 + shape.height ** 2);
    default:
      const _exhaustive: never = shape;
      return _exhaustive;
  }
}

// Usage examples
console.log(processValue("hello"));
console.log(processValue(42));
console.log(makeSound(new Dog()));
console.log(makeSound(new Cat()));

let fish: Fish = { swim: () => "Swimming" };
let bird: Bird = { fly: () => "Flying" };
console.log(move(fish));
console.log(move(bird));

console.log(getArea({ kind: "circle", radius: 5 }));
console.log(getArea({ kind: "square", sideLength: 4 }));
