package academy.javaengineering.git;

import java.util.List;

/**
 * Demonstrates Git operations from Java.
 */
public class GitOperations {

    public record CommitInfo(
        String hash,
        String author,
        String date,
        String message
    ) {}

    public record BranchInfo(
        String name,
        boolean isCurrent,
        String lastCommit
    ) {}

    public List<CommitInfo> getCommitHistory(String repoPath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "log", "--oneline", "-10");
        pb.directory(new java.io.File(repoPath));
        
        Process process = pb.start();
        List<String> output = new String(process.getInputStream().readAllLines()).lines().toList();
        
        return output.stream()
            .map(line -> {
                String[] parts = line.split(" ", 2);
                return new CommitInfo(parts[0], "Author", "Date", parts.length > 1 ? parts[1] : "");
            })
            .toList();
    }

    public String getCurrentBranch(String repoPath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "branch", "--show-current");
        pb.directory(new java.io.File(repoPath));
        
        Process process = pb.start();
        return new String(process.getInputStream().readAllLines()).strip();
    }

    public boolean isClean(String repoPath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "status", "--porcelain");
        pb.directory(new java.io.File(repoPath));
        
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllLines()).strip();
        return output.isEmpty();
    }
}
