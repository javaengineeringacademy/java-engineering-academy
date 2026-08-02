package academy.javaengineering.git;

import java.util.List;

/**
 * Demonstrates Git diff operations.
 */
public class GitDiff {

    public record DiffEntry(
        String file,
        String status,
        int additions,
        int deletions
    ) {}

    public List<DiffEntry> getDiff(String repoPath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "diff", "--stat");
        pb.directory(new java.io.File(repoPath));
        
        Process process = pb.start();
        String output = new String(process.getInputStream().readAllLines());
        
        return output.lines()
            .filter(line -> line.contains("|"))
            .map(line -> {
                String[] parts = line.split("\\|");
                return new DiffEntry(parts[0].trim(), "modified", 0, 0);
            })
            .toList();
    }

    public String getDiffContent(String repoPath, String file) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "diff", file);
        pb.directory(new java.io.File(repoPath));
        
        Process process = pb.start();
        return new String(process.getInputStream().readAllLines());
    }
}
