package academy.javaengineering.git;

/**
 * Demonstrates Git branch management.
 */
public class GitBranchManager {

    public void createBranch(String repoPath, String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "branch", branchName);
        pb.directory(new java.io.File(repoPath));
        pb.start().waitFor();
    }

    public void checkoutBranch(String repoPath, String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "checkout", branchName);
        pb.directory(new java.io.File(repoPath));
        pb.start().waitFor();
    }

    public void deleteBranch(String repoPath, String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "branch", "-d", branchName);
        pb.directory(new java.io.File(repoPath));
        pb.start().waitFor();
    }

    public void mergeBranch(String repoPath, String branchName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("git", "merge", branchName);
        pb.directory(new java.io.File(repoPath));
        pb.start().waitFor();
    }
}
