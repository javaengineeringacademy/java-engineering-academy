# File I/O Quiz

## Questions

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
11. What is the difference between `fread` and `fgetc`?
12. What does `ftell` return and when is it useful?
13. What is the difference between `remove` and `rename` for files?
14. What is `tmpfile` used for and what happens when it is closed?
15. How do you ensure file I/O operations are thread-safe?

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
11. `fread` reads a block of data at once; `fgetc` reads one character at a time
12. Returns current file position; useful before/after fseek to track position
13. `remove` deletes a file; `rename` changes a file's name
14. Creates a temporary file automatically deleted when closed or program exits
15. Use file locking mechanisms (e.g., `flock` on POSIX) or mutex around shared FILE operations
