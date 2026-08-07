# File I/O Quiz

1. What are the file opening modes (r, w, a, rb, etc.)?
2. What is the difference between text and binary mode?
3. Why should you always check if fopen returns NULL?
4. What does fclose do besides closing the file?
5. What is the difference between fgets and gets?
6. How do you read a binary file?
7. What is fseek used for?
8. What happens if you write to a file opened in "r" mode?
9. What is the advantage of buffered I/O?
10. How do you handle file errors?

## Answers

1. r: read, w: write (truncate), a: append, rb: binary read, wb: binary write, ab: binary append
2. Text mode may translate newlines; binary preserves exact bytes
3. To avoid NULL pointer dereference
4. Flushes buffered data to disk
5. fgets reads until newline or limit; gets is unsafe (no bounds)
6. Using fread with binary mode
7. To move file position for random access
8. Undefined behavior (runtime error)
9. Reduces system calls by buffering data
10. Check return values and use perror/strerror
