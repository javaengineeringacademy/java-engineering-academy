# Smart Pointers Quiz

## Questions

### 1. What is the main difference between `std::unique_ptr` and `std::shared_ptr`?
A) `unique_ptr` is faster  
B) `unique_ptr` allows exclusive ownership, `shared_ptr` allows shared ownership  
C) `shared_ptr` is older  
D) No difference

### 2. What is `std::weak_ptr` used for?
A) Strong ownership  
B) Breaking circular references  
C) Faster access  
D) Thread safety

### 3. Why use `std::make_shared` instead of `std::shared_ptr<T>(new T)`?
A) It's faster  
B) It's exception-safe and more efficient  
C) It's required by the standard  
D) No difference

### 4. Can `std::unique_ptr` be copied?
A) Yes  
B) No, but can be moved  
C) Only with custom deleter  
D) Only in C++14

### 5. What happens when the last `std::shared_ptr` to an object is destroyed?
A) Object is leaked  
B) Object is destroyed  
C) Program crashes  
D) Nothing

## Answers
1. B) `unique_ptr` allows exclusive ownership, `shared_ptr` allows shared ownership
2. B) Breaking circular references
3. B) It's exception-safe and more efficient
4. B) No, but can be moved
5. B) Object is destroyed