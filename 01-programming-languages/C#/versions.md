# C# Version History

## C# 1.0
- **Release Date:** January 2002
- **Features:** Classes, structs, interfaces, events, properties, delegates, namespaces, auto memory management (GC), strong typing, versioning
- **Deprecated:** N/A (initial release)
- **Removed:** N/A
- **Performance:** Managed runtime with garbage collection
- **Security:** Type safety, managed code prevents buffer overflows
- **Why Introduced:** Microsoft created C# as a modern, object-oriented language for .NET platform development

## C# 1.1
- **Release Date:** April 2003
- **Features:** ADO.NET data access, XML documentation comments, pound directives
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** ADO.NET improvements
- **Security:** XML documentation for API documentation
- **Why Introduced:** Added ADO.NET for database connectivity and documentation improvements

## C# 1.2
- **Release Date:** October 2003
- **Features:** foreach loop for arrays, IDeserializable, buffered stream improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Buffered stream improvements
- **Security:** Improved serialization
- **Why Introduced:** foreach loop for cleaner iteration, serialization improvements

## C# 2.0
- **Release Date:** November 2005
- **Features:** Generics, nullable types, anonymous methods, iterators (yield), covariant/contravariant delegates, partial classes, static classes, property accessibility, #nullable
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Generics provide type-safe performance without boxing/unboxing
- **Security:** Nullable types prevent null reference exceptions
- **Why Introduced:** Major language enhancement adding generics for type safety and performance

## C# 3.0
- **Release Date:** November 2007
- **Features:** LINQ, lambda expressions, extension methods, auto-implemented properties, object/collection initializers, anonymous types, implicit typed variables (var), query expressions
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** LINQ provides optimized query execution
- **Security:** Strongly-typed LINQ queries prevent SQL injection
- **Why Introduced:** LINQ for data querying, lambda expressions for functional programming

## C# 4.0
- **Release Date:** April 2010
- **Features:** Dynamic binding (dynamic keyword), named/optional parameters, COM interop improvements, covariance/contravariance for generics, embedded interop types
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Embedded interop types reduce COM overhead
- **Security:** Dynamic binding with runtime type checking
- **Why Introduced:** Dynamic binding for COM interop, optional parameters for API design

## C# 5.0
- **Release Date:** August 2012
- **Features:** async/await, caller information attributes, string.Format improvements, iterator support with Task
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** async/await provides efficient asynchronous operations without thread blocking
- **Security:** Caller information for better diagnostics
- **Why Introduced:** async/await for simplified asynchronous programming

## C# 6.0
- **Release Date:** July 2015
- **Features:** Null-conditional operators (?.), string interpolation, expression-bodied members, null-coalescing assignment (??=), auto-property initializers, using static, nameof operator, index initializers
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Null-conditional reduces null checking code
- **Security:** Null-conditional prevents null reference exceptions
- **Why Introduced:** Syntax improvements for concise, safe code

## C# 7.0
- **Release Date:** March 2017
- **Features:** Pattern matching, tuples, deconstruction, local functions, out variables, inline out variables, expression-bodied constructors/finalizers, digit separators, binary literals
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Pattern matching improves branch prediction
- **Security:** Tuples enable safer multi-value returns
- **Why Introduced:** Pattern matching and tuples for cleaner code

## C# 7.1
- **Release Date:** August 2017
- **Features:** Async main, default literal expressions, inferred tuple element names, Pattern matching with generics
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Async main for cleaner program entry
- **Security:** Default literal for safer default values
- **Why Introduced:** Async main and default literal for convenience

## C# 7.2
- **Release Date:** November 2017
- **Features:** In parameters, ref readonly, private protected, Span<T>, stackalloc in nested expressions, non-trailing named arguments
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Span<T> for high-performance stack-based memory access
- **Security:** ref readonly for immutable references
- **Why Introduced:** Span<T> for performance, access modifiers for encapsulation

## C# 7.3
- **Release Date:** May 2018
- **Features:** Tuple equality, stackalloc in initializers, unmanaged generic constraints, improved overload resolution, ref local reassignment
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Tuple equality for faster comparisons
- **Security:** Unmanaged constraints for safe native interop
- **Why Introduced:** Tuple improvements and generic constraints

## C# 8.0
- **Release Date:** September 2019
- **Features:** Nullable reference types, switch expressions, using declarations, default interface methods, async streams, indices and ranges, null-coalescing assignment, pattern matching enhancements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Nullable reference types prevent null-related bugs
- **Security:** Nullable reference types for safer null handling
- **Why Introduced:** Nullable reference types for null safety, switch expressions for pattern matching

## C# 9.0
- **Release Date:** November 2020
- **Features:** Records, init-only setters, top-level statements, pattern matching enhancements, target-typed new, module initializers, partial properties, static anonymous functions, function pointers
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Records provide efficient immutable data types
- **Security:** Init-only setters for immutable object initialization
- **Why Introduced:** Records for immutable data, top-level statements for simplicity

## C# 10.0
- **Release Date:** November 2021
- **Features:** Global usings, file-scoped namespaces, record structs, const string interpolation, CallerArgumentExpression, extended property patterns, async Main improvements
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Global usings reduce compilation time
- **Security:** CallerArgumentExpression for better error reporting
- **Why Introduced:** Syntax simplifications and performance improvements

## C# 11.0
- **Release Date:** November 2022
- **Features:** Raw string literals, list patterns, required members, generic math support, unsigned right shift, UTF-8 string literals, newlines in string interpolations, improved overload resolution
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Raw string literals for cleaner code
- **Security:** Required members for safer object initialization
- **Why Introduced:** Raw strings, list patterns, and generic math for modern development

## C# 12.0
- **Release Date:** November 2023
- **Features:** Primary constructors, collection expressions, inline arrays, default lambda parameters, alias any type, time abstraction, experimental attribute
- **Deprecated:** N/A
- **Removed:** N/A
- **Performance:** Collection expressions for optimized collection creation
- **Security:** Primary constructors for cleaner class initialization
- **Why Introduced:** Primary constructors for reduced boilerplate, collection expressions for simpler collections
