# Senior Level Exercises

## Exercise 1: Design a Library
Design a library interface with opaque pointers.

```c
// mylib.h
#ifndef MYLIB_H
#define MYLIB_H

typedef struct MyLib MyLib;

MyLib *mylib_create(void);
void mylib_destroy(MyLib *lib);
int mylib_process(MyLib *lib, const char *input);
const char *mylib_get_result(const MyLib *lib);

#endif
```

## Exercise 2: Cross-platform Code
Write code that works on Windows, Linux, and macOS.

## Exercise 3: Performance Analysis
Profile and optimize a performance-critical module.

## Exercise 4: Code Review Guidelines
Create code review guidelines for a team.

## Exercise 5: Architecture Decision
Document an architecture decision for a complex feature.
