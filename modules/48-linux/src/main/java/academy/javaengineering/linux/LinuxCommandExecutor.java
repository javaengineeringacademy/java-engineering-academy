package academy.javaengineering.linux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates Linux command execution from Java.
 */
public class LinuxCommandExecutor {

    public record CommandResult(
        int exitCode,
        List<String> output,
        List<String> errors
    ) {}

    public CommandResult executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        
        List<String> output = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.add(line);
            }
        }
        
        int exitCode = process.waitFor();
        
        return new CommandResult(exitCode, output, errors);
    }

    public String getSystemInfo() throws Exception {
        CommandResult result = executeCommand("uname -a");
        return String.join("\n", result.output());
    }

    public String getDiskUsage() throws Exception {
        CommandResult result = executeCommand("df -h");
        return String.join("\n", result.output());
    }

    public String getMemoryInfo() throws Exception {
        CommandResult result = executeCommand("free -m");
        return String.join("\n", result.output());
    }
}
