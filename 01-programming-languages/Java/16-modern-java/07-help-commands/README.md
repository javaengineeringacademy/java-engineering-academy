# Help Commands

Java provides several command-line tools and help commands for development, debugging, and monitoring.

## Key Commands

### java command
```bash
java --help          # Show java command help
java -version        # Show Java version
java -verbose:class  # Show class loading information
```

### javac command
```bash
javac --help         # Show javac command help
javac -version       # Show compiler version
javac -Xlint         # Enable all warnings
```

### jar command
```bash
jar --help           # Show jar command help
jar tf file.jar      # List jar contents
jar xf file.jar      # Extract jar contents
```

### jshell
```bash
jshell               # Start Java shell (REPL)
jshell --help        # Show jshell help
```

### jcmd
```bash
jcmd                 # List running Java processes
jcmd <pid> help      # Show available commands
jcmd <pid> VM.flags  # Show JVM flags
```

### jinfo
```bash
jinfo <pid>          # Show Java info for process
jinfo -flags <pid>   # Show JVM flags
```

### jmap
```bash
jmap <pid>           # Show memory map
jmap -heap <pid>     # Show heap summary
jmap -dump <pid>     # Dump heap to file
```

### jstack
```bash
jstack <pid>         # Show thread stack trace
jstack -l <pid>      # Show locked threads
```

## IDE-Specific Help

### IntelliJ IDEA
- Help → Find Action (Ctrl+Shift+A)
- Help → Keymap Reference
- Help → Product Documentation

### Eclipse
- Help → Help Contents
- Help → Welcome Screen
- Help → About → Installation Details

### VS Code
- Help → Show All Commands (Ctrl+Shift+P)
- Help → Keyboard Shortcuts Reference
- Help → Documentation
