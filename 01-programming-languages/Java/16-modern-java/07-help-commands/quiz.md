# Help Commands Quiz

## Questions

1. What command shows the Java version?
2. How do you start the Java shell (REPL)?
3. What command shows thread stack traces?
4. How do you list running Java processes?
5. What command shows JVM flags?
6. How do you create a jar file?
7. What command dumps the heap?
8. How do you show class loading information?
9. What command shows memory map?
10. How do you start a flight recording?

## Answers

1. **`java -version`**
2. **`jshell`**
3. **`jstack <pid>`**
4. **`jcmd`** or **`jps`**
5. **`jinfo -flags <pid>`** or **`jcmd <pid> VM.flags`**
6. **`jar cf app.jar .`**
7. **`jmap -dump:format=b,file=heap.hprof <pid>`**
8. **`java -verbose:class`**
9. **`jmap <pid>`**
10. **`jcmd <pid> JFR.start`**
