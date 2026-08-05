# Generics Quiz

## Quiz 1: Generic Classes

### Question 1
What is the benefit of using generics?
- A) Type safety at compile time
- B) No casting required
- C) Code reusability
- D) All of the above

**Answer: D**

### Question 2
How do you declare a generic class?
- A) class Box<T> { }
- B) class Box<T> { }
- C) class Box<T> { }
- D) class Box<T> { }

**Answer: A**

### Question 3
Can a generic class have multiple type parameters?
- A) Yes
- B) No
- C) Only in Java 8+
- D) Only with interfaces

**Answer: A**

### Question 4
What is type erasure?
- A) Removing generic type information at compile time
- B) Adding generic type information at compile time
- C) Keeping generic type information at runtime
- D) Removing generic type information at runtime

**Answer: A**

### Question 5
What is the raw type?
- A) Generic type without type parameter
- B) Non-generic type
- C) Primitive type
- D) Wrapper type

**Answer: A**

---

## Quiz 2: Generic Methods

### Question 1
How do you declare a generic method?
- A) <T> void print(T element) { }
- B) void print<T>(T element) { }
- C) void print(T element) { }
- D) T void print(T element) { }

**Answer: A**

### Question 2
Can generic methods be static?
- A) Yes
- B) No
- C) Only in Java 8+
- D) Only with classes

**Answer: A**

### Question 3
What is the scope of type parameter in generic method?
- A) Method only
- B) Class only
- C) Both method and class
- D) Neither

**Answer: A**

### Question 4
Can generic methods be overloaded?
- A) Yes
- B) No
- C) Only in Java 8+
- D) Only with generics

**Answer: A**

### Question 5
What is the benefit of generic methods?
- A) Type safety
- B) Code reusability
- C) No casting
- D) All of the above

**Answer: D**

---

## Quiz 3: Bounded Types

### Question 1
What is an upper bound?
- A) T extends Number
- B) T super Number
- C) T implements Number
- D) T imports Number

**Answer: A**

### Question 2
What is a lower bound?
- A) T extends Number
- B) T super Number
- C) T implements Number
- D) T imports Number

**Answer: B**

### Question 3
Can a type parameter have multiple bounds?
- A) Yes
- B) No
- C) Only in Java 8+
- D) Only with classes

**Answer: A**

### Question 4
What is the keyword for multiple bounds?
- A) &
- B) |
- C) ,
- D) ;

**Answer: A**

### Question 5
What is the benefit of bounded types?
- A) Restrict types
- B) Provide methods
- C) Both A and B
- D) Neither

**Answer: C**

---

## Quiz 4: Wildcards

### Question 1
What is an unbounded wildcard?
- A) <?>
- B) <? extends T>
- C) <? super T>
- D) <T>

**Answer: A**

### Question 2
What is an upper bounded wildcard?
- A) <?>
- B) <? extends T>
- C) <? super T>
- D) <T>

**Answer: B**

### Question 3
What is a lower bounded wildcard?
- A) <?>
- B) <? extends T>
- C) <? super T>
- D) <T>

**Answer: C**

### Question 4
What is PECS principle?
- A) Producer Extends, Consumer Super
- B) Producer Super, Consumer Extends
- C) Both A and B
- D) Neither

**Answer: A**

### Question 5
When should you use <? extends T>?
- A) When reading from collection
- B) When writing to collection
- C) Both A and B
- D) Neither

**Answer: A**

---

## Quiz 5: Advanced Topics

### Question 1
What is a generic interface?
- A) Interface with type parameter
- B) Interface without type parameter
- C) Abstract class
- D) Concrete class

**Answer: A**

### Question 2
Can we create generic enum?
- A) Yes
- B) No
- C) Only in Java 8+
- D) Only with classes

**Answer: B**

### Question 3
What is recursive type bound?
- A) <T extends Comparable<T>>
- B) <T extends Object>
- C) <T>
- D) <?>

**Answer: A**

### Question 4
What is the benefit of wildcards?
- A) Flexibility
- B) Type safety
- C) Both A and B
- D) Neither

**Answer: C**

### Question 5
What is the difference between <?> and <? extends Object>?
- A) No difference
- B) <?> is more restrictive
- C) <? extends Object> is more restrictive
- D) Neither is restrictive

**Answer: A**

---

## Score Sheet

| Quiz | Questions | Correct | Score |
|------|-----------|---------|-------|
| Generic Classes | 5 | /5 | % |
| Generic Methods | 5 | /5 | % |
| Bounded Types | 5 | /5 | % |
| Wildcards | 5 | /5 | % |
| Advanced Topics | 5 | /5 | % |
| **Total** | **25** | **/25** | **%** |

---

## Passing Score: 80% (20/25)
